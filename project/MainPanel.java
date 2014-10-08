package project;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalSliderUI;

import project.MediaProgressChecker;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
//import a03.MediaProgressChecker;
import project.PlaybackPanel;
import project.VolumePanel;
import net.miginfocom.swing.MigLayout;

public class MainPanel extends JPanel {

	String iconPath = "./icons";
	//all GUI/video objects set private for use within class
	private PlaybackPanel pbP;
	private VolumePanel vP;
	
	private JSlider mediaProgress;
	private JLabel videoTime;
	//private JTextField videoName;
	private File mediaFile;
	private EmbeddedMediaPlayer currentVideo;
	private EmbeddedMediaPlayerComponent videoPlayer;
	private MediaProgressChecker mPC;
	private JPanel panel;
	
	/**
	 * Create the panel.
	 */
	public MainPanel(EmbeddedMediaPlayerComponent vidPlayer) {
		
		setLayout(new MigLayout("", "[82.00][130.00,grow][351.00,grow][grow]", "[417.00,grow][18.00][]"));
		setBackground(Color.GRAY);
		
		//video player set from the input of the constructor
		videoPlayer = vidPlayer;
		//panel = new JPanel();
		//panel.add(videoPlayer);
		add(videoPlayer, "cell 0 0 4 1,grow");
		//---------------------MEDIA PLAYER SETUP----------------------------//
		
		
		//setup of media progress slider to keep track of media
		mediaProgress = new JSlider();
		mediaProgress.setValue(0);
		mediaProgress.setBackground(Color.BLACK);
		//modify default UI to remove progress knob of slider
		mediaProgress.setUI(new MetalSliderUI() {
			@Override
			public void paintThumb(Graphics g) {
				//do nothing to get rid of the knob
			}
		});
		//mouse listener for entering and leaving for cursor change, also clicking on point
		//of progress to jump to part
		mediaProgress.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				//when it's enabled, when mouse enters, hand cursor is used
				if (mediaProgress.isEnabled()) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));		
				}
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				//when exits, default cursor is used
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));		
			}
			@Override
			public void mousePressed(MouseEvent arg0) {	
				if (mediaProgress.isEnabled()) {
					currentVideo.pause();
					
				}
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {	
				if (mediaProgress.isEnabled()) {
					try {
						//get mouse point
						double point = mediaProgress.getMousePosition().getX();
						//get ratio of slider value per pixel
						double ratio = mediaProgress.getMaximum() / (mediaProgress.getWidth()-8);
						//get the value to be used for value/ time
						double result = ratio * point;
						//set slider value, play video in time
						mediaProgress.setValue((int)result);
						currentVideo.setTime((long)result);
					} catch (NullPointerException e) {
						//do nothing
					}
					currentVideo.play();
					pbP.setToPause();
				}
			}			
		});
		

		mediaProgress.setEnabled(false);
		add(mediaProgress, "cell 1 1 3 1 ,growx");
		
		
		videoTime = new JLabel("  00:00:00");
		videoTime.setOpaque(true);
		videoTime.setForeground(Color.WHITE);
		videoTime.setBackground(Color.BLACK);
		add(videoTime, "cell 0 1,growx");
		
		//edit the slider for video progress
		mediaProgress.setUI(new MetalSliderUI() {
			@Override
			public void paintThumb(Graphics g) {
				//do nothing to get rid of the knob
			}
		});
		
		//-------------------- MEDIA BUTTON SETUP---------------------------------//
		
		pbP = new PlaybackPanel();
		add(pbP, "cell 2 2,growx,aligny center");
		
		//---------------------------MEDIA VOLUME CONTROL--------------------------//
		
		vP = new VolumePanel();
		add(vP, "cell 3 2,growx,aligny center");
		
	}
	
	//-------------------------METHODS FOR FUNCTION ENABLITLIY------------------//

	//method to enable all media buttons
	public void setMediaButtonOn() {
		pbP.videoOn();
		vP.volumeOn();
		mediaProgress.setEnabled(true);
	}
	//method to enable only audio buttons
	public void setAudioButtonOn() {
		pbP.audioOn();
		vP.volumeOn();
		mediaProgress.setEnabled(true);
	}
	
	//-----------------------METHODS TO SET UP COMPONENTS FROM OTHER CLASSES------------------//
	
	//method to set the current video (media)
	public void setCurrentVid(EmbeddedMediaPlayer vid, File vidFile, JTextField field){
		currentVideo = vid;
		mediaFile = vidFile;
		pbP.setCurrentVideo(currentVideo);
		vP.setCurrentVideo(currentVideo);
		//media name is shown on text field to show what's playing
		field.setText(vidFile.getName());
		//set up of progress slider with media time length
		mediaProgress.setMinimum(0);
		mediaProgress.setMaximum((int)currentVideo.getLength());
		//when a valid current video is set, begin media progress checking
		mPC = new MediaProgressChecker(this);
		mPC.execute();
	}
	//same as above but without passing in textfield. this method is for restart()
	public void setCurrentVid(EmbeddedMediaPlayer vid, File vidFile){
		currentVideo = vid;
		mediaFile = vidFile;
		pbP.setCurrentVideo(currentVideo);
		vP.setCurrentVideo(currentVideo);
		//set up of progress slider with media time length
		mediaProgress.setMinimum(0);
		mediaProgress.setMaximum((int)currentVideo.getLength());
		//when a valid current video is set, begin media progress checking
		mPC = new MediaProgressChecker(this);
		mPC.execute();
	}
	//an additional button to easily open files
	public void setOpenButton(JButton openButton) {
		ImageIcon openFile = new ImageIcon(iconPath + "/open_button.gif");
		openButton.setIcon(openFile);
		openButton.setOpaque(false);
		openButton.setContentAreaFilled(false);
		openButton.setBorderPainted(false);
		openButton.setFocusPainted(false);
		add(openButton);
	}
	//an additional button to easily download files
	public void setButton(JButton button) {
		add(button);
	}
	
	//----------------------METHODS TO ACCESS PRIVATE OBJECTS-----------//
	
	//method to retrieve the current
	public EmbeddedMediaPlayer getMedia() {
		return currentVideo;
	}
	
	//--------------------METHOD TO UPDATE GUI FROM OTHER CLASSES-----------------//
	
	//method to literally restart video that was playing, when it ends
	public void restart() {
		currentVideo.startMedia(mediaFile.getAbsolutePath());
		currentVideo.pause();
		setCurrentVid(currentVideo, mediaFile);
		pbP.setToDefault();
	}
	
	//method used by video progress checker to update progress bar of video
	public void updateMediaProgress() {
		mediaProgress.setValue((int)currentVideo.getTime());
		String hs,ms,ss;
		long time = currentVideo.getTime()/1000;
		int hours = (int) time / 3600;
	    int remainder = (int) time - hours * 3600;
	    int mins = remainder / 60;
	    remainder = remainder - mins * 60;
	    int secs = remainder;
	    if (hours < 10) {
	    	hs = "0" + hours;
	    } else {
	    	hs = "" + hours;
	    }
	    if (mins < 10) {
	    	ms = "0" + mins;
	    } else {
	    	ms = "" + mins;
	    }
	    if (secs < 10) {
	    	ss = "0" + secs;
	    } else {
	    	ss = "" + secs;
	    }
	    videoTime.setText("   " + hs + ":" + ms + ":" + ss);
	}

}
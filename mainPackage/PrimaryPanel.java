package mainPackage;

import helperClasses.MediaProgressChecker;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.plaf.metal.MetalSliderUI;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
//import a03.MediaProgressChecker;
import playback.PlaybackPanel;
import playback.VolumePanel;
import net.miginfocom.swing.MigLayout;

/**
 * This panel is the main panel which holds the embedded media player and its component.
 * Along with other components related to playback.
 * @author namjun 
 *
 */
public class PrimaryPanel extends JPanel {

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
	private JPanel vPanel;
	
	/**
	 * Create the panel.
	 */
	public PrimaryPanel(EmbeddedMediaPlayerComponent vidPlayer) {
		
		setLayout(new MigLayout("", "[82.00][130.00,grow][40.00%][30.00%]", "[417.00,grow][18.00][]"));
		setBackground(Color.GRAY);
		
		//video player set from the input of the constructor
		videoPlayer = vidPlayer;
		
		vPanel = new JPanel();
		vPanel.add(videoPlayer);
		vPanel.setBackground(Color.black);
		vPanel.setLayout(null);
		
		vPanel.addComponentListener(new ComponentAdapter(){
			
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel panel = (JPanel) e.getSource();
				panel.getComponent(0).setSize(panel.getWidth(), panel.getHeight());
			}
			
		});
		
		add(vPanel, "cell 0 0 4 1,grow");
		//---------------------MEDIA PLAYER SETUP----------------------------//
		
		
		//setup of media progress slider to keep track of media
		mediaProgress = new JSlider();
		mediaProgress.setValue(0);
		mediaProgress.setBackground(Color.GRAY);
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
		videoTime.setBackground(Color.GRAY);
		add(videoTime, "cell 0 1,growx");
		
		//edit the slider for video progress
		mediaProgress.setUI(new MetalSliderUI() {
			@Override
			public void paintThumb(Graphics g) {
				//do nothing to get rid of the knob
			}
		});
		
		//-------------------- MEDIA BUTTON SETUP---------------------------------//
		
		pbP = PlaybackPanel.getInstance();
		add(pbP, "cell 2 2,growx,aligny center");
		
		//---------------------------MEDIA VOLUME CONTROL--------------------------//
		
		vP = VolumePanel.getInstance();
		add(vP, "cell 3 2,growx,aligny center");
		
	}
	
	//-------------------------METHODS FOR FUNCTION ENABLITLIY------------------//

	/**
	 * enables all media buttons
	 */
	public void setMediaButtonOn() {
		pbP.videoOn();
		vP.volumeOn();
		mediaProgress.setEnabled(true);
	}
	
	/**
	 * enables audio buttons
	 */
	public void setAudioButtonOn() {
		pbP.audioOn();
		vP.volumeOn();
		mediaProgress.setEnabled(true);
	}
	
	//-----------------------METHODS TO SET UP COMPONENTS FROM OTHER CLASSES------------------//
	
	/**
	 * sets the current video
	 * @param vid
	 * @param vidFile
	 * @param field
	 */
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
	
	/**
	 * sets the current video
	 * @param vid
	 * @param vidFile
	 */
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
	
	//----------------------METHODS TO ACCESS PRIVATE OBJECTS-----------//
	
	/**
	 * retrieves current embedded media player
	 * @return
	 */
	public EmbeddedMediaPlayer getMedia() {
		return currentVideo;
	}
	
	//--------------------METHOD TO UPDATE GUI FROM OTHER CLASSES-----------------//
	
	/**
	 * method to literally restart video that was playing, when it ends
	 */
	public void restart() {
		currentVideo.startMedia(mediaFile.getAbsolutePath());
		currentVideo.pause();
		setCurrentVid(currentVideo, mediaFile);
		pbP.setToDefault();
	}

	/**
	 * method used by video progress checker to update progress bar of video
	 */
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

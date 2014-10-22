package playback;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import playback.FastBackwarder;
import playback.FastForwarder;

public class PlaybackPanel extends JPanel implements MouseListener {
	
	private static PlaybackPanel _playbackPanel = new PlaybackPanel();
	
	//all media play back buttons
	private JButton playButton, ffButton, fbButton;
	//current media for play back
	private EmbeddedMediaPlayer currentVideo;
	//all swing workers used for the functions
	private FastForwarder ff;
	private FastBackwarder fb;
	//all button icons normally
	private String iconPath = "./icons";
	private ImageIcon iconPlay = new ImageIcon(iconPath + "/PlayButtonN.png");
	private ImageIcon iconPause = new ImageIcon(iconPath + "/PauseButtonN.png");
	private ImageIcon iconFF = new ImageIcon(iconPath + "/FFButtonN.png");
	private ImageIcon iconFB = new ImageIcon(iconPath + "/FBButtonN.png");
	//all button icons when highlighted
	private ImageIcon iconPlayH = new ImageIcon(iconPath + "/PlayButtonH.png");
	private ImageIcon iconPauseH = new ImageIcon(iconPath + "/PauseButtonH.png");
	private ImageIcon iconFFH = new ImageIcon(iconPath + "/FFButtonH.png");
	private ImageIcon iconFBH = new ImageIcon(iconPath + "/FBButtonH.png");
	//all booleans for current play back status
	private boolean isPaused = true;
	private boolean isFastForwarding = false;
	private boolean isFastBackwarding = false;
	private boolean firstPlay = true;
	
	public static PlaybackPanel getInstance(){
		return _playbackPanel;
	}
	
	/**
	 * Create the panel.
	 */
	private PlaybackPanel() {
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow]"));
		setBackground(Color.GRAY);
		
		fbButton = setImageButton(iconFB);
		//add(fbButton, "cell 0 0,growx");
		add(fbButton, "cell 0 0,alignx right");
		
		playButton = setImageButton(iconPlay);
		add(playButton, "cell 1 0,growx");
		
		ffButton = setImageButton(iconFF);
		add(ffButton, "cell 2 0,alignx left");
		
		//===================CONSTRUCTOR-SETS UP THE PLAYBACK PANEL==============//
		//Play/Pause button setup
		playButton.addMouseListener(this);
		//when the play/pause button is pressed...
		playButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				playBtnPressed();
			}	
		});
		//cannot click the button until it is set to 
		playButton.setEnabled(false);
			
		//Fast Backward button setup;
		fbButton.addMouseListener(this);
		//for when rewind is clicked
		fbButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rewindBtnPressed();
			}	
		});
		//cannot click button until set to
		fbButton.setEnabled(false);
						
		//Fast Forward button setup
		ffButton.addMouseListener(this);
		//when fast forward button is clicked
		ffButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				forwardBtnPressed();
			}	
		});
		//cannot click button until set to
		ffButton.setEnabled(false);	
		
	}
	
	//==================METHODS USED BY CLASSES, INCLUDING THIS ONE======//
	
	//sets the current video the play back buttons are operating for
	public void setCurrentVideo(EmbeddedMediaPlayer m){
		currentVideo = m;
		setToPlay(); //paused at first, so set to play
	}
	//method to reset all booleans to their default values
	public void setToDefault() {
		isPaused = true;
		isFastForwarding = false;
		isFastBackwarding = false;
	}
	//enables all functions available for audio
	public void audioOn(){
		playButton.setEnabled(true);
		setToPlay();
		ffButton.setEnabled(false);
		fbButton.setEnabled(false);
	}
	//enables all functions available for media
	public void videoOn(){
		playButton.setEnabled(true);
		setToPlay();
		ffButton.setEnabled(true);
		fbButton.setEnabled(true);
	}
	//method to set play button for "Play"
	public void setToPlay() {
		playButton.setIcon(iconPlay);
		isPaused = true;
	}
	//method to set play button for "Pause"
	public void setToPause() {
		playButton.setIcon(iconPause);
		isPaused = false;
	}
	
	public void playBtnPressed(){
		//for when there is no video yet
		if (currentVideo == null ) {
			//warning message to be popped up
			String msg = "Please open a media file before trying to play it!";
			JOptionPane.showMessageDialog(null, msg);
			//for when a valid video is there
		} else {
			//when video is paused
			if (isPaused == true) {
				//when there is an instance of fast forward or backward call stop on the 
				//swing worker to stop it
				if (ff != null) {
					ff.stop();
					isFastForwarding = false;
				} 
				if (fb != null) {
					fb.stop();
					isFastBackwarding = false;
				}
				//it starts playing, and button changes to pause button
				currentVideo.play();
				setToPause();
				//media player is glitchy? and saves previous sessions settings
				//so volume and mute reset for the first play of media
				if (firstPlay) {
					/**VOLUME MUTE REFRESH*/
					//only for first play when program opened
					firstPlay = false;
				}
			//when video is playing
			} else {
				//video is paused, and button changes to play button
				currentVideo.pause();
				setToPlay();
			}
		}
	}
	
	public void rewindBtnPressed(){
		//if it is rewinding and clicked
		if (isFastBackwarding) {
			//rewind stops and video is paused normally
			fb.stop();
			isFastBackwarding = false;
		//if it is not rewinding and clicked
		} else {
			if (isFastForwarding) {
				ff.stop();
				isFastForwarding = false;
			}
			//for when its clicked while video is playing, video is paused first
			if (isPaused == false) {
				currentVideo.pause();
			}
			setToPlay();
			//rewinding swing worker is instantiated and executed
			fb = new FastBackwarder(currentVideo);
			fb.execute();
			//hence rewind becomes true
			isFastBackwarding = true;
		}
	}
	
	public void forwardBtnPressed(){
		if (isFastForwarding) {
			ff.stop();
			isFastForwarding = false;
		} else {
			if (isFastBackwarding) {
				fb.stop();
				isFastBackwarding = false;
			}
			if (isPaused == false) {
				currentVideo.pause();
			}
			setToPlay();
			ff = new FastForwarder(currentVideo);
			ff.execute();
			isFastForwarding = true;
		}
	}
	
	//set up and return a button using only an image icon
	//set up and return a button using only an image icon
	private JButton setImageButton(ImageIcon img) {
		JButton imgButton = new JButton(img);
		imgButton.setOpaque(false);
		imgButton.setContentAreaFilled(false);
		imgButton.setBorderPainted(false);
		imgButton.setFocusPainted(false);
		return imgButton;
	}
	
	//just changes cursors back and forth when entering and exiting component
	//only when enabled, as well as switching the button icons from highlighted one
	@Override
	public void mouseClicked(MouseEvent m) {
	}
	@Override
	public void mouseEntered(MouseEvent m) {
		JButton s = (JButton)m.getSource();
		if (s.isEnabled()) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			if (s.equals(playButton)) {
				if (isPaused == true) {
					playButton.setIcon(iconPlayH);
				} else {
					playButton.setIcon(iconPauseH);
				}
			} else if (s.equals(fbButton)) {
				fbButton.setIcon(iconFBH);
			} else if (s.equals(ffButton)) {
				ffButton.setIcon(iconFFH);
			}
		}

	}

	@Override
	public void mouseExited(MouseEvent m) {	
		JButton s = (JButton)m.getSource();
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (s.equals(playButton)) {
			if (isPaused == true) {
				playButton.setIcon(iconPlay);
			} else {
				playButton.setIcon(iconPause);
			}
		} else if (s.equals(fbButton)) {
			fbButton.setIcon(iconFB);
		} else if (s.equals(ffButton)) {
			ffButton.setIcon(iconFF);
		}
	}
	@Override
	public void mousePressed(MouseEvent m) {	
	}
	@Override
	public void mouseReleased(MouseEvent m) {	
	}

}

package playback;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VolumePanel extends JPanel implements MouseListener {

	private static VolumePanel _volumePanel = new VolumePanel();
	
	private EmbeddedMediaPlayer currentVideo;
	
	ImageIcon plus = new ImageIcon(this.getClass().getResource("icons/volumeUp.png"));
	ImageIcon minus = new ImageIcon(this.getClass().getResource("icons/volumeDown.png"));
	ImageIcon muteTrue = new ImageIcon(this.getClass().getResource("icons/muteTrue.png"));
	ImageIcon muteFalse = new ImageIcon(this.getClass().getResource("icons/muteFalse.png"));
	
	private JSlider volumeSlider;
	private JButton volumeUp, volumeDown, muteButton;
	
	public static VolumePanel getInstance(){
		return _volumePanel;
	}
	
	/**
	 * create the panel
	 */
	private VolumePanel() {
		
		setOpaque(false);
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow]"));
		
		//set up of volume slider
		volumeSlider = new JSlider(0,100,50);
		volumeSlider.setOpaque(false);
		//does have ticks, every 10
		volumeSlider.setSnapToTicks(true);
		volumeSlider.setMajorTickSpacing(10);
		volumeSlider.addMouseListener(this);
		//whenever volume slider value changes volume changes relatively
		volumeSlider.addChangeListener(new ChangeListener() {
			int volume;
			@Override
			public void stateChanged(ChangeEvent e) {
				if (volumeSlider.getValue() == 0) {
					volume = 0;
				} else {
					volume = volumeSlider.getValue() + 10;
				}
				currentVideo.setVolume(volume);
			}
		});
		volumeSlider.setEnabled(false);
		add(volumeSlider, "cell 1 0 2 1,growx");
				
		//set up of icon imaged volume up button
		volumeUp = setImageButton(plus);
		volumeUp.setSize(30,30);
		volumeUp.setLocation(230,25);
		volumeUp.addMouseListener(this);
		//when pressed increases volume 10 values, unless already 'max'
		volumeUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				upBtnPressed();
			}
		});
		volumeUp.setEnabled(false);
		add(volumeUp, "cell 2 1,alignx right");
				
		//set up of icon imaged volume down button
		volumeDown = setImageButton(minus);
		volumeDown.setSize(30,30);
		volumeDown.setLocation(65,25);
		volumeDown.addMouseListener(this);
		//when pressed decreases volume 10 values, unless already 'zero'
		volumeDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				downBtnPressed();
			}
		});
		volumeDown.setEnabled(false);
		add(volumeDown, "cell 1 1,alignx left");
				
		//set up of icon imaged mute/not mute button
		muteButton = setImageButton(muteFalse);
		muteButton.setSize(60,60);
		muteButton.setLocation(0,0);
		muteButton.addMouseListener(this);
		//when pressed switches between two images
		muteButton.addActionListener(new ActionListener() {
			//boolean to keep track of whether media is muted
			boolean isMute = false;
			int volBeforeMute;
			//image icons loaded
			@Override
			public void actionPerformed(ActionEvent e) {
				//when muted
				if (isMute) {
					//not muted and icon set to open speaker image, volume is what it was before
					//mute
					muteButton.setIcon(muteFalse);
					currentVideo.mute();
					isMute = false;
					volumeSlider.setValue(volBeforeMute);
				} else {
					//when not muted, muted and icon set to mute speaker image, volume is saved 
					//before going to zero
					muteButton.setIcon(muteTrue);
					volBeforeMute = volumeSlider.getValue();
					currentVideo.mute();
					isMute = true;
					volumeSlider.setValue(0);
				}
			}
		});
		muteButton.setEnabled(false);
		add(muteButton, "cell 0 0,alignx right");
	}
	
	/**
	 * sets the curret embedded media player
	 * @param m
	 */
	public void setCurrentVideo(EmbeddedMediaPlayer m) {
		currentVideo = m;
	}
	
	/**
	 * enables the volume components
	 */
	public void volumeOn() {
		volumeSlider.setEnabled(true);
		volumeUp.setEnabled(true);
		volumeDown.setEnabled(true);
		muteButton.setEnabled(true);
	}
	
	/**
	 * increases volume by 10
	 */
	public void upBtnPressed(){
		int currentVol = volumeSlider.getValue();
		if (currentVol == 100) {
			//do nothing
		} else {
			volumeSlider.setValue(currentVol + 10);
		}
	}
	
	/**
	 * decreases volume by 10
	 */
	public void downBtnPressed(){
		int currentVol = volumeSlider.getValue();
		if (currentVol == 0) {
			//do nothing
		} else {
			volumeSlider.setValue(currentVol - 10);
		}
	}
	
	/**
	 * set up and return a button using only an image icon
	 */
	private JButton setImageButton(ImageIcon img) {
		JButton imgButton = new JButton(img);
		imgButton.setOpaque(false);
		imgButton.setContentAreaFilled(false);
		imgButton.setBorderPainted(false);
		imgButton.setFocusPainted(false);
		return imgButton;
	}
	
	@Override
	public void mouseClicked(MouseEvent m) {
	}
	
	/**
	 * changes cursor to a hand shape when mouse is hovering over volume slider
	 */
	@Override
	public void mouseEntered(MouseEvent m) {
		JComponent s = (JComponent)m.getSource();
		if (s.isEnabled()) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}
	@Override
	public void mouseExited(MouseEvent m) {	
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	@Override
	public void mousePressed(MouseEvent m) {	
	}
	@Override
	public void mouseReleased(MouseEvent m) {	
	}
}

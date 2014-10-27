package editWindows;

import helperClasses.AudioProcessor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Frame which appears when user selects the overlay function.
 * 
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class OverlayFrame extends JFrame{
	//fields used in the class
	EmbeddedMediaPlayer vid;
	File vidFile;
	String musicPath;
	private String savePath;
	private JLabel descripA;
	private JLabel descripB;
	private JTextField songLoc;
	private JButton selectFile, action, cancel;
	private ActionListener ov;
	
	//Get user's screen dimension
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	public OverlayFrame(EmbeddedMediaPlayer video, File videoFile,String saveDir) {
		//input is saved into respective fields
		savePath = saveDir;
		vid = video;
		vidFile = videoFile;
		
		//frame set up
		setTitle("Video config");
		setSize(220, 190);
		setLocation((_screenWidth-220)/2, (_screenHeight-180)/2);
		setLayout(null);
		
		//the labels set up 
		descripA = new JLabel();
		descripA.setText("Please select an audio");
		descripA.setBounds(10, 10, 200, 30);
		add(descripA);
		descripB = new JLabel();
		descripB.setText("file to overlay video: ");
		descripB.setBounds(10, 40, 200, 30);
		add(descripB);
		
		//set up of the text field and image icon button to open files
		songLoc = new JTextField("select an audio file");
		songLoc.setEnabled(false);
		songLoc.setBounds(10, 80, 160, 30);
		add(songLoc);
		ImageIcon icon = new ImageIcon(this.getClass().getResource("resources/open_button.gif"));
		selectFile = new JButton(icon);
		selectFile.setOpaque(false);
		selectFile.setContentAreaFilled(false);
		selectFile.setBorderPainted(false);
		selectFile.setFocusPainted(false);
		selectFile.setBounds(180, 80, 30, 30);
		selectFile.addActionListener(new ActionListener() {

			//action listener for the select file button
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//opens a file chooser, when a file is selected
				JFileChooser fileOpener = new JFileChooser();
				fileOpener.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int click = fileOpener.showOpenDialog(null);
				if (click == JFileChooser.APPROVE_OPTION) {
					musicPath = fileOpener.getSelectedFile().getAbsolutePath();
					songLoc.setText(musicPath);
				} else {
					//do nothing
				}
			}
		});
		add(selectFile);

		//action button, in this case 'Overlay' button set up
		action = new JButton("Overlay");
		action.setBounds(10, 120, 95, 40);
		//action listener for overlaying, check audio signal and sets up the audio processing 
		//swing worker with appropriate inputs
		ov = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkIfAudio()) {
					long time = vid.getLength()/1000;
					AudioProcessor aP = new AudioProcessor("ov",vidFile);
					aP.setSaveDir(savePath);
					aP.setForOverlay(musicPath,time);
					aP.execute();
					dispose();
				} else {
					String msg = "Selected file is not an audio file!";
					JOptionPane.showMessageDialog(null, msg);
				}
			}
		};
		action.addActionListener(ov);
		add(action);
		
		//cancel button set up, disposes the frame
		cancel = new JButton("Cancel");
		cancel.setBounds(115, 120, 95, 40);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		add(cancel);
		
		setVisible(true);
	}
	
	public boolean checkIfAudio() {
		boolean isAudio = false;
	
		//bash command to 'grep' to verify file as media
		String audCmd = "avconv -i " + musicPath + " 2>&1 | grep Audio:";
		
		try {
			ProcessBuilder audCheckBuilder = new ProcessBuilder("/bin/bash","-c",audCmd);
			//process run
			Process audCheck = audCheckBuilder.start();
			int audTerm = audCheck.waitFor();
			//a correct termination indicates it is an audio file
			if (audTerm == 0) {
				isAudio = true;
			}
		} catch (Exception ex) {
			//if exception occurs nothing extra happens
		}
		
		if (isAudio) {
			return true;
		}
		return false;
	}
	

	//method to change the second label, i.e 'overlay' -> 'replace'

	public void setLabel(String text) {
		descripB.setText(text);
	}
	
	//method to override action listener for the action button
	public void setAction(String action, ActionListener a) {
		this.action.setText(action);
		this.action.removeActionListener(ov);
		this.action.addActionListener(a);
	}
}

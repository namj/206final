package project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * 
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class ReplaceFrame extends OverlayFrame{

	private String savePath;

	public ReplaceFrame(EmbeddedMediaPlayer video, File videoFile, String saveDir) {
		//uses Overlay Frame's constructor
		super(video, videoFile, saveDir);
		//with a changed label
		super.setLabel("file to replace video:");
		
		//saved path is stored in a private field
		savePath = saveDir;
		
		//action listener for the replace function
		ActionListener a = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//checks for audio signal and sets up audio process swing worker with inputs
				if (checkIfAudio()) {
					long time = vid.getLength()/1000;
					AudioProcessor aP = new AudioProcessor("rp",vidFile);
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

		//overrides the action button in overlay frame to make it an appropriate replace frame
		super.setAction("Replace", a);
	}
	
}

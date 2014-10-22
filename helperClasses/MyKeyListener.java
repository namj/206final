package helperClasses;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;

import editWindows.EditFrame;
import playback.PlaybackPanel;
import playback.VolumePanel;
import project.MainFrame;

public class MyKeyListener implements KeyEventDispatcher {
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {  
		
		if (e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED) {  
			int code = e.getKeyCode(); 
			//when ctrl + O is pressed
			if (79 == code) {  
				//when item is selected, a File chooser opens to select a file
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(null);
				//for when cancel/exit is pressed in the file chooser
				if (result == JFileChooser.CANCEL_OPTION) {
					//nothing is to be done
				//for when a file is selected
				} else if (result == JFileChooser.APPROVE_OPTION) {
					MainFrame.getInstance().startPlayVideo(fileChooser.getSelectedFile());
				}
			//when ctrl + h is pressed
			} else if (72 == code){
				MainFrame.openHelpFrame();
			//when ctrl + space is pressed
			} else if (32 == code) {
				PlaybackPanel.getInstance().playBtnPressed();
			//right arrow key
			} else if (39 == code) {
				PlaybackPanel.getInstance().forwardBtnPressed();
			//left arrow	
			} else if (37 == code) {
				PlaybackPanel.getInstance().rewindBtnPressed();
			//up	
			} else if (38 == code) {
				VolumePanel.getInstance().upBtnPressed();
			//down
			} else if (40 == code) {
				VolumePanel.getInstance().downBtnPressed();
			//E + ctrl 
			} else if (69 == code) {
				EditFrame frame = new EditFrame(MainFrame.getInstance());
			//ctrl + D
			} else if (68 == code) {
				MainFrame.getInstance().downloadFromUrl();
			}
		}  
		// pass on this key event  
		return false;  
	}  
  
}



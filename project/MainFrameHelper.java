package project;

import helperClasses.AudioProcessor;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import editWindows.DownloadFrame;

public class MainFrameHelper {

	public static void downloadFromUrl(File _mediaFile, String savePath) {
		String dlURL;
		//option pane that will take in the URL of download
		dlURL = JOptionPane.showInputDialog(null, "Please Enter URL:", "Download", 
				JOptionPane.DEFAULT_OPTION);
		//if cancelled do nothing
		if (dlURL == null) {
			//download cancelled before beginning
		} else {
			String msg = "Is this an open source file?";
			int reply = JOptionPane.showConfirmDialog(null, msg);
			//Whether its open source is confirmed, if yes
			if (reply == JOptionPane.YES_OPTION) {
				int rep = MainFrameHelper.setUpSaveFile(_mediaFile, savePath);
				if (rep == JFileChooser.APPROVE_OPTION) {
					//download frame opened and download commences
					DownloadFrame downloadFrame = new DownloadFrame(dlURL);
					downloadFrame.setSaveDir(savePath);
					downloadFrame.startDownload();
				}
			} else if (reply == JOptionPane.NO_OPTION){
				String warning = "Please only download from open source files\n"
						+ "Downloading non-open source files are illegal!";
				JOptionPane.showMessageDialog(null, warning);
			}
		}
	}
	
	public static int checkAudioSignal(File _mediaFile) {
		
		if (_mediaFile != null) {
			boolean isVideo = false;
			boolean isAudio = false;
			//bash command to 'grep' to verify file as media
			String audCmd = "avconv -i " + _mediaFile.getAbsolutePath() + " 2>&1 | grep Audio:";
			String vidCmd = "avconv -i " + _mediaFile.getAbsolutePath() + " 2>&1 | grep Video:";
			
			ProcessBuilder audCheckBuilder = new ProcessBuilder("/bin/bash","-c",audCmd);
			ProcessBuilder vidCheckBuilder = new ProcessBuilder("/bin/bash","-c",vidCmd);
			try {
				//process run
				Process audCheck = audCheckBuilder.start();
				int audTerm = audCheck.waitFor();
				Process vidCheck = vidCheckBuilder.start();
				int vidTerm = vidCheck.waitFor();
				//a correct termination indicates it is a media file
				if (audTerm == 0) {
					isAudio = true;
				} 
				if (vidTerm == 0){
					isVideo = true;
				}
				//only video files with audio signals are checked correct (0 for success)
				if (isAudio && !isVideo) {
					JOptionPane.showMessageDialog(null, "Opened file must be of video type");
					return 3;
				} else if (isAudio && isVideo) {
					return 0;
				}
			} catch (Exception ex) {
				//if exception occurs nothing extra happens
			}
		} else {
			JOptionPane.showMessageDialog(null, "Open a file before attempting any audio operation.");
			return 1;
		}
		return 2;
	}
	
	//method to extract (method used as it is used more than once)
	//input string to know if 'ex' or 'rm&ex'
	public static void extractAudio(String eORr, File _mediaFile) {
		//file chooser to direct save file name
		JFileChooser dirChooser = new JFileChooser(_mediaFile);
		int response = dirChooser.showSaveDialog(null);
		//when save is clicked
		if (response == JFileChooser.APPROVE_OPTION) {
			//file retrieved and its path
			File file = dirChooser.getSelectedFile();
			String saveDir = file.getAbsolutePath();
			AudioProcessor aP;
			if (eORr.equals("ex")) {
				//processor with 'ex' input for just extracting
				aP = new AudioProcessor("ex",_mediaFile);
			} else {
				//processor with 'rm&ex' input for extract -> removal
				aP = new AudioProcessor("rm&ex",_mediaFile);
			}
			//set the save directory and execute worker
			aP.setSaveDir(saveDir);
			aP.execute();
		} else {
			//nothing happens when cancelled
		}
	}
	
	public static int setUpSaveFile(File _mediaFile, String savePath) {
		JFileChooser saveChooser = new JFileChooser(_mediaFile);
		saveChooser.setDialogTitle("Select directory, enter file name to save as...");
		int response = saveChooser.showSaveDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			savePath = saveChooser.getSelectedFile().getAbsolutePath();
		}
		return response;
	}
	
	public static void openHelpFrame() {
		new HelpFrame();
	}
	
	public static void openHotKeyFrame(){
		new HotKeyFrame();
	}
	
	public static JButton setImageButton(ImageIcon img) {
		JButton imgButton = new JButton(img);
		imgButton.setOpaque(false);
		imgButton.setContentAreaFilled(false);
		imgButton.setBorderPainted(false);
		imgButton.setFocusPainted(false);
		return imgButton;
	}
		
}

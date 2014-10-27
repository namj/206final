package mainPackage;

import helperClasses.AudioProcessor;


import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import longTaskProcessors.EffectInserter;
import editWindows.DownloadFrame;

/**
 * This is a utility class containing methods used in the MainFrame.
 * @author namjun park (npar350)
 *
 */
public class MainFrameHelper {

	/**
	 * Commences downloading from URL function after regular prompting
	 */
	public static void downloadFromUrl() {
		String dlURL;

		//option pane that will take in the URL of download
		dlURL = JOptionPane.showInputDialog(null, "Please Enter URL:", "Download", 
				JOptionPane.DEFAULT_OPTION);
		//if cancelled do nothing
		if (dlURL == null) {
			//download cancelled before beginning
		} else {
			String msg = "Is this an open source file?";
			int reply = JOptionPane.showConfirmDialog(null, msg, null , JOptionPane.YES_NO_OPTION);
			//Whether its open source is confirmed, if yes
			if (reply == JOptionPane.YES_OPTION) {
				
				String fileName = dlURL.substring(dlURL.lastIndexOf('/')+1, dlURL.length() );
				String userHome = System.getProperty( "user.home" );
				File theFile = new File(userHome+"/Downloads/"+fileName);
				
				if (theFile.exists()){
					String [] options = { "Overwrite", "Resume download", "Cancel" };
					int response = JOptionPane.showOptionDialog(null,  fileName+" already exists in ~/Downloads.", "Uh-Oh!", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
					if (response == 0){
						//delete file
						theFile.delete();
						//download frame opened and download commences
						DownloadFrame downloadFrame = new DownloadFrame(dlURL);
						downloadFrame.startDownload();	
					} else if (response == 1){
						//download frame opened and download commences
						DownloadFrame downloadFrame = new DownloadFrame(dlURL);
						downloadFrame.startDownload();	
					}
					
				} else {
					//download frame opened and download commences
					DownloadFrame downloadFrame = new DownloadFrame(dlURL);
					downloadFrame.startDownload();	
				}
				
				
				
			} else if (reply == JOptionPane.NO_OPTION){
				String warning = "Please only download from open source files\n"
						+ "Downloading non-open source files are illegal!";
				JOptionPane.showMessageDialog(null, warning);
			}
		}
	}
	
	/**
	 * checks for signals in the media file
	 * @param _mediaFile
	 * @return
	 */
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
	
	/**
	 * method to extract audio from a file
	 * @param eORr
	 * @param _mediaFile
	 */
	public static void extractAudio(String eORr, File _mediaFile) {
		//file chooser to direct save file name
		//JFileChooser dirChooser = new JFileChooser(_mediaFile);
		JFileChooser saveChooser = new JFileChooser(_mediaFile);
		saveChooser.setDialogTitle("Select directory, enter file name to save as...");
		int response = saveChooser.showSaveDialog(null);
		String savePath = null;
		if (response == JFileChooser.APPROVE_OPTION) {
			savePath = saveChooser.getSelectedFile().getAbsolutePath();
		}
		
		if (savePath != null){
			File theFile = new File(savePath);
			
			if ((new File (savePath)).exists()){
				String [] options = { "Overwrite", "Cancel" };
				saveChooser.setDialogTitle("Select directory, enter file name to save as...");
				int response2 = JOptionPane.showOptionDialog(null,  theFile.getName()+" already exists!", "Uh-Oh!", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (response2 == 0){
					response = JFileChooser.APPROVE_OPTION;
				} else {
					response =  JFileChooser.CANCEL_OPTION;
				}
			}
		}

		//when save is clicked
		if (response == JFileChooser.APPROVE_OPTION) {
			//file retrieved and its path
			File file = saveChooser.getSelectedFile();
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
	
	/**
	 * opens the help frame
	 */
	public static void openHelpFrame() {
		new HelpFrame();
	}
	
	/**
	 * opens the hotkey frame
	 */
	public static void openHotKeyFrame(){
		new HotKeyFrame();
	}
	
	/**
	 * sets an icon for a Jbutton
	 * @param img
	 * @return
	 */
	public static JButton setImageButton(ImageIcon img) {
		JButton imgButton = new JButton(img);
		imgButton.setOpaque(false);
		imgButton.setContentAreaFilled(false);
		imgButton.setBorderPainted(false);
		imgButton.setFocusPainted(false);
		return imgButton;
	}
		
}

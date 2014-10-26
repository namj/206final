package editWindows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import longTaskProcessors.Downloader;

/**
 * 
 * This frame shows when user downloads a file.
 * It displays information about download progress
 *
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class DownloadFrame extends JFrame{
	private String dlURL;
	private String saveDir;
	//Components in the download frame
	private JProgressBar dlProgress;
	private JButton cancelButton, pauseButton;
	private Downloader downloader;
	private JLabel url,fileSize,speed,time;
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	//constructor for the download frame, with input URL
	public DownloadFrame(String url) {
		//frame setup and URL setup
		super("Downloading... 0%");
		dlURL = url;
		
		setBackground(Color.LIGHT_GRAY);
		setSize(450,230);
		setLayout(null);
		setLocation((_screenWidth-450)/2,(_screenHeight-230)/2);
		
		//all texts in JLabels set up
		this.url = new JLabel(url);
		this.url.setBounds(25, 10, 400, 20);
		fileSize = new JLabel("File Size:   0");
		fileSize.setBounds(25, 35, 400, 20);
		speed = new JLabel("Download Speed:   0");
		speed.setBounds(25, 60, 400, 20);
		time = new JLabel("Time left:   0");
		time.setBounds(25, 85, 400, 20);
		add(this.url);
		add(fileSize);
		add(speed);
		add(time);
		
		//set up for cancel button for canceling download
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(315,170,110,30);
		//cancel button stops download
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (downloader.isRunning()) {
					downloader.stop();
				} else {
					exit();
				}
			}
		});
		add(cancelButton);
		//set up for pause/resume button for pausing and resuming downloads
		pauseButton = new JButton("Pause");
		pauseButton.setBounds(195,170,110,30);
		pauseButton.addActionListener(new ActionListener() {
			//default is download is not paused
			boolean isPaused = false;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//if paused execute a downloader swing worker and set button text to pause
				//else stop the download and change button to resume
				if (isPaused) {
					downloader = new Downloader(getItself());
					downloader.execute();
					pauseButton.setText("Pause");
					isPaused = false;
				} else {
					pauseButton.setText("Resume");
					downloader.pause();
					isPaused = true;
				}
			}
		});
		add(pauseButton);
		
		//set up of progress bar to keep track of download
		dlProgress = new JProgressBar(0,100);
		dlProgress.setValue(0);
		dlProgress.setSize(400, 30);
		dlProgress.setLocation(25, 130);
		add(dlProgress);
		
		setVisible(true);
	}
	
	/**
	 * method that returns the URL passed into this download frame
	 * @return
	 */
	public String getURL() {
		return dlURL;
	}
	
	/**
	 * returns instance of this download frame
	 * @return
	 */
	private DownloadFrame getItself() {
		return this;
	}
	
	
	/**
	 * sets the text of filesize label
	 * @param fs
	 */
	public void setFileSize(String fs) {
		fileSize.setText("File Size:   " + fs);
	}
	
	/**
	 * Method that updates the status of download. Sets text and values for:
	 * frame title, 2labels, progressbar
	 * @param p
	 * @param s
	 * @param t
	 */
	public void updateDlInfo(String p,String s,String t) {
		this.setTitle("Downloading... " + p + "%");
		dlProgress.setValue(Integer.parseInt(p));
		speed.setText("Download Speed:   " + s);
		time.setText("Time left:   " + t);
	}
	
	/**
	 * Commences download of media file
	 */
	public void startDownload() {
		downloader = new Downloader(this);
		downloader.setSave(saveDir);
		downloader.execute();
	}
	
	/**
	 * disposes this frame after download finishes
	 */
	public void exit() {
		this.dispose();
	}
	
	/**
	 *sets the path of outputfile
	 * @param savePath
	 */
	public void setSaveDir(String savePath) {
		saveDir = savePath;
	}

	
}

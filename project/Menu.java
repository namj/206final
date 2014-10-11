package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenu;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import project.MainPanel;
import project.AudioProcessor;
import project.HelpFrame;
import project.CreateTitleCreditFrame;
import project.DownloadFrame;
import project.EditFrame;
import project.OverlayFrame;
import project.ReplaceFrame;

import com.sun.jna.Native;

import javax.swing.JTextField;
import javax.swing.JLayeredPane;

public class Menu extends JFrame implements ActionListener {

	//Get user's screen dimension
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private String _mediaPath = "";
	private File _mediaFile;
	private String savePath = "";
	
	private MainPanel container;
	private EmbeddedMediaPlayerComponent ourMediaPlayer;
	private EmbeddedMediaPlayer currentVideo;
	//additional buttons for the main panel
	private JButton openButton;
	private JButton dlButton;
	private JButton editButton;
	
	
	private JPanel contentPane;
	private JTextField mediaNameField;
	
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((_screenWidth-1450)/2,(_screenHeight-900)/2,1450, 900);
		
		//Make sure mediaplaycomp does paint over jmenu
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

		WindowListener exitListener = new WindowAdapter() {
			//Before the frame is closed set volume to default, and not mute if muted
			@Override
			public void windowClosing(WindowEvent e) {
				//if a video has been used, set default volume to 50 and make it not muted
				if (currentVideo != null) {
					currentVideo.setVolume(60);
					if (currentVideo.isMute()) {
						currentVideo.mute(); //not mute if muted
					}
				}
				System.exit(0); //exit program
			}		
		};
		addWindowListener(exitListener);
		
		
		//embedded media player setup
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		ourMediaPlayer = new EmbeddedMediaPlayerComponent();
		
		//instantiate main panel passing in Media Player
		container = new MainPanel(ourMediaPlayer);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][][573.00,grow][:150:300,grow]", "[][137.00,grow]"));
		
		dlButton = new JButton("Download");
		contentPane.add(dlButton, "cell 0 0,growy");
		dlButton.setActionCommand("Download File");
		dlButton.addActionListener(this);
		
		editButton = new JButton("Edit");
		contentPane.add(editButton, "cell 1 0,growy");
		editButton.setActionCommand("Edit");
		editButton.addActionListener(this);
		
		//openButton = new JButton("O");
		openButton = setImageButton(new ImageIcon("./icons/open_button.gif"));
		contentPane.add(openButton, "cell 2 0,alignx right,growy");
		openButton.setActionCommand("Open File");
		openButton.addActionListener(this);
		
		mediaNameField = new JTextField("Please open a media file...");
		mediaNameField.setEditable(false);
		contentPane.add(mediaNameField, "cell 3 0,grow");
		mediaNameField.setColumns(10);
		
		contentPane.add(container, "cell 0 1 4 1,grow");	
		
		//Add menu bar
		setJMenuBar(setUpMenuBar());
		
	}
	
	private JMenuBar setUpMenuBar() {
		//create object for all menu bar, menus and items
		JMenu file, edit, help, _space, _space2;

		JMenuItem _open, _exit, _dl, _title, _credit;
		JMenuItem _rmAudio,_exAudio,_ovAudio, _rpAudio, _read, _addText1, _addText2;
		JMenuBar menuBar = new JMenuBar();
		
		//set the graphics (color) for the Menu bar
		menuBar.setBackground(Color.DARK_GRAY);
		menuBar.setBorderPainted(true);
		
		//empty spaces to put in between menu options (spacings)
		_space = new JMenu();
		_space.setEnabled(false);
		_space2 = new JMenu();
		_space2.setEnabled(false);
		
		//setup of menu 'file' 
		file = new JMenu("File");
		file.setForeground(Color.LIGHT_GRAY);
		//setup of all the items belonging to the 'file' menu
		_open = new JMenuItem("Open");
		_dl = new JMenuItem("Download");
		_open.setActionCommand("Open File");
		_open.addActionListener(this);
		_dl.setActionCommand("Download File");
		_dl.addActionListener(this);
		_exit = new JMenuItem("Exit");
		_exit.setActionCommand("Exit");
		_exit.addActionListener(this);
		file.add(_open);
		file.add(_dl);
		file.addSeparator();
		file.add(_exit);
		menuBar.add(file);
		menuBar.add(_space);
		
		//setup of menu 'edit'
		edit = new JMenu("Edit");
		edit.setForeground(Color.LIGHT_GRAY);
		//setup of all the items belonging to the 'edit' menu
		_title = new JMenuItem("Add title page(s)");
		_title.setActionCommand("Create title");
		_title.addActionListener(this);
		_credit = new JMenuItem("Add credit page(s)");
		_credit.setActionCommand("Create credit");
		_credit.addActionListener(this);
		_rmAudio = new JMenuItem("Remove Audio");
		_rmAudio.setActionCommand("rmAudio");
		_rmAudio.addActionListener(this);
		_exAudio = new JMenuItem("Extract Audio");
		_exAudio.setActionCommand("exAudio");
		_exAudio.addActionListener(this);
		_ovAudio = new JMenuItem("Overlay Audio");
		_ovAudio.setActionCommand("ovAudio");
		_ovAudio.addActionListener(this);
		_rpAudio = new JMenuItem("Replace Audio");
		_rpAudio.setActionCommand("rpAudio");
		_rpAudio.addActionListener(this);
		_addText1 = new JMenuItem("Add text (start/end)");
		_addText1.setActionCommand("addTextStartEnd");
		_addText1.addActionListener(this);
		_addText2 = new JMenuItem("Add text (specified)");
		_addText2.setActionCommand("addTextSpecified");
		_addText2.addActionListener(this);
		
		edit.add(_title);
		edit.add(_credit);
		edit.addSeparator();
		edit.add(_rmAudio);
		edit.add(_exAudio);
		edit.add(_ovAudio);
		edit.add(_rpAudio);
		edit.addSeparator();
		edit.add(_addText1);
		edit.add(_addText2);
		

		menuBar.add(edit);
		menuBar.add(_space2);
		
		//setup of menu 'help'
		help = new JMenu("Help");
		help.setForeground(Color.LIGHT_GRAY);
		_read = new JMenuItem("Open ReadMe");
		_read.setActionCommand("Open readme");
		_read.addActionListener(this);
		help.add(_read);
		menuBar.add(help);
		//menuBar.requestFocusInWindow();
		
		//final menu bar is returned at the end of the setup 
		return menuBar;
	}
	
	
	private int checkAudioSignal() {
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
	
	//method to start playing a video given a file
	public void startPlayVideo(File mediaF) {
		_mediaFile = mediaF;
		_mediaPath = _mediaFile.getAbsolutePath();
		
		//booleans to decide whether selected file is of a media file
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
		} catch (Exception ex) {
			//if exception occurs nothing extra happens
		}
		//when media file is selected
		if (isVideo || isAudio) {
			//current video is instantiated and paused immediately when it starts playing
			currentVideo = ourMediaPlayer.getMediaPlayer();
			currentVideo.startMedia(_mediaPath);
			currentVideo.pause();
			//video is set in the main panel
			container.setCurrentVid(currentVideo,_mediaFile,mediaNameField);
			
			if (isVideo) {
				//media buttons (play, fast-forward, etc) are enabled
				container.setMediaButtonOn();
			} else if (isAudio) {
				//audio buttons (play, etc) are enabled
				container.setAudioButtonOn();
			}
		//warning message if file is not media
		} else {
			JOptionPane.showMessageDialog(this, "File is not an audio or video type!");
		}
	}
	
	//method to extract (method used as it is used more than once)
	//input string to know if 'ex' or 'rm&ex'
	public void extractAudio(String eORr) {
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
	
	public int setUpSaveFile() {
		JFileChooser saveChooser = new JFileChooser(_mediaFile);
		saveChooser.setDialogTitle("Select directory, enter file name to save as...");
		int response = saveChooser.showSaveDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			savePath = saveChooser.getSelectedFile().getAbsolutePath();
		}
		return response;
	}
	
	public static void openHelpFrame() {
		HelpFrame help = new HelpFrame();
	}
	
	private JButton setImageButton(ImageIcon img) {
		JButton imgButton = new JButton(img);
		imgButton.setOpaque(false);
		imgButton.setContentAreaFilled(false);
		imgButton.setBorderPainted(false);
		imgButton.setFocusPainted(false);
		return imgButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Open File")) {
			//when item is selected, a File chooser opens to select a file
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = fileChooser.showOpenDialog(this);
			//for when cancel/exit is pressed in the file chooser
			if (result == JFileChooser.CANCEL_OPTION) {
				//nothing is to be done
			//for when a file is selected
			} else if (result == JFileChooser.APPROVE_OPTION) {
				startPlayVideo(fileChooser.getSelectedFile());
			}
		} else if (e.getActionCommand().equals("Download File")){
			String dlURL;
			//option pane that will take in the URL of download
			dlURL = JOptionPane.showInputDialog(this, "Please Enter URL:", "Download", 
					JOptionPane.DEFAULT_OPTION);
			//if cancelled do nothing
			if (dlURL == null) {
				//download cancelled before beginning
			} else {
				String msg = "Is this an open source file?";
				int reply = JOptionPane.showConfirmDialog(null, msg);
				//Whether its open source is confirmed, if yes
				if (reply == JOptionPane.YES_OPTION) {
					int rep = setUpSaveFile();
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
		} else if (e.getActionCommand().equals("Edit")) {
			//pop up edit frame when edit button or item is selected
			EditFrame editFrame = new EditFrame(this);
		} else if (e.getActionCommand().equals("Exit")) {
			//exit when exit item is pressed
			System.exit(0);
		} else if (e.getActionCommand().equals("rmAudio")) {
			//attain return value from checking audio signal
			int audioCheck = checkAudioSignal();
			//if signal is successful....
			if (audioCheck == 0) {
				//ask if audio getting removed should be saved (extracted)
				String msg = "Would you like to save the audio into another file?";
				int response = JOptionPane.showConfirmDialog(null, msg);
				if (response == JOptionPane.YES_OPTION) {
					//perform extraction based on 'rm&ex' when yes 
					extractAudio("rm&ex");
				} else if (response == JOptionPane.NO_OPTION) {
					int rep = setUpSaveFile();
					if (rep == JFileChooser.APPROVE_OPTION) {
						//if no, begin audio process for removal (rm)
						AudioProcessor aP = new AudioProcessor("rm",_mediaFile);
						aP.setSaveDir(savePath);
						aP.execute();
					}
				}
			//if check returns a 2, no audio signal
			} else if (audioCheck == 2) {
				String msg = "The media contains no audio signal!\n" +
						"There is no audio to be removed.";
				JOptionPane.showMessageDialog(null,msg);
			}
		} else if (e.getActionCommand().equals("exAudio")) {
			//same as above
			int audioCheck = checkAudioSignal();
			if (audioCheck == 0) {
				//perform extraction based on 'ex'
				extractAudio("ex");
			} else if (audioCheck == 2) {
				String msg = "The media contains no audio signal!\n" +
						"There is no audio to be extracted.";
				JOptionPane.showMessageDialog(null,msg);
			}
		} else if (e.getActionCommand().equals("ovAudio")) {
			//same as above
			int audioCheck = checkAudioSignal();
			if (audioCheck == 0) {
				int rep = setUpSaveFile();
				if (rep == JFileChooser.APPROVE_OPTION) {
					//overlay frame pops up after audio is checked
					OverlayFrame ovFrame = new OverlayFrame(currentVideo,_mediaFile, savePath);
				}
			} else if (audioCheck == 2) {
				String msg = "The media contains no audio signal!\n" +
						"There is no audio to be overlayed.";
				JOptionPane.showMessageDialog(null,msg);
			}
		} else if (e.getActionCommand().equals("rpAudio")) {
			//gets checked audio int
			int audioCheck = checkAudioSignal();
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				int rep = setUpSaveFile();
				if (rep == JFileChooser.APPROVE_OPTION) {
					ReplaceFrame rpFrame = new ReplaceFrame(currentVideo,_mediaFile, savePath);
				}
			}
		//for command create title/credit page, open appropriate frame
		} else if (e.getActionCommand().equals("Create title")) {
			CreateTitleCreditFrame titleFrame = new CreateTitleCreditFrame(_mediaPath, "Create Title page(s)");
		} else if (e.getActionCommand().equals("Create credit")) {
			CreateTitleCreditFrame creditFrame = new CreateTitleCreditFrame(_mediaPath, "Create Credit page(s)");
		//when help item is pressed, open the readme file in a scrollpane
		} else if (e.getActionCommand().equals("Open readme")) {
			openHelpFrame();
		} else if (e.getActionCommand().equals("addTextStartEnd")) {
			TextInsertFrame frame = new TextInsertFrame(_mediaPath, currentVideo);
		} else if (e.getActionCommand().equals("addTextSpecified")) {
			TextInsertFrame2 frame = new TextInsertFrame2(_mediaPath, currentVideo);
		} else if (e.getActionCommand().equals("addEffect")) {
			EffectInsertFrame frame = new EffectInsertFrame(_mediaPath, currentVideo);
		}
	}

}

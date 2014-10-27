package project;

import helperClasses.AudioProcessor;
import helperClasses.MyKeyListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import net.miginfocom.swing.MigLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenu;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import project.SubMainPanel;
import project.HelpFrame;

import com.sun.jna.Native;

import editWindows.AddSubtitlesFrame;
import editWindows.CreateTitleCreditFrame;
import editWindows.DownloadFrame;
import editWindows.EditFrame;
import editWindows.EffectApplyFrame;
import editWindows.FadeApplyFrame;
import editWindows.OverlayFrame;
import editWindows.ReplaceFrame;
import editWindows.RotateApplyFrame;
import editWindows.TextInsertFrame;
import editWindows.TextInsertFrame2;

import javax.swing.JTextField;
import javax.swing.JLayeredPane;

public class MainFrame extends JFrame implements ActionListener{

	//Get user's screen dimension
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private String _mediaPath = "";
	private File _mediaFile;
	private String savePath = "";
	
	private SubMainPanel container;
	private EmbeddedMediaPlayerComponent ourMediaPlayer;
	private EmbeddedMediaPlayer currentVideo;
	//additional buttons for the main panel
	private JButton openButton;
	private JButton dlButton;
	private JButton editButton;
	private subtitleJMenu subMenu;
	
	private JPanel contentPane;
	private JTextField mediaNameField;
	
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	
	private static MainFrame frame = new MainFrame();
	
	public static MainFrame getInstance(){
		return frame;
	}
	

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
				int r = JOptionPane.showConfirmDialog(null, "New to this? Would you like to open a helper?", "Welcome" , 2);
				if (r == JOptionPane.YES_OPTION) {
					MainFrameHelper.openHelpFrame();
					MainFrameHelper.openHotKeyFrame();
				} else {
					//nothing happens
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private MainFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((_screenWidth-1350)/2,(_screenHeight-800)/2,1350, 800);
		setTitle("npar35_VAMIX");
		
		//Make sure mediaplaycomp does paint over jmenu
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

		//Hijack the keyboard manager
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyKeyListener());

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
		container = new SubMainPanel(ourMediaPlayer);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
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
		openButton = MainFrameHelper.setImageButton(new ImageIcon("./icons/open_button.gif"));
		contentPane.add(openButton, "cell 2 0,alignx right,growy");
		openButton.setActionCommand("Open File");
		openButton.addActionListener(this);
		
		mediaNameField = new JTextField("Please open a media file...");
		mediaNameField.addMouseListener(_clickFileNameField);
		mediaNameField.setEditable(false);
		contentPane.add(mediaNameField, "cell 3 0,grow");
		mediaNameField.setColumns(10);
		
		contentPane.add(container, "cell 0 1 4 1,grow");	
		
		//Add menu bar
		setJMenuBar(setUpMenuBar());
		
	}
	
	/**
	 * This method sets up EVERYTHING for the JMenubar including JMenu, JMenuitems.
	 * @return
	 */
	private JMenuBar setUpMenuBar() {
		//create object for all menu bar, menus and items
		JMenu file, edit, help, options,_space, _space2, _space3, _subTracks;

		JMenuItem _open, _exit, _dl, _title, _credit,_rmAudio,_exAudio,_ovAudio, _rpAudio, _read, _addText1, _addText2, _hKeys, _addEffect, _subs;
		JMenuItem _addRotate, _addFade, _addSubStream;
		JMenuBar menuBar = new JMenuBar();
		
		//set the graphics (color) for the Menu bar
		menuBar.setBackground(Color.DARK_GRAY);
		menuBar.setBorderPainted(true);
		
		//empty spaces to put in between menu options (spacings)
		_space = new JMenu();
		_space.setEnabled(false);
		_space2 = new JMenu();
		_space2.setEnabled(false);
		_space3 = new JMenu();
		_space3.setEnabled(false);
		
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
		_addEffect = new JMenuItem("Apply effect");
		_addEffect.setActionCommand("addEffect");
		_addEffect.addActionListener(this);
		_addRotate = new JMenuItem("Apply rotate");
		_addRotate.setActionCommand("addRotate");
		_addRotate.addActionListener(this);
		_addFade = new JMenuItem("Apply fade");
		_addFade.setActionCommand("addFade");
		_addFade.addActionListener(this);
		_addSubStream = new JMenuItem("Add subtitle stream");
		_addSubStream.setActionCommand("mergeSub");
		_addSubStream.addActionListener(this);
		
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
		edit.addSeparator();
		edit.add(_addEffect);
		edit.add(_addRotate);
		edit.add(_addFade);
		edit.addSeparator();
		edit.add(_addSubStream);
		
		menuBar.add(edit);
		menuBar.add(_space2);
		
		//set up of menu 'options'
		options = new JMenu("Options");
		options.setForeground(Color.LIGHT_GRAY);
		//setup items belonging to 'Options' menu

		subMenu = new subtitleJMenu();
		subMenu.addMenuListener(new MenuListener(){

			@Override
			public void menuSelected(MenuEvent e) {
				if (currentVideo != null){
					subMenu.setupItems(currentVideo);
				}
			}
			@Override
			public void menuDeselected(MenuEvent e) {
				//do nothing
			}
			@Override
			public void menuCanceled(MenuEvent e) {
				//do nothing
			}
		});
		
		options.add(subMenu);
		
		menuBar.add(options);
		menuBar.add(_space3);
		
		//setup of menu 'help'
		help = new JMenu("Help");
		help.setForeground(Color.LIGHT_GRAY);
		_read = new JMenuItem("Open ReadMe");
		_read.setActionCommand("Open readme");
		_read.addActionListener(this);
		_hKeys = new JMenuItem("Hot keys");
		_hKeys.setActionCommand("Hot keys");
		_hKeys.addActionListener(this);
		help.add(_read);
		help.addSeparator();
		help.add(_hKeys);
		menuBar.add(help);
		
		//final menu bar is returned at the end of the setup 
		return menuBar;
	}
	
	/**
	 * method to start playing a video given a file
	 * @param mediaF
	 */
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
	
	//mouse adaptor added hooked image pane to detect double click for download
	private final MouseAdapter _clickFileNameField = new MouseAdapter() {
	    public void mousePressed(MouseEvent e){
	    	//when item is selected, a File chooser opens to select a file
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = fileChooser.showOpenDialog(null);
			//for when cancel/exit is pressed in the file chooser
			if (result == JFileChooser.CANCEL_OPTION) {
				//nothing is to be done
			//for when a file is selected
			} else if (result == JFileChooser.APPROVE_OPTION) {
				startPlayVideo(fileChooser.getSelectedFile());
			}
	    }
	};
	
	/**
	 * actionPerformed overriden to perform functionalities for given actioncommands from components
	 */
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
				subMenu.setupItems(currentVideo);
			}
			
		} else if (e.getActionCommand().equals("Download File")){
			MainFrameHelper.downloadFromUrl(_mediaFile, savePath);
			
		} else if (e.getActionCommand().equals("Edit")) {
			//pop up edit frame when edit button or item is selected
			EditFrame editFrame = new EditFrame(this);
			
		} else if (e.getActionCommand().equals("Exit")) {
			//exit when exit item is pressed
			System.exit(0);
			
		} else if (e.getActionCommand().equals("rmAudio")) {
			//attain return value from checking audio signal
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//if signal is successful....
			if (audioCheck == 0) {
				//ask if audio getting removed should be saved (extracted)
				String msg = "Would you like to save the audio into another file?";
				int response = JOptionPane.showConfirmDialog(null, msg);
				if (response == JOptionPane.YES_OPTION) {
					//perform extraction based on 'rm&ex' when yes 
					MainFrameHelper.extractAudio("rm&ex", _mediaFile);
				} else if (response == JOptionPane.NO_OPTION) {
					int rep = MainFrameHelper.setUpSaveFile(_mediaFile, savePath);
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
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			if (audioCheck == 0) {
				//perform extraction based on 'ex'
				MainFrameHelper.extractAudio("ex", _mediaFile);
			} else if (audioCheck == 2) {
				String msg = "The media contains no audio signal!\n" +
						"There is no audio to be extracted.";
				JOptionPane.showMessageDialog(null,msg);
			}
			
		} else if (e.getActionCommand().equals("ovAudio")) {
			//same as above
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			if (audioCheck == 0) {
				int rep = MainFrameHelper.setUpSaveFile(_mediaFile, savePath);
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
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				int rep = MainFrameHelper.setUpSaveFile(_mediaFile, savePath);
				if (rep == JFileChooser.APPROVE_OPTION) {
					ReplaceFrame rpFrame = new ReplaceFrame(currentVideo,_mediaFile, savePath);
				}
			}
			
		} else if (e.getActionCommand().equals("Hot keys")){
			//open guide for hotkeys
			MainFrameHelper.openHotKeyFrame();
			
		//for command create title/credit page, open appropriate frame
		} else if (e.getActionCommand().equals("Create title")) {
			
			//gets checked audio int
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				CreateTitleCreditFrame titleFrame = new CreateTitleCreditFrame(_mediaPath, "Create Title page(s)");
			}
			
		} else if (e.getActionCommand().equals("Create credit")) {
			//gets checked audio int
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				CreateTitleCreditFrame creditFrame = new CreateTitleCreditFrame(_mediaPath, "Create Credit page(s)");
			}
			
		//when help item is pressed, open the readme file in a scrollpane
		} else if (e.getActionCommand().equals("Open readme")) {
			MainFrameHelper.openHelpFrame();
			
		} else if (e.getActionCommand().equals("addTextStartEnd")) {
			//gets checked audio int
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				TextInsertFrame frame = new TextInsertFrame(_mediaPath, currentVideo);
			}
			
		} else if (e.getActionCommand().equals("addTextSpecified")) {
			//gets checked audio int
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				TextInsertFrame2 frame = new TextInsertFrame2(_mediaPath, currentVideo);
			}
			
		} else if (e.getActionCommand().equals("addEffect")) {
			//gets checked audio int
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				EffectApplyFrame frame = new EffectApplyFrame(_mediaPath, currentVideo);
			}
			
		} else if (e.getActionCommand().equals("addRotate")) {
			//gets checked audio int
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				RotateApplyFrame frame = new RotateApplyFrame(_mediaPath, currentVideo);
			}
			
		} else if (e.getActionCommand().equals("addFade")) {
			//gets checked audio int
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				FadeApplyFrame frame = new FadeApplyFrame(_mediaPath, currentVideo);
			}
			
		} else if (e.getActionCommand().equals("mergeSub")){
			//gets checked audio int
			int audioCheck = MainFrameHelper.checkAudioSignal(_mediaFile);
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				AddSubtitlesFrame frame = new AddSubtitlesFrame(_mediaPath, currentVideo);
			}
		}
	}
	
	/**
	 * returns the current embeddedMediaPlayerComponent set for this frame
	 * @return
	 */
	public EmbeddedMediaPlayerComponent getMediaPlayerComp(){
		return ourMediaPlayer;
	}
	
	/**
	 * returns the current embeddedmediaplayer set for this frame
	 * @return
	 */
	public EmbeddedMediaPlayer getMediaPlayer(){
		return currentVideo;
	}
	
	/**
	 * returns the mediafile set for this frame
	 * @return
	 */
	public File getMediaFile(){
		return _mediaFile;
	}
	
	/**
	 * returns the current savePath(for output files) specified for this frame
	 * @return
	 */
	public String getSavePath(){
		return savePath;
	}

}

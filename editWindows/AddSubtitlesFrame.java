package editWindows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import longTaskProcessors.SubtitleAdder;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.Font;

public class AddSubtitlesFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JLabel pleaseChooseLabel;
	private JButton fileChooseButton, addStreamButton;
	private JLabel lblNewLabel;
	private String selectedSrt;
	
	private JTextField srtField;

	private String mediaPath;
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();

	/**
	 * Create the frame.
	 */
	public AddSubtitlesFrame(String mPath, EmbeddedMediaPlayer currentVideo) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds((_screenWidth-350)/2,(_screenHeight-230)/2, 350, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[20%,grow][30%][][30%][20%]"));
		
		mediaPath = mPath;
		
		pleaseChooseLabel = new JLabel("Please choose an srt file...");
		pleaseChooseLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		contentPane.add(pleaseChooseLabel, "cell 0 0,alignx center,aligny bottom");
		
		fileChooseButton = new JButton("Navigate");
		fileChooseButton.addActionListener(this);
		contentPane.add(fileChooseButton, "cell 0 1,alignx center,aligny top");
		
		lblNewLabel = new JLabel("Selected file:");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		contentPane.add(lblNewLabel, "cell 0 2,alignx center,aligny bottom");
		
		srtField = new JTextField();
		srtField.setEnabled(false);
		contentPane.add(srtField, "cell 0 3,growx,aligny top");
		
		addStreamButton = new JButton("Add subtitle stream to video");
		addStreamButton.addActionListener(this);
		contentPane.add(addStreamButton, "cell 0 4,alignx center,growy");
		
		setVisible(true);
	}
	
	/**
	 * actionPerformed method overridden to react to button presses
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fileChooseButton ){
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setDialogTitle("Select an srt file");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("srt files", "srt");
			fileChooser.addChoosableFileFilter(filter);
	 
			int result = fileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION){	
				
				selectedSrt = fileChooser.getSelectedFile().toString();
				srtField.setText(selectedSrt);
			}
			
		} else if (e.getSource() == addStreamButton){
			if (srtField.getText().length() == 0){
				JOptionPane.showMessageDialog(null, "Please choose an srt file to add to video");
			} else {
				//display JFilechooser to specify output 
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select directory to save video to");
				int result = fileChooser.showSaveDialog(this);
				if (result == JFileChooser.APPROVE_OPTION){	
					//store path of directory to save it to
					String outputPathName = fileChooser.getSelectedFile().toString();
	
					File out = new File(outputPathName);
					boolean cancelled = false;
					//if output name specified exists loop until user selects to overwrite or exits filechooser
					while (out.exists()){
						int result2 = JOptionPane.showConfirmDialog(null, "file already exists. Would you like to overwrite?", "", 0);
						if (result2 == JOptionPane.YES_OPTION){
							break;
						} else {
							int result3 = fileChooser.showSaveDialog(this);
							if (result3 == JFileChooser.APPROVE_OPTION){	
								//store path of directory to save it to
								outputPathName = fileChooser.getSelectedFile().toString();								
								out = new File(outputPathName);
							} else if (result3 == JFileChooser.CANCEL_OPTION){
								cancelled = true;
								break;
							}
						}
					}
			
					//only initialise and execute swingworker is filechooser has exited without being deliberately exited/cancelled
					if (cancelled == false){
						SubtitleAdder adder = new SubtitleAdder(mediaPath, srtField.getText(), outputPathName);
						adder.execute();
					}
				}
			}
		}
	}
}

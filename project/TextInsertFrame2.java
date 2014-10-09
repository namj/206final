package project;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class TextInsertFrame2 extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField _startField, _endField;
	private JTextArea _textField;
	private JButton _previewBtn, _generateBtn;
	private JComboBox<String> _fontBox, _colourBox, _sizeBox; 
	private EmbeddedMediaPlayer _currentVideo;
	private String _mediaPath;
	private int _startTime, _endTime;


	/**
	 * Create the frame.
	 */
	public TextInsertFrame2(String mediaPath, EmbeddedMediaPlayer currentVideo) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[15.00%][35.00%][25.00%][25.00%]", "[][grow][][][][]"));
		
		_mediaPath = mediaPath;
		_currentVideo = currentVideo;
		
		String[] fonts = {"FreeMono.ttf", "Kinnari.ttf", "Purisa-Oblique.ttf", "TakaoPGothic.ttf", "TlwgTypist-Bold.ttf", "Ubuntu-M.ttf"};
		String[] sizes = { "10" , "20", "30", "40", "50", "60"};
		String[] colours = { "black", "white", "red", "blue", "yellow", "green", "purple", "orange" };
		
		JLabel lblNewLabel = new JLabel("New label");
		contentPane.add(lblNewLabel, "cell 0 0");
		
		_previewBtn = new JButton("Preview");
		contentPane.add(_previewBtn, "cell 3 0,alignx right");
		
		_textField = new JTextArea();
		contentPane.add(_textField, "cell 0 1 4 1,grow");
		
		JLabel lblNewLabel_1 = new JLabel("Font    ");
		contentPane.add(lblNewLabel_1, "flowx,cell 0 2,alignx left");
		
		_fontBox = new JComboBox<String>(fonts);
		contentPane.add(_fontBox, "cell 1 2,growx");
		
		JLabel lblNewLabel_4 = new JLabel("Start time  ");
		contentPane.add(lblNewLabel_4, "flowx,cell 2 2,alignx right");
		
		_startField = new JTextField("00:00:00");
		contentPane.add(_startField, "cell 3 2");
		_startField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Colour\t");
		contentPane.add(lblNewLabel_2, "flowx,cell 0 3,alignx left");
		
		_colourBox = new JComboBox<String>(colours);
		contentPane.add(_colourBox, "cell 1 3,growx");
		
		JLabel lblNewLabel_5 = new JLabel("Finish time ");
		contentPane.add(lblNewLabel_5, "flowx,cell 2 3,alignx right");
		
		_endField = new JTextField("00:00:00");
		contentPane.add(_endField, "cell 3 3");
		_endField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Size    ");
		contentPane.add(lblNewLabel_3, "flowx,cell 0 4,alignx left");
		
		_sizeBox = new JComboBox<String>(sizes);
		contentPane.add(_sizeBox, "cell 1 4,growx");
		
		_generateBtn = new JButton("Generate");
		_generateBtn.addActionListener(this);
		contentPane.add(_generateBtn, "cell 1 5 2 1,alignx center,growy");
	}


	@Override
	public void actionPerformed(ActionEvent e) { 
		//when generate button has been pushed
		if (e.getSource() == _generateBtn ){
			
			//Make sure user entered time formats correctly.
			Boolean _formatCorrect = true;
			String start = _startField.getText();
			String finish = _endField.getText();
			//make sure length is 8, and check for numbers and : at right indexes
			if (start.length() == 8){
				for (int i = 0; i<start.length(); i++){
					if (i == 2 || i == 5){
						if(start.charAt(i) != ':'){
							_formatCorrect = false;
							break;
						}
					}
					else {
						if (!Character.isDigit(start.charAt(i))){
							_formatCorrect = false;
							break;
						}
					}
				}
			}else{
				_formatCorrect = false;
			}
			//make sure length is 8, and check for numbers and : at right indexes
			if (finish.length() == 8){
				for (int i = 0; i<finish.length(); i++){
					if (i == 2 || i == 5){
						if(finish.charAt(i) != ':'){
							_formatCorrect = false;
							break;
						}
					}
					else {
						if (!Character.isDigit(finish.charAt(i))){
							_formatCorrect = false;
							break;
						}
					}
				}
			}else{
				_formatCorrect = false;
			}
			
			//complain to user if time given in wrong format
			if (_formatCorrect == false){
				JOptionPane.showMessageDialog(null, "Format of time(s) specified is incorrect");
			}
			
			//if outputfield is blank complain to user
			if (_textField.getText().length() == 0){
				JOptionPane.showMessageDialog(null, "Output name field is blank");
			}
			
			Boolean _everythingElseCorrect = true;
			if (_formatCorrect == true){
				// if time formats are given correctly, then make sure time given is within video length range
				//acquire video length in seconds first
				int length = (int) (_currentVideo.getLength()/1000);
				
				//convert user given time format which will be hrs:mins:secs, into seconds.
				int hoursToSecs = Integer.parseInt(_startField.getText().substring(0,2)) * 3600;
				int minsToSecs = Integer.parseInt(_startField.getText().substring(3, 5)) * 60;
				int secsToSecs = Integer.parseInt(_startField.getText().substring(6));
				
				int hoursToSecs2 = Integer.parseInt(_endField.getText().substring(0,2)) * 3600;
				int minsToSecs2 = Integer.parseInt(_endField.getText().substring(3, 5)) * 60;
				int secsToSecs2 = Integer.parseInt(_endField.getText().substring(6));
				
				//sum the seconds
				_startTime = hoursToSecs + minsToSecs + secsToSecs;
				_endTime = hoursToSecs2 + minsToSecs2 + secsToSecs2;
				
				//if start time is less than 0 or longer than video length
				if (_startTime < 0 || _startTime > length){
					JOptionPane.showMessageDialog(null, "Start time specifed isnt within range of video length");
					_everythingElseCorrect = false;
				}
				//if end time is less thatn 0 or longer than video length
				if (_endTime < 0 || _endTime > length){
					JOptionPane.showMessageDialog(null, "End time specifed isnt within range of video length");
					_everythingElseCorrect = false;
				}
				//if start time is equal or longer than end time
				if (_startTime >= _endTime){
					JOptionPane.showMessageDialog(null, "Start time must be less than end time");
					_everythingElseCorrect = false;
				}
				
			}
			
			if(_formatCorrect == true && _everythingElseCorrect == true && _textField.getText().length() > 0){
				//initialise and execute swing worker to insert text.
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select directory to save video to");
				int result = fileChooser.showSaveDialog(this);
				if (result == JFileChooser.APPROVE_OPTION){	
					//store path of directory to save it to
					String savePath = fileChooser.getCurrentDirectory().getAbsolutePath();
					String outputPathName = fileChooser.getSelectedFile().toString();
					System.out.println(savePath);

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
						
						TextInserter inserter = new TextInserter(2, _mediaPath, savePath, outputPathName, _textField.getText(), 
								_fontBox, _sizeBox, _colourBox, _startTime, _endTime);
						inserter.execute();
						
					}
				}
			}
		
			
		}
		
	}
	
	

}

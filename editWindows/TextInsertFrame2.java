package editWindows;

import helperClasses.Logger;
import helperClasses.TextManager;
import helperClasses.TimeFormatChecker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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

import longTaskProcessors.Previewer;
import longTaskProcessors.TextInserter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import javax.swing.JScrollPane;

/**
 * This frame allows user to insert text anywhere on the video for
 * specified inteval
 * @author namjun
 *
 */
public class TextInsertFrame2 extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField _startField, _endField;
	private JTextArea _textField;
	private JButton _previewBtn, _generateBtn;
	private JComboBox<String> _fontBox, _colourBox, _sizeBox; 
	private EmbeddedMediaPlayer _currentVideo;
	private String _mediaPath;
	private int _startTime, _endTime;

	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	private JScrollPane scrollPane;
	
	/**
	 * Create the frame.
	 */
	public TextInsertFrame2(String mediaPath, EmbeddedMediaPlayer currentVideo) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds((_screenWidth-450)/2,(_screenHeight-300)/2, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[15.00%][35.00%][25.00%][25.00%]", "[][grow][][][][]"));
		
		_mediaPath = mediaPath;
		_currentVideo = currentVideo;
		
		
		//create string arrays to be used for comboBoxes.
		String[] fonts = {"FreeMono.ttf", "Kinnari.ttf", "Purisa-Oblique.ttf", "TakaoPGothic.ttf", "TlwgTypist-Bold.ttf", "Ubuntu-M.ttf"};
		String[] sizes = { "10" , "20", "30", "40", "50", "60"};
		String[] colours = { "black", "white", "red", "blue", "yellow", "green", "pink", "orange" };
		
		//instantiate and setup comboBoxes
		_fontBox = new JComboBox<String>(fonts);
		_fontBox.addActionListener(this);
		_colourBox = new JComboBox<String>(colours);
		_colourBox.addActionListener(this);
		_sizeBox = new JComboBox<String>(sizes);
		_sizeBox.addActionListener(this);
		
		//add relevant components to the frame.
		JLabel lblNewLabel = new JLabel("New label");
		contentPane.add(lblNewLabel, "cell 0 0");
		
		_previewBtn = new JButton("Preview");
		_previewBtn.addActionListener(this);
		contentPane.add(_previewBtn, "cell 3 0,alignx right");
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 1 4 1,grow");
		
		_textField = new JTextArea();
		scrollPane.setViewportView(_textField);
		_textField.setDocument(new TextManager(220));
		_textField.setLineWrap(true);
		_textField.setWrapStyleWord(true);
		Logger.getInstance().pullTextForInsert(_textField);
		
		JLabel lblNewLabel_1 = new JLabel("Font    ");
		contentPane.add(lblNewLabel_1, "flowx,cell 0 2,alignx left");
	
		_fontBox.setSelectedIndex(Logger.getInstance().pullfontIndexForInsert());
		contentPane.add(_fontBox, "cell 1 2,growx");
		
		JLabel lblNewLabel_4 = new JLabel("Start time  ");
		contentPane.add(lblNewLabel_4, "flowx,cell 2 2,alignx right");
		
		_startField = new JTextField(Logger.getInstance().pullStartTimeForInsert());
		contentPane.add(_startField, "cell 3 2");
		_startField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Colour\t");
		contentPane.add(lblNewLabel_2, "flowx,cell 0 3,alignx left");
		
		_colourBox.setSelectedIndex(Logger.getInstance().pullColourIndexForInsert());
		contentPane.add(_colourBox, "cell 1 3,growx");
		
		JLabel lblNewLabel_5 = new JLabel("Finish time ");
		contentPane.add(lblNewLabel_5, "flowx,cell 2 3,alignx right");
		
		_endField = new JTextField(Logger.getInstance().pullFinishTimeForInsert());
		contentPane.add(_endField, "cell 3 3");
		_endField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Size    ");
		contentPane.add(lblNewLabel_3, "flowx,cell 0 4,alignx left");
		
		_sizeBox.setSelectedIndex(Logger.getInstance().pullSizeIndexForInsert());
		contentPane.add(_sizeBox, "cell 1 4,growx");
		
		_generateBtn = new JButton("Generate");
		_generateBtn.addActionListener(this);
		contentPane.add(_generateBtn, "cell 1 5 2 1,alignx center,growy");
		
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) { 
		
		//if comboBoxes for font,size,colour has been used, update the font/text on the textArea.
		if (e.getSource() == _fontBox || e.getSource() == _sizeBox){
			
			try {
				_textField.setFont(Font.createFont(Font.TRUETYPE_FONT, new File ("./fonts/"+_fontBox.getSelectedItem().toString())));
				_textField.setFont(_textField.getFont().deriveFont(Font.PLAIN, Integer.parseInt(_sizeBox.getSelectedItem().toString())));
			} catch (FontFormatException | IOException e1) {
				e1.printStackTrace();
			} 
			
		} else if (e.getSource() == _colourBox) {
			
			if (_colourBox.getSelectedIndex() == 0){
				_textField.setForeground(Color.black);
			} else if (_colourBox.getSelectedIndex() == 1){
				_textField.setForeground(Color.white);
			} else if (_colourBox.getSelectedIndex() == 2){
				_textField.setForeground(Color.red);
			} else if (_colourBox.getSelectedIndex() == 3){
				_textField.setForeground(Color.blue);
			} else if (_colourBox.getSelectedIndex() == 4){
				_textField.setForeground(Color.yellow);
			} else if (_colourBox.getSelectedIndex() == 5){
				_textField.setForeground(Color.green);
			} else if (_colourBox.getSelectedIndex() == 6){
				_textField.setForeground(Color.pink);
			} else if (_colourBox.getSelectedIndex() == 7){
				_textField.setForeground(Color.orange);
			} 
			
		//when generate button has been pushed	
		} else if (e.getSource() == _generateBtn || e.getSource() == _previewBtn){
			
			//Make sure user entered time formats correctly.
			Boolean _formatCorrect = true;
			
			//use TimeFormateChecker to check formats of times are correct.
			TimeFormatChecker checker = new TimeFormatChecker(_startField, _endField, _textField, _currentVideo);
			_formatCorrect = checker.checkTimeFormat();
			System.out.println(_formatCorrect);
			
			if (e.getSource() == _generateBtn){
				//if(_formatCorrect == true && _everythingElseCorrect == true && _textField.getText().length() > 0){
				if(_formatCorrect == true && _textField.getText().length() > 0){
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
									_fontBox, _sizeBox, _colourBox, _startField, _endField);
							inserter.execute();
							
						}
					}
				}
			} else if (e.getSource() == _previewBtn){
				//for preview to occur, timeformats given must be correct and textArea (_textField) must not be empty
				if(_formatCorrect == true && _textField.getText().length() > 0){
					
					int _startTime = checker.getStartTime();
					int _endTime = checker.getEndTime();
					
					_endTime = _endTime - _startTime;
				
					Previewer p = new Previewer();
					p.viewTextOverlay(_mediaPath, _textField.getText(), _fontBox.getSelectedItem().toString(), _sizeBox.getSelectedItem().toString(), _colourBox.getSelectedItem().toString(), 
							Integer.toString(_startTime), Integer.toString(_endTime));
				}
			}
		}
		
	}

}

package project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class EffectInsertFrame extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField _startField;
	private JTextField _endField;
	private JLabel startLabel;
	private JLabel endLabel;
	private JLabel entireLengthLabel;
	private JRadioButton radio1, radio2;
	private JButton previewBtn;
	private JButton generateBtn;
	private JComboBox<String> effectComboBox;
	private String _mediaPath;
	private EmbeddedMediaPlayer _currentVideo;
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	private JLabel lblCommingSoon;

	/**
	 * Create the frame.
	 */
	public EffectInsertFrame(String mediaPath, EmbeddedMediaPlayer currentVideo) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds((_screenWidth-450)/2,(_screenHeight-300)/2, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[20%][5%][25%][20%][5%][45%]", "[][20%][20%][][20%][20%][20%]"));
		
		String [] effects = { "Mirror effect", "Bouncing screen" };
		
		_mediaPath = mediaPath;
		_currentVideo = currentVideo;
		
		previewBtn = new JButton("Preview");
		previewBtn.addActionListener(this);
		contentPane.add(previewBtn, "cell 5 0,alignx right,aligny top");
		
		JLabel lblSelectFilter = new JLabel("Select effect");
		contentPane.add(lblSelectFilter, "cell 0 1 6 1,alignx center,aligny bottom");
		
		effectComboBox = new JComboBox<String>(effects);
		contentPane.add(effectComboBox, "flowx,cell 0 2 6 1,alignx center,aligny top");
		
		radio1 = new JRadioButton("\n");
		radio1.setEnabled(false);
		radio1.addActionListener(this);
		
		lblCommingSoon = new JLabel("Comming Soon!");
		contentPane.add(lblCommingSoon, "cell 1 3 3 1");
		contentPane.add(radio1, "cell 1 4,alignx center,aligny bottom");
		
		_startField = new JTextField();
		_startField.setText("00:00:00");
		_startField.setEnabled(false);
		contentPane.add(_startField, "cell 2 4,growx,aligny bottom");
		_startField.setColumns(10);
		
		radio2 = new JRadioButton("");
		radio2.addActionListener(this);
		radio2.setSelected(true);
		contentPane.add(radio2, "cell 4 4,aligny bottom");
		
		entireLengthLabel = new JLabel("Entire length");
		contentPane.add(entireLengthLabel, "cell 5 4,growx,aligny bottom");
		
		startLabel = new JLabel("Start time");
		startLabel.setEnabled(false);
		contentPane.add(startLabel, "cell 3 4,alignx left,aligny bottom");
		
		_endField = new JTextField();
		_endField.setText("00:00:00");
		_endField.setEnabled(false);
		contentPane.add(_endField, "cell 2 5,growx,aligny top");
		_endField.setColumns(10);
		
		endLabel = new JLabel("End time");
		endLabel.setEnabled(false);
		contentPane.add(endLabel, "cell 3 5,alignx left,aligny top");
		
		generateBtn = new JButton("Generate");
		generateBtn.addActionListener(this);
		contentPane.add(generateBtn, "cell 0 6 6 1,alignx center");
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == radio1 || e.getSource() == radio2) {
			
			if (e.getSource() == radio1) {
				radio1.setSelected(true);
				radio2.setSelected(false);
				
				startLabel.setEnabled(true);
				_startField.setEnabled(true);
				endLabel.setEnabled(true);
				_endField.setEnabled(true);
				entireLengthLabel.setEnabled(false);
				
			} else {
				radio1.setSelected(false);
				radio2.setSelected(true);
				
				startLabel.setEnabled(false);
				_startField.setEnabled(false);
				endLabel.setEnabled(false);
				_endField.setEnabled(false);
				entireLengthLabel.setEnabled(true);
			}
			
		}
		
		if (e.getSource() == previewBtn || e.getSource() == generateBtn) {

			if(e.getSource() == previewBtn){
				if (radio1.isSelected()){
					//Make sure user entered time formats correctly.
					Boolean _formatCorrect = true;
			
					TimeFormatChecker checker = new TimeFormatChecker(_startField, _endField, _currentVideo);
					_formatCorrect = checker.checkTimeFormat();
					
					if (_formatCorrect == true){
						int _startTime = checker.getStartTime();
						int _endTime = checker.getEndTime();
						
						_endTime = _endTime - _startTime;
					
						Previewer p = new Previewer();
						p.viewEffectMirror(_mediaPath, _startTime, _endTime );
					}
					
				} else {
					
					Previewer p = new Previewer();
					p.viewEffectMirror(_mediaPath);

				}
				
			} else if (e.getSource() == generateBtn){
				
				if (radio1.isSelected()){
					//Make sure user entered time formats correctly.
					Boolean _formatCorrect = true;
			
					TimeFormatChecker checker = new TimeFormatChecker(_startField, _endField, _currentVideo);
					_formatCorrect = checker.checkTimeFormat();
					
					if (_formatCorrect == true){
						
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
								
								int _startTime = checker.getStartTime();
								int _endTime = checker.getEndTime();
						
								_endTime = _endTime - _startTime;
					
								Previewer p = new Previewer();
								p.viewEffectMirror(_mediaPath, _startTime, _endTime );
								
							}
						}
						
					}
					
				} else {
					
					Previewer p = new Previewer();
					p.viewEffectMirror(_mediaPath);

				}
				
			}

		}
		
	}
	
	
	

}

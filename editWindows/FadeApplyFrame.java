package editWindows;


import helperClasses.TimeFormatChecker;

import java.awt.Dimension;
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
import javax.swing.JCheckBox;

import longTaskProcessors.EffectInserter;
import longTaskProcessors.Previewer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class FadeApplyFrame extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JButton previewBtn;
	private JButton generateBtn;
	private JComboBox<String> effectComboBox;
	private String _mediaPath;
	private EmbeddedMediaPlayer _currentVideo;
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	private JLabel lblNewLabel;
	private JCheckBox inCheckBox;
	private JCheckBox outCheckBox;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JTextField inStartField;
	private JTextField outStartField;
	private JTextField inFinishField;
	private JTextField outFinishField;

	/**
	 * Create the frame.
	 */
	public FadeApplyFrame(String mediaPath, EmbeddedMediaPlayer currentVideo) {
		
		//INITILISE FRAME, setbounds, layout etc
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds((_screenWidth-450)/2,(_screenHeight-250)/2, 450, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[25%,grow][25%,grow][25%,grow][25%,grow]", "[][20%][20%][20%][20%]"));
		
		//assign _medipath and _currenvideo objs from constructor
		_mediaPath = mediaPath;
		_currentVideo = currentVideo;
		
		//add preview button
		previewBtn = new JButton("Preview");
		previewBtn.addActionListener(this);
		contentPane.add(previewBtn, "cell 3 0,alignx right,aligny top");
		
		//add relevant components such as labels, text fields, buttons etc
		JLabel lblSelectFilter = new JLabel("Fade In");
		contentPane.add(lblSelectFilter, "flowx,cell 0 1,alignx right,aligny bottom");
		
		generateBtn = new JButton("Generate");
		generateBtn.addActionListener(this);
		
		lblNewLabel = new JLabel("Fade Out\n");
		contentPane.add(lblNewLabel, "flowx,cell 2 1,alignx right,aligny bottom");
		
		lblNewLabel_1 = new JLabel("Start time");
		contentPane.add(lblNewLabel_1, "cell 0 2,alignx trailing,aligny bottom");
		
		inStartField = new JTextField();
		inStartField.setText("00:00:00");
		contentPane.add(inStartField, "cell 1 2,alignx left,aligny bottom");
		inStartField.setColumns(6);
		
		lblNewLabel_3 = new JLabel("Start time");
		contentPane.add(lblNewLabel_3, "cell 2 2,alignx trailing,aligny bottom");
		
		outStartField = new JTextField();
		outStartField.setText("00:00:00");
		contentPane.add(outStartField, "cell 3 2,alignx left,aligny bottom");
		outStartField.setColumns(6);
		
		lblNewLabel_2 = new JLabel("Finish time");
		contentPane.add(lblNewLabel_2, "cell 0 3,alignx trailing,aligny top");
		
		inFinishField = new JTextField();
		inFinishField.setText("00:00:00");
		contentPane.add(inFinishField, "cell 1 3,alignx left,aligny top");
		inFinishField.setColumns(6);
		
		lblNewLabel_4 = new JLabel("Finish time");
		contentPane.add(lblNewLabel_4, "cell 2 3,alignx trailing,aligny top");
		
		outFinishField = new JTextField();
		outFinishField.setText("00:00:00");
		contentPane.add(outFinishField, "cell 3 3,alignx left,aligny top");
		outFinishField.setColumns(6);
		contentPane.add(generateBtn, "cell 0 4 4 1,alignx center,growy");
		
		//add checkboxes to indicate whether to perform fade in/out or both
		inCheckBox = new JCheckBox("");
		contentPane.add(inCheckBox, "cell 1 1,alignx left,aligny bottom");
		
		outCheckBox = new JCheckBox("");
		contentPane.add(outCheckBox, "cell 3 1,alignx left,aligny bottom");
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource() == previewBtn || e.getSource() == generateBtn) {
			
			//if either preview or generate button was pressed, first check whether the time formats given are valid.
			
			//if inCheckbox is ticked, check whether its start time is greater or equal to 00:00:00, and is less than video length.
			if (inCheckBox.isSelected()){
				//first, instantiate a time format checker.
				TimeFormatChecker checker = new TimeFormatChecker(inStartField, inFinishField, _currentVideo);
				checker.checkTimeFormat();
			}
			
			//if outCheckbox is ticked, check whether its start time is greater or equal to 00:00:00, and is less than video length.
			if (outCheckBox.isSelected()){
				//first, instantiate a time format checker.
				TimeFormatChecker checker = new TimeFormatChecker(inStartField, inFinishField, _currentVideo);
				checker.checkTimeFormat();
			}
			
			//make sure inFinishTime and outStartTime do not overlap.
			

			if(e.getSource() == previewBtn){
					
				Previewer p = new Previewer();
				if (effectComboBox.getSelectedIndex() == 0) {
					p.viewEffectRotate(_mediaPath, "1");
				} else if (effectComboBox.getSelectedIndex() == 1) {
					p.viewEffectRotate(_mediaPath, "1, transpose = 1");
				} else if (effectComboBox.getSelectedIndex() == 2) {
					p.viewEffectRotate(_mediaPath, "2");
				}

			} else if (e.getSource() == generateBtn){
				
					
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
						
						//instantiate and effectInserter object
						EffectInserter inserter = new EffectInserter();
						
						//use effectInserters method according which item has been selected in comboBox.
						if (effectComboBox.getSelectedIndex() == 0) {
							inserter.insertEffectRotate(_mediaPath, outputPathName, "1");
						} else if (effectComboBox.getSelectedIndex() == 1) {
							inserter.insertEffectRotate(_mediaPath, outputPathName, "1, transpose=1");
						} else if (effectComboBox.getSelectedIndex() == 2) {
							inserter.insertEffectRotate(_mediaPath, outputPathName, "2");
						}
						
					}
				}
				
			}

		}
		
	}

}

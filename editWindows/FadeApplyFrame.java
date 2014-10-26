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

/**
 * This class extends JFrame. Allows users to specify the times they would
 * like the fade effect to apply for
 * 
 * @author namjun
 *
 */
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
			//declare 2 timeFormat checkers which could be used.
			TimeFormatChecker inChecker = null, outChecker = null;
			
			boolean fadeInFormat = false;
			//if inCheckbox is ticked, check whether its start time is greater or equal to 00:00:00, and is less than video length.
			if (inCheckBox.isSelected()){
				//first, instantiate a time format checker.
				inChecker = new TimeFormatChecker(inStartField, inFinishField, _currentVideo);
				fadeInFormat = inChecker.checkTimeFormat();
			}
			
			boolean fadeOutFormat = false;
			//if outCheckbox is ticked, check whether its start time is greater or equal to 00:00:00, and is less than video length.
			if (outCheckBox.isSelected()){
				//first, instantiate a time format checker.
				outChecker = new TimeFormatChecker(outStartField, outFinishField, _currentVideo);
				fadeOutFormat = outChecker.checkTimeFormat();
			}
			
			//if neither checkboxes are ticked, complain to user.
			if (!inCheckBox.isSelected() && !outCheckBox.isSelected()){
				JOptionPane.showMessageDialog(null, "You must have atleast one of fade in or fade out ticked!");
			}
			
			//If both fade in and out were ticked and have correct formats, make sure inFinishTime and outStartTime do not overlap.
			if (fadeInFormat == true && fadeOutFormat == true){
				if (inChecker.getEndTime() >= outChecker.getStartTime()){
					//entering here means they overlapped. Alert user of this error and set the 2 booleans back to false so that process doenst go on
					JOptionPane.showMessageDialog(null, "Finish time of fade in and start time of fade out mustn't overlap!");
					fadeInFormat = false;
					fadeOutFormat = false;
				}
			}
			
			//Now, further process should only continue if: 1)inCheckbox is ticked, and inTimeFormat is correct. 2)other way around 3)both ticked both correct.
			//if formats and restraints on fade in/out times are correct.
			
			//this int represents one of the 3 situations described above. default value is -1.
			int situation = -1;
			
			if (inCheckBox.isSelected() && fadeInFormat == true && !outCheckBox.isSelected()){
				situation = 1;
			} else if (outCheckBox.isSelected() && fadeOutFormat == true && !inCheckBox.isSelected()) {
				situation = 2;
			} else if (inCheckBox.isSelected() && fadeInFormat == true && outCheckBox.isSelected() && fadeOutFormat == true) {
				situation = 3;
			}
			
			if (situation != -1){
				if(e.getSource() == previewBtn){
						
					if (situation == 1){
						//preview fade in
						Previewer p = new Previewer();
						p.viewEffectFadeIn(_mediaPath, getNumberOfFrames(inStartField), getNumberOfFrames(inFinishField) - getNumberOfFrames(inStartField));
					} else if (situation == 2){
						//preview fade out
						Previewer p = new Previewer();
						p.viewEffectFadeOut(_mediaPath, getNumberOfFrames(outStartField), getNumberOfFrames(outFinishField) - getNumberOfFrames(outStartField));
					} else if (situation == 3){
						//preview fade in AND out
						Previewer p = new Previewer();
						p.viewEffectFadeIO(_mediaPath, getNumberOfFrames(inStartField), getNumberOfFrames(inFinishField) - getNumberOfFrames(inStartField), 
								getNumberOfFrames(outStartField), getNumberOfFrames(outFinishField) - getNumberOfFrames(outStartField));
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
							
							EffectInserter e1 = new EffectInserter();
							
							if (situation == 1){
								e1.insertEffectFadeIn(_mediaPath, outputPathName, getNumberOfFrames(inStartField), getNumberOfFrames(inFinishField) - getNumberOfFrames(inStartField));
							} else if (situation == 2){
								e1.insertEffectFadeOut(_mediaPath, outputPathName, getNumberOfFrames(outStartField), getNumberOfFrames(outFinishField) - getNumberOfFrames(outStartField));
							} else if (situation == 3){
								e1.insertEffectFadeIO(_mediaPath, outputPathName, getNumberOfFrames(inStartField), getNumberOfFrames(inFinishField) - getNumberOfFrames(inStartField), 
										getNumberOfFrames(outStartField), getNumberOfFrames(outFinishField) - getNumberOfFrames(outStartField));
							}
							
						}
					}
					
				}
			}
		}
		
	}
	
	/**
	 * converts the text(in format 00:00:00) inside the textfield given as parameter into seconds 
	 * and returns it
	 * @param field
	 * @return
	 */
	private int getNumberOfFrames(JTextField field) { 
		//get the current input video's fps
		int frameRate = (int)_currentVideo.getFps();
		String[] parts = field.getText().split(":");
		int seconds = Integer.parseInt(parts[0])*3600 + Integer.parseInt(parts[1])*60 + Integer.parseInt(parts[2]);
		int frameNumber = seconds * frameRate;
		return frameNumber;
	}

}

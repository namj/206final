package project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class EffectInsertFrame extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField _startField;
	private JTextField _endField;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JButton previewBtn;
	private JButton generateBtn;
	private JComboBox<String> effectComboBox;
	private String _mediaPath;
	private EmbeddedMediaPlayer _currentVideo;
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();

	/**
	 * Create the frame.
	 */
	public EffectInsertFrame(String mediaPath, EmbeddedMediaPlayer currentVideo) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds((_screenWidth-450)/2,(_screenHeight-300)/2, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[20%][5%][25%][20%][5%][45%]", "[][20%][20%][20%][20%][20%]"));
		
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
		
		rdbtnNewRadioButton = new JRadioButton("\n");
		contentPane.add(rdbtnNewRadioButton, "cell 1 3,alignx center,aligny bottom");
		
		_startField = new JTextField();
		_startField.setText("00:00:00");
		contentPane.add(_startField, "cell 2 3,growx,aligny bottom");
		_startField.setColumns(10);
		
		rdbtnNewRadioButton_1 = new JRadioButton("");
		contentPane.add(rdbtnNewRadioButton_1, "cell 4 3,aligny bottom");
		
		lblNewLabel_2 = new JLabel("Entire length");
		contentPane.add(lblNewLabel_2, "cell 5 3,growx,aligny bottom");
		
		lblNewLabel = new JLabel("Start time");
		contentPane.add(lblNewLabel, "cell 3 3,alignx left,aligny bottom");
		
		_endField = new JTextField();
		_endField.setText("00:00:00");
		contentPane.add(_endField, "cell 2 4,growx,aligny top");
		_endField.setColumns(10);
		
		lblNewLabel_1 = new JLabel("End time");
		contentPane.add(lblNewLabel_1, "cell 3 4,alignx left,aligny top");
		
		generateBtn = new JButton("Generate");
		generateBtn.addActionListener(this);
		contentPane.add(generateBtn, "cell 0 5 6 1,alignx center");
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == previewBtn || e.getSource() == generateBtn) {
			
			//Make sure user entered time formats correctly.
			Boolean _formatCorrect = true;
			
			TimeFormatChecker checker = new TimeFormatChecker(_startField, _endField, _currentVideo);
			_formatCorrect = checker.checkTimeFormat();
			
			
			if(_formatCorrect == true){
				
				int _startTime = checker.getStartTime();
				int _endTime = checker.getEndTime();
				
				_endTime = _endTime - _startTime;
			
				Previewer p = new Previewer();
				p.viewEffectMirror(_mediaPath, _startTime, _endTime );
			}

		}
		
	}
	
	
	

}

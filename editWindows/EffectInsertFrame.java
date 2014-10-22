package editWindows;

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

import longTaskProcessors.EffectInserter;
import longTaskProcessors.Previewer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class EffectInsertFrame extends JFrame implements ActionListener{

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

	/**
	 * Create the frame.
	 */
	public EffectInsertFrame(String mediaPath, EmbeddedMediaPlayer currentVideo) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds((_screenWidth-450)/2,(_screenHeight-300)/2, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[33%][34%][33%]", "[][20%][20%][20%][20%]"));
		
		String [] effects = { "Mirror effect", "Bouncing screen" };
		
		_mediaPath = mediaPath;
		_currentVideo = currentVideo;
		
		previewBtn = new JButton("Preview");
		previewBtn.addActionListener(this);
		contentPane.add(previewBtn, "cell 2 0,alignx right,aligny top");
		
		JLabel lblSelectFilter = new JLabel("Select effect an effect to apply onto video!");
		contentPane.add(lblSelectFilter, "cell 0 1 3 1,alignx center,aligny bottom");
		
		effectComboBox = new JComboBox<String>();
		contentPane.add(effectComboBox, "flowx,cell 1 2,alignx center,aligny center");
		
		generateBtn = new JButton("Generate");
		generateBtn.addActionListener(this);
		contentPane.add(generateBtn, "cell 1 4,grow");
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource() == previewBtn || e.getSource() == generateBtn) {

			if(e.getSource() == previewBtn){
					
				Previewer p = new Previewer();
				if (effectComboBox.getSelectedIndex() == 0){
					p.viewEffectMirror(_mediaPath);
				} else if (effectComboBox.getSelectedIndex() == 1) {
					p.viewEffectBounce(_mediaPath);
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
						System.out.println("why?");
						EffectInserter inserter = new EffectInserter();
						System.out.println(effectComboBox.getSelectedIndex());
						if (effectComboBox.getSelectedIndex() == 0){
							inserter.InsertEffectMirror(_mediaPath, outputPathName);
						} else if (effectComboBox.getSelectedIndex() == 1) {
							inserter.InsertEffectBounce(_mediaPath, outputPathName);
						}
					}
				}
				
			}

		}
		
	}

}

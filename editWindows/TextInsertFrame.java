package editWindows;

import helperClasses.Logger;
import helperClasses.TextManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JScrollPane;

import longTaskProcessors.Previewer;
import longTaskProcessors.TextInserter;

/**
 * This frame allows user to specify text, time, font options to be 
 * inserted on their video
 * @author Namjun Park npar350
 *
 */
public class TextInsertFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	
	private JLabel lblNewLabel, timeLabel1, fontLabel1, colourLabel1, sizeLabel1, lblNewLabel_1, timeLabel2, fontLabel2, colourLabel2, sizeLabel2;
	private JComboBox<String> timeBox1, fontBox1, colourBox1, sizeBox1, timeBox2, fontBox2, colourBox2, sizeBox2;
	private JButton previewBtn1, previewBtn2, generateBtn;
	private JTextArea textArea1, textArea2;
	private JRadioButton checkBtn1, checkBtn2;
	private String _selectedVid;
	private EmbeddedMediaPlayer _currentVideo;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	/**
	 * create the frame
	 * @param mediaPath
	 * @param currentVideo
	 */
	public TextInsertFrame(String mediaPath, EmbeddedMediaPlayer currentVideo) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds((_screenWidth-780)/2,(_screenHeight-510)/2, 780, 510);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][grow][grow][]", "[][grow][][][grow][][]"));
		
		//set up relevant string arrays for the comobBoxes
		String[] fonts = {"FreeMono.ttf", "Kinnari.ttf", "Purisa-Oblique.ttf", "TakaoPGothic.ttf", "TlwgTypist-Bold.ttf", "Ubuntu-M.ttf"};
		String[] sizes = { "10" , "20", "30", "40", "50", "60"};
		String[] colours = { "black", "white", "red", "blue", "yellow", "green", "pink", "orange" };
		String[] times = { "5" , "10" , "15" , "20" , "25" , "30" };
		
		//instantiate/setup comboBoxes
		timeBox1 = new JComboBox<String>(times);
		timeBox1.setSelectedIndex(Logger.getInstance().pullTimeIndexForInsertTop());
		fontBox1 = new JComboBox<String>(fonts);
		fontBox1.addActionListener(this);
		sizeBox1 = new JComboBox<String>(sizes);
		sizeBox1.addActionListener(this);
		colourBox1 = new JComboBox<String>(colours);
		colourBox1.addActionListener(this);
		
		//instantiate/setup comboBoxes
		timeBox2 = new JComboBox<String>(times);
		timeBox2.setSelectedIndex(Logger.getInstance().pullTimeIndexForInsertBot());
		fontBox2 = new JComboBox<String>(fonts);
		fontBox2.addActionListener(this);
		sizeBox2 = new JComboBox<String>(sizes);
		sizeBox2.addActionListener(this);
		colourBox2 = new JComboBox<String>(colours);
		colourBox2.addActionListener(this);

		_selectedVid = mediaPath;
		_currentVideo = currentVideo;
		
		//instantiate labels,buttons etc for this frame and add components to this frame/layout
		lblNewLabel = new JLabel("Beginning");
		contentPane.add(lblNewLabel, "flowx,cell 0 0,alignx left");
		
		timeLabel1 = new JLabel("Time(sec)");
		contentPane.add(timeLabel1, "flowx,cell 1 0,alignx right");
		
		contentPane.add(timeBox1, "cell 1 0,alignx right");
		
		previewBtn1 = new JButton("Preview");
		previewBtn1.addActionListener(this);
		contentPane.add(previewBtn1, "cell 2 0,alignx right");
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 1 3 1,grow");
		
		textArea1 = new JTextArea();
		scrollPane.setViewportView(textArea1);
		textArea1.setDocument(new TextManager(220));
		textArea1.setLineWrap(true);
		textArea1.setWrapStyleWord(true);
		Logger.getInstance().pullTextForInsertTop(textArea1);
		
		checkBtn1 = new JRadioButton("");
		checkBtn1.setSelected(true);
		checkBtn1.addActionListener(this);
		contentPane.add(checkBtn1, "cell 3 1,aligny top");
		
		fontLabel1 = new JLabel("font");
		contentPane.add(fontLabel1, "flowx,cell 0 2,aligny top");
		
		fontBox1.setSelectedIndex(Logger.getInstance().pullfontIndexForInsertTop());
		contentPane.add(fontBox1, "cell 0 2,growx");
		
		colourLabel1 = new JLabel("colour");
		contentPane.add(colourLabel1, "flowx,cell 1 2");
		
		colourBox1.setSelectedIndex(Logger.getInstance().pullColourIndexForInsertTop());
		contentPane.add(colourBox1, "cell 1 2,growx");
		
		sizeLabel1 = new JLabel("size");
		contentPane.add(sizeLabel1, "flowx,cell 2 2");
		
		sizeBox1.setSelectedIndex(Logger.getInstance().pullSizeIndexForInsertTop());
		contentPane.add(sizeBox1, "cell 2 2,growx");
		
		lblNewLabel_1 = new JLabel("End");
		contentPane.add(lblNewLabel_1, "cell 0 3,alignx left");
		
		timeLabel2 = new JLabel("Time(sec)");
		contentPane.add(timeLabel2, "flowx,cell 1 3,alignx right");
		
		contentPane.add(timeBox2, "cell 1 3,alignx right");
		
		previewBtn2 = new JButton("Preview");
		previewBtn2.addActionListener(this);
		contentPane.add(previewBtn2, "cell 2 3,alignx right");
		
		scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1, "cell 0 4 3 1,grow");
		
		textArea2 = new JTextArea();
		scrollPane_1.setViewportView(textArea2);
		textArea2.setDocument(new TextManager(220));
		textArea2.setLineWrap(true);
		textArea2.setWrapStyleWord(true);
		Logger.getInstance().pullTextForInsertBot(textArea2);
		
		checkBtn2 = new JRadioButton("");
		checkBtn2.setSelected(true);
		checkBtn2.addActionListener(this);
		contentPane.add(checkBtn2, "cell 3 4,aligny top");
		
		fontLabel2 = new JLabel("font");
		contentPane.add(fontLabel2, "flowx,cell 0 5");
	
		fontBox2.setSelectedIndex(Logger.getInstance().pullfontIndexForInsertBot());
		contentPane.add(fontBox2, "cell 0 5,growx");
		
		colourLabel2 = new JLabel("colour");
		contentPane.add(colourLabel2, "flowx,cell 1 5");
		
		colourBox2.setSelectedIndex(Logger.getInstance().pullColourIndexForInsertBot());
		contentPane.add(colourBox2, "cell 1 5,growx");
		
		sizeLabel2 = new JLabel("size");
		contentPane.add(sizeLabel2, "flowx,cell 2 5");
		
		sizeBox2.setSelectedIndex(Logger.getInstance().pullSizeIndexForInsertBot());
		contentPane.add(sizeBox2, "cell 2 5,growx");
		
		generateBtn = new JButton("Generate");
		generateBtn.setFont(new Font("Dialog", Font.PLAIN, 25));
		generateBtn.addActionListener(this);
		contentPane.add(generateBtn, "cell 0 6 4 1,alignx center");
		
		setVisible(true);
		
	}
	
	/**
	 * actionPerformed method overridden to react to button presses
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//if combobox for font,colour,size has been used, update font shown on textarea
		if (e.getSource() == fontBox1 || e.getSource() == sizeBox1){
			
			try {
				InputStream is = this.getClass().getResourceAsStream("resources/"+fontBox1.getSelectedItem().toString());
				textArea1.setFont(Font.createFont(Font.TRUETYPE_FONT, is));
				textArea1.setFont(textArea1.getFont().deriveFont(Font.PLAIN, Integer.parseInt(sizeBox1.getSelectedItem().toString())));
			} catch (FontFormatException | IOException e1) {
				e1.printStackTrace();
			} 
			
		} else if (e.getSource() == colourBox1) {
			
			if (colourBox1.getSelectedIndex() == 0){
				textArea1.setForeground(Color.black);
			} else if (colourBox1.getSelectedIndex() == 1){
				textArea1.setForeground(Color.white);
			} else if (colourBox1.getSelectedIndex() == 2){
				textArea1.setForeground(Color.red);
			} else if (colourBox1.getSelectedIndex() == 3){
				textArea1.setForeground(Color.blue);
			} else if (colourBox1.getSelectedIndex() == 4){
				textArea1.setForeground(Color.yellow);
			} else if (colourBox1.getSelectedIndex() == 5){
				textArea1.setForeground(Color.green);
			} else if (colourBox1.getSelectedIndex() == 6){
				textArea1.setForeground(Color.pink);
			} else if (colourBox1.getSelectedIndex() == 7){
				textArea1.setForeground(Color.orange);
			} 
			
		//if combobox for font,colour,size has been used, update font shown on textarea	
		} else if (e.getSource() == fontBox2 || e.getSource() == sizeBox2){
			
			try {
				InputStream is = this.getClass().getResourceAsStream("resources/"+fontBox2.getSelectedItem().toString());
				textArea2.setFont(Font.createFont(Font.TRUETYPE_FONT, is));
				textArea2.setFont(textArea2.getFont().deriveFont(Font.PLAIN, Integer.parseInt(sizeBox2.getSelectedItem().toString())));
			} catch (FontFormatException | IOException e1) {
				e1.printStackTrace();
			} 
			
		} else if (e.getSource() == colourBox2) {
			
			if (colourBox2.getSelectedIndex() == 0){
				textArea2.setForeground(Color.black);
			} else if (colourBox2.getSelectedIndex() == 1){
				textArea2.setForeground(Color.white);
			} else if (colourBox2.getSelectedIndex() == 2){
				textArea2.setForeground(Color.red);
			} else if (colourBox2.getSelectedIndex() == 3){
				textArea2.setForeground(Color.blue);
			} else if (colourBox2.getSelectedIndex() == 4){
				textArea2.setForeground(Color.yellow);
			} else if (colourBox2.getSelectedIndex() == 5){
				textArea2.setForeground(Color.green);
			} else if (colourBox2.getSelectedIndex() == 6){
				textArea2.setForeground(Color.pink);
			} else if (colourBox2.getSelectedIndex() == 7){
				textArea2.setForeground(Color.orange);
			} 
			
		} else if (e.getSource() == checkBtn1){
			
			if (!checkBtn1.isSelected()){
				//disable all components at top half
				previewBtn1.setEnabled(false);
				timeBox1.setEnabled(false);
				textArea1.setEnabled(false);
				fontBox1.setEnabled(false);
				colourBox1.setEnabled(false);
				sizeBox1.setEnabled(false);
			}else{
				//or enable them
				previewBtn1.setEnabled(true);
				timeBox1.setEnabled(true);
				textArea1.setEnabled(true);
				fontBox1.setEnabled(true);
				colourBox1.setEnabled(true);
				sizeBox1.setEnabled(true);
			}
				
		} else if (e.getSource() == checkBtn2){
			if (!checkBtn2.isSelected()){
				//disable all components at bottom half
				previewBtn2.setEnabled(false);
				timeBox2.setEnabled(false);
				textArea2.setEnabled(false);
				fontBox2.setEnabled(false);
				colourBox2.setEnabled(false);
				sizeBox2.setEnabled(false);
			}else{
				//or enable them
				previewBtn2.setEnabled(true);
				timeBox2.setEnabled(true);
				textArea2.setEnabled(true);
				fontBox2.setEnabled(true);
				colourBox2.setEnabled(true);
				sizeBox2.setEnabled(true);
			}
		} else if (e.getSource() == generateBtn){
			
			//show JFIlechooser for outputfile path
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
					
					TextInserter inserter = new TextInserter(1, _selectedVid, savePath, outputPathName, textArea1.getText(), fontBox1, sizeBox1, colourBox1, timeBox1,
							textArea2.getText(), fontBox2, sizeBox2, colourBox2, timeBox2, _currentVideo.getLength());
					inserter.execute();
				}
			}
			
		//if preview buttons have been clicked, show preview
		} else if (e.getSource() == previewBtn1){
			Previewer p = new Previewer();
			p.viewTextOverlay(_selectedVid, textArea1.getText(), fontBox1.getSelectedItem().toString(), sizeBox1.getSelectedItem().toString(), colourBox1.getSelectedItem().toString(), 
					"0", timeBox1.getSelectedItem().toString());
		} else if (e.getSource() == previewBtn2){
			Previewer p = new Previewer();
			
			int length = (int) (_currentVideo.getLength()/1000);
			String l = Integer.toString(length);
			int lengthMinusTime = length - Integer.parseInt(timeBox2.getSelectedItem().toString());
			String lMinusT = Integer.toString(lengthMinusTime);
			
			p.viewTextOverlay(_selectedVid, textArea2.getText(), fontBox2.getSelectedItem().toString(), sizeBox2.getSelectedItem().toString(), colourBox2.getSelectedItem().toString(), 
					lMinusT, l);
		
		}
		
	}

}

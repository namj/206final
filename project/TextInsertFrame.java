package project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class TextInsertFrame extends JFrame {

	private JPanel contentPane;
	JLabel lblNewLabel, timeLabel1, fontLabel1, colourLabel1, sizeLabel1, lblNewLabel_1, timeLabel2, fontLabel2, colourLabel2, sizeLabel2;
	JComboBox<String> timeBox1, fontBox1, colourBox1, sizeBox1, timeBox2, fontBox2, colourBox2, sizeBox2;
	JButton previewBtn1, previewBtn2, generateBtn;
	JTextArea textArea1, textArea2;
	JRadioButton checkBtn1, checkBtn2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextInsertFrame frame = new TextInsertFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TextInsertFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 784, 512);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][grow][grow][]", "[][grow][][][grow][][]"));
		
		String[] fonts = {"FreeMono.ttf", "Kinnari.ttf", "Purisa-Oblique.ttf", "TakaoPGothic.ttf", "TlwgTypist-Bold.ttf", "Ubuntu-M.ttf"};
		String[] sizes = { "10" , "20", "30", "40", "50", "60"};
		String[] colours = { "black", "white", "red", "blue", "yellow", "green", "purple", "orange" };
		String[] times = { "5" , "10" , "15" , "20" , "25" , "30" };

		
		lblNewLabel = new JLabel("Beginning");
		contentPane.add(lblNewLabel, "flowx,cell 0 0,alignx left");
		
		timeLabel1 = new JLabel("Time(sec)");
		contentPane.add(timeLabel1, "flowx,cell 1 0,alignx right");
		
		timeBox1 = new JComboBox<String>(times);
		contentPane.add(timeBox1, "cell 1 0,alignx right");
		
		previewBtn1 = new JButton("Preview");
		contentPane.add(previewBtn1, "cell 2 0,alignx right");
		
		textArea1 = new JTextArea();
		contentPane.add(textArea1, "cell 0 1 3 1,grow");
		
		checkBtn1 = new JRadioButton("");
		contentPane.add(checkBtn1, "cell 3 1,aligny top");
		
		fontLabel1 = new JLabel("font");
		contentPane.add(fontLabel1, "flowx,cell 0 2,aligny top");
		
		fontBox1 = new JComboBox<String>(fonts);
		contentPane.add(fontBox1, "cell 0 2,growx");
		
		colourLabel1 = new JLabel("colour");
		contentPane.add(colourLabel1, "flowx,cell 1 2");
		
		colourBox1 = new JComboBox<String>(colours);
		contentPane.add(colourBox1, "cell 1 2,growx");
		
		sizeLabel1 = new JLabel("size");
		contentPane.add(sizeLabel1, "flowx,cell 2 2");
		
		sizeBox1 = new JComboBox<String>(sizes);
		contentPane.add(sizeBox1, "cell 2 2,growx");
		
		lblNewLabel_1 = new JLabel("End");
		contentPane.add(lblNewLabel_1, "cell 0 3,alignx left");
		
		timeLabel2 = new JLabel("Time(sec)");
		contentPane.add(timeLabel2, "flowx,cell 1 3,alignx right");
		
		timeBox2 = new JComboBox<String>(times);
		contentPane.add(timeBox2, "cell 1 3,alignx right");
		
		previewBtn2 = new JButton("Preview");
		contentPane.add(previewBtn2, "cell 2 3,alignx right");
		
		textArea2 = new JTextArea();
		contentPane.add(textArea2, "cell 0 4 3 1,grow");
		
		checkBtn2 = new JRadioButton("");
		contentPane.add(checkBtn2, "cell 3 4,aligny top");
		
		fontLabel2 = new JLabel("font");
		contentPane.add(fontLabel2, "flowx,cell 0 5");
		
		fontBox2 = new JComboBox<String>(fonts);
		contentPane.add(fontBox2, "cell 0 5,growx");
		
		colourLabel2 = new JLabel("colour");
		contentPane.add(colourLabel2, "flowx,cell 1 5");
		
		colourBox2 = new JComboBox<String>(colours);
		contentPane.add(colourBox2, "cell 1 5,growx");
		
		sizeLabel2 = new JLabel("size");
		contentPane.add(sizeLabel2, "flowx,cell 2 5");
		
		sizeBox2 = new JComboBox<String>(sizes);
		contentPane.add(sizeBox2, "cell 2 5,growx");
		
		generateBtn = new JButton("Generate");
		contentPane.add(generateBtn, "cell 0 6 4 1,alignx center");
		
	}

}

package project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

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

public class FilterInsertFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JButton btnNewButton;
	private JButton btnNewButton_1;

	/**
	 * Create the frame.
	 */
	public FilterInsertFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[20%][5%][25%][20%][5%][45%]", "[][20%][20%][20%][20%][20%]"));
		
		btnNewButton = new JButton("Preview");
		contentPane.add(btnNewButton, "cell 5 0,alignx right,aligny top");
		
		JLabel lblSelectFilter = new JLabel("Select Filter");
		contentPane.add(lblSelectFilter, "cell 0 1 6 1,alignx center,aligny bottom");
		
		JComboBox comboBox = new JComboBox();
		contentPane.add(comboBox, "flowx,cell 0 2 6 1,alignx center,aligny top");
		
		rdbtnNewRadioButton = new JRadioButton("\n");
		contentPane.add(rdbtnNewRadioButton, "cell 1 3,alignx center,aligny bottom");
		
		textField_1 = new JTextField();
		contentPane.add(textField_1, "cell 2 3,growx,aligny bottom");
		textField_1.setColumns(10);
		
		rdbtnNewRadioButton_1 = new JRadioButton("");
		contentPane.add(rdbtnNewRadioButton_1, "cell 4 3,aligny bottom");
		
		lblNewLabel_2 = new JLabel("Entire length");
		contentPane.add(lblNewLabel_2, "cell 5 3,growx,aligny bottom");
		
		lblNewLabel = new JLabel("Start time");
		contentPane.add(lblNewLabel, "cell 3 3,alignx center,aligny bottom");
		
		textField = new JTextField();
		contentPane.add(textField, "cell 2 4,growx,aligny top");
		textField.setColumns(10);
		
		lblNewLabel_1 = new JLabel("End time");
		contentPane.add(lblNewLabel_1, "cell 3 4,alignx center,aligny top");
		
		btnNewButton_1 = new JButton("Generate");
		contentPane.add(btnNewButton_1, "cell 0 5 6 1,alignx center");
	}

}

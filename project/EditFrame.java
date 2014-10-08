package project;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Frame that appears when user selects edit button from 
 * main frame. It contains buttons which lead user to
 * perform an edit according to what they pressed.
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class EditFrame extends JFrame{
	private JButton tP,cP,rm,ex,ov,rp,exit,textS,testE;
	private JLabel page, audio, text;
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	public EditFrame(ActionListener parent) {
		//frame set up
		setSize(430,380);
		setLocation((_screenWidth-430)/2,(_screenHeight-380)/2);
		setTitle("Edit video");
		setLayout(null);
		setResizable(false);
		
		//set up of all add page buttons and the label
		//the relative action listeners are applied from the parent
		page = new JLabel("Add Page");
		page.setBounds(180, 5, 80, 30);
		add(page);
		tP = new JButton("+ Title Page");
		tP.setBounds(10, 35, 200, 40);
		tP.setActionCommand("Create title");
		tP.addActionListener(parent);
		add(tP);
		cP = new JButton("+ Credit Page");
		cP.setActionCommand("Create credit");
		cP.addActionListener(parent);
		cP.setBounds(220, 35, 200, 40);
		add(cP);
		
		//set up of all Audio operation buttons and the label
		//the relative action listeners are applied from the parent
		audio = new JLabel("Audio Operation");
		audio.setBounds(155, 90, 160, 30);
		add(audio);
		rm = new JButton("Remove Audio");
		rm.setBounds(10, 115, 200, 40);
		rm.setActionCommand("rmAudio");
		rm.addActionListener(parent);
		add(rm);
		ex = new JButton("Extract Audio");
		ex.setBounds(220, 115, 200, 40);
		ex.setActionCommand("exAudio");
		ex.addActionListener(parent);
		add(ex);
		ov = new JButton("Overlay with Audio");
		ov.setBounds(10, 160, 200, 40);
		ov.setActionCommand("ovAudio");
		ov.addActionListener(parent);
		add(ov);
		rp = new JButton("Replace Audio");
		rp.setBounds(220, 160, 200, 40);
		rp.setActionCommand("rpAudio");
		rp.addActionListener(parent);
		add(rp);
		
		text = new JLabel("Text");
		text.setBounds(195, 215, 80, 30);
		add(text);
		textS = new JButton("+ Insert text");
		textS.setBounds(10, 245, 200, 40);
		textS.setActionCommand("addText");
		textS.addActionListener(parent);
		add(textS);
		
		
		
		//set up of exit button, which closes the frame when pressed
		exit = new JButton("Exit");
		exit.setBounds(140, 310, 150, 30);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		add(exit);
		
		setVisible(true);
	}
}

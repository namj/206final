package editWindows;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

/**
 * Frame that appears when user selects edit button from 
 * main frame. It contains buttons which lead user to
 * perform an edit according to what they pressed.
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class EditFrame extends JFrame{
	private JButton tP,cP,rm,ex,ov,rp,exit,textS,textD, effectA, effectB, effectC;
	private JLabel page, audio, text, effect;
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	public EditFrame(ActionListener parent) {
		//frame set up
		setSize(430,525);
		setLocation((_screenWidth-430)/2,(_screenHeight-500)/2);
		setTitle("Edit video");
		getContentPane().setLayout(null);
		setResizable(false);
		
		//set up of all add page buttons and the label
		//the relative action listeners are applied from the parent
		page = new JLabel("Add Page");
		page.setBounds(180, 5, 80, 30);
		getContentPane().add(page);
		tP = new JButton("+ Title Page");
		tP.setBounds(10, 35, 200, 40);
		tP.setActionCommand("Create title");
		tP.addActionListener(parent);
		getContentPane().add(tP);
		cP = new JButton("+ Credit Page");
		cP.setActionCommand("Create credit");
		cP.addActionListener(parent);
		cP.setBounds(220, 35, 200, 40);
		getContentPane().add(cP);
		
		//set up of all Audio operation buttons and the label
		//the relative action listeners are applied from the parent
		audio = new JLabel("Audio Operation");
		audio.setBounds(155, 90, 160, 30);
		getContentPane().add(audio);
		rm = new JButton("Remove Audio");
		rm.setBounds(10, 115, 200, 40);
		rm.setActionCommand("rmAudio");
		rm.addActionListener(parent);
		getContentPane().add(rm);
		ex = new JButton("Extract Audio");
		ex.setBounds(220, 115, 200, 40);
		ex.setActionCommand("exAudio");
		ex.addActionListener(parent);
		getContentPane().add(ex);
		ov = new JButton("Overlay with Audio");
		ov.setBounds(10, 160, 200, 40);
		ov.setActionCommand("ovAudio");
		ov.addActionListener(parent);
		getContentPane().add(ov);
		rp = new JButton("Replace Audio");
		rp.setBounds(220, 160, 200, 40);
		rp.setActionCommand("rpAudio");
		rp.addActionListener(parent);
		getContentPane().add(rp);
		
		text = new JLabel("Insert Text");
		text.setBounds(175, 215, 80, 30);
		getContentPane().add(text);
		textS = new JButton("+ Start/End");
		textS.setBounds(10, 245, 200, 40);
		textS.setActionCommand("addTextStartEnd");
		textS.addActionListener(parent);
		getContentPane().add(textS);
		
		textD = new JButton("+ Specify Interval");
		textD.setBounds(220, 245, 200, 40);
		textD.setActionCommand("addTextSpecified");
		textD.addActionListener(parent);
		getContentPane().add(textD);
		
		effect = new JLabel("Apply Effects");
		effect.setBounds(165, 300, 100, 30);
		getContentPane().add(effect);
		effectA = new JButton("+ Effect");
		effectA.setBounds(10, 330, 200, 40);
		effectA.setActionCommand("addEffect");
		effectA.addActionListener(parent);
		getContentPane().add(effectA);
		effectB = new JButton("+ Rotate");
		effectB.setBounds(220, 330, 200, 40);
		effectB.setActionCommand("addRotate");
		effectB.addActionListener(parent);
		getContentPane().add(effectB);
		effectC = new JButton("+ Fade in/out");
		effectC.setBounds(10, 375, 200, 40);
		effectC.setActionCommand("addFade");
		effectC.addActionListener(parent);
		getContentPane().add(effectC);
		
		
		
		//set up of exit button, which closes the frame when pressed
		exit = new JButton("Exit");
		exit.setFont(new Font("Dialog", Font.BOLD, 30));
		exit.setBounds(140, 440, 150, 50);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		getContentPane().add(exit);
		
		setVisible(true);
	}
}

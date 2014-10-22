package project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

/**
 * 
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class HelpFrame extends JFrame{
	private File helpme;
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	public HelpFrame() {
		//set up of frame
		setSize(400,700);
		setLocation(_screenWidth-400, (_screenHeight-700)/2);
		setTitle("Help!");
		setLayout(new BorderLayout());
		
		//panel to contain the scroll pane
		JPanel container = new JPanel();
		add(container,BorderLayout.CENTER);
		
		//read me file is selected to be read
		helpme = new File("./userGuide.txt");
		//text area with its word limits vertically and horizontally
		JTextArea logText = new JTextArea(42,32);
		JScrollPane scroll = new JScrollPane(logText);
		logText.setEditable(false);
		BufferedReader in;
		try {
			//text from the readme.txt file is read and put in the log text
			in = new BufferedReader(new FileReader(helpme));
			String line = in.readLine();
			while(line != null){
				logText.append(line + "\n");
				line = in.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//scroll pane with log text is added to container panel
		container.add(scroll);
		
		setVisible(true);
	}
}

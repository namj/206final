package longTaskProcessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import project.MainFrame;

/**
 * This class is a subclass of swingworker. Its role is to add subtitle stream to
 * a video file, by using avconv commands through process builder.
 * @author namjun
 *
 */
public class SubtitleAdder extends SwingWorker<Integer, Integer> implements ActionListener{

	private String vidPath, srtPath, outputNamePath;
	
	private JFrame frame;
	private JProgressBar pBar;
	private JLabel pText;
	private JButton cButton;
	
	private boolean isCancelled = false;
	
	public SubtitleAdder(String vPath, String sPath, String oPath){
		
		vidPath = vPath;
		srtPath = sPath;
		outputNamePath = oPath;
		
		pBar = new JProgressBar();
		pBar.setMaximum((int) (MainFrame.getInstance().getMediaPlayer().getFps() * MainFrame.getInstance().getMediaPlayer().getLength()/1000));
		
		pText = new JLabel("adding subtitle stream...");
		
		cButton = new JButton("Cancel");
		
		frame = new JFrame();
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setLayout(new BorderLayout());
		frame.setLocation(600,400);
		frame.setSize(300,150);
		frame.add(pText,BorderLayout.NORTH);
		frame.add(pBar,BorderLayout.CENTER);
		frame.add(cButton,BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	
	
	@Override
	protected Integer doInBackground() throws Exception {

		//command to play add mirror effect to video
		String cmd = "avconv -i "+vidPath+" -i "+srtPath+" -map 0 -map 1 -c copy -f matroska -y "+ outputNamePath;
		ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
		Builder.redirectErrorStream(true);
		Process process = Builder.start();
		
		InputStream stdoutC = process.getInputStream();
		BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
		String line = null;
		//print output from terminal to console
		while ((line = stdoutD.readLine()) != null) {
			System.out.println(line);
			
			Matcher matcher = Pattern.compile("frame=\\s*(\\d+)").matcher(line);
			if(matcher.find()) {
				publish((int)(Integer.parseInt(matcher.group(1))));
			}
			
			//if cancel button has been pressed
			if (isCancelled){
				//destroy process and return exit value
				process.destroy();
				process.waitFor();
				return -100;
			}
			
		}
		//if process hasn't finished happily, return exit value which will be non zero
		if (process.waitFor() != 0){
			return process.waitFor();
		}
		
		return 0;
	}
	
	@Override
	protected void done() {
		//display error message if processes didnt finish happliy
		try {
			if (this.get() == 0) {
				JOptionPane.showMessageDialog(null, "Done");
			} else if (this.get() == -100){
				JOptionPane.showMessageDialog(null, "Process Cancelled.");
			} else {
				JOptionPane.showMessageDialog(null, "Error adding subtitles");
			}
		} catch (InterruptedException | ExecutionException e) {
			
			e.printStackTrace();
		}
		
		frame.dispose();
	}
	
	@Override
	protected void process(List<Integer> chunks) {
		//update jprogressbar
		for (int i : chunks){
			pBar.setValue(i);
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cButton){
			isCancelled = true;
		}
		
	}
	
}

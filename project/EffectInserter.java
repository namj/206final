package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class EffectInserter implements ActionListener {
	
	private JFrame _frame;
	private JLabel _label, _progressText;
	private JButton _cancelButton;
	private boolean _isCancelled = false;
	
	
	public EffectInserter(){
		
		_frame = new JFrame();
		_progressText = new JLabel("encoding...");
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(this);
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_progressText,BorderLayout.NORTH);
		_frame.add(_cancelButton,BorderLayout.SOUTH);
		
		_frame.setVisible(true);
		
	}
	
	public void InsertEffectMirror(String vidPath, String outputNamePath){
		
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add mirror effect to video
				String cmd = "avconv -i "+vidPath+" -vf \"crop=iw/2:ih:0:0,split[tmp],pad=2*iw[left]; [tmp]hflip[right]; [left][right] overlay=W/2\" "+ outputNamePath +".mp4";
				ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
				Process process = Builder.start();
				
				InputStream stdoutC = process.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
					//if cancel button has been pressed
					//if cancel button has been pressed
					if (_isCancelled){
						//destroy process and return exit value
						process.destroy();
						int exitValue = process.waitFor();
						return exitValue;
					}
					
				}
				
				return 0;
			}
			
			@Override
			protected void done() {
				
				//display error message if processes didnt finish happliy
				try {
					if (this.get() != 0){
						JOptionPane.showMessageDialog(null, "Error playing preview.");
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
		};
		
		worker.execute();
		
	}
	
	public void InsertEffectMirror(String vidPath, String outputNamePath, int ss, int t){
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		_isCancelled = true;
		
	}
	

}

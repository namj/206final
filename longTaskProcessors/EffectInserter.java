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

public class EffectInserter implements ActionListener {
	
	private JFrame _frame;
	private JProgressBar _progressBar;
	private JLabel _progressText;
	private JButton _cancelButton;
	private boolean _isCancelled = false;
	
	
	public EffectInserter(){
		
		_frame = new JFrame();
		
		_progressText = new JLabel("encoding...");
		_progressBar = new JProgressBar();
		_progressBar.setMaximum((int) (MainFrame.getInstance().getMediaPlayer().getFps() * MainFrame.getInstance().getMediaPlayer().getLength()/1000));
	
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(this);
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_progressText,BorderLayout.NORTH);
		_frame.add(_progressBar,BorderLayout.CENTER);
		_frame.add(_cancelButton,BorderLayout.SOUTH);
		
		_frame.setVisible(true);
		
	}
	
	public void insertEffectMirror(final String vidPath, final String outputNamePath){
		
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add mirror effect to video
				String cmd = "avconv -i "+vidPath+" -vf \"crop=iw/2:ih:0:0,split[tmp],pad=2*iw[left]; [tmp]hflip[right]; [left][right] overlay=W/2\" -f mp4 -c:a copy -y "+ outputNamePath;
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
					if (_isCancelled){
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
						JOptionPane.showMessageDialog(null, "Error applying effect");
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
			
			@Override
			protected void process(List<Integer> chunks) {
				for (int i: chunks){
					_progressBar.setValue(i);
				}
			}
			
		};

		worker.execute();
		
	}
	
	public void insertEffectBounce(final String vidPath, final String outputNamePath){
		
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avconv -i "+vidPath+" -vf \"crop=in_w/1.5:in_h/1.5:(in_w-out_w)/1.5+((in_w-out_w)/1.5)*sin(n/10):(in_h-out_h)/1.5 +((in_h-out_h)/1.5)*sin(n/7)\" -f mp4 -c:a copy -y "+ outputNamePath;
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
					if (_isCancelled){
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
					if (this.get() ==0) {
						JOptionPane.showMessageDialog(null, "Done");
					} else if (this.get() == -100){
						JOptionPane.showMessageDialog(null, "Process Cancelled.");
					} else {
						JOptionPane.showMessageDialog(null, "Error applying effect");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
			
			@Override
			protected void process(List<Integer> chunks) {
				for (int i: chunks){
					_progressBar.setValue(i);
				}
			}
		};
		
		worker.execute();
		
	}
	
	public void insertEffectNegate(final String vidPath, final String outputNamePath){
		
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avconv -i "+vidPath+" -vf \"negate=1\" -f mp4 -c:a copy -y "+ outputNamePath;
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
					if (_isCancelled){
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
					if (this.get() ==0) {
						JOptionPane.showMessageDialog(null, "Done");
					} else if (this.get() == -100){
						JOptionPane.showMessageDialog(null, "Process Cancelled.");
					} else {
						JOptionPane.showMessageDialog(null, "Error applying effect");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
			
			@Override
			protected void process(List<Integer> chunks) {
				for (int i: chunks){
					_progressBar.setValue(i);
				}
			}
		};
		
		worker.execute();
		
	}
	
	public void insertEffectRotate(final String vidPath, final String outputNamePath, final String transposeInput){
		
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avconv -i "+vidPath+" -vf \"transpose="+ transposeInput +"\" -f mp4 -c:a copy -y "+ outputNamePath;
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
					if (_isCancelled){
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
						JOptionPane.showMessageDialog(null, "Error applying effect");
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
			
			@Override
			protected void process(List<Integer> chunks) {
				for (int i: chunks){
					_progressBar.setValue(i);
				}
			}
		};
		
		worker.execute();
		
	}
	
	public void insertEffectFadeIn(final String vidPath, final String outputNamePath, final int i, final int j){
		
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avconv -i "+vidPath+" -vf \"fade=in:"+i+":"+j+"\" -f mp4 -c:a copy -y "+ outputNamePath;
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
					if (_isCancelled){
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
						JOptionPane.showMessageDialog(null, "Error applying effect");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
			
			@Override
			protected void process(List<Integer> chunks) {
				for (int i: chunks){
					_progressBar.setValue(i);
				}
			}
		};
		
		worker.execute();
		
	}
	
	public void insertEffectFadeOut(final String vidPath, final String outputNamePath, final int i, final int j){
		
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avconv -i "+vidPath+" -vf \"fade=out:"+i+":"+j+"\" -f mp4 -c:a copy -y "+ outputNamePath;
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
					if (_isCancelled){
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
						JOptionPane.showMessageDialog(null, "Error applying effect");
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
			
			@Override
			protected void process(List<Integer> chunks) {
				for (int i: chunks){
					_progressBar.setValue(i);
				}
			}
		};
		
		worker.execute();
		
	}

	public void insertEffectFadeIO(final String vidPath, final String outputNamePath, final int i, final int j, 
			final int k, final int l){
		
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){
	
			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avconv -i "+vidPath+" -vf \"fade=in:"+i+":"+j+", "
						+ "fade=out:"+k+":"+l+"\" -f mp4 -c:a copy -y "+ outputNamePath;
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
					if (_isCancelled){
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
						JOptionPane.showMessageDialog(null, "Error applying effect");
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
			
			@Override
			protected void process(List<Integer> chunks) {
				for (int i: chunks){
					_progressBar.setValue(i);
				}
			}
		};
		
		worker.execute();
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		_isCancelled = true;
		
	}

}

package longTaskProcessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import mainPackage.MainFrame;

/**
 * This class allows user to preview their title/credit pages
 * before they actually merge it with their main video.
 * 
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class Previewer {
	
	private JFrame _frame;
	private JLabel _label;

	
	/**
	 * constructor for this class
	 */
	public Previewer() {
		
		_frame = new JFrame();
		_label = new JLabel("Previewing...");
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_label,BorderLayout.CENTER);
	
		_frame.setVisible(true);
		
	}
	
	/**
	 * preview title/credit page
	 * @param _text
	 * @param _musicPath
	 * @param _imagePath
	 * @param _resolution
	 * @param _font
	 * @param _textSize
	 * @param _colour
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void viewTitleCredit(final String _text, final String _musicPath, final String _imagePath, final String _resolution, final String _font, final String _textSize, final String _colour) throws InterruptedException, IOException {
	
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {

				publish("Please wait...(1/3)");
				
				//command to generate video out of image
				String cmd = "avconv -loop 1 -i "+ _imagePath +" -t 00:00:10 -r 24 -s "+_resolution+" -y videoFromImage.mp4";
				ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
				Process process = Builder.start();
				if (process.waitFor() != 0){
					return process.waitFor();
				}
				
				publish("Please wait...(2/3)");
				
				//command to apply text and music
				String cmd2 = "avconv -i videoFromImage.mp4 -i "+ _musicPath +" -c:a copy -t 10 -vf \"drawtext=fontcolor="+_colour+":fontsize="+_textSize+":fontfile=./src/editWindows/resources/"+_font+":text='"+ _text +"':x=(main_w-text_w)/2:y=(main_h-text_h)/2\" -y preview.mp4";
				ProcessBuilder Builder2 = new ProcessBuilder("/bin/bash","-c",cmd2);
				Process process2 = Builder2.start();
				if (process2.waitFor() != 0){
					return process2.waitFor();
				}
				
				publish("Please wait...(3/3)");
				
				//command to play the video through avplay.
				String cmd3 = "avplay -i preview.mp4";
				ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd3);
				Process process3 = Builder3.start();
				if (process3.waitFor() != 0){
					return process3.waitFor();
				}
				
				return 0;
			}
			
			@Override
			protected void process(List<String> chunks) {
				for (int i = 0; i< chunks.size(); i++){
					_label.setText(chunks.get(i));
				}
			}
			
			@Override
			protected void done() {
				
				//display error message if processes didnt finish happliy
				try {
					if (this.get() != 0){
						JOptionPane.showMessageDialog(null, "Error playing preview.");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
				//delete video files when done
				try {
					Files.deleteIfExists(Paths.get("videoFromImage.mp4"));
					Files.deleteIfExists(Paths.get("preview.mp4"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
		};
		
		worker.execute();
		
	}
	
	/**
	 * preview insert text to video
	 * @param vidPath
	 * @param text
	 * @param font
	 * @param size
	 * @param colour
	 * @param ss
	 * @param t
	 */
	public void viewTextOverlay(final String vidPath, final String text, final String font, final String size, final String colour, final String ss, final String t){
		
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play the video through avplay.
				String cmd3 = "avplay -i "+vidPath+" -ss "+ss+" -t "+t+" -vf \"drawtext=fontcolor="+colour+":fontsize="+size+":fontfile=./src/editWindows/resources/"+font+":text='"+text+"':x=(main_w-text_w)/2:y=(main_h-text_h)/2\"";
				ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd3);
				Process process3 = Builder3.start();
				
				InputStream stdoutC = process3.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
					//if cancel button has been pressed
					
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
	
	/**
	 * preview insert mirror effect on video
	 * @param vidPath
	 */
	public void viewEffectMirror(final String vidPath){
		
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play the video through avplay.
				String cmd3 = "avplay -i "+vidPath+" -vf \"crop=iw/2:ih:0:0,split[tmp],pad=2*iw[left]; [tmp]hflip[right]; [left][right] overlay=W/2\"";
				ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd3);
				Process process3 = Builder3.start();
				
				InputStream stdoutC = process3.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
					//if cancel button has been pressed
					
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
	
	/**
	 * preview negate effect on video
	 * @param vidPath
	 */
	public void viewEffectNegate(final String vidPath){
		
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play the video through avplay.
				String cmd3 = "avplay -i "+vidPath+" -vf \"negate=1\"";
				ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd3);
				Process process3 = Builder3.start();
				
				InputStream stdoutC = process3.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
					//if cancel button has been pressed
					
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
	
	/**
	 * preview bouncing video
	 * @param vidPath
	 */
	public void viewEffectBounce(final String vidPath){
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play the video through avplay.
				//String cmd3 = "avplay -i "+vidPath+" -vf \"crop=in_w/2:in_h/2:(in_w-out_w)/2+((in_w-out_w)/2)*sin(n/10):(in_h-out_h)/2 +((in_h-out_h)/2)*sin(n/7)\"";
				String cmd3 = "avplay -i "+vidPath+" -vf \"crop=in_w/1.5:in_h/1.5:(in_w-out_w)/1.5+((in_w-out_w)/1.5)*sin(n/10):(in_h-out_h)/1.5 +((in_h-out_h)/1.5)*sin(n/7)\"";
				ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd3);
				Process process3 = Builder3.start();
				
				InputStream stdoutC = process3.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
					//if cancel button has been pressed
					
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
	
	/**
	 * preview rotated video
	 * @param vidPath
	 * @param transposeValue
	 */
	public void viewEffectRotate(final String vidPath, final String transposeValue){
		
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play the video through avplay.
	
				String cmd3 = "avplay -i "+vidPath+" -vf \"transpose="+ transposeValue +"\"";
				ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd3);
				Process process3 = Builder3.start();
				
				InputStream stdoutC = process3.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
					//if cancel button has been pressed
					
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
	
	/**
	 * preview fade in effect on video
	 * @param vidPath
	 * @param i
	 * @param j
	 */
	public void viewEffectFadeIn(final String vidPath, final int i, final int j){
		
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avplay -i "+vidPath+" -vf \"fade=in:"+i+":"+j+"\"";
				ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
				Builder.redirectErrorStream(true);
				Process process = Builder.start();
				
				InputStream stdoutC = process.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
				}
				
				return 0;
			}
			
			@Override
			protected void done() {
				
				//display error message if processes didnt finish happliy
				try {
					if (this.get() != 0){
						JOptionPane.showMessageDialog(null, "Error applying effect");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
		};
		
		worker.execute();
		
	}
	
	/**
	 * preview fade out effect on video
	 * @param vidPath
	 * @param i
	 * @param j
	 */
	public void viewEffectFadeOut(final String vidPath, final int i, final int j){
		
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avplay -i "+vidPath+" -vf \"fade=out:"+i+":"+j+"\"";
				ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
				Builder.redirectErrorStream(true);
				Process process = Builder.start();
				
				InputStream stdoutC = process.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
				}
				
				return 0;
			}
			
			@Override
			protected void done() {
				
				//display error message if processes didnt finish happliy
				try {
					if (this.get() != 0){
						JOptionPane.showMessageDialog(null, "Error applying effect");
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

	/**
	 * preview fade in&out effect on video
	 * @param vidPath
	 * @param i
	 * @param j
	 * @param k
	 * @param l
	 */
	public void viewEffectFadeIO(final String vidPath, final int i, final int j, final int k, final int l){
		
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){
	
			@Override
			protected Integer doInBackground() throws Exception {
				
				//command to play add bounce effect to video
				String cmd = "avplay -i "+vidPath+" -vf \"fade=in:"+i+":"+j+", "
						+ "fade=out:"+k+":"+l+"\"";
				ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
				Builder.redirectErrorStream(true);
				Process process = Builder.start();
				
				InputStream stdoutC = process.getInputStream();
				BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
				String line = null;
				//print output from terminal to console
				while ((line = stdoutD.readLine()) != null) {
					System.out.println(line);
				}
				
				return 0;
			}
			
			@Override
			protected void done() {
				
				//display error message if processes didnt finish happliy
				try {
					if (this.get() != 0){
						JOptionPane.showMessageDialog(null, "Error applying effect");
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
}

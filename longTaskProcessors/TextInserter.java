package longTaskProcessors;

import helperClasses.Logger;
import helperClasses.TimeFormatChecker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import project.MainFrame;

/**
 * This class extends swingworker. This class processes all the avconv commands that take 
 * a long time. The avconv commands (processes) are those that:
 * 
 * 
 *  
 * @author Namjun Park (npar350)
 *
 */

public class TextInserter extends SwingWorker<Integer, Integer> implements ActionListener {
	
	private int _whichOne; //int to indicate whether to put text and start/end or at specified interval. 1 means start/end 2 means specified
	
	private JTextField _startField, _finishField;
	
	private int _startTime, _finishTime;
	
	private String _text; //text to apply
	private String _text2;
	
	private String _videoPath; //path to main video
	private String _savePath; //path to save videos to
	private String _outputPathName; //path of outputfile and its name
	
	private JFrame _frame;
	private JLabel _progressText;
	private JProgressBar _progressBar;
	private JButton _cancelButton;
	private boolean _isCancelled = false;
	
	private String _font;
	private int _fontIndex;
	private String _textSize;
	private int _sizeIndex;
	private String _colour;
	private int _colourIndex;
	private String _time;
	private int _timeIndex;
	
	private String _font2;
	private int _fontIndex2;
	private String _textSize2;
	private int _sizeIndex2;
	private String _colour2;
	private int _colourIndex2;
	private String _time2;
	private int _timeIndex2;
	
	private long _videoLength;
	

	//constructor for this swingworker
	public TextInserter(int sel, String path, String savePath, String outputPathName, String text, JComboBox<String> font, JComboBox<String> size, JComboBox<String> colour, 
			JComboBox<String> time, String text2, JComboBox<String> font2, JComboBox<String> size2, JComboBox<String> colour2, JComboBox<String> time2, 
			long videoLength){
		
		_whichOne = sel;
		
		_text = text;
		_text2 = text2;
		_videoPath = path;
		_savePath = savePath;
		_outputPathName = outputPathName;
		
		_font = font.getSelectedItem().toString();
		_fontIndex = font.getSelectedIndex();
		_textSize = size.getSelectedItem().toString();
		_sizeIndex = size.getSelectedIndex();
		_colour = colour.getSelectedItem().toString();
		_colourIndex = colour.getSelectedIndex();
		_time = time.getSelectedItem().toString();
		_timeIndex = time.getSelectedIndex();
		
		_font2 = font2.getSelectedItem().toString();
		_fontIndex2 = font2.getSelectedIndex();
		_textSize2 = size2.getSelectedItem().toString();
		_sizeIndex2 = size2.getSelectedIndex();
		_colour2 = colour2.getSelectedItem().toString();
		_colourIndex2 = colour2.getSelectedIndex();
		_time2 = time2.getSelectedItem().toString();
		_timeIndex2 = time2.getSelectedIndex();
		
		_videoLength = videoLength;
		
		_frame = new JFrame();
		_progressText = new JLabel("encoding...");
		_progressBar = new JProgressBar();
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(this);
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_progressText,BorderLayout.NORTH);
		_frame.add(_progressBar, BorderLayout.CENTER);
		_progressBar.setMaximum((int) (MainFrame.getInstance().getMediaPlayer().getFps() * MainFrame.getInstance().getMediaPlayer().getLength()/1000));
		_frame.add(_cancelButton,BorderLayout.SOUTH);
		_frame.setVisible(true);
		
	}
	
	public TextInserter(int sel, String path, String savePath, String outputPathName, String text, JComboBox<String> font, JComboBox<String> size, 
			JComboBox<String> colour, JTextField startField, JTextField finishField){
		
		_whichOne = sel;
		
		_text = text;
		_videoPath = path;
		_savePath = savePath;
		_outputPathName = outputPathName;
		
		_font = font.getSelectedItem().toString();
		_fontIndex = font.getSelectedIndex();
		_textSize = size.getSelectedItem().toString();
		_sizeIndex = size.getSelectedIndex();
		_colour = colour.getSelectedItem().toString();
		_colourIndex = colour.getSelectedIndex();
		
		TimeFormatChecker checker = new TimeFormatChecker(startField, finishField);
		
		_startTime = checker.getStartTime();
		_finishTime = checker.getEndTime();
		
		_startField = startField;
		_finishField = finishField;
		
		_frame = new JFrame();
		_progressText = new JLabel("encoding...");
		_progressBar = new JProgressBar();
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(this);
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_progressText,BorderLayout.NORTH);
		_frame.add(_progressBar, BorderLayout.CENTER);
		_progressBar.setMaximum((int) (MainFrame.getInstance().getMediaPlayer().getFps() * MainFrame.getInstance().getMediaPlayer().getLength()/1000));
		_frame.add(_cancelButton,BorderLayout.SOUTH);
		_frame.setVisible(true);
		
	}
	

	@Override
	protected Integer doInBackground() throws Exception {
		
		if (_whichOne == 1){
			
			int endLength = (int) (_videoLength/1000 - Integer.parseInt(_time2));
			
			String cmd = "avconv -i "+ _videoPath +" -c:a copy -vf \"drawtext=fontcolor="+_colour+":fontsize="+_textSize+":fontfile=./fonts/"+_font+":text='"+ _text +"':x=(main_w-text_w)/2:y=(main_h-text_h)/2:draw='lt(t,"+_time+")', "
					+ "drawtext=fontcolor="+_colour2+":fontsize="+_textSize2+":fontfile=./fonts/"+_font2+":text='"+ _text2 +"':x=(main_w-text_w)/2:y=(main_h-text_h)/2:draw='gt(t,"+ endLength +")'\" -f mp4 -y "+ _outputPathName;
			ProcessBuilder Builder2 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder2.redirectErrorStream(true);
			Process process2 = Builder2.start();
			InputStream stdoutC = process2.getInputStream();
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
					process2.destroy();
					process2.waitFor();
					return -100;
				}
			}
			//if process hasn't finished happily, return exit value which will be non zero
			if (process2.waitFor() != 0){
				return process2.waitFor();
			}
			
			return 0;
			
		} else {
			
			String cmd = "avconv -i "+ _videoPath +" -c:a copy -vf \"drawtext=fontcolor="+_colour+":fontsize="+_textSize+":"
					+ "fontfile=./fonts/"+_font+":text='"+ _text +"':x=(main_w-text_w)/2:y=(main_h-text_h)/2:draw='gte(t,"+_startTime+")*lte(t,"+_finishTime+")'\" -f mp4 -y "+ _outputPathName;
			ProcessBuilder Builder2 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder2.redirectErrorStream(true);
			Process process2 = Builder2.start();
			InputStream stdoutC = process2.getInputStream();
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
					process2.destroy();
					process2.waitFor();
					return -100;
				}
			}
			//if process hasn't finished happily, return exit value which will be non zero
			if (process2.waitFor() != 0){
				return process2.waitFor();
			}
			
			
			return 0;
		}
		
	}
	
	@Override
	protected void done() {
		
		try {
			//if background work finished peacefully
			if (this.get() == 0) {
				JOptionPane.showMessageDialog(null,"Done!");
			} else if (_isCancelled) {
				JOptionPane.showMessageDialog(null, "Cancelled");
			} else {
				JOptionPane.showMessageDialog(null,"Error! (Exit Status:"+this.get()+")");
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if user decides to save session
		
		int result = JOptionPane.showConfirmDialog(null, "would you like to save your last(this) session?", "" , 1);
		if (result == JOptionPane.OK_OPTION){
			try {
				if (_whichOne == 1){
					Logger.getInstance().updateForText(_text, _timeIndex, _fontIndex, _sizeIndex, _colourIndex, _text2, _timeIndex2, _fontIndex2, _sizeIndex2, _colourIndex2);
					JOptionPane.showMessageDialog(null,"Saved");
				} else if (_whichOne == 2){
					Logger.getInstance().updateForText2(_text, _fontIndex, _sizeIndex, _colourIndex, _startField.getText(), _finishField.getText());
					JOptionPane.showMessageDialog(null,"Saved");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (result == JOptionPane.NO_OPTION){
			//if user denied to save session delete the edit log file so that next time user creates title/credit page nothing is continued
			try {
				Logger.getInstance().deleteLogForInsert();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//close progress frame;
		_frame.dispose();
		
	}
	
	@Override
	protected void process(List<Integer> chunks) {
		for (int i: chunks){
			_progressBar.setValue(i);
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _cancelButton){
			_isCancelled = true;
			_frame.dispose();
		}
		
	}
	
}

package helperClasses;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * This class checks if time formats are given correctly like : (00:00:00)
 * And also converts those formats into seconds as well.
 * @author namjun
 *
 */

public class TimeFormatChecker {
		
	String _start, _finish;
	JTextArea _textArea;
	EmbeddedMediaPlayer _currentVideo;
	
	
	
	public TimeFormatChecker(JTextField startField, JTextField endField, JTextArea textArea, EmbeddedMediaPlayer currentVideo){
	
		_currentVideo = currentVideo;
		
		_textArea = textArea;
		
		_start = startField.getText();
		_finish = endField.getText();
	}
	
	public TimeFormatChecker(JTextField startField, JTextField endField, EmbeddedMediaPlayer currentVideo){
		
		_currentVideo = currentVideo;
		
		_start = startField.getText();
		_finish = endField.getText();
	}
	
	public TimeFormatChecker(JTextField startField, JTextField endField){
		
		_start = startField.getText();
		_finish = endField.getText();
	}
	
	/**
	 * Check if time formats (00:00:00) are given correctly
	 * 
	 * @return
	 */
	
	public boolean checkTimeFormat(){
		
		//Make sure user entered time formats correctly
		Boolean _formatCorrect = true;

		//make sure length is 8, and check for numbers and : at right indexes
		if (_start.length() == 8){
			for (int i = 0; i<_start.length(); i++){
				if (i == 2 || i == 5){
					if(_start.charAt(i) != ':'){
						_formatCorrect = false;
						break;
					}
				}
				else {
					if (!Character.isDigit(_start.charAt(i))){
						_formatCorrect = false;
						break;
					}
				}
			}
		}else{
			_formatCorrect = false;
		}
		//make sure length is 8, and check for numbers and : at right indexes
		if (_finish.length() == 8){
			for (int i = 0; i<_finish.length(); i++){
				if (i == 2 || i == 5){
					if(_finish.charAt(i) != ':'){
						_formatCorrect = false;
						break;
					}
				}
				else {
					if (!Character.isDigit(_finish.charAt(i))){
						_formatCorrect = false;
						break;
					}
				}
			}
		}else{
			_formatCorrect = false;
		}
		
		
		
		//complain to user if time given in wrong format
		if (_formatCorrect == false){
			JOptionPane.showMessageDialog(null, "Format of time(s) specified is incorrect");
		}
		
		if (_textArea != null){
			if (_textArea.getText().length() == 0){
				_formatCorrect = false;
				JOptionPane.showMessageDialog(null, "Text area is blank!");
			}
		}
		
		Boolean _everythingElseCorrect = true;
		if (_formatCorrect == true){
			// if time formats are given correctly, then make sure time given is within video length range
			//acquire video length in seconds first
			int length = (int) (_currentVideo.getLength()/1000);
			
			int _startTime = getStartTime();
			int _endTime = getEndTime();
			
			//if start time is less than 0 or longer than video length
			if (_startTime < 0 || _startTime > length){
				JOptionPane.showMessageDialog(null, "Start time specifed isnt within range of video length");
				_everythingElseCorrect = false;
			}
			//if end time is less thatn 0 or longer than video length
			if (_endTime < 0 || _endTime > length){
				JOptionPane.showMessageDialog(null, "End time specifed isnt within range of video length");
				_everythingElseCorrect = false;
			}
			//if start time is equal or longer than end time
			if (_startTime >= _endTime){
				JOptionPane.showMessageDialog(null, "Start time(s) must be less than end time(s)");
				_everythingElseCorrect = false;
			}
			
		} else {
			return false;
		}
		
		return _everythingElseCorrect;
		
	}
	
	/**
	 * This method converts 00:00:00 format into seconds
	 * @return
	 */
	public int getStartTime(){
		int hoursToSecs = Integer.parseInt(_start.substring(0,2)) * 3600;
		int minsToSecs = Integer.parseInt(_start.substring(3, 5)) * 60;
		int secsToSecs = Integer.parseInt(_start.substring(6));
		
		int _startTime = hoursToSecs + minsToSecs + secsToSecs;
		
		return _startTime;
		
	}
	
	/**
	 * This method converts 00:00:00 format into seconds
	 * @return
	 */
	public int getEndTime(){
		int hoursToSecs2 = Integer.parseInt(_finish.substring(0,2)) * 3600;
		int minsToSecs2 = Integer.parseInt(_finish.substring(3, 5)) * 60;
		int secsToSecs2 = Integer.parseInt(_finish.substring(6));
		
		int _endTime = hoursToSecs2 + minsToSecs2 + secsToSecs2;
		
		return _endTime;
	}

}

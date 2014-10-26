package helperClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JTextArea;

/**
 * This class handles everything there is to do with logging.
 * It updates the log files on request and deletes them as well.
 * log files are used to save text editing session
 * 
 * @author Namjun Park (npar350) Andy Choi (mcho588)
 *
 */

public class Logger {
	
	//declare/initialise components
	private File _homedir = new File(System.getProperty("user.home"));
	private File _vamixFolder = new File(_homedir, "/.vamix");
	
	//constructor
	private Logger() { 
		//if vamix folder doesnt exist, make one
		if (!_vamixFolder.exists()){
			_vamixFolder.mkdir();
		}
		
	}
	private static Logger instance = new Logger();
	//return singleton object of this panel
	public static Logger getInstance() {
		return instance;
	}
	
	//function which saves editing session by updating log files.
	public void updateForPage(String text, String musicPath, String imagePath, int fontCombo, int sizeCombo, int colourCombo) throws IOException {
		
		//update edit log file
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/editlog.txt", false)));
		out.println(musicPath);
		out.println(imagePath);
		out.println(fontCombo);
		out.println(sizeCombo);
		out.println(colourCombo);
		out.close();
		
		//update textlog
		PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/textlog.txt", false)));
		out2.print(text);
		out2.close();
		
	}
	
	public void updateForText(String text1, int timeCombo1, int fontCombo1, int sizeCombo1, int colourCombo1, 
			String text2, int timeCombo2, int fontCombo2, int sizeCombo2, int colourCombo2) throws IOException {
		
		//update 
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/insertLogTop.txt", false)));
		out.println(timeCombo1);
		out.println(fontCombo1);
		out.println(colourCombo1);
		out.println(sizeCombo1);
		out.close();
		
		//update textlog
		PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/insertLogTop2.txt", false)));
		out2.print(text1);
		out2.close();
		
		//update
		PrintWriter out3 = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/insertLogBot.txt", false)));
		out3.println(timeCombo2);
		out3.println(fontCombo2);
		out3.println(colourCombo2);
		out3.println(sizeCombo2);
		out3.close();
		
		//update textlog
		PrintWriter out4 = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/insertLogBot2.txt", false)));
		out4.print(text2);
		out4.close();
		
		
	}
	
	public void updateForText2(String text1, int fontCombo1, int sizeCombo1, int colourCombo1, String startTime, String finishTime) throws IOException {
		
		//update 
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/insertLog.txt", false)));
		out.println(fontCombo1);
		out.println(colourCombo1);
		out.println(sizeCombo1);
		out.println(startTime);
		out.println(finishTime);
		out.close();
		
		//update textlog
		PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/insertLog2.txt", false)));
		out2.print(text1);
		out2.close();	
		
	}
	

	
	//this method should return the text, ie first line of editlog file
	public void pullTextForPage(JTextArea text){
		
		//if textlog exists, write its contents to the textArea.
		if (new File(_vamixFolder + "/textlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/textlog.txt"));
				text.setText("");
				String line = in.readLine();
				while(line != null){
				  text.append(line + "\n");
				  line = in.readLine();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}
	
	//this method should return the path of music file last used. ie second line of edit log file
	public String pullMusicPathForPage(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//should be first line;
				String line = in.readLine();
				in.close();
				return line;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return "";
	}
	
	//this method should return the path of image file last used. ie third line of edit log file
	public String pullImagePathForPage(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//the secondline should contain path to image
				in.readLine();
				String line = in.readLine();
				in.close();
				return line;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return "";
	}
	
	public int pullFontIndexForPage(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//should be in 3rdline
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullSizeIndexForPage(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//should be in 4th line
				in.readLine();
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullColourIndexForPage(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//should be in 5th line
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}
	
	public void pullTextForInsertTop(JTextArea text){
		
		//if textlog exists, write its contents to the textArea.
		if (new File(_vamixFolder + "/insertLogTop2.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogTop2.txt"));
				text.setText("");
				String line = in.readLine();
				while(line != null){
				  text.append(line + "\n");
				  line = in.readLine();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}
	
	public int pullTimeIndexForInsertTop(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/insertLogTop.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogTop.txt"));
				//should be in 1st line
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullfontIndexForInsertTop(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/insertLogTop.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogTop.txt"));
				//should be in 2nd line
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullColourIndexForInsertTop(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/insertLogTop.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogTop.txt"));
				//should be in 3rd line
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}
	
	public int pullSizeIndexForInsertTop(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/insertLogTop.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogTop.txt"));
				//should be in 4th line
				in.readLine();
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}
	
	public void pullTextForInsertBot(JTextArea text){
		
		//if textlog exists, write its contents to the textArea.
		if (new File(_vamixFolder + "/insertLogBot2.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogBot2.txt"));
				text.setText("");
				String line = in.readLine();
				while(line != null){
				  text.append(line + "\n");
				  line = in.readLine();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}
	
	public int pullTimeIndexForInsertBot(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/insertLogBot.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogBot.txt"));
				//should be in 1st line
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullfontIndexForInsertBot(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/insertLogBot.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogBot.txt"));
				//should be in 2nd line
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullColourIndexForInsertBot(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/insertLogBot.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogBot.txt"));
				//should be in 3rd line
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}
	
	public int pullSizeIndexForInsertBot(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/insertLogBot.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLogBot.txt"));
				//should be in 4th line
				in.readLine();
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}
	
	public void pullTextForInsert(JTextArea text){
		
		//if textlog exists, write its contents to the textArea.
		if (new File(_vamixFolder + "/insertLog2.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLog2.txt"));
				text.setText("");
				String line = in.readLine();
				while(line != null){
				  text.append(line + "\n");
				  line = in.readLine();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}

	public int pullfontIndexForInsert(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/insertLog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLog.txt"));
				//should be in 1st line
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullColourIndexForInsert(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/insertLog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLog.txt"));
				//should be in 2nd line
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}
	
	public int pullSizeIndexForInsert(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/insertLog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLog.txt"));
				//should be in 3rd line
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}
	
	public String pullStartTimeForInsert(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/insertLog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLog.txt"));
				//should be in 4th line
				in.readLine();
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return line;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return "00:00:00";
	}
	
	public String pullFinishTimeForInsert(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/insertLog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/insertLog.txt"));
				//should be in 5th line
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return line;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return "00:00:00";
	}
	
	
	//method that deletes editlog and  textlog if they exist.
	public void deleteLogForPage() throws IOException{
		Files.deleteIfExists(Paths.get(_vamixFolder + "/editlog.txt"));
		Files.deleteIfExists(Paths.get(_vamixFolder + "/textlog.txt"));
	}
	
	public void deleteLogForInsert() throws IOException{
		Files.deleteIfExists(Paths.get(_vamixFolder + "/insertLogTop"));
		Files.deleteIfExists(Paths.get(_vamixFolder + "/insertLogTop2"));
		Files.deleteIfExists(Paths.get(_vamixFolder + "/insertLogBot"));
		Files.deleteIfExists(Paths.get(_vamixFolder + "/insertLogBot2"));
	}
	
}

package project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class subtitleJMenu extends JMenu implements ActionListener  {

	private EmbeddedMediaPlayer currentVideo = null;
	
	private JMenuItem addSub;
	private JMenuItem hideSub;
	private ArrayList<JRadioButtonMenuItem> subtitleList;
	
	/**
	 * create the JMenu
	 */
	public subtitleJMenu(){
		
		setText("Subtitles");
		
		addSub = new JMenuItem("add subs");
		addSub.setEnabled(false);
		addSub.addActionListener(this);
		hideSub = new JMenuItem("hide subs");
		hideSub.setEnabled(false);
		hideSub.addActionListener(this);
		subtitleList = new ArrayList<JRadioButtonMenuItem>();
		
		add(addSub);
		add(hideSub);
		add(new JSeparator());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == addSub){
			if(MainFrame.getInstance().getMediaPlayer() != null){
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setDialogTitle("Select an srt file");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("srt files", "srt");
				fileChooser.addChoosableFileFilter(filter);
		 
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION){	
					
					File f = fileChooser.getSelectedFile();
					MainFrame.getInstance().getMediaPlayer().setSubTitleFile(f);
					setupItems(MainFrame.getInstance().getMediaPlayer());
					uncheckAllRadio();
					//subtitleList.get(subtitleList.size() - 1).setSelected(true);
					setupItems(MainFrame.getInstance().getMediaPlayer());
						
				}
			}
		}
		
		int i = 1;
		for(JRadioButtonMenuItem item : subtitleList){
			if (e.getSource() == item){
				MainFrame.getInstance().getMediaPlayer().setSpu(i + 1);
				item.setSelected(true);
			}
			else{
				item.setSelected(false);
			}
			i++;
		}
		
		if(e.getSource() == hideSub){
			MainFrame.getInstance().getMediaPlayer().setSpu(-1);
			uncheckAllRadio();
		}
		
	}

	public void uncheckAllRadio(){
		for(JRadioButtonMenuItem item : subtitleList){
			item.setSelected(false);
		}
	}
	
	public void setupItems(EmbeddedMediaPlayer cVideo){
		
		removeAll();
		
		if (currentVideo != null){
			if (currentVideo.getMediaMeta() != cVideo.getMediaMeta()){
				subtitleList.clear();
			}
			
			addSub.setEnabled(true);
		}
		
		currentVideo = cVideo;
		
		add(addSub);
		add(hideSub);
		add(new JSeparator());
		
		if (currentVideo.getSpuCount() != 0) {
			hideSub.setEnabled(true);
			for(int i = 1; i < currentVideo.getSpuCount(); i++){
				JRadioButtonMenuItem x =  new JRadioButtonMenuItem("Subtitle " + i);
				subtitleList.add(x);
				add(subtitleList.get(i-1));
				subtitleList.get(i-1).addActionListener(this);
			}
			
			for(int i = 0; i<subtitleList.size(); i++){
				if (currentVideo.getSpu() == i+2){
					subtitleList.get(i).setSelected(true);
				}
			}
			
		} else {
			hideSub.setEnabled(false);
		}
		
		
		
	}
	
}

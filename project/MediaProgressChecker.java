package project;

import java.util.List;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * 
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class MediaProgressChecker extends SwingWorker<Void,Void>{
	private MainPanel main;
	
	//constructor of swing worker takes in Main panel as input to invoke update on
	public MediaProgressChecker(MainPanel mp) {
		main = mp;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		EmbeddedMediaPlayer media = main.getMedia();
		long length = media.getLength();
		//after a media is opened, continuously checks for media progress
		//when video is finish and not playable, restart
		while (true) {
			Thread.sleep(50);
			publish();
			if (media.isPlayable() == false && media.getTime() > length) {
				main.restart();
				break;
			}
		}
		return null;
	}

	@Override
	protected void process(List<Void> chunks) {
		//update progress bar UI, every publish()
		main.updateMediaProgress();
	}

}

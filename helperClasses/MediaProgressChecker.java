package helperClasses;

import java.util.List;

import javax.swing.SwingWorker;

import mainPackage.PrimaryPanel;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * 
 * This class checks the progress of the media file playing. It is a 
 * subclass of swingworker and works as background while video it playing
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class MediaProgressChecker extends SwingWorker<Void,Void>{
	private PrimaryPanel main;
	
	/**
	 * constructor of swing worker takes in Main panel as input to invoke update on
	 */
	public MediaProgressChecker(PrimaryPanel mp) {
		main = mp;
	}
	
	/**
	 * after a media is opened, continuously checks for media progress
	 */
	@Override
	protected Void doInBackground() throws Exception {
		EmbeddedMediaPlayer media = main.getMedia();
		long length = media.getLength();
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

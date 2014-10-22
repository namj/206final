package playback;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Swingworker that fastfowards the video
 * FF is done on background thread so user can
 * push other buttons while its happening.
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class FastForwarder extends SwingWorker<Void,Void>{
	private EmbeddedMediaPlayer currentVideo;
	//boolean to command fast forwarding
	private boolean fastForward = true;
	
	//constructor to set up video to operate on
	public FastForwarder(EmbeddedMediaPlayer vid){
		currentVideo = vid;
	}
	
	//method to stop/ complete swing worker action of fast forwarding
	public void stop() {
		fastForward = false;
	}
	
	//-----------------When Executed--------------------------------//
	
	@Override
	protected Void doInBackground() throws Exception {
		//the video fast forwards until set to stop
		while (fastForward) {
			Thread.sleep(50);
			currentVideo.skip(100);
			//if fast forwarded till end, stop and play so that it moves back into
			//start
			if (currentVideo.getTime() > currentVideo.getLength()) {
				currentVideo.play();
				break;
			}
		}
		return null;
	}
}

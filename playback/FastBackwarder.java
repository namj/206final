package playback;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Swingworker that rewinds the video
 * FF is done on background thread so user can
 * push other buttons while its happening.
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class FastBackwarder extends SwingWorker<Void,Void>{
	
	private EmbeddedMediaPlayer currentVideo;
	//boolean to command rewinding
	private boolean fastBackward = true;
	
	/**
	 * constructor to set up video for rewind.
	 * @param vid
	 */
	public FastBackwarder(EmbeddedMediaPlayer vid){
		currentVideo = vid;
	}
	
	/**
	 * stops/ complete swing worker action of rewinding
	 */
	public void stop() {
		fastBackward = false;
	}
	
	//-------------------When Executed--------------------------//
	
	@Override
	protected Void doInBackground() throws Exception {
		//while commanded to rewind, rewinds until set to stop
		while (fastBackward) {
			Thread.sleep(50);
			currentVideo.skip(-100);
		}
		return null;
	}
}

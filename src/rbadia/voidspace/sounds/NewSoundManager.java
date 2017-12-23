package rbadia.voidspace.sounds;

import java.applet.Applet;
import java.applet.AudioClip;

import rbadia.voidspace.main.Level1State;

public class NewSoundManager extends SoundManager {
	private static final boolean SOUND_ON = true;
	
	private AudioClip fireballSound = Applet.newAudioClip(Level1State.class.getResource(
		    "/rbadia/voidspace/sounds/Fireball.wav"));
	private AudioClip fireballHitSound = Applet.newAudioClip(Level1State.class.getResource(
		    "/rbadia/voidspace/sounds/fireExplosion.wav"));
	
	public void playFireballSound(){
    	if(SOUND_ON){
    		new Thread(new Runnable(){
    			public void run() {
    				fireballSound.play();
    			}
    		}).start();
    	}
    }

public void playFireballHit(){
	// play sound for Fireball explosions
	if(SOUND_ON){
		new Thread(new Runnable(){
			public void run() {
				fireballHitSound.play();
			}
		}).start();	
	}
}
}

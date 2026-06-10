package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	Clip clip;
	URL soundURL[] = new URL[30];
	FloatControl fc;
	int volumeScale = 3;
	float volume;
	
	public Sound() {
		
		soundURL[0] = getClass().getResource("/sound/Themewalin.wav");
		soundURL[1] = getClass().getResource("/sound/grab key.wav");
		soundURL[2] = getClass().getResource("/sound/healthup.wav");
		soundURL[3] = getClass().getResource("/sound/die.wav");
		soundURL[4] = getClass().getResource("/sound/powerup.wav");
		soundURL[5] = getClass().getResource("/sound/unlock.wav");
		soundURL[6] = getClass().getResource("/sound/spraysound.wav");
		soundURL[7] = getClass().getResource("/sound/hitrat.wav");
		soundURL[8] = getClass().getResource("/sound/hittaken.wav");
		soundURL[9] = getClass().getResource("/sound/levelup.wav");
		soundURL[10] = getClass().getResource("/sound/cursor.wav");
		soundURL[11] = getClass().getResource("/sound/pukesound.wav");
		soundURL[12] = getClass().getResource("/sound/coingrab.wav");
		soundURL[13] = getClass().getResource("/sound/die.wav");
		soundURL[14] = getClass().getResource("/sound/transistion.wav");
		soundURL[15] = getClass().getResource("/sound/the boss level.wav");
		soundURL[16] = getClass().getResource("/sound/fartblast.wav");
	}
	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			checkVolume();
			
			
		}
		catch(Exception e) {
			
		}
		
	}
public void play() {
	clip.start();
	
}
public void loop () {
	clip.loop(Clip.LOOP_CONTINUOUSLY);
	
}
public void stop() {
	clip.stop();
	
}
public void checkVolume() {
	switch(volumeScale) {
	case 0: volume = -80f; break;
	case 1: break;
	case 2: volume = -20f; break;
	case 3: volume = -12f; break;
	case 4: volume = -5f;break;
	case 5: volume = 6f;  break;
	
	}
	fc.setValue(volume);
}
}

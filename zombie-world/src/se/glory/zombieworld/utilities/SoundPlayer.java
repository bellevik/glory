package se.glory.zombieworld.utilities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundPlayer {
	public static void playSound() {
		
	}
	
	public static void playBackgroudMusic() {
		Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));
		backgroundMusic.setVolume(1);
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
	}
	
	public static void playSoundEffect() {
		Sound sound = null;
		
		float rand = new Random().nextFloat();
		
		if (rand < 0.25f) {
			sound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/zombie/1.mp3"));
			long id = sound.play();
			sound.setVolume(id, 0.6f);
		} else if (rand < 0.50f) {
			sound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/zombie/2.mp3"));
			long id = sound.play();
			sound.setVolume(id, 0.6f);
		} else if (rand < 0.75f) {
			sound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/zombie/3.mp3"));
			long id = sound.play();
			sound.setVolume(id, 0.4f);
		} else if (rand < 1f) {
			sound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/zombie/4.mp3"));
			long id = sound.play();
			sound.setVolume(id, 0.2f);
		}
	}
	
	public enum SOUND_EFFECTS {
		
	}
}

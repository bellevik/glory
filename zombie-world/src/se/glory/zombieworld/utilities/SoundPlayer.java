package se.glory.zombieworld.utilities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundPlayer {
	private Random random = new Random();
	private Sound[] soundEffects = new Sound[4];
	
	public SoundPlayer() {
		soundEffects[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/zombie/1.mp3"));
		soundEffects[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/zombie/2.mp3"));
		soundEffects[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/zombie/3.mp3"));
		soundEffects[3] = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/zombie/4.mp3"));
	}
	
	public void playBackgroudMusic() {
		Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));
		backgroundMusic.setVolume(1);
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
	}
	
	public void playSoundEffect() {
		float rand = random.nextFloat() * 1000;
		
		if (rand < 300) {
			long id = soundEffects[0].play();
			soundEffects[0].setVolume(id, 0.6f);
		} else if (rand < 600) {
			long id = soundEffects[1].play();
			soundEffects[1].setVolume(id, 0.6f);
		} else if (rand < 850) {
			long id = soundEffects[2].play();
			soundEffects[2].setVolume(id, 0.4f);
		} else if (rand < 1000) {
			long id = soundEffects[3].play();
			soundEffects[3].setVolume(id, 0.1f);
		}
	}
	
	public enum SOUND_EFFECTS {
		
	}
}

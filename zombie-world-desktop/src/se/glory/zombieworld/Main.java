package se.glory.zombieworld;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static final String TITLE = "Zombie World", VERSION = "0.0.0.1";
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = TITLE + " v" + VERSION;
		cfg.useGL20 = true;
		cfg.width = 1000;
		cfg.height = 600;
		
		new LwjglApplication(new ZombieWorld(), cfg);
	}
}

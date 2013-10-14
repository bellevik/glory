package se.glory.zombieworld.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/*
 * This class will handle textures. It«s good to have for optimization.
 * Instead of loading the same image file every time a creature is created
 * this class will load the image ONCE and reuse the same load. This will spare
 * memory.
 */
public class TextureHandler {
	
	public static Texture humanTexture;
	public static Texture zombieTexture;
	public static Texture bulletTexture;
	
	public static void createTextures() {
		bulletTexture = new Texture(Gdx.files.internal("img/bullet.png"));
	}	
}
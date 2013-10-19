package se.glory.zombieworld.utilities.progressbars;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ItemBar extends ProgressBar{

	//Creates a statindicator specific progressbar to support showing stats for weapons
	public ItemBar(Stage stage, int x, int y, int filledAmount) {
		super(stage, x, y, 2, 0, 1, filledAmount, new Texture(Gdx.files.internal("img/shop/smallBar.png")), 
				new Texture(Gdx.files.internal("img/shop/statIndicator.png")), new Texture(Gdx.files.internal("img/shop/smallBar.png")));
	}

}

package se.glory.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class ItemContainer extends Actor {
	
	Texture texture;
	
	public ItemContainer(Stage stage, float x, float y) {
		texture = new Texture(Gdx.files.internal("img/itemBase.png"));
		
		setWidth(texture.getWidth());
		setHeight(texture.getHeight());
		
		setBounds(x, y, getWidth(), getHeight());
		
		Image actor = new Image(texture);
		
		stage.addActor(actor);
		actor.setPosition(x, y);
	}
}

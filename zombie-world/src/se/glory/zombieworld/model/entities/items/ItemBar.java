package se.glory.zombieworld.model.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ItemBar extends ProgressBar{

	public ItemBar(Stage stage, int x, int y, int filledAmount) {
		super(stage, x, y, 2, 0, 1, filledAmount, new Texture(Gdx.files.internal("img/shop/smallBar.png")), 
				new Texture(Gdx.files.internal("img/shop/statIndicator.png")), new Texture(Gdx.files.internal("img/shop/smallBar.png")));
		// TODO Auto-generated constructor stub
		
	/*	int percentageToNumber = (int)((filledPercentage/100)*60);
		System.out.println("percfers " + percentageToNumber);
		
		super.forceHealthUpdate(percentageToNumber);
		System.out.println(super.getHealthPercentGoal());
		System.out.println(super.getLastKnownHealthPercent());*/
	}

}

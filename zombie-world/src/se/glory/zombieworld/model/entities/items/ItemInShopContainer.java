package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.entities.weapons.EMeleeWeapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ItemInShopContainer {

	private Stage stage;
	private Texture texture;
	private Image bg;
	private Texture weapon;
	private Image weaponActor;
	private Image rangeIndicatorBar;
	private Image fireRateIndicatorBar;
	private Image dmgIndicatorBar;
	private String itemName;
	private String itemCost;
	
	
	public ItemInShopContainer(Stage stage,float x,float y,EMeleeWeapon item){
		this.stage = stage;
		texture = new Texture(Gdx.files.internal("img/shop/shopframe.png"));
		weapon =item.getTexture(1);
		
		rangeIndicatorBar = new Image ();
		dmgIndicatorBar = new Image();
		fireRateIndicatorBar = new Image();
		
		
		bg = new Image(texture);	
		stage.addActor(bg);
		bg.setPosition(x, y);
		
		weaponActor = new Image (weapon);
		stage.addActor(weaponActor);
		weaponActor.setPosition(x+10, y+10);
		
	}public void hideContainer(){
		bg.setVisible(false);
		weaponActor.setVisible(false);
	}
	
}

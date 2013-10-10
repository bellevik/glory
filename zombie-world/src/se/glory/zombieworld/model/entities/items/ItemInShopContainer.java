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
		weapon =item.getTexture(1);
		
		
		
		bg = new Image(new Texture(Gdx.files.internal("img/shop/shopframe.png")));	
		stage.addActor(bg);
		bg.setPosition(x, y);
		bg.setVisible(false);
		
		rangeIndicatorBar = new Image(new Texture(Gdx.files.internal("img/shop/smallBar.png")));
		stage.addActor(rangeIndicatorBar);
		rangeIndicatorBar.setPosition(x+80, y+10);
		rangeIndicatorBar.setVisible(false);
		
		dmgIndicatorBar = new Image(new Texture(Gdx.files.internal("img/shop/smallBar.png")));
		stage.addActor(dmgIndicatorBar);
		dmgIndicatorBar.setPosition(x+80, y+40);
		dmgIndicatorBar.setVisible(false);
		
		fireRateIndicatorBar = new Image(new Texture(Gdx.files.internal("img/shop/smallBar.png")));
		stage.addActor(fireRateIndicatorBar);
		fireRateIndicatorBar.setPosition(x+80, y+70);
		fireRateIndicatorBar.setVisible(false);
		
		
		
		
		weaponActor = new Image (weapon);
		stage.addActor(weaponActor);
		weaponActor.setPosition(x+10, y+10);
		weaponActor.setVisible(false);
		
		
	}public void setContainerVisability(boolean visable){
		bg.setVisible(visable);
		weaponActor.setVisible(visable);
		rangeIndicatorBar.setVisible(visable);
		dmgIndicatorBar.setVisible(visable);
		fireRateIndicatorBar.setVisible(visable);
		
	}public void setIndicatorRange(){
		
	}
	
}

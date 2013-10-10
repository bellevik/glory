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
	private ItemBar rangeIndicatorBar;
	private ItemBar fireRateIndicatorBar;
	private ItemBar dmgIndicatorBar;
	private String itemName;
	private String itemCost;
	
	
	public ItemInShopContainer(Stage stage,float x,float y,EMeleeWeapon item){
		this.stage = stage;
		weapon =item.getTexture(1);
		
		bg = new Image(new Texture(Gdx.files.internal("img/shop/shopframe.png")));	
		stage.addActor(bg);
		bg.setPosition(x, y);
		bg.setVisible(false);
		
		int range = (int)((item.getRange()/100)*60);
		int dmg = (int)((item.getDamage()/100)*60);
		int fireRate = 36;
		
		rangeIndicatorBar = new ItemBar(stage, (int)x+80, (int)y+10, range);
		rangeIndicatorBar.setVisibility(false);
		
		dmgIndicatorBar = new ItemBar(stage, (int)x+80, (int)y+40, dmg);
		dmgIndicatorBar.setVisibility(false);
		
		fireRateIndicatorBar = new ItemBar(stage, (int)x+80, (int)y+70, fireRate);
		fireRateIndicatorBar.setVisibility(false);
		
		weaponActor = new Image (weapon);
		stage.addActor(weaponActor);
		weaponActor.setPosition(x+10, y+10);
		weaponActor.setVisible(false);
		
		
	}public void setContainerVisability(boolean visable){
		bg.setVisible(visable);
		weaponActor.setVisible(visable);
		rangeIndicatorBar.setVisibility(visable);
		dmgIndicatorBar.setVisibility(visable);
		fireRateIndicatorBar.setVisibility(visable);
		
	}public void setIndicatorRange(){
		
	}
	
}

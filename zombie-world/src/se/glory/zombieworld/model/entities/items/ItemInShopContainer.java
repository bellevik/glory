package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.progressbars.ItemBar;
import se.glory.zombieworld.model.entities.weapons.EMeleeWeapon;
import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ItemInShopContainer {

	private Stage stage;
	private Texture texture;
	private Image bg;
	private Image bgOwned;
	private Texture weapon;
	private Image weaponActor;
	private ItemBar rangeIndicatorBar;
	private ItemBar fireRateIndicatorBar;
	private ItemBar dmgIndicatorBar;
	private String itemName;
	private String itemCost;
	private Item item;
	
	
	public ItemInShopContainer(Stage stage,float x,float y,EMeleeWeapon item){
		this.stage = stage;
		this.item = item;
		weapon =item.getTexture(1);
		
		
		bg = new Image(new Texture(Gdx.files.internal("img/shop/shopframe.png")));	
		stage.addActor(bg);
		bg.setPosition(x, y);
		bg.setVisible(false);
		
		bgOwned = new Image(new Texture(Gdx.files.internal("img/shop/shopframeowned3.png")));
		stage.addActor(bgOwned);
		bgOwned.setPosition(x, y);
		bgOwned.setVisible(false);
		
		
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
		if (WorldModel.player.existsInSwapList(item)){
			bgOwned.setVisible(visable);
		}
		bg.setVisible(visable);
		weaponActor.setVisible(visable);
		rangeIndicatorBar.setVisibility(visable);
		dmgIndicatorBar.setVisibility(visable);
		fireRateIndicatorBar.setVisibility(visable);
		
	}public void buyItem(){
		WorldModel.player.addItemToQuickSwap(item);
		bgOwned.setVisible(true);
	}
	public float getBgX() {
		return bg.getX();
	}
	
	public float getBgY() {
		return bg.getY();
	}
	public Image getBg(){
		return bg;
	}

	
	
	
}

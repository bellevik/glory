package se.glory.zombieworld.ui;

import se.glory.zombieworld.model.StageModel;
import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.items.EquippableItem;
import se.glory.zombieworld.utilities.Constants;
import se.glory.zombieworld.utilities.progressbars.ItemBar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ItemInShopContainer {
	private Texture healthBarTexture;
	private Image bg;
	private Image bgOwned;
	private Texture weapon;
	private Image weaponActor;
	private Image weaponNameActor;
	
	private ItemBar rangeIndicatorBar;
	private ItemBar fireRateIndicatorBar;
	private ItemBar dmgIndicatorBar;
	
	private Image rangeImage, fireRateImage, damageImage;
	
	private EquippableItem item;
	
	
	public ItemInShopContainer(Stage stage, float x, float y, EquippableItem item){
		this.item = item;
		weapon =item.getShopTexture();
		
		bg = new Image(new Texture(Gdx.files.internal("img/shop/shopframe.png")));	
		stage.addActor(bg);
		bg.setPosition(x, y);
		bg.setVisible(false);
		
		bgOwned = new Image(new Texture(Gdx.files.internal("img/shop/shopframeowned3.png")));
		stage.addActor(bgOwned);
		bgOwned.setPosition(x, y);
		bgOwned.setVisible(false);
		
		
		int range = (int)((item.getRange() / Constants.MAX_RANGE)*60);
		int dmg = Math.abs((int)((item.getDamage() / Constants.MAX_DAMAGE)*60));
		int fireRate = (int)((item.getFireRate() / Constants.MAX_FIRE_RATE)*60);
		

		rangeIndicatorBar = new ItemBar(stage, (int)x+80, (int)y+10, range);
		rangeIndicatorBar.setVisibility(false);
		//------
		rangeImage = new Image(new Texture(Gdx.files.internal("img/shop/rangetext.png")));
		stage.addActor(rangeImage);
		rangeImage.setPosition(rangeIndicatorBar.getActorX() + rangeIndicatorBar.getWidth() / 2 - rangeImage.getWidth() / 2, rangeIndicatorBar.getActorY() + rangeIndicatorBar.getHeight() - 5);
		rangeImage.setVisible(false);
		
		healthBarTexture = (item.getItemType() == Constants.ItemType.CONSUMABLE) ? new Texture(Gdx.files.internal("img/shop/healtext.png")) : new Texture(Gdx.files.internal("img/shop/damagetext.png"));
		
		dmgIndicatorBar = new ItemBar(stage, (int)x+80, (int)y+40, dmg);
		dmgIndicatorBar.setVisibility(false);
		//------
		damageImage = new Image(healthBarTexture);
		stage.addActor(damageImage);
		damageImage.setPosition(dmgIndicatorBar.getActorX() + dmgIndicatorBar.getWidth() / 2 - damageImage.getWidth() / 2, dmgIndicatorBar.getActorY() + dmgIndicatorBar.getHeight() - 5);
		damageImage.setVisible(false);
		
		fireRateIndicatorBar = new ItemBar(stage, (int)x+80, (int)y+70, fireRate);
		fireRateIndicatorBar.setVisibility(false);
		//------
		fireRateImage = new Image(new Texture(Gdx.files.internal("img/shop/fireratetext.png")));
		stage.addActor(fireRateImage);
		fireRateImage.setPosition(fireRateIndicatorBar.getActorX() + fireRateIndicatorBar.getWidth() / 2 - fireRateImage.getWidth() / 2, fireRateIndicatorBar.getActorY() + fireRateIndicatorBar.getHeight() - 5);
		fireRateImage.setVisible(false);
		
		weaponActor = new Image (weapon);
		stage.addActor(weaponActor);
		weaponActor.setPosition(x+10, y+10);
		weaponActor.setVisible(false);
		
		weaponNameActor = new Image (new Texture(Gdx.files.internal("data/weapons/"+item.getItemName()+"/"+item.getItemName()+"nametext.png")));
		stage.addActor(weaponNameActor);
		weaponNameActor.setPosition(weaponActor.getX() + weaponActor.getWidth() / 2 - weaponNameActor.getWidth() / 2, weaponActor.getY() + weaponActor.getHeight());
		weaponNameActor.setVisible(false);
		
		
	}public void setContainerVisability(boolean visable){
		if(StageModel.quickSelection.existsInList(item) || StageModel.itemView.existsInList(item)) {
			bgOwned.setVisible(visable);
		}

		bg.setVisible(visable);
		weaponActor.setVisible(visable);
		weaponNameActor.setVisible(visable);
		rangeIndicatorBar.setVisibility(visable);
		dmgIndicatorBar.setVisibility(visable);
		fireRateIndicatorBar.setVisibility(visable);
		fireRateImage.setVisible(visable);
		damageImage.setVisible(visable);
		rangeImage.setVisible(visable);
		
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
	
	public EquippableItem getItem () {
		return item;
	}
	
}

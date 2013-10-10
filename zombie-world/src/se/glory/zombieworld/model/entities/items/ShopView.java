package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.entities.weapons.EMeleeWeapon;
import se.glory.zombieworld.model.entities.weapons.ERangedWeapon;
import se.glory.zombieworld.model.entities.weapons.WeaponArsenal;
import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ShopView {
	


	private EMeleeWeapon [] weapon;
	private ItemInShopContainer[] itemInShopContainers;
	private WeaponArsenal wa = new WeaponArsenal();
	private String[] strWeaponList;
	
	private Image background;
	
	public ShopView(Stage stage){ 
		
		background = new Image(new Texture(Gdx.files.internal("img/shop/shop.png")));
		stage.addActor(background);
		//background.setVisible(false);
		
		
		
		
		
		
		strWeaponList = wa.getWeaponList();
		itemInShopContainers = new ItemInShopContainer[strWeaponList.length];
		weapon = new  EMeleeWeapon[strWeaponList.length];
		
		for (int i=0;i<strWeaponList.length;i++){
			weapon[i] = wa.getWeapon(strWeaponList[i]);
		}
		
		for (int i=0;i<weapon.length;i++){
			
			itemInShopContainers[i] = new ItemInShopContainer(stage,180*i+50,300*1,weapon[i]);
	
		}
	}
	
	public void buyItem() {
		if(Gdx.input.isTouched()) {
			int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
			int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
		}
	}
	public void hideShopView(){
		background.setVisible(false);
		for (int i=0;i<itemInShopContainers.length;i++){
			itemInShopContainers[i].hideContainer();
		}
	}
	
}

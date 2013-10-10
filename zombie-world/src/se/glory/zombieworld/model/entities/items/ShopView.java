package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.entities.weapons.EMeleeWeapon;
import se.glory.zombieworld.model.entities.weapons.ERangedWeapon;
import se.glory.zombieworld.model.entities.weapons.WeaponArsenal;
import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ShopView {

	private EMeleeWeapon [] weapon;
	private ItemInShopContainer[] itemInShopContainers;
	private WeaponArsenal wa = new WeaponArsenal();
	private String[] strWeaponList;
	public ShopView(Stage stage){ 
		itemInShopContainers = new ItemInShopContainer[6];
		
		
		strWeaponList = wa.getWeaponList();
		weapon = new  EMeleeWeapon[strWeaponList.length];
		
		for (int i=0;i<strWeaponList.length;i++){
			weapon[i] = wa.getWeapon(strWeaponList[i]);
		}
		
		for (int i=0;i<weapon.length;i++){
			itemInShopContainers[i] = new ItemInShopContainer(stage,10*i,10*1,weapon[i]);
		}
		
	}
	
	public void buyItem() {
		if(Gdx.input.isTouched()) {
			int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
			int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
		}
	}
	
}

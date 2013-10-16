package se.glory.zombieworld.model.entities.items;

import se.glory.zombieworld.model.entities.weapons.ERangedWeapon;
import se.glory.zombieworld.model.entities.weapons.EquippableItem;
import se.glory.zombieworld.model.entities.weapons.WeaponArsenal;
import se.glory.zombieworld.screens.GameScreen;
import se.glory.zombieworld.utilities.ScreenCoordinates;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ShopView {
	

	private Image doneButton;
	private EquippableItem [] weapon;
	private ItemInShopContainer[] itemInShopContainers;
	private WeaponArsenal wa = new WeaponArsenal();
	private String[] strWeaponList;
	
	private Image background;
	
	public ShopView(Stage stage){ 
		
		background = new Image(new Texture(Gdx.files.internal("img/shop/shop.png")));
		stage.addActor(background);
		background.setVisible(false);
		
		doneButton = new Image(new Texture(Gdx.files.internal("img/shop/shopdone.png")));
		doneButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setShopViewVisability(false);//Här sätter du vart den ska länka
				System.out.println("clicked");
			}
		});
		doneButton.setPosition(400, 10);
		stage.addActor(doneButton);
		doneButton.setVisible(false);
		
		
		
		
		
		
		strWeaponList = wa.getWeaponList();
		itemInShopContainers = new ItemInShopContainer[strWeaponList.length];
		weapon = new EquippableItem[strWeaponList.length];
		
		for (int i=0;i<strWeaponList.length;i++){
			weapon[i] = wa.getWeapon(strWeaponList[i]);
		}
		
		for (int i=0;i<weapon.length;i++){
			
			itemInShopContainers[i] = new ItemInShopContainer(stage,180*i+50,300*1,weapon[i]);
	
		}
	}
	
	public void checkClicked() {
		if(Gdx.input.isTouched()) {
			int currentX = ScreenCoordinates.getRealX(Gdx.input.getX());
			int currentY = ScreenCoordinates.getRealY(Gdx.input.getY());
			for (int i=0;i<itemInShopContainers.length;i++){
				if (currentX>itemInShopContainers[i].getBgX()&&currentX<itemInShopContainers[i].getBgX() + itemInShopContainers[i].getBg().getImageWidth()
							&&currentY>itemInShopContainers[i].getBgY()&&currentY<itemInShopContainers[i].getBgY()+itemInShopContainers[i].getBg().getImageHeight()){
					
					itemInShopContainers[i].buyItem();	
					System.out.println("bought");
				}
					
			}
		}
	}
	public void setShopViewVisability(boolean visable){
		background.setVisible(visable);
		for (int i=0;i<itemInShopContainers.length;i++){
			itemInShopContainers[i].setContainerVisability(visable);
			doneButton.setVisible(visable);
		}
	}
	public boolean getShopViewVisabiliy(){
		return background.isVisible();
	}
}

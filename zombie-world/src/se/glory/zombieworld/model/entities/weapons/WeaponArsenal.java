package se.glory.zombieworld.model.entities.weapons;

import java.util.HashMap;
import java.util.Random;

import se.glory.zombieworld.utilities.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/*
 * Class used for reading and storing all weapons
 * available in the Game's weapons-folder.
 */
public class WeaponArsenal {
	private HashMap<String, EMeleeWeapon> weapons = new HashMap<String, EMeleeWeapon>();
	
	/*
	 * Public main constructor.
	 * This is used when the game is started.
	 * It loops through the weapons-folder to obtain all
	 * Images and weapon-data.
	 */
	public WeaponArsenal() {
		FileHandle weaponList = Gdx.files.internal("data/weapons/weapons.txt");
		String text = weaponList.readString();
		String[] weaponNames = text.split(System.getProperty("line.separator"));
		
		for(int i = 0; i < weaponNames.length; i++) {
			if(Gdx.files.internal("data/weapons/" + weaponNames[i]).exists()) {
				FileHandle statFile = Gdx.files.internal("data/weapons/" + weaponNames[i] + "/" + weaponNames[i] + ".txt");
				String[] stats = statFile.readString().split("::");
				if(stats[0].equals("melee")) {
					weapons.put(weaponNames[i], new EMeleeWeapon(weaponNames[i], Float.parseFloat(stats[Constants.WEAPON_DAMAGE]),
							Float.parseFloat(stats[Constants.WEAPON_RANGE])));
				} else {
					weapons.put(weaponNames[i], new ERangedWeapon(weaponNames[i], Float.parseFloat(stats[Constants.WEAPON_DAMAGE]),
							Float.parseFloat(stats[Constants.WEAPON_RANGE]), Integer.parseInt(stats[Constants.WEAPON_CLIP_SIZE]),
							Integer.parseInt(stats[Constants.WEAPON_CLIPS]), Float.parseFloat(stats[Constants.WEAPON_FIRE_RATE])));
				}
			}
		}
	}

	public EMeleeWeapon getWeapon(String weapon) {
		return weapons.get(weapon);
	}
	
	/*
	 * Returns a random weapon form the weapon arsenal. This method
	 * will be used to randomize out weapons on the map.
	 */
	public EMeleeWeapon getRandomWeapon() {
		String[] tmp = getWeaponList();
		Random r = new Random();
		return getWeapon(tmp[r.nextInt(tmp.length)]);
	}

	/* Returns an array containing the names of all available weapons */
	public String[] getWeaponList() {
		String[] names = new String[weapons.size()];
		Object[] weapon = weapons.keySet().toArray();
		
		for(int i = 0; i < weapon.length; i++) {
			names[i] = weapon[i].toString();
		}
		
		return names;
	}	
}
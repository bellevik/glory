package se.glory.zombieworld.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Score {
	public static long currentScore;
	
	public static void addScore(Constants.ScoreType type) {
		switch (type) {
		case KILL_HUMAN: 
			currentScore -= 1452;
		case KILL_ZOMBIE:
			currentScore += 97;
		case TIME:
			currentScore += 1;
		}
	}
	
	public static String[] getHighscoreList () {
		FileHandle file = Gdx.files.internal("highscore.txt");
		String text = file.readString();
		return text.split("::");
	}
	
	public static String getHighscoreAtPosition (int pos) {
		if (getHighscoreList().length <= pos) {
			return "";
		}
		String tmp[] = getHighscoreList()[pos].split(":");
		return tmp[0] + "   " + tmp[1];
	}
	
	public static void addNewHighscore (String highscore) {
		FileHandle file = Gdx.files.local("highscore.txt");
		//Write existing plus new or just new?
		file.writeString("::"+highscore, false);
	}
	// TODO SORT SCORE!
}
package se.glory.zombieworld.utilities;

import java.util.Arrays;

import se.glory.zombieworld.model.entities.weapons.EquippableItem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Score {
	public static int currentScore, zombiesKilled, humansKilled, shotsFired = 0;
	
	public static void addScore(Constants.ScoreType type) {
		switch (type) {
		case KILL_HUMAN: 
			currentScore -= 1452;
			humansKilled++;
		case KILL_ZOMBIE:
			currentScore += 126;
			zombiesKilled++;
		case TIME:
			currentScore += 1;
		}
	}
	
	public static void resetScore () {
		currentScore = 0;
	}
	
	public static UserScore[] getHighscoreList () {
		FileHandle file = Gdx.files.internal("highscore.txt");
		String text = file.readString();
		
		String[] tmp = text.split("::");
		UserScore[] score = new UserScore[tmp.length];
		
		for (int i = 0; i < score.length; i++) {
			String[] tmp1 = tmp[i].split(":");
			score[i] = new UserScore(tmp1[0], Integer.parseInt(tmp1[1]));
		}
		
		return score;
	}
	
	public static String getHighscoreAtPosition (int pos) {
		if (getHighscoreList().length <= pos) {
			return "";
		}
		UserScore[] score = getHighscoreList();
		return score[pos].name + "    " + score[pos].score;
	}
	
	public static void addNewHighscore (String name, int points) {
		UserScore[] score = new UserScore[6];
		for (int i = 0; i < getHighscoreList().length; i++) {
			score[i] = getHighscoreList()[i];
		}
		score[getHighscoreList().length] = new UserScore(name, points);
		
		for (int j = getHighscoreList().length + 1; j < 6; j++) {
			score[j] = new UserScore("", 0);
		}
		
		Arrays.sort(score);
		
		String hs = "";
		for (int j = 0; j < 4; j++) {
			hs += score[j].name + ":" + score[j].score + "::";
		}
		hs += score[4].name + ":" + score[4].score;
		
		FileHandle file = Gdx.files.local("highscore.txt");
		file.writeString(hs, false);
	}
	
}
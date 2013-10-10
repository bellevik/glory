package se.glory.zombieworld.utilities;


public class Score {
	public static long currentScore;
	
	public static void addScore(Constants.ScoreType type) {
		switch (type) {
		case KILL_HUMAN: 
			currentScore -= 111;
		case KILL_ZOMBIE:
			currentScore += 97;
		case TIME_POINT:
			currentScore += 1;
		}
	}
}

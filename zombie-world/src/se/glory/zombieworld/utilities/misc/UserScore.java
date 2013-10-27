package se.glory.zombieworld.utilities.misc;

public class UserScore implements Comparable<UserScore> {
	public final String name;
	public final int score;
	
	public UserScore (String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	@Override
	public int compareTo(UserScore o) {
		return (this.score > o.score) ? -1 : (this.score < o.score) ? 1 : 0;
	}
}
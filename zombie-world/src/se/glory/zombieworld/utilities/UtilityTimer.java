package se.glory.zombieworld.utilities;

public class UtilityTimer {

	private int interval = 1000;
	private long startTime = 0;
	private long elapsedTime = 0;
	
	public UtilityTimer(int interval){
		
		this.interval = interval;
		resetTimer();
		//Setting startTime to startTime minus interval to make the first check instant
		//Probably not necessary for this project
	//	startTime = startTime - interval;
	//	elapsedTime = System.currentTimeMillis() - startTime;
	}
	
	// Getters
	public int getInterval(){
		return interval;
	}
	public void stopTimer(){
		startTime = System.currentTimeMillis() - interval;
	}
	public boolean isDone(){
		elapsedTime = System.currentTimeMillis() - startTime;
		return (elapsedTime >= interval);
	}
	
	// Setters
	public void resetTimer(){
		startTime = System.currentTimeMillis();
		elapsedTime = 0;
	}	
}
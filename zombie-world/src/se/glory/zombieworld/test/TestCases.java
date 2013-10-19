package se.glory.zombieworld.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import se.glory.zombieworld.model.WorldModel;
import se.glory.zombieworld.model.entities.Creature;
import se.glory.zombieworld.model.entities.Human;
import se.glory.zombieworld.model.entities.Zombie;
import se.glory.zombieworld.utilities.AStarPathFinder;
import se.glory.zombieworld.utilities.Point;
import se.glory.zombieworld.utilities.UtilityTimer;
import se.glory.zombieworld.utilities.progressbars.Healthbar;

public class TestCases {

	WorldModel testWorld;
	ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	ArrayList<Creature> humans = new ArrayList<Creature>();
	Human testHuman;
	
	@Test
	public void testTimer() {
		//Creates a new timer with the interval 1000 milliseconds
		UtilityTimer timer = new UtilityTimer(1000);
		//Checks that the interval is what we wanted
		assertThat(timer.getInterval(), is(1000));
		//Resets the timer
		timer.resetTimer();
		//Checks that the timer is not done (has not passed 1 second)
		assertThat(timer.isDone(), is(false));
		//Waits for the timer to get through the 1 second
		while(!timer.isDone()){
			//Checks that the elapsed time has not reached the interval time yet
			assertTrue("Elapsed time less than interval", timer.getElapsedTime() < 1000);
			assertTrue("Elapsed time more than 0", timer.getElapsedTime() >= 0);
		}
		//Checks so the timer really is done
		assertThat(timer.isDone(), is(true));
		//Checks the elapsed time to see if it really is the same as interval
		//to make sure it is done
		assertThat(timer.getElapsedTime(), is((long)timer.getInterval()));
	}
	
	@Test
	public void testAStarPathFinder() {
		
		//Creates the list with blocked points the AStar algorithm has to get through
		ArrayList<Point> blocked = new ArrayList<Point>();
		
		blocked.add(new Point(4, 3));
		blocked.add(new Point(4, 4));
		blocked.add(new Point(4, 5));
		
		//Creates the correctPath to reference check with the shortestPath
		ArrayList<Point> correctPath = new ArrayList<Point>();
		
		correctPath.add(new Point(3, 5));
		correctPath.add(new Point(3, 6));
		correctPath.add(new Point(4, 6));
		correctPath.add(new Point(5, 6));
		correctPath.add(new Point(6, 5));
		correctPath.add(new Point(7, 4));
		
		//Creates the false path to doublecheck the assertThat method
		ArrayList<Point> falsePath = new ArrayList<Point>();
		
		falsePath.add(new Point(6, 1));
		falsePath.add(new Point(4, 4));
		falsePath.add(new Point(3, 3));
		falsePath.add(new Point(2, 2));
		falsePath.add(new Point(1, 1));
		falsePath.add(new Point(4, 7));
		
		int startX = 2;
		int startY = 4;
		
		int goalX = 7;
		int goalY = 4;
		
		//Creates the shortest path through the AStarPathFinder class
		ArrayList<Point> shortestPath = AStarPathFinder.getShortestPath(startX, startY, goalX, goalY, blocked);
		
		//Asserts the correct path has the same points as the shortestPath
		assertThat(correctPath, is(shortestPath));
		//Asserts the false path does not have the same points as the shortestPath to make sure the assertThat doesn't accept all paths
		assertThat(falsePath, not(shortestPath));
	}
}

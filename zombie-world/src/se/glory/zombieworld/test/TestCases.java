package se.glory.zombieworld.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import se.glory.zombieworld.model.entities.Creature;
import se.glory.zombieworld.model.entities.Human;
import se.glory.zombieworld.model.entities.Zombie;
import se.glory.zombieworld.utilities.AStarPathFinder;
import se.glory.zombieworld.utilities.Point;

import com.badlogic.gdx.physics.box2d.World;

public class TestCases {

	World testWorld;
	ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	ArrayList<Creature> humans = new ArrayList<Creature>();
	Human testHuman;
	
	@Test
	public void testReactions() {
		//difficult without creating a world...
		
	//	assertTrue("truetrue", new Vector2(20,0).x==20);
	//	testWorld = new World(new Vector2(0, 0), true);
	//	testHuman = new Human(testWorld, 150, 150);
	//	humans.add(testHuman);
	//	zombies.add(new Zombie(testWorld, 200,150));
		
	//	assertsEquals("Human starts at correct x position", 150, 150);
	//	assertsEquals(150, 150);
		assertNotNull("Human object null", testHuman);
		assertTrue("truetrue", testHuman.getBody().getPosition().x==150);
//		assertsEquals("Human starts at correct y position", expected, actual)
		
	//	testHuman.autoUpdateMovement(zombies);
		fail("Test not finished");
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
	
	@Test
	public void testHealthBar() {
		//Difficult without creating a stage....
		fail("Test not finished");
	}
}

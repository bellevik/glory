package se.glory.test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import se.glory.entities.Creature;
import se.glory.entities.Human;
import se.glory.entities.Zombie;
import se.glory.entities.ai.AStarPathFinder;

public class TestCases {

	World testWorld;
	ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	ArrayList<Creature> humans = new ArrayList<Creature>();
	Human testHuman;
	
	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	@Test
	public void testReactions() {
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

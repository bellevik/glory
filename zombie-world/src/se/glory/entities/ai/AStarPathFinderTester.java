package se.glory.entities.ai;

import java.awt.Point;
import java.util.ArrayList;

public class AStarPathFinderTester {
	public void testPathFinder() {
		ArrayList<Point> blocked = new ArrayList<Point>();
		
		blocked.add(new Point(4, 3));
		blocked.add(new Point(4, 4));
		blocked.add(new Point(4, 5));
		
		int startX = 2;
		int startY = 4;
		
		int goalX = 7;
		int goalY = 4;
		
		ArrayList<Point> shortestPath = AStarPathFinder.getShortestPath(startX, startY, goalX, goalY, blocked);
		
		for (Point p : shortestPath) {
			System.out.println(p.getX() + ":" + p.getY());
		}
	}
}

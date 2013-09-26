package se.glory.entities.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

/*
 * This is a static class implementing the path finding algorithm known as A*. 
 * Read about the algorithm on Wikipedia: http://en.wikipedia.org/wiki/A*_search_algorithm
 */

public class AStarPathFinder {
	
	/*
	 * Given a start position, a goal position and a list of "blocked" positions 
	 * the function returns an ArrayList of Points, key positions to go through 
	 * to get to the goal without entering a blocked position. 
	 */
	public static ArrayList<Point> getShortestPath(int startX, int startY, int goalX, int goalY, ArrayList<Point> blocked) {
		// Create the open and the closed list.
		SortedNodeList open = new SortedNodeList();
		SortedNodeList closed = new SortedNodeList();
		
		// Create a node with the goal position.
		Node goal = new Node(goalX, goalY);
		
		// Add a node with the start position in the open list.
		open.add(new Node(startX, startY, goal));
		
		// Declare the "current" node, this is the node we are currently looking into.
		Node current;
		
		while (true) {
			// Get the node from the open list with the lowest F value.
			current = open.getFirst();
			
			// If the current node we are checking is located at the goal, we are done.
			if (current.equals(goal))
				break;
			
			// Remove the node from the open list and add it to the closed list.
			open.remove(current);
			closed.add(current);
			
			// Add all the surrounding nodes which are valid.
			addSurroundingNodes(current, goal, open, closed, blocked);
		}
		
		ArrayList<Point> shortestPath = new ArrayList<Point>();
		
		/*
		 * Trace the path backwards through the use of every nodes parent 
		 * and add the position to the list of key positions.
		 */
		while (current.getParent() != null) {
			shortestPath.add(new Point(current.getX(), current.getY()));
			current = current.getParent();
		}
		
		// Reverse the list of key positions and return the list.
		Collections.reverse(shortestPath);
		return shortestPath;
	}
	
	/*
	 * Check all nodes in all 8 directions from the node n, 
	 * if valid, add the node to the open list.
	 */
	private static void addSurroundingNodes(Node n, Node goal, SortedNodeList open, SortedNodeList closed, ArrayList<Point> blocked) {
		boolean north = false, east = false, south = false, west = false;
		boolean ne, se, sw, nw;
		
		// North
		if (!isBlocked(n.getX(), n.getY()+1, blocked)) {
			addToOpenList(new Node(n.getX(), n.getY()+1, n, goal), open, closed);
			north = true;
		}
		
		// East
		if (!isBlocked(n.getX()+1, n.getY(), blocked)) {
			addToOpenList(new Node(n.getX()+1, n.getY(), n, goal), open, closed);
			east = true;
		}
		
		// South
		if (!isBlocked(n.getX(), n.getY()-1, blocked)) {
			addToOpenList(new Node(n.getX(), n.getY()-1, n, goal), open, closed);
			south = true;
		}
		
		// West
		if (!isBlocked(n.getX()-1, n.getY(), blocked)) {
			addToOpenList(new Node(n.getX()-1, n.getY(), n, goal), open, closed);
			west = true;
		}
		
		/*
		 * Diagonal movement, check for corner cutting.
		 * E.g. if the north and east node isn't blocked, 
		 * we may walk on the north-east node - if it isn't blocked.
		 */
		ne = north && east;
		se = south && east;
		sw = south && west;
		nw = north && west;
		
		if (ne && !isBlocked(n.getX()+1, n.getY()+1, blocked))
			addToOpenList(new Node(n.getX()+1, n.getY()+1, n, goal), open, closed);
		
		if (se && !isBlocked(n.getX()+1, n.getY()-1, blocked))
			addToOpenList(new Node(n.getX()+1, n.getY()-1, n, goal), open, closed);
		
		if (sw && !isBlocked(n.getX()-1, n.getY()-1, blocked))
			addToOpenList(new Node(n.getX()-1, n.getY()-1, n, goal), open, closed);
		
		if (nw && !isBlocked(n.getX()-1, n.getY()+1, blocked))
			addToOpenList(new Node(n.getX()-1, n.getY()+1, n, goal), open, closed);
	}
	
	// Check if the position [x, y] is on the list of blocked positions.
	private static boolean isBlocked(int x, int y, ArrayList<Point> blocked) {
		for (Point p : blocked) {
			if (p.x == x && p.y == y)
				return true;
		}
		
		return false;
	}
	
	// Check if the position [x, y] is on the list of closed positions.
	private static boolean isClosed(int x, int y, SortedNodeList closed) {
		return closed.contains(new Node(x, y));
	}
	
	private static void addToOpenList(Node n, SortedNodeList open, SortedNodeList closed) {
		// Don't add the node to the open list if it is already on the closed list.
		if (isClosed(n.getX(), n.getY(), closed))
			return;
		
		// If the node isn't on the open list, add it.
		if (!open.contains(n)) {
			open.add(n);
		} else {
			// If the node is already on the open list, check if going this way is better
			if (open.get(n).getG() > n.getG()) {
				// If so, set the "current" node as the parent
				open.get(n).setParent(n.getParent());
				open.sort();
			}
		}
	}
	
	private static class SortedNodeList {
        private ArrayList<Node> list = new ArrayList<Node>();

        public Node getFirst() {
                return list.get(0);
        }

        public void add(Node node) {
                list.add(node);
                sort();
        }

        public void remove(Node n) {
                list.remove(n);
        }
        
        public Node get(Node needle) {
        	for (Node n : list) {
            	if (n.getX() == needle.getX() && n.getY() == needle.getY())
            		return n;
            }
            
            return null;
        }
        
        public void sort() {
        	Collections.sort(list);
        }

        public boolean contains(Node needle) {
            for (Node n : list) {
            	if (n.getX() == needle.getX() && n.getY() == needle.getY())
            		return true;
            }
            
            return false;
        }
	}
	
	private static class Node implements Comparable<Node> {
		private int x, y;
		private double f, g, h;
		private Node parent;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Node(int x, int y, Node goal) {
			this(x, y);	
			
			// Calculate the distance cost (H) with the Manhattan-formula.
			h = (Math.abs(x - goal.getX()) + Math.abs(y - goal.getY())) * 10;
			
			// Calculate the distance cost (H) with the Euclidean-formula.
			// h = Math.sqrt((x - goal.getX()) * (x - goal.getX()) + (y - goal.getY()) * (y - goal.getY())) * 10;
		}

		public Node(int x, int y, Node parent, Node goal) {
			this(x, y, goal);
			setParent(parent);
		}

		public double getF() {
			return f;
		}

		public double getG() {
			return g;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
			
			/*
			 * Calculate the direction cost (G).
			 * If the direction is straight, set it to 10 + [the parent's G value].
			 * If the direction is diagonal, set it to 14 + [the parent's G value].
			 */
			if (getX() != parent.getX() && getY() != parent.getY())
				g = 14 + parent.getG();
			else
				g = 10 + parent.getG();
			
			// Update F, the total cost of going through this node.
			f = g + h;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public int compareTo(Node other) {
			return Double.compare(getF(), other.getF());
		}
		
		public boolean equals(Node n) {
			if (getX() == n.getX() && getY() == n.getY())
				return true;
			else
				return false;
		}
	}
}
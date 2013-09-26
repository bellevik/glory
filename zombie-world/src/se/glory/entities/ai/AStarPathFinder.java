package se.glory.entities.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class AStarPathFinder {
	public static ArrayList<Point> getShortestPath(int startX, int startY, int goalX, int goalY, ArrayList<Point> blocked) {
		SortedNodeList open = new SortedNodeList();
		SortedNodeList closed = new SortedNodeList();
		
		// Create goal node
		Node goal = new Node(goalX, goalY);
		
		// Add start node
		open.add(new Node(startX, startY, goal));
		
		// Setup the "current" node
		Node current;
		
		while (true) {
			// Get the square from the open list with the lowest F value
			current = open.getFirst();
			
			// If the current node we are checking is located at the goal, we are done
			if (current.equals(goal))
				break;
			
			// Switch to closed list
			open.remove(current);
			closed.add(current);
						
			addSurroundingNodes(current, goal, open, closed, blocked);
		}
		
		ArrayList<Point> shortestPath = new ArrayList<Point>();
		
		while (current.getParent() != null) {
			shortestPath.add(new Point(current.getX(), current.getY()));
			current = current.getParent();
		}
		
		Collections.reverse(shortestPath);
		return shortestPath;
	}
	
	private static void addSurroundingNodes(Node n, Node goal, SortedNodeList open, SortedNodeList closed, ArrayList<Point> blocked) {
		if (!isBlockedOrClosed(n.getX(), n.getY()+1, closed, blocked))
			addToOpenList(new Node(n.getX(), n.getY()+1, n, goal), open);
		if (!isBlockedOrClosed(n.getX()+1, n.getY()+1, closed, blocked))
			addToOpenList(new Node(n.getX()+1, n.getY()+1, n, goal), open);
		if (!isBlockedOrClosed(n.getX()+1, n.getY(), closed, blocked))
			addToOpenList(new Node(n.getX()+1, n.getY(), n, goal), open);
		if (!isBlockedOrClosed(n.getX()+1, n.getY()-1, closed, blocked))
			addToOpenList(new Node(n.getX()+1, n.getY()-1, n, goal), open);
		if (!isBlockedOrClosed(n.getX(), n.getY()-1, closed, blocked))
			addToOpenList(new Node(n.getX(), n.getY()-1, n, goal), open);
		if (!isBlockedOrClosed(n.getX()-1, n.getY()-1, closed, blocked))
			addToOpenList(new Node(n.getX()-1, n.getY()-1, n, goal), open);
		if (!isBlockedOrClosed(n.getX()-1, n.getY(), closed, blocked))
			addToOpenList(new Node(n.getX()-1, n.getY(), n, goal), open);
		if (!isBlockedOrClosed(n.getX()-1, n.getY()+1, closed, blocked))
			addToOpenList(new Node(n.getX()-1, n.getY()+1, n, goal), open);
	}
	
	private static boolean isBlockedOrClosed(int x, int y, SortedNodeList closed, ArrayList<Point> blocked) {
		if (isBlocked(x, y, blocked) || isClosed(x, y, closed))
			return true;
		
		return false;
	}
	
	private static boolean isBlocked(int x, int y, ArrayList<Point> blocked) {
		for (Point p : blocked) {
			if (p.x == x && p.y == y)
				return true;
		}
		
		return false;
	}
	
	private static boolean isClosed(int x, int y, SortedNodeList closed) {
		return closed.contains(new Node(x, y));
	}
	
	private static void addToOpenList(Node n, SortedNodeList open) {
		if (!open.contains(n)) {
			open.add(n);
		} else {
			// Check if this is a better path
			if (open.get(n).getG() > n.getG()) {
				// Set the "current" node as parent
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
	
	public static class Node implements Comparable<Node> {
		private int x, y;
		private double f, g, h;
		private Node parent;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Node(int x, int y, Node goal) {
			// Manhattan distance
			h = (Math.abs(x - goal.getX()) + Math.abs(y - goal.getY())) * 10;
			
			// Euclidean distance
			// h = Math.sqrt((x - goal.getX()) * (x - goal.getX()) + (y - goal.getY()) * (y - goal.getY())) * 10;
		}

		public Node(int x, int y, Node parent, Node goal) {
			this(x, y, goal);
			setParent(parent);
		}

		public double getF() {
			return f;
		}

		public void setF(double f) {
			this.f = f;
		}

		public double getG() {
			return g;
		}

		public void setG(double g) {
			this.g = g;
		}

		public double getH() {
			return h;
		}

		public void setH(double h) {
			this.h = h;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
			
			// Cost of direction
			if (getX() != parent.getX() && getY() != parent.getY())
				g = parent.getG() + 14;
			else
				g = parent.getG() + 10;
			
			// Update F
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
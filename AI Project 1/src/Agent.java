import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public abstract class Agent 
{
	HashMap<GridCell, Double> cost = new HashMap<GridCell, Double>();
	HashMap<GridCell, GridCell> parent;
	PriorityQueue<GridPairs> fringe;
	TreeSet<GridCell> closed;
	ArrayList<GridCell> path;
	
	protected GridCell start;
	protected GridCell end;
	protected int moveCount;
	protected int cellsExplored;
	protected double totalDistance;
	protected long runTime;
	protected long usedMem;
	
	public Agent(GridCell home, GridCell goal)
	{
		start = home;
		end = goal;
		moveCount = 0;
		cellsExplored = 0;
		totalDistance = 0;
	}

	public String getStats()
	{
		return ("Runtime: " + runTime/1000000 + " ms\n" +
				"Memory Used: " + usedMem + " bytes\n" +
				"Cells Explored: " + cellsExplored + "\n" +
				"Cells in Path: " + moveCount + "\n" + 
				"Total Distance (cost): " + totalDistance);
	}
	
	public abstract double heuristic(GridCell source);
	
	public ArrayList<GridCell> search(Grid homeGrid)
	{
		System.gc();
		cost = new HashMap<GridCell, Double>();
		cost.put(start, new Double(0));
		
		parent = new HashMap<GridCell, GridCell>();
		parent.put(start, start);
		
		fringe = new PriorityQueue<GridPairs>();
		fringe.add(new GridPairs(start, heuristic(start)));
		
		closed = new TreeSet<GridCell>();
		
		path = new ArrayList<GridCell>();
		
		runTime = System.nanoTime();
		usedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
				
		while(fringe.size() > 0) //While cells on the edge of the explored space still exist
		{
			GridPairs gp = fringe.poll(); //Get the cell with the smallest cost
			GridCell currCell = gp.getCell(); //Get that cell
			GridPairs temp;
			
			if(currCell.equals(end)) //If the goal is located, return the path to the goal
			{
				runTime = System.nanoTime() - runTime;
				usedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory() - usedMem;
				totalDistance = cost.get(currCell);
				cellsExplored = closed.size();
				
				do {
					path.add(0, parent.get(currCell));
					currCell = parent.get(currCell);
				}while(!(currCell.equals(start)));
				
				moveCount = path.size();
				
				return path;
			}
			
			closed.add(currCell); //Close the cell we just took out of the fringe
			
			ArrayList<GridCell> neighbors = homeGrid.getNeighbors(currCell); //Get that cell's neighbors
			
			for(GridCell i : neighbors)
			{
				temp = new GridPairs(i, 0);
				
				if(!(closed.contains(i))) //Does this neighbor need to be explored?
				{
					if(!(fringe.contains(temp))) //Does this neighbor need to be added to the fringe?
					{
						cost.put(i, new Double(Double.MAX_VALUE)); //We don't know the cost of this cell yet, so we set it to be changed later
						parent.put(i, null); //We don't know which cell leads to this cell the fastest, so we set it to be changed later
					}
					
					Double newCost = new Double((cost.get(currCell) + currCell.distance(i))); //New cost based off traveling from the current cell to this neighbor
					
					if(newCost < cost.get(i)) //Is the new cost cheaper than previous discovered costs?
					{
						cost.put(i, newCost); //If it is, update the cost
						parent.put(i, currCell); //Set it as the new fastest path
						if(fringe.contains(temp)) //If the fringe already has this neighbor
						{
							fringe.remove(temp); //Remove it so we do not duplicate it when we add
						}
						
						fringe.add(new GridPairs(i, cost.get(i) + heuristic(i))); //Add it to the fringe with its updated cost from the start to here + here to best possible end
					}
				}
			}
		}
		
		return null;
	}
}

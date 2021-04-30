import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Sequential extends Agent
{	
	ArrayList<HashMap<GridCell, Double>> costList;
	ArrayList<HashMap<GridCell, GridCell>> parentList;
	ArrayList<PriorityQueue<GridPairs>> fringeList;
	ArrayList<TreeSet<GridCell>> closedList;
	
	double w1;
	double w2;
	
	public Sequential(GridCell home, GridCell goal, double weight1, double weight2)
	{
		super(home, goal);
		start = home;
		end = goal;
		moveCount = 0;
		cellsExplored = 0;
		totalDistance = 0;
		w1 = weight1;
		w2 = weight2;
	}
	
	public double heuristic(GridCell source, int index)
	{
		double delX = Math.abs(source.x()-end.x());
		double delY = Math.abs(source.y()-end.y());
		
		switch(index)
		{
			default:
			case 0:
				return (0.25 * (delX + delY));
			case 1:
				return (Math.sqrt(2)*Math.min(delX, delY) + 0.25*(Math.max(delX, delY) - Math.min(delX, delY)));
			case 2:
				return (Math.sqrt(2)*Math.min(delX, delY) + Math.max(delX, delY) - Math.min(delX, delY));
			case 3:
				return (Math.sqrt((delX*delX) + (delY*delY)));
			case 4:
				return (0.25 * (delX + delY))*2;
		}
	}
	
	public double key(GridCell vert, int index)
	{
		return costList.get(index).get(vert) + w1*heuristic(vert, index);
	}
	
	public void expandStates(Grid homeGrid, GridCell vert, int index)
	{
		GridPairs temp = new GridPairs(vert, 0);
		
		fringeList.get(index).remove(temp);
		
		ArrayList<GridCell> neighbors = homeGrid.getNeighbors(vert); //Get that cell's neighbors
		
		for(GridCell i : neighbors)
		{
			GridPairs tempN = new GridPairs(i, 0);
			
			if(!(closedList.get(index).contains(i))) //Does this neighbor need to be explored?
			{
				if(!(fringeList.get(index).contains(tempN))) //Does this neighbor need to be added to the fringe?
				{
					costList.get(index).put(i, new Double(Double.MAX_VALUE)); //We don't know the cost of this cell yet, so we set it to be changed later
					parentList.get(index).put(i, null); //We don't know which cell leads to this cell the fastest, so we set it to be changed later
				}
				
				Double newCost = new Double((costList.get(index).get(vert) + vert.distance(i))); //New cost based off traveling from the current cell to this neighbor
				
				if(newCost < costList.get(index).get(i)) //Is the new cost cheaper than previous discovered costs?
				{
					costList.get(index).put(i, newCost); //If it is, update the cost
					parentList.get(index).put(i, vert); //Set it as the new fastest path
					if(!(closedList.get(index).contains(i)))
					{
						if(fringeList.get(index).contains(tempN)) //If the fringe already has this neighbor
						{
							fringeList.get(index).remove(tempN); //Remove it so we do not duplicate it when we add
						}
						
						fringeList.get(index).add(new GridPairs(i, key(i, index))); //Add it to the fringe with its updated cost from the start to here + here to best possible end
					}
				}
			}
		}
	}
	
	public ArrayList<GridCell> getPath(int index)
	{
		ArrayList<GridCell> path = new ArrayList<GridCell>();
		
		runTime = System.nanoTime() - runTime;
		usedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory() - usedMem;
		totalDistance = costList.get(index).get(end);
		for(int i = 0; i < 5; i++)
			cellsExplored += closedList.get(i).size();
		
		GridCell currCell = end;
		
		do {
			path.add(0, parentList.get(index).get(currCell));
			currCell = parentList.get(index).get(currCell);
		}while(!(currCell.equals(start)));
		
		moveCount = path.size();
		
		return path;
	}
	
	@Override
	public ArrayList<GridCell> search(Grid homeGrid)
	{
		System.gc();
		cellsExplored = 0;
		costList = new ArrayList<HashMap<GridCell, Double>>();
		parentList = new ArrayList<HashMap<GridCell, GridCell>>();
		fringeList = new ArrayList<PriorityQueue<GridPairs>>();
		closedList = new ArrayList<TreeSet<GridCell>>();
		
		for(int i = 0; i < 5; i++)
		{
			costList.add(new HashMap<GridCell, Double>());
			parentList.add(new HashMap<GridCell, GridCell>());
			fringeList.add(new PriorityQueue<GridPairs>());
			closedList.add(new TreeSet<GridCell>());
			
			costList.get(i).put(start, new Double(0));
			costList.get(i).put(end, Double.MAX_VALUE);
			
			parentList.get(i).put(start, null);
			parentList.get(i).put(end, null);
			
			fringeList.get(i).add(new GridPairs(start, key(start, i)));
		}
		
		runTime = System.nanoTime();
		usedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		
		while(fringeList.get(0).peek().getKey() < Double.MAX_VALUE) //While cells on the edge of the explored space still exist
		{
			for(int i = 1; i < 5; i++)
			{
				double minKey = fringeList.get(i).peek().getKey();
				if(minKey <= w2*fringeList.get(0).peek().getKey())
				{
					if(costList.get(i).get(end) <= minKey)
					{
						if(costList.get(i).get(end) < Double.MAX_VALUE)
						{
							return getPath(i);
						}
					}
					else
					{
						GridPairs temp = fringeList.get(i).poll();
						expandStates(homeGrid, temp.getCell(), i);
						closedList.get(i).add(temp.getCell());
					}
				}
				else
				{
					if(costList.get(0).get(end) <= fringeList.get(0).peek().getKey())
					{
						if(costList.get(0).get(end) < Double.MAX_VALUE)
						{
							return getPath(0);
						}
					}
					else
					{
						GridPairs temp = fringeList.get(0).poll();
						expandStates(homeGrid, temp.getCell(), 0);
						closedList.get(0).add(temp.getCell());
					}
				}
			}
		}
		
		return null;
	}

	@Override
	public double heuristic(GridCell source) 
	{
		return 0;
	}
}

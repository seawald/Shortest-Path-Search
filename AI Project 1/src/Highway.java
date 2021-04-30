import java.util.ArrayList;
import java.util.Random;

public class Highway 
{
	ArrayList<GridCell> path;
	Random rand;
	GridCell[][] map;
	GridCell start;
	boolean redo;
	
	public Highway(GridCell source, int edge, GridCell[][] inMap, Random rng)
	{
		path = new ArrayList<GridCell>();
		rand = rng;
		start = source;
		map = inMap;
		redo = false;
	}
	
	public void printPath()
	{
		for(int i = 0; i < path.size(); i++)
			System.out.println(path.get(i).toString());
	}
	
	public boolean invalidStart()
	{
		return redo;
	}
	
	public GridCell[][] getMap()
	{
		return map;
	}
	
	public void plotHighway(int edge)
	{
		path = new ArrayList<GridCell>();
		redo = false;
		GridCell temp;
		int vertX = start.x();
		int vertY = start.y();
		int incX;
		int incY;
		int chance;
		boolean atEdge;
		
		if(edge == 0) //left side start
		{
			incX = 1;
			incY = 0;
		}
		else if(edge == 1) //top side start
		{
			incX = 0;
			incY = 1;
		}
		else if(edge == 2) //right side start
		{
			incX = -1;
			incY = 0;
		}
		else if(edge == 3) //bottom side start
		{
			incX = 0;
			incY = -1;
		}
		else
		{
			redo = true;
			return;
		}
		
		for(int i = 0; i < 20; i++)
		{
			temp = map[vertX+(i*incX)][vertY+(i*incY)];
			
			if(temp.getHighway())
			{
				redo = true;
				return;
			}
			
			path.add(temp);
		}
		
		atEdge = false;
		
		while(!atEdge)
		{
			chance = rand.nextInt(100)+1;
			
			vertX = path.get(path.size()-1).x();
			vertY = path.get(path.size()-1).y();
			
			if(chance <= 40)
			{
				if(incX == 0)
				{
					if(chance <= 20)
						incX = 1;
					else
						incX = -1;
					incY = 0;
				}
				else
				{
					incX = 0;
					if(chance <= 20)
						incY = 1;
					else
						incY = -1;
				}
			}
			
			for(int i = 1; i < 20; i++)
			{
				if(vertX+(i*incX) < 0 || vertX+(i*incX) > map.length-1 || vertY+(i*incY) < 0 || vertY+(i*incY) > map[0].length-1)
				{
					atEdge = true;
					break;
				}
				
				temp = map[vertX+(i*incX)][vertY+(i*incY)];
				
				if(temp.getHighway() || path.contains(temp))
				{
					redo = true;
					return;
				}
				
				path.add(temp);
			}
		}
		
		if(path.size() < 100)
		{
			redo = true;
			return;
		}
		
		for(int i = 0; i < path.size(); i++)
		{
			path.get(i).setHighway(true);
			map[path.get(i).x()][path.get(i).y()] = path.get(i);
		}
		path = new ArrayList<GridCell>();
	}
}

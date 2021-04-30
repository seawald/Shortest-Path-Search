
public class Handler 
{
	Grid g;
	long seed;
	Agent a;
	
	double w1;
	double w2;
	
	long avgRunTime;
	long avgUsedMem;
	double avgMoveCount;
	double avgCellsExplored;
	double avgTotalDistance;
	
	public Handler()
	{
		seed = System.currentTimeMillis();
		g = new Grid(160, 120, seed);
		
		w1 = 1;
		w2 = 1;
		
		avgRunTime = 0;
		avgUsedMem = 0;
		avgMoveCount = 0;
		avgCellsExplored = 0;
		avgTotalDistance = 0;
	}
	
	public Handler(long sd)
	{
		seed = sd;
		g = new Grid(160, 120, seed);
		
		w1 = 1;
		w2 = 1;
		
		avgRunTime = 0;
		avgUsedMem = 0;
		avgMoveCount = 0;
		avgCellsExplored = 0;
		avgTotalDistance = 0;
	}
	
	public void printSeed()
	{
		System.out.println("Seed: " + seed);
	}
	
	public void printGrid()
	{
		g.display();
	}
	
	public void setWeight(double w)
	{
		w1 = w;
	}
	
	public void setWeight(double w1, double w2)
	{
		this.w1 = w1;
		this.w2 = w2;
	}
	
	public void printCellInfo(int x, int y)
	{
		GridCell cell = g.getCell(x, y);
		double c = a.cost.get(cell);
		double h = a.heuristic(cell);
		
		System.out.println("---Cell: " + cell.toString() + "---");
		System.out.println("Cost: " + c);
		System.out.println("Heuristic: " + h);
		System.out.println("f value: " + (c+h));
		System.out.println("---End of Info---");
	}
	
	public Agent getSearchType(int type)
	{
		switch(type)
		{
			default:
			case 0:
				return new AgentAStar(g.start, g.end);
			case 1:
				return new AgentUniformCost(g.start,g.end);
			case 2:
				return new AgentAStar(g.start, g.end, w1);
			case 3:
				return new Sequential(g.start, g.end, w1, w2);
		}
	}
	
	public void makeNewMap()
	{
		g.hardPoints = g.harden(8);
		g.highways(4);
		g.block(20);
		g.placeGoals();
	}
	
	public boolean buildNewMap(String path)
	{
		if(g.buildFromFile(path))
			return true;
		
		return false;
	}
	
	public void runSearch(int type) 
	{
		a = g.buildPath(getSearchType(type));
		System.out.println("-------Search Statistics-------\n" + a.getStats());
	}
	
	public void outputMap()
	{
		g.output();
	}
	
	public void benchmark(int type)
	{
		for(int i = 1; i <= 5; i++)
		{
			for(int j = 1; j <= 10; j++)
			{
				g.buildFromFile("C:/Users/Seawald/eclipse-workspace/AI Project 1/testMaps/input" + i + "/input" + j + ".txt");
				a = g.buildPath(getSearchType(type));
				avgRunTime += a.runTime;
				avgUsedMem += a.usedMem;
				avgMoveCount += a.moveCount;
				avgCellsExplored += a.cellsExplored;
				avgTotalDistance += a.totalDistance;
			}
		}
	
	avgRunTime /= 50;
	avgUsedMem /= 50;
	avgMoveCount /= 50;
	avgCellsExplored /= 50;
	avgTotalDistance /= 50;
	
	System.out.println("Average runtime: " + avgRunTime/1000000 + " ms");
	System.out.println("Average memory usage: " + avgUsedMem + " bytes");
	System.out.println("Average move count: " + avgMoveCount);
	System.out.println("Average cells explored: " + avgCellsExplored);
	System.out.println("Average total distance (cost): " + avgTotalDistance);
	}
}

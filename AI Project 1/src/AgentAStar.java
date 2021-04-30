public class AgentAStar extends Agent
{
	double weight;
	
	public AgentAStar(GridCell home, GridCell goal) 
	{
		super(home, goal);
		weight = 1;
	}
	
	public AgentAStar(GridCell home, GridCell goal, double w) 
	{
		super(home, goal);
		weight = w;
	}

	@Override
	public double heuristic(GridCell source) 
	{
		double delX = Math.abs(source.x()-end.x());
		double delY = Math.abs(source.y()-end.y());
		return (0.25 * (delX + delY)) * weight;
		//return (Math.sqrt(2)*Math.min(delX, delY) + Math.max(delX, delY) - Math.min(delX, delY));
		//return (Math.sqrt(2)*Math.min(delX, delY) + 0.25*(Math.max(delX, delY) - Math.min(delX, delY)));
	}
}

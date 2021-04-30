public class AgentUniformCost extends Agent
{

	public AgentUniformCost(GridCell home, GridCell goal) 
	{
		super(home, goal);
	}

	@Override
	public double heuristic(GridCell source) 
	{
		return 0;
	}
}

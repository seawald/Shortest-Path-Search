public class GridPairs implements Comparable<GridPairs>
{
	private GridCell cell;
	private double key;
	
	public GridPairs(GridCell one, double dis) 
	{
		cell = one;
		key = dis;
	}
	
	public GridCell getCell()
	{
		return cell;
	}
	
	public double getKey()
	{
		return key;
	}

	@Override
	public boolean equals(Object o)
	{
		GridPairs other;
		if(o == null || !(o instanceof GridPairs))
		{
			return false;
		}
		
		other = (GridPairs) o;
		
		return (cell.equals(other.getCell()));
	}
	
	@Override
	public int compareTo(GridPairs o) 
	{
		if(this.key < o.key)
			return -1;
		else if(this.key == o.key)
			return 0;
		else
			return 1;
	}
}

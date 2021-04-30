public class GridCell implements Comparable<GridCell>
{
	private int x;
	private int y;
	private int blockedStatus; //0 is default and unblocked, 1 is partially blocked, 2 is blocked off
	private boolean isHighway;
	private boolean isStart;
	private boolean isEnd;
	private boolean isPath;
	
	public GridCell(int x, int y)
	{
		this.x = x;
		this.y = y;
		blockedStatus = 0;
	}
	
	public GridCell(int x, int y, int status)
	{
		this.x = x;
		this.y = y;
		blockedStatus = status;
	}
	
	public int getStatus()
	{
		return blockedStatus;
	}
	
	public void setStatus(int status)
	{
		blockedStatus = status;
	}
	
	public boolean getHighway()
	{
		return isHighway;
	}
	
	public void setHighway(boolean highway)
	{
		isHighway = highway;
	}
	
	public void setStart()
	{
		isStart = true;
		if(blockedStatus == 2)
		{
			blockedStatus = 0;
		}
	}
	
	public boolean getStart()
	{
		return isStart;
	}
	
	public void setEnd()
	{
		isEnd = true;
		if(blockedStatus == 2)
		{
			blockedStatus = 0;
		}
	}
	
	public boolean getEnd()
	{
		return isEnd;
	}
	
	public boolean getPath()
	{
		return isPath;
	}
	
	public void setPath(boolean in)
	{
		isPath = in;
	}
	
	public int x()
	{
		return x;
	}
	
	public int y()
	{
		return y;
	}
	
	public int status()
	{
		return blockedStatus;
	}
	
	public boolean spotEquals(GridCell other)
	{
		if(x == other.x() && y == other.y())
		{
			return true;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return ("(" + x + "," + y + ")");
	}
	
	@Override
	public boolean equals(Object o)
	{
		GridCell other;
		if(o == null || !(o instanceof GridCell))
		{
			return false;
		}
		
		other = (GridCell) o;
				
		if(x == other.x() && y == other.y() && blockedStatus == other.getStatus() && isHighway == other.getHighway())
		{
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return (x+y)*(x+y+1)/2+y;
	}
	
	@Override
	public int compareTo(GridCell inCell) 
	{
		if(this.hashCode() < inCell.hashCode())
			return -1;
		else if(this.hashCode() == inCell.hashCode())
			return 0;
		else
			return 1;
	}
	
	public double distance (GridCell neighbor)
	{
		double output = 0;
		boolean isDiagonal = true;
		if(x == neighbor.x() || y == neighbor.y()) //Diagonal check
		{
			isDiagonal = false;
		}
		
		if(blockedStatus == 0)
		{
			if(isDiagonal)
			{
				if (neighbor.getStatus() == 0)
					output = Math.sqrt(2);
				else if (neighbor.getStatus() == 1)
					output = ((Math.sqrt(2) + Math.sqrt(8)) / 2);
				else
					output = 0;
			}
			else
			{
				if (neighbor.getStatus() == 0)
					output = 1;
				else if (neighbor.getStatus() == 1)
					output = 1.5;
				else
					output = 0;
			}
		}
		
		if(blockedStatus == 1)
		{
			if(isDiagonal)
			{
				if (neighbor.getStatus() == 0)
					output = ((Math.sqrt(2) + Math.sqrt(8)) / 2);
				else if (neighbor.getStatus() == 1)
					output = Math.sqrt(8);
				else
					output = 0;
			}
			else
			{
				if (neighbor.getStatus() == 0)
					output = 1.5;
				else if (neighbor.getStatus() == 1)
					output = 2;
				else
					output = 0;
			}
		}
		
		if(isHighway && !isDiagonal)
			output /= 4;
		
		return output;
	}
}

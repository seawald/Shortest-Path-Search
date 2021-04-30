import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Grid 
{
	int rows;
	int columns;
	int totalCells;
	GridCell[][] map;
	GridCell[] hardPoints;
	GridCell start;
	GridCell end;
	Random rand;
	long seed;
	
	public Grid(int numColumns, int numRows, long sd)
	{
		columns = numColumns;
		rows = numRows;
		totalCells = rows*columns;
		map = new GridCell[columns][rows];
		rand = new Random();
		seed = sd;
		rand.setSeed(seed);
		
		for(int i = 0; i < columns; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				map[i][j] = new GridCell(i, j);
			}
		}
	}
	
	public GridCell getCell(int x, int y)
	{
		return map[x][y];
	}
	
	public void display()
	{
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
		for(int j = 0; j < rows; j++)
		{
			for(int i = 0; i < columns; i++)
			{
				if(map[i][j].getStart())
					System.out.print("+");
				else if(map[i][j].getEnd())
					System.out.print("=");
				else if(map[i][j].getPath())
					System.out.print("@");
				else if(map[i][j].getHighway())
				{
					if(map[i][j].status() == 0)
						System.out.print("A");
					else
						System.out.print("B");
				}
				else
				{
					if(map[i][j].status() == 1)
						System.out.print(":");
					else if(map[i][j].status() == 2)
						System.out.print(".");
					else
						System.out.print("_");
				}
			}
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	public void hardenAt(int x, int y)
	{
		int minX, maxX;
		int minY, maxY;
		int coin;
		
		minX = x;
		minY = y;
		
		maxX = x;
		maxY = y;
		
		if((minX - 15) < 0)
			minX = 0;
		else
			minX = minX - 15;
		if((maxX + 15) >= columns)
			maxX = columns;
		else
			maxX = maxX + 15;
		if((minY - 15) < 0)
			minY = 0;
		else
			minY = minY - 15;
		if((maxY + 15) >= rows)
			maxY = rows;
		else
			maxY = maxY + 15;
		
		for(int o = minX; o < maxX; o++)
		{
			for(int p = minY; p < maxY; p++)
			{
				coin = rand.nextInt(2);
				if(coin == 0)
				{
					map[o][p].setStatus(1);
				}
			}
		}
	}
	
	public GridCell[] harden(int numPoints)
	{
		if(numPoints > totalCells)
		{
			System.out.println("Bruh");
			return new GridCell[0];
		}
		
		GridCell[] hardenPoints = new GridCell[numPoints];
		int coord;
		int tempX;
		int tempY;
		
		for(int i = 0; i < numPoints; i++)
		{
			coord = rand.nextInt(totalCells);
			tempX = (coord%columns);
			tempY = (coord/columns);
			
			hardenPoints[i] = new GridCell(tempX, tempY, 1);
			hardenAt(tempX, tempY);
		}
		
		return hardenPoints;
	}
	
	public void highways(int numPoints)
	{
		GridCell[] startPoints = new GridCell[numPoints];
		int startPointsSize = 0;
		
		Highway plotter;
		
		GridCell temp;
		int tempX = -1;
		int tempY = -1;
		int side; //0 = left, 1 = top, 2 = right, 3 = bottom
		boolean isDuplicate = false;
		
		for(int i = 0; i < numPoints; i++)
		{
			side = rand.nextInt(4);
			
			if(side == 0)
			{
				tempX = 0;
				tempY = rand.nextInt(rows);
			}
			else if(side == 1)
			{
				tempX = rand.nextInt(columns);
				tempY = 0;
			}
			else if(side == 2)
			{
				tempX = columns-1;
				tempY = rand.nextInt(rows);
			}
			else if(side == 3)
			{
				tempX = rand.nextInt(columns);
				tempY = rows-1;
			}
			else
				System.out.println("Error: Invalid side chosen in the highways function.");
			
			temp = new GridCell(tempX, tempY);
			for(int j = 0; j < startPointsSize; j++)
			{
				if(temp.toString().equals(startPoints[j].toString()))
				{
					isDuplicate = true;
					break;
				}
			}
			
			if(isDuplicate)
			{
				i--;
				continue;
			}
			
			plotter = new Highway(temp, side, map, rand);
			plotter.plotHighway(side);
			if(plotter.invalidStart())
			{
				i--;
				continue;
			}
			
			startPoints[i] = temp;
		}
	}
	
	public int block(int percent)
	{
		int chance;
		int count = 0;
		
		for(int i = 0; i < columns; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				if(!map[i][j].getHighway())
				{
					chance = rand.nextInt(100);
					if(chance < percent)
					{
						map[i][j].setStatus(2);
						count++;
					}
				}
			}
		}
		return count;
	}
	
	public void placeGoals()
	{
		int tempX = -1;
		int tempY = -1;
		int side;
		boolean isValid;
		
		side = rand.nextInt(4);
		
		if(side == 0) //left
		{
			tempX = rand.nextInt(20);
			tempY = rand.nextInt(rows);
		}
		else if(side == 1) //top
		{
			tempX = rand.nextInt(columns);
			tempY = rand.nextInt(20);
		}
		else if(side == 2) //right
		{
			tempX = rand.nextInt(20) + (columns-20);
			tempY = rand.nextInt(rows);
		}
		else if(side == 3) //bottom
		{
			tempX = rand.nextInt(columns);
			tempY = rand.nextInt(20) + (rows-20);
		}
		else
		{
			System.out.println("Error: Invalid side chosen in the placeGoals function.");
			return;
		}
		
		start = map[tempX][tempY];
		start.setStart();
		map[start.x()][start.y()].setStart();
		System.out.println("Start: " + start.x() + " " + start.y());
		
		isValid = false;
		
		while(!isValid)
		{
			side = rand.nextInt(4);
			
			if(side == 0) //left
			{
				tempX = rand.nextInt(20);
				tempY = rand.nextInt(rows);
			}
			else if(side == 1) //top
			{
				tempX = rand.nextInt(columns);
				tempY = rand.nextInt(20);
			}
			else if(side == 2) //right
			{
				tempX = rand.nextInt(20) + (columns-20);
				tempY = rand.nextInt(rows);
			}
			else if(side == 3) //bottom
			{
				tempX = rand.nextInt(columns);
				tempY = rand.nextInt(20) + (rows-20);
			}
			else
			{
				System.out.println("Error");
				return;
			}
			
			end = map[tempX][tempY];
			
			if((Math.abs(start.x()-end.x()) + Math.abs(start.y()-end.y()) >= 100))
			{
				isValid = true;
				end.setEnd();
				map[end.x()][end.y()].setEnd();
				System.out.println("End: " + end.x() + " " + end.y());
			}
		}
	}
	
	public ArrayList<GridCell> getNeighbors(GridCell center)
	{
		ArrayList<GridCell> list = new ArrayList<GridCell>();
		
		for(int i = -1; i <= 1; i++)
		{
			for(int j = -1; j <= 1; j++)
			{
				if(i == 0 && j == 0)
					continue;
				else
				{
					try 
					{
						if(map[center.x()+i][center.y()+j].getStatus() != 2)
							list.add(map[center.x()+i][center.y()+j]);
					} 
					catch(Exception ArrayIndexOutOfBoundsException) 
					{
						continue;
					}
				}
					
			}
		}
		return list;
	}
	
	public Agent buildPath(Agent ag)
	{
		ArrayList<GridCell> out = ag.search(this);
		if(out != null)
		{
			for(GridCell currCell : out)
			{
				map[currCell.x()][currCell.y()].setPath(true);
			}
		}
		else
			System.out.println("No path found.");
		
		return ag;
	}
	
	@Override
	public String toString()
	{
		String out = "";
		for(int j = 0; j < rows; j++)
		{
			for(int i = 0; i < columns; i++)
			{
				if(map[i][j].getHighway())
				{
					if(map[i][j].status() == 0)
						out += ("A");
					else
						out += ("B");
				}
				else
				{
					if(map[i][j].status() == 1)
						out += ("2");
					else if(map[i][j].status() == 2)
						out += ("0");
					else
						out += ("1");
				}
			}
			out += "\n";
		}
		return out;
	}
	
	public boolean buildFromFile(String filePath)
	{
		try {
		File f = new File(filePath);
		Scanner in = new Scanner(f);
		
		String startString = in.nextLine();
		String endString = in.nextLine();
		String[] inHard = new String[8];
		for(int i = 0; i < inHard.length; i++)
			inHard[i] = in.nextLine();
		
		String currRow;
		char currChar;
		
		for(int j = 0; j < rows; j++)
		{
			currRow = in.nextLine();
			for(int i = 0; i < columns; i++)
			{
				currChar = currRow.charAt(i);
				
				if(currChar == '0')
					map[i][j].setStatus(2);
				else if(currChar == '1')
					map[i][j].setStatus(0);
				else if(currChar == '2')
					map[i][j].setStatus(1);
				else if(currChar == 'A')
				{
					map[i][j].setStatus(0);
					map[i][j].setHighway(true);
				}
				else if(currChar == 'B')
				{
					map[i][j].setStatus(1);
					map[i][j].setHighway(true);					
				}
			}
		}
		
		int tempX;
		int tempY;
		
		tempX = Integer.parseInt(startString.substring(1, startString.indexOf(',')));
		tempY = Integer.parseInt(startString.substring(startString.indexOf(',')+1, startString.indexOf(')')));
		
		map[tempX][tempY].setStart();
		start = map[tempX][tempY];
		
		tempX = Integer.parseInt(endString.substring(1, endString.indexOf(',')));
		tempY = Integer.parseInt(endString.substring(endString.indexOf(',')+1, endString.indexOf(')')));
		
		map[tempX][tempY].setEnd();
		end = map[tempX][tempY];
		
		hardPoints = new GridCell[8];
		
		for(int i = 0; i < inHard.length; i++)
		{	
			tempX = Integer.parseInt(inHard[i].substring(1, inHard[i].indexOf(',')));
			tempY = Integer.parseInt(inHard[i].substring(inHard[i].indexOf(',')+1, inHard[i].indexOf(')')));
			
			hardPoints[i] = new GridCell(tempX, tempY, 1);
		}
		
		in.close();
		return true;
		} catch (FileNotFoundException e) {
			System.out.println("Error: No file found.");
			return false;
		}
	}
	
	public void output()
	{
		try {
			FileWriter out = new FileWriter("C:/Users/Seawald/eclipse-workspace/AI Project 1/output.txt");
			
			out.write(start.toString() + "\n");
			out.write(end.toString() + "\n");
			for(int i = 0; i < hardPoints.length; i++)
				out.write(hardPoints[i].toString() + "\n");
			out.write(toString());
			out.close();
			
		} catch (IOException e) {
			System.out.println("An error has occured creating the output file.");
			e.printStackTrace();
		}
	}
	
	public GridCell[][] getPoints()
	{
		GridCell[][] points = new GridCell[10][2];
		
		points[0][0] = start;
		points[0][1] = end;
		
		int tempX = -1;
		int tempY = -1;
		int side;
		boolean isValid;
		GridCell tempStart;
		GridCell tempEnd;
		
		for(int i = 1; i < 10; i++)
		{	
			side = rand.nextInt(4);
			
			if(side == 0) //left
			{
				tempX = rand.nextInt(20);
				tempY = rand.nextInt(rows);
			}
			else if(side == 1) //top
			{
				tempX = rand.nextInt(columns);
				tempY = rand.nextInt(20);
			}
			else if(side == 2) //right
			{
				tempX = rand.nextInt(20) + (columns-20);
				tempY = rand.nextInt(rows);
			}
			else //bottom
			{
				tempX = rand.nextInt(columns);
				tempY = rand.nextInt(20) + (rows-20);
			}
			
			tempStart = map[tempX][tempY];
			tempStart.setStart();
			System.out.println("Start " + i + " : " + tempStart.x() + " " + tempStart.y());
			
			isValid = false;
			
			while(!isValid)
			{
				side = rand.nextInt(4);
				
				if(side == 0) //left
				{
					tempX = rand.nextInt(20);
					tempY = rand.nextInt(rows);
				}
				else if(side == 1) //top
				{
					tempX = rand.nextInt(columns);
					tempY = rand.nextInt(20);
				}
				else if(side == 2) //right
				{
					tempX = rand.nextInt(20) + (columns-20);
					tempY = rand.nextInt(rows);
				}
				else //bottom
				{
					tempX = rand.nextInt(columns);
					tempY = rand.nextInt(20) + (rows-20);
				}
				
				tempEnd = map[tempX][tempY];
				
				if((Math.abs(tempStart.x()-tempEnd.x()) + Math.abs(tempStart.y()-tempEnd.y()) >= 100))
				{
					isValid = true;
					map[tempX][tempY].setEnd();
					System.out.println("End " + i + " : " + tempEnd.x() + " " + tempEnd.y());
				}
			}
		}
		
		return points;
	}
	
	public static void main(String[] args)
	{
		Grid g = new Grid(160, 120, 1602915799655l);
		System.out.println("Seed: " + g.seed);
		g.hardPoints = g.harden(8);
		g.highways(4);
		g.block(20);
		g.placeGoals();
		Agent friend = g.buildPath(new Sequential(g.start, g.end, 1.625, 1.2));
		System.out.println(friend.getStats());
		g.output();
		g.display();
		g.getPoints();
	}
}

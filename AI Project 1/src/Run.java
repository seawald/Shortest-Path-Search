import java.util.Scanner;

public class Run 
{
    public static void main(String[] args)
    {
    	Scanner in = new Scanner(System.in);
    	String input;
    	int command = 0;
    	
    	Handler h = new Handler();
    	
    	boolean active = true;
    	boolean benchmarkFlag =  false;
    	
    	System.out.println("Welcome to the pathfinding program!");
    	
    	while(active)
    	{
    		System.out.println("If you would like to generate a new random map in the program enter: 0");
	    	System.out.println("If you would like to generate a new seeded map in the program enter: 1");
	    	System.out.println("If you would like to generate a map based on a single input file enter: 2");
	    	System.out.println("If you would like to run a benchmark on a search by testing 50 maps enter: 3");
	    	
	    	input = in.nextLine();
    	
	    	if(input.length() != 1)
	    	{
	    		System.out.println("Command not recognized, please try again.");
	    		continue;
	    	}
	    	
	    	try {
	    		command = Integer.parseInt(input);
	    	} catch (Exception e){
	    		System.out.println("Command not recognized, please try again.");
	    		continue;
	    	}
	    	
	    	switch(command)
	    	{
	    		case 0:
	    			
	    			h.makeNewMap();
	    			active = false;
	    			break;
	    			
	    		case 1:
	    			
	    			System.out.println("Please enter the desired seed using only numbers:");
	    			try {
	    				h = new Handler(Long.parseLong(in.nextLine()));
	    				h.makeNewMap();
	    				active = false;
	    				break;
	    			} catch (Exception e) {
	    				System.out.println("Invalid seed, please try again.");
			    		break;
	    			}
	    			
	    		case 2:
	    			
	    			System.out.println("Please enter the file path:");
	    			if(h.buildNewMap(in.nextLine()))
	    			{
	    				active = false;
	    				break;
	    			}
	    			else
	    				break;
	    			
	    		case 3:
	    			
	    			benchmarkFlag = true;
	    			active = false;
	    			break;
	    			
	    		default:
	    			
	    			System.out.println("Command not recognized, please try again.");
		    		break;
	    	}
    	}
    	
    	active = true;
    	
    	while(active)
    	{
    		System.out.println("Enter the type of search you would like to use: A* =  0, Uniform Cost = 1, Weighted A* = 2, Sequential = 3");
    		input = in.nextLine();
    		
    		try {
	    		command = Integer.parseInt(input);
	    	} catch (Exception e){
	    		System.out.println("Command not recognized, please try again.");
	    		continue;
	    	}
    		
    		Double w1;
			Double w2;
    		
    		switch(command)
    		{
	    		case 0:
	    			
	    			active = false;
	    			break;
	    			
	    		case 1:
	    			
	    			active = false;
	    			break;
	    			
	    		case 2:
	    			
	    			System.out.println("Please enter the desired weight as a double:");
	    			
	    			try {
	    				w1 = Double.parseDouble(in.nextLine());
	    				active = false;
	    				break;
	    			} catch (Exception e) {
	    				System.out.println("Invalid weight, please try again.");
			    		break;
	    			}
	    			
	    		case 3:
	    			
	    			System.out.println("Please enter the desired first weight as a double:");
	    			
	    			try {
	    				w1 = Double.parseDouble(in.nextLine());
	    			} catch (Exception e) {
	    				System.out.println("Invalid weight, please try again.");
			    		break;
	    			}
	    			
	    			System.out.println("Please enter the desired second weight as a double:");
	    			
	    			try {
	    				w2 = Double.parseDouble(in.nextLine());
	    			} catch (Exception e) {
	    				System.out.println("Invalid weight, please try again.");
			    		break;
	    			}
	    			
	    			h.setWeight(w1, w2);
	    			active = false;
    				break;
	    			
	    		default:
	    			System.out.println("Command not recognized, please try again.");
		    		break;
    		}
    		
    		if(active == false)
    		{
	    		if(benchmarkFlag)
	    		{
	    			h.benchmark(command);
	    		}
	    		else
	    		{
	    			h.runSearch(command);
	    			h.printGrid();
	    		}
    		}
    	}
    	
    	if(command != 3 && !benchmarkFlag)
    	{
    		active = true;
    		int tempX;
			int tempY;
			
    		while(active)
    		{
    			System.out.println("If you would like to inspect an individual cell, enter its X coordinate (0 - 159), a comma \',\', and then its Y coordinate (0-119) with no spaces.");
    			System.out.println("To output your map as a readable text file, enter O.");
    			System.out.println(" To quit, enter Q.");
    			input = in.nextLine();
    			
    			if(input.length() == 1)
    			{
    				if(input.toLowerCase().equals("q"))
    				{
    					active = false;
    					break;
    				}
    				else if(input.toLowerCase().equals("o"))
    				{
    					h.outputMap();
    					continue;
    				}
    			}
    				
    			try {
    				tempX = Integer.parseInt(input.substring(0, input.indexOf(',')));
    				tempY = Integer.parseInt(input.substring(input.indexOf(',')+1, input.length()));
    				h.printCellInfo(tempX, tempY);
    			} catch (Exception e) {
    				System.out.println("Input not recognized, please try again.");
    			}
    		}
    	}
    	
    	in.close();
    	System.gc();
    	System.out.println("Program qutting. Thank you!");
    }
}

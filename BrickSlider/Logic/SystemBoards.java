/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Logic;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Presentation.WindowMethods;

/*
 * Defines a structure that holds and loads pre-defined game boards loaded from boards.csv file.
 */
public class SystemBoards {

	//Fields
	private static HashMap<Integer, ArrayList<GameBoard>> boards; //Available boards map defines be board sizes.
	private final static String CSV_FILE_NAME = "boards.csv";
	private static boolean loadedBoards; //Determines whether boards loaded successfully or not.
	
	/*
	 * Loads boards from boards.csv file to boards HashMap.
	 * @param window - A frame to show notification relative to if loading boards failed
	 */
	public static void loadBoards(JFrame window)
	{
		int currentBoardDimension=0;
		boards = new HashMap<Integer, ArrayList<GameBoard>>();
		ArrayList<GameBoard> sameDimensionBoards = new ArrayList<GameBoard>();
		try {
				Scanner input = null;
				try
				{
					input = new Scanner(new FileReader("./"+CSV_FILE_NAME)); //First try and load boards.csv from outside program jar file, if could not locate or load file catch exception.
				}
				catch (Exception e)
				{
					input = new Scanner(new FileReader("/boards/"+CSV_FILE_NAME)); //If could not locate a boards.csv file outside jar - load boards.csv from resource folder if exists.
				}
				while(input.hasNext()) 
				{
					Integer boardDimension = Integer.parseInt(input.next());
					if (boardDimension != currentBoardDimension && !boards.containsKey(boardDimension)) { //If no board exists in the current dimension 
						sameDimensionBoards = new ArrayList<GameBoard>();}
					else {
						sameDimensionBoards = boards.get(boardDimension);}
					currentBoardDimension=boardDimension;
					GameBoard currentBoard = new GameBoard(boardDimension); 
					for (int i=0; i<currentBoard.getBoardSize() ; i++) //Parse each row (number of row as board dimension parsed).
					{
						String row = input.next();
						String[] rowCells = row.split(",");
						for(String cell : rowCells)
						{
							int cellIndex=Integer.parseInt(cell);
							if(cellIndex == 0)
								cellIndex = (int) Math.pow(currentBoard.getBoardSize(), 2);
							currentBoard.addBrickLocation(cellIndex-1);
						}
					}
					sameDimensionBoards.add(currentBoard);
					boards.put(currentBoardDimension, sameDimensionBoards);
				}
				loadedBoards=true;
				input.close();
			}
		catch (Exception e)
		{
			WindowMethods.showMessage(window, "<html>Program encountered problem while trying to read system boards csv file.<br/>Please make sure the csv boards file exists and contains a legal boards format.<br/>You can still use Auto Shuffle mode.</html>", "Error loading boards from CSV", JOptionPane.ERROR_MESSAGE);
			loadedBoards=false;
		}
	}
	/*
	 * Gets a wanted board size an return a random board from the collection of boards available from that size.
	 * @param boardSize - wanted board size.
	 * @return GameBoard from current size, if down not exist returns null.
	 */
	public static GameBoard getRandomBoard(int boardSize)
	{
		if (boards.containsKey(boardSize))
			return boards.get(boardSize).get(new Random().nextInt(boards.get(boardSize).size()));
		else return null;
	}
	
	//Getters and Setters
	public static boolean isLoadedBoards() {
		return loadedBoards;
	}

	public static void setLoadedBoards(boolean loadedBoards) {
		SystemBoards.loadedBoards = loadedBoards;
	}
	
	public static HashMap<Integer, ArrayList<GameBoard>> getBoards() {
		return boards;
	}
}

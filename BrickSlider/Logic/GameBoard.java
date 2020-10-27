/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Logic;

import java.util.ArrayList;

/*
 * Defines a game board by indexes positions. (Used for pre-shuffled board loaded from boards.csv)
 */
public class GameBoard {

	//Fields
	private ArrayList<Integer> shuffledBoard;
	private int boardSize;
	
	//Constructor
	public GameBoard(int boardSize)
	{
		this.shuffledBoard = new ArrayList<Integer>();
		this.boardSize=boardSize;
	}
	
	//Methods
	public void addBrickLocation(int i)
	{
		this.shuffledBoard.add(i);
	}
	
	//Getters and Setters
	public int getBoardSize() {
		return boardSize;
	}
	
	public ArrayList<Integer> getShuffledBoard() {
		return shuffledBoard;
	}
}

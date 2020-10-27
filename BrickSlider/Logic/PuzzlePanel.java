/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Logic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Logic.Brick;
import Presentation.PuzzleWindow;
import Presentation.WindowMethods;

/*
 * Contains all puzzle bricks and logic behind process of the game (pre and during).
 */
public class PuzzlePanel extends JPanel{
	
	//Fields
	private PuzzleWindow puzzleWindow; //Main window that the puzzle board panel is located in.
	private static ArrayList<Brick> bricks; // Current permutation of bricks over the board.
	private final ArrayList<Point> solution; //The solved form of the board.
	private Stack<ArrayList<Brick>> lastMoves; //A Stack containing all last board arrangements.
	private Image sourceImage; //The image the end-user is playing with.
	private int boardSize; // The board dimensions chosen by end-user (board will be boardSizeXboardSize)
	private int squareBoardSize; // A squared value of boardSize, used to make calculation on the amount of bricks.
	private Brick emptyBrick; //Determines a brick that represents an empty spot on the board.
	private static int emptyBrickIndex; //The index of the empty brick spot on board.
	public static int movesMade; // The amount of moves (Legal Brick clicks) made in the board.
	
	//Constructor
	public PuzzlePanel(PuzzleWindow puzzleWindow, BufferedImage sourceImage, int boardSize, String shuffleMethod)
	{
		super();
		this.puzzleWindow = puzzleWindow;
		this.setBackground(new Color(0, 0, 0));
		this.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		this.setBounds(10, 11, 850, 850);
        this.setLayout(new GridLayout(boardSize, boardSize, 0, 0));
        this.setFocusable(true);
        if(sourceImage.getWidth()!=850 && sourceImage.getHeight()!=850)  //Check for user image that is not in the correct dimensions and resize it (Slows Runtime).
        	this.sourceImage = sourceImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
        else this.sourceImage = sourceImage;
		this.boardSize=boardSize;
		this.squareBoardSize = (int) Math.pow(boardSize, 2);
		this.addKeyListener(new ClickAction(this, this.boardSize));
		this.solution = new ArrayList<Point>(); 
		this.lastMoves=new Stack<ArrayList<Brick>>();
		bricks = new ArrayList<Brick>();
		movesMade=0;
		emptyBrickIndex = squareBoardSize-1;
		initiateSolution();
		createBricks();
		bricks.add(emptyBrick);
		if(shuffleMethod =="Auto shuffle") //Dynamic shuffle
			shuffleBoard(10000*squareBoardSize);
		else systemShuffle(); //Shuffle from list of boards loaded from boards.csv
		addBricksToPanel();
	}
	
	/*
	 * Adds all the relevant bricks to the puzzle panel.
	 */
	private void addBricksToPanel()
	{
        for (Brick brick : bricks) {
            this.add(brick);
            brick.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            brick.addActionListener(new ClickAction(this, this.boardSize));
        }
	}
	
	/*
	 * Determines the correct solution for the generated board.
	 */
	private void initiateSolution()
	{
		for ( int i=0; i<this.boardSize; i++)
			for ( int j=0; j<this.boardSize; j++)
			{
				solution.add(new Point (i,j));
			}
	}
	
	/*
	 * Creates new bricks that are relevant for the current board and adds them to the bricks list.
	 */
	private void createBricks()
	{
       for (int i = 0; i < this.boardSize; i++) {

            for (int j = 0; j < this.boardSize; j++) {

                Brick brick = new Brick(createBrickImageCut(j,i)); //Creates the brick with the correct image cut by the row and column coordinates.
                brick.setPosition(new Point(i, j));

                if (i == this.boardSize-1 && j == this.boardSize-1) { // Located empty brick and creates it.
                	emptyBrick = new Brick();
                	emptyBrick.setPosition(new Point(i, j));
                } else {
                    bricks.add(brick);
                }
            }
        }
	}
	
	/*
	 * Creates an image cut from the full game source image to fill the brick determined by the bricks row an column on board.
	 * @param row - The bricks row coordinates.
	 * @param column - The bricks column coordinates.
	 * @return An Image that is the correct image portion to fill the brick in location (row, column).
	 */
	private Image createBrickImageCut (int row, int column)
	{
		return createImage(new FilteredImageSource(sourceImage.getSource(), //Returns an image cut with the correct proportions and location.
        	   new CropImageFilter(row * this.getWidth() / this.boardSize, column * this.getHeight() / boardSize, (this.getWidth() / this.boardSize), (this.getHeight() / this.boardSize))));
	}
	
	/*
	 * Shuffles board according to given randomized game board positions loaded from boards.csv at start-up. 
	 */
	private void systemShuffle() {
		GameBoard randomBoard = SystemBoards.getRandomBoard(boardSize);
		if(randomBoard != null)
		{
			ArrayList<Brick> shuffledBricks = new ArrayList<Brick>();
			for (int i =0; i<squareBoardSize; i++)
			{
				int shuffledBoardIndex = randomBoard.getShuffledBoard().get(i);
				if(shuffledBoardIndex==squareBoardSize-1)
					emptyBrickIndex = i;
				shuffledBricks.add(i, bricks.get(shuffledBoardIndex));
			}
			bricks = shuffledBricks;
		}
		else throw new NullPointerException();
	}

	/*
	 * A dynamic shuffle method, locates a neighboring brick to the empty index and randomizes a move over the board by iteration.
	 * @param shuffleMoves - the number of moves over the board that should be made.
	 */
	private void shuffleBoard(int shuffleMoves)
	{
        Random rand = new Random();
        int[] neighborOffsets = { -boardSize, +boardSize, -1, +1 }; //Possible moves set - up down left right
        while (shuffleMoves > 0) {
            int neighborBrick;
            do 
            {
            	neighborBrick = emptyBrickIndex + neighborOffsets[rand.nextInt(4)];
            }
            while (!isLegalBoardPlace(neighborBrick) || !canMove(neighborBrick, emptyBrickIndex));
            Collections.swap(PuzzlePanel.bricks, neighborBrick, emptyBrickIndex);
            emptyBrickIndex= neighborBrick;
            shuffleMoves--;
        }
	}
	
	/*
	 * Checks that a place is within the limits of the board.
	 *@param place - that place to check if is part of the board.
	 *@return True if the place is a legal place in the board False otherwise.
	 */
	private boolean isLegalBoardPlace(int place)
	{
		return ((place>=0) && (place<squareBoardSize));
	}
	
	/*
	 * Check if a brick can be moves determined by the brick clicked and the empty brick locations.
	 * @param - clickedIndex - The brick wished to be moved, the brick that was allegedly clicked.
	 * @param emptyIndex - The location of the empty brick, the place to check if the clicked brick can be moved to.
	 * @return True if brick can be moves false otherwise.
	 */
	private boolean canMove(int clickedIndex, int emptyIndex)
	{
		return ((clickedIndex - 1 == emptyIndex && clickedIndex%boardSize != 0) || (clickedIndex + 1 == emptyIndex && (clickedIndex+1)%boardSize != 0)
                || (clickedIndex - (boardSize) == emptyIndex) || (clickedIndex + (boardSize)  == emptyIndex));
	}
	
	/*
	 * Cancels last move made over board by loading the last positioning of the board if moves has been made.
	 */
	public void undoMove()
	{
		if(lastMoves.isEmpty()) { //No last moves
			WindowMethods.playSound("error.wav");
			PuzzleWindow.undoError.setVisible(true);	
			this.requestFocusInWindow();
		}
		else { 
			WindowMethods.playSound("undo_move.wav");
			PuzzleWindow.undoError.setVisible(false);
			bricks = lastMoves.pop(); //Changes current board state to last state recorded.
			int movesHistory = lastMoves.size();
			if(movesHistory==0) {
			PuzzleWindow.undoMovesLeft.setVisible(false);
			}
			else PuzzleWindow.undoMovesLeft.setText("Moves to undo: "+movesHistory);
			this.removeAll();
		
		      for (Brick brick : bricks) {
			        this.add(brick);
			        if(((Brick) brick).isLastBrick())
			        	emptyBrickIndex = bricks.indexOf(brick);
		      }
		      this.requestFocusInWindow();
		       this.validate();
		       this.repaint();
		}
	}
	
	/*
	 * Pauses puzzle panel by changing its content to a different image 
	 * Or n-pauses it and loads last the last brick positioning on board.
	 * @param pause - Determines whether the puzzle panel should be paused or un-paused.
	 */
	public void pausePuzzle(boolean pause) throws IOException
	{
		if(pause)
		{
			this.removeAll();
        	this.setLayout(new BorderLayout());
        	JLabel pauseImage = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/images/pause_image.jpg"))));
        	this.add(pauseImage);
        	this.repaint();
        	this.revalidate();
		}
		else
		{
			this.setLayout(new GridLayout(boardSize, boardSize, 0, 0));
			updateBricks();
		}
	}
	
	/*
	 * Updates brick view over panel by deleting panel contents and adding the updated bricks by location.
	 */
    private void updateBricks() {
    	this.removeAll();

       for (Brick brick : PuzzlePanel.bricks)
        	this.add(brick);
        
        this.validate();
        this.repaint();
    }
    
    /*
     * Shows the full image played with (Occurs if puzzle is solved).
     */
    public void showSolvedFullImage()
    {
    	this.removeAll();
    	this.setLayout(new BorderLayout());
    	JLabel completeImage = new JLabel(new ImageIcon(this.sourceImage));
    	this.add(completeImage);
    	this.repaint();
    }
    
    /*
     * Performs a move on the board if clicked index brick can be moves
     * @param clickedIndex - the index of the brick to try and perform a move with.
     */
    private void performMove(int clickedIndex)
    { 
        if (isLegalBoardPlace(clickedIndex) && canMove(clickedIndex, emptyBrickIndex))
        {
        	movesMade++;
        	PuzzleWindow.undoError.setVisible(false);
        	lastMoves.push(new ArrayList<Brick>(PuzzlePanel.bricks));
        	int movesHistory = lastMoves.size();
        	if (movesHistory>0) {
        		PuzzleWindow.undoMovesLeft.setVisible(true);
            	PuzzleWindow.undoMovesLeft.setText("Moves to undo: "+movesHistory);
        	}
            Collections.swap(PuzzlePanel.bricks, clickedIndex, emptyBrickIndex);
            updateBricks();
            WindowMethods.playSound("brick_slide_sound.wav");
            PuzzleWindow.movesCounter.setText(movesMade+"");
            emptyBrickIndex=clickedIndex;
        }
        else WindowMethods.playSound("brick_cant_move.wav");
        this.requestFocusInWindow();
    }
    
    /*
     * Checks whether the current board locations solves the board.
     */
    private void checkSolution() {

        ArrayList<Point> current = new ArrayList<Point>(); 

        for (Brick brick : PuzzlePanel.bricks) { //Convert the list of bricks to their location Point values.
            current.add(brick.getPosition());
        }

        if (compareList(solution, current)) { //Compares to board solution.
        	puzzleWindow.Solved(this, puzzleWindow);
        }
    }
    
    /*
     * Helper method to compare the list of current positions to the list of solution positions.
     * @param ls1 - first list to compare.
     * @param ls2 - second list to compare.
     * @return True if lists are equal, False otherwise.
     */
    public boolean compareList(List<Point> ls1, List<Point> ls2) {
        return ls1.toString().contentEquals(ls2.toString());
    }
    
    /*
     * A Private inner class to handle Key presses and Clicks.
     */
	private class ClickAction extends AbstractAction implements KeyListener, ActionListener {
		
		//Fields
		private PuzzlePanel puzzlePanel;
		private int boardSize;
		
		//Constructor
		public ClickAction(PuzzlePanel puzzlePanel, int boardSize)
		{
			this.puzzlePanel = puzzlePanel;
			this.boardSize=boardSize;
		}
		
		@Override
        public void keyPressed(KeyEvent k) { //Checks for keyboard key presses
        	checkKey(k);
            checkSolution();
        }
		
        @Override
        public void actionPerformed(ActionEvent e) { //Checks for actions made such as mouse clicks
            checkButton(e);
            checkSolution();
        }
        
        private void checkButton(ActionEvent e) { //Check what certain brick was pressed.

            Brick clickedBrick = (Brick) e.getSource();
            int clickedIndex = PuzzlePanel.bricks.indexOf(clickedBrick);
            performMove(clickedIndex);
	}
        
        private void checkKey(KeyEvent k) { //Checks what certain keyboard key (Or key combination) was pressed.
        	int key = k.getKeyCode();
            if(k.isControlDown() &&key== KeyEvent.VK_Z) //Ctrl+Z Cancels last move on board.
            	puzzleWindow.undoMoveButton.doClick();
            else{
            	try { //UP, DOWN, LEFT, RIGHT Keyboard keys operation. moves the brick that can be moved to the location wanted if exists.
            	Integer clickedIndex = (key == KeyEvent.VK_DOWN)? emptyBrickIndex-boardSize : (key == KeyEvent.VK_UP)? emptyBrickIndex+boardSize : (key == KeyEvent.VK_RIGHT)? emptyBrickIndex-1 : (key == KeyEvent.VK_LEFT)? emptyBrickIndex+1 : null;
                if(clickedIndex != null)
                	performMove(clickedIndex);
            	}
            	catch (NullPointerException e) {}
            }
	}
		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent e) {}
	}
}

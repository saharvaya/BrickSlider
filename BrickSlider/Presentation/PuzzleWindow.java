/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Presentation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import Logic.PuzzlePanel;
import Logic.SystemBoards;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;

/*
 * A window that encapsulates all puzzle game real-time actions.
 * In this window the game is played and controlled by user.
 */
public class PuzzleWindow extends JFrame {

	//Components
	//Containers
	private Container windowContainer;
	private PuzzlePanel puzzleBoardPanel;
	private JPanel gameDetailsPanel;
	//Labels
	private JLabel secondLabel;
	private JLabel minuteLabel;
	private JLabel hourLabel;
	private JLabel shuffleModeLabel;
	private JLabel gameModeLabel;
	private JLabel levelLabel;
	private JLabel movesLabel;
	private JLabel authorsLabel;
	private JLabel previewLabel;
	private JLabel timerLabel;
	public static JLabel levelCounter;
	public static JLabel undoError;
	public static JLabel undoMovesLeft;
    public static JLabel movesCounter;
	//Buttons
	public JButton undoMoveButton;
	private JButton restartButton;
	private JButton exitButton;
	private JButton pauseButton;
	private JButton soundOnOffButton;
	//Images and icons
	public JLabel pauseImage;
	private JLabel previewImage;
	private JLabel logoImage;
	//Fields
	private static int seconds;
	private static int minutes;
	private static int hours;
	private final int delay = 1000;
	private static Timer timer;
	private static int totalMovesMade;
	private static int boardSize;
	private static int level;
	private static boolean userPaused;
	private static Image previewImageIcon;
	private static BufferedImage gameImage;
	private String gameMode;
	private String shuffleMethod;

	
	//Constructor
	public PuzzleWindow(BufferedImage image, String gameMode, int boardDimensions, String shuffleMethod) throws IOException {
		//=============================== Initialize window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1219, 911);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Brick Slider - "+gameMode);
		getContentPane().setLayout(null);
		WindowMethods.setIcon(this);
		WindowMethods.setLookFeel();
		if (gameMode =="Level Mode")
			level = 1;
		userPaused = false;
		totalMovesMade=0;
		boardSize=boardDimensions;
		this.gameMode=gameMode;
		this.shuffleMethod=shuffleMethod;
		PuzzleWindow.gameImage=image;

		initializeWindowPanels();
		initializeGameDetailsPanelComponents();
		initiateTimerTime();

		addComponentsToPanels();
		timer = new Timer(delay, new TimerListener());;
		timer.start();
		this.setContentPane(windowContainer);
	}
	
	/*
	 * Initializes all components related to the Game Details Panel.
	 * Calls helper methods to initialize game timer, moves counter and image preview related components.
	 */
	private void initializeGameDetailsPanelComponents() throws IOException
	{
		if (gameMode == "Level Mode") {
			levelLabel = new JLabel("Level:");
			levelLabel.setForeground(Color.WHITE);
			levelLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
			levelLabel.setBounds(29, 390, 125, 25);
			levelCounter = new JLabel("<html>"+level+" <font color=\"white\" size=\"2\">("+boardSize+"x"+boardSize+" board)</font></html>");
			levelCounter.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 18));
			levelCounter.setBounds(90, 390, 120, 25);
			levelCounter.setForeground(Color.YELLOW);
		}
		logoImage = new JLabel();
		logoImage.setBounds(163, 11, 150, 140);
		logoImage.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/program_logo.png")).getScaledInstance(logoImage.getWidth(), logoImage.getHeight(), Image.SCALE_SMOOTH)));
		authorsLabel = new JLabel("<HTML><b>Created By:</b><br/>Itay Bouganim<br/>Sahar Vaya</HTML>");
		authorsLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		authorsLabel.setBounds(29, 41, 96, 72);
		exitButton = new JButton("Exit game");
		exitButton.setBackground(UIManager.getColor("Button.highlight"));
		exitButton.setBorder(new BevelBorder(BevelBorder.RAISED, UIManager.getColor("Button.focus"), null, null, null));
		exitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		exitButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		exitButton.setBounds(198, 767, 115, 72);
		exitButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/exit_icon.png"))));
		exitButton.setContentAreaFilled(false);
		exitButton.setIconTextGap(-50);
		exitButton.setVerticalTextPosition(SwingConstants.TOP);
		exitButton.addActionListener(new ButtonListener(this));
		restartButton = new JButton("Restart");
		restartButton.setBackground(UIManager.getColor("Button.highlight"));
		restartButton.setBorder(new BevelBorder(BevelBorder.RAISED, UIManager.getColor("Button.focus"), null, null, null));
		restartButton.setHorizontalTextPosition(SwingConstants.CENTER);
		restartButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		restartButton.setBounds(24, 767, 115, 72);
		restartButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/restart_icon.png"))));
		restartButton.setContentAreaFilled(false);
		restartButton.setIconTextGap(-50);
		restartButton.setVerticalTextPosition(SwingConstants.TOP);
		restartButton.addActionListener(new ButtonListener(this));
		shuffleModeLabel = new JLabel("<html>Shuffle Mode:&emsp;<font color=\"BLACK\">"+shuffleMethod+"</font></html>");
		shuffleModeLabel.setForeground(Color.WHITE);
		shuffleModeLabel.setFont(new Font("Tahoma", Font.BOLD, 10));
		shuffleModeLabel.setBounds(25, 124, 157, 25);
		gameModeLabel = new JLabel("Game Mode:  "+gameMode);
		gameModeLabel.setForeground(Color.WHITE);
		gameModeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		gameModeLabel.setBounds(29, 370, 300, 25);
		soundOnOffButton = new JButton("Sound On");
		soundOnOffButton.setBorder(new BevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.LIGHT_GRAY, null, null));
		soundOnOffButton.setBackground(Color.DARK_GRAY);
		soundOnOffButton.setContentAreaFilled(false);
		soundOnOffButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		soundOnOffButton.setBounds(15, 11, 96, 27);
		soundOnOffButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/sound_on.png"))));
		soundOnOffButton.addActionListener(new ButtonListener(this));
		initiateTimerComponents();
		initiateMovesComponents();
		initiateImagePreviewComponents();
	}
	
	/*
	 * Adds all Frame related components to the associated panels.
	 * Adds Game details and puzzle panels to the window container.
	 */
	private void addComponentsToPanels()
	{
		if(gameMode == "Level Mode")
		{
			gameDetailsPanel.add(levelLabel);
			gameDetailsPanel.add(levelCounter);
		}
		gameDetailsPanel.add(logoImage);
		gameDetailsPanel.add(exitButton);
		gameDetailsPanel.add(soundOnOffButton);
		gameDetailsPanel.add(secondLabel);
		gameDetailsPanel.add(minuteLabel);
		gameDetailsPanel.add(hourLabel);
		gameDetailsPanel.add(timerLabel);
		gameDetailsPanel.add(pauseButton);
		gameDetailsPanel.add(movesLabel);
		gameDetailsPanel.add(shuffleModeLabel);
		gameDetailsPanel.add(gameModeLabel);
		gameDetailsPanel.add(undoError);
		gameDetailsPanel.add(movesCounter);
		gameDetailsPanel.add(undoMovesLeft);
		gameDetailsPanel.add(undoMoveButton);
		gameDetailsPanel.add(restartButton);
		gameDetailsPanel.add(previewLabel);
		gameDetailsPanel.add(previewImage);
		gameDetailsPanel.add(pauseImage);
		gameDetailsPanel.add(authorsLabel);
		
		windowContainer.add(gameDetailsPanel, BorderLayout.EAST);
		windowContainer.add(puzzleBoardPanel, BorderLayout.WEST);
	}
	
	/*
	 * Initializes window panels, game details panel and a puzzle panel.
	 */
	private void initializeWindowPanels()
	{
		windowContainer = new Container();
		try {
		puzzleBoardPanel = new PuzzlePanel(this, gameImage, boardSize, shuffleMethod);
		}
		catch(NullPointerException e) {
			noBoardFoundError(this);
		}
		gameDetailsPanel = new JPanel();
		gameDetailsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, null, null, null));
		gameDetailsPanel.setBackground(Color.GRAY);
		gameDetailsPanel.setBounds(870, 11, 323, 850);
		gameDetailsPanel.setLayout(null);
	}
	
	/*
	 * Initiates all components related to the preview image located in the game details panel.
	 */
	private void initiateImagePreviewComponents()
	{
		previewLabel = new JLabel("Image preview:");
		previewLabel.setForeground(new Color(128, 0, 0));
		previewLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		previewLabel.setBounds(15, 420, 115, 16);
		previewImage = new JLabel("");
		previewImage.setBorder(new LineBorder(null, 4));
		previewImage.setBounds(13, 440, 300, 300);
		PuzzleWindow.previewImageIcon = gameImage.getScaledInstance(previewImage.getWidth(), previewImage.getHeight(), Image.SCALE_SMOOTH);
		previewImage.setIcon(new ImageIcon(previewImageIcon));
	}
	
	/*
	 * Initiates all components related to the moves counter located in the game details panel.
	 */
	private void initiateMovesComponents() throws IOException
	{
		movesLabel = new JLabel("Moves made:");
		movesLabel.setForeground(Color.WHITE);
		movesLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		movesLabel.setBounds(29, 275, 125, 25);
		undoError = new JLabel("No moves to undo.");
		undoError.setForeground(Color.YELLOW);
		undoError.setFont(new Font("Tahoma", Font.BOLD, 12));
		undoError.setBounds(170, 333, 125, 25);
		undoError.setVisible(false);
		movesCounter = new JLabel("0");
		movesCounter.setHorizontalAlignment(SwingConstants.RIGHT);
		movesCounter.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 18));
		movesCounter.setBounds(35, 299, 95, 25);
		undoMovesLeft = new JLabel();
		undoMovesLeft.setForeground(Color.WHITE);
		undoMovesLeft.setFont(new Font("Tahoma", Font.BOLD, 9));
		undoMovesLeft.setBounds(181, 268, 150, 25);
		undoMovesLeft.setVisible(false);
		undoMoveButton = new JButton();
		undoMoveButton.setBackground(UIManager.getColor("Button.background"));
		undoMoveButton.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, null, null, null));
		undoMoveButton.setSelected(true);
		undoMoveButton.setContentAreaFilled(false);
		undoMoveButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		undoMoveButton.setIconTextGap(-51);
		undoMoveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		undoMoveButton.setText("Undo Move");
		undoMoveButton.setBounds(180, 288, 84, 50);
		undoMoveButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/undo_move.png"))));
		undoMoveButton.addActionListener(new ButtonListener(this));
	}
	
	/*
	 * Initiates all components related to the game timer located in the game details panel.
	 */
	private void initiateTimerComponents() throws IOException
	{
		if(this.gameMode=="Against the Clock")
			timerLabel = new JLabel("Remaining Time :");
		else timerLabel = new JLabel("Elapsed Time :");
		timerLabel.setForeground(new Color(255, 255, 255));
		timerLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		timerLabel.setBounds(29, 173, 155, 25);
		Font timerFont = new Font("Arial Rounded MT Bold", Font.BOLD, 18);
		secondLabel = new JLabel(": 00");
		minuteLabel = new JLabel(": 00");
		hourLabel = new JLabel(" 00");
		secondLabel.setFont(timerFont);
		minuteLabel.setFont(timerFont);
		hourLabel.setFont(timerFont);
		pauseImage = new JLabel();
		pauseImage.setText("Paused");
		pauseImage.setHorizontalAlignment(SwingConstants.LEFT);
		pauseImage.setBounds(150, 200, 77, 25);
		pauseImage.setFont(new Font("Tahoma", Font.BOLD, 11));
		pauseImage.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/pause_icon.png"))));
		pauseImage.setVisible(false);
		pauseButton = new JButton("Pause");
		pauseButton.setBackground(Color.BLACK);
		pauseButton.setHorizontalTextPosition(SwingConstants.CENTER);
		pauseButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		pauseButton.setBounds(30, 227, 80, 25);
		pauseButton.setVerticalTextPosition(SwingConstants.CENTER);
		pauseButton.addActionListener(new ButtonListener(this));
		
		secondLabel.setBounds(109, 200, 40, 25);
		minuteLabel.setBounds(64, 200, 40, 25);
		hourLabel.setBounds(24, 200, 40, 25);
	}
	
	/*
	 * Initializes timer starting values according to the selected game mode.
	 */
	private void initiateTimerTime() {
		if(gameMode =="Against the Clock")
		{
			int squareBoardSize =(int) (Math.pow(boardSize, 2));
			seconds =0;
			minutes = (boardSize<=3)? (squareBoardSize) : (squareBoardSize*2);
			if(minutes>=60) {
				hours=minutes/60;
				minutes = minutes%60;
			}
			else hours=0;
			setTimerText();
		}
		else 
		{
			hours = 0;
			minutes= 0;
			seconds =0;
		}
	}
	
	/*
	 * Changes timer label text according to the current time.
	 */
	private void setTimerText()
	{
		String secondsText = (seconds<10)? ": 0"+seconds : ": "+seconds;
		String minutesText = (minutes<10)? ": 0"+minutes : ": "+minutes;
		String hoursText = (hours<10)? " 0"+hours : " "+hours;
		secondLabel.setText(secondsText);
		minuteLabel.setText(minutesText);
		hourLabel.setText(hoursText);
	}
	
	/*
	 * Calls the frames puzzle panel and cancels the last move the end-user made.
	 */
	private void undoMove() {
		this.getPuzzleBoardPanel().undoMove();
	}
	
	/*
	 * A response method for a failed game (Can be called only during "Against the Clock" game type).
	 */
	public void Failed() {
		timer.stop();
		WindowMethods.playSound("time_out.wav");
		this.setEnabled(false);
		WindowMethods.showMessage(this, "You failed to beat the clock! try again.", "Failed brick board", JOptionPane.INFORMATION_MESSAGE);
		try {
			NewGameWindow newGame = new NewGameWindow();
			newGame.setVisible(true);
			this.dispose();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/*
	 * A response method for a user completing a game board.
	 * @param solvedPuzzle - A solved puzzle panel.
	 * @param activeWindow - An active puzzle game window contains the solved puzzle.
	 */
	public void Solved(PuzzlePanel solvedPuzzle, PuzzleWindow activeWindow) {
		WindowMethods.playSound("game_won.wav");
		pauseGame (this, true, false);
		solvedPuzzle.showSolvedFullImage();  //Displays the full game image after the board is solved.
		if(gameMode != "Level Mode") {
		SummaryWindow gameSummary;
		try {
			if(gameMode == "Against the Clock")
				gameSummary = new SummaryWindow(this, "you solved the board with the following statistics:", "Beat the clock", movesCounter.getText(), this.gameMode);
			else gameSummary = new SummaryWindow(this, "you solved the board with the following statistics:", hours+":"+minutes+":"+seconds, movesCounter.getText(), this.gameMode);
			gameSummary.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		else
		{
			if ((level+1)!=4) //If this is not the third level - the last one.
			{
			WindowMethods.showMessage(solvedPuzzle, "<html><b>Congratulations! you finished level "+level+".</b><br/>Press OK to continue to level number "+(level+1)+"</html>","Solved brick board - Level "+level, JOptionPane.INFORMATION_MESSAGE);
	        	JDialog loading = WindowMethods.showLoading();
	    		loading.setVisible(true);
	        	SwingWorker<Void, Void> process = new SwingWorker<Void, Void>(){  //Background process - Increases the board size and loads a new puzzle panel with the new dimensions.
	                @Override
	                protected Void doInBackground() throws Exception {
	                	boardSize++;
	                	totalMovesMade= totalMovesMade+PuzzlePanel.movesMade; //Sums up all the moves from the different levels.
	                	activeWindow.remove(puzzleBoardPanel);
	                	if(shuffleMethod == "CSV file shuffle" && !SystemBoards.getBoards().containsKey(boardSize))	
	                		shuffleMethod = "Auto shuffle";
	    	        	puzzleBoardPanel = new PuzzlePanel(activeWindow, gameImage, boardSize, shuffleMethod);
	    	        	activeWindow.getContentPane().add(puzzleBoardPanel);
	    	        	activeWindow.revalidate();
	    	        	puzzleBoardPanel.movesMade=0;
	    	        	activeWindow.movesCounter.setText(puzzleBoardPanel.movesMade+"");
	    	        	level++;
	    	        	levelCounter.setText(level+"");
	       				WindowMethods.playSound("timer_on.wav");
	       			return null;
	                }
	             };
	             process.addPropertyChangeListener(new PropertyChangeListener() {
	                @Override
	                public void propertyChange(PropertyChangeEvent evt) {  // After new puzzle panel is loaded and the new board is loaded to the current game window.
	                   if (evt.getPropertyName().equals("state")) {
	                      if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
	                   	   loading.dispose();
	                   	   pauseGame (activeWindow, false, false);
	                      }
	                   }
	                }
	             });
	             process.execute();  
			}
			else {
				totalMovesMade= totalMovesMade+PuzzlePanel.movesMade;
				SummaryWindow gameSummary;
				try {
					gameSummary = new SummaryWindow(this, "you solved all three levels with the following statistics:", hours+":"+minutes+":"+seconds, totalMovesMade+"", this.gameMode);
					gameSummary.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
	}
	
	/*
	 * Displays error message and return to main menu if system board does not contain a board of the requested dimensions.
	 * @param currentWindow - The current game window that failed to load due to the lack of resource.
	 */
	private void noBoardFoundError(PuzzleWindow currentWindow)
	{	
		currentWindow.dispose();
		WindowMethods.playSound("error.wav");
		WindowMethods.showMessage(null,"<html><b>No board currently exists in the dimensions chosen.</b><br/>Plesae try and select a different board size.</html>", "No board available", JOptionPane.ERROR_MESSAGE);
		try {
			NewGameWindow newGameWindow = new NewGameWindow();
			newGameWindow.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	 * Handles all game pause occurrences - user made and artificially made.
	 * @param activeWindow - the window to pause the game in.
	 * @param pause - determines whether to pause on un-pause game (pause - true, un-pause - false).
	 * @param userPause - determine whether the user has paused the game or it was initiated by the program.
	 */
	public void pauseGame (PuzzleWindow activeWindow, boolean pause, boolean userPause)
	{
		if(userPause)
		{
			WindowMethods.playSound("timer_on.wav");
			if(pause && !userPaused)
			{
				pauseButton.setText("Unpause");
				activeWindow.pauseImage.setVisible(true);
				activeWindow.puzzleBoardPanel.setEnabled(false);
				try {
					activeWindow.puzzleBoardPanel.pausePuzzle(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				activeWindow.timer.stop();
				userPaused=true;
			}
			else if (!pause && userPaused)
			{
				pauseButton.setText("Pause");
				activeWindow.pauseImage.setVisible(false);
				activeWindow.puzzleBoardPanel.setEnabled(true);
				try {
					activeWindow.puzzleBoardPanel.pausePuzzle(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				activeWindow.toFront();
				activeWindow.timer.start();
				activeWindow.getPuzzleBoardPanel().requestFocusInWindow();
				userPaused=false;
			}
		}
		else
		{
			if(pause && !userPaused)
			{
				pauseButton.setText("Unpause");
				activeWindow.pauseImage.setVisible(true);
				activeWindow.setEnabled(false);
				try {
					activeWindow.puzzleBoardPanel.pausePuzzle(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				activeWindow.timer.stop();
			}
			else if (!pause && !userPaused)
			{
				pauseButton.setText("Pause");
				activeWindow.pauseImage.setVisible(false);
				activeWindow.setEnabled(true);
				try {
					activeWindow.puzzleBoardPanel.pausePuzzle(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				activeWindow.toFront();
				activeWindow.timer.start();
				activeWindow.getPuzzleBoardPanel().requestFocusInWindow();
			}
		}
	}
	
	/*
	 * Private internal class that implements ActionListener to handle all button clicks in the game windows main frame.
	 */
	private class ButtonListener implements ActionListener{	
	//Fields
	private PuzzleWindow openPuzzleWindow;
	
		//Constructor
		public ButtonListener (PuzzleWindow openPuzzleWindow)
		{
			this.openPuzzleWindow=openPuzzleWindow;
		}
		
	public void actionPerformed(ActionEvent e) {
		Object comp = e.getSource(); // Get the key pressed.
		if(comp instanceof JButton){
		String button = ((JButton)comp).getText(); //Get button text.
		if(button=="Exit game")
		{
			WindowMethods.playSound("button_click.wav");
			pauseGame (openPuzzleWindow, true, false);
			try {
				ExitGameMessageWindow exitWindow = new ExitGameMessageWindow(openPuzzleWindow);
				WindowMethods.playSound("notification.wav");
				exitWindow.setVisible(true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(button=="Restart")
		{
			WindowMethods.playSound("button_click.wav");
			if(gameMode != "Level Mode") {
			pauseGame (openPuzzleWindow, true, false);
			WindowMethods.playSound("notification.wav");
			int choice = JOptionPane.showOptionDialog(null, "Are you sure you want to restart board?", "Restart", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			switch (choice)
				{
				case 0: 
					WindowMethods.playSound("button_click.wav");
					restartPuzzleBoard();
					break;
				case (1):
					case (-1):
					WindowMethods.playSound("button_click.wav");
					openPuzzleWindow.toFront();
					pauseGame (openPuzzleWindow, false, false);
					break;
				}
			
			}
			else  {
				WindowMethods.playSound("notification.wav");
				pauseGame (openPuzzleWindow, true, false);
				WindowMethods.showMessage(openPuzzleWindow, "Can not restart game in Level Mode", "Restart", JOptionPane.INFORMATION_MESSAGE);
				pauseGame (openPuzzleWindow, false, false);
			}
		}
		else if (button=="Undo Move")
		{
			undoMove();
		}
		else if (button=="Pause")
		{
			pauseGame(openPuzzleWindow, true, true);
		}
		else if (button=="Unpause")
		{
			pauseGame(openPuzzleWindow, false, true);
		}
		else if (button=="Sound On")
		{
			WindowMethods.playSound("button_click.wav");
			changeSoundStatus(false);
		}
		else if (button=="Sound Off")
		{
			WindowMethods.playSound("button_click.wav");
			changeSoundStatus(true);
		}
				}
			}
	
	/*
	 * Changes the windows sound status (System sounds ON or OFF).
	 * @param sound - the program sound state (true - on, false - off).
	 */
	private void changeSoundStatus(boolean sound)
	{
		if (sound)
		{
			soundOnOffButton.setText("Sound On");
			try {
				soundOnOffButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/sound_on.png"))));
			} catch (IOException e) {
				e.printStackTrace();
			}
			WindowMethods.sound=true;
			WindowMethods.playSound("sound_on.wav");
		}
		else
		{
			soundOnOffButton.setText("Sound Off");
			try {
				soundOnOffButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/sound_off.png"))));
			} catch (IOException e) {
				e.printStackTrace();
			}
			WindowMethods.sound=false;
		}
		openPuzzleWindow.puzzleBoardPanel.requestFocusInWindow();
	}
	
	/*
	 * Restarts the puzzle panel within the window, initializes all puzzle related settings.
	 */
	private void restartPuzzleBoard() {
	JDialog loading = WindowMethods.showLoading();
	loading.setVisible(true);
	openPuzzleWindow.setEnabled(false);
	SwingWorker<Void, Void> process = new SwingWorker<Void, Void>(){ //Background processes that loads the new puzzle panel.
         @Override
         protected Void doInBackground() throws Exception {
        	 	openPuzzleWindow.timer=new Timer(delay, new TimerListener());;
        	 	initiateTimerTime();
				openPuzzleWindow.setTimerText();
				openPuzzleWindow.remove(openPuzzleWindow.getPuzzleBoardPanel());
				PuzzlePanel newPuzzle = new PuzzlePanel(openPuzzleWindow, gameImage, boardSize, shuffleMethod);
				openPuzzleWindow.setPuzzleBoardPanel(newPuzzle);
				newPuzzle.movesMade=0;
				openPuzzleWindow.movesCounter.setText(newPuzzle.movesMade+"");
				openPuzzleWindow.getContentPane().add(newPuzzle);
				openPuzzleWindow.revalidate();
				pauseGame (openPuzzleWindow, false, false);
				if(gameMode =="Against the Clock")
					WindowMethods.playSound("stopwatch_on.wav");
				else WindowMethods.playSound("timer_on.wav");
			return null;
         }
      };
      process.addPropertyChangeListener(new PropertyChangeListener() {  // After new puzzle panel is loaded and the new board is loaded to the current game window.
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("state")) {
               if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
            	   loading.dispose();
            	   userPaused=false;
            	   pauseGame(openPuzzleWindow, false, false);
            	   openPuzzleWindow.setEnabled(true);
            	   openPuzzleWindow.toFront();
               }
            }
         }
      });
      process.execute();  
	}
}
	
	/*
	 * An internal class that is responsible for timer changes and responses.
	 */
	private class TimerListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		Object comp = e.getSource();
		if(comp instanceof Timer){
			if (gameMode != "Against the Clock") { //Counts time normally
			seconds ++;
			if(seconds == 60){
				seconds = 0;
				minutes ++;
				if(minutes == 60){
					minutes = 0;
					hours ++;
				}
			}
			}
			else { //Against the clock - counts time backwards.
				seconds --;
				if(seconds == -1){
					seconds = 59;
					minutes --;
					if(minutes == -1){
						minutes = 59;
						hours --;
					}
				}
				if (hours==0 & minutes==0 & seconds==0) { //Against the clock time is over.
					setTimerText();
					Failed();
				}
			}
			setTimerText();
		}
		}
	}
	
	//Getters and Setters
	public Timer getTimer()
	{
		return this.timer;
	}

	public void setPuzzleBoardPanel(PuzzlePanel puzzleBoardPanel) {
		this.puzzleBoardPanel = puzzleBoardPanel;
	}
	
	public PuzzlePanel getPuzzleBoardPanel() {
		return puzzleBoardPanel;
	}
}

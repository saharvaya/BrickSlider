/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Presentation;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/*
 * A Window that is displayed in the end of each game in case the puzzle was solved.
 * Shows main details about the game and presents the end-user options to exit game or to return to main menu.
 */
public class SummaryWindow extends JFrame {
	
	//Components
	//Panel
	private Container windowPanel;
	//Buttons
	private JButton mainMenuButton;
	private JButton exitProgramButton;
	//Images
	private JLabel wonAnimation;
	//Labels
	private JLabel finishedTime;
	private JLabel modeLabel;
	private JLabel finishedMoves;
	private JLabel congratulationLabel;
	
	//Fields
	private static PuzzleWindow openPuzzleWindow;

	//Constructor
	public SummaryWindow(PuzzleWindow openWindow, String notification, String timeFinished, String movesFinished, String gameMode) throws IOException {
		this.setTitle("Brick Slider - Game Won");
		this.setResizable(false);
		this.setBounds(100, 100, 400, 300);
		openPuzzleWindow = openWindow;
		this.setLocationRelativeTo(openPuzzleWindow);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setAlwaysOnTop(true);
		WindowMethods.setIcon(this);
		WindowMethods.setLookFeel();
		windowPanel = new Container();
		windowPanel.setLayout(null);
		openPuzzleWindow=openWindow;
		
		initiateComponents(notification, timeFinished, movesFinished, gameMode);
		addComponentsToPanel();
		
		this.setContentPane(windowPanel);	
	}
	
	/*
	 * Initiates and sets all window components.
	 * @param notification - notification text to display in window.
	 * @param timeFinished - A string describing the time took to complete the puzzle.
	 * @param movesFinished - the amount of moves counted during the game.
	 * @param gameMode - the game mode that was played.
	 */
	private void initiateComponents(String notification, String timeFinished, String movesFinished, String gameMode)
	{
		mainMenuButton = new JButton("Return to main menu");
		mainMenuButton.setBounds(40, 219, 140, 23);
		mainMenuButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		mainMenuButton.addActionListener(new ButtonListener(this));
		exitProgramButton = new JButton("Exit program");
		exitProgramButton.setBounds(214, 219, 140, 23);
		exitProgramButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		exitProgramButton.addActionListener(new ButtonListener(this));
		congratulationLabel = new JLabel("<html><font size=\"+2\"><b>Congratulations! </b></font><br/><font size=\"3\">"+notification+"</font></html>");
		congratulationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		congratulationLabel.setBounds(68, 16, 297, 61);
		finishedTime = new JLabel("<html><font size=\"+1\" face=\"SansSerif\"><b>Total Time: </b></font>"+"<font size=\"4\">"+timeFinished+"</font></html>");
		finishedTime.setVerticalAlignment(SwingConstants.TOP);
		finishedTime.setBounds(40, 85, 300, 40);
		finishedMoves = new JLabel("<html><font size=\"+1\" face=\"SansSerif\"><b>Moves made: </b></font>"+"<font size=\"4\">"+movesFinished+"</font></html>");
		finishedMoves.setVerticalAlignment(SwingConstants.TOP);
		finishedMoves.setBounds(40, 125, 300, 40);
		modeLabel = new JLabel("<html><font size=\"+1\" face=\"SansSerif\"><b>Game mode: </b></font>"+"<font size=\"4\">"+gameMode+"</font></html>");
		modeLabel.setVerticalAlignment(SwingConstants.TOP);
		modeLabel.setBounds(40, 165, 300, 40);
	    wonAnimation = new JLabel(new ImageIcon(this.getClass().getResource("/animations/solved.gif")));
	    wonAnimation.setBounds(20, 13, 70, 70);
	}
	
	/*
	 * Adds all frame components to panel
	 */
	private void addComponentsToPanel()
	{
		windowPanel.add(wonAnimation);
		windowPanel.add(mainMenuButton);
		windowPanel.add(exitProgramButton);
		windowPanel.add(finishedTime);
		windowPanel.add(finishedMoves);
		windowPanel.add(modeLabel);
		windowPanel.add(congratulationLabel);
	}
	
	/*
	 * Disposes window.
	 */
	public void closeWindow()
	{
		this.dispose();
	}
	
	/*
	 * An internal class implementing the ActionListener responsible of the button click in window.
	 */
	private class ButtonListener implements ActionListener{
		private SummaryWindow sumWindow;
		
		public ButtonListener (SummaryWindow sumWindow)
		{
			this.sumWindow=sumWindow;
		}
		
		public void actionPerformed(ActionEvent e) {
			Object comp = e.getSource();
			String button;
			if(comp instanceof JButton){
				WindowMethods.playSound("button_click.wav");
				button = ((JButton)comp).getText();
				if(button=="Return to main menu")
				{
					this.sumWindow.dispose();
					openPuzzleWindow.dispose();
					try {
						NewGameWindow newGame = new NewGameWindow();
						newGame.setVisible(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else if(button=="Exit program")
				{
					System.exit(0);
				}
				else if(button=="Cancel")
				{
					openPuzzleWindow.getTimer().start();
					openPuzzleWindow.setEnabled(true);
					openPuzzleWindow.toFront();
					openPuzzleWindow.pauseImage.setVisible(false);
				}
			}
			}
		}
}

/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Presentation;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Font;
import java.io.IOException;

import javax.swing.UIManager;
import java.awt.Container;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/*
 * Starting window, welcomes the user and presents options to start game (goes to main menu to choose game details) or to exit program.
 */
public class WelcomeWindow extends JFrame {
	
	//Components
	//Container
	private Container windowContainer;
	//Labels
	private JLabel welcomeLabel;
	//Buttons
	private JButton newGameButton;
	private JButton exitButton;

	public WelcomeWindow() throws IOException {
		this.setTitle("Brick Slider");
		WindowMethods.setLookFeel();
		WindowMethods.setIcon(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 481, 202);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.windowContainer=new Container();
		this.toFront();
		windowContainer.setLayout(null);
		initiateComponents();
		addComponentsToPanel();

		this.setContentPane(windowContainer);
	}
	
	/*
	 * Initiates and sets all window components.
	 */
	private void initiateComponents() throws IOException
	{

		welcomeLabel = new JLabel("Welcome to Sliding Blocks OOP 182 - Hope you will enjoy!");
		welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		welcomeLabel.setBounds(79, 23, 319, 14);
		
		newGameButton = new JButton("New Game");
		newGameButton.setBounds(79, 56, 141, 61);
		newGameButton.setBorder(new BevelBorder(BevelBorder.RAISED, UIManager.getColor("Button.focus"), null, null, null));
		newGameButton.setContentAreaFilled(false);
		newGameButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		newGameButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/play_icon.png"))));
		newGameButton.addActionListener(new ButtonListener(this));
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(257, 56, 141, 61);
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		exitButton.setContentAreaFilled(false);
		exitButton.setBorder(new BevelBorder(BevelBorder.RAISED, UIManager.getColor("Button.focus"), null, null, null));
		exitButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/exit_icon.png"))));
		exitButton.addActionListener(new ButtonListener(this));
	}
	
	/*
	 * Adds all frame components to panel
	 */
	private void addComponentsToPanel()
	{
		windowContainer.add(welcomeLabel);
		windowContainer.add(newGameButton);
		windowContainer.add(exitButton);
	}
	
	/*
	 * A inner class responsible for listening to button clicks in window.
	 */
	private class ButtonListener implements ActionListener{	
		WelcomeWindow welcomeWindow;
		
		public ButtonListener(WelcomeWindow welcomeWindow)
		{
			this.welcomeWindow=welcomeWindow;
		}
		
		public void actionPerformed(ActionEvent e) {
			Object comp = e.getSource();
			if(comp instanceof JButton){
				String button = ((JButton)comp).getText();
				if(button =="New Game")
				{
					NewGameWindow newGameWindow = null;
					try {
						WindowMethods.playSound("button_click.wav");
						WindowMethods.playSound("start_sound.wav");
						newGameWindow = new NewGameWindow();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					welcomeWindow.dispose();
					newGameWindow.setVisible(true);
				}
				else if(button =="Exit")
				{
					WindowMethods.playSound("notification.wav");
					int choice = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Exiting", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					switch (choice)
						{
						case 0: 
							WindowMethods.playSound("button_click.wav");
							System.exit(0);
							break;
						case 1: 
							WindowMethods.playSound("button_click.wav");
							break;
						}
				}
			}
		}
	}
}

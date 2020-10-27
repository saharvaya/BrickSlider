/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Presentation;

import java.awt.Container;
import java.awt.Font;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ExitGameMessageWindow extends JFrame {
	
	//Components
	//Container
	private Container windowPanel;
	//Buttons
	private JButton mainMenuButton;
	private JButton exitProgramButton;
	private JButton cancelButton;
	//Labels
	private JLabel exitLabel;
	private JLabel exitImage;
	//Fields
	private static PuzzleWindow openPuzzleWindow;
	
	//Constructor
	public ExitGameMessageWindow(PuzzleWindow openWindow) throws IOException {
		this.setTitle("Brick Slider - Exit Game");
		this.setResizable(false);
		this.setBounds(100, 100, 405, 202);
		this.setLocationRelativeTo(null);
		WindowMethods.setIcon(this);
		WindowMethods.setLookFeel();
		windowPanel = new Container();
		windowPanel.setLayout(null);
		openPuzzleWindow=openWindow;
		this.addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent evt) {
			     cancelButton.doClick();
			   }
			  });
		initiateComponents();
		addComponentsToPanels();
		
		this.setContentPane(windowPanel);
	}
	
	/*
	 * Initializes all window components.
	 */
	private void initiateComponents() throws IOException
	{
		mainMenuButton = new JButton("Return to main menu");
		mainMenuButton.setBounds(35, 78, 142, 23);
		mainMenuButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		mainMenuButton.addActionListener(new ButtonListener(this));
		exitProgramButton = new JButton("Exit program");
		exitProgramButton.setBounds(216, 78, 142, 23);
		exitProgramButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		exitProgramButton.addActionListener(new ButtonListener(this));
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(128, 112, 133, 44);
		cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cancelButton.addActionListener(new ButtonListener(this));
		exitLabel = new JLabel("<html><b>Warning! Any game progress will not be saved.</b><br/>Choose where do you want to exit?</html>");
		exitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		exitLabel.setBounds(55, 23, 263, 44);
		exitLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		exitImage = new JLabel("");
		exitImage.setHorizontalAlignment(SwingConstants.TRAILING);
		exitImage.setBounds(337, 11, 48, 56);
		exitImage.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/exit_icon.png"))));
	}
	
	/*
	 * Adds window components to containers.
	 */
	private void addComponentsToPanels()
	{
		windowPanel.add(mainMenuButton);
		windowPanel.add(exitProgramButton);
		windowPanel.add(cancelButton);
		windowPanel.add(exitLabel);
		windowPanel.add(exitImage);
	}
	
	/*
	 * Disposes window
	 */
	public void closeWindow()
	{
		this.dispose();
	}
	
	/*
	 * A private Inner class that is responsible for all button runtime operations in window.
	 */
	private class ButtonListener implements ActionListener{
		//Fields
		private ExitGameMessageWindow exiWindow;
		
		//Constructor
		public ButtonListener (ExitGameMessageWindow exitWindow)
		{
			this.exiWindow=exitWindow;
		}
		
		public void actionPerformed(ActionEvent e) {
			Object comp = e.getSource();
			String button;
			if(comp instanceof JButton){
				WindowMethods.playSound("button_click.wav");
				button = ((JButton)comp).getText();
				if(button=="Return to main menu")
				{
					this.exiWindow.dispose();
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
					this.exiWindow.dispose();
					openPuzzleWindow.setVisible(true);
					openPuzzleWindow.pauseGame(openPuzzleWindow, false, false);
				}
			}
			}
		}
}

/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Presentation;

import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Logic.ResourceLoader;

/*
 * A Helper class containing static methods used in all windows to enrich user experience
 */
public class WindowMethods extends JFrame{
	
	//Fields
	private static HashMap<String, BufferedImage> systemImages; //All system images loaded to systemImages at startup.
	public static boolean sound = true; // Determines whether the program sound will be on or off.
	
	/*
	 * Loads all system images to systemImages HashMap
	 */
	public static void loadImages() throws IOException
	{
		systemImages = new HashMap<String, BufferedImage>();
		String[] imageNames = Images.getImageNames();
		for(String imageName : imageNames)
		{
			imageName = imageName+".jpg";
			systemImages.put(imageName, ImageIO.read(ResourceLoader.load("/images/"+imageName)));
		}
	}
	
	/*
	 * Methods responsible for returning a loaded image.
	 * @param imageName - the name of the images requested.
	 * @return A BufferedImage type image. 
	 */
	public static BufferedImage getImage(String imageName)
	{
		return systemImages.get(imageName);
	}
	
	/*
	 * Sets the look and feel of the window to Windows Classic theme.
	 */
	static void setLookFeel()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Sets windows icon to the programs icon.
	 * @param frame - A frame to set the program icon to.
	 */
	static void setIcon (JFrame frame) {
		frame.setIconImage(new ImageIcon(frame.getClass().getResource("/icons/program_icon.png")).getImage());
	}
	
	/*
	 * Plays a sound by the sound file name.
	 * @param soundName - the file sound name to be played.
	 */
	public static synchronized void playSound(final String soundName) {
		if(sound) //If sound is enabled.
		{
		   try {
			   InputStream inputSound = ResourceLoader.load("/sounds/"+soundName);
			   InputStream bufferedInputSound = new BufferedInputStream(inputSound);
		    Clip clip = AudioSystem.getClip();
			  AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedInputSound);
			    clip.open(inputStream);
			    clip.start(); 
		   } catch (Exception e) {
		     System.err.println(e.getMessage());
		   }
	}
  }
	
	/*
	 * Shows a message by using JOptionPane and cancels close operation.
	 * @param parentComponents - Displays the message relative to this component.
	 * @param message - A message to display in the dialog.
	 * @param title - A title of the dialog window.
	 * @param messageType - Determines message behavior, choices and icon.
	 */
	public static void showMessage(Component parentComponent, String message, String title, int messageType) {
		JOptionPane pane = new JOptionPane(message, messageType);
		JDialog dialog = pane.createDialog(parentComponent, title);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(true);
		dialog.pack();
		dialog.setVisible(true);
  }
	
	/*
	 * Shows a loading window animation to be used when loading time takes significant time.
	 * @return A JDialog with loading text and animation to be displayed.
	 */
	static JDialog showLoading() {
	    JLabel loadingAnimation = new JLabel(new ImageIcon(WindowMethods.class.getResource("/animations/loading.gif")));
	    loadingAnimation.setHorizontalAlignment(SwingConstants.LEFT);
	    loadingAnimation.setText("<html><font size=\"4\" face=\"SansSerif\"><b>Loading game board...</b></font><br/><font size=\"2\" face=\"SansSerif\">Shuffling please wait.</font></html>");
		JDialog loadingFrame = new JDialog();
		loadingFrame.setUndecorated(true);
	    loadingFrame.add(loadingAnimation);
	    loadingFrame.pack();
	    loadingFrame.setLocationRelativeTo(null);
	    loadingFrame.setIconImage(new ImageIcon(WindowMethods.class.getResource("/icons/program_icon.png")).getImage());
	    loadingFrame.setAlwaysOnTop(true);
	    loadingFrame.repaint();
	 
	  return loadingFrame;
    }
}

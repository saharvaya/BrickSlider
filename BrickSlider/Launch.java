/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
import java.awt.EventQueue;

import Logic.SystemBoards;
import Presentation.WelcomeWindow;
import Presentation.WindowMethods;

public class Launch {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeWindow startWindow = new WelcomeWindow();
					WindowMethods.loadImages();
					SystemBoards.loadBoards(startWindow);
					startWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

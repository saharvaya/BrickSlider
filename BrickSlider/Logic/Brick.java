/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Logic;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/*
 * Represents a certain brick in the puzzle panel game board.
 */
public class Brick extends JButton{
	
	//Fields
	private boolean isLastBrick; //Determines whether this is the empty spot brick.
	private ImageIcon imageCut; //The bricks relative image part.
	private Point position; //The brick position coordinates.
	
	//Constructors
	public Brick() // Empty brick constructor
	{
		super();
    	this.setBorderPainted(false);
    	this.setContentAreaFilled(false);
		this.isLastBrick=true;
	}
	
	public Brick(Image image){ //Filled brick constructor
		super();
		this.imageCut=new ImageIcon(image);
		this.setIcon(imageCut);
		this.isLastBrick=false;
        BorderFactory.createLineBorder(Color.DARK_GRAY);
        
        this.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseEntered(MouseEvent e) { //Hover effect
	                setBorder(BorderFactory.createLineBorder(Color.CYAN));
	            }
	            @Override
	            public void mouseExited(MouseEvent e) {
	                setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
	            }
	        });
	}
	
	//Getters and Setters
	public Point getPosition() {
		return this.position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	
    public void setLastBrick() {
        
    	this.isLastBrick = true;
    }

    public boolean isLastBrick() {

        return this.isLastBrick;
    }
}
/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Presentation;

import java.util.Random;

/*
 * An Enum defining the existing system images loaded on runtime.
 */
public enum Images {
	
	//Enum image values
	DEFAULT("Default"),
	BREAKING_BAD("Breaking Bad"),
	THE_AVNEGERS("The Avengers"),
	STORM_TROOPER("Storm Trooper"),
	RICK_AND_MORTY("Rick and Morty"),
	FAMILY_GUY("Family Guy"),
	SOUTH_PARK("South Park"),
	STRANGER_THINGS("Stranger Things"),
	SILICON_VALLEY("Silicon Valley"),
	THE_BIG_BANG_THEORY("The Big Bang Theory"),
	X_MEN("X-Men"),
	DEADPOOL("Deadpool"),
	JUSTICE_LEAGUE("Justice League"),
	THE_WALKING_DEAD("The Walking Dead"),
	LORD_OF_THE_RINGS("Lord Of The Rings"),
	GAME_OF_THRONES("Game Of Thrones"),
	LION("Lion"),
	FOX("Fox"),
	RED_PANDA("Red Panda"),
	JAPAN("Japan"),
	FLAGS("Flags");
	
	//Fields
	private String name;
	  
    //Constructor
	private Images (String name) {
	   this.name = name;
	}
	//Methods
	/*
	 * Return the name of specific image.
	 * @return string representing system image name.
	 */
	  public String getName() {
	    return this.name;
	  }
	  
	  /*
	   * Return the amount of system images.
	   * @return an integer representing the amount of system images.
	   */
	  public static int getSize()
	  {
		  int size=0;
		  for (Images instance : Images.values()) {
			  size++;
		  }
		  return size;
	  }
	  
	  /*
	   * Return an array of all system image names.
	   * @return an array of Strings representing all system image names.
	   */
	  public static String[] getImageNames()
	  {
		  	String[] imageNames = new String[getSize()];
		  	int index =0;
		    for (Images instance : Images.values()) {
		      imageNames[index]= instance.getName();
		      index++;
		    }
		    return imageNames;
	  }	  
	  
	  /*
	   * Return a random enum value representing a random system image.
	   * @return a random enum value representing a random system image.
	   */
	  public static Images randomizeImage()
	  {
		  return Images.values()[new Random().nextInt(Images.values().length)];
	  }	  
}

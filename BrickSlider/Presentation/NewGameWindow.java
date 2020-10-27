/**
 * Brick-Slider
 * @author Itay Bouganim, 305278384
 * @author Sahar Vaya , 205583453
 */
package Presentation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import Logic.SystemBoards;

import javax.swing.SwingWorker;
import javax.swing.JCheckBox;

/*
 * A window that lets a user choose all wanted game parameters and start a game with the chosen parameters.
 */
public class NewGameWindow extends JFrame {
	
	//Components
	//Containers
	private Container windowContainer;
	//Combo Boxes
	private JComboBox<String> imageChoiceCombo;
	private JComboBox<String> dimensionsChoiceCombo;
	//Buttons
	private JButton chooseFromFileButton;
	private JButton randomImageButton;
	private JButton customDimensionButton;
	private JButton startGameButton;
	private JButton dimResetButton;
	//Radio Buttons
	private JRadioButton singleGameModeRadio;
	private JRadioButton levelModeRadio;
	private JRadioButton autoShuffleRadio;
	private JRadioButton csvShuffleRadio;
	private JRadioButton againstTheClockRadio;
	//Labels
	private JLabel shuffleChooseLabel;
	private JLabel imagePreviewLabel;
	private JLabel modeChooeLabel;
	private JLabel noCSVBoardsLabel;
	private JLabel imageChooseLabel;
	private JLabel dimensionChooseLabel;
	private JLabel chooseNLabel;
	//Text Fields
	private JTextField dimSizeText;
	//Images
	private JLabel previewImage;
	private JLabel logoImage;
	//Check Boxes
	private JCheckBox randomImageCheck;
	//Fields
	private int boardSize;
	private BufferedImage selectedImage;
	private BufferedImage userImage;
	private JFileChooser fileChooser;

	//Constructor
	public NewGameWindow() throws IOException {
		//=============================== Initialize window
		this.setTitle("Brick Slider");
		getContentPane().setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 850, 500);
		this.setLocationRelativeTo(null);
		WindowMethods.setIcon(this);
		WindowMethods.setLookFeel();
		
		windowContainer = new Container();
		windowContainer.setBounds(0, 0, 846, 418);
		windowContainer.setLayout(null);
		logoImage = new JLabel("");
		logoImage.setBounds(48, 19, 130, 120);
		logoImage.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/program_logo.png")).getScaledInstance(logoImage.getWidth(), logoImage.getHeight(), Image.SCALE_SMOOTH)));
		startGameButton = new JButton("Start Game");
	    startGameButton.setBackground(Color.LIGHT_GRAY);
		startGameButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		startGameButton.setBounds(58, 400, 130, 43);
		startGameButton.addActionListener(new StartGameListener());
		initiateImageChoiceComponents();
		initiateDimensionChoiceComponenets();
		initiateGameModeComponents();
		initiateShuffleComponents();
		addComponentsToPanels();
		this.setContentPane(windowContainer);

		this.userImage = null;
		this.boardSize=3; //Default board size
	    SelectImage();
	}
	
	/*
	 * Adds all window related components to panels.
	 */
	private void addComponentsToPanels()
	{
		 if(!SystemBoards.isLoadedBoards())
			 windowContainer.add(noCSVBoardsLabel);
		 windowContainer.add(shuffleChooseLabel);
		 windowContainer.add(autoShuffleRadio);
		 windowContainer.add(csvShuffleRadio);
		 windowContainer.add(logoImage);
		 windowContainer.add(randomImageButton);
		 windowContainer.add(imageChooseLabel);
		 windowContainer.add(imageChoiceCombo);
		 windowContainer.add(imagePreviewLabel);
		 windowContainer.add(previewImage);
		 windowContainer.add(chooseFromFileButton);
		 windowContainer.add(dimensionsChoiceCombo);
		 windowContainer.add(dimensionChooseLabel);
		 windowContainer.add(customDimensionButton);
		 windowContainer.add(dimResetButton);
		 windowContainer.add(chooseNLabel);
		 windowContainer.add(dimSizeText);
		 windowContainer.add(modeChooeLabel);
		 windowContainer.add(singleGameModeRadio);
		 windowContainer.add(levelModeRadio);
		 windowContainer.add(againstTheClockRadio);
		 windowContainer.add(startGameButton);
		 windowContainer.add(randomImageCheck);
	}
	
	/*
	 * Initializes all image choice combo box components.
	 */
	private void ImageComboChoiceInitialize() {
		imageChoiceCombo = new JComboBox<String>();
		imageChoiceCombo.setBounds(58, 247, 300, 38);
		imageChoiceCombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		String[] imageNames = Images.getImageNames();
		imageChoiceCombo.setModel(new DefaultComboBoxModel<String>(imageNames));
		imageChoiceCombo.addItem("Custom user image");
		imageChoiceCombo.setSelectedIndex(0);
		imageChoiceCombo.addActionListener(new ImageChoiceListener());
	}
	
	/*
	 * Initializes all image choice related components.
	 */
	private void initiateImageChoiceComponents()
	{
		 ImageComboChoiceInitialize();
		 imageChooseLabel = new JLabel("Choose Image:");
	 	 imageChooseLabel.setForeground(new Color(0, 0, 128));
		 imageChooseLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		 imageChooseLabel.setBounds(58, 203, 115, 16);
		 imagePreviewLabel = new JLabel("Image preview:");
		 imagePreviewLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		 imagePreviewLabel.setForeground(new Color(128, 0, 0));
		 imagePreviewLabel.setBounds(415, 12, 115, 22);
		 previewImage = new JLabel();
		 previewImage.setBorder(new LineBorder(null, 4));
		 previewImage.setBounds(415, 36, 400, 400);
		 chooseFromFileButton = new JButton("Choose custom image");
		 chooseFromFileButton.setBounds(193, 221, 165, 23);
		 chooseFromFileButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		 chooseFromFileButton.addActionListener(new ImageChoiceListener());
		 randomImageButton = new JButton("I'm Feeling lucky");
		 randomImageButton.setToolTipText("Click to choose random image.");
		 randomImageButton.setBounds(58, 221, 115, 23);
		 randomImageButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		 randomImageButton.addActionListener(new ImageChoiceListener());
		 randomImageCheck = new JCheckBox("Start with random image.");
		 randomImageCheck.setFont(new Font("Tahoma", Font.PLAIN, 9));
		 randomImageCheck.setBounds(58, 376, 155, 23);
	}
	
	/*
	 * Initializes dimension choice combo box components.
	 */
	private void DimensionsComboChoiceInitialize() {
		dimensionsChoiceCombo = new JComboBox<String>();
		String[] dimensions = new String[] {"3x3", "4x4", "5x5", "NxN (Custom)"};
		dimensionsChoiceCombo.setModel(new DefaultComboBoxModel<String>(dimensions));
		dimensionsChoiceCombo.setSelectedIndex(0);
		dimensionsChoiceCombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		dimensionsChoiceCombo.setBounds(58, 323, 300, 38);
		dimensionsChoiceCombo.addActionListener(new DimensionChoiceListener(dimensions.length, this));		
	}
	
	/*
	 * Initializes all board dimension choice related components.
	 */
	private void initiateDimensionChoiceComponenets()
	{
		 DimensionsComboChoiceInitialize();
		 dimensionChooseLabel = new JLabel("Choose Board Dimension:");
		 dimensionChooseLabel.setForeground(new Color(0, 0, 128));
		 dimensionChooseLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		 dimensionChooseLabel.setBounds(58, 296, 165, 16);
		 customDimensionButton = new JButton(">");
		 customDimensionButton.setToolTipText("Click to set custom board dimensions.");
		 customDimensionButton.setVisible(false);
		 customDimensionButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		 customDimensionButton.setBounds(316, 372, 42, 30);
		 customDimensionButton.addActionListener(new DimensionChoiceListener(-1, this));
		 dimResetButton = new JButton("Reset");
		 dimResetButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		 dimResetButton.setBounds(275, 405, 83, 20);
		 dimResetButton.setVisible(false);
		 dimResetButton.addActionListener(new DimensionChoiceListener(-1, this));
		 chooseNLabel = new JLabel("Choose N:");
		 chooseNLabel.setVisible(false);
		 chooseNLabel.setForeground(new Color(0, 100, 0));
		 chooseNLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		 chooseNLabel.setBounds(212, 372, 59, 16);
		 dimSizeText = new JTextField();
		 dimSizeText.setVisible(false);
		 dimSizeText.setBounds(275, 372, 34, 30);
		 dimSizeText.setFont(new Font("SansSerif", Font.BOLD, 11));
		 dimSizeText.setColumns(10);
		 dimSizeText.addKeyListener(new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            if (dimSizeText.getText().length() >= 3 ) // limit to 3 characters
                e.consume();
        }});
	}
	
	/*
	 * Initializes all game mode choice related components.
	 */
	private void initiateGameModeComponents()
	{
		 modeChooeLabel = new JLabel("Choose Game Mode:");
		 modeChooeLabel.setForeground(new Color(0, 0, 128));
		 modeChooeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		 modeChooeLabel.setBounds(243, 36, 130, 16);
		 singleGameModeRadio = new JRadioButton("Single Game");
		 singleGameModeRadio.setFont(new Font("Tahoma", Font.PLAIN, 11));
		 singleGameModeRadio.setBounds(243, 60, 115, 18);
		 levelModeRadio = new JRadioButton("Level Mode");
		 levelModeRadio.setBounds(243, 120, 115, 18);
		 levelModeRadio.setFont(new Font("Tahoma", Font.PLAIN, 11));
		 againstTheClockRadio = new JRadioButton("Against the Clock");
		 againstTheClockRadio.setBounds(243, 90, 115, 18);
		 againstTheClockRadio.setFont(new Font("Tahoma", Font.PLAIN, 11));
		 singleGameModeRadio.addActionListener(new ModeChoiceListener());
		 levelModeRadio.addActionListener(new ModeChoiceListener());
		 againstTheClockRadio.addActionListener(new ModeChoiceListener());
		 singleGameModeRadio.setSelected(true);
	}
	
	/*
	 *Initializes all board shuffle mode related components.
	 */
	private void initiateShuffleComponents()
	{
		 shuffleChooseLabel = new JLabel("Choose shuffle method:");
		 shuffleChooseLabel.setForeground(new Color(0, 0, 128));
		 shuffleChooseLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		 shuffleChooseLabel.setBounds(58, 157, 170, 16);
		 autoShuffleRadio = new JRadioButton("Auto shuffle");
		 autoShuffleRadio.setBounds(60, 176, 100, 18);
		 autoShuffleRadio.setFont(new Font("Tahoma", Font.PLAIN, 11));
		 csvShuffleRadio = new JRadioButton("CSV file shuffle");
		 csvShuffleRadio.setBounds(160, 176, 120, 18);
		 csvShuffleRadio.setFont(new Font("Tahoma", Font.PLAIN, 11));
		 if(!SystemBoards.isLoadedBoards())
			 csvShuffleRadio.setEnabled(false);
		 noCSVBoardsLabel = new JLabel("<html>No CSV boards<br/> available.</html>");
		 noCSVBoardsLabel.setForeground(new Color(128, 0, 0));
		 noCSVBoardsLabel.setFont(new Font("Tahoma", Font.BOLD, 9));
		 noCSVBoardsLabel.setBounds(270, 175, 130, 25);
		 autoShuffleRadio.setSelected(true);
		 autoShuffleRadio.addActionListener(new ShuffleChoiceListener());
		 csvShuffleRadio.addActionListener(new ShuffleChoiceListener());
	}
	
	/*
	 * Defines selected image by image chosen in selecting combo box.
	 * If chose user image calls the user image choice method.
	 */
	private void SelectImage() {
		if(imageChoiceCombo.getSelectedItem()!=null)
		{
			if(!(imageChoiceCombo.getSelectedIndex()==imageChoiceCombo.getItemCount()-1))
			{
				String imageName = (String)imageChoiceCombo.getSelectedItem()+".jpg";
				selectedImage = WindowMethods.getImage(imageName);
			}
			else if(userImage==null){
			int choice = JOptionPane.showOptionDialog(null, "No custom image uploaded yet,\nDo you want to upload image?", "Upload custom image", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			WindowMethods.playSound("button_click.wav");
			switch (choice)
				{
				case 0: 
					chooseUserImage();
					break;
				case 1: 
					break;
				}
			}
			else
			{
				selectedImage=userImage;
			}
			ShowSelectedImage(false);
		}
	}
	
	/*
	 * Shows selected image in image preview section.
	 */
	private void ShowSelectedImage(boolean newImage)
	{
		try {
				ImageIcon image = new ImageIcon(selectedImage.getScaledInstance(previewImage.getWidth(), previewImage.getHeight(), Image.SCALE_SMOOTH));
				previewImage.setIcon(image);
				previewImage.repaint();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unvalid selection.", "No image found", JOptionPane.OK_OPTION );
		}
	}
	
	/*
	 * Opens file chooser to select user image from end-users system.
	 * if image is valid parses, selects and shows it in image preview section.
	 */
	private void chooseUserImage()
	{
		File selectedFile = openFileChooser("Please choose an image.", "Image files", "Open image", ImageIO.getReaderFileSuffixes());
		  try {
	        	userImage = ImageIO.read(selectedFile);
	        	selectedImage = userImage;
	        	imageChoiceCombo.setSelectedIndex(imageChoiceCombo.getItemCount()-1);
	        	ShowSelectedImage(true);
	        } catch (IOException ex) {}
	}
	
	/*
	 * A method responsible for selecting file from users system by using JFileChooser.
	 * @param titleText - text for the title of the file chooser window to be opened.
	 * @param filterText - text to show in file filtering of the file chooser window to be opened.
	 * @param openButtonText - text to show in the selection button of the file chooser window to be opened.
	 * @param allowedFileTypes - a set of allowed file types to be chosen in JFilChooser.
	 * @return A File a user chose.
	 */
	private File openFileChooser(String titleText, String filterText, String openButtonText, String[] allowedFileTypes)
	{
		this.fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(titleText);
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(filterText, allowedFileTypes); //Limit allowed file types to be chosen.
		fileChooser.setFileFilter(imageFilter);
		fileChooser.setAcceptAllFileFilterUsed(false); //Accept showing files only by file filtering limitations.
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showDialog(null, openButtonText);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile(); 
		      return selectedFile;
		}
		return null;
	}

	/*
	 * Returns a string representing game mode chosen by user by the selected radio button.
	 * @return A String representing the selected game mode.
	 */
	private String getGameMode()
	{
		return (singleGameModeRadio.isSelected())? singleGameModeRadio.getText() : (levelModeRadio.isSelected())? levelModeRadio.getText() : (againstTheClockRadio.isSelected())? againstTheClockRadio.getText() : "";
	}
	
	/*
	 * Returns a string representing shuffle mode chosen by user by the selected radio button.
	 * @return A String representing the selected shuffle mode.
	 */
	private String getShuffleMethod()
	{
		String shuffleMethod = (csvShuffleRadio.isSelected())? csvShuffleRadio.getText() : autoShuffleRadio.getText();
		return shuffleMethod;
	}
	
	/*
	 * Loads a new PuzzleWindow by the selected parameters and starts a new game.
	 */
	private void StartGame() throws IOException
	{
		JDialog loading = WindowMethods.showLoading(); //Loading animations.
		loading.setVisible(true);
		
		//Background process
		SwingWorker<Void, Void> process = new SwingWorker<Void, Void>(){
	         @Override
	         protected Void doInBackground() throws Exception {
	        	 String gameMode = getGameMode();
	        	 if(randomImageCheck.isSelected()) //If random image check box is checked.
	        		 randomImageButton.doClick();
	        	 PuzzleWindow puzzleGame = new PuzzleWindow(selectedImage, gameMode, boardSize, getShuffleMethod());
	        	 puzzleGame.setVisible(true);
	        	 puzzleGame.toFront();
	        	 if(gameMode != "Against the Clock") 
	        		 WindowMethods.playSound("timer_on.wav");
	        	 else WindowMethods.playSound("stopwatch_on.wav");
				return null;
	         }
	      };
	      process.addPropertyChangeListener(new PropertyChangeListener() { //Show after background process finished running.
	         @Override
	         public void propertyChange(PropertyChangeEvent evt) {
	            if (evt.getPropertyName().equals("state")) {
	               if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
	            	   loading.dispose();
	               }
	            }
	         }
	      });
	      process.execute();  
	      this.dispose();
	}
	
	/*
	 * Selects a random image from system images.
	 */
	public void getRandomImage()
	{
		Images image = Images.randomizeImage();
		String imageName = image.getName()+".jpg";
		selectedImage = WindowMethods.getImage(imageName);
		imageChoiceCombo.setSelectedItem(image.getName());
		ShowSelectedImage(false);
	}
	
	/*
	 * An inner class responsible for listening to changes in game mode choices.
	 */
	private class ModeChoiceListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			WindowMethods.playSound("radio_button_select.wav");
			Object comp = e.getSource();
			String selectedMode = ((JRadioButton)comp).getText();
			if(selectedMode=="Single Game")
			{
				singleGameModeRadio.setSelected(true);
				againstTheClockRadio.setSelected(false);
				levelModeRadio.setSelected(false);
				dimensionsChoiceCombo.setEnabled(true);
			}
			else if(selectedMode=="Against the Clock")
			{
				againstTheClockRadio.setSelected(true);
				levelModeRadio.setSelected(false);
				singleGameModeRadio.setSelected(false);
				dimensionsChoiceCombo.setEnabled(true);
			}
			else if(selectedMode=="Level Mode")
			{
				levelModeRadio.setSelected(true);
				againstTheClockRadio.setSelected(false);
				singleGameModeRadio.setSelected(false);
				dimensionsChoiceCombo.setEnabled(false);
				dimensionsChoiceCombo.setSelectedIndex(0);
			}
		}
	}
	
	/*
	 * An inner class responsible for listening to changes in shuffle mode choices.
	 */
	private class ShuffleChoiceListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			WindowMethods.playSound("radio_button_select.wav");
			Object comp = e.getSource();
			String selectedMode;
			if(comp instanceof JRadioButton)
				selectedMode = ((JRadioButton)comp).getText();
			else selectedMode = ((JButton)comp).getText();
			if(selectedMode=="CSV file shuffle")
			{
				autoShuffleRadio.setSelected(false);
				csvShuffleRadio.setSelected(true);
			}
			else if(selectedMode=="Auto shuffle")
			{
				csvShuffleRadio.setSelected(false);
				autoShuffleRadio.setSelected(true);
			}
		}
	}
	
	/*
	 * An inner class responsible for listening to Start Game button click.
	 */
	private class StartGameListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			
			try {
				WindowMethods.playSound("button_click.wav");
				WindowMethods.playSound("start_sound.wav");
				StartGame();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * An inner class responsible for listening to changes in dimension choices.
	 */
	private class DimensionChoiceListener implements ActionListener {
		//Fields
		private int dimensions;
		private NewGameWindow currentWindow;
		Object comp;
		
		//Constructor
		public DimensionChoiceListener(int dimensions, NewGameWindow currentWindow) {
			this.dimensions=dimensions; 
			this.currentWindow=currentWindow;
			}
		public void actionPerformed(ActionEvent e) {
		comp = e.getSource();
		//Listens to combo box changes.
		if(comp instanceof JComboBox) {
			if(dimensionsChoiceCombo.getSelectedIndex()==dimensions-1)
			{
				chooseNLabel.setVisible(true);
				dimSizeText.setVisible(true);
				customDimensionButton.setVisible(true);
				dimResetButton.setVisible(true);
				autoShuffleRadio.setSelected(true);
				csvShuffleRadio.setSelected(false);
				autoShuffleRadio.setEnabled(false);
				csvShuffleRadio.setEnabled(false);
			}
			else
			{
				if(chooseNLabel.isVisible() && dimSizeText.isVisible())
				{
				chooseNLabel.setVisible(false);
				dimSizeText.setVisible(false);
				customDimensionButton.setVisible(false);
				dimResetButton.setVisible(false);
				csvShuffleRadio.setEnabled(true);
				autoShuffleRadio.setEnabled(true);
				}
				String[] sizeChosen = dimensionsChoiceCombo.getSelectedItem().toString().split("x", 0);
				if(sizeChosen[0]!="N")
				 boardSize = Integer.parseInt(sizeChosen[0]);
			}
		}
		//Listens to buttons changes.
		else if(comp instanceof JButton && ((JButton) comp).getText()=="Reset")
		{
			WindowMethods.playSound("button_click.wav");
			boardSize=-1;
			String[] dimensions = new String[] {"3x3", "4x4", "5x5", "NxN (Custom)"};
			dimensionsChoiceCombo.setModel(new DefaultComboBoxModel<String>(dimensions));
			dimensionsChoiceCombo.setSelectedIndex(0);
			dimSizeText.setText("");
		}
		else if(comp instanceof JButton & ((JButton) comp).getText()==">") {
			String dimText = dimSizeText.getText();
			if(dimText!= null & !dimText.isEmpty())
			{
				boolean legalBoardSize = true;
				for (int i=0; i<dimText.length(); i++)
				{
					if(!(Character.isDigit(dimText.charAt(i))))
		        	{
						WindowMethods.playSound("error.wav");
						WindowMethods.showMessage(null, "Please enter a legal numeric value.\nN can be greater or equal to 2.", "Can only accept digits", JOptionPane.OK_OPTION );
		        		dimSizeText.setText(null);
		        		dimText = dimSizeText.getText();
		        		boardSize=-1;
		        		legalBoardSize=false;
		        	}
				}
				int dim = Integer.parseInt(dimText);
				if (dim<2)
				{
					WindowMethods.playSound("error.wav");
	        		WindowMethods.showMessage(null, "Please enter a legal numeric value.\nN can be greater or equal to 2.", "Can only accept digits", JOptionPane.OK_OPTION );
	        		dimSizeText.setText(null);
	        		boardSize=-1;
	        		legalBoardSize=false;
				}
				if (legalBoardSize)
				{
					WindowMethods.playSound("button_click.wav");
					boardSize=dim;
					String[] dimensions = new String[] {"3x3", "4x4", "5x5", dimText+"x"+dimText+" (Custom)"};
					dimensionsChoiceCombo.setModel(new DefaultComboBoxModel(dimensions));
					dimensionsChoiceCombo.setSelectedIndex(3);
					String[] sizeChosen = dimensionsChoiceCombo.getSelectedItem().toString().split("x", 0);
					boardSize = Integer.parseInt(sizeChosen[0]);
				}
			}
			else {
				WindowMethods.playSound("error.wav");
        		WindowMethods.showMessage(null, "Please enter a legal numeric value.\nN can be greater or equal to 2.", "Can only accept digits", JOptionPane.OK_OPTION );
			}
		}
		}
	}
	
	/*
	 * An inner class responsible for listening to changes in image choices.
	 */	
	private class ImageChoiceListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Object comp = e.getSource();
			if(comp instanceof JComboBox)
				SelectImage();
			else if(comp instanceof JButton)
			{	
				WindowMethods.playSound("button_click.wav");
				if (((JButton) comp).getText()=="Choose custom image")
					chooseUserImage();
				else if(((JButton) comp).getText()=="I'm Feeling lucky")
					getRandomImage();
			}
		}
	}
}


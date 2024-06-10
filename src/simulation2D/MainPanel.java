package simulation2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ai.PathFinder;
import entity.Robot;
import tile.TileManager;

public class MainPanel extends JPanel implements Runnable{	//Runnable and use Thread for run method
	
	// SCREEN SETTINGS
	//final byte originalTileSize = 16;						//define and instantiate originalTileSize a as byte for 16x16 pixel tile
	//final byte scale = 1; 								//define and instantiate scale as a byte in case of changing tile size
	//public final byte tileSize = originalTileSize*scale;	//define and instantiate tileSize as a byte for 16x16 pixel tile
	public final byte tileSize = 16;						//define and instantiate tileSize as a byte for 16x16 pixel tile
	
	public final int maxScreenCol = 120; 					//define and instantiate maxScreenCol as an integer for displaying tiles in column
	public final int maxScreenRow = 60;						//define and instantiate maxScreenRow as an integer for displaying tiles in row
	
	public final int screenWidth = tileSize * maxScreenCol;		//define and instantiate screenWidth as an integer for 40*(16*3) = 1920 pixels
	public final int screenHeight = tileSize * maxScreenRow;	//define and instantiate screenHeight as an integer for 20*(16*3) = 960 pixels

	// WORLD SETTINGS
	public int remainingTile = 0;							//define and instantiate remainingTile as an integer for store remaining tile
	public String mapName = "/maps/map170.txt";					//define and instantiate mapName as a String for file's name
	public int maxWorldCol = 1024; 							//define and instantiate maxWorldCol as an integer and initially set to 1024
	public int maxWorldRow = 1024; 							//define and instantiate maxWorldRow as an integer and initially set to 1024
	public boolean jobDone = setMap(mapName);				//define and instantiate jobDone as a boolean get return from setMap(mapName)
																//also set maxWorldCol, maxWorldRow size

	public TileManager tileManager = new TileManager(this);		//create a TileManager instance for handling tiles in the world.
	
	public final int worldWidth = tileSize * maxWorldCol;	//Define and instantiate the total width of the game world in pixels.
	public final int worldHeight = tileSize * maxWorldRow;	//Define and instantiate the total height of the game world in pixels.
	
	// TIME FPS SETTING
	final int FPS = 120;									//Define a constant for frames per second (FPS) as 240 (fast), 120(Moderate), 60(Slow)
	
	// Initialize variables for measuring time.
	public float secs = 0f;						// Seconds counter, initially set to 0.
	public int mins = 0;						// Minutes counter, initially set to 0.	
	public int hours = 0;						// Hours counter, initially set to 0.
	public float speedFactor = 0.5f;			// Define a speed factor with an initial value of 0.5 step
	

	KeyHandler keyH = new KeyHandler();									//define and instantiate keyHandler as KeyHandler object
	Thread mainThread;													//define mainThread as Thread object to be used as the main thread.
	public TileChecker tileChecker = new TileChecker(this);				// Create a CollisionChecker instance for handling collision detection in the simulation.
	
	public PathFinder pathFinder = new PathFinder(this);				// Create a pathFinder instance for finding path
	
	public UI ui = new UI(this);										// Create a UI (User Interface) instance for managing the user interface of the simulation.
	public Robot robot = new Robot(this, keyH);							// Create a Robot instance for automation in the simulation, 
																		// which can uses keyH (KeyHandler) for input for debugging purpose.

	public MainPanel() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	MainPanel class constructor
		*
		* Method parameters		:	none
		*
		* Method return			:	MainPanel
		*
		* Synopsis				:	Constructor of the MainPanel class. Creates an instance of the MainPanel window panel. 
		* 								This class initializes and runs main JPanel, initialize graphic components and run the simulation
		* 
		* References			:   Oracle. (2023). Class JFrame. Retrieved October 29, 2023,  
		*								from https://docs.oracle.com/javase/8/docs/api/javax/swing/JFrame.html
		*
		*							Oracle. (2023). Class JPanel. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/javax/swing/JPanel.html.
		*
		*							Oracle. (2023). Class JComponent. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/javax/swing/JComponent.html
		*
		*							Oracle. (2023). Class KeyListener. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/java/awt/event/KeyListener.html
		*
		*							Oracle. (2023). Class Thread. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
		*
		*							Oracle. (2023). Class System. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/1.5.0/docs/api/java/lang/System.html
		*
		*							Oracle. (2023). Class ClassLoader. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html
		*
		*							Oracle. (2023). Class ImageIO. Retrieved October 29, 2023,
		*								from https://docs.oracle.com/javase/8/docs/api/javax/imageio/ImageIO.html
		*
		*							Oracle. (2023). Class Font. Retrieved October 30, 2023,
		* 								from https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html
		*
		* Modifications			:
		*							Date			Developer				Notes
		*							----			---------				-----
		*							2023-10-07		W. Poomarin				Build layout
		*							2023-10-08		W. Poomarin				Load map
		*							2023-10-09		W. Poomarin				Unit control, test with keypad
		*							2023-10-10		W. Poomarin				Collision Mechanic
		*							2023-10-12		W. Poomarin				Cut Grass
		*							2023-10-14		W. Poomarin				Bouncing on 60,120 degree
		*							2023-10-18		W. Poomarin				Add UI
		*							2023-10-20		W. Poomarin				Speed Control
		*							2023-10-22		W. Poomarin				Find, Snap and Cut grass on edges Mechanic
		*							2023-10-24		W. Poomarin				Find charging station
		*							2023-10-25		W. Poomarin				Charging Mechanic
		*							2023-10-26		W. Poomarin				Count remaining grass and Move animation on edge animation
		*							2023-10-27		W. Poomarin				Stop when finish and show result
		*							2023-10-28		W. Poomarin				Make charging animation faster and Limit charging time to 1 hour
		*							2023-10-29		W. Poomarin				Load large map dimension from map files
		*							2023-10-30		W. Poomarin				Map Editor, count time method, change load file method, add status
		*							2023-10-31		W. Poomarin				Optimization: Combine tile checker classes into one class "TileChecker"
		*							2023-11-03		W. Poomarin				Change Methods' and data members' Name, Add number of Charge
		*							2023-11-06		W. Poomarin				Clean and correct some errors
		*							2023-11-07		W. Poomarin				Remove some methods
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));	//set panel dimension
		this.setBackground(Color.black);	//set Background color						
		this.setDoubleBuffered(true); 		//set to true, all the drawing from this component will be done in an off screen paint buffer.
											//to improve rendering performance. (JComponent feature)
		this.addKeyListener(keyH);			//add keyHandler (KeyListener) to this panel
		this.setFocusable(true);			//setFocusable, so this panel can be focused to receive key input 
	}
	
	public void startMainThread() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	startMainThread
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method start main thread
		* 
		* References			:   Oracle. (2023). Class Thread. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
		*
		* Modifications			:
		*							Date			Developer				Notes
		*							----			---------				-----
		*							2023-10-07		W. Poomarin				Build layout
		*							2023-10-08		W. Poomarin				Load map
		*							2023-10-09		W. Poomarin				Unit control, test with keypad
		*							2023-10-10		W. Poomarin				Collision Mechanic
		*							2023-10-12		W. Poomarin				Cut Grass
		*							2023-10-14		W. Poomarin				Bouncing on 60,120 degree
		*							2023-10-18		W. Poomarin				Add UI
		*							2023-10-20		W. Poomarin				Speed Control
		*							2023-10-22		W. Poomarin				Find, Snap and Cut grass on edges Mechanic
		*							2023-10-24		W. Poomarin				Find charging station
		*							2023-10-25		W. Poomarin				Charging Mechanic
		*							2023-10-26		W. Poomarin				Count remaining grass and Move animation on edge animation
		*							2023-10-27		W. Poomarin				Stop when finish and show result
		*							2023-10-28		W. Poomarin				Make charging animation faster and Limit charging time to 1 hour
		*							2023-10-29		W. Poomarin				Load large map dimension from map files
		*							2023-10-30		W. Poomarin				Map Editor, count time method, change load file method, add status
		*							2023-10-31		W. Poomarin				Optimization: Combine tile checker classes into one class "TileChecker"
		*							2023-11-03		W. Poomarin				Change Methods' and data members' Name, Add number of Charge
		*							2023-11-06		W. Poomarin				Clean and correct some errors
		*							2023-11-07		W. Poomarin				Remove some methods
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		mainThread = new Thread(this);		//instantiate Thread
		mainThread.start();					//start Thread
	}

	@Override
	public void run() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	run
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method run main thread
		* 
		* References			:   Oracle. (2023). Class Thread. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
		*
		*							Oracle. (2023). Class System. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/1.5.0/docs/api/java/lang/System.html
		*
		* Modifications			:
		*							Date			Developer				Notes
		*							----			---------				-----
		*							2023-10-07		W. Poomarin				Build layout
		*							2023-10-08		W. Poomarin				Load map
		*							2023-10-09		W. Poomarin				Unit control, test with keypad
		*							2023-10-10		W. Poomarin				Collision Mechanic
		*							2023-10-12		W. Poomarin				Cut Grass
		*							2023-10-14		W. Poomarin				Bouncing on 60,120 degree
		*							2023-10-18		W. Poomarin				Add UI
		*							2023-10-20		W. Poomarin				Speed Control
		*							2023-10-22		W. Poomarin				Find, Snap and Cut grass on edges Mechanic
		*							2023-10-24		W. Poomarin				Find charging station
		*							2023-10-25		W. Poomarin				Charging Mechanic
		*							2023-10-26		W. Poomarin				Count remaining grass and Move animation on edge animation
		*							2023-10-27		W. Poomarin				Stop when finish and show result
		*							2023-10-28		W. Poomarin				Make charging animation faster and Limit charging time to 1 hour
		*							2023-10-29		W. Poomarin				Load large map dimension from map files
		*							2023-10-30		W. Poomarin				Map Editor, count time method, change load file method, add status
		*							2023-10-31		W. Poomarin				Optimization: Combine tile checker classes into one class "TileChecker"
		*							2023-11-03		W. Poomarin				Change Methods' and data members' Name, Add number of Charge
		*							2023-11-06		W. Poomarin				Clean and correct some errors
		*							2023-11-07		W. Poomarin				Remove some methods
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		double drawInterval = 1000000000/FPS;	//Define and instantiate drawInterval as a drawInterval and set how many nanoseconds to complete 1 frame
		double delta = 0;						//Define and instantiate drawInterval as a delta and reset delta = 0
		long lastTime = System.nanoTime();		//Define and instantiate lastTime as a long and set to time from the machine, use nanoTime for better accuracy frame rate
		long currentTime;						//define current time
		//long timer = 0;							//for display FPS (for debugging)
		//int drawCount = 0;						//for display FPS (for debugging)	
		
		while (mainThread != null) {	//as long as gameThread exists

			currentTime = System.nanoTime();						//Instantiate currentTime as a long and set to time from the machine, use nanoTime for better accuracy frame rate
			delta += (currentTime - lastTime) / drawInterval;		//set delta how much time has passed
			//timer += (currentTime - lastTime);					//(for debugging)
			lastTime = currentTime;									//update lastTime = currentTime
			
			if (!jobDone) {											//if jobDone is false
				if (delta >= 1) {									//when delta is big enough to complete 1 frame
					// 1 UPDATE Section: update information. Example: Position, State.
					update();
					
					// 2 DRAW Section: draw the screen with the updated information. 
					repaint();										//triggers 'paintComponent(Graphics g)' method
					
					//UI	
					countTime();
					
					delta--;										//reset delta
					//drawCount++;									//for display FPS
				}
			} else {												//else
				//Job finished
				JOptionPane.showMessageDialog(null, "Job Done in " + hours + " hr " + mins + " mins " + secs + " secs" 
						+ " Number of Charges: " + robot.numberOfCharge, "Done", JOptionPane.PLAIN_MESSAGE);		//Display pop-up window
				break;
			}
		}
	}
	 
	public void update() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	update
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method calculates and update data members
		* 
		*
		* Modifications			:
		*							Date			Developer				Notes
		*							----			---------				-----
		*							2023-10-07		W. Poomarin				Build layout
		*							2023-10-08		W. Poomarin				Load map
		*							2023-10-09		W. Poomarin				Unit control, test with keypad
		*							2023-10-10		W. Poomarin				Collision Mechanic
		*							2023-10-12		W. Poomarin				Cut Grass
		*							2023-10-14		W. Poomarin				Bouncing on 60,120 degree
		*							2023-10-18		W. Poomarin				Add UI
		*							2023-10-20		W. Poomarin				Speed Control
		*							2023-10-22		W. Poomarin				Find, Snap and Cut grass on edges Mechanic
		*							2023-10-24		W. Poomarin				Find charging station
		*							2023-10-25		W. Poomarin				Charging Mechanic
		*							2023-10-26		W. Poomarin				Count remaining grass and Move animation on edge animation
		*							2023-10-27		W. Poomarin				Stop when finish and show result
		*							2023-10-28		W. Poomarin				Make charging animation faster and Limit charging time to 1 hour
		*							2023-10-29		W. Poomarin				Load large map dimension from map files
		*							2023-10-30		W. Poomarin				Map Editor, count time method, change load file method, add status
		*							2023-10-31		W. Poomarin				Optimization: Combine tile checker classes into one class "TileChecker"
		*							2023-11-03		W. Poomarin				Change Methods' and data members' Name, Add number of Charge
		*							2023-11-06		W. Poomarin				Clean and correct some errors
		*							2023-11-07		W. Poomarin				Remove some methods
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		robot.update();							//run update method from robot object
		
	}
	
	public void paintComponent(Graphics g) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	update
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method uses build-in paintComponent method of JPanel and use Graphics class features to draw objects on the screen.
		* 
		* References			:   Oracle. (2023). Class JFrame. Retrieved October 29, 2023,  
		*								from https://docs.oracle.com/javase/8/docs/api/javax/swing/JFrame.html
		*
		*							Oracle. (2023). Class JPanel. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/javax/swing/JPanel.html.
		*
		*							Oracle. (2023). Class JComponent. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/javax/swing/JComponent.html
		*
		*							Oracle. (2023). Class Graphics. Retrieved October 29, 2023,   
		*								from https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics.html
		*
		*							Oracle. (2023). Class Graphics2D. Retrieved October 29, 2023,   
		*								from https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics2D.html
		* 
		*
		* Modifications			:
		*							Date			Developer				Notes
		*							----			---------				-----
		*							2023-10-07		W. Poomarin				Build layout
		*							2023-10-08		W. Poomarin				Load map
		*							2023-10-09		W. Poomarin				Unit control, test with keypad
		*							2023-10-10		W. Poomarin				Collision Mechanic
		*							2023-10-12		W. Poomarin				Cut Grass
		*							2023-10-14		W. Poomarin				Bouncing on 60,120 degree
		*							2023-10-18		W. Poomarin				Add UI
		*							2023-10-20		W. Poomarin				Speed Control
		*							2023-10-22		W. Poomarin				Finding, Snap and Cut grass on edges Mechanic
		*							2023-10-24		W. Poomarin				Find charging station
		*							2023-10-25		W. Poomarin				Charging Mechanic
		*							2023-10-26		W. Poomarin				Count remaining grass and Move animation on edge animation
		*							2023-10-27		W. Poomarin				Stop when finish and show result
		*							2023-10-28		W. Poomarin				Make charging animation faster and Limit charging time to 1 hour
		*							2023-10-29		W. Poomarin				Load large map dimension from map files
		*							2023-10-30		W. Poomarin				Map Editor, count time method, change load file method, add status
		*							2023-10-31		W. Poomarin				Optimization: Combine tile checker classes into one class "TileChecker"
		*							2023-11-03		W. Poomarin				Change Methods' and data members' Name, Add number of Charge
		*							2023-11-06		W. Poomarin				Clean and correct some errors
		*							2023-11-07		W. Poomarin				Remove some methods
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		super.paintComponent(g);				//superclass method (JPanel) so it can be called from JPanel
	
		Graphics2D g2 = (Graphics2D)g;			//convert Graphics to Graphics2D class extends Graphics class for more control over geometry
												//, coordinate transformations, color management, and text layout.
		//tile
		tileManager.draw(g2);					//call draw method to render tile graphic
		
		//robot
		robot.draw(g2);							//call draw method to render robot graphic
		
		ui.draw(g2);							//call draw method to render ui graphic

		g2.dispose();							//dispose of this graphics context and release any system resources that it is using to save some memory.
	
	}
	
	public boolean setMap(String mapName) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	update
		*
		* Method parameters		:	the method permits String parameters to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method set maxWorldCol, maxWorldRow from file's data and set return to false to jobDone 
		* 
		* References			:   Oracle. (2023). Class ClassLoader. Retrieved October 29, 2023, 
		*								from https://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html
		* 
		*
		* Modifications			:
		*							Date			Developer				Notes
		*							----			---------				-----
		*							2023-10-07		W. Poomarin				Build layout
		*							2023-10-08		W. Poomarin				Load map
		*							2023-10-09		W. Poomarin				Unit control, test with keypad
		*							2023-10-10		W. Poomarin				Collision Mechanic
		*							2023-10-12		W. Poomarin				Cut Grass
		*							2023-10-14		W. Poomarin				Bouncing on 60,120 degree
		*							2023-10-18		W. Poomarin				Add UI
		*							2023-10-20		W. Poomarin				Speed Control
		*							2023-10-22		W. Poomarin				Find, Snap and Cut grass on edges Mechanic
		*							2023-10-24		W. Poomarin				Find charging station
		*							2023-10-25		W. Poomarin				Charging Mechanic
		*							2023-10-26		W. Poomarin				Count remaining grass and Move animation on edge animation
		*							2023-10-27		W. Poomarin				Stop when finish and show result
		*							2023-10-28		W. Poomarin				Make charging animation faster and Limit charging time to 1 hour
		*							2023-10-29		W. Poomarin				Load large map dimension from map files
		*							2023-10-30		W. Poomarin				Map Editor, count time method, change load file method, add status
		*							2023-10-31		W. Poomarin				Optimization: Combine tile checker classes into one class "TileChecker"
		*							2023-11-03		W. Poomarin				Change Methods' and data members' Name, Add number of Charge
		*							2023-11-06		W. Poomarin				Clean and correct some errors
		*							2023-11-07		W. Poomarin				Remove some methods
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

		
		try {																			//try
			InputStream inputStream = getClass().getResourceAsStream(mapName);								//load file
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			
			maxWorldCol = Integer.parseInt(bufferedReader.readLine());					//get data from the 1st line
			maxWorldRow = Integer.parseInt(bufferedReader.readLine());					//get data from the 1st line

			
			bufferedReader.close();														//close file
			
		} catch (Exception e) {
			System.out.println("fail loading map in mainPanel");						//display error message
		}
		
		return false;																	//return false

	}
	
	public void countTime() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	countTime
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method count and update time related data members
		* 
		*
		* Modifications			:
		*							Date			Developer				Notes
		*							----			---------				-----
		*							2023-10-07		W. Poomarin				Build layout
		*							2023-10-08		W. Poomarin				Load map
		*							2023-10-09		W. Poomarin				Unit control, test with keypad
		*							2023-10-10		W. Poomarin				Collision Mechanic
		*							2023-10-12		W. Poomarin				Cut Grass
		*							2023-10-14		W. Poomarin				Bouncing on 60,120 degree
		*							2023-10-18		W. Poomarin				Add UI
		*							2023-10-20		W. Poomarin				Speed Control
		*							2023-10-22		W. Poomarin				Find, Snap and Cut grass on edges Mechanic
		*							2023-10-24		W. Poomarin				Find charging station
		*							2023-10-25		W. Poomarin				Charging Mechanic
		*							2023-10-26		W. Poomarin				Count remaining grass and Move animation on edge animation
		*							2023-10-27		W. Poomarin				Stop when finish and show result
		*							2023-10-28		W. Poomarin				Make charging animation faster and Limit charging time to 1 hour
		*							2023-10-29		W. Poomarin				Load large map dimension from map files
		*							2023-10-30		W. Poomarin				Map Editor, count time method, change load file method, add status
		*							2023-10-31		W. Poomarin				Optimization: Combine tile checker classes into one class "TileChecker"
		*							2023-11-03		W. Poomarin				Change Methods' and data members' Name, Add number of Charge
		*							2023-11-06		W. Poomarin				Clean and correct some errors
		*							2023-11-07		W. Poomarin				Remove some methods
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		secs = secs + speedFactor;						//set secs = secs + speedFactor 
		if (secs >= 60) {								//set hour, minute, second 
			secs = 0;
			mins++;
			if (mins >= 60) {
				mins = 0;
				hours++;
			}
		}
	}
}

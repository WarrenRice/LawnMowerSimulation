package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;

import simulation2D.KeyHandler;
import simulation2D.MainPanel;

public class Robot extends Entity{
	MainPanel mainPanel;				// Define mainPanel as MainPanel in the simulation.
	KeyHandler keyH;					// Define keyH as KeyHandler in the simulation.
	
	public final int screenX;			// Define and instantiate screenX as an integer for the X-coordinate of the screen position.
	public final int screenY;			// Define and instantiate screenY as an integer for the Y-coordinate of the screen position.

	public final float movePowerMax = 7200f;	// Define and instantiate movePowerMax as a float for the maximum move power allowed.
	public float movePower;						// Define movePower as a float for the current move power.
	public float movePowerPercent;				// Define ovePowerPercent as a float for the current move power in percent unit.
	public String status;						// Define status as a string for displaying robot's status.
	public byte numberOfCharge = 0;				// Define and instantiate numberOfCharge as a byte

	boolean doneFindingArea = false;			// Flag indicating whether the area has been fully explored.
	boolean doneFindingMap = false;				// Flag indicating whether the map has been fully processed.
	boolean reachGoal = false;					// Flag indicating whether the goal has been reached.
	

	
	public Robot(MainPanel mainPanel, KeyHandler keyH) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	Robot class constructor
		*
		* Method parameters		:	the method permits MainPanel and KeyHandler class parameters to be entered
		*
		* Method return			:	Robot
		*
		* Synopsis				:	Constructor of the Robot class. Creates an instance of the Robot. 
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
		*							2023-11-07		W. Poomarin				Remove some methods in TileCheckers Class, Entity Class
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		this.mainPanel = mainPanel;							//Set this.mainPanel data member to mainPanel parameter
		this.keyH = keyH;									//Set this.keyH data member to keyH parameter (for debugging)
		
		screenX = mainPanel.screenWidth/2 - (mainPanel.tileSize/2);		//Set robot's position center to screen
		screenY = mainPanel.screenHeight/2 - (mainPanel.tileSize/2);	//Set robot's position center to screen
		

		
		solidArea = new Rectangle(2, 2, 12, 12);			//Set Rectangle size
		//solidArea.x = 2;
		//solidArea.y = 2;
		//solidArea.width = 12;
		//solidArea.height = 12;

		setDefaultValues();									//Call setDefaultValues to reset data member
		getRobotImage();									//Call getRobotImage to load image file
	}
	
	public void setDefaultValues() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	setDefaultValues
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method set Default Values data members of the robot
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
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		nowTileX = 2;											// Set the current tile X coordinate.
		nowTileY = 2;											// Set the current tile Y coordinate.
		worldX = mainPanel.tileSize * (nowTileX);				// Calculate the world X coordinate based on the tile size.
		worldY = mainPanel.tileSize * (nowTileY);				// Calculate the world Y coordinate based on the tile size.
		
		speed = 16*mainPanel.speedFactor;						//set robot's speed (16 * 0.5 Tile/step)
		movePower = movePowerMax;								//set movePower = movePowerMax
		

	}
	
	public void getRobotImage(){
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	getRobotImage
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method load image file for the robot.
		* 
		* References			:   Oracle. (2023). Class ImageIO. Retrieved October 29, 2023,  
		*								from https://docs.oracle.com/javase/8/docs/api/javax/imageio/ImageIO.html
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
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		try {																				
			robotImage = ImageIO.read(getClass().getResourceAsStream("/unit/robot.png"));	//load image file
		} catch (IOException e) {
			System.out.println("fail loading robot image");									//display error message
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
		* Synopsis				:	This method calculate and update data members
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
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

		
		if (!finish) {
			if (!lowPower) {										// if lowPower = true

				status = "Working";									// set status = "Working";	

				runRobot();											// run runRobot to move robot

				mainPanel.tileChecker.checkGrassTile(this);			// run checkTile to change grass Tile
				
				// Reduce the move power based on the simulation's speed factor.
				movePower = movePower - mainPanel.speedFactor;		// Set movePower = movePower - mainPanel.speedFactor (-0.5/step)
				movePowerPercent = movePower/movePowerMax*100;		// movePowerPercent = movePower/movePowerMax 
				if (movePower <= 0) {								// If move power is depleted, set the robot to lowPower.
					lowPower = true;								// Set lowPower = true
				}

			} else if (!foundCharger) {									
				status = "Low Power";
				
				backHome();											// Return to the base
				
			}else {
				status = "Charging";
				chargeRobot();										// Charge the robot and reset movePower and flags
			}
		} else {
			if (!foundCharger) {									// if findEdge = true
				status = "Finish, going back to base!";
				
				backHome();											// Return to the base

			} else {
				status = "Complete!!";								// set status = "Complete!!";
				mainPanel.jobDone = true;							// set jobDone = true
			}
		}
	}
	
	public void backHome() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	backHome
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method directs the robot to return to its docking station by following a path obtained through pathfinding.
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
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		// Get the goal tile coordinates from the tile manager.
		int goalCol = mainPanel.tileManager.dockX;
		int goalRow = mainPanel.tileManager.dockY;
		
		// Search for the path to the docking station.
		searchPath(goalCol,goalRow);
		
		if (mainPanel.pathFinder.pathList.size() != 0 ) {
			
			// Get the coordinates of the next tile in the path.
			nextTileX = mainPanel.pathFinder.pathList.get(0).col;
			nextTileY = mainPanel.pathFinder.pathList.get(0).row;
			nextX = nextTileX*16;
			nextY = nextTileY*16;
			
			// Calculate the horizontal and vertical distances between the next position and the current position.
			int speedBack = (int) (speed);
			int dX = nextX - worldX;
			int dY = nextY - worldY;
			
			// Calculate the absolute values of the horizontal and vertical distances.
			int absX = Math.abs(dX);
			int absY = Math.abs(dY);

			// Check if the movement at the increased speed is less than or equal to the absolute distances.
			if ( (speedBack <= absX) || (speedBack <= absY) ) {
				if (speedBack <= absX) {
					// Move horizontally based on the sign of the horizontal distance.
					if (dX >= 0) {
						worldX += speedBack;
					} else {
						worldX -= speedBack;
					}
				}
				else if (speedBack <= absY) {
					// Move vertically based on the sign of the vertical distance.
					if (dY >= 0) {
						worldY += speedBack;
					} else {
						worldY -= speedBack;
					}
				}
			} else {
				// If the movement exceeds the distances, update the current tile and world coordinates.
				nowTileX = nextTileX;
				nowTileY = nextTileY;
				worldX = nowTileX*16;
				worldY = nowTileY*16;
					
				mainPanel.pathFinder.pathList.remove(0);
			}

		} else {
			// If there is no path, mark that the charger has been found.
			foundCharger = true;
		}
	}

	public void searchArea() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	searchArea
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method method scans the surrounding area of the robot within a specified range 
		* 							to find the first grass tile, setting the goal tile coordinates and updating flags 
		* 							accordingly.
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
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		// Initialize variables for iteration and search area size.
		int col = 0;
		int row = 0;
		int searchArea = 5;
		
		// Calculate limits for the search area in each direction from the robot's current position.
		int limitTop = worldY/16 - searchArea;
		if (limitTop < 0) {
			limitTop = 0;
		}
		int limitButtom = worldY/16 + searchArea;
		if (limitButtom > mainPanel.maxWorldCol) {
			limitButtom = mainPanel.maxWorldCol;
		}
		int limitLeft = worldX/16 - searchArea;
		if (limitLeft < 0) {
			limitLeft = 0;
		}
		int limitRigth = worldX/16 + searchArea;
		if (limitRigth > mainPanel.maxWorldRow) {
			limitRigth = mainPanel.maxWorldRow;
		}
		
		// Initialize row and column indices to the top-left corner of the search area.
		row = limitTop;
		col = limitLeft;
		
		// Iterate through the search area to find the first grass tile (mapTileNum[col][row] == 1).
		while (col < limitRigth && row < limitButtom) {
			
			if (mainPanel.tileManager.mapTileNum[col][row] == 1) {
				// If a grass tile is found, set the goal tile coordinates and update flags.
				goTileX = col;
				goTileX = col;
				goTileY = row;
				doneFindingArea = true;
				doneFindingMap = true;
				reachGoal = false;
				break;
			}
	
			
			col++;
			if (col == limitRigth) {	
				// Move to the next row when reaching the right limit of the search area.
				col = limitLeft;
				row++;
				if (row == limitButtom) {
					// If the entire search area is traversed without finding a grass tile, update flags.
					doneFindingArea = true;
					doneFindingMap = false;
				}
			}
		}	
	}
	
	public void searchMap() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	searchMap
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method iterates through the entire map, seeking the first grass tile.
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
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		// Initialize variables for iteration.
		int col = 0;
		int row = 0;
		
		// Iterate through the entire map to find the first grass tile (mapTileNum[col][row] == 1).
		while (col < mainPanel.maxWorldCol && row < mainPanel.maxWorldRow) {
			
			if (mainPanel.tileManager.mapTileNum[col][row] == 1) {
			// If a grass tile is found, set the goal tile coordinates and update flags.
				goTileX = col;
				goTileY = row;
				doneFindingMap = true;
				reachGoal = false;
				break;
			}
	
			col++;			// Move to the next world column.
			// Move to the next world row when the last column is reached.
			if (col == mainPanel.maxWorldCol) {			
				col = 0;
				row++;
			}
		}
	}
	
	public void moveToGoal(int goTileX, int goTileY) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	moveToGoal
		*
		* Method parameters		:	the method permits integers parameters to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method navigates the robot toward a specified goal tile by searching 
		* 							for a path using the searchPath method. 
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
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		// Search for a path to the goal tile.
		searchPath(goTileX,goTileY);
		
		if (mainPanel.pathFinder.pathList.size() != 0 ) {
			
			// Get the next tile coordinates and world coordinates from the path list.
			nextTileX = mainPanel.pathFinder.pathList.get(0).col;
			nextTileY = mainPanel.pathFinder.pathList.get(0).row;
			nextX = nextTileX*16;
			nextY = nextTileY*16;
			
			// Calculate the horizontal and vertical distances between the next position and the current position.
			int speedBack = (int) (speed);
			int dX = nextX - worldX;
			int dY = nextY - worldY;
			
			// Calculate the absolute values of the horizontal and vertical distances.
			int absX = Math.abs(dX);
			int absY = Math.abs(dY);
	
			// Check if the movement at the increased speed is less than or equal to the absolute distances.
			if ( (speedBack <= absX) || (speedBack <= absY) ) {
				if (speedBack <= absX) {
					// Move horizontally based on the sign of the horizontal distance.
					if (dX >= 0) {
						worldX += speedBack;
					} else {
						worldX -= speedBack;
					}
				}
				else if (speedBack <= absY) {
					// Move vertically based on the sign of the vertical distance.
					if (dY >= 0) {
						worldY += speedBack;
					} else {
						worldY -= speedBack;
					}
				}
			} else {
				// Update current tile and world coordinates when reaching the next tile.
				nowTileX = nextTileX;
				nowTileY = nextTileY;
				worldX = nowTileX*16;
				worldY = nowTileY*16;
				
				// Remove the first node from the path list.
				mainPanel.pathFinder.pathList.remove(0);
			}
		} else {
			// If no path is available, update flags to indicate completion of area and map searches.
			doneFindingArea = false;
			doneFindingMap = false;
			reachGoal = true;
		}
	}
	
	
	public void runRobot() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	runRobot
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method executes the robot's actions by sequentially executing area search, 
		* 							map search, and movement towards the goal. 
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
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		// If the robot has not completed the area search, continue searching the area.
		if (!doneFindingArea) {
			searchArea();
		}
		
		// If the robot has not completed the map search, continue searching the map.
		if (!doneFindingMap) {
			searchMap();
		}
		
		// If the robot has not reached the goal, continue moving towards the goal.
		if (!reachGoal) {
			moveToGoal(goTileX,goTileY);
		}
	}
	
	
	public void chargeRobot() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	chargeRobot
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method stop and charge robot.
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
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		mainPanel.speedFactor = 5f;							// Set the simulation's speed factor to 5, increasing the robot's movement speed.
		
		movePower = movePower + mainPanel.speedFactor*2;	// Increase the robot's movePower, simulating a recharges process.
		movePowerPercent = movePower/movePowerMax*100;		// movePowerPercent = movePower/movePowerMax
		
		if (movePower > movePowerMax) {						// Check if the movePower exceeds the maximum allowed.
			movePower = movePowerMax;						// Set movePower = movePowerMax
			foundCharger = false;
			lowPower = false;
			numberOfCharge++;
			mainPanel.speedFactor = 0.5f;					// Reset the simulation's speed factor back to 5
		
		}
	}
	
	public void draw(Graphics2D g2) {

		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	draw
		*
		* Method parameters		:	the method permits a Graphics2D class parameter to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method set and render robot graphic
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
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		BufferedImage image = robotImage;									//Set image = robotImage
		g2.drawImage(image, screenX, screenY, mainPanel.tileSize, mainPanel.tileSize, null);	//Set position and size, and Draw image in mainPanel
	}
	
	
	public void searchPath(int goalCol, int goalRow) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	searchPath
		*
		* Method parameters		:	the method permits integers parameters to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method find path to goal from robot position.
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
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		// Calculate the starting column and row based on the robot's position and solid area.
		int startCol = (worldX + solidArea.x)/mainPanel.tileSize;
		int startRow = (worldY + solidArea.y)/mainPanel.tileSize;
		
		// Set nodes and initiate the pathfinding process.
		mainPanel.pathFinder.setNodes(startCol, startRow, goalCol, goalRow);
		mainPanel.pathFinder.search();
	}
}

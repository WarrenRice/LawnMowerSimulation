package simulation2D;

import entity.Entity;

public class TileChecker {
	
	MainPanel mainPanel;							// Define mainPanel as MainPanel in the simulation.

	public TileChecker(MainPanel mainPanel) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	CollisionChecker class constructor
		*
		* Method parameters		:	the method permits MainPanel and KeyHandler class parameters to be entered
		*
		* Method return			:	CollisionChecker
		*
		* Synopsis				:	Constructor of the CollisionChecker class. Creates an instance of the CollisionChecker.
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
		*							2023-11-07		W. Poomarin				Remove some methods in TileCheckers Class
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		this.mainPanel = mainPanel;					//Set this.mainPanel data member to mainPanel parameter
	}
	

public void checkGrassTile(Entity entity) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	checkGrassTile
		*
		* Method parameters		:	the method permits Entity class parameters to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method will determine when the contact tile is tall grass and will change tile to short grass
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
		
		// Calculate the world coordinates for the edges of the entity.
		int entityLeftWorldX = entity.worldX;							
		int entityRightWorldX = entity.worldX + 16;
		int entityTopWorldY = entity.worldY;
		int entityBottomWorldY = entity.worldY + 16;
		
		// Calculate the world columns and rows for the entity's edges.
		int entityLeftCol = entityLeftWorldX/mainPanel.tileSize;
		int entityRightCol = entityRightWorldX/mainPanel.tileSize;
		int entityTopRow = entityTopWorldY/mainPanel.tileSize;
		int entityBottomRow = entityBottomWorldY/mainPanel.tileSize;
		
		// Initialize variables to store tile numbers for entity's corners.
		int tileTopLeft, tileTopRight, TileButtomLeft, TileButtomRight;
		
		// Retrieve tile numbers at the corners of the entity.
		tileTopLeft = mainPanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
		tileTopRight = mainPanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
		TileButtomLeft = mainPanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
		TileButtomRight = mainPanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
		
		// Check if there are remaining tiles to interact with.
		if (mainPanel.remainingTile > 0) {			//if remainingTile > 0
			// Check and update tiles based on their types.
			if (tileTopLeft == 1) {
				mainPanel.tileManager.mapTileNum[entityLeftCol][entityTopRow] = 2;
				mainPanel.remainingTile--;
			}
			if (tileTopRight == 1) {
				mainPanel.tileManager.mapTileNum[entityRightCol][entityTopRow] = 2;
				mainPanel.remainingTile--;
			}
			if (TileButtomLeft == 1) {
				mainPanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow] = 2;
				mainPanel.remainingTile--;
			}
			if (TileButtomRight == 1) {
				mainPanel.tileManager.mapTileNum[entityRightCol][entityBottomRow] = 2;
				mainPanel.remainingTile--;
			}
			
		} else {									//if remainingTile == 0
			entity.finish = true;					//set flag finish = true
		}
	} 
	

	public void checkBrounceTile(Entity entity) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	checkBrounceTile
		*
		* Method parameters		:	the method permits Entity class parameters to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method will determine when the contact tile is marked as collision
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
		
		// Calculate the world coordinates for the edges of the entity.
		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
		
		// Calculate the world columns and rows for the entity's edges.
		int entityLeftCol = (int) Math.floor((entityLeftWorldX - entity.speedX)/mainPanel.tileSize);
		int entityRightCol = (int) Math.floor((entityRightWorldX - entity.speedX)/mainPanel.tileSize);
		int entityTopRow = (int) Math.floor((entityTopWorldY - entity.speedY)/mainPanel.tileSize);
		int entityBottomRow = (int) Math.floor((entityBottomWorldY - entity.speedY)/mainPanel.tileSize);
		
		// Initialize variables to store tile numbers for entity's corners.
		int tileTopLeft, tileTopRight, TileButtomLeft, TileButtomRight;
		
		// Retrieve tile numbers at the corners of the entity.
		tileTopLeft = mainPanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
		tileTopRight = mainPanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
		TileButtomLeft = mainPanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
		TileButtomRight = mainPanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
		
		// Check if the tile are collision
		if (mainPanel.tileManager.tile[tileTopLeft].boundary == true || mainPanel.tileManager.tile[tileTopRight].boundary == true 
			||	mainPanel.tileManager.tile[TileButtomLeft].boundary == true || mainPanel.tileManager.tile[TileButtomRight].boundary == true) 
		{
			entity.collisionOn = true;				//set collisionOn = true
		}
	}
}

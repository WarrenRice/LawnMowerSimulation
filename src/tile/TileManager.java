package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import simulation2D.MainPanel;

public class TileManager {
	MainPanel mainPanel;			// Define mainPanel as MainPanel in the simulation.
	public Tile[] tile;				// Define tile as Tile[] array
	public byte mapTileNum[][];		// Define mapTileNum[][] as integer array[][]
	public int dockX, dockY;		// Define dockX, dockY as integers for dock position

	boolean drawPath = true;		// Define and instantiate drawPath as boolean to display path
	
	
	public TileManager(MainPanel mainPanel) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	TileManager class constructor
		*
		* Method parameters		:	the method permits MainPanel class parameters to be entered
		*
		* Method return			:	TileManager
		*
		* Synopsis				:	This method set Default Values data members of the TileManager
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
		*							2023-11-07		W. Poomarin				Remove some methods in TileCheckers Class
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		this.mainPanel = mainPanel;							// Set this.mainPanel data member to mainPanel parameter
		
		tile = new Tile[8];								// Instantiate tile = new Tile[8]
		mapTileNum = new byte[mainPanel.maxWorldCol][mainPanel.maxWorldRow];		//Instantiate mapTileNum Array to map dimension
						
		getTileImage();										// load image and set some data sets
		loadMap(mainPanel.mapName);							// loadMap to tiles
	
	}
	
	public void getTileImage() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	getTileImage
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method load image and set data members to each tile type
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
		
		try {
			tile[0] = new Tile();																	//Instantiate tile[0] = new Tile()
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));		//load Image
			tile[0].boundary = true;																//set boundary = true
			
			
			tile[1] = new Tile();																	//Instantiate tile[1] = new Tile()
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/highgrass.png"));	//load Image
			
			
			tile[2] = new Tile();																	//Instantiate tile[2] = new Tile()
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));		//load Image
			
			
			tile[3] = new Tile();																	//Instantiate tile[3] = new Tile()
			tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));		//load Image
			tile[3].boundary = true;																//set boundary = true
			
			
			tile[4] = new Tile();																	//Instantiate tile[4] = new Tile()
			tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/dirt.png"));		//load Image
			tile[4].boundary = true;																//set boundary = true
			
			
			tile[5] = new Tile();																	//Instantiate tile[5] = new Tile()
			tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/highgrass.png"));	//load Image
			tile[5].perimeter = true;																//set perimeter = true
			
			
			tile[6] = new Tile();																	//Instantiate tile[6] = new Tile()
			tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));		//load Image
			tile[6].perimeter = true;																//set perimeter = true
			
			
			tile[7] = new Tile();																	//Instantiate tile[7] = new Tile()
			tile[7].image = ImageIO.read(getClass().getResourceAsStream("/tiles/dock.png"));		//load Image
			tile[7].perimeter = true;																//set perimeter = true
			tile[7].dock = true;																	//set dock = true
			
	
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String mapName) {

		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	loadMap
		*
		* Method parameters		:	the method permits String parameters to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method load and set tile type from file.
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
		*							2023-11-07		W. Poomarin				Remove some methods in TileCheckers Class
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		byte num;										// define num as byte to store tile type
		
		try {
			//load file
			InputStream inputStream = getClass().getResourceAsStream(mapName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = bufferedReader.readLine();	//skip first 2 lines
			line = bufferedReader.readLine();			
			
			int col = 0;								// define and instantiate col as integer set to 0
			int row = 0;								// define and instantiate row as integer set to 0
			byte dockCount = 0;
			String numbers[];							// define and instantiate numbers[] as string array
			
			while(col <  mainPanel.maxWorldCol && row < mainPanel.maxWorldRow) {
				
				line = bufferedReader.readLine();		//Read text line
				
				while(col <  mainPanel.maxWorldCol) {
					numbers = line.split(" ");			//Split the string with 'space'
					
					num = Byte.parseByte(numbers[col]);			//use col as an index for numbers[] array
					
					mapTileNum[col][row] = num;					//count grass
					if (num == 1) {
						mainPanel.remainingTile++;
					}
					if (num == 7) {								//get dockX, dockY
						dockX = dockX + col;
						dockY = dockY + row;
						dockCount++;
					}
					col++;
													//Continue until every numbers[] is stored in mapTileNum[][]
				}		
				if (col == mainPanel.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			dockX = dockX/dockCount;							//set dockX, dockY
			dockY = dockY/dockCount;
			bufferedReader.close();
			
		} catch (Exception e) {
			System.out.println("fail loading map");
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
		* Synopsis				:	This method load and set tile type from file
		* 
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
		*							2023-11-07		W. Poomarin				Remove some methods in TileCheckers Class
		*							2023-11-16		W. Poomarin				Build path finder
		*							2023-11-18		W. Poomarin				Test path finder
		*							2023-11-20		W. Poomarin				Robot go home
		*							2023-11-21		W. Poomarin				Robot go to target
		*							2023-11-22		W. Poomarin				Add display path
		*
		** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */
		
		int worldCol = 0, worldRow = 0;							//Define, instantiate worldCol, worldRow as integers and set = 0
		
		int worldX, worldY, screenX, screenY;					//Define, instantiate worldX, worldY, screenX, screenY as integers for drawing position
		int tileNum;
		
		while (worldCol < mainPanel.maxWorldCol && worldRow < mainPanel.maxWorldRow) {
			
			// Extract the tile number at the current world column and row.
			tileNum = mapTileNum[worldCol][worldRow];		
			
			// Calculate world and screen coordinates for the current tile.
			worldX = worldCol*mainPanel.tileSize;			
			worldY = worldRow*mainPanel.tileSize;			
			screenX = worldX - mainPanel.robot.worldX + mainPanel.robot.screenX;
			screenY = worldY - mainPanel.robot.worldY + mainPanel.robot.screenY;	
			
			// Render only tiles that are within the screen boundaries.
			if(	worldX + mainPanel.tileSize > mainPanel.robot.worldX - mainPanel.robot.screenX &&	
				worldX - mainPanel.tileSize < mainPanel.robot.worldX + mainPanel.robot.screenX &&
				worldY + mainPanel.tileSize > mainPanel.robot.worldY - mainPanel.robot.screenY &&
				worldY - mainPanel.tileSize < mainPanel.robot.worldY + mainPanel.robot.screenY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, mainPanel.tileSize, mainPanel.tileSize, null);
			}
			worldCol++;			// Move to the next world column.
			
			// Move to the next world row when the last column is reached.
			if (worldCol == mainPanel.maxWorldCol) {			
				worldCol = 0;
				worldRow++;
			}
		}
		
		//draw path on map
		if(drawPath == true) {
			g2.setColor(new Color(255,255,0,70));													//set path color
			int i;
			for(i = 0; i < mainPanel.pathFinder.pathList.size(); i++) {							//go through pathList
				worldX = mainPanel.pathFinder.pathList.get(i).col * mainPanel.tileSize;			//get position
				worldY = mainPanel.pathFinder.pathList.get(i).row * mainPanel.tileSize;				
				screenX = worldX - mainPanel.robot.worldX + mainPanel.robot.screenX;
				screenY = worldY - mainPanel.robot.worldY + mainPanel.robot.screenY;	
			
				
				g2.fillRect(screenX, screenY, mainPanel.tileSize, mainPanel.tileSize);			//fill color
			}
		}
	}
}

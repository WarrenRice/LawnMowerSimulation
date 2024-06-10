package ai;

import java.util.ArrayList;

import simulation2D.MainPanel;

public class PathFinder {
	
	MainPanel mainPanel;									// Define mainPanel as MainPanel in the simulation.
	Node[][] nodes;											// Define nodes as Node[][], 2D array to represent nodes on the map.
	ArrayList<Node> openList = new ArrayList<>();			// Define openList as ArrayList<Node> for List of open nodes for pathfinding.
	public ArrayList<Node> pathList = new ArrayList<>();	// Define pathList as ArrayList<Node> for List to store the final path.
	Node startNode, goalNode, currentNode;					// Define startNode, goalNode, currentNode as Nodes object for path finding
	boolean goalReached = false;							// Define goalReached as boolean. Flag indicating whether the goal is reached.
	int step = 0;											// Define step as integer for counter for the number of steps taken in pathfinding.
	
	public PathFinder(MainPanel mainPanel) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	PathFinder class constructor
		*
		* Method parameters		:	the method permits MainPanel to be entered
		*
		* Method return			:	PathFinder
		*
		* Synopsis				:	Constructor of the PathFinder class. Creates an instance of the PathFinder to find path for robot
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
		
		this.mainPanel = mainPanel;											// Assign the MainPanel reference.
		instantiateNodes();													//instantiation
	}
	
	public void instantiateNodes() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	instantiateNodes
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method creates Node array and instantiate and set nodes elements
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
		
		//create Node array
		nodes = new Node[mainPanel.maxWorldCol][mainPanel.maxWorldRow];
		
		//instantiate and set nodes elements
		int col = 0;									// Initialize column index to 0.
		int row = 0;									// Initialize row index to 0.
		
		// Loop to iterate through each column and row in the nodes array.
		for(row = 0; row < mainPanel.maxWorldRow; row++) {
			for(col = 0; col < mainPanel.maxWorldCol; col++) {
				nodes[col][row] = new Node(col,row);	// Instantiate and set nodes elements
			}
		}
	}
	
	public void resetNodes() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	resetNodes
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method resets the state of nodes and pathfinding variables.
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
		
		int col = 0;												// Initialize column index to 0.
		int row = 0;												// Initialize row index to 0.
		// Loop through each row and column of the nodes array.
		for(row = 0; row < mainPanel.maxWorldRow; row++) {
			for(col = 0; col < mainPanel.maxWorldCol; col++) {
				//Reset open, check and solid state
				nodes[col][row].open = false;
				nodes[col][row].checked = false;
				nodes[col][row].solid = false;
			}
		}
		
		//Reset other setting
		openList.clear();		// Clear the openList, removing all elements.
		pathList.clear();		// Clear the pathList, removing all elements.
		goalReached = false;	// Reset the goalReached flag.
		step = 0;				// Reset the step counter.
	}
	
	public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	setNodes
		*
		* Method parameters		:	the method permits integers to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method sets the start and goal nodes and initializes node costs.
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
		
		resetNodes();								// Reset the state of nodes and pathfinding variables.
		
		//Set Start and Goal Node
		startNode = nodes[startCol][startRow];		// Set the starting node based on given coordinates.
		currentNode = startNode;					// Set the current node to the starting node.
		goalNode = nodes[goalCol][goalRow];			// Set the goal node based on given coordinates.
		openList.add(currentNode);					// Add the starting node to the open list.
		
		
		int col = 0;								// Initialize column index to 0.
		int row = 0;								// Initialize row index to 0.
		int tileNum;								// Variable to store the tile number.
		
		// Loop through each row and column of the nodes array.
		for(row = 0; row < mainPanel.maxWorldRow; row++) {
			for(col = 0; col < mainPanel.maxWorldCol; col++) {
				//Set Solid Node
				//Check Tiles
				tileNum = mainPanel.tileManager.mapTileNum[col][row];		// Get the tile number from the map.
				// If the tile is a boundary, mark the corresponding node as solid.
				if(mainPanel.tileManager.tile[tileNum].boundary == true) {
					nodes[col][row].solid = true;
				}
				
				// Set cost for each node.
				getCost(nodes[row][col]);
			}
		}
	}
	
	public void getCost(Node node) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	getCost
		*
		* Method parameters		:	the method permits Node to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method calculates G, H, and F costs for a given node.
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
		
		//G cost
		int xDistance = Math.abs(node.col - startNode.col);		// Calculate the absolute horizontal distance.
		int yDistance = Math.abs(node.row - startNode.row);		// Calculate the absolute vertical distance.
		node.gCost = xDistance + yDistance;						// Set the G cost for the node.

		//H cost
		xDistance = Math.abs(node.col - goalNode.col);			// Calculate the absolute horizontal distance to the goal.
		yDistance = Math.abs(node.row - goalNode.row);			// Calculate the absolute vertical distance to the goal.
		node.hCost = xDistance + yDistance;						// Set the H cost for the node.
		
		//F cost
		node.fCost = node.gCost + node.hCost;					// Calculate and set the total F cost for the node.
	}
	
	public void search() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	search
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method executes search to find the path.
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
		
		while(goalReached == false && step < 50000) { // step limit to 50000 in case is trapped
			int col = currentNode.col;			// Get the column index of the current node.
			int row = currentNode.row;			// Get the row index of the current node.
			
			//Check the current node
			currentNode.checked = true;			// Mark the current node as checked.
			openList.remove(currentNode);		// Remove the current node from the open list.
			
			//Open the Up node
			if(row - 1 >= 0) {
				openNode(nodes[col][row-1]);
			}
			
			//Open the Left node
			if(col - 1 >= 0) {
				openNode(nodes[col-1][row]);
			}
			
			//Open the Down node
			if(row + 1 < mainPanel.maxWorldRow) {
				openNode(nodes[col][row+1]);
			}
			
			//Open the Right node
			if(col + 1 < mainPanel.maxWorldCol) {
				openNode(nodes[col+1][row]);
			}
			
			//Find the best node
			int bestNodeIndex = 0;
			int bestNodefCost = 50000;
			
			for(int i = 0; i < openList.size(); i++) {
				
				//Current if this node's F cost is better
				if(openList.get(i).fCost < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = openList.get(i).fCost;
				}
				//If F cost is equal, check the G cost
				else if(openList.get(i).fCost == bestNodefCost) {
					if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i;
					}
				}
			}
			
			//If there is no node in the openList, end the loop
			if(openList.size() == 0) {
				break;
			}
			
			//After the loop, openList[bestNodeIndex] is the next step (= currentNode)
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
			step++;
		}
		
		//return goalReached;
	}
	
	public void openNode(Node node) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	openNode
		*
		* Method parameters		:	the method permits Node to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	This method adds a node to the open list if it meets the criteria.
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
		
		// If the node is not open, not checked, and not solid
		if(node.open == false && node.checked == false && node.solid == false) {
			
			node.open = true;			// Mark the node as open.
			node.parent = currentNode;	// Set the parent of the node to the current node.
			openList.add(node);			// Add the node to the open list for exploration.
		}
	}
	
	public void trackThePath() {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	trackThePath
		*
		* Method parameters		:	none
		*
		* Method return			:	void
		*
		* Synopsis				:	This method tracks the path from the goal node back to the start node.
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
		
		Node current = goalNode;			// Start from the goal node.
		
		while(current != startNode) {
			pathList.add(0,current);		// Add the current node to the beginning of the pathList.
			current = current.parent;		// Move to the parent node for the next iteration.
		}
	}
}

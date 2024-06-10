package simulation2D;

import java.awt.Color;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		* Method				:	void main(String[] args)
		*
		* Method parameters		:	args - the method permits String command line parameters to be entered
		*
		* Method return			:	void
		*
		* Synopsis				:	The method runs the application.
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

		JFrame window = new JFrame();							//Define and instantiate JFrame object as window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//set close window when click on close button
		window.setResizable(false);								//disable resize window
		window.setTitle("Simulation2D");						//set title to "Simulation2D"

		MainPanel mainPanel = new MainPanel();					//Define and instantiate MainPanel object as window
		window.add(mainPanel);									//Add MainPanel to window
		
		window.pack();											//Pack every components
		
		window.setLocationRelativeTo(null);						//Set window to relative position
		window.setVisible(true);								//Display window
		
		mainPanel.startMainThread();							//Start thread startMainThread()
	}

}

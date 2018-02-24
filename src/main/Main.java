package main;

import model.World;
import view.Board;
import editor.LevelEditor;

/*
 * The Main class is used to print the main menu to the user, depending on the user choice in the main menu, 
 * it launches the world or the level editor.
 */
public class Main {

	public final static boolean TERMINAL_OUTPUT = true;
	
	public static void main(String[] args) throws InterruptedException {
		MainMenu menu = new MainMenu();
		int worldId=0;
		int editorId = 0;
		World world;
		LevelEditor le;
		Board board = null;
		
		/* The loop below is used to re launch the menu every time the user has finished a level or a worldEdit*/
		menu.setVisible(true);
		do
		{
			worldId = menu.getWorldId();
			editorId = menu.getEditor();
			
			/* WORLD PART */
			Thread.sleep(200);
			if (worldId>0)
			{
				menu.setVisible(false);
				
				/* Loop used to launch a world */
				do
				{
					world = new World(worldId);
					board = new Board(world);
					world.addObserver(board);
					world.run(); //run the game
					board.setVisible(false); //close the visual interface of the game, when it's over
					board.dispose(); //Destroy the JFrame object of the game
				}while(world.isNeedToRestart() && !world.isNeedToExit());
				menu.setWorldId(0);
				menu.setVisible(true);
			}
			/* ------ */
			
			/* LEVEL EDITOR PART */
			else if (editorId > 0) //if the user clicked to add a new custom world or to edit another one
			{
				menu.setVisible(false);
				le = new LevelEditor(editorId);
				do
				{
					Thread.sleep(200);
				}
				while(!le.isNeedToExit());
				le.setVisible(false); //close the visual interface of the game, when it's over
				le.dispose(); //Destroy the JFrame object of the game
				menu.setEditor(0);
				menu.setVisible(true);
			}
			/* ------ */
		}while(true);
	}

	
}

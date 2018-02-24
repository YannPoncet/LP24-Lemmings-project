package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * Attributes:
 * @nbStudents: store the number of students currently on the map
 * @toSpawn: store the number of students to spawn when the run() method is launched
 * @saved: store the number of students that have reached the end of the map
 * @spawnX: x coordinate where the students shall spawn
 * @spawnY: y coordinate where the students shall spawn
 * @spells: store the number of spells available for each spell type
 * @map: is a list of block that represents the entire map
 * @worldId: stores the worldID, to load the right level
 * @time: time left
 * @allStudents: arrayList containing our students
 * @speed: speed of the game, currently x1 or x4
 * @apparitionFrequency: frequency of apparition of students
 * @isPaused: if the user has clicked on the pause button
 * @needToRestart: if the user wants to restart the level
 * @needToExit: if the user wants to exit to the menu
 * 
 * Methods:
 * @run: start the game
 * @isAir: returns if the cell at the coordinate in parameter is of type air
 * @destroyBlock: destroy the block at the coordinate in parameter 
 * @addNewStudent: add a new student at the coordinates of the spawn
 * @applySkill: used to apply a specified skill to a specified student
 * @waitAndUpdateGUI: inform the observer that he has to update the game
 * @extermination: apply the skill exploder to all the students and sets the toSpawn to 0
 * @removeFromWholeGame: remove a specific student from the arrayList
 * @printIsWonGUI: inform the observer that he has to pass to the victory or defeat screen
 */
public class World extends AbstractWorld {
	public static final int NB_CELL_WIDTH = 24;
	public static final int NB_CELL_HEIGHT = 16; 
	public static final int NB_SPELLS = 6; //if we want to add some spells

	private int worldId;
	private int toSpawn;
	private int toSave;
	private int saved;
	private int spawnX;
	private int spawnY;
	private int exitX;
	private int exitY;
	private int time;
	private int spells[];
	private Block map[][];
	private CopyOnWriteArrayList<Student> allStudents;
	private int speed = 1;
	private int apparitionFrequency = 3;
	private boolean isPaused = false;
	private boolean needToRestart = false;
	private boolean needToExit = false;
	
	/* Constructor */
	public World(int worldId)
	{
		this.worldId = worldId;
		map = new Block[NB_CELL_WIDTH][NB_CELL_HEIGHT];
        spells = new int[NB_SPELLS];
        saved = 0;
        allStudents = new CopyOnWriteArrayList<>();

        /* Reading and storing from the txt file of the level */
        try{
            Scanner reader = new Scanner(new FileInputStream("ressources/levels/world"+worldId+".txt"));
            toSpawn = Integer.parseInt(reader.next());
            toSave = Integer.parseInt(reader.next());
            spawnX = Integer.parseInt(reader.next());
            spawnY = Integer.parseInt(reader.next());
            exitX = Integer.parseInt(reader.next());
            exitY = Integer.parseInt(reader.next());
            time = Integer.parseInt(reader.next());
            
            reader.nextLine();
            
            for (int i = 0; i < NB_SPELLS; i++)
            {
                spells[i] = Integer.parseInt(reader.next());
            }
            
            reader.nextLine();
            
            for (int y = 0; y < NB_CELL_HEIGHT; y++)
            {
                for (int x = 0; x < NB_CELL_WIDTH; x++)
                {	
                    map[x][y] = new Block(Integer.parseInt(reader.next()));
                }
            }
            // WE ADD THIS CONDITION TO CHANGE THE WORLD STYLE IF WE LAUNCH A CUSTOM WORLD
            if (this.worldId >= 50)
            {
                reader.nextLine();
                this.worldId = Integer.parseInt(reader.next());
            }
            
            reader.close();
        } catch(FileNotFoundException e){
            System.out.println("1. In world, scanner said: File not found !");
        }
	}
	
	/* GETTERS AND SETTERS */
	public int getSpells(int i)
	{
		return spells[i];
	}
	
	public Block[][] getmap()
	{
		return map;
	}
	public int getWorldId()
	{
		return this.worldId;
	}
	public CopyOnWriteArrayList<Student> getStudents()
	{
		return allStudents;
	}
	public int getExitX()
	{
		return this.exitX;
	}
	public int getExitY()
	{
		return this.exitY;
	}
	public int getSaved()
	{
		return this.saved;
	}
	public int getToSave()
	{
		return this.toSave;
	}
	public int getTime()
	{
		return this.time;
	}
	public int getSpeed()
	{
		return this.speed;
	}
	public int getApparitionFrequency()
	{
		return this.apparitionFrequency;
	}
	public int getToSpawn()
	{
		return this.toSpawn;
	}
	public boolean getIsPaused()
	{
		return this.isPaused;
	}
	public boolean isNeedToRestart()
	{
		return this.needToRestart;
	}
	public void setIsPaused(boolean b)
	{
		this.isPaused = b;
	}
	public void setSpeed(int s)
	{
		this.speed = s;
	}
	public void setapparitionFrenquency(int f)
	{
		if(f > 5)
		{
			f = 5;
		}
		else if(f < 1)
		{
			f = 1;
		}
		this.apparitionFrequency = f;
	}
	public void increaseSaved() {
		this.saved++;
	}

	public void setNeedToRestart(boolean needToRestart) {
		this.needToRestart = needToRestart;
	}

	public boolean isNeedToExit() {
		return needToExit;
	}

	public void setNeedToExit(boolean needToExit) {
		this.needToExit = needToExit;
	}
	/* ----- */
	
	/* Method used to launch the game */
	public void run() throws InterruptedException
	{
		int sleepTime = 1000; // the students are moving every second
		int i = 0;
		do
		{
			if(!isPaused)
			{
				sleepTime = 1000/this.speed; //we can change the speed while playing
				if (toSpawn >  0)
				{
					if(i == 0)
					{
						this.addNewStudent();
						toSpawn--;
					}
					i++;
					if(i >= apparitionFrequency)
					{
						i = 0;
					}
				}
				
				/* Making the students move */
				for(Student stud: allStudents)
				{
					stud.move();
				}
				time--;
			}
			waitAndUpdateGUI(sleepTime);
		} while(allStudents.size() > 0 && time>0 && !needToRestart && !needToExit);
		if (!needToRestart && !needToExit)
		{
			printIsWonGUI();
			/* Win or lose screen */
			do
			{
				waitAndUpdateGUI(sleepTime);
			}while(!needToRestart && !needToExit);
		}
	}
	
	
	protected void waitAndUpdateGUI(int time){
		notifyObserver();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void printIsWonGUI()
	{
		isWonObserver();
	}
	
	
	public boolean isAir(int x, int y)
	{
		if(map[x][y].getBlockType()==0 || map[x][y].getBlockType()==8 || map[x][y].getBlockType()==9)
				return true;
		return false;
	}
	
	public boolean isBlocked(int x, int y)
	{
		return map[x][y].isBlocked();
	}
	
	public void destroyBlock(int x, int y)
	{
		map[x][y].setBlockType(0);
	}
	
	private void addNewStudent() //Used in the run() method to spawn the students
	{
		Student stud = new Walker(spawnX, spawnY, this, Direction.RIGHT,0);
		this.allStudents.add(stud);
	}
	
	public void applySkill(Student stud, int type)
	{
		if (spells[type-1] > 0)
		{
			stud.changeClass(type);
			spells[type-1]--;
		}
	}
	
	public void removeFromWholeGame(int x, int y, Student stud)
	{
		allStudents.remove(stud);
	}
	public void addInGame(int x, int y, Student stud)
	{
		allStudents.add(stud);
	}
	
	public void extermination()
	{
		this.toSpawn = 0;
		for(Student stud: allStudents)
		{
			stud.changeClass(5);
		}
	}
}

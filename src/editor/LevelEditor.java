package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.World;

public class LevelEditor extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public final static int BOARD_WIDTH = 1350;
	public final static int BOARD_HEIGHT = 800; 
	public static final int NB_CELL_WIDTH = World.NB_CELL_WIDTH;
	public static final int NB_CELL_HEIGHT = World.NB_CELL_HEIGHT;
	public static final int NB_SPELLS = World.NB_SPELLS;
	
	protected BoardPanel mainPanel = new BoardPanel();
	
	/* See in the World class for the purpose of all those variables */
	protected int editorId;
	protected int toSpawn;
	protected int toSave;
	protected int spawnX;
	protected int spawnY;
	protected int exitX;
	protected int exitY;
	protected int time;
	protected int spells[];
	protected int map[][];
	/* ----- */
	
	protected boolean needToExit = false; //obvious
	protected int textureId; //current texture used: ice, dirt, cobble, or sand
	protected boolean isSaved = true; //if the map currently edited is saved
	protected int selectedBlock = 3; //block that the user wants to place
	protected boolean isSpawnPlaced = false; //obvious
	protected boolean isExitPlaced = false; //obvious
	protected boolean askForSave = false; //if we need to ask for the saving of the level
	
	public LevelEditor(int editorId) {
		readFile(editorId); //read the already existing file
        setUndecorated(true);
		setResizable(false); //don't put it below the line below
		mainPanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		add(mainPanel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Project LP24 - Student lemmings");
		setContentPane(mainPanel);
	}
	
	public boolean isNeedToExit()
	{
		return this.needToExit;
	}
	
	//Read the already existing file and stores the data, same as in the World class, see there for more details
	public void readFile(int editorId)
	{
		map = new int[NB_CELL_WIDTH][NB_CELL_HEIGHT];
		spells = new int[World.NB_SPELLS];
		this.editorId = editorId;
		
        try{
            Scanner reader = new Scanner(new FileInputStream("ressources/levels/world"+editorId+".txt"));
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
                    map[x][y] = Integer.parseInt(reader.next());
                    if (map[x][y] == 8)
                    {
                    	isSpawnPlaced = true;
                    }
                    else if (map[x][y] == 9)
                    {
                    	isExitPlaced = true;
                    }
                }
            }
            
            reader.nextLine();
            textureId = Integer.parseInt(reader.next());
            reader.close();
        } catch(FileNotFoundException e){
            System.out.println("File not found !");
        }
	}
	
	public class BoardPanel extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		public BoardPanel()
		{
            this.addMouseListener(this);
		}
		
		public void paintComponent(Graphics g)
		{
			int width = BOARD_WIDTH-150;
			int height = BOARD_HEIGHT;
			
			g.fillRect(0, 0, getWidth(), getHeight());
			
	        Font font = new Font("Arial", 0, 25);
	        g.setFont(font);
	        g.setColor(Color.WHITE);
	        
	        printTheGame(width, height, g); //Printing the graphical elements
		}
		
		/* USELESS FOR THE MOMENT */
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		/* ----- */
		
		/* CLICKS */
		@Override
		public void mousePressed(MouseEvent e){
			int width = BOARD_WIDTH - 150;
			int x = e.getX();
			int y = e.getY();
			
			if (!askForSave)
			{
				if (x < width)
				{
					int mapClickX = x/50;
					int mapClickY = y/50;
					if ((selectedBlock == 8 && isSpawnPlaced) || (selectedBlock == 9 && isExitPlaced))
					{
						
					}
					else if (selectedBlock == 8 && !isSpawnPlaced)
					{
							spawnX = mapClickX;
							spawnY = mapClickY;
							isSpawnPlaced = true;
							map[mapClickX][mapClickY] = selectedBlock;
							isSaved = false;
					}
					else if (selectedBlock == 9 && !isExitPlaced)
					{
							exitX = mapClickX;
							exitY = mapClickY;
							isExitPlaced = true;
							map[mapClickX][mapClickY] = selectedBlock;
							isSaved = false;
					}
					else
					{
						if (map[mapClickX][mapClickY] == 8)
						{
							spawnX = 0;
							spawnY = 0;
							isSpawnPlaced = false;
						}
						else if(map[mapClickX][mapClickY] == 9)
						{
							exitX = 0;
							exitY = 0;
							isExitPlaced = false;
						}
						map[mapClickX][mapClickY] = selectedBlock;
						isSaved = false;
					}
				}
				else
				{
					if (y >= 700 && y < 735)
					{
						if(x >= width+85 && x < width+120) //go back to the menu
						{
							if (isSaved)
							{
								needToExit = true;
							}
							else
							{
								askForSave = true;
							}
						}
						else if (x >= width+45 && x < width+80)
						{
							File thisWorld = new File("ressources/levels/world"+ editorId +".txt");
							thisWorld.delete(); //remove the previous map
							Path path1 = FileSystems.getDefault().getPath("ressources/levels", "worldModel.txt");
							Path path2 = FileSystems.getDefault().getPath("ressources/levels", "world"+ editorId +".txt");
							try {
								Files.copy(path1, path2); //copy the model of an empty map and paste it with the right name
							} catch (IOException e2) {
								e2.printStackTrace();
							}
							isSpawnPlaced = false;
							isExitPlaced = false;
							readFile(editorId); //actualize each variables in the map to print the level well
							isSaved = true;
						}
						else if (x >= width+5 && x < width+40 && !isSaved)
						{
							save();
						}
					}
					if (x >= width+46 && x < width+66)
					{
						for (int i = 0; i < NB_SPELLS; i++)
						{
							if (y >= 5+(i*50) && y < 25+(i*50)) //press the button to increment the number of spells we will be able to apply to the lemmings in this world
							{
								if(spells[i] >= 99)
								{
									spells[i] = 99;
								}
								else
								{
									spells[i]++;
									isSaved = false;
								}
							}
							else if (y >= 25+(i*50) && y < 45+(i*50)) //press the button to decrement the number of spells we will be able to apply to the lemmings in this world
							{
								if(spells[i] <= 0)
								{
									spells[i] = 0;
								}
								else
								{
									spells[i]--;
									isSaved = false;
								}
							}
						}
					}
					if (x >= width+64 && x < width+84)
					{
						if (y >= 305 && y < 325) //press the button to increment the time remaining to win the level
						{
							if (time >= 990)
							{
								time = 990;
							}
							else
							{
								time = time +10;
								isSaved = false;
							}
						}
						else if (y >= 325 && y < 345) //press the button to decrement the time remaining to win the level
						{
							if (time <= 0)
							{
								time = 0;
							}
							else
							{
								time = time - 10;
								isSaved = false;
							}
						}
					}
					if (x >= width+96 && x < width+116)
					{
						if (y >= 355 && y < 375) //press the button to increment the lemmings that will spawn in the level
						{
							if(toSpawn >= 99)
							{
								toSpawn = 99;
							}
							else
							{
								toSpawn++;
								isSaved = false;
							}
						}
						else if (y >= 375 && y < 395) //press the button to decrement the lemmings that will spawn in the level
						{
							if(toSpawn <= 0)
							{
								toSpawn = 0;
							}
							else
							{
								toSpawn--;
								if (toSpawn <= toSave)
								{
									toSave = toSpawn;
								}
								isSaved = false;
							}
						}
						else if (y >= 405 && y < 425) //press the button to increment the lemmings we need to save to win the level
						{
							if(toSave >= 99)
							{
								toSave = 99;
							}
							else
							{
								toSave++;
								if (toSave >= toSpawn)
								{
									toSpawn = toSave;
								}
								isSaved = false;
							}
						}
						else if (y >= 425 && y < 445) //press the button to decrement the lemmings we need to save to win the level
						{
							if(toSave <= 0)
							{
								toSave = 0;
							}
							else
							{
								toSave--;
								isSaved = false;
							}
						}
					}
					if (x >= width+5 && x < width+55)
					{
						if (y >= 460 && y < 510)
						{
							selectedBlock = 3;
						}
						else if (y >= 515 && y < 565)
						{
							selectedBlock = 8;
						}
						else if (y >= 570 && y < 620)
						{
							selectedBlock = 0;
						}
					}
					if (x >= width+60 && x < width+110)
					{
						if (y >= 460 && y < 510)
						{
							selectedBlock = 4;
						}
						else if (y >= 515 && y < 565)
						{
							selectedBlock = 9;
						}
					}
					if (x >= width+15 && x < width+45 && y >= 635 && y < 675)
					{
						if (textureId == 100)
						{
							textureId = 103;
							isSaved = false;
						}
						else
						{
							textureId--;
							isSaved = false;
						}
					}
					else if (x >= width+105 && x < width+135 && y >= 635 && y < 675)
					{
						if (textureId == 103)
						{
							textureId = 100;
							isSaved = false;
						}
						else
						{
							textureId++;
							isSaved = false;
						}
					}
				}
			}
			else
			{
				if (y >= 389 && y < 456)
				{
					if (x >= 390 && x < 523)
					{
						save();
						needToExit = true;
					}
					else if (x >= 668 && x < 801)
					{
						needToExit = true;
					}
				}
			}
			repaint();
		}
		/* ----- */

		
		private void printTheGame(int width, int height, Graphics g) {
			/* BACKGROUND */
		 	try {
				g.drawImage(ImageIO.read(new File("ressources/backgrounds/bg"+textureId+".png")), 0, 0, 1200, 800, null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		 	/* --------- */
			
		 	/* SPELLS */
			try {
				for (int i = 0; i < 6; i++)
				{
					g.drawString(""+spells[i], width+80, 35+(i*50));
					g.drawImage(ImageIO.read(new File("ressources/user_interface/skill"+ (i+1) +".png")), width+5, 5+(i*50), 40, 40, null);
					g.drawImage(ImageIO.read(new File("ressources/user_interface/plusMini.png")), width+46, 5+(i*50), 20, 20, null);
					g.drawImage(ImageIO.read(new File("ressources/user_interface/minusMini.png")), width+46, 25+(i*50), 20, 20, null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		 	/* --------- */
			
			/* PRINTING THE USEFUL INFORMATIONS */
			try {
				Font font1 = new Font("Arial", 0, 20);
				g.setFont(font1);
				
				g.drawImage(ImageIO.read(new File("ressources/user_interface/time.png")), width+5, 305, 58, 40, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/plusMini.png")), width+64, 305, 20, 20, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/minusMini.png")), width+64, 325, 20, 20, null);
				g.drawString(""+time, width+90, 332);
				
				g.drawImage(ImageIO.read(new File("ressources/user_interface/tospawn.png")), width+5, 355, 90, 40, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/plusMini.png")), width+96, 355, 20, 20, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/minusMini.png")), width+96, 375, 20, 20, null);
				g.drawString(""+toSpawn, width+120, 382);
				
				g.drawImage(ImageIO.read(new File("ressources/user_interface/objective.png")), width+5, 405, 90, 40, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/plusMini.png")), width+96, 405, 20, 20, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/minusMini.png")), width+96, 425, 20, 20, null);
				g.drawString(""+toSave, width+120, 432);
			} catch (IOException e) {
				e.printStackTrace();
			}
			/* ----- */
			
			/* LIST OF BLOCKS */
			try{
				g.drawImage(ImageIO.read(new File("ressources/blocks/w"+ textureId +"/3.png")), width+5, 460, 50, 50, null);
				if (selectedBlock == 3)
				{
					g.drawImage(ImageIO.read(new File("ressources/user_interface/selected.png")), width+5, 460, 50, 50, null);
				}
				
				g.drawImage(ImageIO.read(new File("ressources/blocks/w"+ textureId +"/4.png")), width+60, 460, 50, 50, null);
				if (selectedBlock == 4)
				{
					g.drawImage(ImageIO.read(new File("ressources/user_interface/selected.png")), width+60, 460, 50, 50, null);
				}
				
				g.drawImage(ImageIO.read(new File("ressources/blocks/w"+ textureId +"/8.png")), width+5, 515, 50, 50, null);
				if (selectedBlock == 8)
				{
					g.drawImage(ImageIO.read(new File("ressources/user_interface/selected.png")), width+5, 515, 50, 50, null);
				}
				
				g.drawImage(ImageIO.read(new File("ressources/blocks/w"+ textureId +"/9.png")), width+60, 515, 50, 50, null);
				if (selectedBlock == 9)
				{
					g.drawImage(ImageIO.read(new File("ressources/user_interface/selected.png")), width+60, 515, 50, 50, null);
				}
				
				g.drawImage(ImageIO.read(new File("ressources/user_interface/remove.png")), width+5, 570, 50, 50, null);
				if (selectedBlock == 0)
				{
					g.drawImage(ImageIO.read(new File("ressources/user_interface/selected.png")), width+5, 570, 50, 50, null);
				}
				
				g.drawImage(ImageIO.read(new File("ressources/user_interface/left.png")), width+15, 635, 30, 40, null);
				g.drawImage(ImageIO.read(new File("ressources/blocks/w"+ textureId +"/3.png")), width+50, 630, 50, 50, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/right.png")), width+105, 635, 30, 40, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			/* --------- */
			
			/* BUTTONS */
			try {
				if (isSaved)
				{
					g.drawImage(ImageIO.read(new File("ressources/user_interface/save.png")), width+5, 700, 35, 35, null);
				}
				else
				{
					g.drawImage(ImageIO.read(new File("ressources/user_interface/unsave.png")), width+5, 700, 35, 35, null);
				}
				g.drawImage(ImageIO.read(new File("ressources/user_interface/restart.png")), width+45, 700, 35, 35, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/exit.png")), width+85, 700, 35, 35, null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		 	/* --------- */
			
			
			int cellWidth = width/NB_CELL_WIDTH;
			int cellHeight = height/NB_CELL_HEIGHT;
            int blockType = 0;
            
            /* MAP */
            for (int y = 0; y < NB_CELL_HEIGHT; y++)
            {
                for (int x = 0; x < NB_CELL_WIDTH; x++)
                {
                    blockType = map[x][y];
                    if (blockType != 0)
                    {
                    	try {
							g.drawImage(ImageIO.read(new File("ressources/blocks/w"+textureId+"/"+blockType+".png")),  x*cellWidth, y*cellHeight, cellWidth, cellHeight, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
                    }
				}
			}
            if (askForSave)
            {
            	try {
					g.drawImage(ImageIO.read(new File("ressources/messages/save_message.png")), 0, 0, 1350, 800, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
		}
		
		/* SAVING THE LEVEL */
		public void save()
		{
			try {
				File world=new File("ressources/levels/world"+ editorId +".txt");
				FileWriter worldWriter = new FileWriter(world);
				worldWriter.write(""+ toSpawn +" "+ toSave +" "+ spawnX +" "+ spawnY +" "+ exitX +" "+ exitY +" "+ time +"\r\n");
				for (int i = 0; i < NB_SPELLS; i++)
				{
					worldWriter.write(""+ spells[i] +" ");
				}
				worldWriter.write("\r\n");
				for (int y = 0; y < NB_CELL_HEIGHT; y++)
				{
					for (int x = 0; x < NB_CELL_WIDTH; x++)
					{
						worldWriter.write(""+ map[x][y] +" ");
					}
					worldWriter.write("\r\n");
				}
				worldWriter.write(""+ textureId);
				worldWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isSaved = true;
		}
		/* ----- */
	}
}
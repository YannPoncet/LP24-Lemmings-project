package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.World;
import model.Student;
import model.Block;
import model.Direction;
import observer.Observer;

public class Board extends JFrame implements Observer {
	
	private static final long serialVersionUID = 1L;
	public final static int BOARD_WIDTH = 1350;
	public final static int BOARD_HEIGHT = 800;
	
	protected BoardPanel mainPanel = new BoardPanel();
	protected CopyOnWriteArrayList<Student> studs;
	protected Block map[][];
	protected int stateOfTheGame; //1 at the beginning of the game, 2 at the end
	
	/*Below, most used attributes of the world */
	protected int saved;
	protected int toSave;
	protected int time;
	protected int wordID;
	/* ----- */
	
	protected World world;
	private int selectedSkill = 0; //Indicate the skill selected by the user, 0 for nothing
	
	public Board(World world) {
		setUndecorated(true); //put the window decoration away
		setResizable(false); //don't put it under the line below
		mainPanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		add(mainPanel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); //center on the screen
		setTitle("Project LP24 - Student lemmings"); //title
		setContentPane(mainPanel);
		this.studs = world.getStudents();
		this.map = world.getmap();
		this.stateOfTheGame = 1;
		this.wordID= world.getWorldId();
		this.world = world;
	}
	
	@Override
	public void update() {
		repaint();
	}
	
	@Override
	public void isWon() {
		this.stateOfTheGame = 2;
		repaint();
	}
	

		
	public class BoardPanel extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		boolean walkState = true; //used to alternate the sprites of the students
		
		public BoardPanel()
		{
            this.addMouseListener(this);
		}
		
		public void paintComponent(Graphics g)
		{
			int width = getWidth()-150;
			int height = getHeight();
			g.fillRect(0, 0, getWidth(), getHeight()); //screen background
			
	        Font font = new Font("Arial", 0, 25); //font
	        g.setFont(font);
	        g.setColor(Color.WHITE);
	        
			if (stateOfTheGame == 1) //if we're in the game, we print it, see below
			{	 
				printTheGame(width, height, g); 
			}
			else if (stateOfTheGame== 2) //else if we are at the end of the game
			{
				
				/* WIN OR LOSE SCREEN */
			    try {
					g.drawImage(ImageIO.read(new File("ressources/backgrounds/bg"+world.getWorldId()+".png")), 0, 0, 1200, 800, null);

			        if (world.getSaved() >= world.getToSave())
			        {
						g.drawImage(ImageIO.read(new File("ressources/messages/win.png")), width/2-500/2, height/3, 500, 100, this); //Win
			        }
			        else
			        {
						g.drawImage(ImageIO.read(new File("ressources/messages/lose.png")), width/2-500/2, height/3, 500, 100, this); //Lose
			        }
					g.drawImage(ImageIO.read(new File("ressources/user_interface/restart.png")), width+5, 680, 35, 35, null);
					g.drawImage(ImageIO.read(new File("ressources/user_interface/exit.png")), width+45, 680, 35, 35, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			    /* ----- */
			    
			}
			
			if (stateOfTheGame == 1 || stateOfTheGame == 2)
			{
				
				/* PRINTING THE USEFULL INFORMATIONS */
				try {
					g.drawImage(ImageIO.read(new File("ressources/user_interface/time_left.png")), width+5, 480, 90, 40, null);
					g.drawString(""+world.getTime(), width+100, 510);
					
					g.drawImage(ImageIO.read(new File("ressources/user_interface/tospawn.png")), width+5, 530, 90, 40, null);
					g.drawString(""+world.getToSpawn(), width+100, 560);
					
					g.drawImage(ImageIO.read(new File("ressources/user_interface/objective.png")), width+5, 580, 90, 40, null);
					g.drawString(""+world.getToSave(), width+100, 610);
					
					g.drawImage(ImageIO.read(new File("ressources/user_interface/saved.png")), width+5, 630, 90, 40, null);
					g.drawString(""+world.getSaved(), width+100, 660);
				} catch (IOException e) {
					e.printStackTrace();
				}
				/* ----- */
			}
		}

	

		//All those events are useless for the moment, we use the mousePressed event.
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) { }
		
		
		/*
		 * The mousePressed is used here to know where the user clicked and do the corresponding behavior
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			//We shall do this in a Controller class. But we do not know how to implement it
			Graphics g = getGraphics();
			int clickedX = e.getX()/50;
			int clickedY = e.getY()/50;
			int width = getWidth()-150;
			int height = getHeight();
			int cellWidth = width/World.NB_CELL_WIDTH;
			int cellHeight = height/World.NB_CELL_HEIGHT;
			
			if(stateOfTheGame == 1)
			{
				if(!world.getIsPaused())
				{
					/* IF THE CLICK IS IN A CELL OF THE MAP */
					if (clickedX >= 0 && clickedX < World.NB_CELL_WIDTH && clickedY >= 0 && clickedY < World.NB_CELL_HEIGHT && selectedSkill != 0)
					{
						//to make a selection visual effect in the grid
                    	try {
							g.drawImage(ImageIO.read(new File("ressources/user_interface/selected.png")),  clickedX*cellWidth, clickedY*cellHeight, cellWidth, cellHeight, this);
						} catch (IOException exception) {
							exception.printStackTrace();
						}
                    	
                    	for(Student stud : world.getStudents())
                    	{
                    		if (stud.getX() == clickedX && stud.getY() == clickedY && stud.getClassType() != 2 && stud.getClassType() != selectedSkill)
                    		{
                    			world.applySkill(stud, selectedSkill);
                    			if(world.getSpells(selectedSkill-1) == 0)
							    {
							        selectedSkill = 0;
							    }
                    			break;
                    		}
                    	}
					}
					/* ----- */
					
					/* ELSE IF IT IS ON A BUTTON */
					else if(e.getX() > width+2 && e.getX() < width+37 && e.getY() > 430 && e.getY() < 465)
					{
						world.extermination();
					}
					else if(e.getX() > width+39 && e.getX() < width+74 && e.getY() > 430 && e.getY() < 465)
					{
						world.setapparitionFrenquency(world.getApparitionFrequency()-1);
					}
					else if(e.getX() > width+75 && e.getX() < width+110 && e.getY() > 430 && e.getY() < 465)
					{
						world.setapparitionFrenquency(world.getApparitionFrequency()+1);
					}
					else if(e.getX() > width+112 && e.getX() < width+147 && e.getY() > 430 && e.getY() < 465)
					{
						if(world.getSpeed() == 4)
						{
							try {
								g.drawImage(ImageIO.read(new File("ressources/user_interface/faster.png")), width+112, 430, 35, 35, null);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							world.setSpeed(1);
						}
						else
						{
							try {
								g.drawImage(ImageIO.read(new File("ressources/user_interface/fasterS.png")), width+112, 430, 35, 35, null);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							world.setSpeed(4);
						}
					}
					
					else
					{	
						/* ELSE IF IT IS ON A SKILL */
						if(e.getX() > width+5 && e.getX() < width+115)
						{
							clickedOnSkill(e,width, height, clickedX, clickedY, g);
						}
					}
				}
				
				if(e.getX() > width+45 && e.getX() < width+80 && e.getY() > 680 && e.getY() < 715)
				{
					//clicked on restart
					world.setNeedToRestart(true);
				}
				else if(e.getX() > width+85 && e.getX() < width+120 && e.getY() > 680 && e.getY() < 715)
				{
					//clicked on exit to menu
					world.setNeedToExit(true);
				}
				/* IF IT IS ON THE PAUSE BUTTON */
				else if(e.getX() > width+5 && e.getX() < width+40 && e.getY() > 680 && e.getY() < 715)
				{
					if(world.getIsPaused())
					{
						try {
							g.drawImage(ImageIO.read(new File("ressources/user_interface/pause.png")), width+5, 680, 35, 35, null);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						world.setIsPaused(false);
					}
					else
					{
						try {
							g.drawImage(ImageIO.read(new File("ressources/user_interface/pauseS.png")), width+5, 680, 35, 35, null);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						world.setIsPaused(true);
					}
				}
			}
			else if (stateOfTheGame == 2)
			{
				if(e.getX() > width+5 && e.getX() < width+40 && e.getY() > 680 && e.getY() < 715)
				{
					//clicked on restart
					world.setNeedToRestart(true);
				}
				else if(e.getX() > width+45 && e.getX() < width+80 && e.getY() > 680 && e.getY() < 715)
				{
					//clicked on exit to menu
					world.setNeedToExit(true);
				}
			}
		}

		
		/*
		 * This function is used to print the different images corresponding to the skills depending if the skill if selected or not, it also
		 * changes the attribute selected skill to the correct value.
		 */
		private void clickedOnSkill(MouseEvent e, int width, int height, int clickedX, int clickedY, Graphics g) {
			if(e.getY() > 120 && e.getY() < 160)
			{
				if(selectedSkill == 1)
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/basher.png")), width+5, 120, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 0;
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/basherS.png")), width+5, 120, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 1;
				}
			}
			else if(e.getY() > 170 && e.getY() < 210)
			{
				if(selectedSkill == 2)
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/blocker.png")), width+5, 170, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 0;
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/blockerS.png")), width+5, 170, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 2;
				}
			}
			else if(e.getY() > 220 && e.getY() < 260)
			{
				if(selectedSkill == 3)
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/climber.png")), width+5, 220, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 0;
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/climberS.png")), width+5, 220, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 3;
				}
			}
			else if(e.getY() > 270 && e.getY() < 310)
			{
				if(selectedSkill == 4)
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/digger.png")), width+5, 270, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 0;
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/diggerS.png")), width+5, 270, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 4;
				}
			}
			else if(e.getY() > 320 && e.getY() < 360)
			{
				if(selectedSkill == 5)
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/exploder.png")), width+5, 320, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 0;
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/exploderS.png")), width+5, 320, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 5;
				}
			}
			else if(e.getY() > 370 && e.getY() < 410)
			{
				if(selectedSkill == 6)
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/floater.png")), width+5, 370, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 0;
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/floaterS.png")), width+5, 370, 110, 40, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					selectedSkill = 6;
				}
			}
		}
		
		/*
		 * This function is used to print the background, the spells, the buttons, the level and the students
		 */
		private void printTheGame(int width, int height, Graphics g) {
			
				/* BACKGROUND */
			 	try {
					g.drawImage(ImageIO.read(new File("ressources/backgrounds/bg"+world.getWorldId()+".png")), 0, 0, 1200, 800, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			 	/* --------- */
				
			 	
			 	/* SPELLS */
				for (int k = 0; k < World.NB_SPELLS; k++)
				{
					g.drawString(""+world.getSpells(0), width+120, 150);
					try {
						if(selectedSkill == 1)
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/basherS.png")), width+5, 120, 110, 40, null);
						}
						else
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/basher.png")), width+5, 120, 110, 40, null);
						}
						g.drawString(""+world.getSpells(1), width+120, 200);
						if(selectedSkill == 2)
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/blockerS.png")), width+5, 170, 110, 40, null);
						}
						else
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/blocker.png")), width+5, 170, 110, 40, null);
						}
						g.drawString(""+world.getSpells(2), width+120, 250);
						if(selectedSkill == 3)
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/climberS.png")), width+5, 220, 110, 40, null);
						}
						else
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/climber.png")), width+5, 220, 110, 40, null);
						}
						g.drawString(""+world.getSpells(3), width+120, 300);
						if(selectedSkill == 4)
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/diggerS.png")), width+5, 270, 110, 40, null);
						}
						else
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/digger.png")), width+5, 270, 110, 40, null);
						}			
						g.drawString(""+world.getSpells(4), width+120, 350);
						if(selectedSkill == 5)
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/exploderS.png")), width+5, 320, 110, 40, null);
						}
						else
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/exploder.png")), width+5, 320, 110, 40, null);
						}
						g.drawString(""+world.getSpells(5), width+120, 400);
						if(selectedSkill == 6)
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/floaterS.png")), width+5, 370, 110, 40, null);
						}
						else
						{
							g.drawImage(ImageIO.read(new File("ressources/user_interface/floater.png")), width+5, 370, 110, 40, null);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			 	/* --------- */
				
				
				/* BUTTONS */
				try {
					g.drawImage(ImageIO.read(new File("ressources/user_interface/explode.png")), width+2, 430, 35, 35, null);
					g.drawImage(ImageIO.read(new File("ressources/user_interface/plus.png")), width+39, 430, 35, 35, null);
					g.drawImage(ImageIO.read(new File("ressources/user_interface/minus.png")), width+75, 430, 35, 35, null);
					if(world.getSpeed() == 4)
					{
						g.drawImage(ImageIO.read(new File("ressources/user_interface/fasterS.png")), width+112, 430, 35, 35, null);
					}
					else
					{
						g.drawImage(ImageIO.read(new File("ressources/user_interface/faster.png")), width+112, 430, 35, 35, null);
					}
					if(world.getIsPaused())
					{
						g.drawImage(ImageIO.read(new File("ressources/user_interface/pauseS.png")), width+5, 680, 35, 35, null);
					}
					else
					{
						g.drawImage(ImageIO.read(new File("ressources/user_interface/pause.png")), width+5, 680, 35, 35, null);
					}
					g.drawImage(ImageIO.read(new File("ressources/user_interface/restart.png")), width+45, 680, 35, 35, null);
					g.drawImage(ImageIO.read(new File("ressources/user_interface/exit.png")), width+85, 680, 35, 35, null);
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			 	/* --------- */
				
				
				int cellWidth = width/World.NB_CELL_WIDTH;

				int cellHeight = height/World.NB_CELL_HEIGHT;
	            int blockType = 0;
	            
	            
	            /* MAP */
	            for (int y = 0; y < World.NB_CELL_HEIGHT; y++)
	            {
	                for (int x = 0; x < World.NB_CELL_WIDTH; x++)
	                {
	                    blockType = map[x][y].getBlockType();
	                    if (blockType != 0)
	                    {
	                    	try {
								g.drawImage(ImageIO.read(new File("ressources/blocks/w"+wordID+"/"+blockType+".png")),  x*cellWidth, y*cellHeight, cellWidth, cellHeight, this);
							} catch (IOException e) {
								e.printStackTrace();
							}
	                    }
	                    
					}
				}
			 	/* --------- */
	            
	            
	            /* STUDENTS AND THEIR ANNIMATIONS */
		        Random rand = new Random();
	            int rnd = 0; 
	            for(Student stud: studs)
	            {
	                try {
	            		if (stud.getDirection().equals(Direction.RIGHT))
	                     {
	                         if (walkState)
	                         {
	                             g.drawImage(ImageIO.read(new File("ressources/student_sprites/"+ stud.getClassType() +"/char1.png")), stud.getX()*cellWidth, stud.getY()*cellHeight, cellWidth, cellHeight, this);
	                         }
	                         else
	                         {
	                             g.drawImage(ImageIO.read(new File("ressources/student_sprites/"+ stud.getClassType() +"/char2.png")), stud.getX()*cellWidth, stud.getY()*cellHeight, cellWidth, cellHeight, this);
	                         }
	                     }
	                     else
	                     {
	                         if (walkState)
	                         {
	                             g.drawImage(ImageIO.read(new File("ressources/student_sprites/"+ stud.getClassType() +"/char3.png")), stud.getX()*cellWidth, stud.getY()*cellHeight, cellWidth, cellHeight, this);
	                         }
	                         else
	                         {
	                             g.drawImage(ImageIO.read(new File("ressources/student_sprites/"+ stud.getClassType() +"/char4.png")), stud.getX()*cellWidth, stud.getY()*cellHeight, cellWidth, cellHeight, this);
	                         }
	                     }
	                 } catch (IOException e) {
	                     e.printStackTrace();
	                 }
	                 if (rnd == 99)
	                 {
	                     walkState = !walkState;
	                 }
	                 rnd = rand.nextInt(5);
	                 if (rnd == 4)
	                 {
	                     walkState = !walkState;
	                     rnd = 99;
	                 }
	            }
	            walkState = !walkState;
	            /* --------- */
		}
	}
}

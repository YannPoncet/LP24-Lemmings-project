package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * The main menu is a graphical class used to print the different choices to the user
 * when he launches the game.
 */
public class MainMenu extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public final static int BOARD_WIDTH = 1350;
	public final static int BOARD_HEIGHT = 800;
	
	protected BoardPanel mainPanel = new BoardPanel();
	protected int worldId = 0; //Used to store the id of the level to launch
	protected int editor = 0; //Used to store the custom level to edit
	private boolean isFree[]; //Used to know which custom levels can be launched
	
	/* Constructor */
	public MainMenu() {
		setResizable(false); //don't put it below, it causes some problems with swing
		mainPanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		add(mainPanel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Project LP24 - Student lemmings");
		setContentPane(mainPanel);
		isFree = new boolean[16];
	}
	
	/* Getters and setters */
	public int getWorldId()
	{
		return this.worldId;
	}
	public void setWorldId(int id)
	{
		this.worldId = id;
	}
	public int getEditor()
	{
		return this.editor;
	}
	public void setEditor(int e)
	{
		this.editor = e;
	}
	
	/* Panel */
	public class BoardPanel extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		private boolean isEditOn = false; //If the user want to edit a level, it is true
		private boolean isDeleteOn = false; //If the user want to delete a level, it is true
		
		public BoardPanel()
		{
            this.addMouseListener(this);
		}
		
		public void paintComponent(Graphics g)
		{
			/* GRAPHICAL ELEMENTS: titles, icons */
			g.fillRect(0, 0, getWidth(), getHeight());
			
	        Font font = new Font("Arial", 0, 25);
	        g.setFont(font);
	        g.setColor(Color.WHITE);
		    try {
		    	g.drawImage(ImageIO.read(new File("ressources/user_interface/title.png")), 175, 20, 1000, 200, null);
				g.drawImage(ImageIO.read(new File("ressources/messages/campaign.png")), 100, 200, 500, 100, null);
				g.drawImage(ImageIO.read(new File("ressources/messages/custom.png")), 100, 375, 500, 100, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/edit.png")), 600, 395, 160, 60, null);
				g.drawImage(ImageIO.read(new File("ressources/user_interface/delete.png")), 770, 395, 160, 60, null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    /* ----- */
		    
		    /* CAMPAIGN THUMBNAILS */
		    for(int i = 1; i < 9; i++)
			{
				try {
					g.drawImage(ImageIO.read(new File("ressources/thumbnails/thumb"+ i +".png")), 150 + ((i-1)*125), 300, 100, 67, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		    /* ----- */
		    
		    /* CUSTOM THUMBNAILS AND FILLING THE ISFREE CELL */
		    for(int i = 0; i < 2; i++)
		    {
		    	for(int k = 0; k < 8; k++)
		    	{
		    		try {
		    			if(i == 0) //If we are on the fist line
		    			{
		    				g.drawImage(ImageIO.read(new File("ressources/thumbnails/thumb"+ (k+50) +".png")), 150 + (k*125), 475 + (i*92), 100, 67, null);
		    				isFree[k] = false;
		    			}
		    			else //If we are on the second line
		    			{
		    				g.drawImage(ImageIO.read(new File("ressources/thumbnails/thumb"+ (k+58) +".png")), 150 + (k*125), 475 + (i*92), 100, 67, null);
		    				isFree[k+8] = false;
		    			}
					} catch (IOException e) {
						//We enter the catch state if there is no thumbnail, that's how we detect that there is also no level created
						try {
							g.drawImage(ImageIO.read(new File("ressources/thumbnails/thumb0.png")), 150 + (k*125), 475 + (i*92), 100, 67, null);
							if(i == 0) //If we are on the fist line
							{
								isFree[k] = true;
							}
							else //If we are on the second line
							{
								isFree[k+8] = true;
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
		    	}
		    }
		    /* ---- */
		}
			
		/* CLICKS */
		@Override
		public void mousePressed(MouseEvent e) {
			Graphics g = getGraphics();
			int x = e.getX();
			int y = e.getY();

			/* CLICK ON THE EDIT BUTTON */
			if (x > 600 && x < 760 && y > 395 && y < 455)
			{
				if (isEditOn) //If we are already in edit mode
				{
					isEditOn = false;
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/edit.png")), 600, 395, 160, 60, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else
				{
					isEditOn = true;
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/editS.png")), 600, 395, 160, 60, null);
						if (isDeleteOn)
						{
							isDeleteOn = false;
							g.drawImage(ImageIO.read(new File("ressources/user_interface/delete.png")), 770, 395, 160, 60, null);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			/* ----- */
			
			/* CLICK ON THE DELETE BUTTON */
			else if (x > 770 && x < 930 && y > 395 && y < 455)
			{
				if (isDeleteOn) //If we are already in delete mode
				{
					isDeleteOn = false;
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/delete.png")), 770, 395, 160, 60, null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else
				{
					isDeleteOn = true;
					try {
						g.drawImage(ImageIO.read(new File("ressources/user_interface/deleteS.png")), 770, 395, 160, 60, null);
						if (isEditOn)
						{
							isEditOn = false;
							g.drawImage(ImageIO.read(new File("ressources/user_interface/edit.png")), 600, 395, 160, 60, null);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			/* ----- */
			
			/* CLICK ON A CAMPAIGN LEVEL */
			else if (y >= 300 && y < 367)
			{
				for (int i = 0; i < 8; i++)
				{
					if (x >= (150 + i*125) && x < (250 + i*125))
					{
						worldId = i+1; //We will launch the world i+1
					}
				}
			}
			/* ----- */
			
			/* CLICK ON A CUTOM LEVEL ON THE FIRST LINE */
			else if (y >= 475 && y < 542)
			{
				for (int i = 0; i < 8; i++)
				{
					if (x >= (150 + i*125) && x < (250 + i*125))
					{
						if (!isFree[i] && !isEditOn && !isDeleteOn) //launch the level if the level was edited
						{
							worldId = i+50;
						}
						else if (isEditOn || (isFree[i] && !isDeleteOn)) //open the level editor for a custom world
						{
							editor = i+50;
							if (isFree[i] == true) //if the slot was empty, we create the files for a new custom world before the level editor launch
							{
								Path path = FileSystems.getDefault().getPath("ressources/thumbnails", "thumbEdit.png");
								Path path1 = FileSystems.getDefault().getPath("ressources/thumbnails", "thumb"+ (i+50) +".png");
								try {
									Files.copy(path, path1); //copy the icon to place it on the main menu after the graphic interface is updated
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								Path path2 = FileSystems.getDefault().getPath("ressources/levels", "worldModel.txt");
								Path path3 = FileSystems.getDefault().getPath("ressources/levels", "world"+ (i+50) +".txt");
								try {
									Files.copy(path2, path3); //copy the model of an empty map and paste it with the right name
								} catch (IOException e2) {
									e2.printStackTrace();
								}
							}
							isEditOn = false;
							repaint();
						}
						else if (isDeleteOn && !isFree[i]) //delete the files of an existing custom world
						{
							File ftxt = new File("ressources/levels/world"+ (i+50) +".txt");
							ftxt.delete();
							File fpng = new File("ressources/thumbnails/thumb"+ (i+50) +".png");
							fpng.delete();
							repaint();
							isDeleteOn = false;
						}
					}
				}
			}
			/* ----- */
			
			/* CLICK ON A CUTOM LEVEL ON THE SECOND LINE */
			else if (y >= 567 && y < 634)
			{
				for (int i = 0; i < 8; i++)
				{
					if (x >= (150 + i*125) && x < (250 + i*125))
					{
						if (!isFree[i+8] && !isEditOn && !isDeleteOn) //launch the level if the level was edited
						{
							worldId = i+58;
						}
						else if (isEditOn || (isFree[i+8] && !isDeleteOn)) //open the level editor for a custom world
						{
							editor = i+58;
							if (isFree[i+8] == true) //if the slot was empty, we create the files for a new custom world before the level editor launch
							{
								Path path = FileSystems.getDefault().getPath("ressources/thumbnails", "thumbEdit.png");
								Path path1 = FileSystems.getDefault().getPath("ressources/thumbnails", "thumb"+ (i+58) +".png");
								try {
									Files.copy(path, path1);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								Path path2 = FileSystems.getDefault().getPath("ressources/levels", "worldModel.txt");
								Path path3 = FileSystems.getDefault().getPath("ressources/levels", "world"+ (i+58) +".txt");
								try {
									Files.copy(path2, path3);
								} catch (IOException e2) {
									e2.printStackTrace();
								}
							}
							isEditOn = false;
							repaint();
						}
						else if (isDeleteOn && !isFree[i+8]) //delete the files of an existing custom world
						{
							File ftxt = new File("ressources/levels/world"+ (i+58) +".txt");
							ftxt.delete();
							File fpng = new File("ressources/thumbnails/thumb"+ (i+58) +".png");
							fpng.delete();
							repaint();
							isDeleteOn = false;
						}
					}
				}
			}
			/* ----- */
		}
		
		/* UNUSED FUNCTIONS */
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		/* ----- */

	}
}


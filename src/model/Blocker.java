package model;

public class Blocker extends Student
{
	public Blocker(int x, int y, World world, Direction dir,int f)
	{
		super(x,y,world,dir,2,f);
		this.world.getmap()[posX][posY].setBlocked(true); //Setting the Block to blocked
	}
	
	@Override
	public void move() //A blocker doesn't move
	{
		if (posX == world.getExitX() && posY == world.getExitY()) //If he reaches the end of the stage
		{
			world.increaseSaved();
			this.remove();
		} 
			else
			{
			if(posY==World.NB_CELL_HEIGHT-1)	//if he is at the bottom of the map
			{
				remove();
			}
			else if(world.isAir(posX,posY+1))	//Checking if a ground is existing and falling if it doesn't exist
			{
				world.getmap()[posX][posY].setBlocked(false);
				translate(Direction.DOWN);
				world.getmap()[posX][posY].setBlocked(true);
			}
			else if(fallHeight>DEADLY_FALL_HEIGHT)	//if the student has fallen for too long he dies
			{
				remove();
			}
		}
	}
	
	public void remove()
	{
		this.world.getmap()[posX][posY].setBlocked(false); //Setting the Block to unblocked 
		this.world.removeFromWholeGame(posX, posY, this);
	}
}
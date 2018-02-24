package model;

public class Digger extends Student
{	
	private boolean hasAlreadyDig = false; //The digger can dig only while there are breakable blocks, stores if it has already dig
	public Digger(int x, int y, World world, Direction dir,int f)
	{
		super(x,y,world,dir,4,f);
	}
	
	@Override
	public void move()
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
			else if(this.world.isAir(posX,posY+1))	//Checking if a ground is existing and falling if it doesn't exist
			{
				translate(Direction.DOWN);
			}
			else if(this.fallHeight>DEADLY_FALL_HEIGHT)	//if the student has fallen for too long he dies
			{
				this.remove();
			}
			else
			{
				if(this.world.getmap()[posX][posY+1].getBlockType() == 4 || (this.fallHeight>1 && hasAlreadyDig)) 	//he loose his power if the block is unbreakable, if he has fallen
				{
					super.changeClass(0); //he becomes a walker
				}
				else if(this.world.getmap()[posX][posY+1].getBlockType() != 4) //else he breaks the block bottom, only if this block is breakable
				{
					this.world.getmap()[posX][posY+1].decreaseBlockType();
					hasAlreadyDig = true;
				}
				this.fallHeight=0; 
			}
		}
	}
}
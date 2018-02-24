package model;

public class Floater extends Student
{
	public Floater(int x, int y, World world, Direction dir,int f)
	{
		super(x,y,world,dir,6,f);
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
			else if(world.isAir(posX,posY+1))	//Checking if a ground is existing and falling if it doesn't exist
			{
				translate(Direction.DOWN);
				fallHeight = 0;
			}
			else if(direction==Direction.RIGHT)
			{
				if(posX==World.NB_CELL_WIDTH-1) //if he is at the right border of the map
				{
					remove();
				}
				else if(world.isAir(posX+1,posY) && !world.isBlocked(posX+1,posY)) //if he can go right, he does
				{
					translate(direction);
				}
				else if(this.world.isAir(posX-1,posY) && !world.isBlocked(posX-1,posY)) //else he bounces and move left if he can
				{
					direction=Direction.LEFT;
					translate(direction);
				}
			}
			else if(direction==Direction.LEFT)
			{
				if(posX==0) //if he is at the left border of the map
				{
					remove();
				}
				else if(world.isAir(posX-1,posY) && !world.isBlocked(posX-1,posY)) //if he can go left, he does
				{
					translate(direction);
				}
				else if(world.isAir(posX+1,posY) && !world.isBlocked(posX+1,posY)) //else he bounces and move right if he can
				{
					direction=Direction.RIGHT;
					translate(direction);
				}
			}
		}
	}
}
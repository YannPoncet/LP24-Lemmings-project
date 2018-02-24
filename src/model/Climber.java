package model;

public class Climber extends Student
{
	private boolean isClimbing; //Obvious
	public Climber(int x, int y, World world, Direction dir,int f)
	{
		super(x,y,world,dir,3,f);
		this.isClimbing=false;
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
			else if(world.isAir(posX,posY+1) && isClimbing==false)	//Checking if a ground is existing and falling if it doesn't exist
			{
				translate(Direction.DOWN);
			}
			else if(fallHeight>DEADLY_FALL_HEIGHT)	//if the student has fallen for too long he dies
			{
				remove();
			}
			else
			{
				fallHeight=0;
				if(direction==Direction.RIGHT)
				{
					if(posX==World.NB_CELL_WIDTH-1) //if he is at the right border of the map
					{
						remove();
					}
					else if(world.isAir(posX+1,posY) && !world.isBlocked(posX+1,posY)) //if he can go right, he does
					{
						isClimbing=false;
						translate(direction);
					}
					//if it is blocked or he is at the top of the map or there is no air above the student and he is climbing, then the direction changes
					else if(world.isBlocked(posX+1,posY) || posY==0 || (!world.isAir(posX,posY-1) && isClimbing) || (!world.isAir(posX,posY-1) && !isClimbing))
					{
						isClimbing=false;
						direction=Direction.LEFT;
					}
					else if(!world.isAir(posX+1,posY)) //else if there is a wall on the right, he can climb on it
					{
						isClimbing=true;
						translate(Direction.UP);
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
						isClimbing=false;
						translate(direction);
					}
					//if it is blocked or he is at the top of the map or there is no air above the student and he is climbing, then the direction changes
					else if(world.isBlocked(posX-1,posY) || posY==0 || (!world.isAir(posX,posY-1) && isClimbing) || (!world.isAir(posX,posY-1) && !isClimbing))
					{
						isClimbing=false;
						direction=Direction.RIGHT;
					}
					else if(!world.isAir(posX-1,posY)) //else if there is a wall on the left, he can climb on it
					{
						isClimbing=true;
						translate(Direction.UP);
					}
				}
			}
		}
	}
}
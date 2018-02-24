package model;

public class Basher extends Student
{
	private boolean alreadyDig = false; //The basher can dig only while there are breakable blocks, stores if it has already dig
	
	public Basher(int x, int y, World world, Direction dir,int f)
	{
		super(x,y,world, dir,1,f);
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
				this.fallHeight=0;
				if(this.direction==Direction.RIGHT)
				{
					if(posX==World.NB_CELL_WIDTH-1) //if he is at the right border of the map
					{
						remove();
					}
					else if(this.world.isAir(posX+1,posY) && !this.world.isBlocked(posX+1, posY)) //if he can go right, he does
					{
						if(posX+1!=World.NB_CELL_WIDTH-1 && this.world.isAir(posX+2,posY) && alreadyDig)
						{
							super.changeClass(0);
						}
						super.translate(this.direction);
					}
					else if(this.world.isAir(posX+1,posY) && this.world.isBlocked(posX+1, posY) && this.world.isAir(posX-1,posY) && !this.world.isBlocked(posX-1, posY)) //he changes his direction if there is a blocker on the next block without loosing his power
					{
						this.direction=Direction.LEFT;
						translate(this.direction);
					}
					else if(this.world.getmap()[posX+1][posY].getBlockType() != 4) //else he breaks the block on the right, only if this block is breakable
					{
						this.world.getmap()[posX+1][posY].decreaseBlockType();
						alreadyDig = true;
					}
					else if(this.world.getmap()[posX+1][posY].getBlockType() == 4) //else he loose his power, if the block is unbreakable
					{
						super.changeClass(0); //he becomes a walker
					}
				}
				else if(this.direction==Direction.LEFT)
				{
					if(posX==0) //if he is at the left border of the map
					{
						remove();
					}
					else if(this.world.isAir(posX-1,posY) && !this.world.isBlocked(posX-1, posY)) //if he can go right, he does
					{
						if(posX-1!=0 && this.world.isAir(posX-2,posY) && alreadyDig)
						{
							super.changeClass(0);
						}
						super.translate(this.direction);
					}
					else if(this.world.isAir(posX-1,posY) && this.world.isBlocked(posX-1, posY) && this.world.isAir(posX+1,posY) && !this.world.isBlocked(posX+1, posY)) //he changes his direction if there is a blocker on the next block without loosing his power
					{
						this.direction=Direction.RIGHT;
						translate(this.direction);
					}
					else if(this.world.getmap()[posX-1][posY].getBlockType() != 4) //else he breaks the block on the right, only if this block is breakable
					{
						this.world.getmap()[posX-1][posY].decreaseBlockType();
						alreadyDig = true;
					}
					else if(this.world.getmap()[posX-1][posY].getBlockType() == 4) //else he loose his power, if the block is unbreakable
					{
						super.changeClass(0); //he becomes a walker
					}
				}
			}
		}
	}
}
package model;

public class Exploder extends Student
{
	private int decount; //At 5 at the begin, the exploder explodes when at 0
	
	public Exploder(int x, int y, World world, Direction dir,int f)
	{
		super(x,y,world,dir,5,f);
		this.decount=5;
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
			super.move();
			this.decount--;
			if(decount==0)
				explode();
		}
	}
	
	/*
	 * This function make the exploder explode: he then breaks all the nearby blocks
	 */
	public void explode()
	{
		if(posY!=World.NB_CELL_HEIGHT-1 && (this.world.getmap()[posX][posY+1].getBlockType() == 3 || this.world.getmap()[posX][posY+1].getBlockType() == 2 || this.world.getmap()[posX][posY+1].getBlockType() == 1))
		{
			this.world.destroyBlock(posX,posY+1);
		}
		if(posY!=0 && (this.world.getmap()[posX][posY-1].getBlockType() == 3 || this.world.getmap()[posX][posY-1].getBlockType() == 2 || this.world.getmap()[posX][posY-1].getBlockType() == 1))
		{
			this.world.destroyBlock(posX,posY-1);
		}
		if(posX!=World.NB_CELL_WIDTH-1 && (this.world.getmap()[posX+1][posY].getBlockType() == 3 || this.world.getmap()[posX+1][posY].getBlockType() == 2 || this.world.getmap()[posX+1][posY].getBlockType() == 1))
		{
			this.world.destroyBlock(posX+1,posY);
		}
		if(posX!=0 && (this.world.getmap()[posX-1][posY].getBlockType() == 3 || this.world.getmap()[posX-1][posY].getBlockType() == 2 || this.world.getmap()[posX-1][posY].getBlockType() == 1))
		{
			this.world.destroyBlock(posX-1,posY);
		}
		this.remove();
	}
}
package model;

/*
 * Attributes:
 * @world: the world on which the student is
 * @direction: indicate the direction where the student goes
 * @posX/posY: store the current position of the student
 * @fallHeight: if the students is falling, store the height of the fall, 0 otherwise
 * @classType: used in other methods to know the type of the Student
 * 
 * Methods:
 * @move: make the student move depending of his class and his direction
 * @remove: remove the student from the map
 * @changeClass: change the class/type of the student
 * @translate: used to increase/decrease the student's x/y depending of the direction, used in move()
 */
public abstract class Student {
	//Attributes
	public static int DEADLY_FALL_HEIGHT=5;
	protected World world;
	protected Direction direction;
	protected int posX;
	protected int posY;
	protected int fallHeight;
	protected int classType;
	
	//Constructor
	public Student(int x, int y, World w, Direction dir, int c, int f)
	{
		this.posX = x;
		this.posY = y;
		this.fallHeight = f;
		this.direction = dir;
		this.world = w;
		this.classType = c;
	}
	
	//Getters
	public World getWorld()
	{
		return this.world;
	}
	
	public int getX()
	{
		return this.posX;
	}
	
	public int getY()
	{
		return this.posY;
	}
	
	public Direction getDirection()
	{
		return this.direction;
	}
	
	public int getFallHeight()
	{
		return this.fallHeight;
	}
	
    public int getClassType()
    {
        return this.classType;
    }
	
	//Setters
	public void setX(int x)
	{
		this.posX=x;
	}
	
	public void setY(int y)
	{
		this.posY=y;
	}
	
	public void setDirection(Direction d)
	{
		this.direction=d;
	}
	
	public void setFallHeight(int f)
	{
		this.fallHeight=f;
	}
	
	//Other methods
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
	
	public void translate(Direction dir)
	{
		if(dir==Direction.RIGHT)
		{
			posX++;
		}
		else if(dir==Direction.LEFT)
		{
			posX--;
		}
		else if(dir==Direction.DOWN)
		{
			posY++;
			fallHeight++;
		}
		else
		{
			posY--;
		}
	}
	
	public void remove()
	{
		this.world.removeFromWholeGame(posX, posY, this);
	}
	
	public void changeClass(int type)
	{
		Student temp;
		switch(type)
		{
			case 1: 	temp = new Basher(posX,posY,world,direction,this.fallHeight);
						break;
			case 2: 	temp = new Blocker(posX,posY,world,direction,this.fallHeight);
						break;
			case 3: 	temp = new Climber(posX,posY,world,direction,this.fallHeight);
						break;
			case 4: 	temp = new Digger(posX,posY,world,direction,this.fallHeight);
						break;
			case 5: 	temp = new Exploder(posX,posY,world,direction,this.fallHeight);
						break;
			case 6: 	temp = new Floater(posX,posY,world,direction,this.fallHeight);
						break;
			default : 	temp = new Walker(posX,posY,world,direction,this.fallHeight);
		}
		world.addInGame(posX, posY, temp);
		world.removeFromWholeGame(posX, posY, this);
	}
	
}
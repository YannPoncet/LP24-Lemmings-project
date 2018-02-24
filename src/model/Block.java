package model;

/*
 * Attributes:
 * @blockType: store the type of the block :
 * 4 for an unbreakable block
 * 3 for a complete block, breakable
 * 2 for an 1/3 destroyed block
 * 1 for a 2/3 destroyed block
 * 0 for air
 * @blocked: indicate if the students can move on the block
 * 
 * Methods:
 *
 */
public class Block {
	//Attributes
	private int blockType;
	private boolean blocked;
	
	//Constructor
	public Block(int blockType)
	{
		this.blockType = blockType;
		this.blocked = false;	//tells if the block contains a Student Blocker
	}
	
	//Getters
	
	public int getBlockType()
	{
		return this.blockType;
	}
	
	public boolean isBlocked()
	{
		return blocked;
	}
	
	public void setBlockType(int type)
	{
		this.blockType=type;
	}
	
	public void setBlocked(boolean blocked)
	{
		this.blocked=blocked;
	}
	
	public void decreaseBlockType() //Used to "dig" blocks in 3 different steps
	{
		this.blockType--;
	}
}

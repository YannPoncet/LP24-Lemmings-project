package model;

/* The walker is the normal Student, keeps walking and bouncing while he can*/
public class Walker extends Student
{
	public Walker(int x, int y, World world, Direction dir,int f)
	{
		super(x,y,world,dir,0,f);
	}
}
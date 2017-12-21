package rbadia.voidspace.model;

public class Boss extends GameObject {
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED = 4;
	
	public static final int WIDTH =64;
	public static final int HEIGHT = 64;
	
	public Boss(int xPos, int yPos) {
		super(xPos, yPos, Asteroid.WIDTH, Asteroid.HEIGHT);
		this.setSpeed(DEFAULT_SPEED);
	}

	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}
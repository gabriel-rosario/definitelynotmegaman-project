package rbadia.voidspace.model;

/**
 * Represents a bullet fired by a ship.
 */
public class Fireball extends GameObject {
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED = -1;
	public static final  int WIDTH = 20;
	public static final int HEIGHT = 100;
	
	public Fireball(int xPos, int yPos) {
		super(xPos, yPos, WIDTH, HEIGHT);
		this.setSpeed(6);
	}
	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}

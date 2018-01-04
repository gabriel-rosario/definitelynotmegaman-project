package rbadia.voidspace.model;

public class Heart extends GameObject {
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED = 4;
	
	public static final int WIDTH = 20;
	public static final int HEIGHT = 18;
	
	public Heart(int xPos, int yPos) {
		super(xPos, yPos, Heart.WIDTH, Heart.HEIGHT);
		this.setSpeed(DEFAULT_SPEED);
	}

	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}

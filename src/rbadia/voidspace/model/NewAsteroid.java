package rbadia.voidspace.model;

import java.util.Random;

public class NewAsteroid extends Asteroid{

	/**
	 * 
	 */
private static final long serialVersionUID = -3661835329252582014L;
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	
	Random random = new Random();
	int asteroidSpeed = random.nextInt();
	
	
	public NewAsteroid(int xPos, int yPos) {
		super(xPos, yPos);
		this.setSpeed(this.asteroidSpeed);
	}

	public int getDefaultSpeed(){
		return this.asteroidSpeed;
	}
}


package rbadia.voidspace.main;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.NewAsteroid;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level3State extends Level2State{
	
	protected NewAsteroid asteroid;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6056436154088977036L;

	public Level3State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
	}
	
	public Asteroid newAsteroid(Level3State screen){
		int xPos = (int) (screen.getWidth() - Asteroid.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - Asteroid.HEIGHT- 32));
		asteroid = new NewAsteroid(xPos, yPos);
		return asteroid;
	}
	
	
	
	//Plataformas
	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i<4)	platforms[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			if(i==4) platforms[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			if(i>4){	
				int k=4;
				platforms[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40 );
				k=k+2;
			}
		}
		return platforms;
	}
}

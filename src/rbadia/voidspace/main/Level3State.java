package rbadia.voidspace.main;

import java.awt.Graphics2D;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;


public class Level3State extends Level2State{
		
	private static final long serialVersionUID = 6056436154088977036L;

	public Level3State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
	}	
	
	int position = rand.nextInt();
	
	@Override
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();
		
		switch(position % 2) {
		case 0:
			if((asteroid.getX() + asteroid.getPixelsWide() >  0)) {
				
				//CHANGE THIS FOR ANGLE OF DESCENT, make this random
				asteroid.translate(-asteroid.getSpeed(), asteroid.getSpeed());
				
				getGraphicsManager().drawAsteroid(asteroid, g2d, this);	
			}
			else {
				//change delay thing for multiple asteroids
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
					
					//Set Asteroid to the right of the screen, random Y position. make x random
					asteroid.setLocation(this.getWidth() - asteroid.getPixelsWide(),
							rand.nextInt(this.getHeight() - asteroid.getPixelsTall() - 32));
				}
				else {
					// draw explosion
					getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
				}
			}	
			break;
		case 1:
			if((asteroid.getX() + asteroid.getPixelsWide() < this.getWidth())) {
				
				//CHANGE THIS FOR ANGLE OF DESCENT, make this random
				asteroid.translate(asteroid.getSpeed(), asteroid.getSpeed());
				
				getGraphicsManager().drawAsteroid(asteroid, g2d, this);	
			}
			else {
				//change delay thing for multiple asteroids
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
					
					//Set Asteroid to the right of the screen, random Y position. make x random
					asteroid.setLocation(0 + asteroid.getPixelsWide(),
							rand.nextInt(this.getHeight() - asteroid.getPixelsTall() - 32));
				}
				else {
					// draw explosion
					getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
				}
			}
			break;
		}
		
		
	}

	
	//Plataformas
	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i<n)	platforms[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);			
		}
		return platforms;
	}
}

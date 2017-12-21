package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;


public class Level3State extends Level2StateNew{

	private static final long serialVersionUID = 6056436154088977036L;
	private BufferedImage level3Background;
	public Level3State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		
		try {

			this.level3Background = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level4.jpg"));


		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"VoidSpace - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public void updateScreen(){
		Graphics2D g2d = getGraphics2D();
		GameStatus status = this.getGameStatus();

		// save original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		clearScreen();
		g2d.drawImage(level3Background,null,0,0);
		drawStars(50);
		drawFloor();
		drawPlatforms();
		drawMegaMan();
		drawAsteroid();
		drawBullets();
		drawBigBullets();
		checkBullletAsteroidCollisions();
		checkBigBulletAsteroidCollisions();
		checkMegaManAsteroidCollisions();
		checkAsteroidFloorCollisions();

		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));


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

package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;


public class Level3State extends Level2StateNew{

	private static final long serialVersionUID = 6056436154088977036L;

	private BufferedImage level3Background;
	protected int levelAsteroidsDestroyed = 0;
	protected int randomPosition;
	protected int randomPosition2;
	protected Asteroid asteroid2;
	protected long asteroid2Time;
	
	public Asteroid getAsteroid2() 				{ return asteroid2; 		}



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
	
	@Override
	public void doStart() {	
		super.doStart();
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
		newAsteroid2(this);
	}
	
	@Override
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
		drawAsteroid2();
		drawBullets();
		drawBigBullets();
		drawLeftBullets();
		checkBullletAsteroidCollisions();
		checkBigBulletAsteroidCollisions();
		checkLeftBulletAsteroidCollisions();
		checkMegaManAsteroidCollisions();
		checkAsteroidFloorCollisions();
		checkBullletAsteroid2Collisions();
		checkBigBulletAsteroid2Collisions();
		checkMegaManAsteroid2Collisions();
		checkAsteroid2FloorCollisions();

		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));


	}
	
	@Override
	protected void checkBullletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroid.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				levelAsteroidsDestroyed++;
				randomPosition = rand.nextInt(10);
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}
	
	@Override
	protected void checkBigBulletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(asteroid.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				randomPosition = rand.nextInt(10);
				damage=0;
			}
		}
	}
	
	@Override
	protected void checkMegaManAsteroidCollisions() {
		GameStatus status = getGameStatus();
		if(asteroid.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroid(asteroid);
			randomPosition = rand.nextInt(10);
		}
	}
	
	protected void checkAsteroidFloorCollisions() {
		for(int i=0; i<9; i++){
			if(asteroid.intersects(floor[i])){
				removeAsteroid(asteroid);
				randomPosition = rand.nextInt(10);

			}
		}
	}



	@Override
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();

		switch(randomPosition % 2) {
		case 0:
			if((asteroid.getX() + asteroid.getPixelsWide() >  0)) {

				//CHANGE THIS FOR ANGLE OF DESCENT, make this random
				asteroid.translate(rand.nextInt(3)* -asteroid.getSpeed(), rand.nextInt(3)*asteroid.getSpeed());

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
				asteroid.translate(rand.nextInt(3)*asteroid.getSpeed(), rand.nextInt(3)*asteroid.getSpeed());

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
	
	private Asteroid newAsteroid2(Level1StateNew screen) {
		int xPos = (int) (screen.getWidth() - Asteroid.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - Asteroid.HEIGHT- 32));
		asteroid2 = new Asteroid(xPos, yPos);
		return asteroid2;		
	}
	
	protected void checkAsteroid2FloorCollisions() {
		for(int i=0; i<9; i++){
			if(asteroid2.intersects(floor[i])){
				removeAsteroid2(asteroid2);
				randomPosition2 = rand.nextInt(10);

			}
		}
	}
	
	protected void drawAsteroid2() {
		Graphics2D g2d = getGraphics2D();

		switch(randomPosition2 % 2) {
		case 0:
			if((asteroid2.getX() + asteroid2.getPixelsWide() >  0)) {

				//CHANGE THIS FOR ANGLE OF DESCENT, make this random
				asteroid2.translate(rand.nextInt(3)* -asteroid2.getSpeed(), rand.nextInt(3)*asteroid2.getSpeed());

				getGraphicsManager().drawAsteroid(asteroid2, g2d, this);	
			}
			else {
				//change delay thing for multiple asteroids
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){

					//Set Asteroid to the right of the screen, random Y position. make x random
					asteroid2.setLocation(this.getWidth() - asteroid2.getPixelsWide(),
							rand.nextInt(this.getHeight() - asteroid2.getPixelsTall() - 32));
				}
				else {
					// draw explosion
					getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
				}
			}	
			break;
		case 1:
			if((asteroid2.getX() + asteroid2.getPixelsWide() < this.getWidth())) {

				//CHANGE THIS FOR ANGLE OF DESCENT, make this random
				asteroid2.translate(rand.nextInt(3)*asteroid2.getSpeed(), rand.nextInt(3)*asteroid2.getSpeed());

				getGraphicsManager().drawAsteroid(asteroid2, g2d, this);	
			}
			else {
				//change delay thing for multiple asteroids
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){

					//Set Asteroid to the right of the screen, random Y position. make x random
					asteroid2.setLocation(0 + asteroid2.getPixelsWide(),
							rand.nextInt(this.getHeight() - asteroid2.getPixelsTall() - 32));
				}
				else {
					// draw explosion
					getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
				}
			}
			break;
		}
	}

	private void removeAsteroid2(Asteroid asteroid22) {
		// "remove" asteroid
				asteroidExplosion = new Rectangle(
						asteroid2.x,
						asteroid2.y,
						asteroid2.getPixelsWide(),
						asteroid2.getPixelsTall());
				asteroid2.setLocation(-asteroid2.getPixelsWide(), -asteroid2.getPixelsTall());
				this.getGameStatus().setNewAsteroid(true);
				asteroid2Time = System.currentTimeMillis();
				// play asteroid explosion sound
				this.getSoundManager().playAsteroidExplosionSound();
	}


	protected void checkMegaManAsteroid2Collisions() {
		GameStatus status = getGameStatus();
		if(asteroid2.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroid(asteroid2);
			randomPosition2 = rand.nextInt(10);

		}
	}

	protected void checkBigBulletAsteroid2Collisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(asteroid2.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				randomPosition2 = rand.nextInt(10);
				damage=0;
			}
		}
	}

	protected void checkBullletAsteroid2Collisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroid2.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				randomPosition2 = rand.nextInt(10);
				break;
			}
		}
	}
	
	//Skip level
	@Override
	public boolean isLevelWon() {
		if (getInputHandler().isNPressed()) {
			MegaManMain.audioClip.stop();
			return true;
		}
		return levelAsteroidsDestroyed >= 5;
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

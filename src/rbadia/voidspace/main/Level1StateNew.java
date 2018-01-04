package rbadia.voidspace.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.BossBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.LeftBigBullet;
import rbadia.voidspace.model.LeftBullet;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.sounds.SoundManager;

public class Level1StateNew extends Level1State {

	private BufferedImage leftBulletImg;
	private BufferedImage megaFireLImg;
	private BufferedImage megaManLImg;
	private BufferedImage megaFallLImg;
	private BufferedImage background;
	private BufferedImage leftBigBulletImg;
	
	protected LeftBigBullet leftBigBullet;
	protected ArrayList<LeftBigBullet> leftBigBullets;
	protected LeftBullet leftBullet;
	protected ArrayList<LeftBullet> leftBullets;

	protected boolean megamanFacingRight = true;
	protected boolean megamanFacingLeft = true;

	public Level1StateNew(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic,
			InputHandler inputHandler, GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		leftBullets = new ArrayList<LeftBullet>();
		leftBigBullets = new ArrayList<LeftBigBullet>();


		try {
			this.megaManLImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaMan3Left.png"));
			this.megaFallLImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFallLeft.png"));
			this.megaFireLImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireLeft.png"));
			this.background = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/background.jpg"));
			this.leftBulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bullet.png"));
			this.leftBigBulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigBullet.png"));


		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"VoidSpace - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	//getters

	public ArrayList<LeftBullet> getLeftBullets()		{ return leftBullets;   	}
	public ArrayList<LeftBigBullet> getLeftBigBullets() {return leftBigBullets;}


	public void drawMegaManL (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaManLImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawMegaFallL (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFallLImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawMegaFireL (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFireLImg, megaMan.x, megaMan.y, observer);
	}
	
	@Override
	public void doGettingReady() {
		setCurrentState(GETTING_READY);
		getGameLogic().drawGetReady();
		repaint();
		LevelLogic.delay(2000);
		//Music starts
		MegaManMain.audioClip.close();
		MegaManMain.audioFile = new File("audio/MegaManMain.wav");
		try {
			MegaManMain.audioStream = AudioSystem.getAudioInputStream(MegaManMain.audioFile);
			MegaManMain.audioClip.open(MegaManMain.audioStream);
			MegaManMain.audioClip.start();
			MegaManMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void doStart() {	

		setStartState(START_STATE);
		setCurrentState(getStartState());
		// init game variables
		bullets = new ArrayList<Bullet>();
		bigBullets = new ArrayList<BigBullet>();


		//numPlatforms = new Platform[5];

		GameStatus status = this.getGameStatus();

		status.setGameOver(false);
		status.setNewAsteroid(false);

		// init the life and the asteroid
		newMegaMan();
		newFloor(this, 9);
		newPlatforms(getNumPlatforms());
		newAsteroid(this);

		lastAsteroidTime = -NEW_ASTEROID_DELAY;
		lastLifeTime = -NEW_MEGAMAN_DELAY;

		bigFont = originalFont;
		biggestFont = null;

		// Display initial values for scores
		getMainFrame().getDestroyedValueLabel().setForeground(Color.BLACK);
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));

	}

	@Override
	protected void drawMegaMan() {
		//draw one of three possible MegaMan poses according to situation
		Graphics2D g2d = getGraphics2D();
		GameStatus status = getGameStatus();
		if(!status.isNewMegaMan()){
			if((Gravity() == true) || ((Gravity() == true) && (Fire() == true || Fire2() == true))){
				if(megamanFacingRight) {
					getGraphicsManager().drawMegaFallR(megaMan, g2d, this);
				}else {
					this.drawMegaFallL(megaMan, g2d, this);
				}
			}
		}

		if((Fire() == true || Fire2()== true) && (Gravity()==false)){
			getGraphicsManager().drawMegaFireR(megaMan, g2d, this);
		}

		if((FireLeft() == true || Fire2Left()== true) && (Gravity()==false)){
			this.drawMegaFireL(megaMan, g2d, this);
		}

		if((Gravity()==false) && (Fire()==false) && (Fire2()==false)&&(FireLeft()==false)&&(Fire2Left()==false)){
			if(megamanFacingRight) {
				getGraphicsManager().drawMegaMan(megaMan, g2d, this);
			}else {
				this.drawMegaManL(megaMan, g2d, this);
			}
		}
	}

	@Override
	//Bullet fire pose
	protected boolean Fire(){
		MegaMan megaMan = this.getMegaMan();
		List<Bullet> bullets = this.getBullets();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);

			if((bullet.getX() > megaMan.getX() + megaMan.getWidth()) && 
					(bullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60)){
				return true;
			}
		}
		return false;
	}
	protected boolean FireLeft(){
		MegaMan megaMan = this.getMegaMan();
		List<LeftBullet> leftBullets = this.getLeftBullets();
		for(int i=0; i<leftBullets.size(); i++){
			LeftBullet leftBullet = leftBullets.get(i);

			if((leftBullet.getX() < megaMan.getX()) && (leftBullet.getX() >= megaMan.getX() - 60)){
				return true;
			}
		}
		return false;
	}


	//BigBullet fire pose
	@Override
	protected boolean Fire2(){
		MegaMan megaMan = this.getMegaMan();
		List<BigBullet> bigBullets = this.getBigBullets();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if((bigBullet.getX() > megaMan.getX() + megaMan.getWidth()) && (bigBullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60)){
				return true;
			}
		}
		return false;
	}
	
	protected boolean Fire2Left() {
		MegaMan megaMan = this.getMegaMan();
		List<LeftBigBullet> leftBigBullets = this.getLeftBigBullets();
		for(int i=0; i<leftBigBullets.size(); i++){
			LeftBigBullet leftBigBullet = leftBigBullets.get(i);
			if((leftBigBullet.getX() < megaMan.getX()) && (leftBigBullet.getX() >= megaMan.getX() - 60)){
				return true;
			}
		}
		return false;	}

	@Override
	public void fireBullet(){
		if(megamanFacingRight) {
			Bullet bullet = new Bullet(megaMan.x + megaMan.width - Bullet.WIDTH/2,
					megaMan.y + megaMan.width/2 - Bullet.HEIGHT +2);
			bullets.add(bullet);
			this.getSoundManager().playBulletSound();
		}else {
			LeftBullet leftBullet = new LeftBullet(megaMan.x + LeftBullet.WIDTH/2,
					megaMan.y + megaMan.width/2 - LeftBullet.HEIGHT +2);
			leftBullets.add(leftBullet);
			this.getSoundManager().playBulletSound();
		}
	}

	/**
	 * Fire the "Power Shot" bullet
	 */
	@Override
	public void fireBigBullet(){
		if(megamanFacingRight) {
			//BigBullet bigBullet = new BigBullet(megaMan);
			int xPos = megaMan.x + megaMan.width - BigBullet.WIDTH / 2;
			int yPos = megaMan.y + megaMan.width/2 - BigBullet.HEIGHT + 4;
			BigBullet  bigBullet = new BigBullet(xPos, yPos);
			bigBullets.add(bigBullet);
			this.getSoundManager().playBulletSound();
		}else {
			//BigBullet bigBullet = new BigBullet(megaMan);
			int xPos = megaMan.x + BigBullet.WIDTH / 2;
			int yPos = megaMan.y + megaMan.width/2 - BigBullet.HEIGHT + 4;
			
			LeftBigBullet  leftBigBullet = new LeftBigBullet(xPos, yPos);
			leftBigBullets.add(leftBigBullet);
			this.getSoundManager().playBulletSound();
		}

	}
	@Override
	public boolean moveBullet(Bullet bullet){

		if(bullet.getY() - bullet.getSpeed() >= 0){
			bullet.translate(bullet.getSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
	}

	public boolean moveBulletLeft(LeftBullet leftBullet){

		if(leftBullet.getY() - leftBullet.getSpeed() >= 0){
			leftBullet.translate(leftBullet.getSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
	}



	/** Move a "Power Shot" bullet once fired.
	 * @param bigBullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	@Override
	public boolean moveBigBullet(BigBullet bigBullet){
			if(bigBullet.getY() - bigBullet.getSpeed() >= 0){
				bigBullet.translate(bigBullet.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
	}
	
	public boolean moveBigBulletLeft(LeftBigBullet leftBigBullet) {
		if(leftBigBullet.getY() - leftBigBullet.getSpeed() >= 0){
			leftBigBullet.translate(leftBigBullet.getSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
	}

	@Override
	public void moveMegaManLeft(){
		if(megaMan.getX() - megaMan.getSpeed() >= 0){
			megamanFacingRight = false;
			megaMan.translate(-megaMan.getSpeed(), 0);
		}
	}

	@Override
	public void moveMegaManRight(){
		if(megaMan.getX() + megaMan.getSpeed() + megaMan.width < getWidth()){
			megamanFacingRight = true;
			megaMan.translate(megaMan.getSpeed(), 0);
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
		g2d.drawImage(background,null,0,0);
		drawStars(50);
		drawFloor();
		drawPlatforms();
		drawMegaMan();
		drawAsteroid();
		drawBullets();
		drawLeftBullets();
		drawBigBullets();
		drawLeftBigBullets();
		checkBullletAsteroidCollisions();
		checkLeftBulletAsteroidCollisions();
		checkBigBulletAsteroidCollisions();
		checkLeftBigBulletAsteroidCollisions();
		checkMegaManAsteroidCollisions();
		checkAsteroidFloorCollisions();

		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));


	}
	public void drawLeftBullet(LeftBullet leftBullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(leftBulletImg, leftBullet.x, leftBullet.y, observer);
	}
	protected void drawLeftBullets() {
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<leftBullets.size(); i++){
			LeftBullet leftBullet = leftBullets.get(i);
			drawLeftBullet(leftBullet, g2d, this);

			boolean remove =   this.moveBulletLeft(leftBullet);
			if(remove){
				leftBullets.remove(i);
				i--;
			}
		}
	}
	
	public void drawLeftBigBullet(LeftBigBullet leftBigBullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(leftBigBulletImg, leftBigBullet.x, leftBigBullet.y, observer);
	}
	
	protected void drawLeftBigBullets() {
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<leftBigBullets.size(); i++){
			LeftBigBullet leftBigBullet = leftBigBullets.get(i);
			drawLeftBigBullet(leftBigBullet, g2d, this);

			boolean remove =   this.moveBigBulletLeft(leftBigBullet);
			if(remove){
				leftBigBullets.remove(i);
				i--;
			}
		}
	}
	
	
	protected void checkLeftBulletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBullets.size(); i++){
			LeftBullet leftBullet = leftBullets.get(i);
			if(asteroid.intersects(leftBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				leftBullets.remove(i);
				break;
			}
		}
	}
	
	
	protected void checkLeftBigBulletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<leftBigBullets.size(); i++){
			LeftBigBullet leftBigBullet = leftBigBullets.get(i);
			if(asteroid.intersects(leftBigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				leftBigBullets.remove(i);
				break;
			}
		}
	}

	//Skips Levels
	@Override
	public boolean isLevelWon() {
		if (getInputHandler().isNPressed()) {
			MegaManMain.audioClip.stop();
			return true;
		}
		return super.isLevelWon();
	}
	@Override
	public void doLevelWon(){
		setCurrentState(LEVEL_WON);
		getGameLogic().drawYouWin();
		MegaManMain.audioClip.stop();
		repaint();
		LevelLogic.delay(5000);
		
		
	}
	@Override
	public void doGameOver(){
		this.getGameStatus().setGameOver(true);
		MegaManMain.audioClip.stop();
	}
}

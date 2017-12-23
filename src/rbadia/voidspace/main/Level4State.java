package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.BossBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Fireball;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.NewSoundManager;
import rbadia.voidspace.sounds.SoundManager;

public class Level4State extends Level3State{

	private NewSoundManager newSoundManager;
	protected int numPlatforms=8;
	private BufferedImage level4Background;
	private BufferedImage boss;
	private BufferedImage bossBulletImage;
	private BufferedImage fireballImage;
	private BufferedImage fireExplosionImage;
	protected Rectangle fireExplosion;
	private boolean isBossGoingUp = true;
	private int bossYPos = this.getHeight()/5;
	private int bossXPos = this.getWidth()+10;
	private int bulletBossCollision = 0;
	private long lastBulletFire;
	private long lastFireball;
	private final long  NEW_BOSS_FIRE_DELAY = 10000;
	private final long  NEW_BOSS_BULLET_DELAY = 2000;
	protected BossBullet bossBullet;
	protected ArrayList<BossBullet> bossBullets;
	protected Fireball fireball;
	protected ArrayList<Fireball> fireballs;






	/**
	 * 
	 */
	private static final long serialVersionUID = 6710229566087339766L;

	public Level4State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan, NewSoundManager newSoundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);

		bossBullets = new ArrayList<BossBullet>();
		fireballs = new ArrayList<Fireball>();
		this.setNewSoundManager(newSoundMan);

		try {
			this.boss = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/BOSS.gif"));
			this.level4Background = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level3.jpg"));
			this.bossBulletImage = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigBullet.png"));
			this.fireballImage = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/fireball.png"));
			this.fireExplosionImage = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/fireExplo.png"));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"VoidSpace - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
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
		g2d.drawImage(level4Background,null,-50,-65);
		drawStars(50);
		drawFloor();
		drawBoss(g2d);
		drawPlatforms();
		drawMegaMan();
		drawAsteroid();
		drawAsteroid2();
		drawBullets();
		drawBigBullets();
		drawBossBigBullets();
		fireBossBigBullet();
		drawBossFireball();
		fireFireballs();
		checkBullletAsteroidCollisions();
		checkBossBulletMegamanCollision(g2d);
		checkFireBallMegamanCollision(g2d);
		checkBulletBossCollisons(g2d);
		checkBigBulletAsteroidCollisions();
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
	public void doGettingReady() {
		MegaManMain.audioClip.close();
		MegaManMain.audioFile = new File("audio/AlwaysWinter.wav");
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

	//getters
	public ArrayList<BossBullet> getBossBullets()		{ return bossBullets;   	}
	public ArrayList<Fireball> getFireball()		{ return fireballs;   	}
	public NewSoundManager getNewSoundManager() { return newSoundManager; }

	//Setters
	public void setNewSoundManager(NewSoundManager newSoundManager) { this.newSoundManager = newSoundManager; }




	public void drawBoss (Graphics2D g2d){
		if(bossXPos>this.getWidth()/2) {
			g2d.drawImage(boss,null, bossXPos,bossYPos);
			bossXPos-=1;
		}else {
			if(isBossGoingUp) {
				if(bossYPos>=1) {
					g2d.drawImage(boss,null, this.getWidth()/2,bossYPos);
					bossYPos-=1;
				}else {
					isBossGoingUp=false;
				}
			}else {
				if(bossYPos+boss.getHeight()-50<=this.getHeight()){
					g2d.drawImage(boss,null, this.getWidth()/2,bossYPos);
					bossYPos+=1;
				}else {
					isBossGoingUp =true ;
				}
			}
		}
	}

	//BULLETS

	public void drawBossBullet(BossBullet bossBullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bossBulletImage, bossBullet.x, bossBullet.y, observer);
	}

	protected void drawBossBigBullets() {
		Graphics2D g2d = getGraphics2D();

		for(int i=0; i<bossBullets.size(); i++){
			BossBullet bossBullet = bossBullets.get(i);
			this.drawBossBullet(bossBullet, g2d,null);

			boolean remove = this.moveBossBullet(bossBullet);
			if(remove){
				bossBullets.remove(i);
				i--;
			}
		}
	}

	public boolean moveBossBullet(BossBullet bossBullet){

		if(bossBullet.getY() - bossBullet.getSpeed() >= 0)
		{
			bossBullet.translate(-(bossBullet.getSpeed()), 0);
			return false;
		}
		else{
			return true;
		}
	}


	public void fireBossBigBullet() {
		long currentTime = System.currentTimeMillis();
		if(bossXPos==this.getWidth()/2) {
			if((currentTime - lastBulletFire) > NEW_BOSS_BULLET_DELAY) {
				int xPos = this.getWidth()*4/5 - BossBullet.WIDTH / 2;
				int yPos = (bossYPos+boss.getHeight())/2- BossBullet.HEIGHT + 4;
				BossBullet  bossBullet = new BossBullet(xPos, yPos);
				bossBullets.add(bossBullet);
				this.getSoundManager().playBulletSound();
				lastBulletFire = System.currentTimeMillis();
			}
		}

	}

	protected void checkBulletBossCollisons(Graphics2D g2d) {

		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if((bullet.x+bullet.width/2>=this.getWidth()*4/5)&&((bullet.y+bullet.height/2>=bossYPos)||(bullet.y+bullet.height/2<=bossYPos+boss.getHeight()))){
				Rectangle bulletExplosion = new Rectangle(
						bullet.x,
						bullet.y,
						bullet.getPixelsWide(),
						bullet.getPixelsTall());
				getGraphicsManager().drawAsteroidExplosion(bulletExplosion,g2d, this);
				// increase Boss hit count
				bulletBossCollision++;
				randomPosition = rand.nextInt(10);
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}

	protected void checkBossBulletMegamanCollision(Graphics2D g2d) {

		GameStatus status = getGameStatus();
		for(int i=0; i<bossBullets.size(); i++){
			BossBullet bossBullet = bossBullets.get(i);
			if(megaMan.intersects(bossBullet)){
				Rectangle bulletExplosion = new Rectangle(
						bossBullet.x,
						bossBullet.y,
						bossBullet.getPixelsWide(),
						bossBullet.getPixelsTall());
				getGraphicsManager().drawAsteroidExplosion(bulletExplosion,g2d, this);
				// increase Boss hit count
				status.setLivesLeft(status.getLivesLeft() - 1);
				//randomPosition = rand.nextInt(10);
				damage=0;
				// remove bullet
				bossBullets.remove(i);
				break;
			}
		}
	}


	//FIREBALL
	public void drawFireball(Fireball fireball, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(fireballImage, fireball.x, fireball.y, observer);
	}

	protected void drawBossFireball() {
		Graphics2D g2d = getGraphics2D();

		for(int i=0; i<fireballs.size(); i++){
			Fireball fireball = fireballs.get(i);
			this.drawFireball(fireball, g2d,null);

			boolean remove = this.moveFireball(fireball);
			if(remove){
				fireballs.remove(i);
				i--;
			}
		}
	}

	public void fireFireballs() {

		long currentTime = System.currentTimeMillis();
		if(bossXPos==this.getWidth()/2) {
			if((currentTime - lastFireball) > NEW_BOSS_FIRE_DELAY) {

				int xPos = this.getWidth()*4/5 - Fireball.WIDTH / 2;
				int yPos = (bossYPos+boss.getHeight())/2- BossBullet.HEIGHT + 4;
				Fireball  fireball = new Fireball(xPos, yPos);
				fireballs.add(fireball);
				this.getNewSoundManager().playFireballSound();
				lastFireball = System.currentTimeMillis();
			}
		}
	}


	public boolean moveFireball(Fireball fireball){

		if(fireball.getY() - fireball.getSpeed() >= 0)
		{
			fireball.translate(-(fireball.getSpeed()), 0);
			return false;
		}
		else{
			return true;
		}
	}

	public void drawFireBallExplosion(Rectangle fireExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(fireExplosionImage, fireExplosion.x, fireExplosion.y, observer);
	}

	protected void checkFireBallMegamanCollision(Graphics2D g2d) {

		GameStatus status = getGameStatus();
		for(int i=0; i<fireballs.size(); i++){
			Fireball fireball = fireballs.get(i);
			if(megaMan.intersects(fireball)){
				Rectangle fireExplosion = new Rectangle(
						fireball.x,
						fireball.y,
						fireball.getPixelsWide(),
						fireball.getPixelsTall());
				drawFireBallExplosion(fireExplosion, g2d,this);
				// increase Boss hit count
				status.setLivesLeft(status.getLivesLeft() - 1);
				//randomPosition = rand.nextInt(10);
				damage=0;
				// remove bullet
				fireballs.remove(i);
				break;
			}
		}
	}






	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];

		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i<4)	platforms[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			
		}
		return platforms;
	}

	@Override
	public boolean isLevelWon() {
		if (getInputHandler().isNPressed()) {
			MegaManMain.audioClip.stop();
			return true;
		}
		return bulletBossCollision >= 100;

	}

}

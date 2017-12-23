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
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level4State extends Level3State{

	protected int numPlatforms=8;
	private BufferedImage level4Background;
	private BufferedImage boss;
	private BufferedImage bossBulletImage;
	private boolean isBossGoingUp = true;
	private int bossYPos = this.getHeight()/5;
	private int bulletBossCollision = 0;
	private long lastBulletFire;
	private final long  NEW_BOSS_BULLET_DELAY = 3000;
	protected BossBullet bossBullet;
	protected ArrayList<BossBullet> bossBullets;
	
	
	

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6710229566087339766L;

	public Level4State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		
		bossBullets = new ArrayList<BossBullet>();
		 
		try {
			this.boss = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/BOSS.gif"));
			this.level4Background = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level3.jpg"));
			this.bossBulletImage = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigBullet.png"));


		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"VoidSpace - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
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
	
	public void drawBossBullet(BossBullet bossBullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bossBulletImage, bossBullet.x, bossBullet.y, observer);
	}
	
	public void drawBoss (Graphics2D g2d){

		if(isBossGoingUp) {

			if(bossYPos>=1) {
				g2d.drawImage(boss,null, this.getWidth()/2,bossYPos);
				bossYPos-=1;

			}else {
				isBossGoingUp=false;
			}
		}
		else {
			if(bossYPos+boss.getHeight()-50<=this.getHeight())
			{
				g2d.drawImage(boss,null, this.getWidth()/2,bossYPos);
				bossYPos+=1;

			}else {
				isBossGoingUp =true ;
			}
		}
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
	protected void drawBossBigBullets2() {
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
	
	public void fireBossBigBullet() {

		long currentTime = System.currentTimeMillis();
		if((currentTime - lastBulletFire) > NEW_BOSS_BULLET_DELAY) {

			int xPos = this.getWidth()*4/5 - BossBullet.WIDTH / 2;
			int yPos = (bossYPos+boss.getHeight())/2- BossBullet.HEIGHT + 4;
			BossBullet  bossBullet = new BossBullet(xPos, yPos);
			bossBullets.add(bossBullet);
			this.getSoundManager().playBulletSound();
			lastBulletFire = System.currentTimeMillis();
		}
	}
	public void fireBossBigBullet2() {

		long currentTime = System.currentTimeMillis();
		if((currentTime - lastBulletFire) > NEW_BOSS_BULLET_DELAY) {

			int xPos = this.getWidth()*4/5 - BossBullet.WIDTH / 2;
			int yPos = (bossYPos+boss.getHeight())- BossBullet.HEIGHT + 4;
			BossBullet  bossBullet = new BossBullet(xPos, yPos);
			bossBullets.add(bossBullet);
			this.getSoundManager().playBulletSound();
			lastBulletFire = System.currentTimeMillis();
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
		drawBullets();
		drawBigBullets();
		drawBossBigBullets();
		fireBossBigBullet();
		fireBossBigBullet2();
		checkBullletAsteroidCollisions();
		checkBulletBossCollisons(g2d);
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

	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];

		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i<4)	platforms[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			if(i>3){	
				int k=7;
				platforms[i].setLocation(50 + (k-i)*50, getHeight()/2 + 140 - i*40 );
				k=k+2;
			}
		}
		return platforms;
	}

	@Override
	public boolean isLevelWon() {
		if (getInputHandler().isNPressed()) {
			MegaManMain.audioClip.stop();
			return true;
		}
		return bulletBossCollision >= 40;

	}

}

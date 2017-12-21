package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level4State extends Level3State{
	
	protected int numPlatforms=8;

	private BufferedImage BossImg;
	protected Boss boss;
	
	public Boss getBoss() 					{ return boss; 		}



	/**
	 * 
	 */
	private static final long serialVersionUID = 6710229566087339766L;

	public Level4State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		// TODO Auto-generated constructor stub
		try {
			this.BossImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/BigAsteroid.png"));
			

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
		drawStars(50);
		drawFloor();
		drawPlatforms();
		drawMegaMan();
		//moveBoss();
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
	
	public void drawBoss (Boss boss, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(BossImg, boss.x, boss.y, observer);	
	}
	
	
//	public void moveBoss () {
//		Graphics2D g2d = getGraphics2D();
//		GameStatus status = getGameStatus();
//		drawBoss(boss, g2d, this);
//	}
		//boolean bossGoingUp = true;
//		if(bossGoingUp) {
//			if(boss.getY()+10 > this.getHeight()) {
//				boss.translate(0, boss.getDefaultSpeed());
//			}else {
//				bossGoingUp = false;
//			}
//		}else {
//			if(boss.getY()+boss.height+10<this.getHeight()) {
//				boss.translate(0, -boss.getDefaultSpeed());
//			}
//		}
	//}

	
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

}

package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.sounds.SoundManager;

public class Level1StateNew extends Level1State {
	
	private BufferedImage megaFireLImg;
	private BufferedImage megaManLImg;
	private BufferedImage megaFallLImg;
	
	protected boolean megamanFacingRight = true;

	public Level1StateNew(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic,
			InputHandler inputHandler, GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		// TODO Auto-generated constructor stub
		try {
			this.megaManLImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaMan3Left.png"));
			this.megaFallLImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFallLeft.png"));
			this.megaFireLImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireLeft.png"));
			

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"VoidSpace - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
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
			if(megamanFacingRight) {
				getGraphicsManager().drawMegaFireR(megaMan, g2d, this);
			}else {
				this.drawMegaFireL(megaMan, g2d, this);
			}
		}

		if((Gravity()==false) && (Fire()==false) && (Fire2()==false)){
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
					(bullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60) || (bullet.getX() < megaMan.getX()) && 
					(bullet.getX() >= megaMan.getX() - 60)){
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
			if((bigBullet.getX() > megaMan.getX() + megaMan.getWidth()) && 
					(bigBullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60) || (bigBullet.getX() < megaMan.getX()) && 
					(bigBullet.getX() >= megaMan.getX() - 60)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void fireBullet(){
		if(megamanFacingRight) {
		Bullet bullet = new Bullet(megaMan.x + megaMan.width - Bullet.WIDTH/2,
				megaMan.y + megaMan.width/2 - Bullet.HEIGHT +2);
		bullets.add(bullet);
		this.getSoundManager().playBulletSound();
		}else {
			Bullet bullet = new Bullet(megaMan.x + Bullet.WIDTH/2,
					megaMan.y + megaMan.width/2 - Bullet.HEIGHT +2);
			bullets.add(bullet);
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
			BigBullet  bigBullet = new BigBullet(xPos, yPos);
			bigBullets.add(bigBullet);
			this.getSoundManager().playBulletSound();
		}
	
	}
	
	@Override
	public boolean moveBullet(Bullet bullet){
		if(megamanFacingRight) {
			if(bullet.getY() - bullet.getSpeed() >= 0){
				bullet.translate(bullet.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
		}else {
			if(bullet.getY() - bullet.getSpeed() >= 0){
				bullet.translate(-bullet.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
		}

	}

	/** Move a "Power Shot" bullet once fired.
	 * @param bigBullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	@Override
	public boolean moveBigBullet(BigBullet bigBullet){
		if(megamanFacingRight) {
			if(bigBullet.getY() - bigBullet.getSpeed() >= 0){
				bigBullet.translate(bigBullet.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
		}else {
			if(bigBullet.getY() - bigBullet.getSpeed() >= 0){
				bigBullet.translate(-bigBullet.getSpeed(), 0);
				return false;
			}
			else{
				return true;
			}
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

	@Override
	public boolean isLevelWon() {
		if (getInputHandler().isNPressed()) {
			return true;
		}
		return super.isLevelWon();
	}

}

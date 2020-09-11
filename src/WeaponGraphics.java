package weapon;

import java.awt.*;
import javax.swing.ImageIcon;

public class WeaponGraphics {
	
	private Image image;
	private String name;
	private int posX;
	private int posY;
	private boolean fire;

	//For now only use 1 gun
	private Image loadImage() {
		ImageIcon ii = new ImageIcon("res/pistol.jpg");
		Image gun1 = ii.getImage();
		Image modGun1 = gun1.getScaledInstance(5, 5, java.awt.Image.SCALE_DEFAULT);
		return modGun1;
	}

	public WeaponGraphics (String gunName, int posX, int posY) {
		this.image = loadImage();
		this.name = gunName;
		this.posX = posX;
		this.posY = posY;
		fire = false;
	}
	
	public void setPos (int newX, int newY) {
		posX = newX;
		posY = newY;
	}
	
	//if KeyEvent == VK_J
	public void fire () {
		fire = true;
	}
	
}

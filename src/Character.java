import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Character {

  private int x = 0;
  private int y = 0;
  private int w;
  private int h;
  private Image image;

  public Character(int pX, int pY) {
    x = pX;
    y = pY;
    loadImage();
  }

  private void loadImage() {

    ImageIcon ii = new ImageIcon("res/char.png");
    image = ii.getImage();

    w = image.getWidth(null);
    h = image.getHeight(null);
  }

  public int getX() {  return x;  }

  public int getY() {  return y;  }

  public int getWidth() {  return w; }

  public int getHeight() { return h; }

  public Image getImage() { return image; }

  public void keyPressed(KeyEvent e) {
    System.out.println(e.getKeyChar());
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_A) { x += -1; }
    if (key == KeyEvent.VK_D){ x +=  1; }
    if (key == KeyEvent.VK_W)   { y += -1; }
    if (key == KeyEvent.VK_S) { y += 1; }
  }

}
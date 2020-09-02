import java.awt.Image;
import javax.swing.ImageIcon;

public class Tile {
  private Image image;
  Tile(String path) {
    ImageIcon ii = new ImageIcon(path);
    image = ii.getImage();
  }
  Image getImage() { return image; }
}

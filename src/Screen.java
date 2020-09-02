import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;

public class Screen extends JPanel implements ActionListener {

  private final int PLAYER_X = 25;
  private final int PLAYER_Y = 25;
  private final int TILE     = 10;
  private Character player;
  private List<BufferedImage> tiles = new ArrayList<BufferedImage>();
  private int[][] map;
  public boolean quit;

  public Screen() {
    quit = false;
    addKeyListener(new TAdapter());
    setBackground(Color.BLACK);
    setFocusable(true);

    player = new Character(PLAYER_X, PLAYER_Y);
    loadTiles();
    initTest();
  }

  public void initTest() { map = new int[50][75]; }

  private void loadTiles() {
    try {
      tiles.add(ImageIO.read(new File("res/void.png")));
      tiles.add(ImageIO.read(new File("res/wall.png")));
      tiles.add(ImageIO.read(new File("res/door.png")));
    } catch (IOException ex) {
      System.out.println("Failed to load image: " + ex.toString());
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    doDrawing(g);
    Toolkit.getDefaultToolkit().sync();
  }

  private void doDrawing(Graphics g) {
    System.out.println("drawn");
    Graphics2D g2d = (Graphics2D) g;

    for (int y=0; y<50 ; y++) {
      for (int x=0; x<75 ; x++) {
        g2d.drawImage( tiles.get(map[y][x]),
                    x * TILE,
                    y * TILE, this);
      }
    }

    g2d.drawImage(player.getImage(), player.getX()*TILE,
            player.getY()*TILE, this);
  }

  // Only update the tiles near the player for performance.
  private void updateDrawing(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    int px = player.getX();
    int py = player.getY();
    for (int y=-1; y < 2 ; y++) {
      for (int x = -1; x < 2; x++) {
        g2d.drawImage(tiles.get(map[py+y][px+x]),
                (px+x) * TILE,
                (py+y) * TILE, this);
      }
    }
    g2d.drawImage(player.getImage(), px*TILE,
            py*TILE, this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

  private class TAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      if (key == KeyEvent.VK_ESCAPE) { quit = true; }
      player.keyPressed(e);
      updateDrawing(getGraphics());
    }
  }
}

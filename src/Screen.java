import java.awt.*;
import java.awt.Color;
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

  private Map map;
  public boolean quit;

  private final int[][] tiles = {
          {10,10,10}, {100,100,100}, {10,10,180},
  };
  private final int TILE = 20;

  public Screen() {
    quit = false;
    addKeyListener(new TAdapter());
    setBackground(Color.BLACK);
    setFocusable(true);
  }

  public void test() {
    map = new Map();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    doDrawing(g);
    Toolkit.getDefaultToolkit().sync();
  }

  private void doDrawing(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    int [][] thismap = map.getMap();
    int []   clr;
    for (int y=0; y< thismap.length ; y++) {
      for (int x=0; x<thismap[0].length ; x++) {
        clr = tiles[thismap[y][x]];
        g2d.setColor(new Color(clr[0], clr[1], clr[2]));
        g2d.fillRect(x * TILE, y * TILE, TILE, TILE);
      }
    }
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
    }
  }
}

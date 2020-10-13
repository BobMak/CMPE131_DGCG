import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Screen extends JPanel implements ActionListener {

  private MapGenerator mapGenerator;
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
    mapGenerator = new MapGenerator();
  }

  public void generate() {
    mapGenerator.generate();
  }

  public MapGenerator getMapGenerator() {
    return mapGenerator;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    doDrawing(g);
    Toolkit.getDefaultToolkit().sync();
  }

  private void doDrawing(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    int [][] thismap = mapGenerator.getMap();
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

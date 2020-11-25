import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Screen extends JPanel implements ActionListener {
  private boolean ASCIIMode;
  public boolean quit;

  private final int[][] tiles = {
    {100,100,100}, {10,10,10}, {10,10,180},
  };

  private final int[][] tilesASCIIColor = {
    {120,120,50}, {100,100,100}, {10,10,180},
  };

  private final String[] tilesASCII = {
    "#", "."
  };

  private int[][] map;

  public Screen() {
    ASCIIMode = false;
    quit = false;

    addKeyListener(new TAdapter());
    setBackground(Color.BLACK);
    setFocusable(true);
    map = new int[][]{{0,0},{0,0}};
  }

  public void toggleASCII() {
    ASCIIMode = !ASCIIMode;
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    doDrawing(g);
    Toolkit.getDefaultToolkit().sync();
  }

  public void setMap( int [][] newMap ) {
    map = newMap;
  }

  private void doDrawing(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    int []   clr;
    int currentDisplayHeight = this.getHeight();
    int currentDisplayWidth  = this.getWidth();
    float tileSize = Math.min(
      (float) currentDisplayHeight/map.length,
      (float) currentDisplayWidth/map[0].length );
    g2d.setFont(new Font("DialogInput", Font.PLAIN, (int)tileSize));
    for (int y=0; y< map.length ; y++) {
      for (int x=0; x<map[0].length ; x++) {
        if ( ASCIIMode)
          clr = tilesASCIIColor[map[y][x]];
        else
          clr = tiles[map[y][x]];
        g2d.setColor(new Color(clr[0], clr[1], clr[2]));
        if ( ASCIIMode ) {
          g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
          g2d.drawString(tilesASCII[map[y][x]],
            Math.round(x * tileSize),
            Math.round(y * tileSize)+tileSize
          );
        }
        else {
          g2d.fillRect(
            Math.round(x * tileSize),
            Math.round(y * tileSize),
            (int)Math.ceil(tileSize),
            (int)Math.ceil(tileSize));
          g2d.setColor(new Color(0,0, 0));
          g2d.drawRect(
            Math.round(x * tileSize),
            Math.round(y * tileSize),
            (int)Math.ceil(tileSize),
            (int)Math.ceil(tileSize));
        }
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

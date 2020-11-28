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
    {60,60,60}, {10,10,10}, {100,100,100},
  };

  private final int[][] tilesASCIIColor = {
    {60,60,60}, {100,100,100}, {180,180,180},
  };

  private final String[] tilesASCII = {
    "#", ".", "#"
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
    // Background
    g2d.setColor(new Color(10, 10, 10));
    g2d.fillRect(
      0,
      0,
      this.getWidth(),
      this.getHeight());
    int[][] pmap = markWalls();
    for (int y=0; y< pmap.length ; y++) {
      for (int x=0; x<pmap[0].length ; x++) {
        if ( ASCIIMode)
          clr = tilesASCIIColor[pmap[y][x]];
        else
          clr = tiles[pmap[y][x]];
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

  private int[][] markWalls() {
    int[][] withWalls = new int[map.length][map[0].length];
    for (int y=0; y< map.length ; y++)
      for (int x=0; x<map[0].length ; x++) {
        if ( map[y][x] == 0 && (get2dmax(map, x, y) != 0 ) )
          withWalls[y][x] = 2;
        else
          withWalls[y][x] = map[y][x];
      }
    return withWalls;
  }

  private int get2dmax(int[][] pmap, int px, int py) {
    int res = 0;
    for (int y=Math.max(0, py-1); y < Math.min(map.length-1, py+2 ) ; y++)
      for (int x=Math.max(0, px-1); x < Math.min(map[0].length-1, px+2) ; x++)
        if ( pmap[y][x] > res )
          res = pmap[y][x];
    return res;
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

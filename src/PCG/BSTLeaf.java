package PCG;


import java.util.Random;

public class BSTLeaf {
  int posx, posy;
  int wid,  hgt;
  BSTLeaf childLeft;
  BSTLeaf childRight;

  public BSTLeaf(int minsize, boolean splitDir, int x, int y, int w, int h) {
    // splitDir is a boolean direction of the split - true if horizontal, false if vertical
    posx = x;
    posy = y;
    wid = w;
    hgt = h;
    if ( minsize < w && minsize < h ) {
      int splitLine = (splitDir) ? w / 2 : h / 2;
      splitLine += Util.randint(-1, 1);
      assert splitLine < w;
      assert splitLine < h;
      int new_x1 = x;
      int new_y1 = y;
      int new_x2 = (splitDir) ? x + splitLine : x;
      int new_y2 = (splitDir) ? y : y + splitLine;
      int new_w1 = (splitDir) ? splitLine     : w;
      int new_w2 = (splitDir) ? w - splitLine : w;
      int new_h1 = (splitDir) ? h : splitLine;
      int new_h2 = (splitDir) ? h : h - splitLine;
      childLeft  = new BSTLeaf(minsize, !splitDir, new_x1, new_y1, new_w1, new_h1);
      childRight = new BSTLeaf(minsize, !splitDir, new_x2, new_y2, new_w2, new_h2);
    }
  }

  public void getRoom( int[][] map ) {
    if ( childLeft != null ) {
      childLeft.getRoom(map);
      childRight.getRoom(map);
    }
    else {
      int bias_x = Math.abs(Util.randint(0, 3));
      int bias_y = Math.abs(Util.randint(0, 3));
      for (int x=bias_x; x<wid-1; x++) {
        map[posy+bias_y][posx+x] = 1;
        map[posy+hgt-1][posx+x]  = 1;
      }
      for (int y=bias_y; y<hgt; y++) {
        map[posy+y][posx+bias_x] = 1;
        map[posy+y][posx+wid-1]  = 1;
      }
    }
  }
}


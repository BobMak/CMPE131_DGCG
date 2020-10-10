package PCG;


public class BSTLeaf {
  // leaf bounds
  private int posx, posy;
  private int wid,  hgt;
  // room bounds
  private int min_x, min_y;
  private int max_x, max_y;
  private BSTLeaf childLeft;
  private BSTLeaf childRight;

  public BSTLeaf(int minsize, boolean splitDir, int x, int y, int w, int h) throws Exception {
    // splitDir is a boolean direction of the split - true if horizontal, false if vertical
    posx = x;
    posy = y;
    wid = w;
    hgt = h;
    if ( minsize < w && minsize < h ) {
      int splitLine = (splitDir) ? w / 2 : h / 2;
      splitLine += Util.randint(0, 3);
      if (splitDir) assert splitLine < w : "splitline is bad";
      else          assert splitLine < h : "splitline is bad";
      assert splitLine > 0 : "splitline is negative";
      int new_x1 = x;
      int new_y1 = y;
      int new_x2 = (splitDir) ? x + splitLine : x;
      int new_y2 = (splitDir) ? y : y + splitLine;
      int new_w1 = (splitDir) ? splitLine     : w;
      int new_w2 = (splitDir) ? w - splitLine : w;
      int new_h1 = (splitDir) ? h : splitLine;
      int new_h2 = (splitDir) ? h : h - splitLine;
      if (splitDir) assert new_w1 + new_w2 == w : "splitline is bad";
      else          assert new_h1 + new_h2 == h : "splitline is bad";
      childLeft  = new BSTLeaf(minsize, !splitDir, new_x1, new_y1, new_w1, new_h1);
      childRight = new BSTLeaf(minsize, !splitDir, new_x2, new_y2, new_w2, new_h2);
    }
  }

  public int[] getRoom() {
    return new int[]{min_x, min_y, max_x, max_y};
  }

  public int[] getBounds() {
    return new int[]{posx, posy, wid, hgt};
  }

  public void placeRooms(int[][] map ) {
    if ( childLeft != null ) {
      childLeft.placeRooms(map);
      childRight.placeRooms(map);
//      int[] room1 = childLeft.getRoom();
//      int[] room2 = childRight.getRoom();
//      min_x = Math.max(room1[0], room2[0]);
//      min_y = Math.max(room1[1], room2[1]);
//      max_x = Math.min(room1[2], room2[2]);
//      max_y = Math.min(room1[3], room2[3]);
      connectRooms(map, childLeft, childRight);
    }
    else {
      int bias_x = Math.abs(Util.randint(0, 3));
      int bias_y = Math.abs(Util.randint(0, 3));
      min_x = posx+bias_x;
      min_y = posy+bias_y;
      max_x = posx+wid-1;
      max_y = posy+hgt-1;
      for (int x=bias_x; x<wid-1; x++) {
        for (int y=bias_y; y<hgt; y++) {
          map[posy+y][posx+x] = 1;
        }
      }
    }
  }

  private void connectRooms( int[][] map, BSTLeaf room1, BSTLeaf room2 ) {
    int[] b1= room1.getBounds();
    int[] b2= room2.getBounds();
    // do a horizontal corridor if rooms are horizontally aligned
    if ( b1[0]==b2[0] ) {
      int x = b1[0]+b1[2]/2;
      int y1 = b1[1]+b1[3]/2;
      int y2 = b2[1]+b2[3]/2;
      int increment = Integer.signum(y2-y1);
      for (int y=y1; y!=y2; y+=increment) {
        map[y][x] = 1;
      }
      return;
    }
    // do a vertical corridor if rooms are vertical aligned
    if ( b1[1]==b2[1] ) {
      int y = b1[1]+b1[3]/2;
      int x1 = b1[0]+b1[2]/2;
      int x2 = b2[0]+b2[2]/2;
      int increment = Integer.signum(x2-x1);
      for (int x=x1; x!=x2; x+=increment) {
        map[y][x] = 1;
      }
      return;
    }
    System.out.println("Failed to build a corridor");
  }
}


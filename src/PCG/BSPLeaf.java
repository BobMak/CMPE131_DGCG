package PCG;


public class BSPLeaf {
  // leaf bounds
  private int posx, posy;
  private int wid,  hgt;
  // room bounds
  private int min_x, min_y;
  private int max_x, max_y;
  private BSPLeaf childLeft;
  private BSPLeaf childRight;

  public BSPLeaf(int minsize,
                 boolean splitDir,
                 int x, int y,
                 int w, int h) throws Exception {
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
      childLeft  = new BSPLeaf(minsize, !splitDir,
        new_x1, new_y1,
        new_w1, new_h1);
      childRight = new BSPLeaf(minsize, !splitDir,
        new_x2, new_y2,
        new_w2, new_h2);
    }
  }

  public int[] getBounds() {
    return new int[]{posx, posy, wid, hgt};
  }

  public void placeRooms(int[][] map, int[] bias ) {
    if ( childLeft != null ) {
      childLeft.placeRooms(map, bias);
      childRight.placeRooms(map, bias);
//      int[] room1 = childLeft.getRoom();
//      int[] room2 = childRight.getRoom();
//      min_x = Math.max(room1[0], room2[0]);
//      min_y = Math.max(room1[1], room2[1]);
//      max_x = Math.min(room1[2], room2[2]);
//      max_y = Math.min(room1[3], room2[3]);
      connectRooms(map, childLeft, childRight);
    }
    else {
      int bias_x_left = Math.abs(Util.randint(bias[0], bias[1]));
      int bias_x_right = Math.abs(Util.randint(bias[0], bias[1]));
      int bias_y_top = Math.abs(Util.randint(bias[2], bias[3]));
      int bias_y_bottom = Math.abs(Util.randint(bias[2], bias[3]));
      min_x = posx+bias_x_left;
      min_y = posy+bias_y_top;
      max_x = posx+wid-bias_x_right;
      max_y = posy+hgt-bias_y_bottom;
      for (int y=min_y; y<max_y; y++) {
        for (int x=min_x; x<max_x; x++) {
          map[y][x] = 1;
        }
      }
    }
  }

  private void connectRooms(int[][] map, BSPLeaf room1, BSPLeaf room2 ) {
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

  // check if the stub pattern matches with the pattern around the (x, y) on the map
  private boolean isStub( int[][] map, int x, int y ) {
    int[][][] stubPatterns =
      { { { -1, -1, -1 },
          { -1,  1, -1 },
          {  1,  1,  1 }, },
        { { -1, -1, -1 },
          { -1,  1, -1 },
          { -1,  1,  1 }, },
        { { -1, -1, -1 },
          { -1,  1, -1 },
          {  1,  1, -1 }, } };
    int sum;
    for ( int[][] pattern : stubPatterns ) {
      // there are 4 possible directions of the pattern, so rotate and check 4 times
      for ( int _=0; _<4; _++ ) {
        sum = 0;
        for ( int suby=-1; suby<2; suby++) {
          for ( int subx=-1; subx<2; subx++ ) {
            sum += map[y + suby][x + subx] * pattern[suby + 1][subx + 1];
          }
        }
        if ( sum==4 ) return true;
        Util.rotateMatrix(3, pattern);
      }
    }
    return false;
  }

  private boolean isTail( int[][] map, int x, int y ) {
    return map[y][x] == 1 &&
            ( map[y-1][x-1] + map[y-1][x] + map[y-1][x+1]
            + map[y][x-1]   + map[y][x+1]
            + map[y+1][x-1] + map[y+1][x] + map[y+1][x+1]) < 2;
  }

  public void removeDeadEnds( int[][] map ) {
    for ( int y=1; y< map.length-1; y++ ) {
      for ( int x=1; x<map[0].length-1; x++ ) {
        if ( isTail(map, x, y) || isStub(map, x, y) ) {
          map[y][x] = 0;
        }
      }
    }
  }
}


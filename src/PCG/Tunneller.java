package PCG;

import java.util.HashSet;
import java.util.Random;

public class Tunneller implements Generator {
  private int border_left;
  private int border_right;
  private int border_top;
  private int border_bottom;

  //
  private HashSet<int[]> checkpoints;
  private int[][] rooms;

  private Random rnd;

  @Override
  public int[][] generate(String[] config) throws Exception {
    int roomDimMax       = Integer.parseInt(config[0]);
    int roomDimMin       = Integer.parseInt(config[1]);
    int primeCorrLenMax  = Integer.parseInt(config[2]);
    int primeCorrLenMin  = Integer.parseInt(config[3]);
    int primeCorrWidth   = Integer.parseInt(config[4]);
    int primeCorrDistMax = Integer.parseInt(config[5]);
    int primeCorrDistMin = Integer.parseInt(config[6]);
    float primeCorrBranchProb = Float.parseFloat(config[7]);
    int primeCorrCount   = Integer.parseInt(config[8]);
    int secCorrLenMax   = Integer.parseInt(config[9]);
    int secCorrLenMin   = Integer.parseInt(config[10]);
    int roomSparsity   = Integer.parseInt(config[11]);
    // initialize map with bigger size so that it can feet the map while placing corridors and rooms
    int[][] map = new int[primeCorrLenMax*2 + secCorrLenMax*2 + roomDimMax*2][primeCorrLenMax*2 + secCorrLenMax*2 + roomDimMax*2];
    rnd = new Random();
    // the starting point is always in the center
    int[] tunnelerLocation = { map.length/2, map.length/2 };
    // will be used to specify the direction of the tunneling
    int tunnelerDirection = Util.randint(1, 4);
    checkpoints = new HashSet<int[]>(100);
    checkpoints.add( tunnelerLocation );
    digMainCorridors(map,
      primeCorrLenMax, primeCorrLenMin,
      primeCorrWidth,   primeCorrCount,
      primeCorrDistMax, primeCorrDistMin,
      primeCorrBranchProb, tunnelerLocation,
      tunnelerDirection, roomSparsity);
    digRooms(map, roomDimMax, roomDimMin);

    findBorders(map);
    // trim unused parts of the map
    int[][] trimmedMap = new int[border_top - border_bottom+4][border_right - border_left+4];
    for ( int y=border_bottom-1; y<border_top+2; y++ ) {
      for ( int x=border_left-1; x<border_right+2; x++ ) {
        trimmedMap[y-border_bottom+1][x-border_left+1] = map[y][x];
      }
    }
    return trimmedMap;
  }

  @Override
  public String[][] getDefaultConfigParameters() {
    return new String [][]{
      {"room dimension max", "10",
        "Maximum width or height of rooms be generated "},
      {"room dimension min", "4",
        "Minimum width or height of rooms be generated "},
      {"primary corridor length max", "100",
        "Length of the corridor"},
      {"primary corridor length min", "90",
        "Length of the corridor"},
      {"primary corridor width", "3",
        "diameter/width of the corridor"},
      {"primary corridor distance between turns max", "20",
        "Will turn every X blocks into random direction"},
      {"primary corridor distance between turns min", "10",
        "Will turn every X blocks into random direction"},
      {"primary corridor branching probability", "0.1",
        "On every turn will branch out with probability X"},
      {"primary corridors", "1",
        "Number of corridors starting in different locations"},
      {"secondary corridors", "1",
        "will branch out from primary corridors or rooms every N blocks"},
      {"secondary corridors length max", "100",
        "Length of the corridor"},
      {"secondary corridors length min", "90",
        "Length of the corridor"},
      {"room sparsity", "5",
        "Place rooms every X steps along main corridors"},
    };
  }

  private void digMainCorridors( int[][] map,
                                 int distMax,
                                 int distMin,
                                 int width,
                                 int count,
                                 int travelDistMax,
                                 int travelDistMin,
                                 float branchProb,
                                 int[] tunnelerLocation,
                                 int direction,
                                 int roomSparsity) throws Exception {
    Util.sop("Digging prime corridor at " + tunnelerLocation);
    if ( isUnoccupied( map, tunnelerLocation[0], tunnelerLocation[1], width, width ) )
      tunnelerLocation = getUnocupiedWithin( map,
        tunnelerLocation[0], tunnelerLocation[1], 100, width );
      // too crowded, can't find any place to put a new corridor start
      if ( tunnelerLocation==null ) {
        Util.sop("[WARNIGN] filed to find a starting area for a primary corridor");
      }
    int maxDist = Util.randint(distMin, distMax) / count;
    // 1 - north, 2 - east, 3 - south, 4 - west
    int [][] digDirectionSteps = {
      {0,  1},
      {1,  0},
      {0, -1},
      {-1, 0},
    };
    // make count number of corridors of length maxDist
    for ( int _=0; _<count; _++ ) {
      int dist = 0;
      int roomCheckPointCount = 0;
      while ( dist < maxDist  ) {
        int travelDist = Util.randint(travelDistMin, travelDistMax);
        for (int step = 0; step < travelDist; step++) {
          // advance one block ahead
          if (tunnelerLocation == null)
            return;
          if ( roomCheckPointCount > roomSparsity ) {
            checkpoints.add( new int[]{tunnelerLocation[0], tunnelerLocation[1] } );
            roomCheckPointCount = 0;
          }
          digOutArea(map, tunnelerLocation[0], tunnelerLocation[1], width, width);
          tunnelerLocation[0] += digDirectionSteps[direction - 1][0];
          tunnelerLocation[1] += digDirectionSteps[direction - 1][1];
          dist += 1;
          roomCheckPointCount += 1;
        }
        checkpoints.add( new int[]{tunnelerLocation[0], tunnelerLocation[1]} );
        int newDirection = getNewDirection(direction);
        // create a branch that goes in a different direction with this one.
        // Split remaining distance between this one and new one
        if (rnd.nextFloat() < branchProb && dist<maxDist) {
          int branchDirection = getNewDirection(direction);
          // re-roll the dice if it is equal to the current new direction of the corridor
          while (branchDirection == newDirection)
            branchDirection = getNewDirection(direction);
          int distanceLeft = maxDist - dist;
          maxDist = maxDist - distanceLeft / 2;
          int[] branchLocation = {
            tunnelerLocation[0] + digDirectionSteps[branchDirection-1][0],
            tunnelerLocation[1] + digDirectionSteps[branchDirection-1][1]
          };
          digMainCorridors(map,
            distanceLeft / 2,
            distanceLeft / 2 - 1,
            width,
            1,
            travelDistMax,
            travelDistMin,
            branchProb,
            branchLocation,
            branchDirection,
            roomSparsity);
        }
        direction = newDirection;
      }
      int[][] chps = checkpoints.toArray(new int[0][]);
      int idx = rnd.nextInt(chps.length);
      int[] newDigPoint = chps[idx];
      tunnelerLocation = getUnocupiedWithin(map, newDigPoint[0], newDigPoint[1], 60, width);
    }
  }

  private void digRooms( int[][] map, int maxDims, int minDims ) {
    Util.sop("Generating rooms");
    for ( int[] chckp : checkpoints ) {
      generateRoom( map, chckp[0], chckp[1], maxDims, minDims );
    }
  }

  //
  private void generateRoom( int[][] map, int x, int y, int maxd, int mind ) {
    int room_width  = Util.randint( mind, maxd );
    int room_height = Util.randint( mind, maxd );
    int[] coordinates = getClosestUnoccupiedSpace( map, x, y, room_width+2, room_height+2 );
    digOutArea(map, coordinates[0], coordinates[1], room_width, room_height);
  }

  private int[] getClosestUnoccupiedSpace( int[][] map, int x, int y, int width, int height ) {
    int[] result = null;
    int bestDist = 1000;
    for ( int sy=-height*2; sy < height*2; sy++ ) {
      for ( int sx=-width*2; sx < width*2; sx++ ) {
        if ( isUnoccupied( map, x+sx, y+sy, width, height )
           && (int)Math.sqrt( Math.pow(sx, 2) + Math.pow(sy, 2) ) < bestDist ) {
          bestDist = (int)Math.sqrt( Math.pow(sx, 2) + Math.pow(sy, 2) );
          result = new int[]{ x+sx, y+sy };
        }
      }
    }
    return result;
  }

  private int[] getUnocupiedWithin(int[][] map, int x, int y, int searchRadius, int requiredSpace) {
    int checkX = x;
    int checkY = y;
    for (int tries=0; tries<1000; tries++) {
      if ( isUnoccupied(map, checkX, checkY, requiredSpace, requiredSpace) ) {
        return new int[] { checkX, checkY };
      }
      checkX = Util.randint( x-searchRadius, x+searchRadius );
      checkY = Util.randint( y-searchRadius, y+searchRadius );
    }
    return null;
  }

  private boolean isUnoccupied(int[][] map, int x, int y, int width, int height ) {
    boolean valid = true;
    int xStart = x - (int)Math.floor((double) width/2);
    int xEnd   = x + (int)Math.ceil ((double) width/2);
    int yStart = y - (int)Math.floor((double) height/2);
    int yEnd   = y + (int)Math.ceil ((double) height/2);
    for ( int yCheck=yStart; yCheck<yEnd; yCheck++ ) {
      for ( int xCheck=xStart; xCheck<xEnd; xCheck++ ) {
        if ( map[ yCheck ][ xCheck ]!=0 ) {
          valid = false;
        }
      }
    }
    return valid;
  }

  private void digOutArea( int[][] map, int x, int y, int width, int height ) {
    int xStart = x - (int)Math.floor((double) width/2);
    int xEnd   = x + (int)Math.ceil ((double) width/2);
    int yStart = y - (int)Math.floor((double) height/2);
    int yEnd   = y + (int)Math.ceil ((double) height/2);
    for ( int idxY = yStart; idxY < yEnd; idxY++ ) {
      for ( int idxX = xStart; idxX < xEnd; idxX++ ) {
        map[ idxY ][ idxX ] = 1;
      }
    }
  }

  private int getNewDirection(int direction) throws Exception {
    if ( direction > 0 && direction < 5 ) {
      int newDir = direction + Util.randint(0, 3) - 1;
      return _intToDirection( newDir );
    }
    throw new Exception("Invalid direction d=" + direction + " has to be in range from 1 to 4");
  }

  private int _intToDirection( int i ) {
    if ( i < 1 )
      i = 4;
    if ( i > 4 )
      i = 1;
    return i;
  }

  private void findBorders( int[][] map ) {
    border_left  = map[0].length;
    border_right = 0;
    border_top   = 0;
    border_bottom= map.length;
    for ( int idx_y=0; idx_y<map.length; idx_y++ )
      for ( int idx_x=0; idx_x< map[0].length; idx_x++ )
        if ( map[idx_y][idx_x] != 0 )
          if ( idx_x < border_left) {
            border_left = idx_x;
            break;
          }
    for ( int idx_y=0; idx_y<map.length; idx_y++ )
      for ( int idx_x=map[0].length-1; idx_x>0; idx_x-- )
        if ( map[idx_y][idx_x] != 0 )
          if ( idx_x > border_right) {
            border_right = idx_x;
            break;
          }
    for ( int idx_x=0; idx_x < map[0].length; idx_x++ )
      for ( int idx_y=0; idx_y< map.length; idx_y++ )
        if ( map[idx_y][idx_x] != 0 )
          if ( idx_y < border_bottom) {
            border_bottom = idx_y;
            break;
          }
    for ( int idx_x=map[0].length-1; idx_x>0; idx_x-- )
      for ( int idx_y=map.length-1; idx_y>0; idx_y-- )
        if ( map[idx_y][idx_x] != 0 )
          if ( idx_y > border_top) {
            border_top = idx_y;
            break;
          }

  }
}

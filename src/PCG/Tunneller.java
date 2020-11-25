package PCG;

import java.util.HashSet;
import java.util.Random;

public class Tunneller implements Generator {
  private HashSet<int[]> checkpoints;
  private HashSet<int[]> primeCheckpoints;
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
    int primeCorrCount  = Integer.parseInt(config[8]);
    int secCorrCount    = Integer.parseInt(config[9]);
    int secCorrLenMax   = Integer.parseInt(config[10]);
    int secCorrLenMin   = Integer.parseInt(config[11]);
    int loopsCount      = Integer.parseInt(config[12]);
    int roomSparsity    = Integer.parseInt(config[13]);
    // initialize map with bigger size so that it can feet the map while placing corridors and rooms
    int[][] map = new int[primeCorrLenMax*2 + secCorrLenMax*2 + roomDimMax*2][primeCorrLenMax*2 + secCorrLenMax*2 + roomDimMax*2];
    rnd = new Random();
    // the starting point is always in the center
    int[] tunnelerLocation = { map.length/2, map.length/2 };
    // will be used to specify the direction of the tunneling
    int tunnelerDirection = Util.randint(1, 4);
    checkpoints = new HashSet<int[]>(100);
    primeCheckpoints = new HashSet<>();
    checkpoints.add( tunnelerLocation );
    // excavates primary corridors and leaves checkpoints around which rooms are to be placed
    digPrimeCorridors(map,
      primeCorrLenMax, primeCorrLenMin,
      primeCorrWidth,   primeCorrCount,
      primeCorrDistMax, primeCorrDistMin,
      primeCorrBranchProb, tunnelerLocation,
      tunnelerDirection, roomSparsity);
    // connects different primary corridors and adds loops
    digSecondaryCorridors(map,
      secCorrLenMax, secCorrLenMin,
      secCorrCount,   roomSparsity,
      tunnelerLocation, tunnelerDirection,
      roomSparsity, loopsCount);
    // places rooms in closest unoccupied area around every checkpoint
    digRooms(map, roomDimMax, roomDimMin);
    // trim unused parts of the map
    return trimUnused(map);
  }

  private int[][] trimUnused(int[][] map) {
    int[] borders = findBorders(map);
    int border_left  = borders[0];
    int border_right = borders[1];
    int border_top = borders[2];
    int border_bottom = borders[3];
    try{
      int[][] trimmedMap = new int[border_top - border_bottom+4][border_right - border_left+4];
      for ( int y=border_bottom-1; y<border_top+2; y++ )
        for ( int x=border_left-1; x<border_right+2; x++ )
          trimmedMap[y-border_bottom+1][x-border_left+1] = map[y][x];
      return trimmedMap;
    }
    catch (Exception e) {
      Util.sop("[Warning] Failed to trim borders");
    }
    return map;
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
      {"loops count", "3",
        "Number of loops added with secondary corridors"},
      {"room sparsity", "5",
        "Place rooms every X steps along main corridors"},
    };
  }

  private void digPrimeCorridors(int[][] map,
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
    if ( distMin > distMax )
      throw new Exception("Max corridor length < Min corridor length");
    if ( travelDistMin > travelDistMax )
      throw new Exception("Max travel distance < Min travel distance");
    if ( distMin < 1 )
      throw new Exception("travel distance less then zero");
    Util.sop("Digging prime corridor at [" + tunnelerLocation[0]+"]["+tunnelerLocation[0]+"]");
    if ( !isUnoccupied( map, tunnelerLocation[0], tunnelerLocation[1], width, width ) )
      Util.sop("[WARNIGN] starting a primary corridor on top of the previous one");
    int maxDist = Util.randint(distMin, distMax) / count;
    // 1 - north, 2 - east, 3 - south, 4 - west
    int [][] digDirectionSteps = {
      {0,  1},
      {1,  0},
      {0, -1},
      {-1, 0},
    };
    // make a count number of corridors of length maxDist
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
          if (dist > maxDist)
            break;
          roomCheckPointCount += 1;
        }
        checkpoints.add( new int[]{tunnelerLocation[0], tunnelerLocation[1]} );
        int newDirection = getNewDirection(direction);
        // create a branch that goes in a different direction with this one.
        // Split remaining distance between this one and new one
        if (rnd.nextFloat() < branchProb && dist < maxDist) {
          int branchDirection = getNewDirection(direction);
          // re-roll the dice if it is equal to the current new direction of the corridor
          while (branchDirection == newDirection)
            branchDirection = getNewDirection(direction);
          int distanceLeft = maxDist - dist;
          maxDist = maxDist - distanceLeft / 2;
          int[] branchLocation = {
            tunnelerLocation[0] + digDirectionSteps[branchDirection-1][0]*width,
            tunnelerLocation[1] + digDirectionSteps[branchDirection-1][1]*width
          };
          digPrimeCorridors(map,
            Math.min(distanceLeft / 2, 1) ,
            Math.min(distanceLeft / 2, 1) ,
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
      primeCheckpoints.add(tunnelerLocation);
      // Get next location for a corridor
      int[][] chps = checkpoints.toArray(new int[0][]);
      int idx = rnd.nextInt(chps.length);
      int[] newDigPoint = chps[idx];
      tunnelerLocation = getClosestUnoccupiedSpace(map, newDigPoint[0], newDigPoint[1], width+10, width+10);
    }
  }

  private void digSecondaryCorridors(int[][] map,
                                     int secCorrLenMax,
                                     int secCorrLenMin,
                                     int secCorrCount,
                                     int roomSparsity,
                                     int[] tunnelerLocation,
                                     int tunnelerDirection, int roomSparsity1, int loopsCount) throws Exception {
    int[][] locations = primeCheckpoints.toArray(new int[0][]);
    for ( int[] loc: locations ) {
      int[] anotherLoc = locations[rnd.nextInt(locations.length)];
      connect(map, loc, anotherLoc);
    }
  }

  // draw a horizontal and vertical corridor
  // will pierce through any rooms and corridors on the way
  private void connect(int[][] map, int[] loc1, int[] loc2) throws Exception {
    // dig out vertical corridor
    digOutAreaFromTo(map, loc1[0], loc1[1], 1, loc2[1] - loc1[1]);
    // dig out horizontal
    digOutAreaFromTo(map, loc1[0], loc2[1], loc2[0] - loc1[0], 1);
  }

  private void digRooms( int[][] map, int maxDims, int minDims ) throws Exception {
    Util.sop("Generating rooms around " + checkpoints.size()+ " checkpoints");
    // count successfully generated rooms;
    int _count = 0;
    for ( int[] chckp : checkpoints ) {
      int[] roomxy = generateRoom( map, chckp[0], chckp[1], maxDims, minDims );
      if (roomxy!=null) {
        _count++;
        connect( map, chckp, roomxy );
      }
    }
    Util.sop("Generated " + _count + " rooms");
  }

  private int[] generateRoom( int[][] map, int x, int y, int maxd, int mind ) throws Exception {
    int room_width  = Util.randint( mind, maxd );
    int room_height = Util.randint( mind, maxd );
    int[] coordinates = getClosestUnoccupiedSpace( map, x, y, room_width+2, room_height+2 );
    if ( coordinates != null )
      digOutArea(map, coordinates[0], coordinates[1], room_width, room_height);
    return coordinates;
  }

  private int[] getClosestUnoccupiedSpace( int[][] map, int x, int y, int width, int height ) {
    int[] result = null;
    int bestDist = 1000;
    for ( int sy=-height*2; sy < height*2; sy++ )
      for ( int sx=-width*2; sx < width*2; sx++ )
        if ( isUnoccupied( map, x+sx, y+sy, width, height )
           && (int)Math.sqrt( Math.pow(sx, 2) + Math.pow(sy, 2) ) < bestDist ) {
          bestDist = (int)Math.sqrt( Math.pow(sx, 2) + Math.pow(sy, 2) );
          result = new int[]{ x+sx, y+sy };
        }
    return result;
  }

  // returns a random unoccupied area within the search radius
  private int[] getUnocupiedWithin(int[][] map, int x, int y, int searchRadius, int requiredSpace) {
    int checkX = x;
    int checkY = y;
    for (int tries=0; tries<100; tries++) {
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
    if ( xStart < 0 || xEnd >= map[0].length || yStart < 0 || yEnd >= map.length )
      return false;
    for ( int yCheck=yStart; yCheck<yEnd; yCheck++ )
      for ( int xCheck=xStart; xCheck<xEnd; xCheck++ )
        if ( map[ yCheck ][ xCheck ]!=0 )
          valid = false;
    return valid;
  }

  // dig our area with width and height around x,y
  private void digOutArea( int[][] map, int x, int y, int width, int height ) throws Exception {
    if ( width < 0 || height < 0 )
      throw new Exception("tried to dig out area with width=" + width + " and heihgt=" + height);
    int xStart = x - (int)Math.floor((double) width/2);
    int xEnd   = x + (int)Math.ceil ((double) width/2);
    int yStart = y - (int)Math.floor((double) height/2);
    int yEnd   = y + (int)Math.ceil ((double) height/2);
    if ( xStart < 0 || xEnd >= map[0].length || yStart < 0 || yEnd >= map.length )
      return;
    for ( int idxY = yStart; idxY < yEnd; idxY++ )
      for ( int idxX = xStart; idxX < xEnd; idxX++ )
        map[ idxY ][ idxX ] = 1;
  }

  // dig out area from x,y to x+w,y+h
  private void digOutAreaFromTo( int[][] map, int x, int y, int width, int height ) {
    int incrementX = (int)Math.signum(width);
    int incrementY = (int)Math.signum(height);
    for ( int idxY = y; idxY != y+height; idxY+=incrementY )
      for ( int idxX = x; idxX != x+width; idxX+=incrementX )
        map[ idxY ][ idxX ] = 1;
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

  private int[] findBorders( int[][] map ) {
    int border_left  = map[0].length;
    int border_right = 0;
    int border_top   = 0;
    int border_bottom= map.length;
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
    return new int[] { border_left, border_right, border_top, border_bottom };
  }
}

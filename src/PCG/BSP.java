package PCG;

public class BSP implements Generator {
  public String[][] getDefaultConfigParameters() {
    return new String [][]{
      {"max room dimension", "10",
        "Maximum width or height of the sector where the room will be generated. " +
        "Actual room dimensions will be determined by margins"},
      {"width", "60",
        "Map layout width"},
      {"height", "50",
        "Map layout height"},
      {"min horizontal margin", "1",
        "Minimum margin from the left and right in every room sector. " +
        "2 random margins in range from min to max are applied"},
      {"max horizontal margin", "3",
        "Maximum margin from the left and right in every room sector. " +
        "2 random margins in range from min to max are applied"},
      {"min vertical margin", "1",
        "Minimum margin from the top and bottim in every room sector. " +
        "2 random margins in range from min to max are applied"},
      {"max vertical margin", "3",
        "Maximum margin from the top and bottim in every room sector. " +
        "2 random margins in range from min to max are applied"},
      {"max split ratio", "3",
        "Map sectors will be recursively split into parts proportional to 1/n and (1-1/n) " +
        "where n is a random number from 2 to X "+
        "if X=2, rooms are always evenly split variety is lowest "+
        "The greater the X, the greater the variety"}};
  }

  public int[][] generate(String[] config) throws Exception {
    System.out.println("BSP: room max: "+config[0]
      +" w: "+ config[1]
      +" h: "+ config[2]);
    int maxRoomDim = Integer.parseInt(config[0]);
    int width      = Integer.parseInt(config[1]);
    int height     = Integer.parseInt(config[2]);
    int minHorMarg = Integer.parseInt(config[3]);
    int maxHorMarg = Integer.parseInt(config[4]);
    int minVerMarg = Integer.parseInt(config[5]);
    int maxVerMarg = Integer.parseInt(config[6]);
    int maxSpltRat = Integer.parseInt(config[7]);
    int[][] map  = new int[height][width];
    BSPLeaf base = new BSPLeaf( maxRoomDim, Util.randBool(),
      0, 0,
      width-1, height-1,
      maxSpltRat);
    base.placeRooms(map, new int[]{ minHorMarg, maxHorMarg, minVerMarg, maxVerMarg });
    while ( true ) {
      if ( !base.removeDeadEnds(map) ) {
        break;
      }
    }
    return map;
  }

}

package PCG;

public class BSP implements Generator {
  public String[][] getConfigParameters() {
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

  public int[][] generate(int[] config) throws Exception {
    // config: max room size, width, height
    System.out.println("BSP: room max: "+config[0]
      +" w: "+config[1]
      +" h: "+ config[2]);
    int[][] map  = new int[config[2]][config[1]];
    BSPLeaf base = new BSPLeaf( config[0], Util.randBool(),
      0, 0,
      config[1]-1, config[2]-1,
      config[7]);
    base.placeRooms(map, new int[]{ config[3], config[4], config[5], config[6] });
    while ( true ) {
      if ( !base.removeDeadEnds(map) ) {
        break;
      }
    }
    return map;
  }

}

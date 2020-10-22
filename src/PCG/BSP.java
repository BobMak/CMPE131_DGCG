package PCG;

public class BSP implements Generator {
  public String[][] getConfigParameters() {
    return new String [][]{
      {"max room size", "7"},
      {"width", "75"},
      {"height", "50"},
      {"min horizontal margin", "1"},
      {"max horizontal margin", "3"},
      {"min vertical margin", "1"},
      {"max vertical margin", "3"}};
  }

  public int[][] generate(int[] config) throws Exception {
    // config: max room size, width, height
    System.out.println("BSP: room max: "+config[0]+" w: "+config[1]+" h: "+ config[2]);
    int[][] map  = new int[config[2]][config[1]];
    BSPLeaf base = new BSPLeaf( config[0], Util.randBool(),
      0, 0,
      config[1]-1, config[2]-1);
    base.placeRooms(map, new int[]{ config[3], config[4], config[5], config[6] });
    base.removeDeadEnds(map);
    base.removeDeadEnds(map);
    base.removeDeadEnds(map);
    return map;
  }

}

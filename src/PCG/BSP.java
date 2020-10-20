package PCG;

public class BSP implements Generator {
  BSPLeaf[] leafs;
  public String[][] getConfigParameters() {
    return new String [][]{{"max room size", "7"}, {"width", "75"}, {"height", "50"}};
  }

  public int[][] generate(int[] config) throws Exception {
    // config: max room size, width, height
    int[][] map  = new int[config[0]][config[1]];
    BSPLeaf base = new BSPLeaf( config[2], true, 0, 0, config[1]-1, config[0]-1 );
    base.placeRooms(map);
    base.removeDeadEnds(map);
    base.removeDeadEnds(map);
    base.removeDeadEnds(map);
    return map;
  }

}

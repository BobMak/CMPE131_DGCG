package PCG;

public class BSP implements Generator {
  BSPLeaf[] leafs;
  public int[][] generate(int minspace, int max_x, int max_y) throws Exception {
    int[][] map  = new int[max_y][max_x];
    BSPLeaf base = new BSPLeaf( minspace, true, 0, 0, max_x-1, max_y-1 );
    base.placeRooms(map);
    base.removeDeadEnds(map);
    base.removeDeadEnds(map);
    base.removeDeadEnds(map);
    return map;
  }

}

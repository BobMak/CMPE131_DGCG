package PCG;

public class BST implements Generator {
  BSTLeaf[] leafs;
  public int[][] generate(int minspace, int max_x, int max_y) throws Exception {
    int[][] map  = new int[max_y][max_x];
    BSTLeaf base = new BSTLeaf( minspace, true, 0, 0, max_x-1, max_y-1 );
    base.placeRooms(map);
    base.removeDeadEnds(map);
    base.removeDeadEnds(map);
    base.removeDeadEnds(map);
    return map;
  }

}

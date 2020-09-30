package PCG;

public class BST {
  BSTLeaf[] leafs;
  public static int[][] generate(int minspace, int max_x, int max_y) {
    int[][] map  = new int[max_y][max_x];
    BSTLeaf base = new BSTLeaf( minspace, true, 0, 0, max_x-1, max_y-1 );
    base.getRoom(map);
    return map;
  }

}

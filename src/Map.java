import PCG.BST;

public class Map {
  private int[][] map;

  Map() {
    map = new int[50][75];
    int width  = 50;
    int height = 30;
    map = BST.generate(10, width, height);
  }

  public int[][] getMap() {
    return map;
  }
}

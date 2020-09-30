import PCG.BST;

public class Map {
  private int[][] map;

  Map() {
    map = new int[50][75];
    int width  = 75;
    int height = 50;
    map = BST.generate(10, width, height);
  }

  public int[][] getMap() {
    return map;
  }
}

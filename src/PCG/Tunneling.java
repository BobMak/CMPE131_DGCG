package PCG;

public class Tunneling implements Generator {
  @Override
  public int[][] generate(int[] config) throws Exception {
    return new int[0][];
  }

  @Override
  public String[][] getConfigParameters() {
    return new String [][]{
      {"max room dimension", "10"},
      {"width", "60"},
      {"height", "50"},
      {"min horizontal margin", "1"},
      {"max horizontal margin", "3"},
      {"min vertical margin", "1"},
      {"max vertical margin", "3"},
      {"max split ratio", "3"}};
  }
}

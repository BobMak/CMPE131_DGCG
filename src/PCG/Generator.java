package PCG;

public interface Generator {
  public int[][] generate(int[] config) throws Exception;

  public String[][] getConfigParameters();
}

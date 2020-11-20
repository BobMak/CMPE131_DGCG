package PCG;

public interface Generator {
  public int[][] generate(String[] config) throws Exception;

  public String[][] getDefaultConfigParameters();
}

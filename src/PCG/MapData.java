package PCG;

public class MapData implements java.io.Serializable  {
  int[][] map;
  String algorithm;
  int[] config;

  public MapData(String palgorithm, int[] pconfig, int[][] pmap){
    map = pmap;
    config = pconfig;
    algorithm = palgorithm;
  }
}

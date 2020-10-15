import PCG.BSP;
import PCG.Generator;
import PCG.Util;

import java.util.HashMap;

public class MapGenerator {
  private int[][] map;
  private HashMap<String, Generator> algos = new HashMap<String, Generator>(){{
    put("BSP", new BSP());
  }};
  private String configAlgorithm;
  private int configSizeX;
  private int configSizeY;
  private int configMinRoomSize;

  private String error;

  // Default config
  MapGenerator() {
    configAlgorithm = "BSP";
    configSizeX = 75;
    configSizeY = 50;
    configMinRoomSize = 10;
    error = "        ";
  }

  public void setAlgorithm( String algorithm ) {
    configAlgorithm = algorithm;
  }

  public void setMapSize( int sizeX, int sizeY ) {
    configSizeX = sizeX;
    configSizeY = sizeY;
  }

  public void setMinRoomSize( int minRoomSize ) {
    configMinRoomSize = minRoomSize;
  }

  public int[][] generate() {
    map = new int[configSizeY+1][configSizeX+1];
    assert algos.containsKey(configAlgorithm) : "No algorithm";
    Generator g = algos.get(configAlgorithm);
    try {
      map = g.generate(configMinRoomSize, configSizeX, configSizeY);
      error = "        ";
    }
    catch (AssertionError ex) {
      Util.sop(ex);
      error = "Error: bad parameters";
    }
    catch (Exception ex) {
      Util.sop(ex);
      error = ex.getMessage();
    }
    return map;
  }

  public String getError() {
    return error;
  }

  public int[][] getMap() {
    return map;
  }
}

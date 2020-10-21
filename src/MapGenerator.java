import PCG.BSP;
import PCG.Generator;
import PCG.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class MapGenerator {
  private int[][] map;
  private HashMap<String, Generator> algos = new HashMap<String, Generator>(){{
    put("BSP", new BSP());
  }};
  private String configAlgorithm;
  private int[] config;

  private String error;

  // Default config
  MapGenerator() {
    configAlgorithm = "BSP";
    config = new int[]{10, 75, 50};
    error = "        ";
    map = new int[][] {{0,0},{0,0}};
  }

  public void setConfig( int[] pconfig ) {
    config = pconfig;
  }

  public void setConfigAlgorithm( String algo ) {
    configAlgorithm = algo;
  }

  public int[][] generate() {
//    map = new int[configSizeY+1][configSizeX+1];
    assert algos.containsKey(configAlgorithm) : "No algorithm";
    Generator g = algos.get(configAlgorithm);
    try {
      map = g.generate(config);
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

  public String[][] getConfigs( String algo ) {
    assert algos.containsKey(configAlgorithm) : "No algorithm";
    Generator g = algos.get(configAlgorithm);
    return g.getConfigParameters();
  }

  public void export() {
    try {
      FileOutputStream fileOut =
        new FileOutputStream("data/test.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(new PCG.MapData(configAlgorithm, config, map));
      out.close();
      fileOut.close();
      System.out.printf("Serialized data is saved in data/test.ser");
    } catch (IOException i) {
      i.printStackTrace();
    }
  }
}

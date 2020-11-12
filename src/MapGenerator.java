import PCG.BSP;
import PCG.Generator;
import PCG.MapData;
import PCG.Util;

import java.io.*;
import java.util.HashMap;

public class MapGenerator<FileInputStreamStream> {
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

  public String getConfigAlgorithm() {
    return configAlgorithm;
  }

  public String[][] getDefaultConfig( String algo ) {
    assert algos.containsKey(configAlgorithm) : "No algorithm";
    Generator g = algos.get(configAlgorithm);
    return g.getConfigParameters();
  }

  public int[] getConfig() {
    return config;
  }

  public void exportFile( String customName ) {
    try {
      File theDir = new File("data/");
      if (!theDir.exists()){
        theDir.mkdirs();
      }
      MapData md = new PCG.MapData(configAlgorithm, config, map);
      String filename;
      if ( customName.length() > 0 ) {
        filename = customName;
      }
      else {
        filename = md.getString();
      }
      FileOutputStream fileOut =
        new FileOutputStream("data/"+ filename +".ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(md);
      out.close();
      fileOut.close();
    } catch (IOException i) {
      i.printStackTrace();
    }
  }

  public void importFile(String filename) {
    try {
      FileInputStream fileIn =
        new FileInputStream("data/"+ filename);
      ObjectInputStream objInp = new ObjectInputStream (fileIn);
      MapData md = (MapData) objInp.readObject();
      objInp.close();
      fileIn.close();
      config = md.getConfig();
      map = md.getMap();
      configAlgorithm = md.getAlgorithm();
    } catch (IOException | ClassNotFoundException i) {
      i.printStackTrace();
    }
  }
}

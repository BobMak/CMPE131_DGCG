import PCG.*;

import java.io.*;
import java.util.HashMap;

public class MapGenerator<FileInputStreamStream> {
  private int[][] map;
  private HashMap<String, Generator> strToAlgorithm = new HashMap<String, Generator>(){{
    put("BSP", new BSP());
    put("Tunneler", new Tunneller());
  }};
  private String configAlgorithm;
  private String[] config;

  private String error;
  // Default config
  MapGenerator() {
    configAlgorithm = "Tunneler";
    String[][] strConfigs = getDefaultConfig();
    config = new String[ strConfigs.length ];
    for ( int idx=0; idx< strConfigs.length; idx++ ) {
      config[idx] = strConfigs[idx][1];
    }
    error = "        ";
    map = new int[][] {{0,0},{0,0}};
  }

  public void setConfig( String[] pconfig ) {
    config = pconfig;
  }

  public void setConfigAlgorithm( String algo ) {
    configAlgorithm = algo;
  }

  public int[][] generate() {
    assert strToAlgorithm.containsKey(configAlgorithm) : "No algorithm";
    Generator g = strToAlgorithm.get(configAlgorithm);
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

  public String[][] switchTo( String algorithm ) {
    if ( algorithm.equals(configAlgorithm) )
      return getConfigPhony();
    configAlgorithm = algorithm;
    String[][] strConfigs = getDefaultConfig();
    config = new String[ strConfigs.length ];
    for ( int idx=0; idx< strConfigs.length; idx++ ) {
      config[idx] = strConfigs[idx][1];
    }
    error = "        ";
    return strConfigs;
  }

  public String[][] getDefaultConfig() {
    assert strToAlgorithm.containsKey(configAlgorithm) : "No algorithm";
    Generator g = strToAlgorithm.get(configAlgorithm);
    return g.getDefaultConfigParameters();
  }

  public String[] getConfig() {
    return config;
  }

  public String[][] getConfigPhony() {
    String[] configs = getConfig();
    String[][] configsph = getDefaultConfig();
    for ( int idx=0;idx< configs.length; idx++ ) {
      configsph[idx][1] = configs[idx];
    }
    return configsph;
  }

  public String[] getAlgorithms(){
    return strToAlgorithm.keySet().toArray(new String[0]);
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

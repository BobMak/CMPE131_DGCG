package PCG;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MapData implements java.io.Serializable  {
  int[][] map;
  String algorithm;
  String[] config;

  public MapData(String palgorithm, String[] pconfig, int[][] pmap){
    map = pmap;
    config = pconfig;
    algorithm = palgorithm;
  }

  public String getString() {
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
    Date date = new Date(System.currentTimeMillis());
//    System.out.println(formatter.format(date));
    return algorithm + "_" + Arrays.toString(config) + "_" + formatter.format(date);
  }
  public String[] getConfig(){
    return config;
  }
  public String  getAlgorithm(){
    return algorithm;
  }
  public int[][] getMap(){
    return map;
  }
}

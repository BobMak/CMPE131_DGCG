package PCG;

import java.util.Random;

public class Util {
  public static int randint(int min, int max) {
    Random rn = new Random();
    int n = max - min + 1;
    int i = rn.nextInt() % n;
    return min + i;
  }
}

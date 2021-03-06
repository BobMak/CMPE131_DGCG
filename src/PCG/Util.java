package PCG;

import java.util.*;

public class Util {
  public static int randint(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

  // An Inplace function to rotate a N x N matrix
  // by 90 degrees in anti-clockwise direction
  // source: https://www.geeksforgeeks.org/inplace-rotate-square-matrix-by-90-degrees/
  static void rotateMatrix( int N, int mat[][] )
  {
    for (int x = 0; x < N / 2; x++) {
      for (int y = x; y < N - x - 1; y++) {
        int temp = mat[x][y];
        mat[x][y] = mat[y][N - 1 - x];
        mat[y][N - 1 - x] = mat[N - 1 - x][N - 1 - y];
        mat[N - 1 - x][N - 1 - y] = mat[N - 1 - y][x];
        mat[N - 1 - y][x] = temp;
      }
    }
  }

  static boolean randBool() {
    Random rn = new Random();
    return rn.nextBoolean();
  }

  public static void sop(Object msg){
    System.out.println(msg);
  }
}

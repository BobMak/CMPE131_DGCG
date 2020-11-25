package PCG;

import java.util.HashSet;

public class Coordinates {

	int x;
	int y;
	
	public Coordinates (int x, int y) {
		x = this.x;
		y = this.y;
	}
	
	@Override
	public boolean equals (Object obj) {
		Coordinates coor2 = (Coordinates)obj;
		if (this.x==coor2.x && this.y==coor2.y) {
			return true;
		}
		else return false;
	}
	
	@Override
	public int hashCode () {
		return (" " + x + y).hashCode();
	}
	
	public static void main (String[] args) {
		Coordinates coor = new Coordinates (0,0);
		Coordinates coor2 = new Coordinates (0,0);
		System.out.println(coor.hashCode());
		HashSet hs = new HashSet();
		System.out.println(hs.add(coor));
		System.out.print(hs.add(coor2));
	}
	
}

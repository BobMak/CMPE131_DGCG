
public class Coordinates {

	int x;
	int y;
	
	public Coordinates (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
		return x*31 + y;
	}
}

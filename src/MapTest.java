import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import PCG.Coordinates;

class MapTest {

	MapGenerator mapGen;
	Screen scr;
	GUI gui;
	int[][] testMap;
	HashSet<Coordinates> theOnly;	//used to testMapConnectivity
	
	@BeforeEach
	void init() {
		mapGen = new MapGenerator();
		scr = new Screen();
		gui = new GUI();
		testMap = new int[][]
			{{1,1,0},
			{1,0,0},
			{0,0,0}};
		theOnly = new HashSet();
	}
	
	//Pick the first iterated block, then recursively finds adjacent block to put into the theOnly hashset.
	//If there's any other block (found using a 2nd iteration through map) 
	//that is a '1' block and not in theOnly, 
	//map is disconnected hence invalid
	@Test
	void testMapConnectivity() {
		int initialX = -1;
		int initialY = -1;
		outerloop:
		for (int i=0; i<testMap.length; i++) {
			for (int j=0; j<testMap[0].length; j++) {
				if (testMap[i][j] == 1) {
					initialX = j;
					initialY = i;
					Coordinates initialCoor = new Coordinates(initialX,initialY);
					theOnly.add(initialCoor);
					break outerloop;
				}
			}
		}
		recForOnly (initialX, initialY);
		for (int i=0; i<testMap.length; i++) {
			for (int j=0; j<testMap[0].length; j++) {
				if (testMap[i][j]==1) //'1' block detected
				{
					Coordinates coor = new Coordinates(j,i);
					if (!theOnly.contains(coor))	//theOnly must contain this '1' block
					{
						fail("Disconnected map");
					}
				}
			}
		} 
	}
	
	//Recursively loads values for theOnly hashSet
	void recForOnly(int curX, int curY) {
		Coordinates coor = new Coordinates(curX,curY);
		theOnly.add(coor);
		for (int i=curY-1; i<curY+2; i++) {
			for (int j=curX-1; j<curX+2; j++) {
				if (i>=0 && i<testMap.length   &&   j>=0 && j<testMap[0].length) {
					if (testMap[i][j]!=0 && curX!=j && curY!=i) {
						recForOnly(j,i);
					}
				}
			}
		}
	}
	
	void addForOnly (Coordinates coor) {
		if (!theOnly.contains(coor)) theOnly.add(coor);
	}
}

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import PCG.Coordinates;
import PCG.Tunneller;

class MapTest {

	MapGenerator mapGen;
	Screen scr;
	GUI gui;
	HashSet<Coordinates> theOnly;	//used to testMapConnectivity
	
	@BeforeEach
	void init() {
		mapGen = new MapGenerator();
		scr = new Screen();
		gui = new GUI();
		theOnly = new HashSet();
	}
	
	//Pick the first iterated block, then recursively finds adjacent block to put into the theOnly hashset.
	//If there's any other block (found using a 2nd iteration through map) 
	//that is a '1' block and not in theOnly, 
	//map is disconnected hence invalid
	boolean testMapConnectivity(int[][] testMap) {
		int initialX = -1;
		int initialY = -1;
		outerloop:
		for (int i=0; i<testMap.length; i++) {
			for (int j=0; j<testMap[0].length; j++) {
				if (testMap[i][j] == 1) {
					initialX = j;
					initialY = i;
					Coordinates initialCoor = new Coordinates(initialX,initialY);
					break outerloop;
				}
			}
		}
		recForOnly (initialX, initialY,testMap);
		boolean check = true;
		outerloop2:
		for (int i=0; i<testMap.length; i++) {
			for (int j=0; j<testMap[0].length; j++) {
				if (testMap[i][j]==1) //'1' block detected
				{
					Coordinates coor = new Coordinates(j,i);
					if (!theOnly.contains(coor))	//theOnly must contain this '1' block
					{
						check = false;
						break outerloop2;
					}
					else check = true;
				}
				else check = true;
			}
		} 
		return check;
	}
	
	//Recursively loads values for theOnly hashSet
	void recForOnly(int curX, int curY, int[][] testMap) {
		Coordinates coor = new Coordinates(curX,curY);
		if(!theOnly.add(coor)) return;
		for (int i=curY-1; i<curY+2; i++) 
			for (int j=curX-1; j<curX+2; j++) 
				if (i>=0 && i<testMap.length   &&   j>=0 && j<testMap[0].length) 
					if (testMap[i][j]!=0 && (curX!=j || curY!=i)) 	
						recForOnly(j,i,testMap);	
	}
	
	@Test
	void testTunneller () {
		int[][] test = {{1,1,1},
				{1,0,0},
				{0,0,0}};
		if (testMapConnectivity(test)==false) fail("Nope");
		//Tunneller tnl = new Tunneller();
		//testMap = tnl.generate(config)
	}
}

import java.util.*;
public class Tuple{
	private ArrayList<MyButton> puzzle;
	private ArrayList<Integer> movement;
	public Tuple(ArrayList<MyButton> p, ArrayList<Integer> m) {
		puzzle = p;
		movement = m;
	}
	public ArrayList<MyButton> getPuzzle(){
		return puzzle;
	}
	public ArrayList<Integer> getMovement(){
		return movement;
	}
	public void addMovement(int m) {
		movement.add(m);
	}
	public boolean isSolved(ArrayList<MyButton> puzzle2) {
		boolean isSolved = false;
		for(int i = 0; i < puzzle.size(); i++) {
			if(puzzle.get(i).getActionCommand().equals(puzzle2.get(i).getActionCommand())) {
				isSolved = true;
			}
			else {
				isSolved = false;
				return isSolved;
			}
		}
		return isSolved;
	}
	public int findIndex(MyButton b) {
		for(int i = 0; i  < puzzle.size(); i++) 
			if(b.getActionCommand().equals(puzzle.get(i).getActionCommand()))
				return i;
		return -1;
	}
}
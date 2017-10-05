import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import javax.swing.*;

public class FifteenPuzzle extends JFrame implements ActionListener{

	private GridLayout puzzleGrid;
	private ArrayList<MyButton> buttons;
	private Container container;
	private int noMoves;
	private Stack<Integer> index;
	private int N = 16;
	private ArrayList<MyButton> solvedPuzzle;

   // set up GUI
   public FifteenPuzzle()
   {
      super("Fifteen Puzzle");
      puzzleGrid = new GridLayout(4,4,1,1);
      container = getContentPane();
      container.setLayout(puzzleGrid);
      //stack keeps track of indices of the button last clicked I.e. the previous move
      index = new Stack<Integer>();
      solvedPuzzle = new ArrayList<MyButton>();
      //set number of moves to 0
      noMoves = 0;
      setJMenuBar(addMenu());
      
      buttons = getButtons();
      setSize(145, 155);
      setVisible( true );
      
      
   } 
   private ArrayList<MyButton> getButtons(){
	  ArrayList<MyButton> buttons = new ArrayList<MyButton>(16);
	      
      for(int i = 0; i < N-1; i++ ) {
    	  	buttons.add(new MyButton(Integer.toString(i+1)));
    	  	solvedPuzzle.add(new MyButton(Integer.toString(i+1)));
    	  	buttons.get(i).addActionListener(this);
    	  	container.add(buttons.get(i));
      }
      buttons.add(new MyButton(" "));
      solvedPuzzle.add(new MyButton(" "));
      buttons.get(N-1).setEnabled(false);
      buttons.get(N-1).addActionListener(this);
      container.add(buttons.get(N-1));
      
      return buttons;
   }
@Override
   public void actionPerformed(ActionEvent e) {
	ArrayList<MyButton> positions = new ArrayList<MyButton>(4);
	MyButton left, right, top, down;
	int originalIndex = -1;
	int emptyIndex = -1;
	
	MyButton temp = (MyButton) e.getSource();
	MyButton empty = (MyButton) e.getSource();

	for(int j = 0; j < 16; j++) {
		if(buttons.get(j).getText() == " ") {
			empty = buttons.get(j);
			emptyIndex = j;
		}
		else if(buttons.get(j).equals(e.getSource())) {
			originalIndex = j;
			if(j+4 < 16 && j+4 >= 0) {
				down = buttons.get(j+4);
				positions.add(down);
			}
			if(j-4 < 16 && j-4 >= 0) {
				top = buttons.get(j-4);
				positions.add(top);
			}
			if(j-1 < 16 && j-1 >= 0 && j != 0 && j != 4 && j != 8 && j != 12) {
				left = buttons.get(j-1);
				positions.add(left);
			}
			if(j+1 < 16 && j+1 >= 0 && j != 3 && j != 7 && j != 11 && j != 15) {
				right = buttons.get(j+1);
				positions.add(right);
			}
		}
	}
	if(positions.contains(empty)) {
		swapButtons(buttons, originalIndex, emptyIndex, e.getActionCommand());
		noMoves++;
		index.add(emptyIndex);
	}
	//System.out.println("Index on stack: " + index.peek() + "\n");

}
private JMenuBar addMenu() {
	   JMenuBar menuBar;
	   JMenu menu, subActionsMenu;
	   JMenuItem aboutMenuItem, helpMenuItem, undoPrevMenuItem, undoAllMenuItem,
	   solveMenuItem, mixMenuItem;
	   
	   menuBar = new JMenuBar();
	   menu = new JMenu("More");
	   menuBar.add(menu);
	   
	   aboutMenuItem = new JMenuItem("About");
	   helpMenuItem = new JMenuItem("Help");
	   
	   //action listeners
	   aboutMenuItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	JOptionPane.showMessageDialog(null,
		    		    "Sarah Kazi\nCS 342\n9/23/2017\nProgramming Assignment "
		    		    + "2\nExtra Credit has not been attempted (yet)",
		    		    "About",
		    		    JOptionPane.PLAIN_MESSAGE);
		    }
		});
	   
	   //should describe all menu items.
	   helpMenuItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	JOptionPane.showMessageDialog(null,
		    		    "Fifteen Puzzle-can you rearrange these tiles in numeric order from 1-15? "
		    		    + "You can only move tiles to an empty tile next to it.\nMIX: "
		    		    + "rearrange the numbers in the grid into some random but solvable order\nUndo prev:"
		    		    + "put the puzzle back into the previous positioning of the values\nUndo All:"
		    		    + " restoring the puzzle to its state after its last randomization\nAuto-Solve:"
		    		    + "show the moves that will solve the puzzle with the fewest steps\n",
		    		    "Help",
		    		    JOptionPane.INFORMATION_MESSAGE);
		    }
		});
	   menu.add(aboutMenuItem);
	   menu.add(helpMenuItem);
	   menu.addSeparator();
	   subActionsMenu = new JMenu("Actions");
	   mixMenuItem = new JMenuItem("Mix");

	   undoPrevMenuItem = new JMenuItem("Undo Prev");
	   undoAllMenuItem = new JMenuItem("Undo All");
	   solveMenuItem = new JMenuItem("Auto-Solve");
	   
	   subActionsMenu.add(mixMenuItem);
	   //action listener for mix
	   mixMenuItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    		shuffle();
		    }
		});
	   subActionsMenu.add(undoPrevMenuItem);
	   
	   //undo previous action listener
	   undoPrevMenuItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    		undoPrev();
		    }
	
		});
	
	   subActionsMenu.add(undoAllMenuItem);
	   //undo all action listener
	   undoAllMenuItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    		undoAll(undoPrevMenuItem);
		    }
	   });
	   solveMenuItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	solvePuzzle();
		    }
	   });
	   
	   subActionsMenu.add(solveMenuItem);
	   
	   menu.add(subActionsMenu);	  
	   
	   return menuBar;
	   
}
	private void shuffle() {
		Collections.shuffle(buttons);
		swapButtons(buttons, 15, findEmptySpot(buttons), buttons.get(15).getActionCommand());
    		while(isSolvable() == false) {
    			Collections.shuffle(buttons);
    			swapButtons(buttons, 15, findEmptySpot(buttons), buttons.get(15).getActionCommand());
    		}
    		layoutButtons();
	}
	private void undoAll(JMenuItem undoPrev) {
		Timer t = new Timer();
		
	 	TimerTask repeatedTask = new TimerTask() {
 	        public void run() {
 	            undoPrev.doClick();
 	            if(noMoves == 0){
 	            		System.out.println("Done undoing\n");
 	            		t.cancel();
 	            }
 	        }
	 	};
 	    t.scheduleAtFixedRate(repeatedTask, 0, 1000);

	}
	private void undoPrev() {
		int origIndex, emptyIndex;
    		if(noMoves != 0) {
		    	origIndex = index.pop();
		    	emptyIndex = findEmptySpot(buttons);
		    	swapButtons(buttons, origIndex, emptyIndex, buttons.get(origIndex).getActionCommand());
		    
		    	
		    	System.out.println("orig index " + origIndex + "emptyindex " + emptyIndex + "string of button :" +  buttons.get(origIndex).getActionCommand());
		    	noMoves--;
    		} 
	}
	
   public static void main( String args[] )
   { 
      FifteenPuzzle app = new FifteenPuzzle();
      app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
   }

   
   /*method to decide if there are even or odd inversions of the array*/
   private boolean isSolvable() {
	   //array stores inversion count at index representing button position
	   int[] inversionCount = new int[16];
	   int inversionsFound = 0;
	   int count = 0;
	   //nested for loop to go through 
	   for(int i = 0; i < N; i++) {
		   if(i != findEmptySpot(buttons)) {
			   count = 0; 
			   for(int j = i+1; j < N; j++) {
				   if(j != findEmptySpot(buttons)) {
					   int compareButton = convertToInt(buttons.get(i));
					   int greaterIndex = convertToInt(buttons.get(j));
					   if(compareButton > greaterIndex)
						   inversionCount[i] = count++;
				   }
			   }
		   }
	   }
	   for(int i = 0; i < N; i++) 
		   inversionsFound+= inversionCount[i];
	   System.out.println("Inversions: "+ inversionsFound);
	   if(inversionsFound % 2 == 0)
		   return true;
	   else
		   return false;
   }
   
   public void layoutButtons() {
	   container.removeAll();
       for(MyButton b: buttons) {
    	   	b.addActionListener(this);
    	   	container.add(b);
       }
       container.revalidate();
       container.repaint();
   }
   private int findEmptySpot(ArrayList<MyButton>buttons) {
	   for(int i = 0; i < N; i++) {
		   if(buttons.get(i).isEnabled() == false) {
			   return i;
		   }
	   }
	   return -1;
   }
   private int convertToInt(MyButton b) {
	   return Integer.parseInt(b.getActionCommand());
   }
   private ArrayList<MyButton> swapButtons(ArrayList<MyButton> b, int origIndex, int emptyIndex, String origText) {
	   	b.get(origIndex).setText(" ");
		b.get(origIndex).setEnabled(false);
		b.get(emptyIndex).setText(origText);
		b.get(emptyIndex).setEnabled(true);
		return b;
   }
   private ArrayList<MyButton> getNeighbors(){
	   ArrayList<MyButton> neighbors = new ArrayList<MyButton>();
	   int emptyIndex = findEmptySpot(buttons);
	   for(int i = 0; i < N; i++) {
		   if(i == emptyIndex) {
			   if(i+4 < 16)
				   neighbors.add(buttons.get(i+4));
			   if(i-4 >= 0)
				   neighbors.add(buttons.get(i-4));
			   if(i-1 >= 0 && i != 0 && i != 4 && i != 8 && i != 12)
				   neighbors.add(buttons.get(i-1));
			   if(i+1 < 16 && i != 3 && i != 7 && i != 11 && i != 15)
				   neighbors.add(buttons.get(i+1));
		   }
	   }
//	   System.out.println("Neighbors: ");
//	   for(int i = 0; i < neighbors.size(); i++) {
//		   System.out.println(neighbors.get(i).getActionCommand() + "\n");
//	   }
	   return neighbors;
   }
   @SuppressWarnings("unused")
private int findIndex(ArrayList<MyButton> puzzle, MyButton b) {
	   for(int i = 0; i < N; i++) {
		   if(b.getActionCommand().equals(puzzle.get(i).getActionCommand())) {
			   return i;
		   }
	   }
	   return -1;
   }
   private void solvePuzzle() {
	   
	   
   }



} // end class ButtonTest




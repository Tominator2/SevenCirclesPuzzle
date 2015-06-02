import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;
import java.awt.Color;

/*
Print layout like this in HTML using a fixed-width font,
a gray background and colour tags.
           _____             
          /  O  \
         /O     O\
   _____/    0    \_____
  /  O  \         /  O  \
 /O     O\O     O/O     O\
/    5    \__O__/    1    \
\         /  O  \         /
 \O     O/O     O\O     O/
  \__O__/    6    \__O__/
  /  O  \         /  O  \
 /O     O\O     O/O     O\ 
/    4    \__O__/    2    \
\         /  O  \         /
 \O     O/O     O\O     O/
  \__O__/    3    \__O__/
        \         /
         \O     O/
          \__O__/

 */

public class Puzzle {


    private int tries = 0;
    
    // command line flags
    private static boolean debug       = false;
    private static boolean htmlOutput = false;

    // create the 7 puzzle circles
    public Circle[] circles = {new Circle(new Color[] {
		                          Color.RED,Color.WHITE,Color.BLUE,
					  Color.YELLOW,Color.GREEN,Color.BLACK}),
			       new Circle(new Color[] {
				          Color.RED,Color.BLACK,Color.WHITE,
					  Color.GREEN,Color.YELLOW,Color.BLUE}),
			       new Circle(new Color[] {
				          Color.RED,Color.WHITE,Color.BLUE,
					  Color.BLACK,Color.YELLOW,Color.GREEN}),
			       new Circle(new Color[] {
				          Color.RED,Color.BLACK,Color.YELLOW,
					  Color.WHITE,Color.BLUE,Color.GREEN}),
			       new Circle(new Color[] {
				          Color.RED,Color.GREEN,Color.YELLOW,
					  Color.BLACK,Color.BLUE,Color.WHITE}),
			       new Circle(new Color[] {
				          Color.RED,Color.WHITE,Color.BLACK,
					  Color.GREEN,Color.BLUE,Color.YELLOW}),
			       new Circle(new Color[] {
				          Color.RED,Color.WHITE,Color.GREEN,
					  Color.BLACK,Color.BLUE,Color.YELLOW})
     };

    private Circle layout[] = new Circle[7]; 


    public Puzzle(){

    }


    public void debug(String message) {
	if (debug)
	    System.out.println(message);
    }


    // Solve the puzzle. Start by trying each circle in the center position
    public void solve(){
	// add first circle to the center position (6)
	for (int i = 0; i < circles.length; i = i + 1) {

	    // clear layout
	    for (int j = 0; j < layout.length; j = j + 1) {
		layout[j] = null;
	    }
	    layout[6] = circles[i];
	    layout[6].resetOffset();
	    tries = tries + 1;

	    // add a second loop to try other circles in first position (position '0')
	    for (int j = 1; j < (circles.length); j = j + 1) {
		layout[0] = circles[(i + j)%7];
		layout[0].rotateColourTo(layout[6].getColourAt(0),3);
		tries = tries + 1;
		//printCircles();

		// create a vector containing the remaining circles to try
		// add all the circles to the vector
		Vector remainingCircles = new Vector();
		for (int k = 0; k < circles.length; k = k + 1) {
		    remainingCircles.add(circles[k]);
		}
		remainingCircles.remove(layout[6]); // remove center circle
		remainingCircles.remove(layout[0]); // remove circle in position 0
	        //debug("Remaining circles = " + remainingCircles.size());

		tryPosition(1, remainingCircles);

	    }
	}
    }
    

    // Add a circle at this position (if there is a suitable circle) and then recursively 
    // try the next circle position. 
    public void tryPosition(int position, Vector availableCircles) {

	// If we are trying to add a circle to position 6 then the others have all been 
	// filled so this is the solution!
	if (position == 6) {
	    debug("\n*** SOLUTION FOUND! ***");
	    printLayout();
	    return;
	}

	tries = tries + 1;

	debug("Finding circle for position " + position + ", " + 
	      availableCircles.size() + " circle(s) available."); 

	//if (position >= 5) {
	//    printCircles();
	//}

	Vector possibleCircles = (Vector) availableCircles.clone();

	// find matching colours
	Color centerMatch = layout[6].getColourOpp((position + 3)%6);
	//debug ("Matching position " + position + 
	//       " with center colour: " + Circle.colourToStr(centerMatch));
	Color lastMatch   = layout[position - 1].getColourOpp((position + 4)%6);
	//debug ("Matching position " + position + 
	//       " with previous circle's colour: " + Circle.colourToStr(lastMatch));

	Color nextMatch = null;
	// need to match circle in position 5 with the center, positon 4 and 
	// position 0
	if (position == 5) {
	    nextMatch = layout[0].getColourOpp((position + 2)%6);
	//debug ("Matching position " + position + 
	//       " with next circle's colour: " + Circle.colourToStr(nextMatch));
	}

	// find circles that are a possible match for this position
	returnMatches(possibleCircles, nextMatch, centerMatch, lastMatch);
	debug(possibleCircles.size() +  " possible circle(s) for position " + 
	      position);
	if (possibleCircles.size() == 0) {
	    debug("No matching circles\n");
	    return;
	}

	// try the possible matching circles
	Iterator e = possibleCircles.iterator();
	while(e.hasNext()){
	    Circle tryCircle = (Circle) e.next();  

	    // rotate it to match center circle and add it to the layout
	    tryCircle.rotateColourTo(centerMatch, (position + 3)%6);
	    layout[position] = tryCircle;

	    // remove it from the available circle list
	    availableCircles.remove(tryCircle);
	    tryPosition(position + 1, availableCircles);
   
	    // remove it from the layout
	    layout[position] = null;
	    
	    // add it back to the available circle list
	    availableCircles.add(tryCircle);
	}

    }


    // Remove any circles that don't match these colours
    public void returnMatches(Vector circles, Color c1, Color c2, Color c3) {

	// each circle contains 6 unique colours - if we have any patterns with
	// repeated colours then there will be no matches!
	if (c1 == c2 || c1 == c3 || c2 == c3) {
	    circles.clear();
	    //return;
	}

	Iterator e = circles.iterator();
	while(e.hasNext()){
	    Circle thisCircle = (Circle) e.next();  

	    Boolean found = false;
	    // check 
	    for (int i = 0; i < 6; i = i + 1) {

		if (c1 == null) {  // 2 colour match
		    if (thisCircle.getColourAt(i) == c2 && 
			thisCircle.getColourAt((i+1)%6) == c3) {
			found = true;
		    }
		}
		else { // 3 colour match
		    if (thisCircle.getColourAt(i) == c1 && 
			thisCircle.getColourAt((i+1)%6) == c2 && 
			thisCircle.getColourAt((i+2)%6) == c3) {
			found = true;
		    }
		}
	    }
	    if (!found)
		e.remove();
	}
	debug ("Matching for " + Circle.colourToStr(c1) + Circle.colourToStr(c2) + 
	       Circle.colourToStr(c3) + " leaves " + circles.size() + " circle(s).");
	
    }
    
    
    // Helper method for printing HTML colour tags
    public String dot(int layoutPosition, int colourPosition) {
	Color dotColour= layout[layoutPosition].getColourAt(colourPosition);

	if (htmlOutput) {
	    return "<span style=\"color: rgb(" + dotColour.getRed() + "," + 
		dotColour.getGreen() + "," + dotColour.getBlue() + ");\">@</span>";
	}
	else {
	    return Circle.colourToStr(dotColour);
	}
    }


    // Print the solution as text or HTML
    public void printLayout(){
    	
    	// Add HTML header
	if (htmlOutput) {  
	System.out.println("<html>");
	System.out.println("<body bgcolor=\"aaaaaa\">");
	System.out.println("<h2>Solution for the Seven Circles Puzzle</h2>");
	System.out.println("<pre>");
	System.out.println("<code>");
	}
	
	System.out.println("           _____ ");            
	System.out.println("          /  " + dot(0,0) + "  \\"); 
	System.out.println("         /" + dot(0,5) + "     " + dot(0,1) + "\\"); 
	System.out.println("   _____/    0    \\_____"); 
	System.out.println("  /  " + dot(5,0) + "  \\         /  " + dot(1,0) + "  \\"); 
	System.out.println(" /" + dot(5,5) + "     " + dot(5,1) + "\\" + dot(0,4) + "     " + dot(0,2) + "/" + dot(1,5) + "     " + dot(1,1) + "\\"); 
	System.out.println("/    5    \\__" + dot(0,3) + "__/    1    \\"); 
        System.out.println("\\         /  " + dot(6,0) + "  \\         /"); 
	System.out.println(" \\" + dot(5,4) + "     " + dot(5,2) + "/" + dot(6,5) + "     " + dot(6,1) + "\\" + dot(1,4) + "     " + dot(1,2) + "/"); 
	System.out.println("  \\__" + dot(5,3) + "__/    6    \\__" + dot(1,3) + "__/"); 
	System.out.println("  /  " + dot(4,0) + "  \\         /  " + dot(2,0) + "  \\"); 
	System.out.println(" /" + dot(4,5) + "     " + dot(4,1) + "\\" + dot(6,4) + "     " + dot(6,2) + "/" + dot(2,5) + "     " + dot(2,1) + "\\ "); 
	System.out.println("/    4    \\__" + dot(6,3) + "__/    2    \\"); 
	System.out.println("\\         /  " + dot(3,0) + "  \\         /"); 
	System.out.println(" \\" + dot(4,4) + "     " + dot(4,2) + "/" + dot(3,5) + "     " + dot(3,1) + "\\" + dot(2,4) + "     " + dot(2,2) + "/"); 
	System.out.println("  \\__" + dot(4,3) + "__/    3    \\__" + dot(2,3) + "__/"); 
	System.out.println("        \\         /"); 
	System.out.println("         \\" + dot(3,4) + "     " + dot(3,2) + "/"); 
	System.out.println("          \\__" + dot(3,3) + "__/"); 

 	// Add HTML footer
	if (htmlOutput) {
	    System.out.println("</code>");
	    System.out.println("</pre>");	
	    System.out.println("</body>");	
	    System.out.println("</html>");
	}
	else {
	    System.out.println("\nKEY: 'R' = Red, 'G' = Green, 'B' = Blue,");
	    System.out.println("     'Y' = Yellow, 'K' = Black, and 'W' = White.\n");
	}
    }


    // Print individual circles with position information
    public void printCircles() {

	System.out.println("\nPosition 6 (center) =");
	System.out.print(layout[6]);

	for (int i = 0; i < (layout.length  - 1); i = i + 1) {
	    if (layout[i] != null){
		System.out.println("Position " + i + " =");
		System.out.print(layout[i]);
	    } 
	}

    }


    public static void main(String args[]){

	// check command line flags
	if (args.length == 1) {
	    if (args[0].equals("-d")){
		debug = true;
	    }
	    else if (args[0].equals("-h")){
		htmlOutput = true;
	    }
	}

	Puzzle puzzle = new Puzzle();

	puzzle.solve();
	puzzle.debug("\n" + puzzle.tries + " tries!");

    }

}

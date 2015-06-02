import java.awt.Color;

/* This class implements a single puzzle circle with 6 colours
   corresponding to the clock positions 12, 1:30, 4:30, 6, 7:30, & 10:30, 
   which are numbered starting at 12 o'clock  as 0..5 respectively:
     _______
    /   0   \     
   /5       1\
  (     N     )
   \4       2/
    \___3___/  

*/

public class Circle {


    private Color[] dot;
    private int     offset;


    // this constructor expects an array of 6 colours with Red at 12 o'clock
    // we chould check the length and throw an exception if there are not 
    // enough colours
    public Circle(Color[] dotColours) {

	dot = dotColours;

    }


    // Adjust the offset to rotate this colour to the desired position
    public void rotateColourTo(Color color, int newPosition){

	if (newPosition < 0 || newPosition > 5){
	    System.out.println("Position must be between 0..5 : " + newPosition);
	    System.exit(-1);	
	}

	int currentPosition = -1;

	// do we need to check if we are already in this position?

	for (int i = 0; i < 6; i = i + 1) {
	    if (dot[i] == color)
		currentPosition = i;
	}
	
	if (currentPosition == -1){
	    System.out.println("Could not rotate circle to this colour: " + color);
	    System.exit(-1);	
	}

	offset = currentPosition - newPosition;

    }


    // Returns the circle to its original rotation
    public void resetOffset(){
	offset = 0;
    }


    // Return the dot colour of this position
    public Color getColourAt(int position){
	return getDot(position);
    }


    // Returns the colour of the dot in this position allowing for
    // rotation
    public Color getDot(int position) {

	if (position < 0 || position > 5){
	    System.out.println("Position must be between 0..5 : " + position);
	    System.exit(-1);	
	}

	int rotatedPosition = (position + offset)%6;  
	if (rotatedPosition < 0)
	    rotatedPosition = rotatedPosition + 6;
	return dot[rotatedPosition];
    }


    // Return the dot colour diagonally opposite this position
    public Color getColourOpp(int position){
	return getDot((position + 3)%6);
    }


    public static String colourToStr(Color col){

	if (col == Color.RED) 
	    return "R";
	else if (col == Color.GREEN)
	    return "G";
	else if (col == Color.BLUE)
	    return "B";
	else if (col == Color.YELLOW)
	    return "Y";
	else if (col == Color.WHITE)
	    return "W";
	else if (col == Color.BLACK)
	    return "K";
	else
	    return "?";

    }


    public String toString() {
	String s = "";
	s = s + "   _____\n";
	s = s + "  /  " + colourToStr(getDot(0)) + "  \\ \n";
	s = s + " /" + colourToStr(getDot(5)) + "     " + colourToStr(getDot(1)) + "\\ \n";
	s = s + "/         \\ \n";
	s = s + "\\         /\n";
	s = s + " \\" + colourToStr(getDot(4)) + "     " + colourToStr(getDot(2)) + "/ \n";
	s = s + "  \\__" + colourToStr(getDot(3)) + "__/\n";
	s = s + "\n";
	return s;
    }


    public static void main(String args[]){

	//Circle circle1 = new Circle();
	Circle circle2 = new Circle(new Color[] {
		Color.RED,Color.WHITE,Color.BLUE,
		Color.YELLOW,Color.GREEN,Color.BLACK});
	for (int i = 0; i < 6; i = i + 1) {
	    System.out.println("Position " + i + " is " + 
			       colourToStr(circle2.getColourAt(i)) + 
			       ", opposite is " + 
			       colourToStr(circle2.getColourOpp(i)));
	}

	System.out.print(circle2);

	for (int i = 0; i < 6; i = i + 1) {
	    System.out.println("Rotate R to position " + i);
	    circle2.rotateColourTo(Color.RED,i);
	    System.out.print(circle2);
	}

	System.out.println("Reset position");
	circle2.resetOffset();
	System.out.print(circle2);

	System.out.println("Rotate Blue to position 0");
	circle2.rotateColourTo(Color.BLUE,0);
	System.out.println("Position 0 is " + 
			   colourToStr(circle2.getColourAt(0)) + 
			   ", opposite is " + 
			   colourToStr(circle2.getColourOpp(0)));
	System.out.print(circle2);
	
    }


}

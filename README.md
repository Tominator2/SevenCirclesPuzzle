# SevenCirclesPuzzle

This is some code I wrote back in 2012 to solve a wooden puzzle game that we bought here in Thailand.  The puzzle consists of seven circles and each circle has six coloured dots around the outside (see the image below).

![circlepuzzle](https://cloud.githubusercontent.com/assets/4344677/7930702/c8b89d76-0934-11e5-8fc9-ee62bc152b38.jpg)

To solve the puzzle you must place all seven circles into the frame and rotate them so that adjacent dots are of the same colour.

## Code

The Java code consists of a [Circle class](https://github.com/Tominator2/SevenCirclesPuzzle/blob/master/Circle.java) which is used to represent the individual circles and a [Puzzle class](https://github.com/Tominator2/SevenCirclesPuzzle/blob/master/Puzzle.java) which models the frame and searches for [the solution](https://github.com/Tominator2/SevenCirclesPuzzle/blob/master/solution.txt).

The `solve()` method in [Puzzle.java](https://github.com/Tominator2/SevenCirclesPuzzle/blob/master/Puzzle.java) works by trying each of the circles in turn at the center of the frame and then recursively adding possible circles to the other positions starting at 12 o'clock.

The program also accepts two command line flags:

  `-d` prints out some debugging information while the code runs
  
  `-h` outputs a [colour solution in HTML format](https://github.com/Tominator2/SevenCirclesPuzzle/blob/master/out.html) which you can redirect to a file, e.g.:
  
       javac Puzzle -h > out.html







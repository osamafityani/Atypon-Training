import stanford.karel.SuperKarel;

/**
 * Level 1: Put beepers only in the odd outside spots (e.g. 1x2, 2x1 are considered odd), then print the number how many you've put, and then
 * collect them all (do not duplicate beepers)
 * Level 2: Put beepers on all even spots, then print the number how many you've put, and finally collect them all
 * Level 3: Divide the map (using beepers) into 2 or 4 equal chambers (rectangles surrounded by walls or beepers),
 * depending on the map; see solution in 7x7; please note that you cannot put duplicate beepers.
 * Make sure to clean the map and print how many beepers you've put, then collect them all.
 *
 * 
 */
public class Homework extends SuperKarel {

    public void run() {
        setBeepersInBag(500);
        level1();
        level2();
        level3();
    }

    private void level1() {
        System.out.println("Mission one initiated...");
        int beepersDropped = 0;
        cleanMap();
        beepersDropped += fillOddOut();
        System.out.println(beepersDropped + " Beepers dropped in Level 1");
        System.out.println("Mission one accomplished");
        System.out.println("****************************************");
        cleanMap();
    }
    private void level2(){
        System.out.println("Mission two initiated...");
        int beepersDropped = fillEven();
        System.out.println(beepersDropped + " Beepers dropped in Level 2");
        cleanMap();
        System.out.println("Mission two accomplished");
        System.out.println("****************************************");
    }
    private void level3(){
        int beepersDropped = 0;
        System.out.println("Mission three initiated...");
        System.out.println("Finding this world's dimensions...");
        int[] dimensions = get_dimensions();
        System.out.println(dimensions[0] + ", " + dimensions[1]);
        goHome();
        if (!frontIsClear()) {
            turnLeft();
        }
        // Using divide two times to divide the map into four identical regions if possible.
        beepersDropped += divide(dimensions);
        beepersDropped += divide(dimensions);
        System.out.println(beepersDropped + " Beepers dropped in Level 3");
        goHome();
        cleanMap();
        goHome();
        System.out.println("Mission three accomplished");
        System.out.println("****************************************");
    }
    private void goHome() {
        System.out.println("Going home...");
        while(true) {
            if (facingWest()) {
                moveWall(false, false,1);
                turnLeft();
                moveWall(false, false, 1);
                turnLeft();
                break;
            } else {
                turnLeft();
            }
        }
    }
    private void cleanMap(){
        // In order for this function to work properly, robot needs to be in base.
        System.out.println("Cleaning the map, picking all beepers...");
        boolean finished = true;
        while(finished) {
            moveWall(true, false,1);
            finished = rowUp();
        }
        goHome();  // robot will go back to base after cleaning the entire map.
    }
    private boolean rowUp(){
        // shifts the robot's position one row upwards. The robot will be in the opposite direction of the original one.
        if(facingEast()) {
            turnLeft();
            if (frontIsClear()) {
                move();
                turnLeft();
                return true;
            } else {
                turnLeft();
                return false;
            }
        }
        else{
            turnRight();
            if (frontIsClear()) {
                move();
                turnRight();
                return true;
            } else {
                turnRight();
                return false;
            }
        }
    }
    private int[] moveWall(boolean pick, boolean put, int steps){
        /* Moves unitl it hits the wall. If steps is not set to 0, it will move the number of steps specified.
         * pick: will pick all beepers in its way if true.
         * put: will put beepers at every corner it passes if true.
         *
         * It returns a report for the journey, which includes the number of steps moved as well as dropped beepers.
         */
        int count = 1;
        int dropped = 0;
        while(frontIsClear()){
            if(pick && beepersPresent()){
                pickBeeper();
            }
            if(put && noBeepersPresent()){
                putBeeper();
                dropped++;
            }
            move();
            count++;
            if(steps > 0 && count == steps)
                break;
        }
        if(pick && beepersPresent()) {
            pickBeeper();
        }
        if(put && noBeepersPresent()){
            putBeeper();
            dropped++;
        }
        return new int[]{count, dropped};  // creating and returning the report for this journey.
    }
    private int divide(int[] dimensions) {
        // splits any axis(line) into two identical parts
        int majorAxis;  // Axis that we need to divide
        int minorAxis;  // Axis Orthogonal to the majorAxis
        int[] moveReport;
        int dropped = 0;
        if (facingEast()){
            majorAxis = dimensions[0];
            minorAxis = dimensions[1];
        }else{
            majorAxis = dimensions[1];
            minorAxis = dimensions[0];
        }

        if (majorAxis > 2){
        if (frontIsClear()) {
            moveWall(false,false, majorAxis / 2 + 1);
            turnLeft();
            moveReport = moveWall(false, true, minorAxis);
            int steps_moved = moveReport[0];
            dropped += moveReport[1];
            if(majorAxis % 2 == 0){
                turnLeft();
                move();
                turnLeft();
                moveReport = moveWall(false, true, minorAxis);
                dropped += moveReport[1];
                if(steps_moved<minorAxis && facingWest()){
                    turnAround();
                    moveReport = moveWall(false, true, minorAxis);
                    dropped += moveReport[1];
                }else if (steps_moved<minorAxis && facingEast()){
                    rowUp();
                    moveReport = moveWall(false, true, minorAxis/2);
                    dropped += moveReport[1];
                }
            }else turnAround();
            moveReport = moveWall(false, true, minorAxis);
            dropped += moveReport[1];
        }
        turnAround();
        return dropped;
    }else{
            moveWall(false, false, 0);
            turnLeft();
            return 0;
        }

    }
    private int[] get_dimensions() {
        int[] dimensions = new int[2];
        int steps;
        int[] moveReport;
        moveReport = moveWall(false, false, 0);
        steps = moveReport[0];
        dimensions[0] = steps;
        turnLeft();
        moveReport = moveWall(false, false, 0);
        steps = moveReport[0];
        dimensions[1] = steps;

        return dimensions;
    }
    private int fillOddOut() {
        int dropped = 0;
        int count = 0;

        if (frontIsClear()) {
            move();
        } else {
            turnLeft();
            if (frontIsClear()) {
                move();
            }
        }
        for (int i = 0; i < 4; i++) {
            while (frontIsClear()) {
                if (noBeepersPresent() && (count % 2 == 0)) {
                    putBeeper();
                    dropped++;
                }
                move();
                count++;
            }
            turnLeft();
        }
        goHome();
        return dropped;
    }
    private int fillEven(){
        int dropped = 0;
        int count = 0;
        boolean notFinished = true;

        if(frontIsBlocked()){turnLeft();}
        while(notFinished){
            while(frontIsClear()) {
                if (noBeepersPresent() && (count % 2 == 0)) {
                    putBeeper();
                    dropped++;
                }
                move();
                count++;
            }
            if (noBeepersPresent() && (count % 2 == 0)) {
                putBeeper();
                dropped++;
            }
            notFinished = rowUp();
            count++;

        }
        goHome();
        return dropped;
    }
}
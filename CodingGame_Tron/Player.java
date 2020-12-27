import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

class Robot {
    private int initX;
    private int initY;
    private int curX;
    private int curY;
    private List<ArrayList> tailPositions;

    Robot() {
        this.initX = 0;
        this.initY = 0;
        this.curX = 0;
        this.curY = 0;
    }

    Robot (int x0, int y0, int x1, int y1) {
        this.initX = x0;
        this.initY = y0;
        this.curX = x1;
        this.curY = y1;
    }

    // ------------------------------ Coordonates
    public int getX() {
        return this.curX;
    }

    public int getY() {
        return this.curY;
    }

    public void setX(int x) {
        this.curX = x;
    }

    public void setY(int y) {
        this.curY = y;
    }

    public int[] getPosition() {
        int[] position = {this.curX, this.curY}; 
        return position;
    }

    public void setPosition(int x1, int y1) {
        this.curX = x1;
        this.curY = y1;
    }

    public void setPosition(int x0, int y0, int x1, int y1) {
        this.initX = x0;
        this.initY = y0;
        this.curX = x1;
        this.curY = y1;
    }

    // ------------------------------ Distances
    public int getDistanceFromX(int x) {
        return Math.abs(this.curX - x);
    }

    public int getDistanceFromY(int y) {
        return Math.abs(this.curY - y);
    }

    public int getDistanceFromInitX() {
        return Math.abs(this.initX - this.curX);
    }

    public int getDistanceFromInitY() {
        return Math.abs(this.initY - this.curY);
    }


    @Override
    public String toString() {
        return (" initX = " + initX + "\n initY = " + initY + "\n curX = " + curX + "\n curY = " + curY);
    }
}

class OpponentRobot extends Robot {
   
    OpponentRobot() {
        super();
    }

    OpponentRobot(int x0, int y0, int x1, int y1) {
        super (x0, y0, x1, y1);
    }

}

class PlayerRobot extends Robot {
    
    PlayerRobot() {
        super();
    }

    PlayerRobot(int x0, int y0, int x1, int y1) {
        super(x0, y0, x1, y1);
    }

    public String move() {
        return "LEFT";
    }
}


class Player {
    public static void main(String args[]) {
        // Robots
        PlayerRobot player = null;
        HashMap<Integer, Robot> opponentsMap = new HashMap<Integer, Robot>();

        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int N = in.nextInt(); // total number of players (2 to 4).
            int P = in.nextInt(); // your player number (0 to 3).
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                
                if (i == P) {
                    if (player == null) {
                        player = new PlayerRobot(X0,Y0,X1,Y1);
                    } else {
                        player.setPosition(X1,Y1);
                    }
                } else {
                    if (opponentsMap.size() < N - 1) {
                        Robot opponent = new OpponentRobot(X0,Y0,X1,Y1);
                        opponentsMap.put(i, opponent); 
                    } else {
                        Robot opponent = opponentsMap.get(i);
                        opponent.setPosition(X1, Y1);
                        opponentsMap.put(i, opponent);    
                    }
                }
            }

            // DEBUG
            System.err.println("Player :");
            System.err.println(player);
            System.err.println("Opponent :");
            System.err.println(opponentsMap);
            

            // ACTION
            System.out.println(player.move()); // A single line with UP, DOWN, LEFT or RIGHT
        }
    }
}
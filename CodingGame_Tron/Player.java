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
    Robot(int x0, int y0) {
        this.initX = x0;
        this.initY = y0;
    }

    public int getDistanceX() {
        return Math.abs(this.initX - this.curX);
    }
    public int getDistanceY() {
        return Math.abs(this.initY - this.curY);
    }

    public void setPositions(int x1, int y1) {
        this.curX = x1;
        this.curY = y1;
    }

    public void setPositions(int x0, int y0, int x1, int y1) {
        this.initX = x0;
        this.initY = y0;
        this.curX = x1;
        this.curY = y1;
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

}

class PlayerRobot extends Robot {
    
    PlayerRobot() {
        super();
    }

        
}


class Player {
    public static void main(String args[]) {
        
        Robot playerRobot = new PlayerRobot();
        Robot opponentRobot = new OpponentRobot();

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
                    playerRobot.setPositions(X0,Y0,X1,Y1);
                } else {
                    opponentRobot.setPositions(X0, Y0, X1, Y1);
                }
            }

            // DEBUG
            System.err.println("Player :");
            System.err.println(playerRobot);
            System.err.println("Opponent :");
            System.err.println(opponentRobot);
            


            // ACTION
            System.out.println("UP"); // A single line with UP, DOWN, LEFT or RIGHT
        }
    }
}
import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

class Robot {
    private int initX;
    private int initY;
    private int curX;
    private int curY;

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
    public int getX () {
        return this.curX;
    }

    public int getY () {
        return this.curY;
    }

    public void setX (int x) {
        this.curX = x;
    }

    public void setY (int y) {
        this.curY = y;
    }

    public int[] getPosition () {
        int[] position = {this.curX, this.curY}; 
        return position;
    }

    public void setPosition (int x1, int y1) {
        this.curX = x1;
        this.curY = y1;
    }

    public void setPosition (int x0, int y0, int x1, int y1) {
        this.initX = x0;
        this.initY = y0;
        this.curX = x1;
        this.curY = y1;
    }

    // ------------------------------ Distances
    public int getDistanceFromX (int x) {
        return Math.abs(this.curX - x);
    }

    public int getDistanceFromY (int y) {
        return Math.abs(this.curY - y);
    }

    public int getDistanceFromInitX () {
        return Math.abs(this.initX - this.curX);
    }

    public int getDistanceFromInitY () {
        return Math.abs(this.initY - this.curY);
    }


    @Override
    public String toString() {
        return (" initX = " + initX + "\n initY = " + initY + "\n curX = " + curX + "\n curY = " + curY);
    }
}

class OpponentRobot extends Robot {
   
    OpponentRobot () {
        super();
    }

    OpponentRobot (int x0, int y0, int x1, int y1) {
        super (x0, y0, x1, y1);
    }

}

class PlayerRobot extends Robot {
    
    PlayerRobot () {
        super();
    }

    PlayerRobot (int x0, int y0, int x1, int y1) {
        super(x0, y0, x1, y1);
    }

    public String move() {
        return "LEFT";
    }
}

class Grid {
    private int x;
    private int y;
    private int[][] map;

    Grid () {
        this.x = 30;
        this.y = 20;
        createMap(this.x, this.y);
    }

    Grid (int x, int y) {
        this.x = x;
        this.y = y;
        createMap(x, y);
    }

    public void createMap (int x, int y) {
        this.map = new int[x][y];
        for (int i=0; i < x; i++) {
            for (int j=0; j < y; j++) {
                this.map[i][j] = 0;
            }
        }
    }

    public ArrayList<Integer[]> getLinesPositions() {
        ArrayList<Integer[]> positions = new ArrayList<Integer[]>();
        for(int x = 0; x < this.x; x++) {
            for(int y = 0; y < this.y; y++) {
                if (this.map[x][y] > 0) {
                    Integer[] pos = {x, y};
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    public ArrayList<ArrayList<Integer>> getRobotLinePositions(int robotId) {
        ArrayList<ArrayList<Integer>> positions = new ArrayList<ArrayList<Integer>>();
        for(int x = 0; x < this.x; x++) {
            for(int y = 0; y < this.y; y++) {
                if (this.map[x][y] == robotId) {
                    ArrayList<Integer> pos = new ArrayList<Integer>(Arrays.asList(x, y));
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    public void setLinePosition (int x, int y, int player) {
        // player = 1 if player, 2 if opponent
        this.map[x][y] = player;
    }

    public Boolean isWall(int x, int y) {
        if(x < 0 || x >= this.x || y < 0 || y >= this.y )
            return true;
        return false;
    }

    public Boolean isLine(int x, int y) {
        if (this.map[x][y] > 0) return true;
        return false;
    }

    @Override
    public String toString() {
        String mapStr = "";
        for(int j = 0 ; j < this.y ; j++) {
			for (int i = 0 ; i < this.x ; i++)
                mapStr += this.map[i][j];
            mapStr += "\n";
		}
        return mapStr;
    }

}

class Player {
    // Robots
    static PlayerRobot player = null;
    static HashMap<Integer, Robot> opponentsMap = new HashMap<Integer, Robot>();
    static Grid map = new Grid();

    public static Boolean isCorrectMove(int x, int y) {
        if (map.isWall(x, y)) return false;
        else if (map.isLine(x,y)) return false;
        else return true;
    }

    public static String moveToMake(PlayerRobot player, HashMap<Integer, Robot> opponentsMap, Grid map) {
        String[] moves = {"UP",  "DOWN", "LEFT", "RIGHT"};
        String moveToMake = "";
        int curX = player.getX();
        int curY = player.getY();
        int nextX = -1;
        int nextY = -1;

        int i = 0;
        while (!isCorrectMove(nextX, nextY) && i < moves.length) {
            switch(moves[i]) {
                case "UP":
                    nextX = curX;
                    nextY  = curY - 1;
                    break;
                case "DOWN":
                    nextX = curX;
                    nextY  = curY + 1;
                    break;
                case "LEFT":
                    nextX = curX - 1;
                    nextY = curY;
                    break;
                case "RIGHT":
                    nextX = curX + 1;
                    nextY = curY;
                    break;
                default:
                    return "UP";
            }
            moveToMake = moves[i];
            i++;
        }
        System.err.println("curPosition : " + "[" + curX + "," + curY + "]");
        System.err.println("nextPosition : " + "[" + nextX + "," + nextY + "]");
        return moveToMake;
    }
    
    public static void main(String args[]) {
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
                        map.setLinePosition(X0, Y0, 1);
                        
                    } else {
                        player.setPosition(X1,Y1);
                    }
                    map.setLinePosition(X1, Y1, 1);
                } else {
                    if (opponentsMap.size() < N - 1) {
                        Robot opponent = new OpponentRobot(X0,Y0,X1,Y1);
                        opponentsMap.put(i, opponent); 
                        map.setLinePosition(X0, Y0, 2);
                    } else {
                        Robot opponent = opponentsMap.get(i);
                        opponent.setPosition(X1, Y1);
                        opponentsMap.put(i, opponent);    
                    }
                    map.setLinePosition(X1, Y1, 2);
                }
            }

            // DEBUG
            // System.err.println("Player :");
            // System.err.println(player);
            // System.err.println("Opponent :");
            // System.err.println(opponentsMap);
           

            // ACTION
            String move = moveToMake(player, opponentsMap, map);
            System.out.println(move);
        }
    }
}
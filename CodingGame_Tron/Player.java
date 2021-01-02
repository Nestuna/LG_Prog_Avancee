import java.util.*;

import javax.sound.midi.SysexMessage;
/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

class Robot {
    private Boolean isOpponent;
    private Position initPosition; 
    private Position currentPosition;

    Robot (int x0, int y0, int x1, int y1) {
        this.initPosition = new Position(x0, y0) ;
        this.currentPosition = new Position(x1, y1);
    }

    // ------------------------------ Coordonates
    public Position getInitPosition () {
        return this.initPosition;
    }

    public Position getPosition () {
        return this.currentPosition;
    }

    public void setX (int x) {
        this.currentPosition.setX(x);
    }

    public void setY (int y) {
        this.currentPosition.setY(y);
    }

    public void setPosition (int x1, int y1) {
        this.currentPosition.setX(x1);
        this.currentPosition.setY(y1);
    }

    // ------------------------------ Distances
    public int getDistanceFromX (int x) {
        return Math.abs(this.currentPosition.getX() - x);
    }

    public int getDistanceFromY (int y) {
        return Math.abs(this.currentPosition.getY() - y);
    }

    public int getDistanceFromInitX () {
        return Math.abs(this.initPosition.getX() - this.currentPosition.getX());
    }

    public int getDistanceFromInitY () {
        return Math.abs(this.initPosition.getY() - this.currentPosition.getY());
    }


    @Override
    public String toString() {
        String robotStatus = isOpponent ? "Oppennent" : "Player";
        return robotStatus + ":\n" + "\t Init = " + this.initPosition.toString() + "\n\t Current = " + this.currentPosition.toString();
    }
}

class OpponentRobot extends Robot {

    OpponentRobot (int x0, int y0, int x1, int y1) {
        super (x0, y0, x1, y1);
    }

}

class PlayerRobot extends Robot {

    PlayerRobot (int x0, int y0, int x1, int y1) {
        super(x0, y0, x1, y1);
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
        createMap(this.x, this.y);
    }

    Grid (Position position) {
        this.x = position.getX();
        this.y = position.getY();
        createMap(this.x, this.y);
    }


    public void createMap (int x, int y) {
        this.map = new int[x][y];
        for (int i=0; i < x; i++) {
            for (int j=0; j < y; j++) {
                this.map[i][j] = 0;
            }
        }
    }

    public ArrayList<Position> getLinesPositions() {
        ArrayList<Position> positions = new ArrayList<Position>();
        for(int x = 0; x < this.x; x++) {
            for(int y = 0; y < this.y; y++) {
                if (this.map[x][y] > 0) {
                    Position pos = new Position(x, y);
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

    public void setLinePosition (Position position, int player) {
        x = position.getX();
        y = position.getY();
        // player = 1 if player, 2 if opponent
        this.map[x][y] = player;
    }

    public Boolean isWall(int x, int y) {
        if(x < 0 || x >= this.x || y < 0 || y >= this.y)
            return true;
        return false;
    }

    public Boolean isWall(Position position) {
        int x = position.getX();
        int y = position.getY();
        if(x < 0 || x >= this.x || y < 0 || y >= this.y ) {
            System.err.println("Wall !");
            return true;
        }
        return false;
    }

    public Boolean isLine(int x, int y) {
        if (this.map[x][y] > 0) return true;
        return false;
    }

    public Boolean isLine(Position position) {
        int x = position.getX();
        int y = position.getY();
        if (this.map[x][y] > 0) {
            System.err.println("Line !");
            return true;
        } 
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
class PathFinder {
    public Graph graph;
    public Grid map;
    private ArrayList<LinkedList<Node>> paths;
    private ArrayList<String> directions;
    private HashSet<Node> visitedNodes;

    PathFinder (){
        this.graph = initGraph();
    }

    PathFinder (Grid map) {
        this.map = map;
        this.graph = initGraph();
    }
    
    private Graph initGraph() {
        Graph graph = new Graph(); 
        for (int x = 0; x < Graph.X; x++) {
            for (int y = 0; y < Graph.Y; y++) {
                graph.addNode(new Position(x, y));
            }
        }
        for (Node node : graph.getNodesList().values()) {
            for (Position nextPosition : node.getPosition().nextPositions().values()) {
                Node nextNode = graph.getNode(nextPosition);
                if (nextNode != null) {
                    node.connect(nextNode);
                }
            }
        }
        return graph;
    }

    @Override
    public String toString() {
        return this.graph.toString();
    }

    public LinkedList<Node> findAllPaths(Position startPosition, Position endPosition) {
        LinkedList<Node> queue = new LinkedList<>();
        LinkedList<Node> visited = new LinkedList<>();
        Node start = this.graph.getNode(startPosition);
        Node end = this.graph.getNode(endPosition);

        queue.add(start);
        visited.add(start);
        while(!visited.contains(end)) {
            Node nextNode = queue.getFirst();
            queue.pop();
            for (Node node: nextNode.getNeighbors()) {
                if (!visited.contains(node) && !visited.contains(end)) {
                    queue.add(node);
                    visited.add(node);
                }
            }
        }
        return visited;
    }
    
    public ArrayList<LinkedList<Node>> findBestPaths (Position startPosition, Position endPosition) {
        this.visitedNodes = new HashSet<>();
        ArrayList<LinkedList<Node>> allPaths = new ArrayList<>();
        LinkedList<Node> path = this.findAllPaths(startPosition, endPosition);
        while (path.size() > 0) {
            allPaths.add(path);
            System.err.println(path);
            path = findAllPaths(startPosition, endPosition);
        }
        return allPaths;
    }
}

class Graph {
    final static int X = 30, Y = 20;
    private HashMap<Position, Node> nodesList;

    Graph () {
        nodesList = new HashMap<>();
    }
    
    // ------------------------ Getters & Setters 
    public Node getNode (int x, int y) {
        Position position = new Position(x, y);
        return nodesList.get(position);
    }

    public Node getNode (Position position) {
        return nodesList.get(position);
    }

    public HashMap<Position, Node> getNodesList () {
        return nodesList;
    }

    public Node addNode (Position position) {
        Node node = new Node(position);
        nodesList.put(position, node);
        return node;
    }

    // ------------------------ Methods 
   
    @Override
    public String toString () {
        return nodesList.toString();
    }
}

class Node {
    private Position position;
    private HashSet<Node> neighbors;
    private int distance;

    Node (int x, int y) {
        this.position = new Position(x, y);
    }

    Node (Position position) {
        this.position = position;
        this.neighbors = new HashSet<>();
    }

    Node (Position position, int distance) {
        this.position = position;
        this.distance = distance;
        this.neighbors = new HashSet<>();

    }

    // ------------------------ Getters & Setters
    public Position getPosition() {
        return this.position;
    }

    public HashSet<Node> getNeighbors() {
        return this.neighbors;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    // ------------------------ Methods 
    public void connect(Node node) {
        if (node != this) {
            this.neighbors.add(node);
            node.neighbors.add(this);
        }
    }

    @Override
    public String toString() {
        return this.position.toString();
    }

    public String neighborsToString () {
        String str = " [ "; 
		for (Node neighbor : this.neighbors)
			str += neighbor.getPosition().toString() + " ";
		return str + "]\n";
    }
}

class Position {
    private int x;
    private int y;
    public HashMap<String, Position> nextPositions;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
	public int hashCode()
	{
		return Graph.X * x + y;
    }
    
    // Getters & Setters
    public int getX () {
        return this.x;
    }

    public int getY () {
        return this.y;
    }

    public void setX (int x) {
        this.x = x;
    }

    public void setY (int y) {
        this.y = y;
    }

    public void setPosition (Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    public HashMap<String, Position> nextPositions () {
        this.nextPositions =  new HashMap<>();
        this.nextPositions.put("up", new Position(this.x, this.y - 1));
        this.nextPositions.put("right",  new Position(this.x + 1, this.y));
        this.nextPositions.put("down", new Position(this.x, this.y + 1));
        this.nextPositions.put("left", new Position(this.x - 1, this.y));
        return this.nextPositions;
    }

    @Override
    public String toString() {
        return String.format("(%1$d , %2$d)", this.x, this.y);
    }

    @Override
	public boolean equals(Object pos) {
		Position other = (Position) pos;
		return other.x == x && other.y == y;
	}
}

class Player {
    // Robots and map attributs
    static PlayerRobot player = null;
    static HashMap<Integer, Robot> opponentsList = new HashMap<Integer, Robot>();
    static Grid map = new Grid();

    // Game Main Functions
    public static Boolean isCorrectMove(Position position) {
        if (map.isWall(position)) return false;
        else if (map.isLine(position)) return false;
        else return true;
    }

    public static String moveToMake(PlayerRobot player, HashMap<Integer, Robot> opponentsList, Grid map) {
        String[] moves = {"UP", "RIGHT", "DOWN", "LEFT"};
        String moveToMake = "";
        Position curPos = player.getPosition();
        Position nextPos = new Position(-1,-1);

        int i = 0;
        while (!isCorrectMove(nextPos)) {
            switch(moves[i]) {
                case "UP":
                    nextPos.setPosition(curPos.nextPositions().get("up"));
                    break;
                case "DOWN":
                    nextPos.setPosition(curPos.nextPositions().get("down"));
                    break;
                case "LEFT":
                    nextPos.setPosition(curPos.nextPositions().get("left"));
                    break;
                case "RIGHT":
                    nextPos.setPosition(curPos.nextPositions().get("right"));
                    break;
                default:
                    nextPos.setPosition(curPos.nextPositions().get("up"));
                    moveToMake = "UP";
            }
            moveToMake = moves[i];
            i = (i >= moves.length - 1) ? 0 : i+1 ;
        }

        System.err.println("curPosition : " + "[" + curPos.getX() + "," + curPos.getY() + "]");
        System.err.println("nextPosition : " + "[" + nextPos.getX() + "," + nextPos.getY() + "]");
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
                    if (opponentsList.size() < N - 1) {
                        Robot opponent = new OpponentRobot(X0,Y0,X1,Y1);
                        opponentsList.put(i, opponent); 
                        map.setLinePosition(X0, Y0, 2);
                    } else {
                        Robot opponent = opponentsList.get(i);
                        opponent.setPosition(X1, Y1);
                        opponentsList.put(i, opponent);    
                    }
                    map.setLinePosition(X1, Y1, 2);
                }
            }

            // DEBUG
            // System.err.println("Player : \n" + player);
            // System.err.println("Opponent : \n" + opponentsList);

            Position start = new Position(0,0);
            Position end = new Position(3,3);
            PathFinder pathFinder = new PathFinder();

            LinkedList<Node> allPaths = pathFinder.findAllPaths(start, end);
            for (Node node : allPaths) System.err.println(node);         

            // ACTION
            String move = moveToMake(player, opponentsList, map);
            System.out.println(move);
        }
    }
}
/**
 * Player class for a GameSystem application.
 *
 * @author - Francisco Lima 65466
 * @author - Pâmela Cuna 63560
 */
public class Player {
    /**
     * Constants and instance variables
     */
    private static final int DUPLICATE = 2;

    private String name;
    private int points;
    private char[][] fleet;
    private Boat[][] reference_fleet;
    private int rows, columns;
    private boolean isEliminated;

    /**
     * Initializes a player object with a name and a fleet.
     *
     * @param name  - the name of the player.
     * @param fleet - the fleet of the player.
     * @pre - name != null && fleet != null
     */
    public Player(String name, char[][] fleet) {
        this.name = name;
        points = 0;
        this.reference_fleet = new Boat[fleet.length][fleet[0].length];
        setNull();
        rows = fleet.length;
        columns = fleet[0].length;
        this.fleet = fleet;
        isEliminated = true;
        setReferenceFleet();
        stillInGame();
    }

    /**
     * Sets reference fleet to null.
     */
    private void setNull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                reference_fleet[i][j] = null;
            }
        }
    }

    /**
     * Sets reference fleet to see if there's boats in the positions.
     */
    private void setReferenceFleet() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (fleet[i][j] != '.' && reference_fleet[i][j] == null) {
                    checkBoat(i, j);
                }
            }
        }
    }

    /**
     * Checks if there's a boat in the given row and column.
     *
     * @param i - the row of the fleet.
     * @param j - the column of the fleet.
     * @pre: 0 < i <= rows && 0 < j <= columns
     */
    private void checkBoat(int i, int j) {
        char a = fleet[i][j];
        int counter = 1;
        char dir = 'n';

        if (i + 1 < rows && fleet[i + 1][j] == a) {
            dir = 'b';
            int n = i + 1;
            while (n < rows && fleet[n][j] == a) {
                counter++;
                n++;
            }
        } else if (j + 1 < columns && fleet[i][j + 1] == a) {
            dir = 'd';
            int m = j + 1;
            while (m < columns && fleet[i][m] == a) {
                counter++;
                m++;
            }
        }

        Boat b = new Boat(counter, i, j, dir);

        createBoat(i, j, b);
    }

    /**
     * Creates a boat with given characteristics.
     *
     * @param i - the row's starting point.
     * @param j - the column's starting point.
     * @param b - the pipes of the boat.
     * @pre: 0 < i <= rows && 0 < j <= columns && boat != null
     */
    private void createBoat(int i, int j, Boat b) {
        if (b.getDir() == 'd') {
            for (int k = 0; k < b.getPipes(); k++) {
                reference_fleet[i][j + k] = b;
            }
        } else if (b.getDir() == 'b') {
            for (int l = 0; l < b.getPipes(); l++) {
                reference_fleet[l + i][j] = b;
            }
        } else reference_fleet[i][j] = b;
    }

    /**
     * Returns the number of rows.
     *
     * @return the number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns.
     *
     * @return the number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns the name of the player.
     *
     * @return name of player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the fleet of a player.
     *
     * @return fleet of a player.
     */
    public char[][] getFleet() {
        return fleet;
    }

    /**
     * Returns the points of a player.
     *
     * @return the points of a player.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Updates the points of a player.
     *
     * @param points the amount of points that are going to be added.
     * @pre: points != 0
     */
    public void updatePoints(int points) {
        this.points += points;
    }

    /**
     * Shoots a given position and calculates the size of the boat.
     *
     * @param row the number of the row to be shot.
     * @param col the number of the col to be shot.
     * @return the number of pipes a boat has.
     * @pre: 0 < row <= rows && 0 < col <= columns
     */
    public int shootPos(int row, int col) {
        int boat_pipe = 0;
        Boat b = reference_fleet[row - 1][col - 1];

        if (b != null) {
            boat_pipe = reference_fleet[row - 1][col - 1].getPipes();
            if (b.isFloating()) {
                b.setFloating();
                setFleet(b);
            } else {
                return -1 * boat_pipe;
            }
        }
        stillInGame();
        return boat_pipe;
    }

    /**
     * Sinks a boat.
     *
     * @param b - the boat to be sunk.
     * @pre: b != null
     */
    private void setFleet(Boat b) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (reference_fleet[i][j] == b) {
                    fleet[i][j] = '*';
                }
            }
        }
    }

    /**
     * Determines if they are floating boats in a fleet.
     */
    public void stillInGame() {
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (reference_fleet[i][j] != null) {
                    if (reference_fleet[i][j].isFloating()) {
                        counter++;
                    }
                }
            }
        }

        isEliminated = counter == 0;

    }

    /**
     * Duplicates the points of the winner player.
     */
    public void duplicatePoints() {
        this.points = points * DUPLICATE;
    }

    /**
     * Returns <code>true</code> if the player is eliminated, or <code>false</code> otherwise.
     *
     * @return Returns <code>true</code> if the player is eliminated,
     * or <code>false</code> otherwise.
     */
    public boolean getIsEliminated() {
        return isEliminated;
    }

    /**
     * Compares the points of the current player with another player.
     *
     * @param other - the other player.
     * @return a positive, negative, or zero value if this player's points are greater,
     * less or equal to the other.
     * @pre: other != null
     */
    public int compareTo(Player other) {
        if (points > other.getPoints()) {
            return 1;
        } else if (points < other.getPoints()) {
            return -1;
        } else {
            return other.getName().compareTo(name);
        }
    }
}


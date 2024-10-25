/**
 * GameSystem class for a GameSystem application.
 *
 * @author - Francisco Lima 65466
 * @author - Pâmela Cuna 63560
 */
public class GameSystem {

    /**
     * Constants and instance variables
     */
    private static final int GAINED_POINTS = 100;
    private static final int LOST_POINTS = 30;
    private static final int SURVIVORS = 1;
    private Player[] players;
    private Player current_player;
    private Player finalAlive;
    private int size;
    private int current, counterAlive;

    /**
     * Initializes the object with the given number of players.
     *
     * @param numPlayers - the number of players.
     * @pre: numPlayers > 0
     */
    public GameSystem(int numPlayers) {
        players = new Player[numPlayers];
        finalAlive = null;
        size = 0;
        current = 0;
        counterAlive = numPlayers;
        current_player = null;
    }

    /**
     * Returns the player with the given name.
     *
     * @param playerName - the name of the player.
     * @return the player with the given name
     * @pre: playerName != null
     */
    public Player getPlayer(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getName().equals(playerName)) {
                return players[i];
            }
        }
        return null;
    }

    /**
     * Adds the player with the given name and fleet to the GameSystem.
     *
     * @param playerName - the name of the player to be added.
     * @param fleet      - the fleet of the player.
     * @pre: playerName != null && fleet != null
     */
    public void addPlayer(String playerName, char[][] fleet) {
        players[size] = new Player(playerName, fleet);

        if (size == 0) {
            current_player = players[0];
        }

        size++;

    }

    /**
     * Returns <code>true</code> if the game is over, or <code>false</code> otherwise.
     *
     * @return if the game is over.
     */
    public boolean isGameOver() {
        return counterAlive == SURVIVORS && size != 1;
    }

    /**
     * Determines the final player and duplicates their points.
     */
    private void alivePlayer() {
        finalAlive = players[current];
        finalAlive.duplicatePoints();
    }

    /**
     * Determines the next player to play.
     */
    private void switchNextPlayerToCurrent() {
        do {
            current = (current + 1) % players.length;
        } while (players[current].getIsEliminated());

        current_player = players[current];
    }

    /**
     * Returns the name of the current player.
     *
     * @return name of the current player.
     */
    public String getCurrentPlayerName() {
        return current_player.getName();
    }

    /**
     * Returns the score of the player with the given name.
     *
     * @param player_name - the name of the player.
     * @return the score of the player with the given name
     * @pre: playerName != null
     */
    public int getPlayerScore(String player_name) {
        return getPlayer(player_name).getPoints();
    }

    /**
     * Returns the fleet of the player with the given name.
     *
     * @param player_name - the name of the player
     * @return the fleet of the player with the given name
     * @pre: playerName != null
     */
    public char[][] getPlayerFleet(String player_name) {
        return getPlayer(player_name).getFleet();
    }

    /**
     * Returns the name of the winner.
     *
     * @return the name of the winner.
     */
    public String getWinnerName() {
        String name = null;

        if (getFinalPoints() == null) {
            name = finalAlive.getName();
        } else {
            name = getFinalPoints().getName();
        }
        return name;
    }

    /**
     * Returns the player with the highest points.
     *
     * @return the player with the highest points
     */
    private Player getFinalPoints() {
        int counter = 0;
        Player p = players[0];

        for (int i = 1; i < size; i++) {
            if (players[i].getPoints() == p.getPoints()) {
                counter++;
            } else if (players[i].getPoints() > p.getPoints()) {
                p = players[i];
            }
        }

        if (counter == 0) {
            return p;
        } else
            return null;
    }

    /**
     * Shoots in the given row and column of a player's.
     *
     * @param row        - the row to be shot.
     * @param col        - the column to be shot.
     * @param playerName - the name of the player that is going to be shot.
     * @pre: row > 0 && col > 0 && playerName != null
     */
    public void shoot(int row, int col, String playerName) {
        Player p = getPlayer(playerName);
        int points = p.shootPos(row, col);

        if (points > 0) {
            current_player.updatePoints(points * GAINED_POINTS);
        } else {
            current_player.updatePoints(points * LOST_POINTS);
        }

        p.stillInGame();

        if (p.getIsEliminated()) {
            counterAlive--;
        }

        if (counterAlive != SURVIVORS) {
            switchNextPlayerToCurrent();
        } else {
            alivePlayer();
        }

    }

    /**
     * Returns an iterator over the players.
     *
     * @return an iterator over the players
     */
    public PlayerIterator iterator() {
        return new PlayerIterator(sort(), size);
    }

    /**
     * Returns a sorted array of players.
     *
     * @return a sorted array of players
     */
    private Player[] sort() {
        Player[] aux = new Player[players.length];
        for (int i = 0; i < players.length; i++) {
            aux[i] = players[i];
        }

        for (int j = 0; j < aux.length - 1; j++) {
            int minIdx = j;
            for (int k = j + 1; k < aux.length; k++) {
                if (aux[k].compareTo(aux[minIdx]) > 0) {
                    minIdx = k;
                }
            }

            Player p = aux[j];
            aux[j] = aux[minIdx];
            aux[minIdx] = p;
        }
        return aux;
    }

    /**
     * Returns an iterator over the active players.
     *
     * @return an iterator over the active players
     */
    public PlayerIterator filteredIterator() {
        Player[] aux = new Player[counterAlive];
        int counter = 0;

        for (int i = 0; i < players.length; i++) {
            if (!players[i].getIsEliminated()) {
                aux[counter++] = players[i];
            }
        }

        return new PlayerIterator(aux, counterAlive);
    }

    /**
     * Returns <code>true</code> if the given player name is eliminated,
     * or <code>false</code> otherwise.
     *
     * @param playerName - the name of the player.
     * @return if the given player's name is eliminated.
     * @pre: playerName != null
     */
    public boolean isEliminated(String playerName) {
        return getPlayer(playerName).getIsEliminated();
    }

    /**
     * Returns the fleet's number's of rows of the player with the given name.
     *
     * @param playerName - the player's name.
     * @return the fleet's number's of rows of the player with the given name
     * @pre: playerName != null
     */
    public int getNumLines(String playerName) {
        return getPlayer(playerName).getRows();
    }

    /**
     * Returns the fleet's number's of columns of the player with the given name.
     *
     * @param playerName - the player's name.
     * @return the fleet's number's of columns of the player with the given name
     * @pre: playerName != null
     */
    public int getNumColumns(String playerName) {
        return getPlayer(playerName).getColumns();
    }
}






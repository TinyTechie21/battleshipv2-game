import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Main class for a GameSystem application.
 *
 * @author - Francisco Lima 65466
 * @author - Pâmela Cuna 63560
 */
public class Main {

    /**
     * User's commands.
     */
    private static final String SCORE = "score";
    private static final String FLEET = "fleet";
    private static final String QUIT = "quit";
    private static final String PLAYER = "player";
    private static final String SHOOT = "shoot";
    private static final String SCORES = "scores";
    private static final String PLAYERS = "players";

    /**
     * Feedback given by the program.
     */
    private static final String GAME_OVER = "The game is over\n";
    private static final String NEXT_PLAYER = "Next player: %s\n";
    private static final String INVALID_CMD = "Invalid command\n";
    private static final String NON_EXISTENT_PLAYER = "Nonexistent player\n";
    private static final String POINTS_PLAYER = "%s has %d points\n";
    private static final String INVALID_SHOT = "Invalid shot\n";
    private static final String GAME_NOT_OVER = "The game was not over yet...\n";
    private static final String WON_GAME = "%s won the game!\n";
    private static final String SELF_SHOT = "Self-inflicted shot\n";
    private static final String PLAYERS_IN_GAME = "%s\n";
    private static final String ELIMINATED_PLAYER = "Eliminated player\n";

    /**
     * The name of the file.
     */
    private static final String FILE_NAME = "fleets.txt";


    public static void main(String[] args) throws FileNotFoundException {

        Scanner in = new Scanner(System.in);

        int numPlayers = in.nextInt();
        in.nextLine();
        GameSystem gs = createGameSystem(in, numPlayers);
        processCmd(in, gs);

        in.close();

    }

    /**
     * Command's interpreter.
     *
     * @param in - the input where the data is going to be read.
     * @param gs - the GameSystem in which the game will be started.
     */
    private static void processCmd(Scanner in, GameSystem gs) {
        String cmd;

        do {
            cmd = in.next();
            switch (cmd) {
                case PLAYER:
                    executePlayer(gs);
                    break;
                case SCORE:
                    executeScore(in, gs);
                    break;
                case FLEET:
                    executeFleet(in, gs);
                    break;
                case SHOOT:
                    executeShoot(in, gs);
                    break;
                case SCORES:
                    executeScores(gs);
                    break;
                case PLAYERS:
                    executePlayersInGame(gs);
                    break;
                case QUIT:
                    executeQuit(gs);
                    break;
                default:
                    executeInvalidCmd(in);
            }
        } while (!cmd.equals(QUIT));

    }

    /**
     * Executes an invalid command.
     *
     * @param in - the input in which the data is going to be read.
     */
    private static void executeInvalidCmd(Scanner in) {
        System.out.printf(INVALID_CMD);
        in.nextLine();
    }

    /**
     * Quits the game.
     *
     * @param gs - the GameSystem in which the game is going to quit.
     */
    private static void executeQuit(GameSystem gs) {
        if (!gs.isGameOver())
            System.out.printf(GAME_NOT_OVER);
        else
            System.out.printf(WON_GAME, gs.getWinnerName());
    }

    /**
     * Shoots a player's fleet in the given row and column.
     *
     * @param in - the input in which the data is going to be read.
     * @param gs - the GameSystem in which the game is going to shoot.
     */
    private static void executeShoot(Scanner in, GameSystem gs) {
        int row = in.nextInt();
        int column = in.nextInt();
        String playerName = in.nextLine().trim();

        if (gs.isGameOver())
            System.out.printf(GAME_OVER);
        else if (gs.getCurrentPlayerName().equals(playerName)) {
            System.out.printf(SELF_SHOT);
        } else if (gs.getPlayer(playerName) == null) {
            System.out.printf(NON_EXISTENT_PLAYER);
        } else if (gs.isEliminated(playerName)) {
            System.out.printf(ELIMINATED_PLAYER);
        } else if ((row < 1) || (column < 1) || (row > gs.getNumLines(playerName)) ||
                (column > gs.getNumColumns(playerName))) {
            System.out.printf(INVALID_SHOT);
        } else {
            gs.shoot(row, column, playerName);
        }
    }

    /**
     * Displays the names of all the players that are currently in the game.
     *
     * @param gs - the GameSystem in which the name of the players are going to be displayed.
     */
    private static void executePlayersInGame(GameSystem gs) {
        PlayerIterator it = gs.filteredIterator();
        while (it.hasNext()) {
            Player p = it.next();
            System.out.printf(PLAYERS_IN_GAME, p.getName());
        }
    }

    /**
     * Displays the name and the current score of each player, whether it is still playing
     * or eliminated.
     *
     * @param gs - the GameSystem in which the players name and score will be displayed.
     */
    private static void executeScores(GameSystem gs) {
        PlayerIterator it = gs.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            System.out.printf(POINTS_PLAYER, p.getName(), p.getPoints());
        }
    }

    /**
     * Displays the fleet of a player.
     *
     * @param in - the input in which the data is going to be read.
     * @param gs - the GameSystem in which a fleet is to be displayed.
     */
    private static void executeFleet(Scanner in, GameSystem gs) {
        String player_name = in.nextLine().trim();
        char[][] fleet;

        if (gs.getPlayer(player_name) == null) {
            System.out.printf(NON_EXISTENT_PLAYER);
        } else {
            fleet = gs.getPlayerFleet(player_name);
            for (int i = 0; i < gs.getPlayer(player_name).getRows(); i++) {
                for (int j = 0; j < gs.getPlayer(player_name).getColumns(); j++) {
                    System.out.print(fleet[i][j]);
                }
                System.out.println();
            }
        }
    }

    /**
     * Displays the score of a player.
     *
     * @param in - the input in which the data is going to be read.
     * @param gs - the GameSystem in which the player's score is to be displayed.
     */
    private static void executeScore(Scanner in, GameSystem gs) {
        String player_name = in.nextLine().trim();

        if (gs.getPlayer(player_name) == null)
            System.out.printf(NON_EXISTENT_PLAYER);
        else
            System.out.printf(POINTS_PLAYER, player_name, gs.getPlayerScore(player_name));
    }

    /**
     * Displays the name of the next player.
     *
     * @param gs - the GameSystem in which the next player's name is to be displayed.
     */
    private static void executePlayer(GameSystem gs) {
        if (gs.isGameOver())
            System.out.printf(GAME_OVER);
        else
            System.out.printf(NEXT_PLAYER, gs.getCurrentPlayerName());
    }

    /**
     * Creates the fleet of a player with the number of the fleet.
     *
     * @param numFleet - the number of the fleet that the player has chosen.
     * @return the fleet of the player.
     */
    private static char[][] createFleet(int numFleet) throws FileNotFoundException {
        Scanner reader = new Scanner(new FileReader(FILE_NAME));
        char[][] tempFleet = null;

        for (int n = 0; n < numFleet; n++) {
            int rows = reader.nextInt();
            int columns = reader.nextInt();
            reader.nextLine();
            tempFleet = new char[rows][columns];

            for (int i = 0; i < rows; i++) {
                String line = reader.nextLine().trim();
                for (int j = 0; j < columns; j++)
                    tempFleet[i][j] = line.charAt(j);
            }
        }
        return tempFleet;
    }

    /**
     * Creates a GameSystem with the given number of players.
     *
     * @param in         - the input on which the data is going to be read.
     * @param numPlayers - the number of the players.
     * @return a GameSystem with the given number of players.
     */
    private static GameSystem createGameSystem(Scanner in, int numPlayers)
            throws FileNotFoundException {
        GameSystem gs = new GameSystem(numPlayers);

        for (int i = 0; i < numPlayers; i++) {
            String playerName = in.nextLine().trim();
            int numFleet = in.nextInt();
            in.nextLine();
            char[][] fleet = createFleet(numFleet);
            gs.addPlayer(playerName, fleet);
        }
        return gs;
    }

}
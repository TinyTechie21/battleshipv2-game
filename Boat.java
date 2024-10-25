/**
 * Boat class for a game application.
 *
 * @author - Francisco Lima 65466
 * @author - Pâmela Cuna 63560
 */
public class Boat {

    /**
     * Instance variables
     */
    private boolean floating;
    private int pipes;
    private int row, col;
    private char dir;

    /**
     * Initializes a boat object with the number of pipes, rows, columns and the direction.
     *
     * @param pipes - the number of pipes.
     * @param row   - the number of rows.
     * @param col   - the number of columns.
     * @param dir   - the direction of the boat.
     * @pre - pipes != 0 && row > 0 && col > 0
     */
    public Boat(int pipes, int row, int col, char dir) {
        this.floating = true;
        this.pipes = pipes;
        this.row = row;
        this.col = col;
        this.dir = dir;
    }

    /**
     * Returns the boat's number of pipes.
     *
     * @return the boat's number of pipes.
     */
    public int getPipes() {
        return pipes;
    }

    /**
     * Returns the direction of the boat.
     *
     * @return the direction of the boat.
     */
    public char getDir() {
        return dir;
    }

    /**
     * Returns <code>true</code> if the boat is floating, or <code>false</code> otherwise.
     *
     * @return if the boat is still floating.
     */
    public boolean isFloating() {
        return floating;
    }

    /**
     * Sets the floating to false.
     */
    public void setFloating() {
        floating = false;
    }
}

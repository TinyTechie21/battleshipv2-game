/**
 * An iterator for a collection of players.
 *
 * @author - Francisco Lima 65466
 * @author - Pâmela Cuna 63560
 */
public class PlayerIterator {

    /**
     * Instance variables.
     */
    private Player[] players;
    private int size;
    private int nextIndex;

    /**
     * Constructs an iterator for the specified array of players.
     *
     * @param players - the players elements to iterate over.
     * @param size    - the size of the array.
     * @pre: players != null && size >= 0 && size <= players.length
     */
    public PlayerIterator(Player[] players, int size) {
        this.players = players;
        this.size = size;
        this.nextIndex = 0;
    }

    /**
     * Returns <code>true</code> if the iteration has more elements,
     * or <code>false</code> otherwise.
     *
     * @return <code>true</code> if the iteration has more elements,
     * or <code>false</code> otherwise.
     */
    public boolean hasNext() {
        return nextIndex < size;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @pre: hasNext()
     */
    public Player next() {
        return players[nextIndex++];
    }
}

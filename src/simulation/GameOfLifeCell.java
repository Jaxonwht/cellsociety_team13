package simulation;

/**
 * @author Haotian Wang
 */
public class GameOfLifeCell extends Cell {
    public static final int DEAD = 0;
    public static final int ALIVE = 1;

    public GameOfLifeCell(int state, int i, int j) {
        super(state, i, j);
    }
}

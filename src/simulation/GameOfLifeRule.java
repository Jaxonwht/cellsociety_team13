package simulation;

import java.util.List;

/**
 * @author Haotian Wang
 * A specific Rule class for GameOfLife game.
 */
public class GameOfLifeRule extends Rule {
    // If number of neighbors < UNDERPOPULATION_THRESHOLD, underpopulation happens.
    public static final int UNDERPOPULATION_THRESHOLD = 2;
    // If number of neighbors > OVERPOPULATION_THRESHOLD, overpopulation happens.
    public static final int OVERPOPUlATION_THRESHOLD = 3;

    public GameOfLifeRule(Grid grid) {
        super(grid);
    }

    /**
     * Calculate the next state of all the cells in the grid, according to this article, https://en.wikipedia.org/wiki/Conway's_Game_of_Life#Rules.
     */
    @Override
    public void determineNextStates() {
        for (int i = 0; i < this.getGrid().getNumRow(); i++) {
            for (int j = 0; j < this.getGrid().getNumCol(); j++) {
                Cell cell = this.getGrid().item(i, j);
                List<Cell> neighbors = this.getNeighbors(cell);
                int numAliveNeighbors = 0;
                for (Cell neighbor : neighbors) {
                    if (neighbor != null && neighbor.getState() == GameOfLifeCell.ALIVE) { numAliveNeighbors++; }
                }
                int numDeadNeighbors = neighbors.size() - numAliveNeighbors;
                if (cell.getState() == GameOfLifeCell.DEAD && numAliveNeighbors == OVERPOPUlATION_THRESHOLD) { cell.setNextState(GameOfLifeCell.ALIVE); }
                else if (cell.getState() == GameOfLifeCell.ALIVE && numAliveNeighbors < UNDERPOPULATION_THRESHOLD) { cell.setNextState(GameOfLifeCell.DEAD); }
                else if (cell.getState() == GameOfLifeCell.ALIVE && numAliveNeighbors <= OVERPOPUlATION_THRESHOLD) {}
                else if (cell.getState() == GameOfLifeCell.ALIVE && numAliveNeighbors > OVERPOPUlATION_THRESHOLD) { cell.setNextState(GameOfLifeCell.DEAD); }
            }
        }
    }
}

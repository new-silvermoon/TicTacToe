package com.example.tic_tac_toe.ai;

import com.example.tic_tac_toe.model.BoardGame;
import com.example.tic_tac_toe.model.Cell;
import com.example.tic_tac_toe.model.Player;

import java.util.ArrayList;
import java.util.List;

public class AIPlayer extends AI {

    public interface AIPlayerInterface{
        public boolean checkPlayerWin(String mark);
    }

    public AIPlayerInterface mInterface;

    public AIPlayer(Cell[][] cells,BoardGame boardGame) {
        super(cells);
        mInterface = (AIPlayerInterface) boardGame;

    }



    //Get next best move for AI
    @Override
    public int[] move() {
        int [] result = calculateMoves(BoardGame.BOARD_SIZE-1 , BoardGame.AI_MARK,Integer.MIN_VALUE,Integer.MAX_VALUE);
        return new int[] {result[1],result[2]};

    }

    /*Based on recursive minimax*/
    private int[] calculateMoves(int depth, String mark,int alpha,int beta){

        List<int []> emptyCells = getEmptyCells();

        //AI_MARK is maximizing, HUMAN_MARK is minimizing
        int score;
        int currentScore;
        int selectedRow = -1;
        int selectedCol = -1;

        if(emptyCells.isEmpty() || depth == 0){
            score = evaluate();
            return new int[] {score,selectedRow,selectedCol};
        }
        else{
            for(int[] emptyCell : emptyCells){
                cells[emptyCell[0]][emptyCell[1]] = new Cell(new Player());
                cells[emptyCell[0]][emptyCell[1]].player.setValue(mark);
                if(mark == BoardGame.AI_MARK){
                    score = calculateMoves(depth - 1,BoardGame.HUMAN_MARK,alpha,beta)[0];
                    if(score > alpha){
                        alpha = score;
                        selectedRow = emptyCell[0];
                        selectedCol = emptyCell[1];
                    }
                }
                else{
                    score = calculateMoves(depth -1,BoardGame.AI_MARK,alpha,beta)[0];
                    if(score < beta){
                        beta = score;
                        selectedRow = emptyCell[0];
                        selectedCol = emptyCell[1];
                    }
                }
                //Undo move
                cells[emptyCell[0]][emptyCell[1]] = null;
                //alpha-beta pruning
                if (alpha >= beta){
                    break;
                }
            }

            return new int[] {(mark == BoardGame.AI_MARK) ? alpha : beta,selectedRow,selectedCol};
        }
    }

    //Fins all possible next moves for AI
    private List<int[]> getEmptyCells(){
        List<int[]> nextMoves = new ArrayList<int[]>();

        if(mInterface.checkPlayerWin(BoardGame.AI_MARK)||mInterface.checkPlayerWin(BoardGame.HUMAN_MARK)){
            return nextMoves;
        }

        for(int row=0;row<ROWS;row++){
            for(int col=0;col<COLS;col++){
                if(cells[row][col] == null){
                    nextMoves.add(new int[] {row,col});
                }
            }
        }

        return nextMoves;


    }

    private int evaluate(){
        int score = 0;

        //Evaluating for all rows and columns, primary and secondary diagonal
        List<GridPosition> row_pairs = new ArrayList<GridPosition>();
        List<GridPosition> col_pairs = new ArrayList<GridPosition>();
        List<GridPosition> prim_diag = new ArrayList<GridPosition>();
        List<GridPosition> sec_diag = new ArrayList<GridPosition>();
        for(int i=0; i<BoardGame.BOARD_SIZE; i++){
            for(int j=0; j<BoardGame.BOARD_SIZE; j++){
                row_pairs.add(new GridPosition(i,j));
                col_pairs.add(new GridPosition(j,i));

                if(i==j){
                    prim_diag.add(new GridPosition(i,j));
                }

                if(i == BoardGame.BOARD_SIZE - j - 1){
                    sec_diag.add(new GridPosition(i,j));
                }


            }
        }

        score+= getScoreForLine(row_pairs);
        score+= getScoreForLine(col_pairs);
        score+= getScoreForLine(prim_diag);
        score+= getScoreForLine(sec_diag);

        return score;
    }

    /** The heuristic evaluation function for the given line of 3 cells
     @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     for nxn grid, the scores a re a multiple of 10
     0 otherwise */
    private int getScoreForLine(List<GridPosition> pairs){
        int score = 0;

        //First cell
        if(pairs.size()>0) {
            if (cells[pairs.get(0).x][pairs.get(0).y] != null && cells[pairs.get(0).x][pairs.get(0).y].player.getValue() == BoardGame.AI_MARK) {
                score = 1;
            } else if (cells[pairs.get(0).x][pairs.get(0).y] != null && cells[pairs.get(0).x][pairs.get(0).y].player.getValue() == BoardGame.HUMAN_MARK) {
                score = -1;
            }
        }

        //Second cell
        if(pairs.size() > 1) {
            if (cells[pairs.get(1).x][pairs.get(1).y] != null && cells[pairs.get(1).x][pairs.get(1).y].player.getValue() == BoardGame.AI_MARK) {
                if (score == 1) {
                    score = 10;
                } else if (score == -1) {
                    return 0;
                } else {
                    score = 1;
                }
            } else if (cells[pairs.get(1).x][pairs.get(1).y] != null && cells[pairs.get(1).x][pairs.get(1).y].player.getValue() == BoardGame.HUMAN_MARK) {
                if (score == -1) {
                    score = -10;
                } else if (score == 1) {
                    return 0;
                } else {
                    score = -1;
                }
            }
        }

        //Calculating scores for the rest of the cell-pairs
        for (int i = 2;i<pairs.size();i++){
            if(cells[pairs.get(i).x][pairs.get(i).y]!=null && cells[pairs.get(i).x][pairs.get(i).y].player.getValue() == BoardGame.AI_MARK){
                if(score>0){
                    score *= 10;
                }
                else if (score < 0){
                    return 0;
                }
                else{
                    score = 1;
                }
            }
            else if(cells[pairs.get(i).x][pairs.get(i).y]!=null && cells[pairs.get(i).x][pairs.get(i).y].player.getValue() == BoardGame.HUMAN_MARK){
                if(score<0){
                    score *=10;
                }
                else if (score > 1){
                    return 0;
                }
                else{
                    score = -1;
                }
            }
        }

        return score;
    }

    class GridPosition{
        public int x,y;

        public GridPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


}

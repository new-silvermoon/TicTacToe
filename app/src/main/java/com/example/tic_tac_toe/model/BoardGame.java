package com.example.tic_tac_toe.model;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.tic_tac_toe.ai.AIPlayer;


public class BoardGame implements AIPlayer.AIPlayerInterface {

    private static final String TAG = BoardGame.class.getSimpleName();
    public static final int BOARD_SIZE = 3;
    public static final String AI_MARK = "O";
    public static final String HUMAN_MARK = "X";
    public Player human,ai,current;
    public Cell[][] cells;
    public MutableLiveData<Player> winner = new MutableLiveData<>();




    public BoardGame() {
        human = new Player("HUMAN",HUMAN_MARK);
        ai = new Player("AI",AI_MARK);
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        current = human;

    }

    public boolean isGameEnd(){
        if(findMatchingRows()){
            winner.setValue(current);
            return true;
        }
        if(isBoardFull()){
            current.setName("Draw");
            winner.setValue(current);
            return true;
        }
        return false;
    }

    public boolean isBoardFull(){
        for (Cell[] row: cells){
            for(Cell cell: row){
                if(cell == null||cell.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean findMatchingRows(){
        try{

            for(int i = 0; i<BOARD_SIZE;i++){
                //Checking for matching horizontal rows or vertical rows
                if(areCellsEqual(cells[i][0],cells[i][1],cells[i][2])
                ||areCellsEqual(cells[0][i],cells[1][i],cells[2][i])){
                    return true;
                }

            }
            //Checking for matching diagonal rows
            return areCellsEqual(cells[0][0], cells[1][1], cells[2][2]) ||
                    areCellsEqual(cells[0][2], cells[1][1], cells[2][0]);

        }
        catch(Exception e){
            Log.e(TAG, "findMatchingRows: ",e );
            return false;
        }
    }

    public boolean findMatchingRows(String mark){
        try{

            for(int i = 0; i<BOARD_SIZE;i++){
                //Checking for matching horizontal rows or vertical rows
                if(areCellsEqual(mark,cells[i][0],cells[i][1],cells[i][2])
                        ||areCellsEqual(mark,cells[0][i],cells[1][i],cells[2][i])){
                    return true;
                }

            }
            //Checking for matching diagonal rows
            return areCellsEqual(mark,cells[0][0], cells[1][1], cells[2][2]) ||
                    areCellsEqual(mark,cells[0][2], cells[1][1], cells[2][0]);

        }
        catch(Exception e){
            Log.e(TAG, "findMatchingRows: ",e );
            return false;
        }
    }

    private boolean areCellsEqual(Cell... cells){

        try{
            Cell base = cells[0];
            if(base == null || base.player.getValue()==null){
                return false;
            }
            for (int i =1; i< cells.length; i++){

                if(!base.player.getValue().equals(cells[i].player.getValue())){
                    return false;
                }
            }
            return true;
        }
        catch(Exception e){
            Log.e(TAG, "areCellsEqual: ",e );
            return false;
        }
    }

    private boolean areCellsEqual(String mark,Cell... cells){

        try{
            if(cells[0] == null){
                cells[0] = new Cell(new Player());
            }
            Cell base = cells[0];
            if(base == null || base.player.getValue()==null || !base.player.getValue().equals(mark)){
                return false;
            }
            for (int i =1; i< cells.length; i++){

                if(cells[i] == null){
                    cells[i] = new Cell(new Player());
                }

                if(!base.player.getValue().equals(cells[i].player.getValue())){
                    return false;
                }
            }
            return true;
        }
        catch(Exception e){
            Log.e(TAG, "areCellsEqual: ",e );
            return false;
        }
    }

    public int[] switchPlayer(){
        current = current == human ? ai : human;
        int[] ai_moves = null;

        //Getting AI move
        if (current == ai){
            Cell [][] copyCells = this.cells;
            AIPlayer ai_player = new AIPlayer(this.cells,this);
            ai_moves = ai_player.move();
        }

        return ai_moves;



    }

    public void reset(){
        human = null;
        ai = null;
        current = null;
        cells = null;
    }

    @Override
    public boolean checkPlayerWin(String mark) {
        return findMatchingRows(mark);
    }
}

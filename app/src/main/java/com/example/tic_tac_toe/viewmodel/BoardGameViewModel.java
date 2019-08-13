package com.example.tic_tac_toe.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayMap;

import com.example.tic_tac_toe.model.BoardGame;
import com.example.tic_tac_toe.model.Cell;
import com.example.tic_tac_toe.model.Player;


public class BoardGameViewModel extends ViewModel {

    public ObservableArrayMap<String,String> cells;
    private BoardGame boardGame;

    public void init(){
        boardGame = new BoardGame();
        cells = new ObservableArrayMap<>();
    }

    public void onCellClick(int row,int column){
        if(boardGame.cells[row][column] == null){
            boardGame.cells[row][column] = new Cell(boardGame.current);
            boardGame.cells[row][column].setMarked(1);

            cells.put(stringFromNumbers(row,column),boardGame.current.getValue());
            if(boardGame.isGameEnd()){
                boardGame.reset();
            }
            else{
                int[] ai_moves = boardGame.switchPlayer();
                if (ai_moves!= null && ai_moves[0]!=-1 && ai_moves[1]!=-1){
                    onCellClick(ai_moves[0],ai_moves[1]);
                }
            }
        }
    }

    public String stringFromNumbers(int... numbers) {
        StringBuilder sNumbers = new StringBuilder();
        for (int number : numbers)
            sNumbers.append(number);
        return sNumbers.toString();
    }

    public LiveData<Player> getWinner(){
        return boardGame.winner;
    }

}

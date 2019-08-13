package com.example.tic_tac_toe.ai;

import com.example.tic_tac_toe.model.BoardGame;
import com.example.tic_tac_toe.model.Cell;

abstract class AI {
    protected int ROWS = BoardGame.BOARD_SIZE;
    protected int COLS = BoardGame.BOARD_SIZE;

    protected Cell[][] cells;

    public AI(Cell[][] cells) {
        this.cells = cells;
    }

    abstract int[] move();
}

package com.example.tic_tac_toe.model;

public class Cell {

    public Player player;
    public int isMarked;

    public Cell(Player player) {
        this.player = player;
    }

    public boolean isEmpty(){
        return player==null ||player.getValue() == null || player.getValue().length() == 0;
    }

    public int isMarked() {
        return isMarked;
    }

    public void setMarked(int marked) {
        isMarked = marked;
    }
}

package com.example.tic_tac_toe.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tic_tac_toe.R;
import com.example.tic_tac_toe.databinding.ActivityMainBinding;
import com.example.tic_tac_toe.dialogs.GameOverDialog;
import com.example.tic_tac_toe.model.Player;
import com.example.tic_tac_toe.viewmodel.BoardGameViewModel;

public class MainActivity extends AppCompatActivity {

    //region Variables declaration
    private static final String GAME_OVER_DIALOG_TAG = "game_over_dialog";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String NO_WINNER = "Draw";

    private BoardGameViewModel boardGameViewModel;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InitDataBinding();

    }

    private void onGameOverListener(){
        boardGameViewModel.getWinner().observe(this,gameOverObserver);
    }

    public void InitDataBinding(){
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        boardGameViewModel = ViewModelProviders.of(this).get(BoardGameViewModel.class);
        boardGameViewModel.init();
        binding.setBoardGameViewModel(boardGameViewModel);
        onGameOverListener();
    }


    private final Observer<Player> gameOverObserver = new Observer<Player>() {
        @Override
        public void onChanged(@Nullable Player winner) {
            String winnerName = winner.getName().equalsIgnoreCase("draw") ? "It's a Draw" : "Winner is: "+winner.getName();

            GameOverDialog dialog = GameOverDialog.newInstance(MainActivity.this, winnerName);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), GAME_OVER_DIALOG_TAG);

        }
    };
}

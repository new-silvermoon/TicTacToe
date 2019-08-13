package com.example.tic_tac_toe.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.tic_tac_toe.R;
import com.example.tic_tac_toe.views.MainActivity;

public class GameOverDialog extends DialogFragment {

    private View rootView;
    private MainActivity mainActivity;
    private String winnerName;

    public static GameOverDialog newInstance(MainActivity activity, String winnerName){

        GameOverDialog dialog = new GameOverDialog();
        dialog.mainActivity = activity;
        dialog.winnerName = winnerName;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.end_dialog,null,false);
        ((TextView) rootView.findViewById(R.id.tvWinner)).setText(winnerName);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        mainActivity.InitDataBinding();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


}

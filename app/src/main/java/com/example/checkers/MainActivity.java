package com.example.checkers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

@SuppressLint("UseCompatLoadingForDrawables")
public class MainActivity extends AppCompatActivity {

    private final TextView[][] board = new TextView[8][8];
    private int orangeSide, redSide;
    private boolean isRedTurn;
    private int lastI, lastJ;

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setBoardId();
        resetGame();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {

                int finalI = i;
                int finalJ = j;

                board[i][j].setOnClickListener(v -> {

                    if (board[finalI][finalJ].getBackground() instanceof ColorDrawable && ((ColorDrawable) board[finalI][finalJ].getBackground()).getColor() == Color.GREEN) {

                        board[finalI][finalJ].setTag(board[lastI][lastJ].getTag());
                        board[lastI][lastJ].setTag(0);

                       switch ((Integer)board[finalI][finalJ].getTag()){

                           case R.drawable.ic_red_king_40:
                           case R.drawable.ic_red_soldier_40:
                               if(finalJ < lastJ - 1) board[finalI - 1][finalJ + 1].setTag(0);
                               if (finalJ > lastJ + 1) board[finalI - 1][finalJ - 1].setTag(0);
                               break;

                           case R.drawable.ic_orange_king_40:
                           case R.drawable.ic_orange_soldier_40:
                               if(finalJ < lastJ - 1) board[finalI + 1][finalJ + 1].setTag(0);
                               if (finalJ > lastJ + 1) board[finalI + 1][finalJ - 1].setTag(0);
                               break;
                       }
                        isRedTurn = !isRedTurn;
                        repaint();
                    } else margeTypes(finalI, finalJ);
                });
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void margeTypes(int finalI, int finalJ) {

        switch ((Integer) board[finalI][finalJ].getTag()) {

            case R.drawable.ic_red_king_40:
            case R.drawable.ic_red_soldier_40:
                if (isRedTurn) showAvailablePlaces(finalI, finalJ, 1, (Integer) board[finalI][finalJ].getTag());
                break;

            case R.drawable.ic_orange_king_40:
            case R.drawable.ic_orange_soldier_40:
                if (!isRedTurn) showAvailablePlaces(finalI, finalJ, -1, (Integer) board[finalI][finalJ].getTag());
                break;
        }
    }

    private void showAvailablePlaces(int finalI, int finalJ, int addition, int tag) {

        repaint();
        int add2 = addition + addition;
        board[finalI][finalJ].setBackgroundColor(Color.DKGRAY);
        try {
            if ((Integer) board[finalI + addition][finalJ + 1].getTag() != tag) {
                    if ((Integer) board[finalI + addition][finalJ + 1].getTag() != 0 ) {
                        if((Integer) board[finalI + add2][finalJ + 2].getTag() == 0) {
                            board[finalI + add2][finalJ + 2].setBackgroundColor(Color.GREEN);
                        }
                    }else board[finalI + addition][finalJ + 1].setBackgroundColor(Color.GREEN);
            }
        } catch (IndexOutOfBoundsException ignored){}

        try {
            if ((Integer) board[finalI + addition][finalJ - 1].getTag() != tag) {
                if ((Integer) board[finalI + addition][finalJ - 1].getTag() != 0  ) {
                    if ((Integer) board[finalI + add2][finalJ - 2].getTag() == 0) {
                        board[finalI + add2][finalJ - 2].setBackgroundColor(Color.GREEN);
                    }
                }else board[finalI + addition][finalJ - 1].setBackgroundColor(Color.GREEN);
            }
        } catch (IndexOutOfBoundsException ignored){}

        lastI = finalI;
        lastJ = finalJ;
    }

    @SuppressLint("NonConstantResourceId")
    private void repaint() {

        redSide = 0;
        orangeSide = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {

                switch ((Integer) board[i][j].getTag()) {

                    case R.drawable.ic_red_soldier_40:
                        board[i][j].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_red_soldier_40);
                        break;

                    case R.drawable.ic_orange_soldier_40:
                        board[i][j].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_orange_soldier_40);
                        break;

                    case R.drawable.ic_red_king_40:
                        board[i][j].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_red_king_40);
                        break;

                    case R.drawable.ic_orange_king_40:
                        board[i][j].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_orange_king_40);
                        break;

                    case 0:
                        board[i][j].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        break;

                }
                repaintBackground(i, j);
                syncSides((Integer) board[i][j].getTag());
            }
        }
        checkForWin();
    }

    private void syncSides(int tag) {
        int orangeSoldier = R.drawable.ic_orange_soldier_40;
        int orangeKing = R.drawable.ic_orange_king_40;
        int redSoldier = R.drawable.ic_red_soldier_40;
        int redKing = R.drawable.ic_red_king_40;

        if (tag == orangeSoldier || tag == orangeKing) {
            orangeSide++;
        }
        if (tag == redSoldier || tag == redKing) {
            redSide++;
        }
    }

    private void checkForWin(){

        TextView textView;

        if (redSide == 0){
            Toast toast = Toast.makeText(this, "Red won!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
            textView = toast.getView().findViewById(android.R.id.message);
            textView.setTextColor(Color.RED);
            toast.show();
            resetGame();
        } else if (orangeSide == 0){
            Toast toast = Toast.makeText(this, "Orange won!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
            textView = toast.getView().findViewById(android.R.id.message);
            textView.setTextColor(Color.parseColor("#FFC300"));
            toast.show();
            resetGame();
        }
    }

    private void repaintBackground(int i, int j) {

        if ((i + j) % 2 == 0)
            board[i][j].setBackgroundColor(Color.WHITE);
        else
            board[i][j].setBackgroundColor(Color.BLACK);
    }

    private void resetGame() {

        TextView textView;
        for (TextView[] textViews : board) {
            for (int j = 0; j < board.length; j++) {

                textViews[j].setTag(0);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < board.length; j++) {

                if ((i + j) % 2 == 1) {
                    board[i][j].setTag(R.drawable.ic_red_soldier_40);
                }
            }
        }

        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < board.length; j++) {

                if ((i + j) % 2 == 1) {
                    board[i][j].setTag(R.drawable.ic_orange_soldier_40);
                }
            }
        }
        repaint();

        isRedTurn = new Random().nextBoolean();

        if (isRedTurn){
            Toast toast = Toast.makeText(this, "Red start!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
            textView = toast.getView().findViewById(android.R.id.message);
            textView.setTextColor(Color.RED);
            toast.show();
        }else{
            Toast toast = Toast.makeText(this, "Orange start!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
            textView = toast.getView().findViewById(android.R.id.message);
            textView.setTextColor(Color.parseColor("#FFC300"));
            toast.show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void OnClick(View view) {

        switch (view.getId()) {

            case R.id.undo:
                break;

            case R.id.reset:
                resetGame();
                break;
        }
    }

    private void setBoardId(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = findViewById(getResources().getIdentifier("ib_" + i + "" + j, "id", getPackageName()));
            }
        }
    }
}
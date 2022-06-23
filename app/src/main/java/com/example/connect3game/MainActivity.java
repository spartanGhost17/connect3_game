package com.example.connect3game;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;//changed from android to androidx?
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private int currentPlayer = 0; //0: red 1: yellow
    private int[] gameState= {4,4,4,4,4,4,4,4,4};

    private int[][] winningStates = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6},
            {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
    private boolean gameActive = true;

    public void playAgain(View view){
        //ImageView counter = (ImageView) view;
        TextView text = (TextView) findViewById(R.id.textView);
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);
        Button playAgain = (Button)findViewById(R.id.startButton);
        playAgain.setVisibility(View.INVISIBLE);

        for(int i = 0; i < grid.getChildCount(); i++){
           ImageView counter =  (ImageView)grid.getChildAt(i);
           //counter.setImageDrawable(null);
            counter.setImageResource(0);
        }
        for(int v=0; v<this.gameState.length; v++)
            this.gameState[v]=4;

        gameActive = true;
        currentPlayer = 0;
        text.setText(Arrays.toString(this.gameState));
    }

    public void dropIn(View view){
        ImageView counter = (ImageView) view;
        TextView text = (TextView) findViewById(R.id.textView);
        Button playAgain = (Button) findViewById(R.id.startButton);
        //log current player in cell
        int tappedImageView = Integer.parseInt(counter.getTag().toString());

        List<Integer> convertedGameState;
        text.setText(Arrays.toString(this.gameState));

        Log.i("Tag => ", String.valueOf(tappedImageView));
        Log.i("Index ",String.valueOf(this.gameState[tappedImageView]));


        if(this.gameState[tappedImageView]==4 && gameActive){

            this.gameState[tappedImageView]=this.currentPlayer;
            int winner = endGame();//check for winner
            Log.i("Info ->", "winner is "+winner);
            convertedGameState = Arrays.stream(this.gameState).boxed().collect(Collectors.toList());

            if(winner == 0){//red wins
                counter.setImageResource(R.drawable.red);//setImage
                animateDrop(counter);//animate plate in
                gameActive = false;
                text.setText("Red wins");

                playAgain.setVisibility(View.VISIBLE);
            }
            else if(winner == 1){//yellow wins
                counter.setImageResource(R.drawable.yellow);//setImage
                animateDrop(counter);
                gameActive = false;
                Toast.makeText(this, "Yellow wins", Toast.LENGTH_LONG).show();
                text.setText("Yellow wins");

                playAgain.setVisibility(View.VISIBLE);
            }
            //if no player won, check for draw. if array doesn't contains 4, Draw!
            else if(!convertedGameState.contains(4)) {
                if(this.currentPlayer==0) counter.setImageResource(R.drawable.red);
                else counter.setImageResource(R.drawable.yellow);
                animateDrop(counter);
                gameActive = false;
                text.setText("Oh no! that's a draw");

                playAgain.setVisibility(View.VISIBLE);
            }
            else{//if red and blue haven't won keep playing
                if(this.currentPlayer==0){//red
                    if(counter.getDrawable() == null && winner==4) {//only set if not image exists in view
                        counter.setImageResource(R.drawable.red);//setImage
                        animateDrop(counter);//animate plate in
                    }
                    currentPlayer=1;
                }
                else{//yellow
                    if(counter.getDrawable() == null && winner==4){
                        counter.setImageResource(R.drawable.yellow);//setImage
                        animateDrop(counter);
                    }
                    currentPlayer=0;
                }
            }

        }


    }

    private int endGame(){
        int winner = 4;
        //int count =0;int[] gameState
        int[] tempArray = new int[3];
        for(int i=0; i<this.winningStates.length; i++){

            for(int v=0; v<this.winningStates[0].length; v++){
                //Arrays.stream(Arrays.copyOfRange());
                Log.i("Inner index ", String.valueOf(this.winningStates[i][v]));
                tempArray[v] = this.gameState[this.winningStates[i][v]];
            }
            Log.i("temp array", Arrays.toString(tempArray));
            if(Arrays.stream(tempArray).sum()==0){//if sum is 0 zero wins
                winner = 0;
                break;
            }
            else if(Arrays.stream(tempArray).sum()==3){//if sum is 3 (1 wins)
                winner = 1;
                break;
            }
            tempArray = new int[3];
        }

        return winner;
    }

    private void animateDrop(ImageView imageView){

        imageView.setTranslationY(-1500);
        imageView.animate().translationYBy(1500).rotation(3000).setDuration(1000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
package shubh.scarnesdice;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtuser_score,txtcomp_score;
    ImageView dice_img;
    Button btnroll,btnhold,btnreset;
    int user_score,comp_score;
    private final int MAX_SCORE = 100;
    private Random random=new Random();
    private Random Random2 = new Random();
    private int diceIcons [] = {
            R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6
    };
    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtuser_score= findViewById(R.id.user_score);
        txtcomp_score= findViewById(R.id.comp_score);
        dice_img= findViewById(R.id.dice_img);
        btnroll= findViewById(R.id.diceroll);
        btnhold= findViewById(R.id.dicehold);
        btnreset= findViewById(R.id.dicereset);
        //txt_current_score=(TextView) findViewById(R.id.showcurrentuserscore);
        btnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rolldice();
            }
        });
        btnhold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startComputerTurn();
            }
        });

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetdice();
            }
        });

    }
    private void rolldice(){
        int num=random.nextInt(6)+1;
        int currentScore=Integer.parseInt(txtuser_score.getText().toString());
        dice_img.setImageResource(diceIcons[num-1]);
        if(num==1){
            currentScore=currentScore-user_score;
            txtuser_score.setText(currentScore+"");
            startComputerTurn();
        }
        else{
            currentScore=currentScore+num;

            if (currentScore >= MAX_SCORE){
                endGame("User");
                return;
            }
            user_score=user_score+num;
            txtuser_score.setText(currentScore+"");
        }
    }
    private void startComputerTurn(){
        btnhold.setEnabled(false);
        btnroll.setEnabled(false);
        user_score=0;
        comp_score=0;
        timerHandler.postDelayed(computerTurnRunnable, 700);

    }
    Runnable computerTurnRunnable = new Runnable() {
        @Override
        public void run() {
            if (Random2.nextInt(10) < 6 || comp_score == 0){ // 50% chances that computer will play
                // computer plays first chance
                boolean b = ComputerTurn();
                if (b){
                    timerHandler.postDelayed(this, 800);
                } else {
                    endComputerTurn();
                }
            } else { // hold
                endComputerTurn();
            }
        }
    };
    private boolean ComputerTurn(){
        int num=random.nextInt(6)+1;
        int currentScore=Integer.parseInt(txtcomp_score.getText().toString());
        dice_img.setImageResource(diceIcons[num-1]);
        if(num==1){
            currentScore=currentScore-comp_score;
            txtcomp_score.setText(currentScore+"");
            return false;
        }
        else{
            currentScore=currentScore+num;
            if (currentScore >= MAX_SCORE){
                endGame("Computer");
                return false;
            }
            comp_score=comp_score+num;
            txtcomp_score.setText(currentScore+"");
        }
        return true;
    }
    private void endComputerTurn(){
        btnhold.setEnabled(true);
        btnroll.setEnabled(true);
    }
    private void resetdice(){
        user_score=0;
        comp_score=0;
        txtuser_score.setText("0");
        txtcomp_score.setText("0");

    }
    private void endGame(String winner){
        (Toast.makeText(this, "Game over. " + winner + " won", Toast.LENGTH_LONG)).show();
        onStart();
    }
}

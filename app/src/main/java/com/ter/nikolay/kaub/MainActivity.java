package com.ter.nikolay.kaub;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    public static MainActivity instanse;
    protected TextView mTimerLabel;
    protected TextView team1Player1;
    protected TextView team1Player2;
    protected TextView team1Player3;
    protected TextView team1Player4;
    protected TextView team2Player1;
    protected TextView team2Player2;
    protected TextView team2Player3;
    protected TextView team2Player4;

    protected TextView Team1Count;
    protected TextView Team1FoulCount;
    protected TextView Team2Count;
    protected TextView Team2FoulCount;

    protected int playerSelected = 0;
    protected int Team1CountInt = 0;
    protected int Team1FoulCountInt = 0;
    protected int Team2CountInt = 0;
    protected int Team2FoulCountInt = 0;

    protected CountDownTimer mTimer;
    protected Button btnTimerControl;
    protected int btnTimerVal;
    protected Boolean mTimerIsOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instanse = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findElement();
        setOnClickListener();
        mTimerLabel.setText(timeToString(btnTimerVal));
        registerContextMenu();

    }


    protected void registerContextMenu(){
        registerForContextMenu(team1Player1);
        registerForContextMenu(team1Player2);
        registerForContextMenu(team1Player3);
        registerForContextMenu(team1Player4);
        registerForContextMenu(team2Player1);
        registerForContextMenu(team2Player2);
        registerForContextMenu(team2Player3);
        registerForContextMenu(team2Player4);
    }

    protected void findElement() {
        btnTimerVal      = getResources().getInteger(R.integer.game_time);
        mTimerLabel      = (TextView) findViewById(R.id.mTimerLabel);
        btnTimerControl  = (Button) findViewById(R.id.btnTimerControl);
        team1Player1     = (TextView) findViewById(R.id.team1Player1);
        team1Player2     = (TextView) findViewById(R.id.team1Player2);
        team1Player3     = (TextView) findViewById(R.id.team1Player3);
        team1Player4     = (TextView) findViewById(R.id.team1Player4);
        team2Player1     = (TextView) findViewById(R.id.team2Player1);
        team2Player2     = (TextView) findViewById(R.id.team2Player2);
        team2Player3     = (TextView) findViewById(R.id.team2Player3);
        team2Player4     = (TextView) findViewById(R.id.team2Player4);

        Team1Count       = (TextView) findViewById(R.id.team1Count);
        Team1FoulCount   = (TextView) findViewById(R.id.team1FoulCount);
        Team2Count       = (TextView) findViewById(R.id.team2Count);
        Team2FoulCount   = (TextView) findViewById(R.id.team2FoulCount);
    }

    protected void setOnClickListener() {
        btnTimerControl.setOnClickListener(this);
        mTimerLabel.setOnClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        switch (v.getId()) {
            case R.id.team1Player1:
                playerSelected = 1;
                getMenuInflater().inflate(R.menu.menu_player, menu);
                break;
            case R.id.team1Player2:
                playerSelected = 2;
                getMenuInflater().inflate(R.menu.menu_player, menu);
                break;
            case R.id.team1Player3:
                playerSelected = 3;
                getMenuInflater().inflate(R.menu.menu_player, menu);
                break;
            case R.id.team1Player4:
                playerSelected = 4;
                getMenuInflater().inflate(R.menu.menu_player, menu);
                break;
            case R.id.team2Player1:
                playerSelected = 5;
                getMenuInflater().inflate(R.menu.menu_player, menu);
                break;
            case R.id.team2Player2:
                playerSelected = 6;
                getMenuInflater().inflate(R.menu.menu_player, menu);
                break;
            case R.id.team2Player3:
                playerSelected = 7;
                getMenuInflater().inflate(R.menu.menu_player, menu);
                break;
            case R.id.team2Player4:
                playerSelected = 8;
                getMenuInflater().inflate(R.menu.menu_player, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_point:
                if(playerSelected<5){
                    Team1CountInt++;
                    Team1Count.setText(Integer.toString(Team1CountInt));
                }else{
                    Team2CountInt++;
                    Team2Count.setText(Integer.toString(Team2CountInt));
                }
                checkPointCount();
                break;
            case R.id.add_two_point:
                if(playerSelected<5){
                    Team1CountInt+=2;
                    Team1Count.setText(Integer.toString(Team1CountInt));

                }else{
                    Team2CountInt+=2;
                    Team1Count.setText(Integer.toString(Team2CountInt));
                }
                checkPointCount();
                break;
            case R.id.add_foul:
                if(playerSelected<5){
                    Team1FoulCountInt++;
                    Team1FoulCount.setText(Integer.toString(Team1FoulCountInt));
                }else{
                    Team2FoulCountInt++;
                    Team2FoulCount.setText(Integer.toString(Team2FoulCountInt));
                }

                break;

        }
        return super.onContextItemSelected(item);
    }
protected void checkPointCount(){
    if(Team1CountInt>getResources().getInteger(R.integer.game_point_limit)||
            Team2CountInt>getResources().getInteger(R.integer.game_point_limit)){
        stopGame();
    }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {
        if (v == btnTimerControl || v == mTimerLabel) {
            onClickBtnTimerControl(v);
        }
    }

    public void onClickBtnTimerControl(View v) {
        if (mTimerIsOn) {
            stopTimer();
        } else {
            startTimer();

        }
    }

    public String timeToString(long time) {
        String millis = String.valueOf(time % 10);
        String minute = String.valueOf(time / 60000);
        String second = String.valueOf((time % 60000) / 1000);
        return minute + ":" + second + "." + millis;
    }

    public void startTimer() {
        // Create a new CountDownTimer to track the brew time
        mTimer = new CountDownTimer(btnTimerVal, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnTimerVal = (int) millisUntilFinished;
                mTimerLabel.setText(timeToString(btnTimerVal));
            }

            @Override
            public void onFinish() {
                mTimerIsOn = false;
                stopGame();
                mTimerLabel.setText("00:00.0!");
                btnTimerControl.setText("Start");
            }
        };


        mTimer.start();
        btnTimerControl.setText("Stop");
        mTimerIsOn = true;
    }

    /**
     * Stop the brew timer
     */
    public void stopTimer() {
        if (mTimer != null)
            mTimer.cancel();

        mTimerIsOn = false;
        btnTimerControl.setText("Start");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void stopGame(){
        stopTimer();
        mTimerLabel.setTextColor(getResources().getColor(R.color.end_of_timer));
        Toast.makeText(MainActivity.instanse, "Время матча вышло", Toast.LENGTH_LONG).show();
    }



}

package com.ter.nikolay.kaub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    public static MainActivity instanse;
    protected ModelGame modelGame;
    protected TextView mTimerLabel;
    protected TextView[] playerLabel;
    protected TextView playerLabel1;
    protected TextView playerLabel2;
    protected TextView playerLabel3;
    protected TextView playerLabel4;
    protected TextView playerLabel5;
    protected TextView playerLabel6;
    protected TextView playerLabel7;
    protected TextView playerLabel8;

    protected TextView[] TeamPointCount;
    protected TextView[] TeamFoulCount;


    protected int playerSelected = 0;
    protected int Team1CountInt = 0;
    protected int Team1FoulCountInt = 0;
    protected int Team2CountInt = 0;
    protected int Team2FoulCountInt = 0;

    protected CountDownTimer mTimer;
    protected Button btnTimerControl;
    protected Boolean mTimerIsOn = false;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instanse = this;
        modelGame = new ModelGame(1,3,2,600000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findElement();
        setOnClickListener();
        mTimerLabel.setText(timeToString(modelGame.getTimerVal()));
       // registerContextMenu();
        context = MainActivity.this;


    }


    protected void registerContextMenu(){
        registerForContextMenu(playerLabel1);
        registerForContextMenu(playerLabel2);
        registerForContextMenu(playerLabel3);
        registerForContextMenu(playerLabel4);
        registerForContextMenu(playerLabel5);
        registerForContextMenu(playerLabel6);
        registerForContextMenu(playerLabel7);
        registerForContextMenu(playerLabel8);
    }

    protected void findElement() {
        mTimerLabel      = (TextView) findViewById(R.id.mTimerLabel);
        btnTimerControl  = (Button) findViewById(R.id.btnTimerControl);
        playerLabel = new TextView[8];
        playerLabel[0] = (TextView) findViewById(R.id.playerLabel1);
        playerLabel[1] = (TextView) findViewById(R.id.playerLabel2);
        playerLabel[2] = (TextView) findViewById(R.id.playerLabel3);
        playerLabel[3] = (TextView) findViewById(R.id.playerLabel4);
        playerLabel[4] = (TextView) findViewById(R.id.playerLabel5);
        playerLabel[5] = (TextView) findViewById(R.id.playerLabel6);
        playerLabel[6] = (TextView) findViewById(R.id.playerLabel7);
        playerLabel[7] = (TextView) findViewById(R.id.playerLabel8);

        TeamPointCount = new TextView[2];
        TeamPointCount[0] =(TextView) findViewById(R.id.team1Count);
        TeamPointCount[1] =(TextView) findViewById(R.id.team2Count);

        TeamFoulCount = new TextView[2];
        TeamFoulCount[0] =(TextView) findViewById(R.id.team1FoulCount);
        TeamFoulCount[1] =(TextView) findViewById(R.id.team2FoulCount);

    }

    protected void setOnClickListener() {
        btnTimerControl.setOnClickListener(this);
        mTimerLabel.setOnClickListener(this);
        playerLabel[0].setOnClickListener(this);
        playerLabel[1].setOnClickListener(this);
        playerLabel[2].setOnClickListener(this);
        playerLabel[3].setOnClickListener(this);
        playerLabel[4].setOnClickListener(this);
        playerLabel[5].setOnClickListener(this);
        playerLabel[6].setOnClickListener(this);
        playerLabel[7].setOnClickListener(this);
    }

    protected void checkPointCount(){
        int CheckPointCount = modelGame.checkPointCount();
        if(CheckPointCount>0){
            stopGame("Команда "+Integer.toString(CheckPointCount)+" выйграла");
        }
    }
    protected void checkFoulCount(){
        int ChekFoulCount = modelGame.chekFoulCount();
        if(modelGame.chekFoulCount() > 0){
            Toast.makeText(MainActivity.instanse, "Команда "+Integer.toString(ChekFoulCount)+" выйграла", Toast.LENGTH_LONG).show();
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
        }else{
            for (int i = 0; i < playerLabel.length; i += 1){
                if(v == playerLabel[i]){
                    onClickPlayer(v,i+1);
                    break;
                }
            }
        }
    }

    protected void onClickPlayer(View v,int playerNum){
        playerSelected = playerNum;
        AlertDialog.Builder ad;
        final String[] mCatsName ={"1 очко", "2 очка", "фол", "Отмена"};

        ad = new AlertDialog.Builder(this);
        ad.setTitle("Команда "+(playerSelected<5?'1':'2')+" игрок "+Integer.toString(playerSelected < 5 ? playerSelected : playerSelected-4)); // заголовок для диалога
        ad.setItems(mCatsName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        TeamPointCount[playerSelected < 5 ? 0 : 1].setText(Integer.toString(modelGame.addPoint(playerSelected, 1)));
                        checkPointCount();
                        break;
                    case 1:
                        TeamPointCount[playerSelected < 5 ? 0 : 1].setText(Integer.toString(modelGame.addPoint(playerSelected, 2)));
                        checkPointCount();
                        break;
                    case 2:
                        TeamFoulCount[playerSelected < 5 ? 0 : 1].setText(Integer.toString(modelGame.addFoul(playerSelected)));

                        break;

                }
            }
        });
        ad.setCancelable(false);
        ad.create();
        ad.show();
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
        mTimer = new CountDownTimer(modelGame.getTimerVal(), 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                modelGame.addTime((int) millisUntilFinished);
                mTimerLabel.setText(timeToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                mTimerIsOn = false;
                stopGame("Время игры закончилось");
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
    protected void stopGame(String mes){
        stopTimer();
        mTimerLabel.setTextColor(getResources().getColor(R.color.end_of_timer));

        Toast.makeText(MainActivity.instanse, mes, Toast.LENGTH_LONG).show();
    }



}

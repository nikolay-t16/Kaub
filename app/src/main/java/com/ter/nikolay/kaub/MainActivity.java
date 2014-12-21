package com.ter.nikolay.kaub;

import android.app.Activity;
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
    protected TextView mTimerLabel;
    protected CountDownTimer mTimer;
    protected Button btnTimerControl;
    protected int btnTimerVal;
    protected Boolean mTimerIsOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instanse = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimerLabel = (TextView) findViewById(R.id.mTimerLabel);
        btnTimerControl = (Button) findViewById(R.id.btnTimerControl);
        btnTimerControl.setOnClickListener(this);
        mTimerLabel.setOnClickListener(this);
        btnTimerVal = getResources().getInteger(R.integer.game_time);
        mTimerLabel.setText(timeToString( btnTimerVal ) );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {
        if(v==btnTimerControl|| v==mTimerLabel){
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
    public String timeToString(long time){

        String millis =  String.valueOf(time % 10);
        String minute =  String.valueOf(time / 60000);
        String second =  String.valueOf((time % 60000)/1000);






        return minute+":"+second+"."+millis ;
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
                Toast.makeText(MainActivity.instanse, "Время матча вышло", Toast.LENGTH_LONG).show();
                mTimerLabel.setTextColor(getResources().getColor(R.color.end_of_timer));
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
        if(mTimer != null)
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
}

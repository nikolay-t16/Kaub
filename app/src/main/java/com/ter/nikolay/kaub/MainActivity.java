package com.ter.nikolay.kaub;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {
    protected TextView mTimerLabel;
    protected CountDownTimer mTimer;
    protected Button btnTimerControl;
    protected long btnTimerVal;
    protected Boolean mTimerIsOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimerLabel = (TextView) findViewById(R.id.mTimerLabel);
        btnTimerControl = (Button) findViewById(R.id.btnTimerControl);
        btnTimerControl.setOnClickListener(this);
        btnTimerVal = 10 * 60 * 1000;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {
        if(v==btnTimerControl){
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

    public void startTimer() {
        // Create a new CountDownTimer to track the brew time
        mTimer = new CountDownTimer(btnTimerVal, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnTimerVal = millisUntilFinished;
                mTimerLabel.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                mTimerIsOn = false;


                mTimerLabel.setText("Brew Up!");
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

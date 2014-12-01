package com.ter.nikolay.kaub;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;


public class MainActivity extends Activity {
    Chronometer mChronometer;
    Button btnWatchControl;
    Boolean statusWatchControl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        Button btnWatchControl = (Button) findViewById(R.id.btnWatchControl);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClickBtnWatchControl(View view) {
        if (statusWatchControl) {

            mChronometer.stop();
            statusWatchControl = Boolean.FALSE;
           // btnWatchControl.setText(R.string.btnWatchStart);
        } else {

            mChronometer.start();
            statusWatchControl = Boolean.TRUE;
            //btnWatchControl.setText(R.string.btnWatchStop);
        }
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

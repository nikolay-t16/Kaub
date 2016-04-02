package com.ter.nikolay.kaub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "myLogs";

    public static MainActivity instanse;
    protected static ModelGame modelGame;
    protected TextView       mTimerLabel;
    protected TextView[]     playerLabel;

    protected TextView[]     TeamPointCount;
    protected TextView[]     TeamFoulCount;
    protected LinearLayout   statLL;
    public TextView[]        StatTextView;

    protected int            playerSelected = 0;
    protected int            curentSatat = 0;
    protected CountDownTimer mTimer;
    protected Button         btnTimerControl;
    protected Button         btnRestart;
    protected static Boolean        mTimerIsOn = false;

    protected AlertDialog.Builder playerActionDialog;
    protected AlertDialog.Builder statDelDialog;
    Context context;
    DBHelper sqlHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        findElement();
        setOnClickListener();
        context = MainActivity.this;
        createDialog();
        setValues();
        restoreStat();
        Log.i(TAG, "Время!" + Integer.toString(modelGame.getTimerVal()));
        // создаем базу данных
        DBHelper dbOpenHelper = new DBHelper(this, "kaub");

        SQLiteDatabase database= dbOpenHelper.getWritableDatabase();
        ArrayList data = new ArrayList();

        Cursor dataCursor = database.query(
                "sqlite_master",
                new String[] {"name"},
                null, null, null, null,
                "name");
        dataCursor.moveToFirst();


        if(!dataCursor.isAfterLast()){
            do {
               // String image =dataCursor.getString(1);
                String name = dataCursor.getString(0);
                Log.i(TAG, name);

            } while (dataCursor.moveToNext());
        }
        dataCursor.close();
       /* Cursor userCursor = sqlHelper.myDataBase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        userCursor.moveToFirst();
        while (userCursor.isAfterLast() == false) {
            Log.i(TAG, userCursor.getString(0));
            userCursor.moveToNext();
        }*/

    }

    protected void restoreStat(){
        int[][] stat = modelGame.GameStat;
        for (int i = 0; i < stat.length; i += 1){
            if(modelGame.GameStat[i][0]!=0){
                int team_num = modelGame.GameStat[i][0] < 5 ? 0 : 1;
                String stat_text = modelGame.makeStatText(
                        modelGame.GameStat[i][1],
                        modelGame.GameStat[i][0],
                        modelGame.timeToString(modelGame.GameStat[i][2])
                );
                addStatTextView(stat_text);

            }
        }
    }
    protected void initModelGame(){
        modelGame = new ModelGame(1, 21, 5, 600000);
    }

    protected void setValues(){
        instanse = this;
        if(modelGame==null){
            initModelGame();
        }
        setValuesFromModel();
        if(mTimerIsOn){
            startTimer();
        }
    }
    protected void setValuesFromModel(){
        mTimerLabel.setText(modelGame.getStringTimerVal());
        StatTextView = new TextView[100];
        int[][] stat = modelGame.getStat();
        TeamPointCount[0].setText(Integer.toString(stat[0][0]));
        TeamPointCount[1].setText(Integer.toString(stat[0][1]));
        TeamFoulCount[0].setText(Integer.toString(stat[1][0]));
        TeamFoulCount[1].setText(Integer.toString(stat[1][1]));
        checkFoulCount();
        checkPointCount();
    }
    protected void setDefaultValues(){
        instanse = this;
        if(modelGame==null){
        modelGame = new ModelGame(1, 3, 2, 600000);
        }
        mTimerLabel.setText(modelGame.getStringTimerVal());
        StatTextView = new TextView[100];
        int[][] stat = modelGame.getStat();
        TeamPointCount[0].setText(Integer.toString(stat[0][0]));
        TeamPointCount[1].setText(Integer.toString(stat[0][1]));
        TeamFoulCount[0].setText(Integer.toString(stat[1][0]));
        TeamFoulCount[1].setText(Integer.toString(stat[1][1]));
        checkFoulCount();
        checkPointCount();
        if(mTimerIsOn){
            startTimer();
        }
    }

    protected void createDialog(){
        createPlayerActionDialog();
        createStatDelDialog();
    }
    public void deleteStatByView(View v){
        for (int i = 0; i < StatTextView.length; i += 1){
            if(v == MainActivity.instanse.StatTextView[i]){
                MainActivity.instanse.deleteStat(i);

                break;
            }
        }
    }
    public void deleteStat(Integer indexStat){
        statLL.removeView(StatTextView[indexStat]);
        int[][] stat = modelGame.delStat(indexStat);
        TeamPointCount[0].setText(Integer.toString(stat[0][0]));
        TeamPointCount[1].setText(Integer.toString(stat[0][1]));
        TeamFoulCount[0].setText(Integer.toString(stat[1][0]));
        TeamFoulCount[1].setText(Integer.toString(stat[1][1]));
        checkPointCount();
        checkFoulCount();
    }
    protected void createStatDelDialog(){

        String button1String = "Да";
        String button2String = "Нет";

        statDelDialog = new AlertDialog.Builder(context);
        statDelDialog.setTitle("Отменить действие?");  // заголовок
        statDelDialog.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                deleteStat(curentSatat);

            }
        });
        statDelDialog.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        statDelDialog.setCancelable(true);
        statDelDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
    }

    protected void createPlayerActionDialog(){
        final String[] mCatsName ={"1 очко", "2 очка", "фол", "Отмена"};
        playerActionDialog = new AlertDialog.Builder(this);
        playerActionDialog.setTitle("Команда " + (playerSelected < 5 ? '1' : '2') + " игрок " + Integer.toString(playerSelected < 5 ? playerSelected : playerSelected-4)); // заголовок для диалога
        playerActionDialog.setItems(mCatsName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        addPoint(playerSelected, 1);
                        break;
                    case 1:
                        addPoint(playerSelected, 2);
                        break;
                    case 2:
                        addFoul(playerSelected);
                        break;

                }
            }
        });
        playerActionDialog.setCancelable(true);
        playerActionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        playerActionDialog.create();
    }


    protected void findElement() {
        statLL           = (LinearLayout) findViewById(R.id.statLL);
        mTimerLabel      = (TextView) findViewById(R.id.mTimerLabel);
        btnTimerControl  = (Button) findViewById(R.id.btnTimerControl);
        btnRestart       = (Button) findViewById(R.id.btnRestart);
        playerLabel = new TextView[8];

        TeamPointCount = new TextView[2];
        TeamPointCount[0] =(TextView) findViewById(R.id.team1Count);
        TeamPointCount[1] =(TextView) findViewById(R.id.team2Count);

        TeamFoulCount = new TextView[2];
        TeamFoulCount[0] =(TextView) findViewById(R.id.team1FoulCount);
        TeamFoulCount[1] =(TextView) findViewById(R.id.team2FoulCount);

    }

    protected void setOnClickListener() {
        btnTimerControl.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        mTimerLabel.setOnClickListener(this);


    }

    protected void checkPointCount(){
        int CheckPointCount = modelGame.checkPointCount();
        if(CheckPointCount>0){
            stopGame("Команда " + Integer.toString(CheckPointCount) + " выйграла");
        }else{
            mTimerLabel.setTextColor(getResources().getColor(R.color.black));
        }
    }
    protected void checkFoulCount(){
        int ChekFoulCount = modelGame.chekFoulCount();
        if(modelGame.chekFoulCount() > 0){
            Toast.makeText(MainActivity.instanse, "Команда "+Integer.toString(ChekFoulCount)+" превысила лимит фолов", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {
        boolean t = true;
        if (v == btnTimerControl || v == mTimerLabel) {
            t = false;
            onClickBtnTimerControl(v);
        }
        if (v == btnRestart) {
            t = false;
            onClickBtnRestart();
        }

        if(t){
            for (int i = 0; i < playerLabel.length; i += 1){
                if(v == playerLabel[i]){
                    onClickPlayer(v,i+1);
                    break;
                }
            }
        }
        if(t){
            for (int i = 0; i < StatTextView.length; i += 1){
                if(v == StatTextView[i]){
                    curentSatat = i;
                    onClickStatTextView(v);
                    break;
                }
            }
        }


    }
    public void onClickPlayerAction(View v){

        switch (v.getId()) {
            case R.id.onePointPlayer1:
                addPoint(1,1);
                break;
            case R.id.twoPointPlayer1:
                addPoint(1,2);
                break;
            case R.id.foulPlayer1:
                addFoul(1);
                break;
            case R.id.onePointPlayer2:
                addPoint(2,1);
                break;
            case R.id.twoPointPlayer2:
                addPoint(2, 2);
                break;
            case R.id.foulPlayer2:
                addFoul(2);
                break;
            case R.id.onePointPlayer3:
                addPoint(3,1);
                break;
            case R.id.twoPointPlayer3:
                addPoint(3, 2);
                break;
            case R.id.foulPlayer3:
                addFoul(3);
                break;
            case R.id.onePointPlayer4:
                addPoint(4,1);
                break;
            case R.id.twoPointPlayer4:
                addPoint(4, 2);
                break;
            case R.id.foulPlayer4:
                addFoul(4);
                break;
            case R.id.onePointPlayer5:
                addPoint(5,1);
                break;
            case R.id.twoPointPlayer5:
                addPoint(5, 2);
                break;
            case R.id.foulPlayer5:
                addFoul(5);
                break;
            case R.id.onePointPlayer6:
                addPoint(6,1);
                break;
            case R.id.twoPointPlayer6:
                addPoint(6,2);
                break;
            case R.id.foulPlayer6:
                addFoul(6);
                break;
            case R.id.onePointPlayer7:
                addPoint(7,1);
                break;
            case R.id.twoPointPlayer7:
                addPoint(7,2);
                break;
            case R.id.foulPlayer7:
                addFoul(7);
                break;
            case R.id.onePointPlayer8:
                addPoint(8,1);
                break;
            case R.id.twoPointPlayer8:
                addPoint(8,2);
                break;
            case R.id.foulPlayer8:
                addFoul(8);
                break;

        }

    }

    protected void addStatTextView(String statText){
        TextView stat_text_view = (TextView) new TextView(this);

        stat_text_view.setText(statText);
        statLL.addView(stat_text_view, 0);
        StatTextView[modelGame.GetStatCurent()] = stat_text_view;
        //stat_text_view.setOnClickListener(this);
        //set touch listeners
        stat_text_view.setOnTouchListener(new ChoiceTouchStatListener());
//set drag listeners
        stat_text_view.setOnDragListener(new ChoiceStatDragListener());
    }

    protected void addPoint(int playerNum,int points){
        int team_num = playerNum < 5 ? 0 : 1;
        String points_count = Integer.toString(modelGame.addPoint(playerNum, points));
        String stat_text = modelGame.getStringTimerVal() +
                ": Команда " + Integer.toString(team_num) +
                " Игрок " + Integer.toString(playerNum) +
                " забил " + Integer.toString(points) +
                " очк" + (points == 1 ? "о" : "а") + ";";
        addStatTextView(stat_text);
        TeamPointCount[team_num].setText(points_count);


        checkPointCount();
    }
    protected void addFoul(int playerNum){
        int team_num = playerNum < 5 ? 0 : 1;
       String stat_text = modelGame.getStringTimerVal()+
                            ": Команда "+Integer.toString(team_num)+
                            " Игрок " + Integer.toString(playerNum) +
                            " получил фол;"
                            ;
        TeamFoulCount[playerNum < 5 ? 0 : 1].setText(Integer.toString(modelGame.addFoul(playerNum)));
        addStatTextView(stat_text);
        checkFoulCount();
    }
    protected void onClickStatTextView(View v){
        Toast.makeText(MainActivity.instanse, ""+Integer.toString(curentSatat)+" ", Toast.LENGTH_LONG).show();
        statDelDialog.setMessage("Действие " + (String) StatTextView[curentSatat].getText()); // сообщение
        statDelDialog.show();
    }

    protected void onClickPlayer(View v,int playerNum){
        playerSelected = playerNum;
        playerActionDialog.show();
    }

    public void onClickBtnTimerControl(View v) {
        if (mTimerIsOn) {
            stopTimer();
        } else {
            startTimer();

        }
    }

    protected void onClickBtnRestart(){
        restart();
    }
    protected void restart(){
        stopTimer();
        initModelGame();
        setValuesFromModel();
        statLL.removeAllViews();
        Log.i(TAG, "Время!" + Integer.toString(modelGame.getTimerVal()));
    }


    public void startTimer() {
        // Create a new CountDownTimer to track the brew time
        mTimer = new CountDownTimer(modelGame.getTimerVal(), 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                modelGame.addTime((int) millisUntilFinished);
                mTimerLabel.setText(modelGame.timeToString(millisUntilFinished));
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

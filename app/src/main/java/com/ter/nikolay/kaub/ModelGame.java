package com.ter.nikolay.kaub;

/**
 * Created by nikolay on 10.05.2015.
 */
public class ModelGame {
    protected int[] FoulCount;
    protected int   FoulLimit;
    protected int[] PointCount;
    protected int   PointLimit;
    protected int   TimerVal;
   protected int   GameId;
    ModelGame(int gameId, int pointLimit, int foulLimit, int timerLimit){
        GameId      = gameId;
        FoulCount = new int[2];
        PointCount = new int[2];
        PointLimit = pointLimit;
        FoulLimit = foulLimit;
        addTime(timerLimit);

    }

    public int addFoul(int player_num){
        return FoulCount[player_num>4?1:0]++;
    }

    public int addPoint(int player_num, int point_count){
        return PointCount[player_num>4?1:0]+=point_count;
    }

    public void addTime(int timeVal){
        TimerVal = timeVal;
    }

    public int getTimerVal(){
        return TimerVal;
    }


}

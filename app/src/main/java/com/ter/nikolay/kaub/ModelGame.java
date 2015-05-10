package com.ter.nikolay.kaub;

/**
 * Created by nikolay on 10.05.2015.
 */
public class ModelGame {
    /**
     * 1 - номер игрока,
     * 2 - номер действия
     * 3 - время
     */
    protected int[][]   GameStat;
    protected int[]     FoulCount;
    protected int       FoulLimit;
    protected int[]     PointCount;
    protected int       PointLimit;
    protected int       TimerVal;
    protected int       GameId;

    ModelGame(int gameId, int pointLimit, int foulLimit, int timerLimit){
        GameId      = gameId;
        GameStat    = new int[200][3];
        FoulCount   = new int[2];
        PointCount  = new int[2];
        PointLimit  = pointLimit;
        FoulLimit   = foulLimit;
        addTime(timerLimit);

    }

    /**
     *
     * @param player_num
     * @param action_num
     * 1 - 1 очко, 2 - 2 очка, 3 - фол
     */
    public void addStat(int player_num,int action_num){
        int i = 0;
        while (GameStat[i][0]!=0){
         i++;
        };
        GameStat[i][0] = player_num;
        GameStat[i][1] = action_num;
        GameStat[i][2] = TimerVal;
    }

    public int addFoul(int player_num){
        addStat(player_num,3);
        return FoulCount[player_num>4?1:0]+=1;
    }

    public int addPoint(int player_num, int point_count){
        addStat(player_num,point_count);
        return PointCount[player_num>4?1:0]+=point_count;
    }

    public void addTime(int timeVal){
        TimerVal = timeVal;
    }

    public int getTimerVal(){
        return TimerVal;
    }


}

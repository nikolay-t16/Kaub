package com.ter.nikolay.kaub;

/**
 * Created by nikolay on 10.05.2015.
 */
public class ModelGame {
    /**
     * 1 - 1 очко
     * 2 - 2 лчка
     * 3 - Фол
     */
    protected int[][]   GameStat;
    protected int[]     FoulCount;
    protected int       FoulLimit;
    protected int[]     PointCount;
    protected int       PointLimit;
    protected int       TimerVal;
    protected int       GameId;
    protected int       StatCurent;
    ModelGame(int gameId, int pointLimit, int foulLimit, int timerLimit){
        GameId      = gameId;
        GameStat    = new int[200][3];
        FoulCount   = new int[2];
        PointCount  = new int[2];
        PointLimit  = pointLimit;
        FoulLimit   = foulLimit;
        addTime(timerLimit);

    }
    public int GetStatCurent(){
        return StatCurent;
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
        StatCurent = i;
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

    public String timeToString(long time) {
        String millis = String.valueOf(time % 10);
        String minute = String.valueOf(time / 60000);
        String second = String.valueOf((time % 60000) / 1000);
        return minute + ":" + second + "." + millis;
    }

    public String getStringTimerVal(){
        return timeToString(getTimerVal());
    }
    public int checkPointCount(){
        if(PointCount[0]>=PointLimit) {
            return 1;
        }
        if(PointCount[1]>=PointLimit) {
            return 2;
        }
        return 0;
    }
    public int chekFoulCount(){
        if(FoulCount[0]>=FoulLimit) {
            return 1;
        }
        if(FoulCount[1]>=FoulLimit) {
            return 2;
        }
        return 0;
    }

    public int[][] delStat(int curentSatat){
        int[][] res = new int[2][2];
        if(GameStat[curentSatat][1]==1){
            PointCount[getTeamNum(GameStat[curentSatat][0])]--;
        }
        if(GameStat[curentSatat][1]==2){
            PointCount[getTeamNum(GameStat[curentSatat][0])] +=-2;
        }
        if(GameStat[curentSatat][1]==3){
            FoulCount[getTeamNum(GameStat[curentSatat][0])]--;
        }
        res[0] = PointCount;
        res[1] = FoulCount;
        return  res;
    }
    public int getTeamNum(int playerNum){
        return playerNum < 5 ? 0 : 1;
    }
}

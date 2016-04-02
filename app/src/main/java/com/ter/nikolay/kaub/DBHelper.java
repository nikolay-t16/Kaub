package com.ter.nikolay.kaub;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by nikolay on 28.02.2016.
 */


public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "kaub.db"; // Название файла с БД
    private static final int DATABASE_VERSION = 1;            //Версия БД
    private static final String SP_KEY_DB_VER = "db_ver";
    private final Context mContext;

    public DBHelper(Context context, String DB_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        initialize();
    }

    /**
     * Инициализация БД. Создание новой если ранее не существовала.
     */
    private void initialize() {
        if (databaseExists()) {
            Log.w(MainActivity.TAG, "Существует БД");
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            //if (DATABASE_VERSION != dbVersion) {
                Log.w(MainActivity.TAG, "Обновление БД");
                File dbFile = mContext.getDatabasePath(DATABASE_NAME);
                if (!dbFile.delete()) {
                     Log.w(MainActivity.TAG, "Невозможно обновить БД");
                }
            //}
        }
        if (!databaseExists()) {
            Log.w(MainActivity.TAG, "Создание БД");
            createDatabase();
        }
    }

    /**
     * Проверка существования файла БД. Если существует - возвращает true.
     * @return
     */
    private boolean databaseExists() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    /**
     * Создание БД, копирование файла из Assets.
     */
    private void createDatabase() {
        String parentPath = mContext.getDatabasePath(DATABASE_NAME).getParent();
        String path = mContext.getDatabasePath(DATABASE_NAME).getPath();
        File file = new File(parentPath);
        if (!file.exists()) {
            if (!file.mkdir()) {
                  Log.w(MainActivity.TAG, "Невозможно создать папку БД");
                return;
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            Log.w(MainActivity.TAG, "Загрузка файла -1");
            is = mContext.getAssets().open(DATABASE_NAME);
            Log.w(MainActivity.TAG, "Загрузка файла 0");
            os = new FileOutputStream(path);
            Log.w(MainActivity.TAG, "Загрузка файла 1");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            Log.w(MainActivity.TAG, "Загрузка файла 2");
            os.flush();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            Log.w(MainActivity.TAG, "Загрузка файла 3");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SP_KEY_DB_VER, DATABASE_VERSION);
            editor.commit();
            Log.w(MainActivity.TAG, "Загрузка файла 4");
        } catch (IOException e) {
            Log.w(MainActivity.TAG, "Не загрузился файл");
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }




            }
            Log.w(MainActivity.TAG, "Файл загрузился0");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
    }
}
package cn.utsoft.cd.utupdater.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.utsoft.cd.utupdater.config.DownloadConfig;

/**
 * Create By 李波 on 2017/2/8.
 * Function: 下载信息数据库
 * Desc:
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "download.db";
    private static final int VERSION = 1;

    private static final String SQL_CREATE = "create table " + DownloadConfig.DOWNLOAD_TABLE +
            "(_id integer primary key autoincrement, " +
            "url text, " +
            "tag text, " +
            "path text, " +
            "name text, " +
            "versionName text, " +
            "version integer, " +
            "length long, " +
            "progress long, " +
            "finished integer)";

    private static final String SQL_DROP = "drop table if exists " + DownloadConfig.DOWNLOAD_TABLE;
    private static DBHelper sHelper = null;


    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (sHelper == null) {
            sHelper = new DBHelper(context);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}

package cn.utsoft.cd.utupdater.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.utsoft.cd.utupdater.config.DownloadConfig;
import cn.utsoft.cd.utupdater.entity.RequestBean;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public class DownloadDaoImpl {

    private volatile static DownloadDaoImpl impl;
    private DBHelper dbHelper = null;

    public static DownloadDaoImpl getIns(Context context) {
        if (impl == null) {
            synchronized (DownloadDaoImpl.class) {
                if (impl == null) {
                    impl = new DownloadDaoImpl(context);
                }
            }
        }
        return impl;
    }

    private DownloadDaoImpl(Context context) {
        this.dbHelper = DBHelper.getInstance(context);
    }

    public void insertDownloadInfo(RequestBean info) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put("tag", info.tag);
        values.put("url", info.url);
        values.put("name", info.name);
        values.put("versionName", info.versionName);
        values.put("version", info.version);

        values.put("path", info.path);
        values.put("length", info.length);
        values.put("progress", info.progress);

        values.put("finished", info.finished);

        db.insert(DownloadConfig.DOWNLOAD_TABLE, null, values);

        db.close();
    }

    public void deleteDownloadInfo(String tag) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(DownloadConfig.DOWNLOAD_TABLE, "tag = ?", new String[]{tag});

        db.close();
    }

    public void updateDownloadInfo(String tag, long progress, long length) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.execSQL("update " + DownloadConfig.DOWNLOAD_TABLE + " set progress = ?, length = ? where tag = ?",
                new Object[]{progress, length, tag});
        db.close();
    }

    public void updateDownloadFinishInfo(String tag, long finished) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.execSQL("update " + DownloadConfig.DOWNLOAD_TABLE + " set finished = ? where tag = ?",
                new Object[]{finished, tag});
        db.close();
    }

    public List<RequestBean> queryDownloadInfos(String tag) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<RequestBean> list = new ArrayList<>();

        Cursor cursor = db.query(DownloadConfig.DOWNLOAD_TABLE,
                null,
                "tag = ?",
                new String[]{tag},
                null, null, null);

        while (cursor.moveToNext()) {
            RequestBean bean = new RequestBean();
            bean.tag = cursor.getString(cursor.getColumnIndex("tag"));
            bean.url = cursor.getString(cursor.getColumnIndex("url"));
            bean.name = cursor.getString(cursor.getColumnIndex("name"));
            bean.versionName = cursor.getString(cursor.getColumnIndex("versionName"));
            bean.version = cursor.getInt(cursor.getColumnIndex("version"));
            bean.path = cursor.getString(cursor.getColumnIndex("path"));
            bean.length = cursor.getLong(cursor.getColumnIndex("length"));
            bean.progress = cursor.getLong(cursor.getColumnIndex("progress"));
            bean.finished = cursor.getInt(cursor.getColumnIndex("finished"));
            list.add(bean);
        }

        cursor.close();
        db.close();
        return list;
    }

    public RequestBean queryDownloadInfo(String tag) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        RequestBean bean = null;

        Cursor cursor = db.query(DownloadConfig.DOWNLOAD_TABLE,
                null,
                "tag = ?",
                new String[]{tag},
                null, null, null);

        if (cursor.moveToNext()) {
            bean = new RequestBean();
            bean.tag = cursor.getString(cursor.getColumnIndex("tag"));
            bean.url = cursor.getString(cursor.getColumnIndex("url"));
            bean.name = cursor.getString(cursor.getColumnIndex("name"));
            bean.versionName = cursor.getString(cursor.getColumnIndex("versionName"));
            bean.version = cursor.getInt(cursor.getColumnIndex("version"));
            bean.path = cursor.getString(cursor.getColumnIndex("path"));
            bean.length = cursor.getLong(cursor.getColumnIndex("length"));
            bean.progress = cursor.getLong(cursor.getColumnIndex("progress"));
            bean.finished = cursor.getInt(cursor.getColumnIndex("finished"));
        }
        cursor.close();
        db.close();
        return bean;
    }
}

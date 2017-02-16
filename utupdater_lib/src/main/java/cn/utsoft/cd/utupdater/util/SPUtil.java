package cn.utsoft.cd.utupdater.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 李波 on 2017/2/8.
 * Function: SharedPreferences封装工具类
 * Desc:
 */
public class SPUtil {
    private volatile static SPUtil sSPUtil;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private SPUtil(Context context, String fileName) {
        preferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static SPUtil getIns(Context context) {
        if (sSPUtil == null) {
            synchronized (SPUtil.class) {
                if (sSPUtil == null) {
                    sSPUtil = new SPUtil(context.getApplicationContext(),
                            "download_manager_sp");
                }
            }
        }
        return sSPUtil;
    }

    /**
     * 向SP存入指定key对应的数据
     * 其中value可以是String、boolean、float、int、long等各种基本类型的值
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 清空SP里所以数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取SP数据里指定key对应的value。如果key不存在，则返回默认值defValue。
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    /**
     * 判断SP是否包含特定key的数据
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return preferences.contains(key);
    }
}

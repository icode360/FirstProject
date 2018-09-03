package com.icode.firstproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * SharedPreferences工具
 */
public class SpUtils {

    private SharedPreferences mSharedPreferences;

    private static Context sContext;

    private static Map<String, SoftReference<SharedPreferences>> sMap = new HashMap<String, SoftReference<SharedPreferences>>();

    private SpUtils(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public static void setContext(Context sContext) {
        SpUtils.sContext = sContext;
    }

    /**
     * 获取SpUtils对象
     *
     * @param spName
     * @return
     */
    public static SpUtils obtain(String spName) {
        SharedPreferences sharedPreferences = null;
        if (sMap.containsKey(spName)) {
            SoftReference<SharedPreferences> softReference = sMap.get(spName);
            sharedPreferences = softReference.get();
        }

        if (null == sharedPreferences) {
            sharedPreferences = sContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
            sMap.put(spName, new SoftReference<SharedPreferences>(sharedPreferences));
        }
        return new SpUtils(sharedPreferences);
    }

    public static SharedPreferences getSharedPreferences(String spName) {
        return sContext.getSharedPreferences(spName, Context.MODE_MULTI_PROCESS);
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public void save(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public void saveASyn(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public void save(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public void saveASyn(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public void save(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    public void saveASyn(String key, long value) {
        getEditor().putLong(key, value).commit();
    }

    public void save(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public void saveASyn(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public void save(String key, float value) {
        getEditor().putFloat(key, value).apply();
    }

    public void saveASyn(String key, float value) {
        getEditor().putFloat(key, value).commit();
    }

    public void save(String key, Serializable value) {
        if (value instanceof Integer) {
            getEditor().putInt(key, (Integer) value).apply();
        } else if (value instanceof String) {
            getEditor().putString(key, (String) value).apply();
        } else if (value instanceof Long) {
            getEditor().putLong(key, (Long) value).apply();
        } else if (value instanceof Boolean) {
            getEditor().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Float) {
            getEditor().putFloat(key, (Float) value).apply();
        }
    }

    public void saveASyn(String key, Serializable value) {
        if (value instanceof Integer) {
            getEditor().putInt(key, (Integer) value).commit();
        } else if (value instanceof String) {
            getEditor().putString(key, (String) value).commit();
        } else if (value instanceof Long) {
            getEditor().putLong(key, (Long) value).commit();
        } else if (value instanceof Boolean) {
            getEditor().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Float) {
            getEditor().putFloat(key, (Float) value).commit();
        }
    }

    public void remove(String... keys) {
        if (null != keys && keys.length > 0) {
            for (String key : keys) {
                getEditor().remove(key);
            }
            getEditor().apply();
        }
    }

    public void removeASyn(String... keys) {
        if (null != keys && keys.length > 0) {
            for (String key : keys) {
                getEditor().remove(key);
            }
            getEditor().commit();
        }
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public long getLong(String key) {
        return getLong(key, -1L);
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public float getFloat(String key) {
        return getFloat(key, -1f);
    }

    public float getFloat(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

}

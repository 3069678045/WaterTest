package com.heyi.testing.app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.qihoo360.mobilesafe.core.BuildConfig;
import com.qihoo360.replugin.*;
import com.heyi.testing.greendao.DaoMaster;
import com.heyi.testing.greendao.DaoSession;

/**
 * AppLication class
 *
 * @author auser
 * @date 11/2/2019
 */
public class AppLication extends RePluginApplication {
    public static AppLication instance;
    private static Context context;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static AppLication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 启动调试
        RePlugin.enableDebugger(base, BuildConfig.DEBUG);
    }

    @Override
    protected RePluginConfig createConfig() {
        RePluginConfig c = new RePluginConfig();
        // 允许“插件使用宿主类”。默认为“关闭”
        c.setUseHostClassIfNotFound(true);
        // FIXME RePlugin默认会对安装的外置插件进行签名校验，这里先关掉，避免调试时出现签名错误
        c.setVerifySign(!BuildConfig.DEBUG);
        // 针对“安装失败”等情况来做进一步的事件处理
        c.setEventCallbacks(new HostEventCallbacks(this));
        // FIXME 若宿主为Release，则此处应加上您认为"合法"的插件的签名，例如，可以写上"宿主"自己的。
        // RePlugin.addCertSignature("AAAAAAAAA");

        // 在Art上，优化第一次loadDex的速度
        // c.setOptimizeArtLoadDex(true);
        return c;
    }

    @Override
    protected RePluginCallbacks createCallbacks() {
        return new HostCallbacks(this);
    }

    /**
     * 宿主针对RePlugin的自定义行为
     */
    private class HostCallbacks extends RePluginCallbacks {
        private static final String TAG = "HostCallbacks";

        private HostCallbacks(Context context) {
            super(context);
        }

        @Override
        public boolean onPluginNotExistsForActivity(Context context, String plugin, Intent intent, int process) {
            // FIXME 当插件"没有安装"时触发此逻辑，可打开您的"下载对话框"并开始下载。
            // FIXME 其中"intent"需传递到"对话框"内，这样可在下载完成后，打开这个插件的Activity
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onPluginNotExistsForActivity: Start download... p=" + plugin + "; i=" + intent);
            }
            return super.onPluginNotExistsForActivity(context, plugin, intent, process);
        }


        /*@Override
        public PluginDexClassLoader createPluginClassLoader(PluginInfo pi, String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
            String odexName = pi.makeInstalledFileName() + ".dex";
            if (RePlugin.getConfig().isOptimizeArtLoadDex()) {
                Dex2OatUtils.injectLoadDex(dexPath, optimizedDirectory, odexName);
            }

            long being = System.currentTimeMillis();
            PluginDexClassLoader pluginDexClassLoader = super.createPluginClassLoader(pi, dexPath, optimizedDirectory, librarySearchPath, parent);

            if (BuildConfig.DEBUG) {
                Log.d(Dex2OatUtils.TAG, "createPluginClassLoader use:" + (System.currentTimeMillis() - being));
                String odexAbsolutePath = (optimizedDirectory + File.separator + odexName);
                Log.d(Dex2OatUtils.TAG, "createPluginClassLoader odexSize:" + InterpretDex2OatHelper.getOdexSize(odexAbsolutePath));
            }

            return pluginDexClassLoader;
        }*/

    }

    private class HostEventCallbacks extends RePluginEventCallbacks {

        private static final String TAG = "HostEventCallbacks";

        public HostEventCallbacks(Context context) {
            super(context);
        }

        @Override
        public void onInstallPluginFailed(String path, InstallResult code) {
            // FIXME 当插件安装失败时触发此逻辑。您可以在此处做“打点统计”，也可以针对安装失败情况做“特殊处理”
            // 大部分可以通过RePlugin.install的返回值来判断是否成功
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onInstallPluginFailed: Failed! path=" + path + "; r=" + code);
            }
            super.onInstallPluginFailed(path, code);
        }

        @Override
        public void onStartActivityCompleted(String plugin, String activity, boolean result) {
            // FIXME 当打开Activity成功时触发此逻辑，可在这里做一些APM、打点统计等相关工作
            super.onStartActivityCompleted(plugin, activity, result);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        instance = this;
        setDatabase();
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
    }

    /**
     * 设置greenDAO
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

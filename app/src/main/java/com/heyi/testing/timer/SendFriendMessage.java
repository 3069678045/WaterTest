package com.heyi.testing.timer;

import android.util.Log;
import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;

import java.util.List;


/**
 * SendFriendMessage class
 *
 * @author auser
 * @date 11/5/2019
 */
public class SendFriendMessage {
    private static final String TAG = "ContentValues";
    private static final String chatMsg = "微信2获取聊天消息!*!0";
    private static final String newFriendMsg = "微信4获取添加好友人的信息!*!0";
    private static SendFriendMessage instance;
    private boolean isRunning = false;
    private Thread runThread = new Thread(() -> {
        while (isRunning) {
            try {
                Thread.sleep(6000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaoList = userDaoDao.queryBuilder().where(UserDaoDao.Properties.Message.eq(chatMsg)).limit(1).list();
            if (userDaoList.size() == 0) {
                userDaoDao.insertOrReplace(new UserDao(null, chatMsg, System.currentTimeMillis()));
                Log.e(TAG, "调用了发送聊天消息:" + System.currentTimeMillis());

            }
            userDaoList = userDaoDao.queryBuilder().where(UserDaoDao.Properties.Message.eq(newFriendMsg)).limit(1).list();
            if (userDaoList.size() == 0) {
                userDaoDao.insertOrReplace(new UserDao(null, newFriendMsg, System.currentTimeMillis()));
                Log.e(TAG, "调用了发送新好友消息:" + System.currentTimeMillis());
            }

        }
    });

    private SendFriendMessage() {
    }

    public static SendFriendMessage getInstance() {
        if (instance == null) {
            instance = new SendFriendMessage();
        }
        return instance;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void Strat() {
        if (this.isRunning) return;
        this.isRunning = true;
        runThread.start();
    }

    public void Stop() {
        this.isRunning = false;
        instance = null;
    }
}

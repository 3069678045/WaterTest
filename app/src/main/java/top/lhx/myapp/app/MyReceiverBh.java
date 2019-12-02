package top.lhx.myapp.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import top.lhx.myapp.KeepAlive_Activity;
import top.lhx.myapp.bean.EventMessage;

/**
 * 监听广播锁屏、黑屏
 */
public class MyReceiverBh extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_OFF)) //锁屏、黑屏
        {
            System.out.println("=====锁屏=>>");
            //启动保活Activity
            Intent aIntent = new Intent(context, KeepAlive_Activity.class);
            context.startActivity(aIntent);
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) //亮屏
        {
            System.out.println("=====亮屏=>>");
            //移除保活Activity
            EventBus.getDefault().post(new EventMessage(1));

        }
    }
}
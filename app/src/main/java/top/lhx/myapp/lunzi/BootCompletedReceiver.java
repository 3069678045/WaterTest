package top.lhx.myapp.lunzi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import top.lhx.myapp.StartActivity;

/**
 * BootCompletedReceiver class
 *
 * @author auser
 * @date 11/2/2019
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 开机后执行的代码
        Intent intent2 = new Intent(context, StartActivity.class);
        Log.e("aaa", "执行了开启广播ser");
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);
    }
}

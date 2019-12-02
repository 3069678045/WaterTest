package top.lhx.myapp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.*;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.rabbitmq.client.*;
import top.lhx.myapp.R;
import top.lhx.myapp.app.AppLication;
import top.lhx.myapp.greendao.DaoUser;
import top.lhx.myapp.greendao.DaoUserDao;
import top.lhx.myapp.greendao.UserDao;
import top.lhx.myapp.greendao.UserDaoDao;
import top.lhx.myapp.utils.SpUtil;
import top.lhx.myapp.utils.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Myservices class
 *
 * @author auser
 * @date 11/2/2019
 */
public class Myservices extends Service {
    public static final int NOTICE_ID = 100;
    private final static String TAG = "Myservices";
    private static String exchangeName = "lala";
    private static Myservices instance;
    public String packageName = "com.tencent.mm";
    public String message;
    Thread subscribeThread;
    private MediaPlayer mMediaPlayer;
    private ConnectionFactory factory;
    private String mMessage;
    private String hostName = "MQ";
    private int portNum = 5672;
    private Connection connection;
    private String[] messgeTextzu;
    @SuppressLint("HandlerLeak")
    Handler incomingMessageHandler = new Handler() {
        public void handleMessage(Message msg) {

            message = msg.getData().getString("msg");
            messgeTextzu = message.split("!\\*!");

            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            userDaoDao.insertOrReplace(new UserDao(null,message, System.currentTimeMillis()));
            Log.e("aaa", "msg:" + message);

        }
    };

    public Myservices() {
    }

    public static synchronized Myservices getInstance() {
        return instance;
    }


//private static final String EXCHANGE = "Signal";

    private static void setInstance(Myservices service) {
        instance = service;
    }

    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("KeepAppAlive");
            builder.setContentText("DaemonService is runing...");
            startForeground(NOTICE_ID, builder.build());
            // 如果觉得常驻通知栏体验不好
            // 可以通过启动CancelNoticeService，将通知移除，oom_adj值不变
            Intent intent = new Intent(this, CancelNoticeService.class);
            startService(intent);
        } else {
            startForeground(NOTICE_ID, new Notification());
        }

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wu);
        mMediaPlayer.setLooping(true);
        setInstance(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("aaa", "启动services");
        startService(new Intent(this, MyAccessibilityService.class));
        this.factory = new ConnectionFactory();
        this.factory.setHost(hostName);
        this.factory.setUsername("admin");
        this.factory.setPassword("admin");
        this.factory.setVirtualHost("/");
        this.factory.setPort(portNum);

        this.subscribe(incomingMessageHandler);
        return START_STICKY;
    }


    private void startOpenFriendCircle() {
        Log.e("aaa", "打开===");
        if (!Utils.isAppAvilible(this, this.packageName)) {
            Toast.makeText(this, "未安装微信", Toast.LENGTH_SHORT).show();
        } else if (Utils.isNeededPermissionsGranted(this)) {
            this.startApp(this.packageName);
        }

    }

    private void startApp(String packageName) {
        Log.e("aaa", "打开===startapp");
        PackageManager packageManager = this.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        }
    }


    void subscribe(final Handler handler) {
        this.subscribeThread = new Thread(() -> {

            try {
                startPlayMusic();
                connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.basicQos(1);
                //channel.exchangeDeclare(exchangeName, "fanout");//direct  fanout
                channel.exchangeDeclare(exchangeName, "direct", true, false, null);
                //String mQueue = channel.queueDeclare().getQueue();
                String mQueue = SpUtil.get("SerialNumber", "");
                Log.e("aaa", "mqduilie" + mQueue);
                channel.queueDeclare(mQueue, true, false, false, null);
                channel.queueBind(mQueue, exchangeName, mQueue);
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        super.handleDelivery(consumerTag, envelope, properties, body);
                        new Thread(() -> {
                            startPlayMusic();
                            startService(new Intent(Myservices.this, MyAccessibilityService.class));
                        }).start();

                        Myservices.this.mMessage = new String(body, StandardCharsets.UTF_8);
                        Log.e("aaa", "[_] [ ] [x] Received '" + envelope.getRoutingKey() + "':'" + Myservices.this.mMessage + "'");
                        Message msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", Myservices.this.mMessage);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                };

                channel.basicConsume(mQueue, true, consumer);
            } catch (IOException e) {
                Log.e("aaa", "e1" + e);
                e.printStackTrace();
            } catch (TimeoutException e) {
                Log.e("aaa", "e2" + e);
                e.printStackTrace();
            }

            Log.e("aaa", "run: ");
        });
        this.subscribeThread.start();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 如果Service被杀死，干掉通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mManager.cancel(NOTICE_ID);
        }
        Log.d(TAG, "DaemonService---->onDestroy，前台service被杀死");
        // 重启自己
        Intent intent = new Intent(getApplicationContext(), Myservices.class);
        startService(intent);

        stopPlayMusic();
        startService(new Intent(getApplicationContext(), Myservices.class));

    }

    private void startPlayMusic() {
        if (mMediaPlayer != null) {

            Log.e(TAG, "启动后台播放音乐");
            mMediaPlayer.start();
        }
    }

    private void stopPlayMusic() {
        if (mMediaPlayer != null) {
            Log.e(TAG, "关闭后台播放音乐");
            mMediaPlayer.stop();
        }
    }
}

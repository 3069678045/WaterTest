package com.heyi.testing.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.*;

/**
 * Created by shenhua on 1/17/2017.
 * Email shenhuanet@126.com
 */
public class FileUtils1 {

    private static String SD_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator;

    public static void copyAll(Context cxt) {
        File dir = new File(SD_DIR);
        copySelf(cxt,"alivc_resource");
        dir.mkdirs();
    }

    public static void copySelf(Context cxt, String root) {
        try {
            String[] files = cxt.getAssets().list(root);
            if(files != null && files.length > 0) {
                File subdir = new File(SD_DIR + root);
                if (!subdir.exists()) {
                    subdir.mkdirs();
                }
                for (String fileName : files) {
                    File file = new File(SD_DIR + root + File.separator + fileName);
                    if (file.exists()&&!file.isDirectory()){
                        continue;
                    }
                    copySelf(cxt,root + "/" + fileName);
                }
            }else{
                OutputStream myOutput = new FileOutputStream(SD_DIR+root);
                InputStream myInput = cxt.getAssets().open(root);
                byte[] buffer = new byte[1024 * 8];
                int length = myInput.read(buffer);
                while(length > 0)
                {
                    myOutput.write(buffer, 0, length);
                    length = myInput.read(buffer);
                }

                myOutput.flush();
                myInput.close();
                myOutput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

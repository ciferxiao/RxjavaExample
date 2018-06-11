package cifer.com.rxjavaexample;

import android.util.Log;

/**
 * Created by cifer on 2018/5/30 10:25.
 */

public class Translation1 {
    private int status;
    private content content;
    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void show() {

        Log.d("xiao111", "翻译内容 = " + content.out);

    }
}

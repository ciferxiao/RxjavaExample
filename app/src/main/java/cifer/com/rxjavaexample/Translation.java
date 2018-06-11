package cifer.com.rxjavaexample;

import android.util.Log;

/**
 * Created by cifer on 2018/5/29 15:38.
 */
class Translation {

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
     void show() {
        Log.d("xiao111", content.out );
    }

}


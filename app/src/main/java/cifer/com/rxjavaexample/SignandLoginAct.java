package cifer.com.rxjavaexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cifer
 * on 2018/5/30 10:23.
 *
 */

public class SignandLoginAct extends AppCompatActivity {
    final String TAG = "xiao111";
    // 定义Observable接口类型的网络请求对象
    Observable<Translation> observable1;
    Observable<Translation1> observable2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("xiao111"," enter this ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Signandlogin request = retrofit.create(Signandlogin.class);

        observable1 = request.getcall();
        observable2 = request.getcall_1();

        observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Translation>() {
                    @Override
                    public void accept(Translation translation) throws Exception {
                        Log.d(TAG, "第1次网络请求成功");
                        translation.show();
                        // 对第1次网络请求返回的结果进行操作 = 显示翻译结果
                    }
                })

                .observeOn(Schedulers.io())
                .flatMap(new Function<Translation, ObservableSource<Translation1>>() {
                    @Override
                    public ObservableSource<Translation1> apply(Translation translation) throws Exception {
                        return observable2;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Translation1>() {
                    @Override
                    public void accept(Translation1 result) throws Exception {
                        Log.d(TAG, "第2次网络请求成功");
                        result.show();
                        // 对第2次网络请求返回的结果进行操作 = 显示翻译结果
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("xiao111 登录失败");
                    }
                });
    }
}
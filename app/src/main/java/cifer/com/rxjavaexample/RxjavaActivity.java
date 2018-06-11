package cifer.com.rxjavaexample;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by cifer on 2018/5/29 17:13.
 */

public class RxjavaActivity extends AppCompatActivity {
    final String TAG = "xiao111";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //function 1
        Observable<Integer> observable = new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {

            }
        };
        //RxJava 提供了其他方法用于 创建被观察者对象Observable
        //function 2
        Observable observable1 = Observable.just(1, 2, 3);
        // 将会依次调用：
        // onNext("A");
        // onNext("B");
        // onNext("C");
        // onCompleted();

        //function 3
        String[] strings = new String[]{"A", "B", "C"};
        Observable observable2 = Observable.fromArray(strings);
        // 将会依次调用：
        // onNext("A");
        // onNext("B");
        // onNext("C");
        // onCompleted();


        //观察者的创建
        //function 1
        // 1. 创建观察者 （Observer ）对象
        Observer<Integer> observer = new Observer<Integer>() {
            // 2. 创建对象时通过对应复写对应事件方法 从而 响应对应事件


            // 观察者接收事件前，默认最先调用复写 onSubscribe（）
            @Override
            public void onSubscribe(Disposable d) {

            }

            // 当被观察者生产Next事件 & 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onNext(Integer value) {

            }

            // 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onError(Throwable e) {

            }

            // 当被观察者生产Complete事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onComplete() {

            }
        };

        //function 2
        // 说明：Subscriber类 = RxJava 内置的一个实现了 Observer 的抽象类，对 Observer 接口进行了扩展
        // 1. 创建观察者 （Observer ）对象
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {

            // 2. 创建对象时通过对应复写对应事件方法 从而 响应对应事件
            // 观察者接收事件前，默认最先调用复写 onSubscribe（）
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            // 当被观察者生产Next事件 & 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "对Next事件作出响应" + value);
            }

            // 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            // 当被观察者生产Complete事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        };
        //<--特别注意：2种方法的区别，即Subscriber 抽象类与Observer 接口的区别 -->
        // 相同点：二者基本使用方式完全一致（实质上，在RxJava的 subscribe 过程中，Observer总是会先被转换成Subscriber再使用）
        // 不同点：Subscriber抽象类对 Observer 接口进行了扩展，新增了两个方法：
        // 1. onStart()：在还未响应事件前调用，用于做一些初始化工作
        // 2. unsubscribe()：用于取消订阅。在该方法被调用后，观察者将不再接收 & 响应事件
        // 调用该方法前，先使用 isUnsubscribed() 判断状态，确定被观察者Observable是否还持有观察者Subscriber的引用，如果引用不能及时释放，就会出现内存泄露

        //步骤三
        observable.subscribe(observer);
        // 或者 observable.subscribe(subscriber)；

        //<-- Observable.subscribe(Subscriber) 的内部实现 -->

        /*public Subscription subscribe(Subscriber subscriber) {
            subscriber.onStart();
            // 步骤1中 观察者  subscriber抽象类复写的方法，用于初始化工作
            onSubscribe.call(subscriber);
            // 通过该调用，从而回调观察者中的对应方法从而响应被观察者生产的事件
            // 从而实现被观察者调用了观察者的回调方法 & 由被观察者向观察者的事件传递，即观察者模式
            // 同时也看出：Observable只是生产事件，真正的发送事件是在它被订阅的时候，即当 subscribe() 方法执行时
        }*/

        //Rxjava 的链式操作
        io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
            // 1. 创建被观察者 & 生产事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            // 2. 通过通过订阅（subscribe）连接观察者和被观察者
            // 3. 创建观察者 & 定义响应事件的行为
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }
            // 默认最先调用复写的 onSubscribe（）

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "对Next事件" + value + "作出响应");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }

        });

        //注：整体方法调用顺序：观察者.onSubscribe（）> 被观察者.subscribe（）> 观察者.onNext（）>观察者.onComplete()
    }
}





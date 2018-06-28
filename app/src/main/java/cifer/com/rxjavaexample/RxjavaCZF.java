package cifer.com.rxjavaexample;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cifer on 2018/6/27 15:30.
 * rxjava 的一些操作符说明
 */
public class RxjavaCZF {
    private final String TAG = "xiao111";
    private Observable<Integer> observableInt;
    private Observable<String> observableString;


    public void RxjavaTest(){
        //create ：产生一个 Obserable 被观察者对象
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) {
                Log.e(TAG, "Observable emit 1" + "\n");
                e.onNext(1);
                Log.e(TAG, "Observable emit 2" + "\n");
                e.onNext(2);
                Log.e(TAG, "Observable emit 3" + "\n");
                e.onNext(3);
                e.onComplete();
                Log.e(TAG, "Observable emit 4" + "\n" );
                e.onNext(4);
   
            }
        }).subscribe(new Observer<Object>() {
            private int i;
            // Disposable 概念:这个东西可以直接调用切断，当它的 isDisposed() 返回为 false 的时候，
            // 接收器能正常接收事件，但当其为 true 的时候，接收器停止了接收。
            // 所以可以通过此参数动态控制接收事件了。
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe : " + d.isDisposed() + "\n" );
                disposable = d;
            }

            @Override
            public void onNext(Object value) {
                Log.e(TAG, "onNext : value : " + value + "\n" );
                i++;
                if(i == 2){
                    disposable.dispose();
                    Log.e(TAG, "onNext : isDisposable : " + disposable.isDisposed() + "\n");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError : value : " + e.getMessage() + "\n" );
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete" + "\n" );
            }
        });
    }

    //map 基本作用就是将一个 Observable 通过某种函数关系，转换为另一种 Observable
    public void Rxjavamap(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Object, Object>() {
            @Override
            public Object apply(Object o) throws Exception {
                return  o.toString();
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.d(TAG,"map test value =  " + o + "\n");
            }
        });
    }

    //zip 专用于合并事件，该合并不是连接（连接操作符后面会说），
    // 而是两两配对，也就意味着，最终配对出的 Observable 发射事件数目只和少的那个相同。
    public void RxTestZip(){
        Observable.zip(observableInt, observableString, new BiFunction<Integer, String, Object>() {
            @Override
            public Object apply(Integer o, String o2) throws Exception {
                return o + o2;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.d(TAG,"ziptest value = " + o + "\n");
            }
        });
    }
    //注：zip 组合事件的过程就是分别从发射器 A 和发射器 B 各取出一个事件来组合，并且一个事件只能被使用一次，
    // 组合的顺序是严格按照事件发送的顺序来进行的
    //最终接收器收到的事件数量是和发送器发送事件最少的那个发送器的发送事件数目相同


    //concat : 单一的把两个发射器连接成一个发射器
    public void RxjavaConcat(){
        Observable.concat(Observable.just(1,2,4),Observable.just(3,7,8)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "concat : "+ integer + "\n" );
            }
        });
    }

    //flatmap:把一个发射器  Observable 通过某种方法转换为多个 Observables，
    // 然后再把这些分散的 Observables装进一个单一的发射器 Observable
    public void RxjavaFlatmap(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Object, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Object o) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + o);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                Log.e(TAG, "flatMap : accept : " + o + "\n");
            }
        });
    }

    //concatMap 与 FlatMap 的唯一区别就是 concatMap 保证了顺序
    public void RxjavaConcatmap(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).concatMap(new Function<Object, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Object o) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + o);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                Log.e(TAG, "flatMap : accept : " + o + "\n");
            }
        });
    }

    //distinct 相同元素只传输一次
    public void RxjavaDistinct(){
        Observable.just(1, 1, 1, 2, 2, 3, 4, 4,5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "distinct : " + integer + "\n");
                        //log : 1,2,3,4,5
                    }
                });
    }

    //filter: 过滤器，可以接受一个参数，让其过滤掉不符合我们条件的值
    public void RxjavaFilter(){
        Observable.just(1, 20, 65, -5, 7, 19)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer >= 10;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                Log.e(TAG, "filter : " + integer + "\n");
            }
        });
    }

    //buffer:
    public void RxjavaBuffer(){
        Observable.just(1,2,3,4,5).buffer(2,5)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.d(TAG," buffer test vlaues == " + integers);
                    }
                });


    }



}

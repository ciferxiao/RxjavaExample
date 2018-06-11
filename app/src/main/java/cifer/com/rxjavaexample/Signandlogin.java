package cifer.com.rxjavaexample;

import retrofit2.http.GET;

/**
 * Created by cifer on 2018/5/30 10:24.
 */

public interface Signandlogin {
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20register")
    io.reactivex.Observable<Translation> getcall();

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20login")
    io.reactivex.Observable<Translation1> getcall_1();
}

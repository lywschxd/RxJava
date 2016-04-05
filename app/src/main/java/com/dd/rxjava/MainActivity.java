package com.dd.rxjava;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.btn1)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn2)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn3)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn4)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn5)).setOnClickListener(this);
        Thread current = Thread.currentThread();
        Log.d("Rxjava", "Main thread id:"+current.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                String names[]= {"Li0", "Li1", "Li2", "Li3", "Li4", "Li5", "Li6", "Li7", "Li8"};
                Observable.from(names).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("RxJava", "s:"+s);
                    }
                });
                break;
            case R.id.btn2:
                Observable.create(new Observable.OnSubscribe<Drawable>() {
                    @Override
                    public void call(Subscriber<? super Drawable> subscriber) {
                        try {
                            Drawable drawable = getResources().getDrawable(R.drawable.timg);
                            subscriber.onNext(drawable);
                            subscriber.onCompleted();
                        }catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                }).subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {
                        Log.d("RxJava", "Ok");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RxJava", "error");
                    }
                    @Override
                    public void onNext(Drawable drawable) {
                        ((ImageView)findViewById(R.id.image)).setImageDrawable(drawable);
                    }
                });
                break;
            case R.id.btn3:
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        Thread current = Thread.currentThread();
                        Log.d("Rxjava", "Observable thread id:"+current.getId());
                        subscriber.onNext(100);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Integer integer) {
                        Thread current = Thread.currentThread();
                        Log.d("Rxjava", "Subscriber thread id:"+current.getId());
                    }
                });
                break;
            case R.id.btn4:
                Observable.just("hello").observeOn(Schedulers.newThread()).map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        Thread thread = Thread.currentThread();
                        Log.d("RxJava", "map0:"+thread);
                        return s.hashCode();
                    }
                }).observeOn(Schedulers.io()).map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        Thread thread = Thread.currentThread();
                        Log.d("RxJava", "map1:"+thread);
                        return "world";
                    }
                }).observeOn(Schedulers.computation()).map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        Thread thread = Thread.currentThread();
                        Log.d("RxJava", "map1:"+thread);
                        return s.hashCode();
                    }
                }).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("RxJava", "integer:"+integer);
                    }
                });
                break;
            case R.id.btn5:
                startActivity(new Intent(this, RxBusActivity.class));
                break;
        }
    }
}

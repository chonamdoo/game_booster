package com.couchgram.gamebooster.util;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.couchgram.gamebooster.data.AccessbilityAppEvent;
import com.couchgram.gamebooster.data.CallSate;
import com.couchgram.gamebooster.ui.widget.listener.SettingMenuClickListener;
import com.mobvista.msdk.MobVistaConstans;
import com.mobvista.msdk.MobVistaSDK;
import com.mobvista.msdk.out.MobVistaSDKFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

public class TestActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
//    val callStateCheckPublish = PublishProcessor.create<CallSate>()
//    val appCheckPublish = PublishProcessor.create<AccessbilityAppEvent>()
    PublishProcessor<CallSate> callStateCheckPublish = PublishProcessor.create();
    PublishProcessor<AccessbilityAppEvent> appCheckPublish = PublishProcessor.create();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyPermissions.requestPermissions(this, "ddfdsfasf",
                9880, Manifest.permission.READ_CONTACTS);

        Flowable.zip(callStateCheckPublish, appCheckPublish, new BiFunction<CallSate, AccessbilityAppEvent, Object>() {
            @Override
            public Object apply(@NonNull CallSate callSate, @NonNull AccessbilityAppEvent accessbilityAppEvent) throws Exception {
                return null;
            }
        }).subscribeOn(Schedulers.io());

    }

    public <T> ObservableTransformer<T, T> applyProgress() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
                return observable.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        Log.v("DEBUG600","doOnSubscribe");
                    }
                })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.v("DEBUG600","doOnDispose");
                            }
                        });

            }
        };
    }
    private void test(){


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtils.INSTANCE.v("DEBUG700","onPermissionsGranted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
    /*public Observable.Transformer<T, T> io_main(final RxAppCompatActivity context) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                Observable<T> observable = (Observable<T>) tObservable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                DialogHelper.showProgressDlg(context, mMessage);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxLifecycle.bindUntilEvent(context.lifecycle(), ActivityEvent.STOP));
                return observable;
            }
        };
    }*/


}

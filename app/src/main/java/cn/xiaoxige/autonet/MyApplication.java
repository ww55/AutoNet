package cn.xiaoxige.autonet;

import android.app.Application;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.Map;

import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import io.reactivex.FlowableEmitter;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Headers;


/**
 * Created by 小稀革 on 2017/11/26.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Map<String, String> heads = new ArrayMap<>();
        heads.put("token", "0");
        heads.put("userId", "A");

        Map<String, String> domainNames = new ArrayMap<>();
        domainNames.put("pppig", "https://www.pangpangpig.com");
        domainNames.put("upFile", "http://testimage.xxxx.com:8080");
        domainNames.put("test", "http://192.168.1.38:8080");

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
            }
        });

        AutoNetConfig config = new AutoNetConfig.Builder()
                .isOpenStetho(true)
                .setDefaultDomainName("https:www.baidu.com")
                .setHeadParam(heads)
                .setDomainName(domainNames)
                .build();

        AutoNet.getInstance().initAutoNet(this, config).setEncryptionCallback(new IAutoNetEncryptionCallback() {
            @Override
            public String encryption(Long key, String encryptionContent) {
                Log.e("TAG", "加密信息： key = " + key + ", encryptionContent = " + encryptionContent);
                return encryptionContent;
            }
        }).setHeadsCallback(new IAutoNetHeadCallBack() {
            @Override
            public void head(Object flag, Headers headers) {
                Log.e("TAG", "flag = " + flag);
                Log.e("TAG", "头部回调：" + headers);
            }
        }).setBodyCallback(new IAutoNetBodyCallBack() {
            @Override
            public boolean body(Object flag, String object, FlowableEmitter emitter) {
                Log.e("TAG", "flag = " + flag);
                Log.e("TAG", "body： " + object);
                return false;
            }

        }).updateOrInsertDomainNames("jsonTestBaseUrl", "http://api.news18a.com");


    }
}

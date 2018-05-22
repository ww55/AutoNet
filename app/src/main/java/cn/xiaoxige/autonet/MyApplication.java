package cn.xiaoxige.autonet;

import android.app.Application;
import android.util.ArrayMap;
import android.util.Log;

import java.util.Map;

import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import okhttp3.Headers;


/**
 * Created by 小稀革 on 2017/11/26.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Map<String, String> heads = new android.support.v4.util.ArrayMap<>();
        heads.put("token", "0");
        heads.put("userId", "A");

        Map<String, String> domainNames = new android.support.v4.util.ArrayMap<>();
        domainNames.put("pppig", "https://www.pangpangpig.com");
        domainNames.put("upFile", "http://testimage.hxkid.com:4869");

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
            public void head(Headers headers) {
                Log.e("TAG", "头部回调：" + headers);
            }
        }).updateOrInsertDomainNames("jsonTestBaseUrl", "http://api.news18a.com");


    }
}

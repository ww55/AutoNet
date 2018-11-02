package cn.xiaoxige.autonet_api.interceptor;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import cn.xiaoxige.autonet_api.constant.AutoNetConstant;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author by xiaoxige on 2018/5/20.
 * AutoNet default network interceptor
 */

public class AutoDefaultInterceptor implements Interceptor {

    private Object mFlag;
    private String extraDynamicParam;
    private Map<String, String> heads;
    private Long encryptionKey;
    private boolean isEncryption;
    private IAutoNetEncryptionCallback encryptionCallback;
    private IAutoNetHeadCallBack headCallBack;

    public AutoDefaultInterceptor(Object flag, String extraDynamicParam, Map<String, String> heads, Long encryptionKey, Boolean isEncryption,
                                  IAutoNetEncryptionCallback encryptionCallback, IAutoNetHeadCallBack headCallBack) {
        this.mFlag = flag;
        this.extraDynamicParam = extraDynamicParam;
        this.heads = heads;
        this.encryptionKey = encryptionKey;
        this.isEncryption = isEncryption;
        this.encryptionCallback = encryptionCallback;
        this.headCallBack = headCallBack;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // init head
        request = splicingHeads(request);
        // init params
        request = splicingParams(request);
        // init encryption
        request = encryptionParams(request);

        Response proceed = chain.proceed(request);
        if (headCallBack != null) {
            headCallBack.head(this.mFlag, proceed.headers());
        }
        return proceed;
    }

    private Request encryptionParams(Request request) {

        if (!this.isEncryption) {
            return request;
        }

        String method = request.method();
        if (AutoNetConstant.GET.equals(method) || AutoNetConstant.DELETE.equals(method)) {
            HttpUrl httpUrl = request.url();
            String url = httpUrl.url().toString();
            String[] split = url.split("\\?");
            if (split.length == 2) {
                if (this.encryptionCallback != null) {
                    String encryptionParams = this.encryptionCallback.encryption(this.encryptionKey, split[1]);
                    url = split[0] + "?" + encryptionParams;
                    request.newBuilder().url(url).build();
                }
            }
        } else {
            RequestBody body = request.body();
            String badyContent = getBadyContent(body);
            if (this.encryptionCallback != null && !TextUtils.isEmpty(badyContent)) {
                String encryptionBodyContent = this.encryptionCallback.encryption(this.encryptionKey, badyContent);
                if (!TextUtils.isEmpty(encryptionBodyContent)) {
                    RequestBody requestBody = RequestBody.create(body.contentType(), encryptionBodyContent);
                    request = request.newBuilder().method(method, requestBody).build();
                }
            }
        }

        return request;
    }

    private Request splicingParams(Request request) {
        HttpUrl httpUrl = request.url();
        String url = httpUrl.toString();
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(this.extraDynamicParam)) {
            if (!url.endsWith(AutoNetConstant.SLASH) && !this.extraDynamicParam.startsWith(AutoNetConstant.SLASH)) {
                this.extraDynamicParam += AutoNetConstant.SLASH;
            } else if (url.endsWith(AutoNetConstant.SLASH) && this.extraDynamicParam.startsWith(AutoNetConstant.SLASH)) {
                this.extraDynamicParam = this.extraDynamicParam.replace(AutoNetConstant.SLASH, "");
            }

            url += this.extraDynamicParam;
        }
        httpUrl = httpUrl.newBuilder(url).build();
        request = request.newBuilder().url(httpUrl).build();
        return request;
    }

    private Request splicingHeads(Request request) {
        if (heads == null) {
            return request;
        }
        Headers headers = request.headers();
        Headers.Builder builder = headers.newBuilder();
        Set<String> keys = heads.keySet();
        for (String key : keys) {
            builder.add(key, heads.get(key));
        }
        Headers newHeads = builder.build();
        return request.newBuilder().headers(newHeads).build();
    }

    private String getBadyContent(RequestBody body) {
        if (body == null) {
            return null;
        }
        String babyContent;
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            babyContent = buffer.readUtf8();
        } catch (Exception e) {
            return null;
        }
        return babyContent;
    }
}

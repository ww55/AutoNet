package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.Map;
import java.util.Random;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet.entity.TestARequest;
import cn.xiaoxige.autonet.entity.TestRequest;
import cn.xiaoxige.autonet.entity.TestResponseEntity;
import cn.xiaoxige.autonet.entity.ZipTestEntity;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.BiFunction;

public class MainActivity extends RxActivity {

    private TextView tvResult;
    private Button btnGet;
    private Button btnPost;
    private Button btnLocalNet;
    private Button btnNetLocal;
    private Button btnRequestInClass;
    private Button btnChainRequest;
    private Button btnSendFile;
    private Button btnRecvFile;
    private Button btnUpdateHeadToken;
    private Button btnRemoveHeadToken;
    private Button btnZip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerListener();
    }


    private void initView() {
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnRequestInClass = (Button) findViewById(R.id.btnRequestInClass);
        btnChainRequest = (Button) findViewById(R.id.btnChainRequest);
        btnSendFile = (Button) findViewById(R.id.btnSendFile);
        btnRecvFile = (Button) findViewById(R.id.btnRecvFile);
        btnUpdateHeadToken = (Button) findViewById(R.id.btnUpdateHeadToken);
        btnRemoveHeadToken = (Button) findViewById(R.id.btnRemoveHeadToken);
        btnLocalNet = (Button) findViewById(R.id.btnLocalNet);
        btnNetLocal = (Button) findViewById(R.id.btnNetLocal);
        btnZip = (Button) findViewById(R.id.btnZip);
    }

    private void registerListener() {

        // get请求
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 无参数请求
//                MainActivitydoGetAutoProxy.startNet(MainActivity.this);

                // 带有参数的请求
//                MainActivitydoGetAutoProxy.startNet(MainActivity.this, new TestRequest("ina_app", "other", "guidepage"));

                // 绑定生命周期的请求
                MainActivitydoGetAutoProxy.startNet(MainActivity.this, new TestRequest("ina_app", "other", "guidepage"), bindUntilEvent(ActivityEvent.DESTROY));


            }
        });

        // post请求
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 无参数请求
//                MainActivitydoPostAutoProxy.startNet(MainActivity.this);

                // 带有参数的请求
//                MainActivitydoPostAutoProxy.startNet(MainActivity.this, new TestRequest());

                // 绑定生命周期的请求
                MainActivitydoPostAutoProxy.startNet(MainActivity.this, new TestRequest(), bindUntilEvent(ActivityEvent.DESTROY));
            }
        });

        // 先本地后网络
        btnLocalNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivitydoLocalNetAutoProxy.startNet(MainActivity.this);
            }
        });

        // 先网络后本地
        btnNetLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivitydoNetLocalAutoProxy.startNet(MainActivity.this);
            }
        });

        // 直接在类上加注解请求
        btnRequestInClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestRequestInClass testRequestInClass = new TestRequestInClass(MainActivity.this);
                TestRequestInClassAutoProxy.startNet(testRequestInClass);
            }
        });

        // 接受文件
        btnRecvFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getExternalFilesDir(null).toString();
                MainActivityPullFileAutoProxy.pullFile(MainActivity.this, path, "pppig.apk");
            }
        });

        // 发送文件
        btnSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getExternalFilesDir(null).toString();
                MainActivityPushFileAutoProxy.pushFile(MainActivity.this, "upload", path + File.separator + "a.png");
            }
        });
        // 链式调用
        btnChainRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Map map = new ArrayMap();
//
//                map.put("shabi", "hahfdhahfdas");
//                map.put("aaaa", 123);
//                AutoNet.getInstance().createNet()
//                        .doPost()
//                        .setParams(map)
//                        .setReqType(AutoNetTypeAnontation.Type.JSON)
//                        .start(new AbsAutoNetCallback<Object, String>() {
//                            @Override
//                            public boolean handlerBefore(Object o, FlowableEmitter emitter) {
////                                Log.e("TAG", "o = " + o.toString());
////                                emitter.onNext("哈哈， 我拦击了， 我修改了返回结果");
//                                return false;
//                            }
//
//                            @Override
//                            public void onSuccess(String entity) {
//                                super.onSuccess(entity);
//                                tvResult.setText(entity);
//                            }
//                        });

                TestARequest entity = new TestARequest();
                Map o = new ArrayMap();
                o.put("a", 1);
                o.put("b", "fdsa");
                entity.setObject(o);
                AutoNet.getInstance().createNet()
                        .doGet()
                        .setRequestEntity(entity)
                        .setParam("m", "ina_app")
                        .setParam("c", "other")
                        .setParam("a", "guidepage")
                        .setSuffixUrl("/init.php")
                        .setDomainNameKey("jsonTestBaseUrl")
                        .start(new AbsAutoNetCallback<TestResponseEntity, TestResponseEntity.Data>() {
                            @Override
                            public boolean handlerBefore(TestResponseEntity o, FlowableEmitter emitter) {
                                Log.e("TAG", "" + o.toString());
                                emitter.onNext(o.getData());
                                return true;
                            }

                            @Override
                            public void onSuccess(TestResponseEntity.Data entity) {
                                super.onSuccess(entity);
                                Log.e("TAG", "" + entity.toString());
                                tvResult.setText(entity.toString());
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                super.onFailed(throwable);
                                Log.e("TAG", "" + throwable.getMessage());
                            }

                            @Override
                            public void onEmpty() {
                                super.onEmpty();
                            }
                        });


            }
        });

        btnZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 测试Json数据
                Flowable flowable1 = AutoNet.getInstance().createNet()
                        .doGet()
                        .setParam("m", "ina_app")
                        .setParam("c", "other")
                        .setParam("a", "guidepage")
                        .setSuffixUrl("/init.php")
                        .setDomainNameKey("jsonTestBaseUrl")
                        .setFlag("测试Json数据")
                        .setResponseClazz(TestResponseEntity.class)
                        .getFlowable();

                // 百度数据
                Flowable flowable2 = AutoNet.getInstance().createNet()
                        .doGet()
                        .setFlag("百度数据")
                        .getFlowable();

                tvResult.setText("正在请求");

                //noinspection unchecked
                Flowable.zip(flowable1, flowable2, new BiFunction<TestResponseEntity, String, ZipTestEntity>() {
                    @Override
                    public ZipTestEntity apply(TestResponseEntity o, String o2) throws Exception {
                        ZipTestEntity entity = new ZipTestEntity();
                        entity.setEntity(o);
                        entity.setBaiduWebMsg(o2);
                        return entity;
                    }
                }).subscribe(new FlowableSubscriber<ZipTestEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ZipTestEntity o) {
                        tvResult.setText(o.toString());
                    }

                    @Override
                    public void onError(Throwable t) {
                        tvResult.setText("请求失败： " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


            }
        });

        // 修改头信息token信息
        btnUpdateHeadToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = new Random().nextInt(100);
                AutoNet.getInstance().updateOrInsertHead("token", "" + i);
            }
        });

        // 删除头信息token信息
        btnRemoveHeadToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoNet.getInstance().removeHead("token");
            }
        });
    }

    //    @AutoNetResponseEntityClass(TestResponseEntity.class)
    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.GET)
    @AutoNetAnontation("/init.php")
    @AutoNetBaseUrlKeyAnontation("jsonTestBaseUrl")
    public class doGet implements IAutoNetDataBeforeCallBack<TestResponseEntity>, IAutoNetDataCallBack<TestResponseEntity.Data> {
        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("请求失败了：" + throwable.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("请求为空");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(TestResponseEntity.Data entity) {
            buffer.append("json数据请求成功\n" + entity.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public boolean handlerBefore(TestResponseEntity o, FlowableEmitter emitter) {
            Log.e("xiaoxige", o.toString());
            //1
//            emitter.onError(new EmptyError());
            //2
            TestResponseEntity testResponseEntity = (TestResponseEntity) o;
            TestResponseEntity.Data data = ((TestResponseEntity) o).getData();
            emitter.onNext(data);
            return true;
        }
    }

    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.POST)
    public class doPost implements IAutoNetDataCallBack {
        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("请求失败了：" + throwable.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("请求失败了");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            buffer.append("请求成功了\n" + entity.toString());
            tvResult.setText(buffer.toString());
        }
    }

    @AutoNetStrategyAnontation(AutoNetStrategyAnontation.NetStrategy.LOCAL_NET)
    public class doLocalNet implements IAutoNetDataCallBack, IAutoNetLocalOptCallBack {

        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("请求失败了：" + throwable.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("请求失败了");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            buffer.append("成功了\n" + entity.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public Object optLocalData(Map request) {
            // 本地数据交给用户处理
            return "\n这是本地数据,hahahahaha\n";
        }
    }

    @AutoNetStrategyAnontation(AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)
    public class doNetLocal implements IAutoNetDataCallBack, IAutoNetLocalOptCallBack {

        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("请求失败了：" + throwable.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("请求失败了");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            buffer.append("成功了\n" + entity.toString());
            tvResult.setText(buffer.toString());
        }

        @Override
        public Object optLocalData(Map request) {
            // 本地数据交给用户处理
            return "\n这是本地数据,hahahahaha\n";
        }
    }

    @AutoNetBaseUrlKeyAnontation("upFile")
    @AutoNetTypeAnontation(reqType = AutoNetTypeAnontation.Type.STREAM)
    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.POST)
    public class PushFile implements IAutoNetDataCallBack, IAutoNetFileCallBack {

        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("发送文件出错:\n" + throwable.toString() + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("发送文件出错");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            buffer.append("发送文件成功， 服务器并返回:\n" + entity.toString() + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onPregress(float progress) {
            buffer.append("发送文件进度：" + progress + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onComplete(File file) {
            buffer.append("文件发送成功， 文件：" + file.toString() + "\n");
            tvResult.setText(buffer.toString());
        }
    }

    @AutoNetBaseUrlKeyAnontation("pppig")
    @AutoNetTypeAnontation(resType = AutoNetTypeAnontation.Type.STREAM)
    @AutoNetAnontation("/apk/downLoad/android_4.2.4.apk")
    public class PullFile implements IAutoNetDataCallBack, IAutoNetFileCallBack {
        StringBuffer buffer = new StringBuffer();

        @Override
        public void onFailed(Throwable throwable) {
            buffer.append("接收文件出错:\n" + throwable.toString() + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onEmpty() {
            buffer.append("接收文件出错");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onSuccess(Object entity) {
            // 不会被执行
        }

        @Override
        public void onPregress(float progress) {
            buffer.append("接收进度：" + progress + "\n");
            tvResult.setText(buffer.toString());
        }

        @Override
        public void onComplete(File file) {
            buffer.append("文件接收成功， 文件：" + file.toString() + "\n");
            tvResult.setText(buffer.toString());
        }
    }

}

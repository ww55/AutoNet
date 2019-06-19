package cn.xiaoxige.autonet_api.distributor;

import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;

/**
 * @author xiaoxige
 * @date 2019/6/17 0017 17:44
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc:  Distribution and execution
 */
public interface IDistributorExecution<T> {

    /**
     * Automatic Detection of Request Mode and Initiation of Request
     *
     * @param callBack
     * @return
     * @throws Exception
     */
    T start(IAutoNetFileCallBack callBack) throws Exception;

    /**
     * Non-document requests
     *
     * @return
     * @throws Exception
     */
    T startNonFileRequest() throws Exception;

    /**
     * file request
     *
     * @param callBack
     * @return
     * @throws Exception
     */
    T startFileRequest(IAutoNetFileCallBack callBack) throws Exception;

}

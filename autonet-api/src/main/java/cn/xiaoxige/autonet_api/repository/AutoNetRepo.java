package cn.xiaoxige.autonet_api.repository;


import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 * AutoNet's request implementation interface
 */

public interface AutoNetRepo<T> {

    /**
     * get request
     *
     * @return
     */
    T doNetGet() throws Exception;

    /**
     * post request
     *
     * @return
     */
    T doNetPost() throws Exception;

    /**
     * put request
     *
     * @return
     */
    T doPut() throws Exception;

    /**
     * delete request
     *
     * @return
     */
    T doDelete() throws Exception;

    /**
     * upload file
     *
     * @param pushFileKey
     * @param filePath
     * @return
     */
    T pushFile(String pushFileKey, String filePath, IAutoNetFileCallBack callBack) throws Exception;

    /**
     * download file
     *
     * @param filePath
     * @param fileName
     * @return
     */
    T pullFile(String filePath, String fileName, IAutoNetFileCallBack callBack) throws Exception;

}

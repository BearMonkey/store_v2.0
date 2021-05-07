package com.itheima.store.utils;

import java.io.Serializable;

public class Result implements Serializable{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1655235290630900502L;
    // 返回码
    private String respCode;
    // 提示信息
    private String msg;

    private Object data;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result(String respCode, String msg, Object data) {
        this.respCode = respCode;
        this.msg = msg;
        this.data = data;
        System.out.println("respCode:" + respCode + ", msg: " + msg + ", data: " + data);
    }
    
    public Result() {
        this.respCode = "0";
        msg = "success";
        data = null;
        System.out.println("respCode:" + respCode + ", msg: " + msg + ", data: " + data);
    }

    @Override
    public String toString() {
        return "Result [respCode=" + respCode + ", msg=" + msg + ", data=" + data + "]";
    }
}

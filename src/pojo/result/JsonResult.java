package pojo.result;

import pojo.JsonResultType;

/**
 * Created by pz on 16/11/23.
 */
public class JsonResult {

    public int getCode() {
        return code;
    }

    public void setCode(JsonResultType code) {
        //枚举转换为int类型
        this.code = code.ordinal();
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

    private int code;
    private String msg;
    private Object data;
}

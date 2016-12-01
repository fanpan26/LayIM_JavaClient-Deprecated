package pojo.result;

import pojo.JsonResultType;

/**
 * Created by pz on 16/11/27.
 */
public class JsonResultHelper {
    public static JsonResult createResult(JsonResultType type,String msg,Object data){
        JsonResult result = new JsonResult();

        result.setCode(type);
        result.setMsg(msg);
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static JsonResult createSuccessResult(String msg,Object data){
        return createResult(JsonResultType.typeSuccess,msg,data);
    }

    public static  JsonResult createSuccessResult(Object data){
        return createSuccessResult("",data);
    }

    public static JsonResult createFailedResult(String msg){
        return createResult(JsonResultType.typeFailed,msg,null);
    }
}

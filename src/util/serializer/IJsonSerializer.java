package util.serializer;

import java.util.List;

/**
 * Created by pz on 16/11/23.
 */
public interface IJsonSerializer {
    /*
    * 序列化某个对象
    * */
    <T> String toJSON(T t);

    /*
    * 反序列化
    * */
    <T> T toObject(String json,Class<T> clazz);

    /*
    * 序列化成数组
    * */
    <T> List<T> toArray(String json, Class<T> clazz);

}

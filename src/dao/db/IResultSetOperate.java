package dao.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by pz on 16/11/25.
 */
public interface IResultSetOperate {

    //操作resultSet返回相应的数据
    Object getObject(ResultSet resultSet);

    Object getObject(Statement statement);
}

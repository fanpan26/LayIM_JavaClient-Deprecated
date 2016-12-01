package dao.db;

import com.sun.org.apache.bcel.internal.generic.IREM;

import java.sql.*;
import java.util.Map;

/**
 * Created by pz on 16/11/22.
 */

public class SQLHelper {

    private String _connectionString;
    private String _dbUserName;
    private String _dbUserPwd;
    private String DRIVER_NAME="net.sourceforge.jtds.jdbc.Driver";//

    //无参构造函数
    public SQLHelper(){
        //ip:port/dbname
        _connectionString = "jdbc:jtds:sqlserver://*.*.*.*:1433/layim";
        _dbUserName = "sa";
        _dbUserPwd = "*********";

    }

    /*
     *获取连接
     * */
    private Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(_connectionString,_dbUserName,_dbUserPwd);
            System.out.println("connection succeed");
            return connection;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 执行sql语句 UPDATE  INSERT DELETE
     * */
    public boolean ExecuteNonquery(String sql,Map<Integer,Object> params){
        //获取连接
        Connection connection = getConnection();
        if(connection == null) return false;
        PreparedStatement statement = Prepare(connection,sql,params);
        int result = 0;
        try {
            //
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            System.out.println("关闭连接");
            closeAll(connection,statement);
        }
        return result > 0;
    }
    /*
     * ִ
     * */
    public boolean ExecuteNonquery(String sql){
        return ExecuteNonquery(sql,null);
    }

    /*
    * 查询ResultSet  结果集
    * */
    public Object QueryResult(String sql, Map<Integer,Object> params,IResultSetOperate operate){
        Connection connection = getConnection();
        PreparedStatement statement = Prepare(connection,sql,params);
        try {
            ResultSet resultSet = statement.executeQuery();
            //想要获取相应的结果，需要实现IResultOperate接口
            Object object =  operate.getObject(resultSet);
            closeAll(connection,statement);
            return object;

        }catch (SQLException ex) {
            ex.printStackTrace();
            closeAll(connection, statement);
            return null;
        }
    }

    public Object QueryManyResultWithProcedure(String sql, Map<Integer,Object> params, IResultSetOperate operate){
        return QueryManyResult("{call "+sql+"}",params,operate);
    }

    public Object QueryManyResult(String sql, Map<Integer,Object> params, IResultSetOperate operate){
        Connection connection = getConnection();
        PreparedStatement statement = Prepare(connection,sql,params);
        try {
            //想要获取相应的结果，需要实现IResultOperate接口
            Object object =  operate.getObject(statement);
            closeAll(connection,statement);
            return object;

        }catch (Exception ex){
            ex.printStackTrace();
            closeAll(connection,statement);
            return null;
        }
    }


    private CallableStatement PrepareByStoreProcedure(Connection connection,String procedureSql,Map<Integer,Object> params){

        try {
            CallableStatement statement =  connection.prepareCall(procedureSql);
            if(params != null){
                for(int i = 0;i<params.size();i++){
                    statement.setObject(i+1, params.get(i+1));
                }
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PreparedStatement Prepare(Connection connection,String sql,Map<Integer,Object> params){
        try {
            //
            PreparedStatement statement = connection.prepareStatement(sql);

            if(params != null){
                for(int i = 0;i<params.size();i++){
                    //get(key)
                    statement.setObject(i+1, params.get(i+1));
                }
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     关闭
     * */
    private void closeAll(Connection connection,PreparedStatement statement){
        try {
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


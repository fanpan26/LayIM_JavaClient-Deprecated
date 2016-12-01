package dao.operate;

import dao.db.IResultSetOperate;
import net.sourceforge.jtds.jdbc.JtdsPreparedStatement;
import pojo.*;
import pojo.result.BaseDataResult;
import pojo.result.GroupMemberResult;
import pojo.result.JsonResult;
import pojo.result.JsonResultHelper;
import util.log.LayIMLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pz on 16/11/25.
 */
public class LayIMResultSetOperate implements IResultSetOperate {

    //返回MemberList
    public Object getObject(ResultSet resultSet) {
        GroupMemberResult groupMemberResult = new GroupMemberResult();
        List<User> users = new ArrayList<>();
        User owner = new User();


        try {
            int ownerid = 0;
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt(2));
                user.setUsername(resultSet.getString(3));
                user.setSign(resultSet.getString(4));
                user.setAvatar(resultSet.getString(5));
                ownerid = resultSet.getInt(6);
                //群主
                if(ownerid > 0){
                    owner = user;
                }
                users.add(user);
            }

            groupMemberResult.setList(users);
            groupMemberResult.setOwner(owner);

            return JsonResultHelper.createSuccessResult("",groupMemberResult);
        } catch (SQLException e) {
            e.printStackTrace();
            return JsonResultHelper.createFailedResult("程序异常");
        }

    }

    //获取基本信息列表的方法，比较繁琐，应该有更好的业务处理方式
    public Object getObject(Statement statement) {
        JtdsPreparedStatement jtdsPreparedStatement = (JtdsPreparedStatement) statement;
        JsonResult jsonResult = new JsonResult();
        BaseDataResult result = new BaseDataResult();
        try {
            //friend
            List<FriendGroup> friendGroups = null;
            List<User> users = null;
            //group
            List<BigGroup> groups = null;
            //查询多结果集
            if (jtdsPreparedStatement.execute()) {
                //第一个resultSet为mine的数据
                ResultSet mineRs = jtdsPreparedStatement.getResultSet();
                StatusUser mine = getStatusUser(mineRs);
                if (mine.getId() == 0) {
                    return JsonResultHelper.createFailedResult("用户不存在");
                }
                result.setMine(mine);
                //2 3 4 分别为 用户分组， 用户分组详细，群组信息
                int i = 0;

                while (jtdsPreparedStatement.getMoreResults()) {
                    ResultSet resultSet = jtdsPreparedStatement.getResultSet();
                    //好友分组
                    if (i == 0) {
                        friendGroups = getFriendGroupList(resultSet);
                    }
                    //好友列表
                    if (i == 1) {
                        users = getFriendGroupDetailUser(resultSet);
                    }
                    //群组分组
                    if (i == 2) {
                        //群组信息
                        groups = getGroups(resultSet);
                    }
                    i++;
                }

                friendGroups = getFriendGroupList(friendGroups, users);
                result.setFriend(friendGroups);
                result.setGroup(groups);

                jsonResult.setCode(JsonResultType.typeSuccess);
                jsonResult.setData(result);
                return jsonResult;
            }
            jsonResult.setCode(JsonResultType.typeSuccess);
            return jsonResult;

        } catch (SQLException e) {

            e.printStackTrace();
            LayIMLog.error(e);

           return JsonResultHelper.createFailedResult("程序异常");
        }
    }

    //转换成mine
    private StatusUser getStatusUser(ResultSet rs) throws SQLException {
        StatusUser user = new StatusUser();
        while (rs.next()) {
            user.setId(rs.getInt(1));
            user.setUsername(rs.getString(2));
            user.setSign(rs.getString(3));
            user.setAvatar(rs.getString(4));
        }
        return user;
    }

    //转换成组
    private List<FriendGroup> getFriendGroupList(ResultSet rsGroup) throws SQLException {

        List<FriendGroup> friendGroups = new ArrayList<>();
        while (rsGroup.next()) {
            FriendGroup friendGroup = new FriendGroup();

            int gid = rsGroup.getInt(1);
            friendGroup.setId(gid);
            friendGroup.setGroupname(rsGroup.getString(2));

            friendGroups.add(friendGroup);
        }
        return friendGroups;
    }

    private List<User> getFriendGroupDetailUser(ResultSet rsDetail) throws SQLException {
        List<User> friends = new ArrayList<>();

        while (rsDetail.next()) {
            User u = new User();
            u.setFgid(rsDetail.getInt(1));
            u.setId(rsDetail.getInt(2));
            u.setUsername(rsDetail.getString(4));
            u.setSign(rsDetail.getString(5));
            u.setAvatar(rsDetail.getString(6));
            friends.add(u);
        }
        return friends;
    }
    //处理分组和好友间的关系
    private List<FriendGroup> getFriendGroupList(List<FriendGroup> friendGroup, List<User> friends) {
        List<FriendGroup> list = new ArrayList<>();

        for (FriendGroup fg : friendGroup){
            List<User> users = new ArrayList<>();
            for (User u : friends){
                if (fg.getId() == u.getFgid()) {
                    users.add(u);
                }
            }

            fg.setList(users);

            list.add(fg);
        }
        return list;
    }

    private List<BigGroup> getGroups(ResultSet rsGroup) throws SQLException {
        List<BigGroup> bigGroups = new ArrayList<>();
        while (rsGroup.next()) {
            BigGroup group = new BigGroup();
            group.setId(rsGroup.getInt(1));
            group.setGroupname(rsGroup.getString(2));
            group.setAvatar(rsGroup.getString(3));
            bigGroups.add(group);
        }
        return bigGroups;
    }
}

package socket.sender;

import dao.LayIMDao;
import pojo.SocketUser;
import pojo.message.ToClientTextMessage;
import pojo.message.ToDBMessage;
import pojo.message.ToServerMessageMine;
import pojo.message.ToServerTextMessage;
import pojo.result.ToClientMessageResult;
import pojo.result.ToClientMessageType;
import socket.LayIMChatType;
import socket.manager.GroupUserManager;
import util.LayIMFactory;
import util.log.LayIMLog;

import javax.websocket.Session;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 发送信息类
 * 所有从客户端到服务端的消息转发到此类进行消息处理
 * ToServerTextMessage转换为ToClientTextMessage
 * 如果是单聊，直接从缓存取出对象的session进行消息发送，群聊则需要从缓存中取出该群里所有人的id进行遍历发送消息，
 * 遍历过后需要优化在线与否，假如100人中只有一个人在线，则会浪费99次（未做优化）
 */
public class MessageSender {
    //发送信息业务逻辑
    public void sendMessage(ToServerTextMessage message){

        int toUserId = message.getTo().getId();
        //获取发送人
        String sendUserId = Integer.toString(message.getMine().getId());
        String type =  message.getTo().getType();
        //消息提前生成，否则进行循环内生成会浪费资源
        String toClientMessage = getToClientMessage(message);

        System.out.println("当前消息类型是"+type);
        //不能用==做比较，因为一个是static final 值，另外一个是在对象中 == 为false
        if(type.equals(LayIMChatType.GROUP)){
            //群聊，需要遍历该群组里的所有人
            //第一次从缓存中取userId，否则，从数据库中取在存到缓存中
            List<String> users =  new GroupUserManager().getGroupMembers(message.getTo().getId());
            for (String userid : users) {
                //遍历发送消息 自己过滤，不给自己发送(发送人id和群成员内的某个id相同)
                if (!sendUserId.equals(userid)) {
                    sendMessage(Integer.parseInt(userid), toClientMessage);
                }
            }
        }else {
            sendMessage(toUserId, toClientMessage);
        }

        //最后保存到数据库
        saveMessage(message);

    }

    //给单个用户发
    private  void sendMessage(Integer userId,String message){
        SocketUser user = LayIMFactory.createManager().getUser(userId);
        if (user.isExist()) {
            if (user.getSession() == null) {
                LayIMLog.info("该用户不在线 ");
            } else {
                Session session = user.getSession();
                if (session.isOpen()) {
                    //构造用户需要接收到的消息
                    session.getAsyncRemote().sendText(message);
                }
            }
        }else{
            LayIMLog.info("该用户不在线 ");
        }
    }

    //保存到数据库
    //需要加入到队列
    private void saveMessage(ToServerTextMessage message){
        ToDBMessage dbMessage = new ToDBMessage();

        dbMessage.setSendUserId(message.getMine().getId());
        dbMessage.setAddtime(new Date().getTime());
        dbMessage.setChatType( message.getTo().getType().equals(LayIMChatType.FRIEND) ? LayIMChatType.CHATFRIEND:LayIMChatType.CHATGROUP);
        dbMessage.setMsgType(1);//这个参数先不管就是普通聊天记录
        long groupId = getGroupId(message.getMine().getId(),message.getTo().getId(),message.getTo().getType());
        dbMessage.setGroupId(groupId);
        dbMessage.setMsg(message.getMine().getContent());

        LayIMDao dao = new LayIMDao();

        dao.addMsgRecord(dbMessage);
    }

    //根据客户端发送来的消息，构造发送出去的消息
     /*
        *  username: data.mine.username
            , avatar: data.mine.avatar
            , id: data.mine.id
            , type: data.to.type
            , content:data.mine.content
            , timestamp: new Date().getTime()
        * */
    private String getToClientMessage(ToServerTextMessage message) {

        ToClientTextMessage toClientTextMessage = new ToClientTextMessage();

        ToServerMessageMine mine = message.getMine();

        toClientTextMessage.setUsername(mine.getUsername());
        toClientTextMessage.setAvatar(mine.getAvatar());
        toClientTextMessage.setContent(mine.getContent());
        toClientTextMessage.setType(message.getTo().getType());

        //群组和好友直接聊天不同，群组的id 是 组id，否则是发送人的id
        if (toClientTextMessage.getType().equals(LayIMChatType.GROUP)) {
            toClientTextMessage.setId(message.getTo().getId());
        } else {
            toClientTextMessage.setId(mine.getId());
        }
        toClientTextMessage.setTimestamp(new Date().getTime());

        //返回统一消息接口
        ToClientMessageResult result = new ToClientMessageResult();
        result.setMsg(toClientTextMessage);
        result.setType(ToClientMessageType.TYPE_TEXT_MESSAGE);

        return LayIMFactory.createSerializer().toJSON(result);
    }

    //生成对应的groupId
    private long getGroupId(int sendUserId,int toUserId,String type){

        //如果是组内聊天，直接返回组id，否则返回 两个id的组合
        if (type.equals(LayIMChatType.GROUP)){
            return toUserId;
        }


        String sendUserIdStr = Integer.toString(sendUserId);
        String toUserIdStr = Integer.toString(toUserId);

        String groupIdStr = "";
        //按照固定次序生成相应的聊天组
        if (sendUserId > toUserId){
            groupIdStr = sendUserIdStr + toUserIdStr;
        }else{
            groupIdStr = toUserIdStr + sendUserIdStr;
        }

        long groupId = Long.parseLong(groupIdStr);
        return groupId;
    }
}

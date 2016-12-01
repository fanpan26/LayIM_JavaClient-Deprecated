package socket.manager;

import pojo.SocketUser;
import pojo.User;
import util.log.LayIMLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pz on 16/11/23.
 */
public class UserManager implements IUserManager {


    private static Map<String,SocketUser> socketUserMap;


    private OnLineUserManager onLineUserManager;

    private UserManager(){
        socketUserMap = new ConcurrentHashMap<>();
        onLineUserManager = new OnLineUserManager();
    }

    private static UserManager manager = new UserManager();

    public static IUserManager getInstance(){
        return manager;
    }


    public boolean addUser(SocketUser user) {

        String sessionUserId = Integer.toString(user.getUserId());
        removeUser(sessionUserId);
        socketUserMap.put(sessionUserId, user);
        //加入在线列表缓存
        onLineUserManager.addUser(sessionUserId);
        return true;
    }


    public boolean removeUser(SocketUser user) {
        String sessionUserId =  Integer.toString(user.getUserId());
        onLineUserManager.removeUser(sessionUserId);
        return removeUser(sessionUserId);
    }


    public int getOnlineCount() {
        return socketUserMap.size();
    }

    public SocketUser getUser(int userId){
        String key = Integer.toString(userId);
        if(socketUserMap.containsKey(key)){
            return socketUserMap.get(key);
        }
        return new SocketUser();
    }

    private boolean removeUser(String sessionUserId) {
        socketUserMap.remove(sessionUserId);
        return true;
    }


}

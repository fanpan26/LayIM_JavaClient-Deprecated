package pojo;

import java.util.List;

/**
 * Created by pz on 16/11/23.
 */
public class FriendGroup extends Group {
    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    private int online;
    private List<User> list;
}

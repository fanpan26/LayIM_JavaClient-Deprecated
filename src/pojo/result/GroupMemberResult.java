package pojo.result;

import pojo.User;

import java.util.List;

/**
 * Created by pz on 16/11/28.
 */
public class GroupMemberResult {
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    private User owner;
    private List<User> list;
}

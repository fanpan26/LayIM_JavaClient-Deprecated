package pojo.message;

/**
 * Created by pz on 16/11/23.
 */
public class ToServerTextMessage {
    public ToServerMessageMine getMine() {
        return mine;
    }

    public void setMine(ToServerMessageMine mine) {
        this.mine = mine;
    }

    public ToServerMessageTo getTo() {
        return to;
    }

    public void setTo(ToServerMessageTo to) {
        this.to = to;
    }

    private ToServerMessageMine mine;
    private ToServerMessageTo to;
}

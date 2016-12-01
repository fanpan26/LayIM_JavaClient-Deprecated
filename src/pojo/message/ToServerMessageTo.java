package pojo.message;

/**
 * Created by pz on 16/11/23.
 */
public class ToServerMessageTo {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    private int id;
    private String avatar;
    private String sign;
    private String type;
    private String name;
}

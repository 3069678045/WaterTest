package top.lhx.myapp.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * DaoUSer class
 *
 * @author auser
 * @date 11/2/2019
 */
@Entity
public class DaoUser {
    private String message;

    @Generated(hash = 1132327668)
    public DaoUser(String message) {
        this.message = message;
    }

    @Generated(hash = 1108071899)
    public DaoUser() {
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

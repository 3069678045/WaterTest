package com.heyi.testing.greendao;

import org.greenrobot.greendao.annotation.*;

/**
 * UserDao class
 *
 * @author auser
 * @date 11/2/2019
 */
@Entity
public class UserDao {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String message;
    @NotNull
    @Unique
    private long timestamp;

    @Generated(hash = 648017427)
    public UserDao(Long id, @NotNull String message, long timestamp) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Generated(hash = 917059161)
    public UserDao() {
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

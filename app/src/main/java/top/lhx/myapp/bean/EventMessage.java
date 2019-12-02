package top.lhx.myapp.bean;

public class EventMessage {

    public int msgCode;

    public EventMessage(int msgCode) {
        this.msgCode = msgCode;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }
}
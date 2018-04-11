package kkk8888.smsparsing;

/**
 * Created by home on 2017-10-01.
 */

public class LittleMSG {

    String who, msg, now, did;

    public LittleMSG(String who, String msg, String now, String did) {
        this.who = who;
        this.msg = msg;
        this.now = now;
        this.did = did;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }
}

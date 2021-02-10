package com.example.paytm.inpg.entities;

public class ResponseBody {

    private String msg, status;

    public ResponseBody(String msg, String status) {
        this.msg = msg;
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

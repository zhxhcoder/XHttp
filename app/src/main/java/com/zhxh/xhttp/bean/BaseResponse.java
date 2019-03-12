package com.zhxh.xhttp.bean;

public class BaseResponse {

    private int error_code;
    private String reason;


    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                '}';
    }
}

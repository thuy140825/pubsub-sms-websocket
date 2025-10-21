package com.example.myservice.sms.twilio;

import lombok.Getter;

@Getter
public class TwilioAccountConfig {
    private String name; // Tên định danh cho cấu hình (ví dụ: "marketing", "transactional")
    private String accountSid;
    private String authToken;
    private String phoneNumber; // Số điện thoại Twilio cho tài khoản này

    public TwilioAccountConfig(String name, String accountSid, String authToken, String phoneNumber) {
        this.name = name;
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.phoneNumber = phoneNumber;
    }

    // Có thể thêm setters nếu cần, nhưng thường thì cấu hình là immutable
    @Override
    public String toString() {
        return "TwilioAccountConfig{" +
                "name='" + name + '\'' +
                ", accountSid='" + accountSid + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
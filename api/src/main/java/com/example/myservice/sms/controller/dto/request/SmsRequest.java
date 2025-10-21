package com.example.myservice.sms.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    private String configName;
    private String toPhoneNumber;
    private String messageBody;
}
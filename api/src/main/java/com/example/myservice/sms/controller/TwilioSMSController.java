package com.example.myservice.sms.controller;

import com.example.myservice.sms.controller.dto.request.SmsRequest;
import com.example.myservice.sms.twilio.SmsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sms")
@AllArgsConstructor
public class TwilioSMSController {
    private final SmsService smsService;

    @PostMapping("/send")
    public String sendSms(@RequestBody SmsRequest request) {
        return smsService.sendSms(request.getConfigName(),
                request.getToPhoneNumber(),
                request.getMessageBody());
    }

    @PostMapping("/send-specific")
    public String sendSmsWithSpecificClient(@RequestBody SmsRequest request) {
        return smsService.sendSmsWithSpecificClient(request.getConfigName(),
                request.getToPhoneNumber(),
                request.getMessageBody());
    }

}

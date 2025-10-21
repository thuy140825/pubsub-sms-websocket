package com.example.myservice.sms.twilio;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final TwilioConfigManager configManager;

    public SmsService(TwilioConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * Gửi tin nhắn SMS sử dụng cấu hình Twilio cụ thể.
     * @param configName Tên của cấu hình tài khoản Twilio (ví dụ: "marketing", "transactional")
     * @param toPhoneNumber Số điện thoại người nhận (định dạng E.164, ví dụ: +84912345678)
     * @param messageBody Nội dung tin nhắn
     * @return SID của tin nhắn đã gửi, hoặc null nếu gửi thất bại
     */
    public String sendSms(String configName, String toPhoneNumber, String messageBody) {
        TwilioAccountConfig config = configManager.getConfig(configName);

        if (config == null) {
            System.err.println("Lỗi: Không tìm thấy cấu hình Twilio với tên: " + configName);
            return null;
        }

        // Khởi tạo Twilio Client cho cấu hình cụ thể này
        // Lưu ý: Twilio.init() là một phương thức static, nó sẽ thay đổi cấu hình toàn cục.
        // Đối với trường hợp này, nơi bạn muốn gửi tin nhắn từ các tài khoản khác nhau trong cùng một ứng dụng,
        // việc khởi tạo lại Twilio.init() trước mỗi lần gửi là cần thiết.
        // Tuy nhiên, cách tốt hơn (nếu có thể) là tạo TwilioRestClient riêng cho mỗi cấu hình
        // thay vì sử dụng static Twilio.init().
        // Với thư viện Twilio Java hiện tại, cách thông thường là sử dụng Twilio.init()
        // nếu bạn chỉ gửi từ một tài khoản tại một thời điểm.
        // Nếu bạn cần gửi song song từ nhiều tài khoản, bạn sẽ cần tạo một instance Client riêng.

        // Cách 1: Sử dụng Twilio.init() (đơn giản nhưng có thể không tối ưu cho gửi song song)
        Twilio.init(config.getAccountSid(), config.getAuthToken());

        try {
            Message message = Message.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(config.getPhoneNumber()),
                            messageBody)
                    .create();
            System.out.println("Tin nhắn đã được gửi qua tài khoản '" + configName + "'. SID: " + message.getSid());
            return message.getSid();
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi tin nhắn qua tài khoản '" + configName + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Cách 2: Tạo một Twilio Client riêng cho mỗi cấu hình (tốt hơn cho gửi song song hoặc tránh Twilio.init() static)
    // Để làm được điều này, bạn cần sử dụng TwilioRestClient (hoặc Client) constructor trực tiếp
    // Thay vì dựa vào Twilio.init().
    // Lưu ý: Phiên bản thư viện có thể ảnh hưởng đến cách khởi tạo Client trực tiếp.
    // Dưới đây là cách sử dụng Twilio Client trong các phiên bản mới hơn:
    public String sendSmsWithSpecificClient(String configName, String toPhoneNumber, String messageBody) {
        TwilioAccountConfig config = configManager.getConfig(configName);

        if (config == null) {
            System.err.println("Lỗi: Không tìm thấy cấu hình Twilio với tên: " + configName);
            return null;
        }

        // Tạo một instance Client riêng cho mỗi lần gửi (hoặc lưu trữ trong Map nếu bạn gửi nhiều lần)
        TwilioRestClient specificClient = new TwilioRestClient.Builder(config.getAccountSid(), config.getAuthToken()).build();

        try {
            Message message = Message.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(config.getPhoneNumber()),
                            messageBody)
                    .create(specificClient); // Truyền client cụ thể vào phương thức create()
            System.out.println("[Specific Client] Tin nhắn đã được gửi qua tài khoản '" + configName + "'. SID: " + message.getSid());
            return message.getSid();
        } catch (Exception e) {
            System.err.println("[Specific Client] Lỗi khi gửi tin nhắn qua tài khoản '" + configName + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
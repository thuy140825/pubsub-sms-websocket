package com.example.myservice.sms.twilio;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class TwilioConfigManager {
    private static final String CONFIG_FILE = "application.yml"; // Tên file cấu hình
    private final Map<String, TwilioAccountConfig> configs = new HashMap<>();

    public TwilioConfigManager() {
        loadConfigs();
    }

    private void loadConfigs() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Không tìm thấy file cấu hình: " + CONFIG_FILE);
                return;
            }
            props.load(input);

            // Lặp qua các property để tìm các cấu hình Twilio
            props.stringPropertyNames().forEach(key -> {
                if (key.startsWith("twilio.accounts.") && key.endsWith(".sid")) {
                    String accountName = key.substring("twilio.accounts.".length(), key.lastIndexOf(".sid"));
                    String sid = props.getProperty(key);
                    String token = props.getProperty("twilio.accounts." + accountName + ".token");
                    String phone = props.getProperty("twilio.accounts." + accountName + ".phone");

                    if (sid != null && token != null && phone != null) {
                        configs.put(accountName, new TwilioAccountConfig(accountName, sid, token, phone));
                        System.out.println("Đã tải cấu hình Twilio: " + accountName);
                    } else {
                        System.err.println("Cảnh báo: Cấu hình Twilio không đầy đủ cho tài khoản: " + accountName);
                    }
                }
            });

        } catch (IOException ex) {
            System.err.println("Lỗi khi đọc file cấu hình: " + ex.getMessage());
        }
    }

    public TwilioAccountConfig getConfig(String name) {
        return configs.get(name);
    }

    public Map<String, TwilioAccountConfig> getAllConfigs() {
        return new HashMap<>(configs); // Trả về bản sao để tránh thay đổi bên ngoài
    }
}

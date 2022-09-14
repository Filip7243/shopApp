package com.shopapp.shopApp.email;

public interface EmailSender {
    void sendEmail(String receiver, String emailBody);
}

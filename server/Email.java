package server;

import java.util.Properties;

public class Email {
    public Email() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "sandbox.smtp.mailtrap.io");
    }

    public void sendSingleEmail(String message, String email) {

    }

    public void SendMultiple(String message, String[] addresses) {

    }

//    public static void main(String[] args) {
//        Email email = new Email();
//    }
}
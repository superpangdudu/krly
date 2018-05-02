package cn.krly.platform.api.notification;


public interface IMailService {
    void sendMail(String senderAccount, String senderPassword,
                  String senderAddress,
                  String recipientAddress,
                  String smtpServer,
                  String subject, String content);
}

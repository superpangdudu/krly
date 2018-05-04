package cn.krly.service.notification.provider;

import cn.krly.platform.api.notification.IMailService;
import com.alibaba.dubbo.config.annotation.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service(provider = "MailServiceProviderConfig")
public class MailService implements IMailService {
    @Override
    public void sendMail(String senderAccount, String senderPassword,
                         String senderAddress,
                         String recipientAddress,
                         String smtpServer,
                         String subject, String content) {
        try {
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.host", smtpServer);

            Session session = Session.getInstance(properties);
            session.setDebug(true);

            Transport transport = session.getTransport();
            transport.connect(senderAccount, senderPassword);

            Message message = newMessage(session,
                    senderAddress, recipientAddress,
                    subject, content);

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message newMessage(Session session,
                               String senderAddress, String recipientAddress,
                               String subject, String content) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject, "UTF-8");
        message.setContent(content, "text/html;charset=UTF-8");
        message.setSentDate(new Date());

        message.setSender(new InternetAddress(senderAddress));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientAddress));

        return message;
    }

    public static void main(String[] args) {
        MailService mailService = new MailService();
        mailService.sendMail("superlee19", "811014S041777",
                "superlee19@163.com",
                "1659929144@qq.com",
                "smtp.163.com",
                "test", "test content<a href=http://home.baidu.com>关于百度</a>");
    }
}

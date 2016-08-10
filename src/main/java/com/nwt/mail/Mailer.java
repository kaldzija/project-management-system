package com.nwt.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class Mailer
{
    @Autowired
    private Environment environment;

    public boolean sendActivationMail(String email, String token)
    {

        String APP_URL = environment.getRequiredProperty("application.url");
        MessageBodyStyled messageBody = new MessageBodyStyled("Confirmation mail", "Confirmation mail");
        StringBuilder builder = new StringBuilder();
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(getClass().getClassLoader().getResource("activation-mail-template.html").getFile());
            int ch;
            while ((ch = fis.read()) != -1)
            {
                builder.append((char) ch);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        String fileBody = builder.toString();
        fileBody = fileBody.replace("{:link}", APP_URL + "/auth/activate/" + token);
        messageBody.setFileBody(fileBody);
        return sendEmail(email, messageBody);
    }

    public boolean sendRecoveryMail(String email, String token)
    {
        String APP_URL = environment.getRequiredProperty("application.url");
        MessageBodyStyled messageBody = new MessageBodyStyled("Recovery mail", "Recovery mail");
        StringBuilder builder = new StringBuilder();
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(getClass().getClassLoader().getResource("reset-password-mail-template.html").getFile());
            int ch;
            while ((ch = fis.read()) != -1)
            {
                builder.append((char) ch);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        String fileBody = builder.toString();
        fileBody = fileBody.replace("{:link}", APP_URL + "/#/home?passwordRecoveryTokenId=" + token);
        messageBody.setFileBody(fileBody);
        return sendEmail(email, messageBody);
    }

    public boolean sendEmail(String mailTo, MessageBody messageBody)
    {
        List<String> mails = new ArrayList<String>();
        mails.add(mailTo);
        return sendEmail(mails, messageBody);
    }

    public boolean sendEmail(List<String> mailTo, MessageBody messageBody)
    {
        // Sender's email ID needs to be mentioned
        try
        {
            final String username = environment.getRequiredProperty("email");
            final String password = environment.getRequiredProperty("email.password");

            Boolean auth = true;
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            //props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", "587");

            Session session;
            if (auth)
            {
                session = Session.getInstance(props, new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });
            } else
            {
                session = Session.getInstance(props);
            }

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(username));
            // Set To: header field of the header.
            InternetAddress[] addressTo = new InternetAddress[mailTo.size()];
            for (int i = 0; i < mailTo.size(); i++)
            {
                addressTo[i] = new InternetAddress(mailTo.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, addressTo);
            // Set Subject: header field
            message.setSubject(messageBody.getSubject());
            message.setContent(messageBody.getContent(), "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

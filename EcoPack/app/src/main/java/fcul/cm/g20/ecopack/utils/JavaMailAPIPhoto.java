package fcul.cm.g20.ecopack.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class JavaMailAPIPhoto extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Session session;
    private String email, subject, message;
    private Bitmap qr_code;

    public JavaMailAPIPhoto(Context context, String email, String subject, String message, Bitmap qr_code) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.qr_code = qr_code;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("fculcmg20@gmail.com", "fcul2021g20");
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress("fculcmg20@gmail.com"));
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Multipart multipart = new MimeMultipart("related");

            //TEXT
            MimeBodyPart txtPart = new MimeBodyPart();
            txtPart.setContent(message, "text/plain");
            multipart.addBodyPart(txtPart);

            // IMAGE

            String filePath = context.getCacheDir().toString();
            File file = new File(filePath + "QR_CODE.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            qr_code.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

            MimeBodyPart imgPart = new MimeBodyPart();
            //DataSource fds = new FileDataSource(filePath);
            //imgPart.setDataHandler(new DataHandler(fds));
            imgPart.attachFile(filePath + "QR_CODE.jpg");
            imgPart.setFileName("QR_CODE.jpg");
            multipart.addBodyPart(imgPart);

            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
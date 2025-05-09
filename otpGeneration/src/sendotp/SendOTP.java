package sendotp;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.otpGeneration.Client.HibernateUtil;
import com.otpGeneration.Entity.OTP;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;

public class SendOTP {

    // Hardcoded email credentials (consider using environment variables)
    private static final String SENDER_EMAIL = "opt.generatetoemail@gmail.com"; // Fixed email format
    private static final String SENDER_PASSWORD = "rwkwgesjceqeicij"; // Avoid spaces in app passwords
    private static final String RECIPIENT_EMAIL = "anjalitak1504@gmail.com";

    // Generate a 6-digit OTP
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Send OTP via Email
    public static void sendEmail(String otp) {
        // SMTP properties for Gmail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Configure mail session
        jakarta.mail.Session mailSession = jakarta.mail.Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT_EMAIL));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp);

            Transport.send(message);
            System.out.println("OTP sent successfully to " + RECIPIENT_EMAIL);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send OTP.");
        }
    }

    // Store OTP in the database
    public static void storeOTP(String otpCode) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            OTP otp = new OTP(RECIPIENT_EMAIL, otpCode);
            session.save(otp);
            transaction.commit();
            System.out.println("OTP stored successfully in the database!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Ensure rollback on failure
            e.printStackTrace();
            System.out.println("Error storing OTP in the database.");
        }
    }

    // Main method to send and store OTP
    public static void main(String[] args) {
        String otpCode = generateOTP();
        sendEmail(otpCode);
        storeOTP(otpCode);
    }
}

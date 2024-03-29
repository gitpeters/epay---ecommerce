package com.peters.notificationservice.service;

import com.peters.notificationservice.dto.CartResponse;
import com.peters.notificationservice.dto.EmailNotificationDto;
import com.peters.notificationservice.dto.OrderMessageNotification;
import com.peters.notificationservice.dto.PasswordResetDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    LocalDateTime currentDateTime = LocalDateTime.now();

    //Define the desired date and time format
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d 'of' MMMM, yyyy", Locale.ENGLISH);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);

    //Format the current date and time
    String date = currentDateTime.format(dateFormatter);
    String time = currentDateTime.format(timeFormatter);

    public void sendVerificationEmail(EmailNotificationDto messageDto) throws MessagingException, UnsupportedEncodingException {
        String imageLink = "https://i.pinimg.com/originals/b9/e7/e8/b9e7e8e510755020185a1a7ca1ff271b.png";
        String subject = "Email Verification";
        String senderName = "Central Estore Service";
        String mailContent = "<img src="+imageLink+" /> <br/><p> Hi, "+ messageDto.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us. "+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +messageDto.getUrl()+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you. </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(messageDto.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendLoginNotification(EmailNotificationDto messageDto) throws MessagingException, UnsupportedEncodingException {
        String imageLink = "https://static.vecteezy.com/system/resources/thumbnails/002/478/050/small/welcome-label-lettering-with-golden-letters-and-stars-free-vector.jpg";
        String subject = "Successful Login";
        String senderName = "Central Estore Service";
        String mailContent = "<img src="+imageLink+" /> <br/><h2> Dear, "+ messageDto.getFirstName()+ ", </h2>"+
                "<p>You have logged-in successfully to your account "+"" +
                "on "+date+ " at "+time+"</p>"+
                "<br /> <br /> Please if you did not initiate this action, contact our customer support on kindly contact customer support on <b>info@techiebros.come"+
                "<p> Kind Regards </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(messageDto.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void reSendVerificationEmail(EmailNotificationDto messageDto) throws MessagingException, UnsupportedEncodingException {
        String imageLink = "https://i.pinimg.com/originals/b9/e7/e8/b9e7e8e510755020185a1a7ca1ff271b.png";
        String subject = "Email Verification - Resent";
        String senderName = "Central Estore Service";
        String mailContent = "<img src="+imageLink+" /> <br/><p> Hi, "+ messageDto.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us. "+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +messageDto.getUrl()+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you. </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(messageDto.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendResetPasswordEmail(PasswordResetDto passwordReset) throws MessagingException, UnsupportedEncodingException {
        String imageLink = "https://th.bing.com/th/id/OIP.xMQWlxxUn3HMVspv7zQHXgHaBv?pid=ImgDet&rs=1";
        String subject = "Password Reset";
        String senderName = "Central Estore Service";
        String mailContent = "<img src="+imageLink+" /> <br/> <p> Hi, "+ passwordReset.getFirstName()+ ", </p>"+
                "<p>Below is the token to reset your password. "+"" +
                "If you did not initiate this request, kindly contact customer support on <b>info@techiebros.come</b>.</p>"+
                "<h2 style='color: #057d25; letter-spacing: 0.1em'>"+passwordReset.getToken()+"</h2>"+
                "<p> Thank you. </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(passwordReset.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendOrderSummaryEmail(OrderMessageNotification orderSummary) throws MessagingException, UnsupportedEncodingException {
        String subject = "Order Summary";
        String senderName = "Central Estore Service";

        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<p>Hi, ").append(orderSummary.getUserFullName()).append(",</p>");
        mailContent.append("<p>Thank you for your order. Below is the summary of your order:</p></br>");
        mailContent.append("<p style='color:#17662c;'>Order Reference Id:</p>");
        mailContent.append("<p style='font-family: Century Gothic; font-weight: bold;'>").append(orderSummary.getOrderReference()).append("</p>");
        // Build the table with order details
        mailContent.append("<table style=\"border-collapse: collapse; width: 100%;\">");
        mailContent.append("<tr>")
                .append("<th style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">Product Names</th>")
                .append("<th style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">Quantity</th>")
                .append("<th style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">Unit Price</th>")
                .append("<th style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">Subtotal</th>")
                .append("</tr>");

        List<CartResponse> orderItems = orderSummary.getProducts();

        for (CartResponse item : orderItems) {
            mailContent.append("<tr>")
                    .append("<td style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">").append(item.getProductName()).append("</td>")
                    .append("<td style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">").append(item.getUnit()).append("</td>")
                    .append("<td style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">").append(item.getAmount()).append("</td>")
                    .append("<td style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\">").append(item.getSubtotal()).append("</td>")
                    .append("</tr>");
        }
        mailContent.append("<tr>")
                .append("<td style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\" colspan=\"3\"><b>Total Amount</b></td>")
                .append("<td style=\"border: 1px solid #dddddd; text-align: left; padding: 8px;\"><b>").append(orderSummary.getTotalAmount()).append("</b></td>")
                .append("</tr>");

        mailContent.append("</table>");

        mailContent.append("<p>Thank you.</p><hr><br><b>Central Estore Service.</b>");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(orderSummary.getUserEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent.toString(), true);

        mailSender.send(message);
    }

}

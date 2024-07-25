package server;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Properties;

public class Email {
    String host = "smtp.gmail.com";
    String username = "eelection3.portal@gmail.com";
    String from = "eelection3.portal@gmail.com";
    String password = "mngv mtyl wmzk exmi";
    Session session;

    public Email() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        String username = this.username;
        String password = this.password;
        this.session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        this.session.setDebug(false);
    }

    public void sendParticipantRegistrationRequestEmail(String to, String participantEmail, String username) throws MessagingException {
        MimeMessage message = new MimeMessage(this.session);
        message.setFrom(new InternetAddress(this.from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Notification of registration under your school");
        StringBuilder emailMessage = new StringBuilder();
        emailMessage.append("New participant notification\n\n");
        emailMessage.append("This message is to notify you of a new participant's request to register under your school\n\n");
        emailMessage.append("The participant details are as below\n");
        emailMessage.append("\tUsername: ").append(username).append("\n");
        emailMessage.append("\temail: ").append(participantEmail).append("\n");
        emailMessage.append("\nTo verify this participant please login into the command line and confirm with the commands\n");
        emailMessage.append("\tconfirm with:-> confirm yes/no " + username + "\n");
        message.setText(emailMessage.toString());
        Transport.send(message);
    }

    public void sendParticipantConfirmedEmail(String to, String participantName, String school) throws MessagingException {
        MimeMessage message = new MimeMessage(this.session);

        message.setFrom(new InternetAddress(this.from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Registration Confirmation");

        StringBuilder emailMessage = new StringBuilder();
        emailMessage.append("Congratulations, ").append(participantName).append("!\n\n");
        emailMessage.append("We are pleased to inform you that you have been successfully added to ").append(school).append(".\n\n");
        emailMessage.append("Welcome aboard! We look forward to your active participation.\n\n");
        emailMessage.append("Best regards,\n");
        emailMessage.append(school).append(" ADMIN");

        message.setText(emailMessage.toString());

        Transport.send(message);
    }


    public void sendParticipantRejectedEmail(String to, String participantName, String school) throws MessagingException {
        MimeMessage message = new MimeMessage(this.session);

        message.setFrom(new InternetAddress(this.from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Notification of Registration Rejection");

        StringBuilder emailMessage = new StringBuilder();
        emailMessage.append("Dear ").append(participantName).append(",\n\n");
        emailMessage.append("We hope this message finds you well. We regret to inform you that your request to register under ")
                .append(school)
                .append(" has been rejected by the school representatives.\n\n");
        emailMessage.append("Thank you for your interest and understanding.\n\n");
        emailMessage.append("Best regards,\n");
        emailMessage.append(school).append(" ADMIN");

        message.setText(emailMessage.toString());

        Transport.send(message);
    }


    public void sendChallengeReportPDF(String challenge, String participant, String email, JSONArray questions) throws IOException, DocumentException, MessagingException {

        String temporaryFilePath = "results.pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(temporaryFilePath));
        document.open();
        Font headingFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph heading = new Paragraph(challenge + " performance report", headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);
        document.add(new Paragraph("\n")); // Add some space
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Paragraph welcome = new Paragraph("Dear " + participant + ",\n\n" +
                "Thank you for participating in the " + challenge + " challenge. " +
                "We appreciate your effort and dedication. Below you will find a summary of your performance.", normalFont);
        welcome.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(welcome);
        document.add(new Paragraph("\n")); // Add some space before the table
        Font tableFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph tableHeading = new Paragraph("Questions attempted and there answers", tableFont);
        tableHeading.setAlignment(Element.ALIGN_LEFT);
        document.add(tableHeading);
        document.add(new Paragraph("\n"));
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell(createCell("qn", true));
        table.addCell(createCell("Question", true));
        table.addCell(createCell("Answer", true));
        table.addCell(createCell("Score", true));
        for (int i = 0; i < questions.length(); i++) {
            JSONObject qns = questions.getJSONObject(i);
            addRow(table, (i + 1), qns.getString("question"), qns.getString("answer"), qns.getInt("score"));
        }
        document.add(table);
        document.add(new Paragraph("\n")); // Add some space after the table
        Paragraph closing = new Paragraph("We hope you found this challenge rewarding and educational. " +
                "Keep up the great work, and we look forward to your participation in future challenges!", normalFont);
        closing.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(closing);
        document.close();
        Message message = new MimeMessage(this.session);
        message.setFrom(new InternetAddress(this.from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject(challenge + " PDF performance report for " + participant);
        BodyPart messageBodyPart = new MimeBodyPart();
        String emailBody = "Dear " + participant + ",\n\n" +
                "Thank you for participating in the \"" + challenge + "\" challenge. " +
                "We hope you found it both engaging and educational.\n\n" +
                "Attached to this email, you will find a PDF document containing a detailed report of the questions attempted. " +
                "This report includes:\n\n" +
                "- A list of all questions you answered\n" +
                "- The correct answer to each question\n" +
                "- The score received for each correct answer\n\n" +
                "We encourage you to review this report to reflect on your performance and identify areas for future improvement. " +
                "If you have any questions about your results or the challenge in general, please don't hesitate to reach out to us.\n\n" +
                "Once again, thank you for your participation. We look forward to seeing you in future challenges!\n\n" +
                "Best regards,\n" +
                "The MATH CHALLENGE Team";
        messageBodyPart.setText(emailBody);
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new File(temporaryFilePath));
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);
        Transport.send(message);
    }

    private static PdfPCell createCell(String content, boolean isHeader) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, isHeader ? Font.BOLD : Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private static void addRow(PdfPTable table, int qn, String question, String answer, int score) {
        table.addCell(createCell(String.valueOf(qn), false));
        table.addCell(createCell(question, false));
        table.addCell(createCell(answer, false));
        table.addCell(createCell(String.valueOf(score), false));
    }
//    public static void main(String[] args) throws MessagingException {
//        Email email = new Email();
//        email.sendParticipantRegistrationRequestEmail("ogenrwothjimfrank@gmail.com", "frank@gmail.com", "frank");
//    }
}
package utils;

import models.auth.PasswordChangeRequest;
import models.email.Attachment;
import models.email.SendEmailRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class TestDataGenerator {

    public static PasswordChangeRequest createPasswordChangeRequest(
            String oldPassword, String newPassword) {
        return new PasswordChangeRequest(oldPassword, newPassword);
    }

    public static SendEmailRequest createEmailRequest(
            String[] recipients, String subject, String content) {

        SendEmailRequest request = new SendEmailRequest();
        request.setRecipients(Arrays.asList(recipients));
        request.setSubject(subject);
        request.setContent(content);
        return request;
    }

    public static SendEmailRequest createEmailWithAttachment(
            String[] recipients, String subject, String content,
            String fileName, String mimeType, byte[] fileData) {

        SendEmailRequest request = createEmailRequest(recipients, subject, content);

        Attachment attachment = new Attachment();
        attachment.setFileName(fileName);
        attachment.setMimeType(mimeType);
        attachment.setFileData(Base64.getEncoder().encodeToString(fileData));

        request.setAttachments(Collections.singletonList(attachment));
        return request;
    }

    public static String generatePassword(boolean valid) {
        if (valid) {
            return "ValidPass123!";
        } else {
            return "invalid";
        }
    }
}
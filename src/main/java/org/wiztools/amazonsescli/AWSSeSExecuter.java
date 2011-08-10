package org.wiztools.amazonsescli;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

/**
 *
 * @author subWiz
 */
class AWSSeSExecuter {
    static void execute(Credentials creds, MailBean mail) {
        // Create the request:
        SendEmailRequest req = new SendEmailRequest();
        Destination dest = new Destination(mail.getTo());
        req.setDestination(dest);
        req.setSource(mail.getFrom());
        Message message = new Message();
        message.setSubject(new Content(mail.getSubject()));
        message.setBody(new Body(new Content(mail.getMessage())));
        req.setMessage(message);
        
        // Send the email:
        AWSCredentials awsCreds = new BasicAWSCredentials(
                        creds.getAwsAccessKeyId(), creds.getAwsSecretKey());
        AmazonSimpleEmailService service = new 
                        AmazonSimpleEmailServiceClient(awsCreds);
        service.sendEmail(req);
    }
}

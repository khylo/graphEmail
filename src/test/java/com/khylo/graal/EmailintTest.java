package com.khylo.graal;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class EmailintTest {

    @Autowired
    AzureAuthorizationCodeProvider authProvider;

    @Test
    void testEmail(){
// Create a new Outlook client
        GraphServiceClient graphServiceClient = authProvider.getGraphClient();

// Create a new email message
        Message message = new Message();
        message.subject = "Hello from Java!";
        message.body = new ItemBody();
        message.body.contentType = BodyType.TEXT;
        message.body.content = "This is the content of the email.";

// Set the recipient
        EmailAddress targetEmail = new EmailAddress();
        targetEmail.address = "keith.hyland@gmail.com";
        Recipient recipient = new Recipient();
        recipient.emailAddress = targetEmail;
        message.toRecipients = Collections.singletonList(recipient);

// Send the email
        graphServiceClient.me().sendMail(message, true).buildRequest().post();

    }
}

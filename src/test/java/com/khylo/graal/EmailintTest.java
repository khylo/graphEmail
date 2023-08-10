package com.khylo.graal;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.models.UserSendMailParameterSet;
import com.microsoft.graph.requests.GraphServiceClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class EmailintTest {

    @Autowired
    AzureAuthorizationCodeProvider authProvider;

    static GraphServiceClient graphClient;

    static String userPrincipal;

    @BeforeAll
    static void testClientCredentialsProvider() throws IOException {
        Properties secrets = new Properties();
        secrets.load(new BufferedReader(new FileReader(".vscode/secrets.properties")));
        // Example taken from https://learn.microsoft.com/en-gb/graph/sdks/choose-authentication-providers?tabs=java#client-credentials-provider
        final String tenantId = secrets.getProperty("app.tenant.id");
        final String clientId = secrets.getProperty("app.client.id");
        final String clientSecret = secrets.getProperty("app.client.secret.val");

// The client credentials flow requires that you request the
// /.default scope, and pre-configure your permissions on the
// app registration in Azure. An administrator must grant consent
// to those permissions beforehand.
        final List<String> scopes = Arrays.asList("https://graph.microsoft.com/.default");

        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .clientId(clientId).tenantId(tenantId).clientSecret(clientSecret).build();

        if (null == scopes || null == credential) {
            throw new RuntimeException("Unexpected error");
        }
        final TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(
                scopes, credential);

        graphClient = GraphServiceClient.builder()
                .authenticationProvider(authProvider).buildClient();

        userPrincipal = secrets.getProperty("app.user.principal.name");
    }
    @Test
    void testEmail(){
        Message message = new Message();
        message.subject = "Meet for lunch?";
        ItemBody body = new ItemBody();
        body.contentType = BodyType.TEXT;
        body.content = "The new cafeteria is open.";
        message.body = body;
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = "keith.hyland@gmail.com";
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;
        LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
        Recipient ccRecipients = new Recipient();
        EmailAddress emailAddress1 = new EmailAddress();
        emailAddress1.address = "keith_hyland@yahoo.com";
        ccRecipients.emailAddress = emailAddress1;
        ccRecipientsList.add(ccRecipients);
        message.ccRecipients = ccRecipientsList;

        boolean saveToSentItems = false;

        //graphClient.me()
        graphClient.users(userPrincipal)
                .sendMail(UserSendMailParameterSet
                        .newBuilder()
                        .withMessage(message)
                        .withSaveToSentItems(saveToSentItems)
                        .build())
                .buildRequest()
                .post();

    }
    @Test
    void getProfile() {
        //graphClient.me()
        graphClient.users(userPrincipal)
                .buildRequest()
                .get();
    }
    @Test
    void getProfileMe() {
        graphClient.me()
                .buildRequest()
                .get();
    }

    @Test
    void getTodo() {
        //graphClient.me()
        graphClient.users(userPrincipal)
                .todo().buildRequest()
                .get();
    }
}

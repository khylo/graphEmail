package com.khylo.graal;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.*;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

public class AzureAuthorizationCodeProvider {

    @Value("app.client.id")
    final String clientId = "YOUR_CLIENT_ID";
    @Value("app.tenant.id")
    final String tenantId = "YOUR_TENANT_ID"; // or "common" for multi-tenant apps
    @Value("app.cert.path")
    final String clientCertificatePath = "MyCertificate.pem";
    @Value("app.client.secret")
    final String clientSecret = "YOUR_CLIENT_SECRET";
    @Value("app.use.cert")
    final boolean useCert = false;
    final List<String> scopes = Arrays.asList("User.Read");

    public GraphServiceClient getGraphClient() {

        final List<String> scopes = Arrays.asList("https://graph.microsoft.com/.default");
        TokenCredential credential;
        if(useCert)
            credential = new ClientCertificateCredentialBuilder()
                .clientId(clientId).tenantId(tenantId).pemCertificate(clientCertificatePath)
                .build();
        else
            credential = new ClientSecretCredentialBuilder()
                .clientId(clientId).tenantId(tenantId).clientSecret(clientSecret).build();


        if (null == scopes || null == credential) {
            throw new RuntimeException(String.format("Unexpected error, scopes {} or credential {} are null ", scopes, credential));
        }
        final TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(
                scopes, credential);

        return  GraphServiceClient.builder()
                .authenticationProvider(authProvider).buildClient();
    }
}

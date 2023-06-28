package com.khylo.graal.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.*;
import com.microsoft.graph.authentication.IAuthenticationProvider;

@RestController
public class EmailController {

    private final IAuthenticationProvider authenticationProvider;

    public EmailController(IAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody EmailRequest emailRequest) {
        GraphServiceClient<Request> graphClient = GraphServiceClient.builder()
                .authenticationProvider(authenticationProvider)
                .buildClient();

        Message message = new Message();
        // Set email properties...

        graphClient.me().sendMail(message, true).buildRequest().post();
    }
}


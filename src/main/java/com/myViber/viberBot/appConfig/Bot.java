package com.myViber.viberBot.appConfig;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.Request;
import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.ViberBot;
import com.viber.bot.message.TextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class Bot implements ApplicationListener<ApplicationReadyEvent> {

    private ViberBot bot;

    private ViberSignatureValidator signatureValidator;

    @Value("${viberbot.webhook}")
    private String webhookUrl;

    public Bot(ViberBot bot, ViberSignatureValidator signatureValidator) {
        this.bot = bot;
        this.signatureValidator = signatureValidator;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent appReadyEvent) {
        try {
            bot.setWebhook(webhookUrl).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        bot.onMessageReceived((event, message, response) -> response.send(message)); // echos everything back
        bot.onConversationStarted(event -> Futures.immediateFuture(Optional.of( // send 'Hi UserName' when conversation is started
                new TextMessage("Hi " + event.getUser().getName()))));
    }

    @PostMapping(value = "/", produces = "application/json")
    public String incoming(@RequestBody String json,
                           @RequestHeader("X-Viber-Content-Signature") String serverSideSignature)
            throws ExecutionException, InterruptedException, IOException {
        Preconditions.checkState(signatureValidator.isSignatureValid(serverSideSignature, json), "invalid signature");
        InputStream response = bot.incoming(Request.fromJsonString(json)).get();
        return response != null ? CharStreams.toString(new InputStreamReader(response, Charsets.UTF_16)) : null;
    }
}

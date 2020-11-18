package com.myViber.viberBot.appConfig.BotConfig;

import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.ViberBot;
import com.viber.bot.profile.BotProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    @Value("${viberbot.token}")
    private String authToken;

    @Value("${viberbot.name}")
    private String name;

    @Value("${viberbot.avatar}")
    private String avatar;

    @Bean
    ViberBot viberBot() {
        return new ViberBot(new BotProfile(name), authToken);
    }

    @Bean
    ViberSignatureValidator signatureValidator() {
        return new ViberSignatureValidator(authToken);
    }
}

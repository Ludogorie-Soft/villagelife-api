package com.example.ludogorieSoft.village.slack;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class SlackMessage {

    private final MethodsClient client;

    public SlackMessage(MethodsClient client) {
        this.client = client;
    }

    public void publishMessage(String channelName, String message) {
        try {
            client.chatPostMessage(r -> r
                    .channel(channelName)
                    .text(message)
            );
        } catch (IOException | SlackApiException e) {
            log.error("Unsuccessful attempt to send a slack notification. Reason: {}", e.getMessage());
        }
    }
}
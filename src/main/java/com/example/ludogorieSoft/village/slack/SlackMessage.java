package com.example.ludogorieSoft.village.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@Slf4j
public class SlackMessage {
    @Value("${slack.token}")
    private  String SLACK_TOKEN;

    public void publishMessage(String channelName, String message) {
        MethodsClient client = Slack.getInstance().methods();
        try {
            client.chatPostMessage(r -> r
                    .token(SLACK_TOKEN)
                    .channel(channelName)
                    .text(message)
            );
        } catch (IOException | SlackApiException e) {
            log.error("Unsuccessful attempt to send a slack notification. Reason: {}", e.getMessage());
        }
    }
}

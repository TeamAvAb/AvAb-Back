package com.avab.avab.slack;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.slack.api.Slack;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.LayoutBlock;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SlackService {
    @Value("${slack.token}")
    private String token;

    public void sendMessage(SlackChannel channel, List<LayoutBlock> message) {
        try {
            ChatPostMessageRequest request =
                    ChatPostMessageRequest.builder()
                            .channel(channel.getChannelId())
                            .blocks(message)
                            .build();

            ChatPostMessageResponse response =
                    Slack.getInstance().methods(token).chatPostMessage(request);
            System.out.println(response.getMessage());
        } catch (Exception e) {
            log.error("슬랙 메시지 전송 실패: {}", e.getMessage());
        }
    }
}

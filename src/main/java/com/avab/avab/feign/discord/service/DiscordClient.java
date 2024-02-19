package com.avab.avab.feign.discord.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.avab.avab.feign.discord.DiscordFeignConfiguration;
import com.avab.avab.feign.discord.dto.DiscordMessage;

@FeignClient(
        name = "discord-client",
        url =
                "https://discord.com/api/webhooks/1209167990445183026/Wo9un9SILBNAdt3HQYEx76pvn5TFeF-KDKzdxeGyvE4aczwWxv2f7ndsh-eBwunl_QeD",
        configuration = DiscordFeignConfiguration.class)
public interface DiscordClient {

    @PostMapping()
    void sendAlarm(@RequestBody DiscordMessage message);
}

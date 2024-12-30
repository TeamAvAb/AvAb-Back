package com.avab.avab.apiPayload.exception;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.avab.avab.slack.SlackChannel;
import com.avab.avab.slack.SlackService;
import com.slack.api.model.block.LayoutBlock;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExceptionNotifier {
    private final SlackService slackService;

    public void notify(Exception e, WebRequest request) {
        slackService.sendMessage(SlackChannel.SERVER_ERROR, createMessage(e, request));
    }

    private List<LayoutBlock> createMessage(Exception e, WebRequest request) {
        return asBlocks(
                // 타이틀
                header(h -> h.text(plainText(pt -> pt.text("🚨 에러 발생")))),

                // 구분선
                divider(),

                // 에러 정보
                section(section -> section.text(markdownText("*ℹ️ 에러 정보*"))),
                // 발생 시간
                section(section -> section.text(markdownText("*🕖 발생 시간*"))),
                section(
                        section ->
                                section.text(
                                        markdownText(
                                                DateTimeFormatter.ofPattern(
                                                                "yyyy-MM-dd HH:mm:ss:SSS")
                                                        .format(LocalDateTime.now())))),

                // 요청 URL
                section(section -> section.text(markdownText("*🔗 요청 URL*"))),
                section(section -> section.text(markdownText(createRequestFullPath(request)))),

                // Stack Trace
                section(section -> section.text(markdownText("*📄 Stack Trace*"))),
                section(
                        section ->
                                section.text(
                                        markdownText(
                                                "```\n"
                                                        + getStackTrace(e).substring(0, 1000)
                                                        + "\n```"))));
    }

    private String createRequestFullPath(WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        if (queryString != null) {
            fullPath += "?" + queryString;
        }

        return fullPath;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}

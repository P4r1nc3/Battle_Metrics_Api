package com.battlemetrics.service;

import com.battlemetrics.exceptions.EmailSendingException;
import com.battlemetrics.dao.response.bmapi.PlayerSessionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    private final PlayerService playerService;

    @Async
    public void sendNotification(PlayerSessionResponse playerSession) {
        boolean status = playerService.isPlayerOnline(playerSession).isOnline();
        String onlineContent = mailContentBuilder.buildPlayerOnlineContent(playerSession);
        String offlineContent = mailContentBuilder.buildPlayerOfflineContent(playerSession);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springBattleMetricsApi@email.com");
            messageHelper.setTo("konrad.tupta@gmail.com");
            messageHelper.setSubject("Status has changed");
            messageHelper.setText(status ? onlineContent : offlineContent);
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new EmailSendingException("Exception occurred when sending mail.", e);
        }
    }
}

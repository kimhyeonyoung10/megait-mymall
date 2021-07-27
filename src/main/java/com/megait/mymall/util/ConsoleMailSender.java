package com.megait.mymall.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

//JavaMailSender 란?
//        => spring-boot-start-mail에 들어있는 메일 전송 인터페이스
@Component // 이 어노테이션 써야지 AUTOWIRED할 수 있다.
@Profile("local") // 실제 서비스를 주는게 아니기때문에 profile=local로 설정해주어야지 이 안에서만 테스트 할 수 있다.
@Slf4j
public class ConsoleMailSender implements JavaMailSender {
    // createMimeMessage() : MIME 형식으로 메일보낼 때, 해당 메시지를 생성함. (예. html을 메일에 보내기)
    // MIME : text/html, image/png, application/json, ...
    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {

    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {

    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

    }

    // send() : 메일 전송 실행하는 메서드.
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        log.info("TO. {}", simpleMessage.getTo());
        log.info("FROM. {}", simpleMessage.getFrom());
        log.info("TITLE. {}", simpleMessage.getSubject());
        log.info("CONTENT. {}", simpleMessage.getText());
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }
}

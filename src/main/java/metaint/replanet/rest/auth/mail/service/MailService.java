package metaint.replanet.rest.auth.mail.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.mail.dto.MailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class MailService {
    @Autowired
    MailHandler mailHandler;
    private static final String FROM_ADDRESS = "projectdasi2023@gmail.com";

    public void mailSend(MailDto mailDto) throws MessagingException {
        mailHandler.setFrom(FROM_ADDRESS);
        mailHandler.setTo(mailDto.getEmail());
        mailHandler.setSubject(mailDto.getTitle());
        mailHandler.setText(mailDto.getMessage(), true);
        mailHandler.send();
    }

    public void mailSendToOrg(MailDto mailDto) throws MessagingException {
        log.info("[mailSendToOrg() 가입 성공 기부처 메일 전송 ===================================]");
        log.info("[mailSendToOrg() 받아온 email] : " + mailDto.getEmail());
        mailHandler.setFrom(FROM_ADDRESS);
        mailHandler.setTo(mailDto.getEmail());
        mailHandler.setSubject(mailDto.getTitle());
        mailHandler.setText(mailDto.getMessage(), true);
        mailHandler.send();
    }
}
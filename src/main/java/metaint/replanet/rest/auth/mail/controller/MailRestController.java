package metaint.replanet.rest.auth.mail.controller;

import metaint.replanet.rest.auth.mail.dto.MailDto;
import metaint.replanet.rest.auth.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/")
public class MailRestController {
    @Autowired
    MailService mailService;
    @PostMapping("/mailCerti")
    public void mailCerti(MailDto email) throws MessagingException {
        mailService.mailSend(email);
    }
}
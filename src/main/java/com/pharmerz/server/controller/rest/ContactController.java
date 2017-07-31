package com.pharmerz.server.controller.rest;

import com.pharmerz.Appcations.SendEmail;
import com.pharmerz.server.domain.model.UserContacts;
import com.pharmerz.server.domain.repository.IUserContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ContactController {

    @Autowired
    IUserContactRepository iUserContactRepository;

    @Autowired
    SendEmail sendEmail;

    @PostMapping("/contactus")
    public ResponseEntity<UserContacts> postContacts(UserContacts userContacts){

        System.out.println("i m in contactus controller");
        System.out.println(userContacts.getEmail());
        System.out.println(userContacts.getFullName());
        System.out.println(userContacts.getAcknowledge());
        System.out.println(userContacts.getAddress());
        System.out.println(userContacts.getComments());

        String emailTo=userContacts.getEmail();
        String name=userContacts.getFullName();
        String subject="Thank you for registring on our website";
        String body ="Dear "+name+" :\n\n Thank you for registring";

        UserContacts createdUser=iUserContactRepository.save(userContacts);
        sendEmail.sendmail(emailTo,subject,body);

        return new ResponseEntity<UserContacts>(createdUser, HttpStatus.CREATED);

    }

}

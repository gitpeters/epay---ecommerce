package com.peters.User_Registration_and_Email_Verification.event;

import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompletePublisher extends ApplicationEvent {

    private UserEntity user;
    private String applicationUrl;

    public RegistrationCompletePublisher(UserEntity user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}

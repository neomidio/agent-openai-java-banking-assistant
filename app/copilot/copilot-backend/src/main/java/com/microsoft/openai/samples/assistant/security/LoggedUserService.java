package com.microsoft.openai.samples.assistant.security;

import com.microsoft.openai.samples.assistant.session.DemoUser;
import com.microsoft.openai.samples.assistant.session.DemoUserRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoggedUserService {

    private final DemoUserRegistry demoUserRegistry;

    public LoggedUserService(DemoUserRegistry demoUserRegistry) {
        this.demoUserRegistry = demoUserRegistry;
    }

    public LoggedUser getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //this is always true in the PoC code
        if(authentication == null) {
           return mapDemoUser(demoUserRegistry.defaultUser());
        }
        //this code is never executed in the PoC. It's a hook for future improvements requiring integration with authentication providers.
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            Object details = authentication.getDetails();
            //object should be cast to specific type based on the authentication provider
            return new LoggedUser(currentUserName, "changeme@contoso.com", "changeme", "changeme");
        }
        return mapDemoUser(demoUserRegistry.defaultUser());
    }

    public LoggedUser resolveLoggedUser(String email) {
        DemoUser demoUser = demoUserRegistry.findByEmail(email)
                .orElseGet(demoUserRegistry::defaultUser);
        return mapDemoUser(demoUser);
    }

    private LoggedUser mapDemoUser(DemoUser demoUser) {
        return new LoggedUser(demoUser.email(), demoUser.email(), "generic", demoUser.displayName());
    }
}

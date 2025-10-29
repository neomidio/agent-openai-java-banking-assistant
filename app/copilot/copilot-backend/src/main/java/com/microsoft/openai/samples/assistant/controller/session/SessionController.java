package com.microsoft.openai.samples.assistant.controller.session;

import com.microsoft.openai.samples.assistant.session.DemoUser;
import com.microsoft.openai.samples.assistant.session.DemoUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final DemoUserRegistry demoUserRegistry;

    public SessionController(DemoUserRegistry demoUserRegistry) {
        this.demoUserRegistry = demoUserRegistry;
    }

    @GetMapping("/users")
    public List<DemoUser> availableUsers() {
        return demoUserRegistry.allUsers();
    }
}


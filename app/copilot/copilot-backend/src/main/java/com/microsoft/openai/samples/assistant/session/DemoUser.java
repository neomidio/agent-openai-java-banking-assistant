package com.microsoft.openai.samples.assistant.session;

/**
 * Represents one of the demo banking identities available in the sandbox environment.
 */
public record DemoUser(
        String email,
        String displayName,
        String accountId,
        String currency) {
}


package com.microsoft.openai.samples.assistant.langchain4j.factory;

import com.microsoft.openai.samples.assistant.invoice.DocumentIntelligenceInvoiceScanHelper;
import com.microsoft.openai.samples.assistant.langchain4j.agent.SupervisorAgent;
import com.microsoft.openai.samples.assistant.langchain4j.agent.mcp.AccountMCPAgent;
import com.microsoft.openai.samples.assistant.langchain4j.agent.mcp.PaymentMCPAgent;
import com.microsoft.openai.samples.assistant.langchain4j.agent.mcp.TransactionHistoryMCPAgent;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupervisorAgentFactory {

    private final ChatLanguageModel chatLanguageModel;
    private final DocumentIntelligenceInvoiceScanHelper documentIntelligenceInvoiceScanHelper;
    private final String transactionsMCPServerUrl;
    private final String accountsMCPServerUrl;
    private final String paymentsMCPServerUrl;

    public SupervisorAgentFactory(
            ChatLanguageModel chatLanguageModel,
            DocumentIntelligenceInvoiceScanHelper documentIntelligenceInvoiceScanHelper,
            @org.springframework.beans.factory.annotation.Value("${transactions.api.url}") String transactionsMCPServerUrl,
            @org.springframework.beans.factory.annotation.Value("${accounts.api.url}") String accountsMCPServerUrl,
            @org.springframework.beans.factory.annotation.Value("${payments.api.url}") String paymentsMCPServerUrl) {
        this.chatLanguageModel = chatLanguageModel;
        this.documentIntelligenceInvoiceScanHelper = documentIntelligenceInvoiceScanHelper;
        this.transactionsMCPServerUrl = transactionsMCPServerUrl;
        this.accountsMCPServerUrl = accountsMCPServerUrl;
        this.paymentsMCPServerUrl = paymentsMCPServerUrl;
    }

    public SupervisorAgent createForUser(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("userEmail cannot be null or empty");
        }

        AccountMCPAgent accountMCPAgent = new AccountMCPAgent(
                chatLanguageModel,
                userEmail,
                accountsMCPServerUrl);

        TransactionHistoryMCPAgent transactionHistoryMCPAgent = new TransactionHistoryMCPAgent(
                chatLanguageModel,
                userEmail,
                transactionsMCPServerUrl,
                accountsMCPServerUrl);

        PaymentMCPAgent paymentMCPAgent = new PaymentMCPAgent(
                chatLanguageModel,
                documentIntelligenceInvoiceScanHelper,
                userEmail,
                transactionsMCPServerUrl,
                accountsMCPServerUrl,
                paymentsMCPServerUrl);

        return new SupervisorAgent(chatLanguageModel, List.of(
                accountMCPAgent,
                transactionHistoryMCPAgent,
                paymentMCPAgent));
    }
}


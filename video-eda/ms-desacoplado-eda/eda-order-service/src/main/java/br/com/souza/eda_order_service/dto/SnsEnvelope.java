package br.com.souza.eda_order_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SnsEnvelope(
        String Type,
        String MessageId,
        String TopicArn,
        String Message,
        String Timestamp
) {}
package br.com.souza.eda_order_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnsService {

    private final SnsAsyncClient snsClient;

    public void sendMessage(String topicArn, String message) {
        try{
            snsClient.publish(builder -> builder.topicArn(topicArn).message(message));
        }catch (Exception e){
            log.error("Erro ao enviar mensagem SNS: {}", e.getMessage());
        }
    }

}

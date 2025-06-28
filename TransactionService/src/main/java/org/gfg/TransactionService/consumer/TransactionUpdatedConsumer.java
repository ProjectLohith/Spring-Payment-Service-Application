package org.gfg.TransactionService.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.gfg.TransactionService.enums.TransactionStatus;
import org.gfg.TransactionService.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.gfg.TransactionService.constants.KafkaConstants.TRANSACTION_UPDATED_TOPIC;
import static org.gfg.TransactionService.constants.TransactionUpdatedConstants.STATUS;
import static org.gfg.TransactionService.constants.TransactionUpdatedConstants.STATUSMESSAGE;
import static org.gfg.TransactionService.constants.TransactionUpdatedConstants.TRANSACTIONID;

@Service
@Slf4j
public class TransactionUpdatedConsumer {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TransactionRepository repository;

    @KafkaListener(topics = TRANSACTION_UPDATED_TOPIC, groupId = "test-transaction-service")
    public void transactionInitiated(String message) throws JsonProcessingException {
        log.info("Transaction updated message received: {}", message);
        ObjectNode node = objectMapper.readValue(message, ObjectNode.class);

        String status = node.get(STATUS).textValue();
        String statusMessage = node.get(STATUSMESSAGE).textValue();
        String transactionId = node.get(TRANSACTIONID).textValue();

        repository.updateTransactionStatus(TransactionStatus.valueOf(status),
                statusMessage, transactionId);

        log.info("Transaction updated successfully");

    }
}

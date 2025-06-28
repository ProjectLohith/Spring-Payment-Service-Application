package org.gfg.TransactionService.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.gfg.TransactionService.client.UserServiceClient;
import org.gfg.TransactionService.dto.InitiateTransactionRequest;
import org.gfg.TransactionService.enums.TransactionStatus;
import org.gfg.TransactionService.model.Transaction;
import org.gfg.TransactionService.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.gfg.TransactionService.constants.KafkaConstants.TRANSACTION_INITIATED_TOPIC;
import static org.gfg.TransactionService.constants.TransactionInitiatedConstants.AMOUNT;
import static org.gfg.TransactionService.constants.TransactionInitiatedConstants.RECEIVERPHONENO;
import static org.gfg.TransactionService.constants.TransactionInitiatedConstants.SENDERPHONENO;
import static org.gfg.TransactionService.constants.TransactionInitiatedConstants.TRANSACTIONID;

@Service
@Slf4j
public class TransactionService implements UserDetailsService {

    @Autowired
    UserServiceClient userServiceClient;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {

        //here we are not checking the actual given user credentials because at this particular stage,
        //we don't have user's password as we only have username/PhoneNo which is already this method arguments. Also, ideally since we are calling
        // to user service with transaction service so we should use transaction service credentials only , that's why are created a dummy data in user service table for transaction
        // service and generate the Basic token using these transaction service credentials and call that getUser API of user service using actual given username.

        String auth = "transaction-service:transaction-service";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());

        String authValue = "Basic "+ new String(encodedAuth);
        ObjectNode node = userServiceClient.getUser(phoneNo, authValue);

        log.info("user fetched: {}", node);

        if (node == null) {
            throw new UsernameNotFoundException("user does not exist");
        }

        //Here since the aim of this method is to provide User but it is not maintained by transaction service,
        // So we are retrieving the User from user service if present and returning User with needed fields for validation(for Role Access) in security context(next step of execution) like Username and Password and List of Authorities.
        ArrayNode authorities = (ArrayNode) node.get("authorities");

        final List<GrantedAuthority> authorityList = new ArrayList<>();

        authorities.iterator().forEachRemaining(jsonNode -> {
            authorityList.add(new SimpleGrantedAuthority(jsonNode.get("authority").textValue()));
        });

        User user = new User(node.get("phoneNo").textValue(), node.get("password").textValue(), authorityList);
        return user;
    }




    public String initiateTransaction(String senderPhoneNo,
                                      InitiateTransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .senderPhoneNo(senderPhoneNo)
                .receiverPhoneNo(request.getReceiverPhoneNo())
                .amount(request.getAmount())
                .purpose(request.getMessage())
                .status(TransactionStatus.INITIATED).build();

        transactionRepository.save(transaction);

        log.info("transaction saved");

        //publish the data to Kafka

        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put(SENDERPHONENO, transaction.getSenderPhoneNo());
        objectNode.put(RECEIVERPHONENO, transaction.getReceiverPhoneNo());
        objectNode.put(AMOUNT, transaction.getAmount());
        objectNode.put(TRANSACTIONID, transaction.getTransactionId());

        String kafkaMessage = objectNode.toString();
        kafkaTemplate.send(TRANSACTION_INITIATED_TOPIC, kafkaMessage);

        log.info("Message published to Kafka: {}", kafkaMessage);

        return transaction.getTransactionId();
    }



    public List<Transaction> findTransactions(String senderPhoneNo,
                                              Integer pageNo,
                                              Integer limit) {

        Pageable pageable = PageRequest.of(pageNo, limit);

//        Page<Transaction> response = transactionRepository.findBySenderPhoneNo(senderPhoneNo, pageable);

        return transactionRepository.findBySenderPhoneNo(senderPhoneNo, pageable);

    }
}
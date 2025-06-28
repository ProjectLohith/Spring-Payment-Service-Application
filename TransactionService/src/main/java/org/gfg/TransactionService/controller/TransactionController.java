package org.gfg.TransactionService.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.gfg.TransactionService.dto.InitiateTransactionRequest;
import org.gfg.TransactionService.model.Transaction;
import org.gfg.TransactionService.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public String initiateTransaction(@RequestBody @Valid InitiateTransactionRequest request) {
        log.info("CONTROLLER INVOKED");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String senderPhoneNo = userDetails.getUsername();
        return transactionService.initiateTransaction(senderPhoneNo, request);
    }

    @GetMapping("/transaction/all")
    public List<Transaction> getTransactions(@RequestParam("pageNo") Integer pageNo,
                                             @RequestParam("limit") Integer limit) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String senderPhoneNo = userDetails.getUsername();

        return transactionService.findTransactions(senderPhoneNo, pageNo, limit);
    }
}
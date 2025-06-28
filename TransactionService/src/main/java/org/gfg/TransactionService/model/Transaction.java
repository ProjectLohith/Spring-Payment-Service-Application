package org.gfg.TransactionService.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gfg.TransactionService.enums.TransactionStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String senderPhoneNo;

    String receiverPhoneNo;

    String transactionId; //we would return to the user

    Double amount;

    String purpose;

    String transactionStatusMessage;

    @Enumerated(value = EnumType.STRING)
    TransactionStatus status;

    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;

}
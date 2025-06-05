package com.example.serv1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object for CRUD event messages sent via RabbitMQ.
 * This class is used for communication between services.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrudEventMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Type of CRUD operation (CREATE, UPDATE, DELETE)
     */
    private String operation;

    /**
     * Type of entity that was modified (e.g., "Client")
     */
    private String entityType;

    /**
     * ID of the entity that was modified
     */
    private String entityId;

    /**
     * Additional details about the operation
     */
    private String message;

    /**
     * The service that performed the operation
     */
    private String sourceService = "command-service";

    /**
     * When the operation occurred
     */
    private long timestamp;

    /**
     * Status of the message (PUBLISHED, CONSUMED)
     */
    private String status;
}
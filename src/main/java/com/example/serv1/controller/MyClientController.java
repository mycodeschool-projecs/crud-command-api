package com.example.serv1.controller;

import com.example.serv1.model.MyClient;
import com.example.serv1.services.MyClientServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/v1/clients")
@Tag(name = "Client Management", description = "API for managing client data")
public class MyClientController {

    private MyClientServices clientServices;

    public MyClientController(MyClientServices clientServices) {
        this.clientServices = clientServices;
    }

    /**
     * Create a new client
     * @param client The client to create
     * @return The created client
     */
    @Operation(summary = "Create a new client", description = "Creates a new client with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyClient.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or client with email already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<MyClient> createClient(@RequestBody MyClient client) {
        log.info("Creating new client with email: {}", client.getEmail());
        try {
            MyClient newClient = clientServices.addClient(client);
            if (newClient != null) {
                log.info("Client created successfully with ID: {}", newClient.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
            }
            log.error("Failed to create client - returned null");
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Update an existing client
     * @param id The client ID
     * @param client The updated client data
     * @return The updated client
     */
    @Operation(summary = "Update an existing client", description = "Updates an existing client with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyClient.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or ID mismatch"),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MyClient> updateClient(
            @Parameter(description = "ID of the client to update") @PathVariable Long id, 
            @RequestBody MyClient client) {
        log.info("Updating client with ID: {}", id);

        // Ensure the path ID matches the client ID
        if (!id.equals(client.getId())) {
            log.error("Path ID {} does not match client ID {}", id, client.getId());
            return ResponseEntity.badRequest().build();
        }

        try {
            boolean isUpdated = clientServices.updClient(client);
            if (isUpdated) {
                MyClient updatedClient = clientServices.getClientfromEmail(client.getEmail());
                log.info("Client updated successfully: {}", updatedClient.getId());
                return ResponseEntity.ok(updatedClient);
            } else {
                log.error("Failed to update client with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error updating client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all clients
     * @return List of all clients
     */
    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of clients",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyClient.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<MyClient>> getAllClients() {
        log.info("Getting all clients");
        try {
            List<MyClient> clients = clientServices.getClients();
            log.info("Retrieved {} clients", clients.size());
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            log.error("Error retrieving clients: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Delete a client by email
     * @param email The email of the client to delete
     * @return Success status
     */
    @Operation(summary = "Delete a client by email", description = "Deletes a client with the specified email address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Client successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteClientByEmail(
            @Parameter(description = "Email of the client to delete") @PathVariable String email) {
        log.info("Deleting client with email: {}", email);
        try {
            boolean result = clientServices.delClient(email);
            if (result) {
                log.info("Client with email {} deleted successfully", email);
                return ResponseEntity.noContent().build();
            } else {
                log.error("Client with email {} not found", email);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a client by email
     * @param email The email of the client to find
     * @return The client if found
     */
    @Operation(summary = "Get a client by email", description = "Retrieves a client with the specified email address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved client",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyClient.class))),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<MyClient> getClientByEmail(
            @Parameter(description = "Email of the client to find") @PathVariable String email) {
        log.info("Finding client with email: {}", email);
        try {
            MyClient client = clientServices.getClientfromEmail(email);
            if (client != null) {
                log.info("Client found with ID: {}", client.getId());
                return ResponseEntity.ok(client);
            } else {
                log.error("Client with email {} not found", email);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error finding client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a client by ID
     * @param id The ID of the client to find
     * @return The client if found
     */
    @Operation(summary = "Get a client by ID", description = "Retrieves a client with the specified ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved client",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyClient.class))),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "501", description = "Not implemented yet"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MyClient> getClientById(
            @Parameter(description = "ID of the client to find") @PathVariable Long id) {
        log.info("Finding client with ID: {}", id);
        try {
            // This is a placeholder - the service doesn't have a getClientById method yet
            // In a real implementation, we would call clientServices.getClientById(id)
            // For now, we'll return a 501 Not Implemented
            log.warn("getClientById not implemented yet");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        } catch (Exception e) {
            log.error("Error finding client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

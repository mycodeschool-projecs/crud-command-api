package com.example.serv1.services;

import com.example.serv1.model.CrudEventMessage;
import com.example.serv1.model.MyClient;
import com.example.serv1.repository.MyClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing client data
 */
@Service
@Slf4j
public class MyClientServices {
    private final MyClientRepository clientRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name:crud-events-exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key:crud-events}")
    private String routingKey;

    public MyClientServices(MyClientRepository clientRepository, RabbitTemplate rabbitTemplate) {
        this.clientRepository = clientRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Add a new client
     * @param newClient The client to add
     * @return The added client
     * @throws Exception If the client already exists or there's an error
     */
    public MyClient addClient(MyClient newClient) throws Exception {
        log.info("Adding new client with email: {}", newClient.getEmail());

        try {
            Optional<MyClient> existingClients = clientRepository.findByEmail(newClient.getEmail());
            if (!existingClients.isPresent()) {
                // Save the client
                MyClient savedClient = clientRepository.save(newClient);
                log.info("Client saved successfully with ID: {}", savedClient.getId());

                // Publish event to RabbitMQ
                try {
                    publishEvent("CREATE", "Client", savedClient.getId()+"",
                            String.format("Client created: %s %s (%s)", 
                                    savedClient.getName(), savedClient.getSurName(), savedClient.getEmail()));
                } catch (Exception e) {
                    log.error("Failed to publish client creation event: {}", e.getMessage(), e);
                    // Don't fail the client creation if event publishing fails
                }

                return savedClient;
            } else {
                log.error("Client with email {} already exists", newClient.getEmail());
                throw new Exception("Client with this email already exists");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("already exists")) {
                throw e; // Re-throw the specific exception
            }
            log.error("Error adding client: {}", e.getMessage(), e);
            throw new Exception("Error adding client: " + e.getMessage());
        }
    }

    /**
     * Publish an event to RabbitMQ
     * @param operation The operation (CREATE, UPDATE, DELETE)
     * @param entityType The entity type
     * @param entityId The entity ID
     * @param message The event message
     */
    private void publishEvent(String operation, String entityType, String entityId, String message) {
        log.info("Publishing {} event for {} with ID {}: {}", operation, entityType, entityId, message);

        // Create event message
        CrudEventMessage eventMessage = new CrudEventMessage();
        eventMessage.setOperation(operation);
        eventMessage.setEntityType(entityType);
        eventMessage.setEntityId(entityId);
        eventMessage.setMessage(message);
        eventMessage.setTimestamp(System.currentTimeMillis());
        eventMessage.setStatus("PUBLISHED");

        // Send to RabbitMQ
        rabbitTemplate.convertAndSend(exchangeName, routingKey, eventMessage);
        log.info("Event published successfully");
    }

    public List<MyClient> getClients() throws Exception {

        try{
            return clientRepository.findAll();
        }catch (Exception e){
            throw new Exception("Nu a mers");
        }
    }

    public MyClient getClientfromEmail(String email) throws Exception {

        try{
            if(clientRepository.findByEmail(email).isPresent()){
                return clientRepository.findByEmail(email).get();
            }else{
                throw new Exception("Client did not exists!!");
            }

        }catch (Exception e){
            throw new Exception("Something goes wrong on adding client!!!");
        }
    }

    public boolean delClient(String email) throws Exception {

        try{
            Optional<MyClient> delClient=clientRepository.findByEmail(email);

            if(delClient.isPresent()){
                System.out.println("In del client");
                clientRepository.delete(delClient.get());
                return true;
            }else{
                throw new Exception("Client with email did not exists!!");
            }

        }catch (Exception e){
            throw new Exception("Something goes wrong on deleting client!!!");
        }
    }

    public boolean updClient(MyClient client) throws Exception{
        try{
            Optional<MyClient> opClient=clientRepository.findById(client.getId());
            if(opClient.isPresent()){
                MyClient clt=opClient.get();
                clt.setName(client.getName());
                clt.setSurName(client.getSurName());
                clt.setAge(client.getAge());
                clientRepository.save(clt);
                return true;
            }else{
                throw new Exception("Client did not exists!!");
            }

        }catch (Exception e){
            throw new Exception("Somethig goes wrong on updating!");
        }

    }

}

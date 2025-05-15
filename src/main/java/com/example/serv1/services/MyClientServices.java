package com.example.serv1.services;

import com.example.serv1.model.MyClient;
import com.example.serv1.rabbitMQListener.MessagePublisher;
import com.example.serv1.rabbitMQListener.MyMessage;
import com.example.serv1.rabbitMQListener.RabbitTemplateConfig;
import com.example.serv1.repository.MyClientRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyClientServices {
    private MyClientRepository clientRepository;

    private MessagePublisher messagePublisher;

    public MyClientServices(MyClientRepository clientRepository,
                            MessagePublisher messagePublisher){
        this.clientRepository=clientRepository;
        this.messagePublisher=messagePublisher;
    }

    public MyClient addClient(MyClient newClient) throws Exception {

        try{
            if(clientRepository.findByEmail(newClient.getEmail()).size()==0){
                clientRepository.save(newClient);
                return clientRepository.findByEmail(newClient.getEmail()).get(0);
            }else{
               throw new Exception("Client exists!!");

            }

        }catch (Exception e){
            throw new Exception("Something goes wrong on adding client!!!");
        }
    }

    public void sendMessageNote(String note){
        MyMessage<String> message=new MyMessage<>();
        message.setPriority(7);
        message.setContent(note);
        message.setMessage("processed");

        messagePublisher.sendMessageStringNotes(message);
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
            if(clientRepository.findByEmail(email).size()>0){
                return clientRepository.findByEmail(email).get(0);
            }else{
                throw new Exception("Client did not exists!!");
            }

        }catch (Exception e){
            throw new Exception("Something goes wrong on adding client!!!");
        }
    }

    public boolean delClient(String email) throws Exception {

        try{
            List<MyClient> delClient=clientRepository.findByEmail(email);

            if(delClient.size()==1){
                System.out.println("In del client");
                clientRepository.delete(delClient.get(0));
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

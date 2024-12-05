package com.example.serv1.services;

import com.example.serv1.model.MyClient;
import com.example.serv1.repository.MyClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyClientServices {
    private MyClientRepository clientRepository;

    public MyClientServices(MyClientRepository clientRepository){
        this.clientRepository=clientRepository;
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

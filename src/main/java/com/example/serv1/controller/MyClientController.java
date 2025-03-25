package com.example.serv1.controller;

import com.example.serv1.logs.StructuredLogger;
import com.example.serv1.model.MyClient;
import com.example.serv1.services.MyClientServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/v1/client")
//@CrossOrigin

public class MyClientController {

    private MyClientServices clientServices;

    private StructuredLogger logger;
    public MyClientController(MyClientServices clientServices,StructuredLogger logger) {
        this.clientServices = clientServices;
        this.logger=logger;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add")
    public ResponseEntity<MyClient> addClient(@RequestBody MyClient client)  {


        try{
            MyClient newClient=clientServices.addClient(client);
            if(newClient!=null){
                logger.logBuilder()
                        .withLevel("INFO")
                        .withMessage("MS1_ADDUSER_OK")
                        .withField("msAddClient",client).log();
                return ResponseEntity.ok(newClient);
            }
            return ResponseEntity.badRequest().body(null);

        }catch (Exception e){
            logger.logBuilder()
                    .withLevel("ERROR")
                    .withMessage(e.getMessage()).log();
            return ResponseEntity.badRequest().body(null);
        }


    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/upd")
    public ResponseEntity<MyClient> updClient(@RequestBody MyClient client) throws Exception {


        try{
            boolean isUpd= clientServices.updClient(client);
            if(isUpd){
                return ResponseEntity.ok(clientServices.getClientfromEmail(client.getEmail()));

            }else{
                return ResponseEntity.badRequest().body(null);
            }
        }catch (Exception e){
            throw new Exception("Add err");
        }


    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping ("/getall")
    public ResponseEntity<List<MyClient>> getClients() throws Exception {


        try{
                return ResponseEntity.ok(clientServices.getClients());
        }catch (Exception e){
            throw new Exception("Get err");
        }


    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/del/{eml}")
    public ResponseEntity<Boolean> delClient(@PathVariable String eml) throws Exception {

        try{
            boolean result=clientServices.delClient(eml);
            if(result==true){
                return ResponseEntity.ok(true);
            }
                return ResponseEntity.status(300).body(null);
        }catch (Exception e){
                throw new Exception(e.getMessage());
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find/{eml}")
    public ResponseEntity<MyClient> findClient(@PathVariable String eml) throws Exception {

        try{
            MyClient client=clientServices.getClientfromEmail(eml);
            return ResponseEntity.ok(client);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
}
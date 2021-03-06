package com.ims.service;

import com.ims.beans.Client;
import com.ims.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {


    @Autowired
    private ClientRepository clientRepository;

    /**
     * Register a new client
     *
     * @param client the client to be registered
     */
    public Client registerNewClient(Client client) throws Exception {

        if(clientRepository.getClientByUserName(client.getUserName()).size() != 0){
           throw new Exception("Client exists");
        }else {
            Client temp = new Client(client.getUserName(), client.getPassword());
            temp.setAuthCode(generateAuthKey());
            return temp;
        }
    }

    /**
     * Generate unique Auth key for a client
     */
    public String generateAuthKey(){
        String auth = UUID.randomUUID().toString().replace("-", "");

        while(clientRepository.getClientByAuthCode(auth).size() != 0){
            auth = UUID.randomUUID().toString().replace("-", "");
        }

        return auth;
    }

    /**
     * Login a client
     *
     * @param username the username of the client attempting to login
     *
     * @param password the password of the client
     */
    public int loginService(String username, String password){

        System.out.printf("Received login request with %s, %s \n",username,password);


        List<Client> clients = clientRepository.getClientByUserName(username);
        if(clients.size() > 0){
            Client c = clients.get(0);
            System.out.println("Found client: " + c.getUserName());
            if(c.getPassword().equals(password)) {
                System.out.printf("form password: %s matches with client password %s \n",password,c.getPassword());
                return 200;
            }else {
                System.out.printf("form password: %s does not match with client password %s \n",password,c.getPassword());
            }
        }
        return -1;
    }


}

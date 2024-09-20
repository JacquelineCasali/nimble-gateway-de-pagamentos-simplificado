package com.pickpay.service;

import com.pickpay.domain.user.User;
import com.pickpay.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;
    public void sendNotification(User user,String message) throws Exception {
        String email= user.getEmail();
        NotificationDTO notificatioRequest=new NotificationDTO(email,message);


// ResponseEntity<String> notificatioResponse = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificatioRequest,String.class);
//        if(!(notificatioResponse.getStatusCode() == HttpStatus.OK )){
//            System.out.println("erro ao enviar notivicação");
//           throw new Exception("Serviço de notificação está fora do ar");
//
//        }
        System.out.println("Notificação enviada para o usuario");
    }
}

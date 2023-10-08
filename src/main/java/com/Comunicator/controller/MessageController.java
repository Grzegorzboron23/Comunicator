package com.Comunicator.controller;


import com.Comunicator.model.Message;
import com.Comunicator.service.MessageService;
import com.Comunicator.service.UserAndGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired
    UserAndGroupService userAndGroupService;

    @MessageMapping("/chat/{to}")
    public void sendMessagePersonal(@DestinationVariable String to, Message message) {
        messageService.sendMessage(to,message);
    }

    @GetMapping("listmessage/{from}/{to}")
    public List<Map<String,Object>> getListMessageChat(@PathVariable("from") Integer from, @PathVariable("to") Integer to){
        return messageService.getListMessage(from, to);
    }

    @GetMapping("/fetchAllUsers/{myId}")
    public List<Map<String,Object>> fetchAll(@PathVariable("myId") String myId) {
        return userAndGroupService.fetchAll(myId);
    }

}

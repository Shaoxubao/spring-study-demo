package com.baoge.schedule;


import com.baoge.service.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;

@Component
@EnableScheduling
@Slf4j
public class PushMessageJob {

    @Autowired
    WebSocketServer webSocketServer;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendMessage() {
        Map<String, Session> onlineSessionClientMap = WebSocketServer.getOnlineSessionClientMap();
        for (Map.Entry<String, Session> entry : onlineSessionClientMap.entrySet()) {
            String cid = entry.getKey();
            log.info("send to {}", cid);
            webSocketServer.sendToOne(cid, "hello " + cid);
        }
    }

}

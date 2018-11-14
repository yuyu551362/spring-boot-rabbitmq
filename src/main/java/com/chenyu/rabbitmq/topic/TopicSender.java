package com.chenyu.rabbitmq.topic;

import com.chenyu.rabbitmq.MqEventManager;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TopicSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send() {
		String context = "hi, i am message all";
		System.out.println("Sender : " + context);
		for (int i =0;i<=100;i++){
            this.rabbitTemplate.convertAndSend(MqEventManager.EXCHANGE_SYNC,MqEventManager.ML_ROUTINGKEY_SYNC_COMMON+"chenYu"  , context);
        }

	}

}
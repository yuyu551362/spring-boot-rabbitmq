package com.chenyu.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: chenyu
 * @Date: 2018/11/7 09:10
 * @Description:
 */
@Configuration
public class MqConfig {
    /**
     * 定义公共交换机
     * @return
     */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(MqEventManager.EXCHANGE_SYNC);
    }

    /**
     * 定义命名队列，并将之与exchange绑定
     * 原始数据保存队列
     */
    @Bean
    Queue persistOriginalDataQueue(){
        Map<String,Object> arguments = new HashMap<>(  );
        /**
         * 消息因为超时或超过限制在队列里消失，这样我们就丢失了一些消息，
         * 也许里面就有一些是我们做需要获知的。而rabbitmq的死信功能则为我们带来了解决方案。
         * 设置了dead letter exchange与dead letter routingkey（要么都设定，要么都不设定）
         * 那些因为超时或超出限制而被删除的消息会被推动到我们设置的exchange中，再根据routingkey推到queue中
         */
        arguments.put( "x-dead-letter-exchange","dead_exchange" );
        arguments.put( "x-dead-letter-routing-key","dead-routing-key" );
        Queue persistOriginalDataQueue = new Queue( MqEventManager.QUEUENAME_SYNC,true, false, false,arguments );
        return persistOriginalDataQueue;
    }
    @Bean
    Binding mlBinding(Queue persistOriginalDataQueue , TopicExchange exchange){
        Binding binding = BindingBuilder.bind(persistOriginalDataQueue)
                .to( exchange )
                .with(MqEventManager.ML_ROUTINGKEY_SYNC_COMMON+"#" );
        return binding;
    }

    @Bean
    Binding cmsBinding(Queue persistOriginalDataQueue,TopicExchange exchange){
        Binding binding = BindingBuilder.bind( persistOriginalDataQueue )
                .to( exchange )
                .with( MqEventManager.CMS_ROUTINGKEY_SYNC_COMMON +"#");
        return binding;
    }
    /**
     * 定义延迟交换机
     */
    @Bean
    TopicExchange delayedTopicExchange(){
        TopicExchange topicExchange =new TopicExchange( MqEventManager.EXCHANGE_SYNC_DELAYED );
        topicExchange.setDelayed( true );
        return topicExchange;
    }


    @Bean
    public Binding mlDelayBinding(TopicExchange delayedTopicExchange,Queue persistOriginalDataQueue){
        Binding binding = BindingBuilder.bind( persistOriginalDataQueue )
                .to( delayedTopicExchange )
                .with(  MqEventManager.ML_ROUTINGKEY_SYNC_COMMON+"#");
        return binding;
    }

    @Bean
    public Binding cmsDelayBinding(TopicExchange delayedTopicExchange ,Queue persistOriginalDataQueue){
            Binding binding = BindingBuilder.bind( persistOriginalDataQueue )
                .to( delayedTopicExchange )
                .with( MqEventManager.CMS_ROUTINGKEY_SYNC_COMMON    +"#" );
            return binding;
    }


    /**
     * 定义死信交换机
     */
    @Bean
    public TopicExchange deadLetterExchange(){
        return (TopicExchange) ExchangeBuilder.topicExchange( "dead_exchange" ).durable( true ).build();
    }

    @Bean
    Queue deadQueue(){
        return new Queue( "q.resource.share.dead" );
    }

    @Bean
    public Binding deadQueueBinding(TopicExchange deadLetterExchange,Queue deadQueue){
        Binding binding = BindingBuilder.bind( deadQueue ).to( deadLetterExchange ).with( "dead-routing-key" );
        return binding;
    }


}

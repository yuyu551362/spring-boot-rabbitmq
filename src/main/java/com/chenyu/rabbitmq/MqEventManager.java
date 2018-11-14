package com.chenyu.rabbitmq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

/**
 * @Auther: chenyu
 * @Date: 2018/11/7 14:20
 * @Description:
 */
public class MqEventManager {
    /**
     * 核心交换机
     */
    public static final String EXCHANGE_SYNC                                                = "amq.topic";
    /**
     * 延迟交换机
     */
    public static final String EXCHANGE_SYNC_DELAYED                                        = "delayed.topic";



    /**
     * 同步曲库和cms数据队列
     */
    public static final String QUEUENAME_SYNC                                               = "q.resource.share.aync-resource";

    /**
     * 曲库发送的路由
     */
    public static final String ML_ROUTINGKEY_SYNC_COMMON                                    = "m.tp.songLibrary-data-changed.resource.";
    /**
     * cms发送的路由
     */
    public static final String CMS_ROUTINGKEY_SYNC_COMMON                                   = "m.tp.cms-data-changed.resource.";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory defaultConnectionFactory) {
        return new RabbitAdmin(defaultConnectionFactory);
    }

}

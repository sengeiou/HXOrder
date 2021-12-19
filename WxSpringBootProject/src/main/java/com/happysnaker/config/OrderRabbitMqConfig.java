package com.happysnaker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Happysnaker
 * @description
 * @date 2021/12/12
 * @email happysnaker@foxmail.com
 */
@Configuration
public class OrderRabbitMqConfig {
    /**
     * 添加订单相关
     */
    public static final String ORDER_EXCHANGE = "orderExchange";
    public static final String ORDER_DEAD_EXCHANGE = "orderDeadExchange";

    public static final String ORDER_ADD_QUEUE = "orderAddQueue";
    public static final String ORDER_ADD_ROUTEING_KEY = "orderAddKey";

    public static final String ORDER_ADD_DEAD_QUEUE = "orderAddDeadQueue";
    public static final String ORDER_ADD_DEAD_ROUTEING_KEY = "orderAddDeadKey";

    public static final String ROLL_BACK_STOCK_QUEUE = "rollBackStockQueue";
    public static final String ROLL_BACK_STOCK_ROUTEING_KEY = "rollBackStockKey";


    /**
     * 待支付订单定时取消相关，分别需要正常业务(不工作)和死信配置(尝试取消订单)<br/>
     * 注意，ORDER_PENDING_PAYMENT_QUEUE 将不会有任何消费者，信息投递到该队列上只会静静的等待过期，然后投递到死信队列上
     */
    public static final String ORDER_PENDING_PAYMENT_QUEUE = "orderPendingPaymentQueue";
    public static final String ORDER_CANCEL_QUEUE = "orderCancelQueue";

    public static final String ORDER_PENDING_PAYMENT_ROUTEING_KEY = "orderPendingPaymentKey";
    public static final String ORDER_CANCEL_ROUTEING_KEY = "orderCancelKey";
    /**10分钟订单自动关闭*/
    private static final long AUTOMATIC_CANCELLATION_TIME = 1000 * 60 * 10L;

    @Bean
    public RabbitTemplate myRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setExchange("orderExchange");
        rabbitTemplate.setConfirmCallback((data, ack, cause)-> {
            System.out.println("cause: " + cause + " ack: " + ack + " data: "+ data);
            if (ack) {
                return;
            } else {
                // 订单发送失败，发送消息告知用户
            }
        });
        //设置消息投递失败的策略，有两种策略：自动删除或返回到客户端(true是返回客户端，false是自动删除)
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }



    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
    /**
     * 创建直连交换机
     * @return
     */
    @Bean(name = "getDirectExchange")
    public DirectExchange getDirectExchange() {
        return new DirectExchange(ORDER_EXCHANGE, true, false);
    }


    /**
     * 死信交换机
     * @return
     */
    @Bean(name = "getDirectDeadExchange")
    public DirectExchange getDirectDeadExchange() {
        return new DirectExchange(ORDER_DEAD_EXCHANGE, true, false);
    }

    /**
     * 创建回滚队列
     * @return
     */
    @Bean(name = "getRollBackStockQueue")
    public Queue getRollBackStockQueue() {
        return new Queue(ROLL_BACK_STOCK_QUEUE, true, false, false);
    }

    /**
     * 将回滚队列与交换机绑定路由
     * @param getDirectExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding bindRollBackStockQueue(
            @Qualifier(value = "getDirectExchange") DirectExchange getDirectExchange, @Qualifier(value = "getRollBackStockQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(getDirectExchange).with(ROLL_BACK_STOCK_ROUTEING_KEY);
    }

    /**
     * 创建添加订单队列
     * @return
     */
    @Bean(name = "getOrderAddQueue")
    public Queue getOrderAddQueue() {
        Map<String, Object> args = new HashMap<>(5);
        // 绑定死信交换机和路由
        args.put("x-dead-letter-exchange",ORDER_DEAD_EXCHANGE);
        args.put("x-dead-letter-routing-key",ORDER_ADD_DEAD_ROUTEING_KEY);
        //队列的长度为100
        args.put("x-max-length",100);
        //队列中的消息超过3s后过期，自动放入死信队列
        args.put("x-message-ttl", 3000L);
        return new Queue(ORDER_ADD_QUEUE, true, false, false, args);
    }

    /**
     * 将添加订单队列与交换机路由绑定
     * @param getDirectExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding bindOrderAddQueue(
            @Qualifier(value = "getDirectExchange") DirectExchange getDirectExchange, @Qualifier(value = "getOrderAddQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(getDirectExchange).with(ORDER_ADD_ROUTEING_KEY);
    }

    /**
     * 死新队列配置
     * @return
     */
    @Bean(name = "getOrderAddDeadQueue")
    public Queue getOrderAddDeadQueue() {
        return new Queue(ORDER_ADD_DEAD_QUEUE, true, false, false);
    }

    /**
     * 绑定死信交换机路由
     * @param getDirectExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding bindOrderAddDeadQueue(
            @Qualifier(value = "getDirectDeadExchange") DirectExchange getDirectExchange, @Qualifier(value = "getOrderAddDeadQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(getDirectExchange).with(ORDER_ADD_DEAD_ROUTEING_KEY);
    }

    /**
     * 创建定时取消的业务队列，绑定死信，配置超时时间
     * @return
     */
    @Bean("getOrderPendingPaymentQueue")
    public Queue getOrderPendingPaymentQueue() {
        Map<String, Object> args = new HashMap<>(5);
        // 绑定死信交换机和路由
        args.put("x-dead-letter-exchange",ORDER_DEAD_EXCHANGE);
        args.put("x-dead-letter-routing-key",ORDER_CANCEL_ROUTEING_KEY);
        //队列的长度为100
        args.put("x-max-length",100);
        //队列中的消息超过10s后过期，自动放入死信队列
        args.put("x-message-ttl", AUTOMATIC_CANCELLATION_TIME);
        return new Queue(ORDER_PENDING_PAYMENT_QUEUE, true, false, false, args);
    }


    /**
     * 将定时取消的业务队列与交换机路由绑定
     * @param getDirectExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding bindOrderPendingPaymentQueue(
            @Qualifier(value = "getDirectExchange") DirectExchange getDirectExchange, @Qualifier(value = "getOrderPendingPaymentQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(getDirectExchange).with(ORDER_PENDING_PAYMENT_ROUTEING_KEY);
    }



    /**
     * 超时，取消订单死新队列配置
     * @return
     */
    @Bean(name = "getOrderCancelQueue")
    public Queue getOrderCancelQueue() {
        return new Queue(ORDER_CANCEL_QUEUE, true, false, false);
    }

    /**
     * 绑定死信交换机路由
     * @param getDirectExchange
     * @param queue
     * @return
     */
    @Bean
    public Binding bindOrderCancelQueue(
            @Qualifier(value = "getDirectDeadExchange") DirectExchange getDirectExchange, @Qualifier(value = "getOrderCancelQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(getDirectExchange).with(ORDER_CANCEL_ROUTEING_KEY);
    }

}

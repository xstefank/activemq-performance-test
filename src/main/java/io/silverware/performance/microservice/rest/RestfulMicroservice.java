package io.silverware.performance.microservice.rest;

import io.silverware.microservices.annotations.Gateway;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;
import io.silverware.performance.jms.JMSStandard;
import io.silverware.performance.microservice.ActiveMQMicroservice;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 * @author <a href="mailto:stefankomartin6@gmail.com">Martin Štefanko</a>
 */
@Gateway
@Microservice
public class RestfulMicroservice {

   public static final int MESSAGE_COUNT = 60000;

   @Inject
   @MicroserviceReference("producerConncetion")
   private Connection producerConnection;

   @Inject
   @MicroserviceReference
   private ActiveMQMicroservice activeMQMicroservice;

   private JMSStandard jmsStandard = new JMSStandard();

   public void observer(@Observes MicroservicesStartedEvent event) throws JMSException, NamingException {
      //fill up the queue with test data
      Session session = producerConnection.createSession();

      Queue queue = session.createQueue("testQueue");

      MessageProducer messageProducer = session.createProducer(queue);

      for (int i = 1; i <= MESSAGE_COUNT; i++) {
         TextMessage message = session.createTextMessage("Hello message " + i + ", sent on " + new Date());
         System.out.println("Sending message: " + message.getText());
         messageProducer.send(message);
      }

      messageProducer.close();
      session.close();
   }

   public String getMessageMS() throws JMSException {
      return activeMQMicroservice.getMessage();
   }

   public String getMessageStandard() throws NamingException, JMSException {
      return jmsStandard.getMessage();
   }
}

package io.silverware.performance.microservice;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @author <a href="mailto:stefankomartin6@gmail.com">Martin Štefanko</a>
 */
@Microservice
public class ActiveMQMicroservice {

   @Inject
   @MicroserviceReference("jmsConnection11")
   private Connection connection;

   public String getMessage() throws JMSException {
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      Queue queue = session.createQueue("testQueue");

      MessageConsumer messageConsumer = session.createConsumer(queue);


      TextMessage message = (TextMessage) messageConsumer.receive(1000);
      String text = message.getText();

      messageConsumer.close();
      session.close();

      return text;
   }
}

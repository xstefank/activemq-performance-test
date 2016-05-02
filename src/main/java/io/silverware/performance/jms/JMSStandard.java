package io.silverware.performance.jms;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.utils.ActiveMQConstants;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author <a href="mailto:stefankomartin6@gmail.com">Martin Štefanko</a>
 */
public class JMSStandard {

   private Connection connection;


   public Connection getConnection() throws JMSException, NamingException {
      if (connection == null) {
         //lazy init connection
         InitialContext initialContext = new InitialContext();

         ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(ActiveMQConstants.CONNECTION_FACTORY_JNDI);

         connection = connectionFactory.createConnection();
         connection.start();
      }

      return connection;
   }

   public String getMessage() throws JMSException, NamingException {
      Session session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);

      Queue queue = session.createQueue("testQueue");

      MessageConsumer messageConsumer = session.createConsumer(queue);

      TextMessage message = (TextMessage) messageConsumer.receive(1000);
      String text = message.getText();

      messageConsumer.close();
      session.close();

      return text;
   }
}

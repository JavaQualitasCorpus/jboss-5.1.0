/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.jmsra.test;
import javax.jms.Connection;
import javax.jms.Message;

import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Queue;
import javax.naming.Context;

import javax.management.ObjectName;

import javax.naming.InitialContext;
import junit.framework.Assert;

import junit.framework.Test;

import org.jboss.test.JBossTestCase;
import org.jboss.test.JBossTestSetup;

import org.jboss.test.jmsra.bean.*;

/**
 * 
 * <p>Test sync receive.
 *
 * <p>Created: Sat Sep 22 13:31:54 2001.
 *
 * @author <a href="mailto:peter.antman@tim.se">Peter Antman</a>
 * @version $Revision: 81036 $
 */

public class RaSyncRecUnitTestCase extends JBossTestCase {
   private final static String BEAN_JNDI = "QueueRec";
   private final static String QUEUE_FACTORY = "ConnectionFactory";
   private final static String QUEUE = "queue/A";
   private final static int MESSAGE_NR = 10;
   
   /**
    * JMS connection
    */
   protected QueueConnection connection;
   /**
    * JMS session
    */
   protected QueueSession session;
   /**
    * JMS sender
    */
   protected QueueSender sender;

   /**
    * Receiving bean
    */ 
   protected QueueRec rec;

   /**
    *   
    * Constructor for the RaSyncRecUnitTestCase object
    *
    * @param name           Description of Parameter
    * @exception Exception  Description of Exception
    */
   public RaSyncRecUnitTestCase(String name) {
      super(name);
   }
   
   /**
    * The JUnit setup method
    *
    * @exception Exception  Description of Exception
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      // Create a receiver
      Context context = getInitialContext();
      QueueRecHome home = (QueueRecHome)context.lookup(BEAN_JNDI);
      rec = home.create();

      init(context);

      // start up the session
      connection.start();

   }

   /**
    * #Description of the Method
    *
    * @param context        Description of Parameter
    * @exception Exception  Description of Exception
    */
   protected void init(final Context context) throws Exception
   {
      QueueConnectionFactory factory =
	 (QueueConnectionFactory)context.lookup(QUEUE_FACTORY);
      
      connection = factory.createQueueConnection();
      
      session = ((QueueConnection)connection).createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      
      Queue queue = (Queue)context.lookup(QUEUE);
      
      sender = ((QueueSession)session).createSender(queue);
   }

   /**
    * The teardown method for JUnit
    *
    * @exception Exception  Description of Exception
    */
   protected void tearDown() throws Exception
   {
      if (sender != null)
      {
         sender.close();
      }
      if (session != null) { 
	 session.close();
      }
      if (connection != null)
      {
         connection.close();
      }
   }

   /**
    * Test sync receive of message with jms ra.
    */
   public void testSyncRec() throws Exception {
      // Send a message to queue
      TextMessage message = session.createTextMessage();
      message.setText(String.valueOf(MESSAGE_NR));
      message.setIntProperty(Publisher.JMS_MESSAGE_NR, MESSAGE_NR);
      sender.send(message);
      getLog().debug("sent message with nr = " + MESSAGE_NR);

      // Let bean fetch it sync
      int res = rec.getMessage();
      Assert.assertEquals(MESSAGE_NR, res);
      getLog().debug("testSyncRec() OK");
   }

   public static Test suite() throws Exception
   {
      return new JBossTestSetup(getDeploySetup(RaSyncRecUnitTestCase.class, "jmsra.jar"))
         {
            protected void setUp() throws Exception
            {
               super.setUp();
               ClassLoader loader = Thread.currentThread().getContextClassLoader();
               deploy (loader.getResource("messaging/test-destinations-full-service.xml").toString());
            }

             protected void tearDown() throws Exception
             {
                super.tearDown();

                // Remove the messages
                getServer().invoke
                (
                   new ObjectName("jboss.mq.destination:service=Queue,name=testQueue"),
                   "removeAllMessages",
                   new Object[0],
                   new String[0]
                );
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                undeploy (loader.getResource("messaging/test-destinations-full-service.xml").toString());
             }
          };
   }
} // RaSyncRecUnitTestCase






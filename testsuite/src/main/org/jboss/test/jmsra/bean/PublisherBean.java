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
package org.jboss.test.jmsra.bean;

import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.jboss.logging.Logger;

/**
 * Bean to help JMS RA test publish/send JMS messages and test transactional
 * behavior.
 *
 * <p>Created: Mon Apr 23 21:35:25 2001
 *
 * @author  Unknown
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @version $Revision: 81036 $
 */
public class PublisherBean
    implements SessionBean
{
    private static final String CONNECTION_JNDI =
        "java:comp/env/jms/MyQueueConnection";
    
    private static final String QUEUE_JNDI =
        "java:comp/env/jms/QueueName";

    private static final String BEAN_JNDI =
        "java:comp/env/ejb/PublisherCMP";

    private final Logger log = Logger.getLogger(this.getClass());
   
    private SessionContext ctx; // = null;
    private Queue queue; // = null;
    private QueueConnection queueConnection; // = null;
    
    public PublisherBean() {
        // empty
    }

    public void setSessionContext(final SessionContext ctx) {
        this.ctx = ctx;
    }

    public void ejbCreate() {
        try {
            Context context = new InitialContext();
            queue = (Queue)context.lookup(QUEUE_JNDI);

            QueueConnectionFactory factory =
                (QueueConnectionFactory)context.lookup(CONNECTION_JNDI);
            queueConnection = factory.createQueueConnection();
        }
        catch (Exception e) {
            // JMSException or NamingException could be thrown
            log.error("failed to create bean", e);
            throw new EJBException(e);
        }
    }

    /**
     * Send a message with a message nr in property MESSAGE_NR
     */
    public void simple(int messageNr) {
        log.info("sending message");
        sendMessage(messageNr);
        log.info("sent");
    }
    
    /**
     * Try send a message with a message nr in property MESSAGE_NR,
     * but set rollback only
     */
    public void simpleFail(int messageNr) {
        log.info("sending message");
        sendMessage(messageNr);
        log.info("sent");
        
        // Roll it back, no message should be sent if transactins work
        log.info("Setting rollbackOnly");
        ctx.setRollbackOnly();
        log.info("rollback set: " + ctx.getRollbackOnly());
    }

    public void beanOk(int messageNr) {
        log.info("sending message");        
        // DO JMS - First transaction
        sendMessage(messageNr);
        log.info("sent");
        
        PublisherCMPHome h = null;
        try {
            // DO entity bean - Second transaction
            h = (PublisherCMPHome) new InitialContext().lookup(BEAN_JNDI);
            PublisherCMP b = h.create(new Integer(messageNr));
            log.info("calling bean");                        
            b.ok(messageNr);
            log.info("called bean");
        }
        catch (Exception e) {
            log.error("failed to contact 3rdparty bean", e);
            throw new EJBException(e);
        }
        finally {
            try {
                h.remove(new Integer(messageNr));
            }
            catch (Exception e) {
                log.error("failed to remove 3rdparty bean", e);
            }
            finally {
                log.info("done");
            }
        }
    }

    public void beanError(int messageNr) {
        log.info("sending message");                
        // DO JMS - First transaction
        sendMessage(messageNr);
        log.info("sent");
        
        PublisherCMPHome h = null;
        try {
            // DO entity bean - Second transaction
            h = (PublisherCMPHome) new InitialContext().lookup(BEAN_JNDI);
            PublisherCMP b = h.create(new Integer(messageNr));
            log.info("calling bean");            
            b.error(messageNr);
            log.info("bean called (should never get here)");
        }
        catch (Exception e) {
            log.info("caught exception (as expected)");
            throw new EJBException("Exception in erro: " + e);
        }
        finally {
            try {
                h.remove(new Integer(messageNr));
            }
            catch (Exception e) {
                log.error("failed to remove 3rdparty bean", e);
            }
            finally {
                log.info("done");
            }
        }
    }

    public void ejbRemove() throws RemoteException {
        if (queueConnection != null) {
            try {
                queueConnection.close();
            }
            catch (Exception e) {
                log.error("failed to close connection", e);
            }
        }
    }

    public void ejbActivate() {}
    public void ejbPassivate() {}

    private void sendMessage(int messageNr) {
        log.info("sending message wtih nr: " + messageNr);
        QueueSession queueSession = null;
        try {
            QueueSender queueSender = null;
            TextMessage message = null;
            queueSession =
                queueConnection.createQueueSession(true,
                                                   Session.AUTO_ACKNOWLEDGE);
            queueSender = queueSession.createSender(queue);
    
            message = queueSession.createTextMessage();
            message.setText(String.valueOf(messageNr));
            message.setIntProperty(Publisher.JMS_MESSAGE_NR, messageNr);
            queueSender.send(message);
            log.info("sent message with nr = " + messageNr);
        }
        catch (JMSException e) {
            log.debug("failed to send", e);
            ctx.setRollbackOnly();
            throw new EJBException(e);
        }
        finally {
            if (queueSession != null) {
                try {
                    queueSession.close();
                }
                catch (Exception e) {
                    log.error("failed to close session", e);
                }
                finally {
                    log.info("done sending message");
                }
            }
        }
    }

}

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
package org.jboss.test.security.ejb.jbas1852;

import java.security.Principal;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.SessionBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Scott.Stark@jboss.org
 * @version $Revision: 81036 $
 */
public class PublicSessionFacade implements SessionBean
{
   private SessionContext sessionContext;

   public void ejbCreate() throws CreateException
   {
       System.out.println("PublicSessionBean.ejbCreate() called");
   }

   public void ejbActivate()
   {
       System.out.println("PublicSessionBean.ejbActivate() called");
   }

   public void ejbPassivate()
   {
       System.out.println("PublicSessionBean.ejbPassivate() called");
   }

   public void ejbRemove()
   {
       System.out.println("PublicSessionBean.ejbRemove() called");
   }

   public void setSessionContext(SessionContext context)
   {
       sessionContext = context;
   }

   public String callEcho(String arg)
      throws RemoteException
   {
      Principal user = sessionContext.getCallerPrincipal();
      String echoMsg = null;
      try
      {
         InitialContext ctx = new InitialContext();
         String jndiName = "java:comp/env/ejb/TargetEJB";
         SessionHome home = (SessionHome) ctx.lookup(jndiName);
         Session bean = home.create();
         echoMsg = bean.echo("Hello, arg="+arg);
         echoMsg = bean.echo("Hello 2, arg="+arg);
      }
      catch (NamingException e)
      {
         throw new RemoteException("callEcho failed", e);
      }
      catch (CreateException e)
      {
         throw new RemoteException("callEcho failed", e);
      }
      return echoMsg;
   }
}

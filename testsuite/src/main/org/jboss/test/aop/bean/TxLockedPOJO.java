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
package org.jboss.test.aop.bean;

/**
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 81036 $
 */
public class TxLockedPOJO
{

   public TxLockedPOJO()
   {
      field = 0;
   }

   private int field;

   public int getField() { return field; }
   public void setField(int val) 
   { 
      if (val == 6 && field == 0)
      {
         throw new RuntimeException("TxLock didn't work.  2nd thread got there first.");
      }
      if (val == 5)
      {
         // Sleep to test the lock
         try { Thread.sleep(5000); } catch (Exception ignored) { }
      }
      field = val;
   }

}


/*
 * Copyright (c) 2003,  Intracom S.A. - www.intracom.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * This package and its source code is available at www.jboss.org
**/
package org.jboss.jmx.adaptor.snmp.agent;

/**
 * @version $Revision: 44604 $
 *
 * @author  <a href="mailto:spol@intracom.gr">Spyros Pollatos</a>
 * @author  <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 */
public class BadSnmpVersionException extends Exception
{
   /**
    * Constructs a BadSnmpVersionException with null as its error
    * detail message.
    */
   public BadSnmpVersionException()
   {
      // empty
   }
    
   /**
    * Constructs a BadSnmpVersionException with the specified detail message.
    * The error message can be retrieved by the Throwable.getMessage().
    *
    * @param s the detail message
    */
   public BadSnmpVersionException(String s)
   {
      super(s);
   }
   
}


/*
 * Generated by XDoclet - Do not edit!
 */
package org.jboss.test.cmp2.optimisticlock.interfaces;

/**
 * Home interface for Facade.
 */
public interface FacadeHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Facade";
   public static final String JNDI_NAME="FacadeBean";

   public org.jboss.test.cmp2.optimisticlock.interfaces.Facade create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}

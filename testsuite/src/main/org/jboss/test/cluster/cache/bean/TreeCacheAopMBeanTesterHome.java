/*
 * Generated by XDoclet - Do not edit!
 */
package org.jboss.test.cluster.cache.bean;

/**
 * Home interface for test/TreeCacheAopMBeanTester.
 */
public interface TreeCacheAopMBeanTesterHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/test/TreeCacheAopMBeanTester";
   public static final String JNDI_NAME="ejb/test/TreeCacheAopMBeanTester";

   public TreeCacheAopMBeanTester create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}

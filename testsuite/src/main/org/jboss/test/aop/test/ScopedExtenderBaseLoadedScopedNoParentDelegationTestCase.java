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
package org.jboss.test.aop.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * @author <a href="stalep@conduct.no">Stale W. Pedersen</a>
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision:
 */
public class ScopedExtenderBaseLoadedScopedNoParentDelegationTestCase extends ScopedExtenderBaseTest
{

   public ScopedExtenderBaseLoadedScopedNoParentDelegationTestCase(String name)
   {
      super(name);
   }
   
   
   public static Test suite() throws Exception
   {
      TestSuite suite = new TestSuite();
      suite.addTest(new TestSuite(ScopedExtenderBaseLoadedScopedNoParentDelegationTestCase.class));

      AOPClassLoaderHookTestSetup setup = new AOPClassLoaderHookTestSetup(suite, "aop-scopedextender-base-loaded.sar,aop-scopedextender-child-noparentdelegation.sar");
      return setup;
   }

   protected String getExpectedBaseName()
   {
      return "BaseLoadedTester";
   }

   protected String getExpectedChildName()
   {
      return "ScopedChildNoParentDelegationTester";
   }

}

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

package org.jboss.ha.framework.server.deployers;

import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.metadata.ejb.jboss.JBossMetaData;

/**
 * @author Brian Stansberry
 *
 */
public class Ejb3HAPartitionDependencyDeployer extends AbstractHAPartitionDependencyDeployer
{

   /**
    * Create a new Ejb3HAPartitionDependencyDeployer.
    * 
    */
   public Ejb3HAPartitionDependencyDeployer()
   {
      super();
   }

   @Override
   protected boolean accepts(JBossMetaData metaData)
   {      
      return metaData.isEJB3x();
   }

   /**
    * Overrides the superclass to run in PRE_REAL as the merge of annotation
    * metadata and xml metadata only occurs in POST_CLASSLOADER.
    */
   @Override
   protected void configureDeploymentStage()
   {
      setStage(DeploymentStages.PRE_REAL);
   }
   
}

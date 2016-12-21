/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.extension.jacoco.client;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.extension.jacoco.CoverageDataCommand;
import org.jboss.arquillian.extension.jacoco.client.configuration.JaCoCoConfiguration;
import org.jboss.arquillian.extension.jacoco.container.JacocoRemoteExtension;
import org.jboss.arquillian.extension.jacoco.container.StartCoverageData;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * JaCoCoArchiveAppender
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class JaCoCoArchiveAppender implements AuxiliaryArchiveAppender
{

   @Inject
   private Instance<JaCoCoConfiguration> config;

   // Test only...
   public void setConfig(Instance<JaCoCoConfiguration> config) {
      this.config = config;
   }

   @Override
   public Archive<?> createAuxiliaryArchive()
   {
      final JavaArchive ret = ShrinkWrap.create(JavaArchive.class, "arquillian-jacoco.jar")
                  .addPackages(
                        true, 
                        org.jacoco.core.JaCoCo.class.getPackage(),
                        StartCoverageData.class.getPackage())
                  .addPackage(CoverageDataCommand.class.getPackage())
                  .addAsServiceProvider(
                        RemoteLoadableExtension.class, 
                        JacocoRemoteExtension.class);

      if (config.get().isAppendAsmLibrary()) {
          ret.addPackages(true, org.objectweb.asm.ClassReader.class.getPackage());
      }

      return ret;
   }
}

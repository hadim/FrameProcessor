package org.micromanager.plugins.frameaverager;

import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorFactory;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;

public class FrameAveragerFactory implements ProcessorFactory {
    
   private final Studio studio_;
   
   public FrameAveragerFactory(Studio studio, PropertyMap settings) {
      studio_ = studio;
   }

   @Override
   public Processor createProcessor() {
      return new FrameAveragerProcessor(studio_);
   }
}

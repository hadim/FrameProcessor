package org.micromanager.plugins.frameaverager;

import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorFactory;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;

public class FrameAveragerFactory implements ProcessorFactory {
    
   private final Studio studio_;
   private PropertyMap settings_;
   
   public FrameAveragerFactory(Studio studio, PropertyMap settings) {
      studio_ = studio;
      settings_ = settings;
   }

   @Override
   public Processor createProcessor() {
      return new FrameAveragerProcessor(studio_,
            settings_.getInt("numerOfImagesToAverage", 10),
            settings_.getBoolean("enableDuringAcquisition", true),
            settings_.getBoolean("enableDuringLive", true));
   }
}

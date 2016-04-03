package org.micromanager.plugins.frameaverager;

import org.micromanager.LogManager;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorFactory;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;

public class FrameAveragerFactory implements ProcessorFactory {

   private final Studio studio_;
   private PropertyMap settings_;
   private final LogManager log_;

   public FrameAveragerFactory(Studio studio, PropertyMap settings) {
      studio_ = studio;
      settings_ = settings;
      log_ = studio_.logs();
   }

   @Override
   public Processor createProcessor() {
      log_.logMessage("FrameAverager : Create FrameAveragerProcessor");
      return new FrameAveragerProcessor(studio_,
            settings_.getString("processorAlgo", FrameAveragerPlugin.PROCESSOR_ALGO_MEAN),
            settings_.getInt("numerOfImagesToAverage", 10),
            settings_.getBoolean("enableDuringAcquisition", true),
            settings_.getBoolean("enableDuringLive", true));
   }
}

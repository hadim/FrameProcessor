package org.micromanager.plugins.frameaverager;

import org.micromanager.Studio;
import org.micromanager.data.Image;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorContext;

public class FrameAveragerProcessor extends Processor {

   private final Studio studio_;

   public FrameAveragerProcessor(Studio studio) {
      studio_ = studio;
   }

    @Override
    public void processImage(Image image, ProcessorContext pc) {
    }
}

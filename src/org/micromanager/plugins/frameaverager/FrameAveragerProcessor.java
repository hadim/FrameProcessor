package org.micromanager.plugins.frameaverager;

import ij.process.ImageProcessor;
import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.data.Image;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorContext;
import org.micromanager.data.SummaryMetadata;

public class FrameAveragerProcessor extends Processor {

   private final Studio studio_;
   private final LogManager log_;
   
   private final int numerOfImagesToAverage_;
   private final boolean enableDuringAcquisition_;
   private final boolean enableDuringLive_;

    public FrameAveragerProcessor(Studio studio, int numerOfImagesToAverage,
            boolean enableDuringAcquisition, boolean enableDuringLive) {
        
        studio_ = studio;
        log_ = studio_.logs();

        numerOfImagesToAverage_ = numerOfImagesToAverage;
        enableDuringAcquisition_ = enableDuringAcquisition;
        enableDuringLive_ = enableDuringLive;
    }

    @Override
    public void processImage(Image image, ProcessorContext context) {
        
        if (studio_.acquisitions().isAcquisitionRunning() && !enableDuringAcquisition_) {
            context.outputImage(image);
            return;
        }
        
        if (studio_.live().getIsLiveModeOn() && !enableDuringLive_) {
            context.outputImage(image);
            return;
        }
        
        log_.logMessage("processImage");
        context.outputImage(image);
    }
    
   @Override
    public SummaryMetadata processSummaryMetadata(SummaryMetadata summary) {
        log_.logMessage("processSummaryMetadata");
        return summary.copy().build();
    }
}

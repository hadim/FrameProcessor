package org.micromanager.plugins.frameaverager;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.data.Datastore;
import org.micromanager.data.DatastoreFrozenException;
import org.micromanager.data.DatastoreRewriteException;
import org.micromanager.data.Image;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorContext;
import org.micromanager.data.SummaryMetadata;

public class FrameAveragerProcessor extends Processor {

    private final Studio studio_;
    private final LogManager log_;
    private Datastore store_;

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
        
        if (isProcessorEnable()) {
            log_.logMessage("Create Datastore");
            store_ = studio_.data().createRAMDatastore();
            studio_.displays().manage(store_);
            
            log_.logMessage("Display Datastore");
            studio_.displays().createDisplay(store_);
        }
        
    }

    @Override
    public void processImage(Image image, ProcessorContext context) {
        
        if (!isProcessorEnable()) {
            context.outputImage(image);
            return;
        }
        
        int timeIndex = image.getCoords().getTime();
        try {
            store_.putImage(image);
        } catch (DatastoreFrozenException ex) {
            Logger.getLogger(FrameAveragerProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DatastoreRewriteException ex) {
            Logger.getLogger(FrameAveragerProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(FrameAveragerProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        context.outputImage(image);
    }
    
   @Override
    public SummaryMetadata processSummaryMetadata(SummaryMetadata summary) {
        return summary.copy().build();
    }
    
    public final boolean isProcessorEnable(){
        if (studio_.acquisitions().isAcquisitionRunning() && !enableDuringAcquisition_) {
            return false;
        }
        else if (studio_.live().getIsLiveModeOn() && !enableDuringLive_) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void cleanup(ProcessorContext context) {
      store_.freeze();
      // store_.setSavePath(savePath_);
    }
}

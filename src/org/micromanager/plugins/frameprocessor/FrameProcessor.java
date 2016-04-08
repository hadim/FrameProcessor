package org.micromanager.plugins.frameprocessor;

import java.util.HashMap;
import java.util.Map;

import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.Image;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorContext;
import org.micromanager.data.SummaryMetadata;

public class FrameProcessor extends Processor {

   private final Studio studio_;
   private final LogManager log_;

   private final String processorAlgo_;
   private final int numerOfImagesToProcess_;
   private final boolean enableDuringAcquisition_;
   private final boolean enableDuringLive_;
   
   private int newIntendedTime;
   
   private HashMap<Coords, SingleAcqProcessor> singleAquisitions;

   public FrameProcessor(Studio studio, String processorAlgo,
           int numerOfImagesToProcess, boolean enableDuringAcquisition,
           boolean enableDuringLive) {

      studio_ = studio;
      log_ = studio_.logs();

      processorAlgo_ = processorAlgo;
      numerOfImagesToProcess_ = numerOfImagesToProcess;
      enableDuringAcquisition_ = enableDuringAcquisition;
      enableDuringLive_ = enableDuringLive;

      log_.logMessage("FrameProcessor : Algorithm applied on stack image is " + processorAlgo_);
      log_.logMessage("FrameProcessor : Number of frames to process " + Integer.toString(numerOfImagesToProcess));
      
      // Initialize a hashmap of all combinations of the different acquisitions
      // Each index will be a combination of Z, Channel and StagePosition
      singleAquisitions = new HashMap();

   }

   @Override
   public void processImage(Image image, ProcessorContext context) {

      if (!isProcessorEnable()) {
         context.outputImage(image);
         return;
      }
      
      // Get coords without time (set it to 0)
      Coords coords = image.getCoords().copy().time(0).build();
      
      // If this coordinates index does not exist in singleAquisitions hasmap, create it
      SingleAcqProcessor singleAcquProc;
      if (!singleAquisitions.containsKey(coords)) {
         singleAcquProc = new SingleAcqProcessor(coords, studio_,
                 processorAlgo_, numerOfImagesToProcess_);
         singleAquisitions.put(coords, singleAcquProc);
      } else{
         singleAcquProc = singleAquisitions.get(coords);
      }

      // This method will output the processed image if needed
      singleAcquProc.addImage(image, context);
   }

   @Override
   public SummaryMetadata processSummaryMetadata(SummaryMetadata summary) {

      if (studio_.acquisitions().isAcquisitionRunning()) {
         
         // Calculate new number of times
         newIntendedTime = (int) (summary.getIntendedDimensions().getTime() / numerOfImagesToProcess_);
         
         Coords.CoordsBuilder coordsBuilder = summary.getIntendedDimensions().copy();
         SummaryMetadata.SummaryMetadataBuilder builder = summary.copy();
         builder.intendedDimensions(coordsBuilder.time(newIntendedTime).build());

         return builder.build();
      } else {
         return summary;
      }
   }

   public final boolean isProcessorEnable() {
      if (studio_.acquisitions().isAcquisitionRunning() && !enableDuringAcquisition_) {
         return false;
      } else if (studio_.live().getIsLiveModeOn() && !enableDuringLive_) {
         return false;
      }

      return true;
   }

   @Override
   public void cleanup(ProcessorContext context) {

      for (Map.Entry<Coords, SingleAcqProcessor> entry : singleAquisitions.entrySet())
      {
         entry.getValue().clear();
         singleAquisitions.put(entry.getKey(), null);
      }
      singleAquisitions = null;

   }

}

package org.micromanager.plugins.frameaverager;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.Image;
import org.micromanager.data.Metadata;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorContext;
import org.micromanager.data.SummaryMetadata;

public class FrameAveragerProcessor extends Processor {

    private final Studio studio_;
    private final LogManager log_;

    private final String processorAlgo_;
    private final int numerOfImagesToAverage_;
    private final boolean enableDuringAcquisition_;
    private final boolean enableDuringLive_;
    
    private int current_frame_index;
    private Image[] bufferImages;
    private int currentBufferIndex;
    private Image processedImage;
   
    public FrameAveragerProcessor(Studio studio, String processorAlgo, 
            int numerOfImagesToAverage, boolean enableDuringAcquisition,
            boolean enableDuringLive) {
        
        studio_ = studio;
        log_ = studio_.logs();

        processorAlgo_ = processorAlgo;
        numerOfImagesToAverage_ = numerOfImagesToAverage;
        enableDuringAcquisition_ = enableDuringAcquisition;
        enableDuringLive_ = enableDuringLive;
        
        current_frame_index = 0;
        bufferImages = new Image[numerOfImagesToAverage_];
        
        for (int i=0; i < numerOfImagesToAverage_; i++){
            bufferImages[i] = null;
        }
        
        processedImage = null;
        
        log_.logMessage("FrameAverager : Algorithm applied on stack image is " + processorAlgo_);
        
    }

    @Override
    public void processImage(Image image, ProcessorContext context) {
        
        if (!isProcessorEnable()) {
            context.outputImage(image);
            return;
        }

        currentBufferIndex = current_frame_index % numerOfImagesToAverage_;

        if (currentBufferIndex == 0 && current_frame_index != 0){
                        
            try {
                // Process last `numerOfImagesToAverage_` images
                processBufferImages();
            } catch (Exception ex) {
                Logger.getLogger(FrameAveragerProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Output processed image
            context.outputImage(processedImage);
        }

        bufferImages[currentBufferIndex] = image;
        current_frame_index += 1;
    }
    
   @Override
    public SummaryMetadata processSummaryMetadata(SummaryMetadata summary) {
        log_.logMessage("jjjjj");
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
        
        current_frame_index = 0;

        bufferImages = new Image[numerOfImagesToAverage_];
        for (int i=0; i < numerOfImagesToAverage_; i++){
              bufferImages[i] = null;
          }

    }
    
    public void processBufferImages() throws Exception{
        
        if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_MEAN)){
            meanProcessImages();
        }
        else if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_MEDIAN)){
            throw new Exception("FrameAverager : Algorithm called " + processorAlgo_ + " is not implemented or not found.");
            //medianProcessImages();
        }
        else if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_SUM)){
            throw new Exception("FrameAverager : Algorithm called " + processorAlgo_ + " is not implemented or not found.");
            //sumProcessImages();
        }
        else if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_MAX)){
            throw new Exception("FrameAverager : Algorithm called " + processorAlgo_ + " is not implemented or not found.");
            //maxProcessImages();
        }
        else if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_MIN)){
            throw new Exception("FrameAverager : Algorithm called " + processorAlgo_ + " is not implemented or not found.");
            //minProcessImages();
        }
        else{
            throw new Exception("FrameAverager : Algorithm called " + processorAlgo_ + " is not implemented or not found.");
        }
        
    }
    
    public void meanProcessImages(){
        
        // Could be moved outside processImage() ?
        Image img = bufferImages[0];
        int bitDepth = img.getMetadata().getBitDepth();
        int width = img.getWidth();
        int height = img.getHeight();
        int bytesPerPixel = img.getBytesPerPixel();
        int numComponents = img.getNumComponents();
        Coords coords = img.getCoords();
        Metadata metadata = img.getMetadata();

        if (bytesPerPixel == 1) {
            
            // Create new array
            byte[] newPixels = new byte[width * height];
            
            // Init the new array
            for(int i=0; i < newPixels.length; i++)
                newPixels[i] = 0;
            
            // Sum up all pixels from bufferImages
            for(int i=0; i < numerOfImagesToAverage_; i++){
                
                // Get current frame pixels
                img = bufferImages[i];
                byte[] imgPixels = (byte[]) img.getRawPixels();
                
                // Iterate over all pixels
                for (int index = 0; index < newPixels.length; index++)
                    newPixels[index] = (byte) (float) (newPixels[index] + (int) (imgPixels[index] & 0xff));  
            }
            
            // Divide by length to get the mean
            for (int index = 0; index < newPixels.length; index++)
                newPixels[index] = (byte) (int) (newPixels[index] / numerOfImagesToAverage_);
            
            // Create the processed image
            processedImage = studio_.data().createImage(newPixels, width, height,
                    bytesPerPixel, numComponents, coords, metadata);
            
        }
        else if (bytesPerPixel == 2) {
            
            // Create new array
            short[] newPixels = new short[width * height];
            
            // Init the new array
            for(int i=0; i < newPixels.length; i++)
                newPixels[i] = 0;
            
            // Sum up all pixels from bufferImages
            for(int i=0; i < numerOfImagesToAverage_; i++){
                
                // Get current frame pixels
                img = bufferImages[i];
                short[] imgPixels = (short[]) img.getRawPixels();
                
                // Iterate over all pixels
                for (int index = 0; index < newPixels.length; index++)
                    newPixels[index] = (short) (float) (newPixels[index] + (int) (imgPixels[index] & 0xff));  
            }
            
            // Divide by length to get the mean
            for (int index = 0; index < newPixels.length; index++)
                newPixels[index] = (short) (int) (newPixels[index] / numerOfImagesToAverage_);
            
            // Create the processed image
            processedImage = studio_.data().createImage(newPixels, width, height,
                    bytesPerPixel, numComponents, coords, metadata);
            
        }
      }
    
    public void medianProcessImages(){
        // TODO
        processedImage =  bufferImages[0];
    }
    
    public void sumProcessImages(){
        // TODO
        processedImage =  bufferImages[0];
    }
    
    public void maxProcessImages(){
        // TODO
        processedImage =  bufferImages[0];
    }
    
     public void minProcessImages(){
        // TODO
        processedImage =  bufferImages[0];
    }

}

package org.micromanager.plugins.frameaverager;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.micromanager.LogManager;
import org.micromanager.PropertyMap;
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
        log_.logMessage("FrameAverager : Number of frames to process " + Integer.toString(numerOfImagesToAverage_));
        
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
            
            // Clean buffered images
            for (int i=0; i < numerOfImagesToAverage_; i++){
              bufferImages[i] = null;
            }

            // Add metadata to the processed image
            Metadata metadata = processedImage.getMetadata();
            PropertyMap userData = metadata.getUserData();
            userData = userData.copy().putBoolean("FrameProcessed", true).build();
            userData = userData.copy().putString("FrameProcessed-Operation", processorAlgo_).build();
            userData = userData.copy().putInt("FrameProcessed-StackNumber", numerOfImagesToAverage_).build();
            metadata = metadata.copy().userData(userData).build();
            processedImage = processedImage.copyWithMetadata(metadata);
            
            // Output processed image
            context.outputImage(processedImage);
            
            // Clean processed image
            processedImage = null;
        }

        bufferImages[currentBufferIndex] = image;
        current_frame_index += 1;
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
        
        current_frame_index = 0;

        bufferImages = new Image[numerOfImagesToAverage_];
        for (int i=0; i < numerOfImagesToAverage_; i++){
              bufferImages[i] = null;
        }

    }
    
    public void processBufferImages() throws Exception{
        
        if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_MEAN)){
            meanProcessImages(false);
        }
//        else if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_MEDIAN)){
//            throw new Exception("FrameAverager : Algorithm called " + processorAlgo_ + " is not implemented or not found.");
//            //medianProcessImages();
//        }
        else if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_SUM)){
            meanProcessImages(true);
        }
        else if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_MAX)){
            extremaProcessImages("max");
        }
        else if (processorAlgo_.equals(FrameAveragerPlugin.PROCESSOR_ALGO_MIN)){
            extremaProcessImages("min");
        }
        else{
            throw new Exception("FrameAverager : Algorithm called " + processorAlgo_ + " is not implemented or not found.");
        }
        
    }
    
    public void meanProcessImages(boolean onlySum){
        
        // Could be moved outside processImage() ?
        Image img = bufferImages[0];
        int bitDepth = img.getMetadata().getBitDepth();
        int width = img.getWidth();
        int height = img.getHeight();
        int bytesPerPixel = img.getBytesPerPixel();
        int numComponents = img.getNumComponents();
        Coords coords = img.getCoords();
        Metadata metadata = img.getMetadata();

        Object resultPixels = null;
        
        if (bytesPerPixel == 1) {
            
            // Create new array
            float[] newPixels = new float[width * height];
            byte[] newPixelsFinal = new byte[width * height];
            
            // Sum up all pixels from bufferImages
            for(int i=0; i < numerOfImagesToAverage_; i++){
                
                // Get current frame pixels
                img = bufferImages[i];
                byte[] imgPixels = (byte[]) img.getRawPixels();
                
                // Iterate over all pixels
                for (int index = 0; index < newPixels.length; index++)
                    newPixels[index] = (float) (newPixels[index] + (int) (imgPixels[index] & 0xff));  
            }
            
            // Divide by length to get the mean
            for (int index = 0; index < newPixels.length; index++)
                if (onlySum)
                    newPixelsFinal[index] = (byte) (int) (newPixels[index]);
                else
                    newPixelsFinal[index] = (byte) (int) (newPixels[index] / numerOfImagesToAverage_);
            
            resultPixels = newPixelsFinal;
            
        }
        else if (bytesPerPixel == 2) {
            
            // Create new array
            float[] newPixels = new float[width * height];
            short[] newPixelsFinal = new short[width * height];
            
            // Sum up all pixels from bufferImages
            for(int i=0; i < numerOfImagesToAverage_; i++){
                
                // Get current frame pixels
                img = bufferImages[i];
                short[] imgPixels = (short[]) img.getRawPixels();
                
                // Iterate over all pixels
                for (int index = 0; index < newPixels.length; index++)
                    newPixels[index] = (float) (newPixels[index] + (int) (imgPixels[index] & 0xffff));
            }
            
            // Divide by length to get the mean
            for (int index = 0; index < newPixels.length; index++)
                if (onlySum)
                    newPixelsFinal[index] = (short) (int) (newPixels[index]);
                else
                    newPixelsFinal[index] = (short) (int) (newPixels[index] / numerOfImagesToAverage_);
            
            resultPixels = newPixelsFinal;
            
        }
        
        // Create the processed image
        processedImage = studio_.data().createImage(resultPixels, width, height,
                bytesPerPixel, numComponents, coords, metadata);

      }
    
//    public void medianProcessImages(){
//        // TODO
//    }
    
    public void extremaProcessImages(String extremaType) throws Exception{
        
        // Could be moved outside processImage() ?
        Image img = bufferImages[0];
        int bitDepth = img.getMetadata().getBitDepth();
        int width = img.getWidth();
        int height = img.getHeight();
        int bytesPerPixel = img.getBytesPerPixel();
        int numComponents = img.getNumComponents();
        Coords coords = img.getCoords();
        Metadata metadata = img.getMetadata();

        Object resultPixels = null;
        
        if (bytesPerPixel == 1) {
            
            // Create new array
            float[] newPixels = new float[width * height];
            byte[] newPixelsFinal = new byte[width * height];
            
            float currentValue;
            float actualValue;
            
            // Init the new array
            if (extremaType.equals("max"))
                for(int i=0; i < newPixels.length; i++)
                    newPixels[i] = 0;
            else if (extremaType.equals("min"))
                for(int i=0; i < newPixels.length; i++)
                    newPixels[i] = Byte.MAX_VALUE;
            else
                throw new Exception("FrameAverager : Wrong extremaType " + extremaType);
            
            // Iterate over all frames
            for(int i=0; i < numerOfImagesToAverage_; i++){
                
                // Get current frame pixels
                img = bufferImages[i];
                short[] imgPixels = (short[]) img.getRawPixels();

                // Iterate over all pixels
                for (int index = 0; index < newPixels.length; index++){
                    currentValue = (float) (int) (imgPixels[index] & 0xffff);
                    actualValue = (float) newPixels[index];
                    
                    if (extremaType.equals("max"))
                        newPixels[index] = (float) Math.max(currentValue, actualValue);
                    else if (extremaType.equals("min"))
                        newPixels[index] = (float) Math.min(currentValue, actualValue);
                    else
                        throw new Exception("FrameAverager : Wrong extremaType " + extremaType);
                }
            }
            
            // Convert to short
            for (int index = 0; index < newPixels.length; index++)
                newPixelsFinal[index] = (byte) newPixels[index];
            
            resultPixels = newPixelsFinal;
            
        }
        else if (bytesPerPixel == 2) {
            
            // Create new array
            float[] newPixels = new float[width * height];
            short[] newPixelsFinal = new short[width * height];
            
            float currentValue;
            float actualValue;
            
            // Init the new array
            if (extremaType.equals("max"))
                for(int i=0; i < newPixels.length; i++)
                    newPixels[i] = 0;
            else if (extremaType.equals("min"))
                for(int i=0; i < newPixels.length; i++)
                    newPixels[i] = Byte.MAX_VALUE;
            else
                throw new Exception("FrameAverager : Wrong extremaType " + extremaType);
            
            // Iterate over all frames
            for(int i=0; i < numerOfImagesToAverage_; i++){
                
                // Get current frame pixels
                img = bufferImages[i];
                short[] imgPixels = (short[]) img.getRawPixels();

                // Iterate over all pixels
                for (int index = 0; index < newPixels.length; index++){
                    currentValue = (float) (int) (imgPixels[index] & 0xffff);
                    actualValue = (float) newPixels[index];
                    
                    if (extremaType.equals("max"))
                        newPixels[index] = (float) Math.max(currentValue, actualValue);
                    else if (extremaType.equals("min"))
                        newPixels[index] = (float) Math.min(currentValue, actualValue);
                    else
                        throw new Exception("FrameAverager : Wrong extremaType " + extremaType); 
                }
            }
            
            // Convert to short
            for (int index = 0; index < newPixels.length; index++)
                newPixelsFinal[index] = (short) newPixels[index];
            
            resultPixels = newPixelsFinal;
   
        }
        
        // Create the processed image
        processedImage = studio_.data().createImage(resultPixels, width, height,
                bytesPerPixel, numComponents, coords, metadata);
        
    }

}

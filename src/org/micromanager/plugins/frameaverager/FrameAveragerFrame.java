package org.micromanager.plugins.frameaverager;

import mmcorej.CMMCore;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;
import org.micromanager.data.ProcessorConfigurator;
import org.micromanager.internal.utils.MMFrame;

public class FrameAveragerFrame extends MMFrame implements ProcessorConfigurator {

   private final Studio studio_;
   private final CMMCore core_;

   public FrameAveragerFrame(PropertyMap settings, Studio studio) {
      studio_ = studio;
      core_ = studio_.getCMMCore();
   }

    @Override
    public void showGUI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PropertyMap getSettings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

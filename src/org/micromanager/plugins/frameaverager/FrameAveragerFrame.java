package org.micromanager.plugins.frameaverager;

import mmcorej.CMMCore;
import net.miginfocom.swing.MigLayout;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;
import org.micromanager.data.ProcessorConfigurator;
import org.micromanager.internal.utils.MMFrame;

public class FrameAveragerFrame extends MMFrame implements ProcessorConfigurator {

   private static final int DEFAULT_WIN_X = 100;
   private static final int DEFAULT_WIN_Y = 100;
    
   private final Studio studio_;
   private final CMMCore core_;

    public FrameAveragerFrame(PropertyMap settings, Studio studio) {
       studio_ = studio;
       core_ = studio_.getCMMCore();

       initComponents();

       loadAndRestorePosition(DEFAULT_WIN_X, DEFAULT_WIN_Y);
    }

    @Override
    public void showGUI() {
        setVisible(true);
    }

    @Override
    public void cleanup() {
        dispose();
    }

    @Override
    public PropertyMap getSettings() {
        PropertyMap.PropertyMapBuilder builder = studio_.data().getPropertyMapBuilder();
        builder.putString("orientation", "kkk");
        builder.putInt("splits", 3);
        return builder.build();
    }
    
    private void initComponents() {
        setTitle("FrameAverager");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);        
    }
}

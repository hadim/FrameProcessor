package org.micromanager.plugins.frameaverager;

import org.micromanager.data.ProcessorConfigurator;
import org.micromanager.data.ProcessorFactory;
import org.micromanager.data.ProcessorPlugin;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;

import org.scijava.plugin.SciJavaPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = ProcessorPlugin.class)
public class TaggedFrameAverager implements ProcessorPlugin, SciJavaPlugin {

    public static String menuName = "Frame Averager";
    public static String tooltipDescription = "Live Frame Averaging during acquisition";
    public static String versionNumber = "1.0";
    public static String copyright = "Hadrien Mary";

    private Studio studio_;

    @Override
    public void setContext(Studio studio) {
       studio_ = studio;
    }

    @Override
    public ProcessorConfigurator createConfigurator(PropertyMap settings) {
       //return new SplitViewFrame(settings, studio_);
       return null;
    }

    @Override
    public ProcessorFactory createFactory(PropertyMap settings) {
       //return new SplitViewFactory(studio_, settings);
       return null;
    }

    @Override
    public String getName() {
       return menuName;
    }

    @Override
    public String getHelpText() {
       return tooltipDescription;
    }

    @Override
    public String getVersion() {
       return versionNumber;
    }

    @Override
    public String getCopyright() {
       return copyright;
    }
}

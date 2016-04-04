package org.micromanager.plugins.frameaverager;

import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

import mmcorej.CMMCore;

import org.micromanager.PropertyMap;
import org.micromanager.Studio;
import org.micromanager.data.ProcessorConfigurator;
import org.micromanager.internal.utils.MMFrame;

public class FrameAveragerConfigurator extends MMFrame implements ProcessorConfigurator {
    
    private static final String PROCESSOR_ALGO = "Algorithm to apply on stack images";
    private static final String NUMBER_TO_AVERAGE = "Number of images to average";
    private static final String ENABLE_MDA = "Whether or not enable the plugin during acquisition";
    private static final String ENABLE_LIVE = "Whether or not enable the plugin during live";
    
    private final Studio studio_;
    private final CMMCore core_;
    private final PropertyMap settings_;

    public FrameAveragerConfigurator(PropertyMap settings, Studio studio) {
       studio_ = studio;
       core_ = studio_.getCMMCore();
       settings_ = settings;

       initComponents();
       loadSettingValue();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        enableDuringAcquisitionBox_ = new javax.swing.JCheckBox();
        enableDuringLiveBox_ = new javax.swing.JCheckBox();
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setCommitsOnValidEdit(true);
        formatter.setAllowsInvalid(false);
        numerOfImagesToAverageField_ = new javax.swing.JFormattedTextField(formatter);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        processorAlgoBox_ = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FrameAverager Processor Configuration");

        enableDuringAcquisitionBox_.setText("Enable during acquisition");
        enableDuringAcquisitionBox_.setName(""); // NOI18N

        enableDuringLiveBox_.setText("Enable during live mode");
        enableDuringLiveBox_.setName(""); // NOI18N

        numerOfImagesToAverageField_.setName("_"); // NOI18N

        jLabel1.setText("Number of images to average");

        jLabel2.setText("Algorithm to apply on image stack");

        processorAlgoBox_.addItem(FrameAveragerPlugin.PROCESSOR_ALGO_MEAN);
        //processorAlgoBox_.addItem(FrameAveragerPlugin.PROCESSOR_ALGO_MEDIAN);
        processorAlgoBox_.addItem(FrameAveragerPlugin.PROCESSOR_ALGO_SUM);
        processorAlgoBox_.addItem(FrameAveragerPlugin.PROCESSOR_ALGO_MAX);
        processorAlgoBox_.addItem(FrameAveragerPlugin.PROCESSOR_ALGO_MIN);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numerOfImagesToAverageField_)
                            .addComponent(enableDuringAcquisitionBox_)
                            .addComponent(enableDuringLiveBox_))
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(processorAlgoBox_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(processorAlgoBox_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numerOfImagesToAverageField_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(enableDuringAcquisitionBox_)
                .addGap(28, 28, 28)
                .addComponent(enableDuringLiveBox_)
                .addGap(79, 79, 79))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadSettingValue() {
        processorAlgoBox_.setSelectedItem(settings_.getString(
                "processorAlgo", getProcessorAglo()));
        numerOfImagesToAverageField_.setText(settings_.getString(
                "numerOfImagesToAverage", Integer.toString(getNumerOfImagesToAverage())));
        enableDuringAcquisitionBox_.setSelected(
                settings_.getBoolean("enableDuringAcquisition",
                        getEnableDuringAcquisition()));
        enableDuringLiveBox_.setSelected(
                settings_.getBoolean("enableDuringLive",
                        getEnableDuringLive()));
    }
    
    @Override
    public void showGUI() {
        pack();
        setVisible(true);
    }

    @Override
    public void cleanup() {
        dispose();
    }

    @Override
    public PropertyMap getSettings() {
        
        // Save preferences now.
        setProcessorAglo((String) processorAlgoBox_.getSelectedItem());
        setNumerOfImagesToAverage(Integer.parseInt(numerOfImagesToAverageField_.getText()));
        setEnableDuringAcquisition(enableDuringAcquisitionBox_.isSelected());
        setEnableDuringLive(enableDuringLiveBox_.isSelected());
        
        PropertyMap.PropertyMapBuilder builder = studio_.data().getPropertyMapBuilder();
        builder.putString("processorAlgo", (String) processorAlgoBox_.getSelectedItem());
        builder.putInt("numerOfImagesToAverage", Integer.parseInt(numerOfImagesToAverageField_.getText()));
        builder.putBoolean("enableDuringAcquisition", enableDuringAcquisitionBox_.isSelected());
        builder.putBoolean("enableDuringLive", enableDuringLiveBox_.isSelected());
        return builder.build();
    }

    private String getProcessorAglo() {
      return studio_.profile().getString(FrameAveragerConfigurator.class,
              PROCESSOR_ALGO, FrameAveragerPlugin.PROCESSOR_ALGO_MEAN);
    }

    private void setProcessorAglo(String processorAlgo) {
      studio_.profile().setString(FrameAveragerConfigurator.class,
              PROCESSOR_ALGO, processorAlgo);
    }
    
    private int getNumerOfImagesToAverage() {
      return studio_.profile().getInt(FrameAveragerConfigurator.class,
              NUMBER_TO_AVERAGE, 10);
    }

    private void setNumerOfImagesToAverage(int numerOfImagesToAverage) {
      studio_.profile().setInt(FrameAveragerConfigurator.class,
              NUMBER_TO_AVERAGE, numerOfImagesToAverage);
    }

    private boolean getEnableDuringAcquisition() {
      return studio_.profile().getBoolean(FrameAveragerConfigurator.class,
              ENABLE_MDA, true);
    }

    private void setEnableDuringAcquisition(boolean enableDuringAcquisition) {
      studio_.profile().setBoolean(FrameAveragerConfigurator.class,
              ENABLE_MDA, enableDuringAcquisition);
    }

    private boolean getEnableDuringLive() {
      return studio_.profile().getBoolean(FrameAveragerConfigurator.class,
              ENABLE_LIVE, true);
    }

    private void setEnableDuringLive(boolean enableDuringLive) {
      studio_.profile().getBoolean(FrameAveragerConfigurator.class,
              ENABLE_LIVE, true);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox enableDuringAcquisitionBox_;
    private javax.swing.JCheckBox enableDuringLiveBox_;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JFormattedTextField numerOfImagesToAverageField_;
    private javax.swing.JComboBox processorAlgoBox_;
    // End of variables declaration//GEN-END:variables
}

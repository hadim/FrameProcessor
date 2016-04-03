# FrameAverager

A friendly fork of the excellent FrameAverager plugin from [OpenPolScope](http://www.openpolscope.org/pages/MMPlugin_Frame_Averager.htm) available on github at https://github.com/LC-PolScope/Micro-Manager-Addons.

The purpose of this plugin is to update it to Micromanager 2.0.

# Features

This plugin can perform several classic operations during **live** or **MDA** such as `mean`, `sum`, `max` and `min` on a certain number of frames defined by the user.

Current state of the plugin doesn't allow a complex MDA with multiple Z, CHANNEL and XY_POSITION. **Any help on this feature is welcome**.

# Usage

- Download the JAR file [FrameAverager.jar](./dist/FrameAverager.jar).
- Copy it to `YOUR_MICRO_MANAGER_FOLDER/mmplugins/`
- Launch Micromanager

Please report any issue to https://github.com/hadim/FrameAverager/issues.

# Develop

- Import the project into Netbeans/Eclipse (Ant project).
- Add the appropriate MM JAR files to the properties of the project (follow [theses instructions](https://micro-manager.org/wiki/Writing_plugins_for_Micro-Manager)).
- Hack it !
- Launch Micromanager directly from Eclipse/Netbeans by running Debug.

# License

GPLv3. See [LICENSE](LICENSE)

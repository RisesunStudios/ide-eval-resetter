package io.zhile.research.intellij.ier.helper;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.io.FileUtil;

public class Constants {
    public static final PluginClassLoader CLASS_LOADER = (PluginClassLoader) Constants.class.getClassLoader();
    public static final PluginId PLUGIN_ID = CLASS_LOADER.getPluginId();
    public static final String PLUGIN_NAME = PluginManager.getPlugin(PLUGIN_ID).getName();
    public static final String IDE_NAME = ApplicationNamesInfo.getInstance().getProductName();
    public static final String IDE_NAME_LOWER = IDE_NAME.toLowerCase();
    public static final String IDE_HASH = Integer.toHexString(FileUtil.pathHashCode(PathManager.getHomePath()));
}

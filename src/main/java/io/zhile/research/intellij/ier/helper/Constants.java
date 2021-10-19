package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.io.FileUtil;

public class Constants {
    public static final String ACTION_NAME = "Eval Reset";
    public static final String PLUGIN_ID_STR = "io.zhile.research.ide-eval-resetter";
    public static final String IDE_NAME = ApplicationNamesInfo.getInstance().getProductName();
    public static final String IDE_NAME_LOWER = IDE_NAME.toLowerCase();
    public static final String IDE_HASH = Integer.toHexString(FileUtil.pathHashCode(PathManager.getHomePath()));
    public static final int IDE_BASELINE_VERSION = AppHelper.getBuildNumber().getBaselineVersion();
    public static final String PLUGIN_PREFS_PREFIX = "Ide-Eval-Reset";
    public static final String RESET_ACTION_ID = "io.zhile.research.intellij.ier.action.ResetAction";
    public static final String RESTART_ACTION_ID = "io.zhile.research.intellij.ier.action.RestartAction";
}

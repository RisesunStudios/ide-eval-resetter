package io.zhile.research.intellij.ier.common;

import com.intellij.ide.Prefs;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.PropertiesComponentImpl;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.SystemInfo;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.helper.ReflectionHelper;
import org.jdom.Attribute;
import org.jdom.Element;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Resetter {
    private static final String DEFAULT_VENDOR = "jetbrains";
    private static final String OLD_MACHINE_ID_KEY = "JetBrains.UserIdOnMachine";
    private static final String NEW_MACHINE_ID_KEY = DEFAULT_VENDOR + ".user_id_on_machine";
    private static final String DEVICE_ID_KEY = DEFAULT_VENDOR + ".device_id";
    private static final String EVAL_KEY = "evlsprt";
    private static final String AUTO_RESET_KEY = Constants.PLUGIN_PREFS_PREFIX + ".auto_reset." + Constants.IDE_NAME_LOWER + "." + Constants.IDE_HASH;

    public static List<EvalRecord> getEvalRecords() {
        List<EvalRecord> list = new ArrayList<>();

        File evalDir = getEvalDir();
        if (evalDir.exists()) {
            File[] files = evalDir.listFiles();
            if (files == null) {
                NotificationHelper.showError(null, "List eval license file failed!");
            } else {
                for (File file : files) {
                    if (!file.getName().endsWith(".key")) {
                        continue;
                    }

                    list.add(new LicenseFileRecord(file));
                }
            }
        }

        File licenseDir = getLicenseDir();
        if (licenseDir.exists()) {
            File[] files = licenseDir.listFiles();
            if (files == null) {
                NotificationHelper.showError(null, "List license file failed!");
            } else {
                for (File file : files) {
                    if (!file.getName().endsWith(".key") && !file.getName().endsWith(".license")) {
                        continue;
                    }

                    if (file.length() > 0x400) {
                        continue;
                    }

                    list.add(new NormalFileRecord(file));
                }
            }
        }

        Element state = ((PropertiesComponentImpl) PropertiesComponent.getInstance()).getState();
        if (state != null) {
            Attribute attrName, attrValue;
            for (Element element : state.getChildren()) {
                if (!element.getName().equals("property")) {
                    continue;
                }

                attrName = element.getAttribute("name");
                attrValue = element.getAttribute("value");
                if (attrName == null || attrValue == null) {
                    continue;
                }

                if (!attrName.getValue().startsWith(EVAL_KEY)) {
                    continue;
                }

                list.add(new PropertyRecord(attrName.getValue()));
            }
        }

        PreferenceRecord[] prefsValue = new PreferenceRecord[]{
                new PreferenceRecord(OLD_MACHINE_ID_KEY, true),
                new PreferenceRecord(NEW_MACHINE_ID_KEY),
                new PreferenceRecord(DEVICE_ID_KEY),
        };
        for (PreferenceRecord record : prefsValue) {
            if (record.getValue() == null) {
                continue;
            }

            list.add(record);
        }

        try {
            List<String> prefsList = new ArrayList<>();
            for (String name : Preferences.userRoot().node(DEFAULT_VENDOR).childrenNames()) {
                if (!name.toLowerCase().startsWith(Constants.IDE_NAME_LOWER)) {
                    continue;
                }

                getAllPrefsKeys(Preferences.userRoot().node(DEFAULT_VENDOR + "/" + name + "/" + Constants.IDE_HASH), prefsList);
            }

            Method methodGetProductCode = ReflectionHelper.getMethod(IdeaPluginDescriptor.class, "getProductCode");
            if (null != methodGetProductCode) {
                for (IdeaPluginDescriptor descriptor : PluginManager.getPlugins()) {
                    String productCode = (String) methodGetProductCode.invoke(descriptor);
                    if (null == productCode || productCode.isEmpty()) {
                        continue;
                    }

                    getAllPrefsKeys(Preferences.userRoot().node(DEFAULT_VENDOR + "/" + productCode.toLowerCase()), prefsList);
                }
            }

            for (String key : prefsList) {
                if (!key.contains(EVAL_KEY)) {
                    continue;
                }

                if (key.startsWith("/")) {
                    key = key.substring(1).replace('/', '.');
                }
                list.add(new PreferenceRecord(key));
            }
        } catch (Exception e) {
            NotificationHelper.showError(null, "List eval preferences failed!");
        }

        if (SystemInfo.isWindows) {
            for (String name : new String[]{"PermanentUserId", "PermanentDeviceId"}) {
                File file = getSharedFile(name);

                if (null != file && file.exists()) {
                    list.add(new NormalFileRecord(file));
                }
            }
        }

        return list;
    }

    public static void reset(List<EvalRecord> records) {
        for (EvalRecord record : records) {
            Resetter.reset(record);
        }
    }

    public static void reset(EvalRecord record) {
        try {
            record.reset();
        } catch (Exception e) {
            NotificationHelper.showError(null, e.getMessage());
        }
    }

    public static boolean isAutoReset() {
        return Prefs.getBoolean(AUTO_RESET_KEY, false);
    }

    public static void setAutoReset(boolean isAutoReset) {
        Prefs.putBoolean(AUTO_RESET_KEY, isAutoReset);
        syncPrefs();
    }

    public static void syncPrefs() {
        try {
            Preferences.userRoot().sync();
        } catch (BackingStoreException e) {
            NotificationHelper.showError(null, "Flush preferences failed!");
        }
    }

    protected static File getSharedFile(String fileName) {
        String appData = System.getenv("APPDATA");
        if (appData == null) {
            return null;
        }

        return Paths.get(appData, "JetBrains", fileName).toFile();
    }

    protected static File getEvalDir() {
        String configPath = PathManager.getConfigPath();

        return new File(configPath, "eval");
    }

    protected static File getLicenseDir() {
        return new File(PathManager.getConfigPath());
    }

    protected static void getAllPrefsKeys(Preferences prefs, List<String> list) throws BackingStoreException {
        String[] childrenNames = prefs.childrenNames();
        if (childrenNames.length == 0) {
            for (String key : prefs.keys()) {
                list.add(prefs.absolutePath() + "/" + key);
            }
            return;
        }

        for (String childName : childrenNames) {
            getAllPrefsKeys(prefs.node(childName), list);
        }
    }
}

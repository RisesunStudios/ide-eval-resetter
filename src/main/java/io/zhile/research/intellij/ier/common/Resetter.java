package io.zhile.research.intellij.ier.common;

import com.intellij.ide.Prefs;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.PropertiesComponentImpl;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.helper.Reflection;
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
    private static final String IDE_EVAL_PREFIX = DEFAULT_VENDOR + "/" + Constants.IDE_NAME_LOWER + "/" + Constants.IDE_HASH;
    private static final String EVAL_KEY = "evlsprt";

    private static final PropertiesComponentImpl PROPS = (PropertiesComponentImpl) PropertiesComponent.getInstance();

    public static List<RecordItem> getEvalRecords() {
        List<RecordItem> list = new ArrayList<>();

        File evalDir = getEvalDir();
        if (evalDir.exists()) {
            for (File file : evalDir.listFiles()) {
                list.add(new RecordItem(RecordType.FILE, file.getAbsolutePath()));
            }
        }

        Element state = PROPS.getState();
        if (state != null) {
            Attribute attrName, attrValue;
            String key, value;
            for (Element element : state.getChildren()) {
                if (!element.getName().equals("property")) {
                    continue;
                }

                attrName = element.getAttribute("name");
                attrValue = element.getAttribute("value");
                if (attrName == null || attrValue == null) {
                    continue;
                }

                key = attrName.getValue();
                value = attrValue.getValue();
                if (key.startsWith(EVAL_KEY)) {
                    list.add(new RecordItem(RecordType.PROPERTY, key, value));
                }
            }
        }

        RecordItem[] prefsValue = new RecordItem[]{
                new RecordItem(RecordType.PREFERENCE, OLD_MACHINE_ID_KEY, Preferences.userRoot().get(OLD_MACHINE_ID_KEY, null)),
                new RecordItem(RecordType.PREFERENCE, NEW_MACHINE_ID_KEY, Prefs.get(NEW_MACHINE_ID_KEY, null)),
                new RecordItem(RecordType.PREFERENCE, DEVICE_ID_KEY, Prefs.get(DEVICE_ID_KEY, null)),
        };
        for (RecordItem item : prefsValue) {
            if (null == item.getValue()) {
                continue;
            }
            list.add(item);
        }

        try {
            List<String> prefsList = new ArrayList<>();
            getAllPrefsKeys(Preferences.userRoot().node(IDE_EVAL_PREFIX), prefsList);

            Method methodGetProductCode = Reflection.getMethod(IdeaPluginDescriptor.class, "getProductCode");
            Method methodGetReleaseVersion = Reflection.getMethod(IdeaPluginDescriptor.class, "getReleaseVersion");
            if (null != methodGetProductCode && null != methodGetReleaseVersion) {
                for (IdeaPluginDescriptor descriptor : PluginManager.getPlugins()) {
                    String productCode = (String) methodGetProductCode.invoke(descriptor);
                    if (null == productCode || productCode.isEmpty()) {
                        continue;
                    }

                    int releaseVersion = (int) methodGetReleaseVersion.invoke(descriptor);
                    getAllPrefsKeys(Preferences.userRoot().node(DEFAULT_VENDOR + "/" + productCode.toLowerCase() + "/" + releaseVersion), prefsList);
                }
            }

            for (String key : prefsList) {
                if (!key.contains(EVAL_KEY)) {
                    continue;
                }

                if (key.startsWith("/")) {
                    key = key.substring(1).replace('/', '.');
                }
                list.add(new RecordItem(RecordType.PREFERENCE, key, Prefs.get(key, null)));
            }
        } catch (Exception e) {
            NotificationHelper.showError(null, "List eval preferences failed!");
        }

        if (SystemInfo.isWindows) {
            String[] names = new String[]{"PermanentUserId", "PermanentDeviceId"};
            for (String name : names) {
                File file = getSharedFile(name);

                if (null != file && file.exists()) {
                    list.add(new RecordItem(RecordType.FILE, file.getAbsolutePath()));
                }
            }
        }

        return list;
    }

    public static void reset(List<RecordItem> recordItems) {
        for (RecordItem item : recordItems) {
            reset(item);
        }
    }

    public static void reset(RecordItem recordItem) {
        RecordType type = recordItem.getType();
        String key = recordItem.getKey();
        switch (type) {
            case FILE:
                if (!FileUtil.delete(new File(key))) {
                    NotificationHelper.showError(null, "Remove " + type.getValue() + " failed: " + key);
                }
                break;
            case PREFERENCE:
                if (OLD_MACHINE_ID_KEY.equals(key)) {
                    Preferences.userRoot().remove(key);
                } else {
                    Prefs.remove(key);
                }
                break;
            case PROPERTY:
                PROPS.unsetValue(key);
                break;
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

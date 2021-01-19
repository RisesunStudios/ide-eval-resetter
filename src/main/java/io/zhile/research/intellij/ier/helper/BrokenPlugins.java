package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.application.PathManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BrokenPlugins {
    public static void fix() {
        String content = "[]";
        String fileName = "brokenPlugins.json";
        Path brokenPluginsPath = Paths.get(PathManager.getPluginsPath(), fileName);
        File brokenPluginsFile = brokenPluginsPath.toFile();
        if (!brokenPluginsFile.exists() || content.length() == brokenPluginsFile.length()) {
            return;
        }

        try {
            Path bak = brokenPluginsPath.getParent().resolve(fileName + ".tmp");
            Files.write(bak, content.getBytes());
            Files.move(bak, brokenPluginsPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            NotificationHelper.showError(null, "Set broken plugins failed!");
        }
    }
}

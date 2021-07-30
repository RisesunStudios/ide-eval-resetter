package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URI;

public class ErrorReporter extends ErrorReportSubmitter {
    private static final String NEW_ISSUE_URL = "https://gitee.com/pengzhile/ide-eval-resetter/issues/new";

    public String getReportActionText() {
        return "Open Browser to Submit This Issue...";
    }

    public String getPrivacyNoticeText() {
        return "Click the submit button will <b>write the stacktrace text to your clipboard</b>!<br>So that you can paste it directly when writing the issue.";
    }

    public boolean submit(IdeaLoggingEvent[] events, String additionalInfo, @NotNull Component parentComponent, @NotNull Consumer consumer) {
        int length = events.length - 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= length; i++) {
            sb.append(events[i].getThrowableText());
            if (i != length) {  // f**k java7
                sb.append("\n\n\n");
            }
        }

        try {
            Desktop.getDesktop().browse(new URI(NEW_ISSUE_URL));
            StringSelection selection = new StringSelection(sb.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);

            consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE));
        } catch (Exception e) {
            consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.FAILED));
            return false;
        }

        return true;
    }
}

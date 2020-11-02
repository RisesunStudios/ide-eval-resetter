package io.zhile.research.intellij.ier.ui.form;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import io.zhile.research.intellij.ier.common.RecordItem;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.component.ResetTimer;
import io.zhile.research.intellij.ier.helper.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainForm {
    private JPanel rootPanel;
    private JButton btnReset;
    private JList lstMain;
    private JLabel lblLastResetTime;
    private JButton btnReloadList;
    private JLabel lblFound;
    private JLabel lblLastResetTimeLabel;

    private final DialogWrapper dialogWrapper;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public MainForm(DialogWrapper dialogWrapper) {
        this.dialogWrapper = dialogWrapper;
    }

    public JPanel getContent() {
        boldFont(lblFound);
        boldFont(lblLastResetTimeLabel);

        lblLastResetTime.setText(ResetTimer.getLastResetTimeStr());
        lstMain.setModel(listModel);

        reloadRecordItems();
        btnReloadList.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadRecordItems();
            }
        });

        btnReset.setIcon(AllIcons.General.Reset);
        btnReset.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetEvalItems();
            }
        });

        if (null != dialogWrapper) {
            dialogWrapper.getRootPane().setDefaultButton(btnReset);
        }

        rootPanel.setMinimumSize(new Dimension(600, 240));
        return rootPanel;
    }

    private void reloadRecordItems() {
        listModel.clear();

        List<RecordItem> recordItemList = Resetter.getEvalRecords();
        for (RecordItem item : recordItemList) {
            listModel.addElement(item.getType().getValue() + ": \t" + item.getKey() + (null == item.getValue() ? "" : " = \t" + item.getValue()));
        }
    }

    private void resetEvalItems() {
        Resetter.reset(Resetter.getEvalRecords());
        ResetTimer.resetLastResetTime();
        listModel.clear();

        if (Messages.YES == Messages.showYesNoDialog("Reset successfully!\nWould like to restart your IDE?", Constants.PLUGIN_NAME, AllIcons.General.Reset)) {
            if (null != dialogWrapper) {
                dialogWrapper.close(0);
            }

            ApplicationManager.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    ApplicationManager.getApplication().restart();
                }
            });

            return;
        }

        reloadRecordItems();
    }

    private static void boldFont(Component component) {
        Font font = component.getFont();
        component.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
    }
}

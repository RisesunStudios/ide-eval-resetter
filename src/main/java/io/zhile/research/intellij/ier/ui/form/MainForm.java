package io.zhile.research.intellij.ier.ui.form;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import io.zhile.research.intellij.ier.common.EvalRecord;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.component.ResetTimer;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.listener.AppEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainForm {
    private JPanel rootPanel;
    private JButton btnReset;
    private JList lstMain;
    private JLabel lblLastResetTime;
    private JButton btnReload;
    private JLabel lblFound;
    private JLabel lblLastResetTimeLabel;
    private JCheckBox chkResetAuto;

    private final DialogWrapper dialogWrapper;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public MainForm(DialogWrapper dialogWrapper) {
        this.dialogWrapper = dialogWrapper;
    }

    public JPanel getContent() {
        boldFont(lblFound);
        boldFont(lblLastResetTimeLabel);
        reloadLastResetTime();

        chkResetAuto.setSelected(Resetter.isAutoReset());
        chkResetAuto.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Resetter.setAutoReset(chkResetAuto.isSelected());
            }
        });

        lstMain.setModel(listModel);
        reloadRecordItems();

        btnReload.setIcon(AllIcons.Actions.Refresh);
        btnReload.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadLastResetTime();
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

    private void reloadLastResetTime() {
        lblLastResetTime.setText(ResetTimer.getLastResetTimeStr());
    }

    private void reloadRecordItems() {
        listModel.clear();

        List<EvalRecord> recordItemList = Resetter.getEvalRecords();
        recordItemList.forEach(record -> listModel.addElement(record.toString()));
    }

    private void resetEvalItems() {
        if (Messages.YES != Messages.showYesNoDialog("Your IDE will restart after reset!\nAre your sure to reset?", Constants.PLUGIN_NAME, AllIcons.General.Reset)) {
            return;
        }

        Resetter.reset(Resetter.getEvalRecords());
        ResetTimer.resetLastResetTime();
        listModel.clear();

        if (null != dialogWrapper) {
            dialogWrapper.close(0);
        }

        AppEventListener.disable();
        ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().restart());
    }

    private static void boldFont(Component component) {
        Font font = component.getFont();
        component.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
    }
}

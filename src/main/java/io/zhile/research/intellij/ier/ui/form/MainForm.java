package io.zhile.research.intellij.ier.ui.form;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Disposer;
import io.zhile.research.intellij.ier.common.EvalRecord;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.AppHelper;
import io.zhile.research.intellij.ier.helper.PluginHelper;
import io.zhile.research.intellij.ier.helper.ResetTimeHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainForm {
    private JPanel rootPanel;
    private JButton btnReset;
    private JList<String> lstMain;
    private JLabel lblLastResetTime;
    private JButton btnReload;
    private JLabel lblFound;
    private JLabel lblLastResetTimeLabel;
    private JCheckBox chkResetAuto;
    private JLabel lblVersion;

    private DialogWrapper dialogWrapper;
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public MainForm(Disposable disposable) {
        this(disposable, null);
    }

    public MainForm(Disposable disposable, DialogWrapper wrapper) {
        this.dialogWrapper = wrapper;

        Disposer.register(disposable, new Disposable() {
            @Override
            public void dispose() {
                rootPanel.removeAll();

                listModel = null;
                dialogWrapper = null;
            }
        });
    }

    public JPanel getContent(Disposable disposable) {
        Disposer.register(disposable, new Disposable() {
            @Override
            public void dispose() {
                rootPanel.removeAll();
            }
        });

        boldFont(lblFound);
        boldFont(lblLastResetTimeLabel);
        reloadLastResetTime();

        lblVersion.setText("v" + PluginHelper.getPluginVersion());

        chkResetAuto.setSelected(Resetter.isAutoReset());
        addActionEventListener(chkResetAuto, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Resetter.setAutoReset(chkResetAuto.isSelected());
            }
        }, disposable);

        lstMain.setModel(listModel);
        reloadRecordItems();

        btnReload.setIcon(AllIcons.Actions.Refresh);
        addActionEventListener(btnReload, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadLastResetTime();
                reloadRecordItems();
            }
        }, disposable);

        btnReset.setIcon(AllIcons.General.Reset);
        addActionEventListener(btnReset, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetEvalItems();
            }
        }, disposable);

        if (null != dialogWrapper) {
            dialogWrapper.getRootPane().setDefaultButton(btnReset);
            rootPanel.setMinimumSize(new Dimension(600, 240));
        }

        return rootPanel;
    }

    private void reloadLastResetTime() {
        lblLastResetTime.setText(ResetTimeHelper.getLastResetTimeStr());
    }

    private void reloadRecordItems() {
        listModel.clear();

        List<EvalRecord> recordItemList = Resetter.getEvalRecords();
        for (EvalRecord record : recordItemList) {
            listModel.addElement(record.toString());
        }
    }

    private void resetEvalItems() {
        if (Messages.YES != Messages.showYesNoDialog("Your IDE will restart after reset!\nAre your sure to reset?", PluginHelper.getPluginName(), AllIcons.General.Reset)) {
            return;
        }

        Resetter.reset(Resetter.getEvalRecords());
        ResetTimeHelper.resetLastResetTime();
        listModel.clear();

        if (null != dialogWrapper) {
            dialogWrapper.close(0);
        }

        AppHelper.restart();
    }

    private static void boldFont(Component component) {
        Font font = component.getFont();
        component.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
    }

    private static void addActionEventListener(final AbstractButton button, final ActionListener listener, Disposable disposable) {
        button.addActionListener(listener);
        Disposer.register(disposable, new Disposable() {
            @Override
            public void dispose() {
                button.removeActionListener(listener);
            }
        });
    }
}

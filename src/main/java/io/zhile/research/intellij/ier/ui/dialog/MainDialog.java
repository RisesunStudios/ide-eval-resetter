package io.zhile.research.intellij.ier.ui.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import io.zhile.research.intellij.ier.ui.form.MainForm;

import javax.swing.*;

public class MainDialog extends DialogWrapper {
    public MainDialog() {
        super(true);
        init();
        setTitle("Eval Reset");
    }


    @Override
    protected JComponent createCenterPanel() {
        MainForm mainForm = new MainForm(this);
        return mainForm.getContent();
    }

    @Override
    protected JComponent createSouthPanel() {
        return null;
    }
}

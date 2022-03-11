package com.pg.ui;

import com.pg.core.ProblemParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class App extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField problemNum;
    private JLabel StatusMessage;

    public App() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Baekjoon Problem Generator");

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        ProblemParser problemParser = new ProblemParser(problemNum.getText());
        try {
            problemParser.generateMdFile();
            StatusMessage.setText(System.getProperty("user.home") + "/Desktop/READEME.md 파일 생성 성공.");
            StatusMessage.setForeground(new Color(92, 184, 92));
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            int httpStatusIndex = errorMessage.lastIndexOf("Status=");
            String httpStatus = errorMessage.substring(httpStatusIndex + 7, httpStatusIndex + 10);
            switch (httpStatus) {
                case "404": {
                    StatusMessage.setText("없는 문제입니다.");
                    StatusMessage.setForeground(new Color(217, 83, 79));
                }
                break;
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        App dialog = new App();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

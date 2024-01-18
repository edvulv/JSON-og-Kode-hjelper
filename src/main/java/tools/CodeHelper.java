package tools;

import models.CodeSeg;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CodeHelper extends JFrame {
    private JButton csharpButton;
    private JButton cssButton;
    private JButton htmlButton;
    private JButton jsButton;
    private JButton javaButton;
    private JPanel mainpanel;
    private JTextField inputText;
    private JButton searchButton;
    private JList<CodeSeg> codeReturnedList;
    private JTabbedPane tabbedPane;
    private JPanel useCode;
    private JPanel createCode;
    private JComboBox lanComboBox;
    DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
    private JTextArea codeField;
    private JTextArea descriptionField;
    private JButton submitCodeButton;
    private JCheckBox codeCheckBox;
    private JCheckBox descriptionCheckBox;
    private JCheckBox automaticSearchCheckBox;
    private JLabel nowUsingText;
    private JTextArea codeArea;
    private JTextArea descriptionArea;
    private JLabel languageText;
    private JLabel codeAddSuccess;
    private JLabel codeError;
    DefaultListModel<CodeSeg> codeModel = new DefaultListModel<>();


    ArrayList<CodeSeg> codes = new ArrayList<>();
    ArrayList<String> totalLanguages = new ArrayList<>();
    String codeLanguage = "";
    boolean searchCode = true;
    boolean searchDesc = false;
    boolean automaticSearch = true;

    public CodeHelper(String title) {
        super(title);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(mainpanel);
        this.pack();
        this.setSize(600, 400);
        codeAddSuccess.setVisible(true);
        codeError.setVisible(false);
        submitCodeButton.setEnabled(false);

        codeLanguage = "csharp";
        codeReturnedList.setModel(codeModel);
        lanComboBox.setModel(comboBoxModel);
        codes = csvDB.getCodeList();
        for (CodeSeg c : codes) {
            if (!totalLanguages.contains(c.codeLanguage)) {
                totalLanguages.add(c.codeLanguage);
                comboBoxModel.addElement(c.codeLanguage);
            }
        }
        lanComboBox.updateUI();
        codeCheckBox.setSelected(true);
        automaticSearchCheckBox.setSelected(true);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                codeAddSuccess.setVisible(false);
                codeError.setVisible(false);
            } });

        inputText.addCaretListener(new CaretListener() {
            @Override public void caretUpdate(CaretEvent e) { if (automaticSearch) { Search(); } } });
        inputText.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { Search(); } });
        codeCheckBox.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) { searchCode = codeCheckBox.isSelected(); } });
        descriptionCheckBox.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) { searchDesc = descriptionCheckBox.isSelected(); } });
        automaticSearchCheckBox.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) { automaticSearch = automaticSearchCheckBox.isSelected(); } });
        searchButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { Search(); } });

        //Buttons for languages
        csharpButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { codeLanguage = "csharp"; nowUsingText.setText("Now using C#"); } });
        javaButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { codeLanguage = "java"; nowUsingText.setText("Now using Java"); } });
        jsButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { codeLanguage = "js"; nowUsingText.setText("Now using Javascript"); } });
        htmlButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { codeLanguage = "html"; nowUsingText.setText("Now using HTML"); } });
        cssButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { codeLanguage = "css"; nowUsingText.setText("Now using CSS"); } });

        codeReturnedList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    CodeSeg code = codeReturnedList.getSelectedValue();
                    languageText.setText("Language: " + code.codeLanguage);
                    codeArea.setText(code.codeSequence);
                    descriptionArea.setText("Description: " + code.description);
                } catch (NullPointerException mn) {
                    languageText.setText("Language: ");
                    codeArea.setText("");
                    descriptionArea.setText("Description:");}
            }
        });

        submitCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int retErr = CreateCode();
                if (retErr != 0) {
                    System.err.println("error");
                }
            }
        });

        codeField.addCaretListener(new CaretListener() {
            @Override public void caretUpdate(CaretEvent e) { CheckInput(); } });
        descriptionField.addCaretListener(new CaretListener() {
            @Override public void caretUpdate(CaretEvent e) { CheckInput(); } });
    }

    void Search() {
        ArrayList<CodeSeg> newCodeList = SearchList(inputText.getText());
        codeModel.clear();
        for (CodeSeg c : newCodeList) {
            codeModel.addElement(c);
        }
        codeReturnedList.updateUI();
    }

    ArrayList<CodeSeg> SearchList(String searchWord) {
        ArrayList<CodeSeg> cr = new ArrayList<>();
        if (searchCode) {
            for (CodeSeg c: codes) {
                if (c.codeLanguage.equals(codeLanguage)) {
                    boolean canCompare = WordCheckAlgorythm(c.codeSequence);
                    if (canCompare || searchWord.length() < 1) {
                        cr.add(c);
                    }
                }
            }
        }
        if (searchDesc) {
            for (CodeSeg c: codes) {
                if (c.codeLanguage.equals(codeLanguage)) {
                    boolean canCompare = WordCheckAlgorythm(c.description);
                    if (canCompare || searchWord.length() < 1) {
                        cr.add(c);
                    }
                }
            }
        }
        return cr;
    }

    void CheckInput() {
        if (codeField.getText().length() > 2 && descriptionField.getText().length() > 2) {
            submitCodeButton.setEnabled(true);
        } else {
            submitCodeButton.setEnabled(false);
        }
    }

    int CreateCode() {
        int error = 0;
        ArrayList<String> stringList = new ArrayList<>();
        try {
            CodeSeg newCode = new CodeSeg(lanComboBox.getSelectedItem().toString(), codeField.getText(), descriptionField.getText());
            codes.add(newCode);
            for (CodeSeg c : codes) {
                stringList.add(c.csvFormatted());
            }
        } catch (NullPointerException n) {
            error = 1;
        }
        if (error == 0) {
            csvDB.WriteToFile(stringList);
            codeField.setText("");
            descriptionField.setText("");
            codeAddSuccess.setVisible(true);
            codeError.setVisible(false);
            submitCodeButton.setEnabled(false);
        }
        return error;
    }

    boolean WordCheckAlgorythm(String compare) {
        boolean canCompare = false;
        String search = inputText.getText();
        String[] words = search.split("\\s+");
        if (words.length > 1) {
            int hits = 0;
            int neededHits = 0;
            for (String s : words) {
                if (compare.length() < 6) {
                    if (compare.contains(s.charAt(0) + "")) { hits += 1; } //need 1 hit
                    neededHits = 1;
                } else
                if (compare.length() < 14) {
                    if (compare.contains(s) && compare.contains(s.charAt(0) + "")) { hits += 1; } //needs 4 hits
                    neededHits = 4;
                } else
                if (compare.length() < 23) {
                    if (compare.contains(s) || compare.contains(s.charAt(0) + "")) { hits += 1; } //needs 7 hits
                    neededHits = 7;
                } else
                if (compare.length() < 40) {
                    if (compare.contains(s) || compare.contains(s.charAt(0) + "")) { hits += 1; } //needs 14 hits
                    neededHits = 14;
                }
            }
            if (hits >= neededHits) {canCompare = true;}

        } else { canCompare = true; }
        /*
        for (String s : words) {
            if (compare.contains(s) || compare.contains(s.charAt(0) + "")) { hits += 1; }
        }*/
        return canCompare;
    }
    /* Search Algorythm
    * Less than 6 letters: check if input contains the first letter of word
    * Less than 14 over 5: check if input contains 1 word and 3 similar letters/symbols
    * Less than 23 over 13: check if input contains 2 words and 5 similar letters/symbols
    * Less than 40 over 22: check if input contains 4 words and 10 similar letters/symbols
    * */
}

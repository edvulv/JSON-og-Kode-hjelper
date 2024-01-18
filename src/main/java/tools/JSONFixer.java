package tools;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class JSONFixer extends JFrame {
    boolean newFileIsActive = false;
    Color red = Color.decode("#FF0000");
    Color blue = Color.decode("#0380ED");
    DefaultListModel<String> variablesListModel = new DefaultListModel<>();
    DefaultListModel<String> varTypesListModel = new DefaultListModel<>();
    ArrayList<String> revertChange = new ArrayList<>();
    File draftFile = new File("src/main/java/data/draft.txt");
    ArrayList<String> codeBlocksFromFile = new ArrayList<>();
    int blockChangeIndex = 0;
    private JOptionPane WarningPane;
    private JPanel mainpanel;
    private JProgressBar progressBar;
    private JButton searchButton;
    private JTextField docpathField;
    private JLabel progressP;
    private JLabel errorText;
    private JLabel filepathLabel;
    private JLabel filenameLabel;
    private JTextField docnameField;
    private JPanel searchPanel;
    private JTextArea jsonTxtArea;
    private JTree treeTest;
    private JButton saveAndExitFileButton;
    private JPanel jsonFileWall;
    private JList<String> variableList;
    private JList<String> typeList;
    private JLabel currentFileText;
    private JCheckBox hideTreeCheckBox;
    private JPanel treePanel;
    private JButton testfileButton;
    private JTextField changeField;
    private JButton changeButton;
    private JButton clearAllSelectsButton;
    private JRadioButton changeOpt;
    private JRadioButton addOpt;
    private JRadioButton variableOpt;
    private JRadioButton typeOpt;
    private JLabel errorEditor;
    private JCheckBox changeParametersCheckBox;
    private JPanel changeparameterPanel;
    private JButton hideAllPanelsButton;
    private JPanel insertvariablesPanel;
    private JCheckBox insertvariablesCheckBox;
    private JPanel changeallvariablesvaluesPanel;
    private JCheckBox changeallvariablesvaluesCheckBox;
    private JPanel removeinstancesofvariablePanel;
    private JCheckBox removeinstancesofvariableCheckBox;
    private JTextField insertVariableField;
    private JRadioButton addtoallOpt;
    private JRadioButton chooseeachOpt;
    private JButton insertValueButton;
    private JButton undoChangeButton;
    private JButton revertAllChangesButton;
    private JPanel editorWindowTopPanel;
    private JLabel successEditorText;
    private JTextField intancevalueField;
    private JRadioButton removeallOpt;
    private JRadioButton removeinstancewithvalueOpt;
    private JButton removeButton;
    File currentFile;
    boolean processSearch = false;
    String jsonDocument = "";
    String originalJsonFileContents;
    String changedJsonFileContents;

    public JSONFixer(String title){
        super(title);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(mainpanel);
        this.pack();
        this.setSize(600, 400);
        VisibleJSONWall(false);
        HideAllPanels();

        variableList.setModel(variablesListModel);
        typeList.setModel(varTypesListModel);

        errorText.setText("");
        errorEditor.setText("");
        successEditorText.setVisible(false);

        testfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                docpathField.setText("D:\\Programmering\\Tools For Constructions TFC\\src\\main\\java\\data\\");
                docnameField.setText("someInfo.json");
            }
        });

        jsonTxtArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                TextUpdateCheck();
            }
        });

        revertAllChangesButton.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                int deleteAnswer = WarningPane.showConfirmDialog(
                        mainpanel,
                        "Are you sure you want to undo all changes",
                        "Revert file",
                        JOptionPane.WARNING_MESSAGE,
                        JOptionPane.YES_NO_OPTION);
                System.out.println(deleteAnswer);
                if (deleteAnswer == 0) {
                    revertChange.clear();
                    changedJsonFileContents = originalJsonFileContents;
                    jsonTxtArea.setText(originalJsonFileContents);
                    jsonTxtArea.updateUI();
                } else {

                }
            }
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!newFileIsActive) {
                    newFileIsActive = true;
                    //StartTimer();
                    int lastLetter = docpathField.getText().length() - 1;
                    if (docpathField.getText().endsWith("\\")) {
                        jsonDocument = docpathField.getText() + docnameField.getText();
                    } else {
                        docpathField.setText(docpathField.getText() + "\\");
                        jsonDocument = docpathField.getText() + docnameField.getText();
                    }
                    FindFile();
                    searchButton.setEnabled(false);
                }
            }
        });

        hideTreeCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                treeTest.setVisible(!hideTreeCheckBox.isSelected());
            }
        });

        saveAndExitFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveAndExitFile();
            }
        });

        variableList.addListSelectionListener(new ListSelectionListener() { @Override
        public void valueChanged(ListSelectionEvent e) {
            typeList.clearSelection();
        } });
        typeList.addListSelectionListener(new ListSelectionListener() { @Override
        public void valueChanged(ListSelectionEvent e) {
            variableList.clearSelection();
        } });


        //FORM OPTIONS
        clearAllSelectsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClearSelects();
            }
        });

        hideAllPanelsButton.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                HideAllPanels();
            } });

        changeOpt.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) { changeOpt.setSelected(true); addOpt.setSelected(false); } });
        addOpt.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) { changeOpt.setSelected(false); addOpt.setSelected(true); } });
        variableOpt.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) { variableOpt.setSelected(true); typeOpt.setSelected(false); } });
        typeOpt.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) { variableOpt.setSelected(false); typeOpt.setSelected(true); } });


        addtoallOpt.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) { addtoallOpt.setSelected(true); chooseeachOpt.setSelected(false); } });
        chooseeachOpt.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) { addtoallOpt.setSelected(false); chooseeachOpt.setSelected(true); } });
        removeallOpt.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) { removeallOpt.setSelected(true); removeinstancewithvalueOpt.setSelected(false); } });
        removeinstancewithvalueOpt.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) { removeallOpt.setSelected(false); removeinstancewithvalueOpt.setSelected(true); } });

        changeButton.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                EditorCommitAction(1);
                successEditorText.setVisible(false);
            } });

        changeField.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                EditorCommitAction(1);

            } });

        removeButton.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                RemoveInstances();
            } });

        //DO BOXES
        changeParametersCheckBox.addChangeListener(new ChangeListener() { @Override
        public void stateChanged(ChangeEvent e) {
            changeparameterPanel.setVisible(changeParametersCheckBox.isSelected()); } });
        insertvariablesCheckBox.addChangeListener(new ChangeListener() { @Override
        public void stateChanged(ChangeEvent e) {
            insertvariablesPanel.setVisible(insertvariablesCheckBox.isSelected()); } });
        changeallvariablesvaluesCheckBox.addChangeListener(new ChangeListener() { @Override
        public void stateChanged(ChangeEvent e) {
            changeallvariablesvaluesPanel.setVisible(changeallvariablesvaluesCheckBox.isSelected()); } });
        removeinstancesofvariableCheckBox.addChangeListener(new ChangeListener() { @Override
        public void stateChanged(ChangeEvent e) {
            removeinstancesofvariablePanel.setVisible(removeinstancesofvariableCheckBox.isSelected()); } });


        insertValueButton.addActionListener(new ActionListener() { @Override
        public void actionPerformed(ActionEvent e) {
            EditorCommitAction(2);
            successEditorText.setVisible(false);
            errorEditor.setText("");

        } });
        insertVariableField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorCommitAction(2);
            }
        });


        //////////////////////////////////////////////////////////////////////////////////¤################

    }

    void FindFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(jsonDocument)))){
            if (!jsonDocument.contains(".json")) {
                errorText.setText("Not a json file");
                filepathLabel.setText("Incorrect Format");
                filepathLabel.setBackground(red);
                filenameLabel.setBackground(red);
                return;
            }
            errorText.setText("");
            filenameLabel.setText(docnameField.getText());
            filepathLabel.setBackground(blue);
            filenameLabel.setBackground(blue);
            currentFileText.setText("Current File: " + docnameField.getText());
            currentFile = new File(jsonDocument);
            ActiveFile(currentFile);
            VisibleJSONWall(true);

        } catch (FileNotFoundException e) {
            errorText.setText("File not found");
            filepathLabel.setText("Not Found");
            filepathLabel.setBackground(red);
            filenameLabel.setBackground(red);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void ActiveFile(File file) {

        String fromFile = "";
        String jsonInFile = "";
        setSize(1200, 500);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentFile))) {
            ArrayList<String> foundVariables = new ArrayList<>();
            ArrayList<String> foundVariableTypes = new ArrayList<>();
            String forCodeBlock = "";
            codeBlocksFromFile.add("[");
            boolean codeBlockInMaking = false;
            while ((fromFile = bufferedReader.readLine()) != null) {
                jsonInFile = jsonInFile + "\n" + fromFile;
                if (fromFile.contains("{")) {
                    codeBlockInMaking = true;
                    codeBlocksFromFile.add(fromFile);
                } else if (fromFile.contains("}")) {
                    codeBlockInMaking = false;
                    codeBlocksFromFile.add(forCodeBlock);
                    forCodeBlock = "";
                    codeBlocksFromFile.add(fromFile);
                } else if (codeBlockInMaking){
                    if (forCodeBlock.isEmpty()) {
                        forCodeBlock = fromFile;
                    } else {
                        forCodeBlock = forCodeBlock + "\n" + fromFile;
                    }
                }
                try {
                    if (fromFile.contains(":")) {
                        String[] split1 = fromFile.split(":");
                        String[] betweenQuotes = split1[0].split("\"");
                        String variable = betweenQuotes[1];
                        if (!foundVariables.contains(variable)) {
                            foundVariables.add(variable);
                            betweenQuotes = split1[1].split("\"");
                            if (betweenQuotes.length == 1) {
                                if (betweenQuotes[0].contains("null")) {
                                    foundVariableTypes.add("null");
                                } else if (betweenQuotes[0].contains(".")) {
                                    foundVariableTypes.add("float");
                                } else
                                    foundVariableTypes.add("int");
                            } else {
                                foundVariableTypes.add("string");
                            }
                        }

                    }

                } catch (ArrayIndexOutOfBoundsException n) {
                    System.err.println("Error");
                }
            } /*
            codeBlocksFromFile.add("]");
            for (String c : codeBlocksFromFile) {
                System.out.println(c);
            }*/
            int index = 0;
            for (String s : foundVariables) {
                if (!variablesListModel.contains(s)) {
                    variablesListModel.addElement(s);
                    varTypesListModel.addElement(foundVariableTypes.get(index));
                }
                index += 1;
            }

            variableList.updateUI();
            typeList.updateUI();
            originalJsonFileContents = jsonInFile;
            changedJsonFileContents = jsonInFile;

            variablesListModel.equals(foundVariables);
            varTypesListModel.equals(foundVariableTypes);
            variableList.updateUI();
            typeList.updateUI();
            jsonTxtArea.setText(jsonInFile);
            jsonTxtArea.updateUI();


        } catch (FileNotFoundException e) {
            errorText.setText("Error loading file, exiting");
            filepathLabel.setText("Not Found");
            filepathLabel.setBackground(red);
            filenameLabel.setBackground(red);
            searchButton.setEnabled(true);
            VisibleJSONWall(false);
        } catch (IOException e) {
            e.printStackTrace();
            searchButton.setEnabled(true);
        }
    }

    void SaveAndExitFile() {
        VisibleJSONWall(false);
        currentFile = null;
        this.setSize(600, 400);
        newFileIsActive = false;
        searchButton.setEnabled(true);
    }

    void StartTimer() {
        if (!processSearch) {
            processSearch = true;
            try {
                int value = 0;
                while (value <= 100) {
                    progressBar.updateUI();
                    Thread.sleep(50);
                    System.out.println("PROGRESS");
                    System.out.println(value);
                    progressBar.setValue(value);
                    progressP.setText("Progress: " + progressBar.getValue() + "%");
                    value += 1;
                }
            } catch (InterruptedException n) {}
        }
    }

    void VisibleJSONWall(boolean visible) {
        treePanel.setVisible(visible);
        editorWindowTopPanel.setVisible(visible);
        jsonFileWall.setVisible(visible);
    }

    void EditorCommitAction(int action) {
        if (action == 1) {
            try {
                errorEditor.setText("");
                if (changeOpt.isSelected()) { //CHANGE PARAMETER

                    if (variableOpt.isSelected()) {
                        if (changeField.getText().isEmpty()) {
                            int indToRemove = variableList.getSelectedIndex();
                            varTypesListModel.remove(indToRemove);
                            variablesListModel.remove(indToRemove);
                        } else {
                            variablesListModel.set(variableList.getSelectedIndex(), changeField.getText());
                            changeField.setText("");
                        }
                    } else if (typeOpt.isSelected()) {
                        String vb = changeField.getText();
                        vb.toLowerCase();
                        if (vb.equals("string") || vb.equals("null") || vb.equals("int") || vb.equals("float") || vb.equals("double") || vb.equals("boolean")) {
                            varTypesListModel.set(typeList.getSelectedIndex(), changeField.getText());
                            changeField.setText("");
                        } else if (changeField.getText().isEmpty()) {
                            varTypesListModel.set(typeList.getSelectedIndex(), "null");
                            changeField.setText("");
                        } else {
                            errorEditor.setText("This is not a variable type");
                        }
                    }
                } else if (addOpt.isSelected()) { //ADD PARAMETER

                    if (variableOpt.isSelected()) {
                        String vb = changeField.getText();
                        if (variablesListModel.contains(vb)) {
                            errorEditor.setText("Variable already exists");
                        } else if (vb.equals("string") || vb.equals("null") || vb.equals("int") || vb.equals("float") || vb.equals("double") || vb.equals("boolean")) {
                            errorEditor.setText("Varibale name cannot contain a 'type' name");
                        }else {
                            if (vb.contains(",")) {
                                String[] detect = vb.split(",");
                                String changeNew = ValidateString(detect[0]);
                                variablesListModel.addElement((changeNew));
                                vb = detect[1];
                                if (vb.equals("string") || vb.equals("null") || vb.equals("int") || vb.equals("float") || vb.equals("double") || vb.equals("boolean")) {
                                    varTypesListModel.addElement((vb));
                                    changeField.setText("");
                                } else {
                                    varTypesListModel.addElement(("null"));
                                    changeField.setText("");
                                }
                            } else {
                                String changeNew = ValidateString(changeField.getText());
                                variablesListModel.addElement((changeNew));
                                varTypesListModel.addElement(("null"));
                                changeField.setText("");
                            }
                        }
                    } else if (typeOpt.isSelected()) {
                        errorEditor.setText("A variable must exist to add a type (write 'varname,type' to instantly give type)");
                    }
                }
                variableList.updateUI();
                typeList.updateUI();
            } catch (ArrayIndexOutOfBoundsException n) {
                errorEditor.setText("Please select what to change");
            }
        } else if (action == 2) {
            try {
                String variable = variableList.getSelectedValue();
                int indx = variableList.getSelectedIndex();
                String jsonInFile = "";
                String typeForValue = varTypesListModel.get(indx);
                String givenValue = insertVariableField.getText();
                if (addtoallOpt.isSelected()) {
                    for (String s : codeBlocksFromFile) {
                        if (s.contains(":")) {
                            String[] indents = s.split("\""); //Find indentations
                            String customString = indents[0] + "\"" + variable + "\"";
                            if (CheckType(typeForValue) == 1) {
                                customString = customString + ": " + givenValue;
                            } else if (CheckType(typeForValue) == 2) {
                                customString = customString + ": " + Integer.parseInt(givenValue);
                            } else if (CheckType(typeForValue) == 3) {
                                customString = customString + ": " + Float.parseFloat(givenValue);
                            } else if (CheckType(typeForValue) == 4) {
                                customString = customString + ": " + Boolean.parseBoolean(givenValue);
                            }
                            s = s + "," + "\n" + customString;
                        }
                        if (jsonInFile.isEmpty()) {
                            jsonInFile = s;
                        } else {
                            jsonInFile = jsonInFile + "\n" + s;
                        }
                    }
                    System.out.println("THIS IS HAOOED:");
                    System.out.println(jsonInFile);
                    jsonTxtArea.setText(jsonInFile);
                    jsonTxtArea.updateUI();

                } else if (chooseeachOpt.isSelected()) {
                    String regex = "*VALUE*";
                    try {
                        System.out.println("INDX "+ blockChangeIndex);
                        boolean chosen = false;
                        int pastIndex = blockChangeIndex;
                        if (blockChangeIndex != 0 && (givenValue.equals(null) || givenValue.equals(""))) {
                            errorEditor.setText("You must give a value");
                        } else {
                            int index = 0;
                            if (blockChangeIndex == 0) { //Create variable in each block
                                ArrayList<String> newCodeBlock = new ArrayList<>();
                                for (String s : codeBlocksFromFile) {
                                    if (s.contains(":")) { //FIND NEW LINE TO CHANGE
                                        s = s + ",\n" + regex;
                                    }
                                    newCodeBlock.add(s);
                                    if (jsonInFile.isEmpty()) {
                                        jsonInFile = s;
                                    } else {
                                        jsonInFile = jsonInFile + "\n" + s;
                                    }
                                }
                                codeBlocksFromFile = newCodeBlock;
                                blockChangeIndex += 1;
                            } else {
                                ArrayList<String> newCodeBlock = new ArrayList<>();
                                String newLine = "";
                                System.out.println(codeBlocksFromFile);
                                for (String s : codeBlocksFromFile) {
                                    if (!chosen) { //Change old line
                                        if (s.contains(":") && s.contains(regex)) {
                                            newLine = s;
                                            chosen = true;
                                            blockChangeIndex = index;
                                            String[] indents = s.split("\""); //Find indentations
                                            String customString = indents[0] + "\"" + variable + "\":" + "*VALUE*";
                                            int whatType = CheckType(typeForValue);
                                            if (whatType == 1) {
                                                //givenValue = givenValue;
                                            } else if (whatType == 2) {
                                                givenValue = Integer.parseInt(givenValue) + "";
                                            } else if (whatType == 3) {
                                                givenValue = Float.parseFloat(givenValue) + "";
                                            } else if (whatType == 4) {
                                                givenValue = Boolean.parseBoolean(givenValue) + "";
                                            }
                                            newLine = newLine.replace(regex, customString.replace(regex, givenValue));
                                        } else {
                                            newLine = s;
                                        }
                                    }
                                    index += 1;
                                    if (jsonInFile.isEmpty()) {
                                        jsonInFile = s;
                                    } else {
                                        jsonInFile = jsonInFile + "\n" + s;
                                    }
                                    newCodeBlock.add(newLine);
                                }
                                System.out.println(index);
                                System.out.println(pastIndex);
                                codeBlocksFromFile = newCodeBlock;
                            }
                            if (codeBlocksFromFile.get(codeBlocksFromFile.size() - 1) != "]") {
                                codeBlocksFromFile.add("]");
                            }
                            insertVariableField.setText("");
                            jsonTxtArea.setText(jsonInFile);
                            jsonTxtArea.updateUI();
                            //System.out.println(jsonTxtArea.getText());
                            if (blockChangeIndex == pastIndex && blockChangeIndex != 0) {
                                System.out.println("Its not supposed to be");
                                blockChangeIndex = 0;
                                successEditorText.setVisible(true);
                                ClearSelects();
                            }
                        }
                    } catch (NullPointerException n) {
                        errorEditor.setText("Please choose a variable to add first");
                    }

                } else {
                    errorEditor.setText("Choose an option before inserting variable");
                }
            } catch (NullPointerException n) {
                errorEditor.setText("Choose a variable from the list to proceed");
            } catch (ArrayIndexOutOfBoundsException n) {
                errorEditor.setText("List has no type");
            } catch (NumberFormatException n) {
                errorEditor.setText("Input is wrong format, check for discrepancies");
            }
        }
    }
    void RemoveInstances() {
        try {
            if (removeallOpt.isSelected()) {
                int index = variableList.getSelectedIndex();
                String varToRemove = variableList.getSelectedValue();
                ArrayList<String> newCodeBlock = new ArrayList<>();
                String newLine = "";
                for (String s: codeBlocksFromFile) {
                    if (s.contains(varToRemove)) {
                        String[] split1 = s.split(varToRemove);
                        newLine = split1[0];
                        if (newLine.endsWith(",")) {
                            //newLine = newLine.replace(newLine.charAt(newLine.length() - 1), "");
                        }
                    } else {
                        newLine = s;
                    }
                    newCodeBlock.add(newLine);
                }
                codeBlocksFromFile = newCodeBlock;
                jsonTxtArea.setText("");
                for (String s: codeBlocksFromFile) {
                    jsonTxtArea.setText(jsonTxtArea.getText() + "\n" + s);
                }
                variableList.remove(index);
                typeList.remove(index);
                variableList.updateUI();
                typeList.updateUI();
            } else if (removeinstancewithvalueOpt.isSelected()) {

            } else {
                errorEditor.setText("Please choose an option");
            }
        } catch (NullPointerException n) {
            errorEditor.setText("Choose a variable");
        } catch (ArrayIndexOutOfBoundsException n) {
            errorEditor.setText("Error");
        }
    }

    int CheckType(String value) {
        int returnType = 0;
        if (value.equals("string")) {
            returnType = 1;
        } else if (value.equals("int")) {
            returnType = 2;
        } else if (value.equals("float")) {
            returnType = 3;
        } else if (value.equals("boolean")) {
            returnType = 4;
        }
        return returnType;
    }

    void TextUpdateCheck() {
        if (revertChange.isEmpty()) {
            if (!changedJsonFileContents.equals(originalJsonFileContents)) {
                try (BufferedWriter bf = new BufferedWriter(new FileWriter(draftFile))){
                    bf.write(changedJsonFileContents);
                } catch (ArrayIndexOutOfBoundsException n) {
                    System.err.println("Error");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException n) {
                    System.out.println("Error");
                }

            }
        }
    }

    String ValidateString(String nm) {
        String regexx = "^[a-z-A-Z-0-9]+";
        String newsent = "";
        for (int i=0; i < nm.length(); i++) {
            Character c = nm.charAt(i);
            String test = "" + c;
            if (test.contains("-")) {
            } else if (test.matches(regexx)) {
                newsent = newsent + test;
            }
            else if (test.contains("_")){
                newsent = newsent + test;
            }

        }
        return newsent;
    }

    void HideAllPanels() {
        changeparameterPanel.setVisible(false);
        changeParametersCheckBox.setSelected(false);
        insertvariablesPanel.setVisible(false);
        insertvariablesCheckBox.setSelected(false);
        changeallvariablesvaluesPanel.setVisible(false);
        changeParametersCheckBox.setSelected(false);
        removeinstancesofvariablePanel.setVisible(false);
        removeinstancesofvariableCheckBox.setSelected(false);
    }

    void ClearSelects() {
        variableList.clearSelection();
        typeList.clearSelection();
        changeOpt.setSelected(false);
        addOpt.setSelected(false);
        variableOpt.setSelected(false);
        typeOpt.setSelected(false);
        removeallOpt.setSelected(false);
        removeinstancewithvalueOpt.setSelected(false);
        changeField.setText("");
        insertVariableField.setText("");
        insertVariableField.setVisible(false);
    }

    /*IDEAS
    * Add and change variables and types
    * insert a variable at every element with x value
    * Change all of a variable to given value
    * remove all variables of given name*/
}

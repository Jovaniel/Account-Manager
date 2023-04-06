package com.jova.Controller;
import com.jova.AES256;
import com.jova.View.MainPanelClass;
import com.jova.Model.Model;
import password.generator.data.Wordlist;
import password.generator.generators.PassphraseGenerator;
import password.generator.generators.PasswordGenerator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Controller {
    private MainPanelClass mainPanelClass;
    private Model model;
    String[] columnNames = {"Id", "Title", "Email", "Password"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    int row = 0;
    int selectedRow;
    int selectedRowId;
    PassphraseGenerator.Cases cases;
    private boolean passphraseUpperCase, passphraseLowerCase, passphraseTitleCase, includeUpperCase, includeLowerCase, includeSymbols, includeNumbers, wasEditAccountVisible;

    //The line of code below belongs to the AccountManager.jar file. If you are going to create a .jar executable, please use this line of code.
//     Wordlist w = Wordlist.instantiate(new File(String.format("eff_long_wordlist.txt", File.separator)));

    //The line of code below belongs to the AccountManager file. Use it if you are going to make some changes in the script, that way the program won't throw any errors.
    Wordlist w = Wordlist.instantiate(new File("src/main/resources/eff_long_wordlist.txt"));

    public Controller(MainPanelClass mainPanelClass, Model model){
        this.mainPanelClass = mainPanelClass;
        this.model = model;

        if(model.existeRutaBaseDatos()){
            mainPanelClass.setVisible(true);
            mainPanelClass.getCreateDatabasePanel().setVisible(false);
            mainPanelClass.getLoginPanel().setVisible(true);
        }else{
            mainPanelClass.getMainPanel().setVisible(true);
            mainPanelClass.getCreateDatabasePanel().setVisible(true);
        }

        mainPanelClass.newAccountCheckBoxListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                char[] passwordChars = mainPanelClass.getAddNewAccountPassField().getPassword();
                String password = new String(passwordChars);
                if(mainPanelClass.getNewAccountCheckBox().isSelected()){
                    mainPanelClass.getNewAccountShowPassTxtField().setText(password);
                }else{
                    mainPanelClass.getNewAccountShowPassTxtField().setText("");
                }
            }
        });

        mainPanelClass.editAccountCheckBoxListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                char[] getPassword = mainPanelClass.getEditAccountPassTxtField().getPassword();
                String password = new String(getPassword);
                if(mainPanelClass.getEditAccountCheckBox().isSelected()){
                    mainPanelClass.getEditAccountShowPassTxtField().setText(password);
                }else {
                    mainPanelClass.getEditAccountShowPassTxtField().setText("");
                }
            }
        });

        mainPanelClass.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    row = mainPanelClass.getAccountsTable().getSelectedRow();
                    Object idObj = tableModel.getValueAt(row, 0);
                    int id = Integer.parseInt(idObj.toString());
                    String title;
                    String email;
                    String password;
                    try {
                        title = model.getTitleFromDB(id);
                        email = model.getEmailFromDB(id);
                        password = model.getPasswordFromDB(id);
                        String pass = AES256.decrypt(password, model.getMasterKey());
                        mainPanelClass.getAccountsTablePanel().setVisible(false);
                        mainPanelClass.getEditAccountPanel().setVisible(true);

                        mainPanelClass.getEditAccountTitleTxtField().setText(title);
                        mainPanelClass.getEditAccountEmailTxtField().setText(email);
                        mainPanelClass.getEditAccountPassTxtField().setText(pass);

                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    mainPanelClass.getAccountsTablePanel().setVisible(false);
                    mainPanelClass.getEditAccountPanel().setVisible(true);
                }
            }
        });

        mainPanelClass.getEditAccountApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object idObj = tableModel.getValueAt(row, 0);
                int id = Integer.parseInt(idObj.toString());
                try {
                    String title = mainPanelClass.getEditAccountTitleTxtField().getText();
                    String email = mainPanelClass.getEditAccountEmailTxtField().getText();
                    char[] passChar = mainPanelClass.getEditAccountPassTxtField().getPassword();
                    String password = new String(passChar);

                    if(!(title.isEmpty() && email.isEmpty() && password.isEmpty())) {
                        tableModel.setValueAt(title, row, 1);
                        tableModel.setValueAt(email, row, 2);
                        tableModel.setValueAt(password, row, 3);

                        model.updateDataOfDB(id, title, email, password);

                        mainPanelClass.getEditAccountPanel().setVisible(false);
                        mainPanelClass.getAccountsTablePanel().setVisible(true);
                    }else{
                        JOptionPane.showMessageDialog(mainPanelClass.getMainPanel(), "Please. Insert a title, email and password");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        mainPanelClass.getEditAccountCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanelClass.getEditAccountPanel().setVisible(false);
                mainPanelClass.getAccountsTablePanel().setVisible(true);
            }
        });

        mainPanelClass.deleteSelectedAccountButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!(mainPanelClass.getAccountsTable().getRowCount() == 0) && !(mainPanelClass.getAccountsTable().getSelectedRow() <= -1)){
                    try {
                        int conf = JOptionPane.showConfirmDialog(mainPanelClass.getMainPanel(), "Are you sure you want to delete this account?");
                        if(!(conf == 0)) {
                            mainPanelClass.getAccountsTablePanel().setVisible(true);
                        }else{
                            if(!model.isDbEmpty()) {
                                selectedRow = mainPanelClass.getAccountsTable().getSelectedRow();
                                Object idObj = tableModel.getValueAt(selectedRow, 0);
                                selectedRowId = Integer.parseInt(idObj.toString());
                                tableModel.removeRow(selectedRow);
                                model.deleteDataOfDB(selectedRowId);
                            }
                        }
                    } catch (SQLException | NoSuchAlgorithmException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        mainPanelClass.addNewAccountButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanelClass.getAddNewAccountTitleTxtField().setText("");
                mainPanelClass.getAddNewAccountEmailTxtField().setText("");
                mainPanelClass.getAddNewAccountPassField().setText("");
                mainPanelClass.getAccountsTablePanel().setVisible(false);
                mainPanelClass.getAddNewAccountPanel().setVisible(true);
            }
        });

        mainPanelClass.newAccountApplyButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = mainPanelClass.getAddNewAccountTitleTxtField().getText();
                String email = mainPanelClass.getAddNewAccountEmailTxtField().getText();
                char[] getPassword = mainPanelClass.getAddNewAccountPassField().getPassword();
                String password = new String(getPassword);
                try {
                    if(!(title.isEmpty() && email.isEmpty() && password.isEmpty())) {
                        model.insertDataInDB(title, email, password, mainPanelClass.getAccountsTable(), tableModel);
                        mainPanelClass.getAddNewAccountPanel().setVisible(false);
                        mainPanelClass.getAccountsTablePanel().setVisible(true);
                    }else{
                        JOptionPane.showMessageDialog(mainPanelClass.getMainPanel(), "Please. Insert a title, email and password");
                        mainPanelClass.getAccountsTablePanel().setVisible(false);
                        mainPanelClass.getAddNewAccountPanel().setVisible(true);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        mainPanelClass.newAccountCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanelClass.getAccountsTablePanel().setVisible(true);
                mainPanelClass.getAddNewAccountPanel().setVisible(false);
            }
        });

        mainPanelClass.cdbCreateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanelClass.getCreateDatabasePanel().setVisible(false);
                model.crearBaseDatos();

                if(!model.isFileChooserCanceled()){
                    mainPanelClass.getCreateDatabasePanel().setVisible(false);
                    mainPanelClass.getInsertDbPasswordPanel().setVisible(true);
                }
            }
        });

        mainPanelClass.insertDbPasswordNextButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] getPassword = mainPanelClass.getInsertDbPassword().getPassword();
                String password = new String(getPassword);
                char[] getConfirmPassword = mainPanelClass.getInsertDbConfirmPassword().getPassword();
                String confirmPassword = new String(getConfirmPassword);
                    if(password.equals(confirmPassword) && !password.isEmpty()){
                        mainPanelClass.getInsertDbPasswordPanel().setVisible(false);
                        mainPanelClass.getAccountsTablePanel().setVisible(true);
                        try {
                            model.setMasterKey(password);
                            model.insertKeyAndSaltInDB(password);
                            mainPanelClass.getAccountsTable().setVisible(true);
                            model.scanDatabase(mainPanelClass.getAccountsTable(), tableModel);
                            tableDefaultSettings();
                            setButtonGroup();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }else{
                        if(password.isEmpty()){
                            JOptionPane.showMessageDialog(mainPanelClass.getMainPanel(), "Please, insert the password.");
                        }else {
                            JOptionPane.showMessageDialog(mainPanelClass.getMainPanel(), "Both passwords are not the same. Try again.");
                        }
                    }
                }
        });

        mainPanelClass.loginNextButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] password = mainPanelClass.getLoginPasswordField().getPassword();
                String passwordStr = new String(password);
                try {
                    String salt = model.getSaltFromDb();
                    byte[] saltByte = model.hexStringToBytes(salt);
                    String hashPass = model.hashWithSalt(passwordStr, saltByte);
                    String checkPassFromDB = model.getKeyHashFromDB();
                    if(hashPass.equals(checkPassFromDB)) {
                        String newKey = model.hashWithSalt(passwordStr, model.getSalt());
                        String newSalt = model.getNewSalt();
                        model.updateKeyAndSaltInDB(newKey, newSalt);
                        model.setMasterKey(passwordStr);
                        model.scanDatabase(mainPanelClass.getAccountsTable(), tableModel);
                        tableDefaultSettings();
                        setButtonGroup();
                        mainPanelClass.getLoginPanel().setVisible(false);
                        mainPanelClass.getAccountsTablePanel().setVisible(true);
                        mainPanelClass.getAccountsTable().setVisible(true);
                    }else{
                        JOptionPane.showMessageDialog(mainPanelClass.getMainPanel(), "Incorrect Password. Try Again.");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        /*---------------------------------Generate Listeners---------------------------------------------*/

        mainPanelClass.newAccountGenerateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanelClass.getAddNewAccountPanel().setVisible(false);
                mainPanelClass.getPasswordGeneratorPanel().setVisible(true);
                wasEditAccountVisible = false;
            }
        });

        mainPanelClass.editAccountGenerateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanelClass.getEditAccountPanel().setVisible(false);
                mainPanelClass.getPasswordGeneratorPanel().setVisible(true);
                wasEditAccountVisible = true;
            }
        });

        mainPanelClass.passphraseButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(wasEditAccountVisible){
                    mainPanelClass.getEditAccountPanel().setVisible(false);
                }else{
                    mainPanelClass.getAddNewAccountPanel().setVisible(false);
                }
                mainPanelClass.getPasswordGeneratorPanel().setVisible(false);
                mainPanelClass.getPassphrasePanel().setVisible(true);
            }
        });

        mainPanelClass.passphraseUpperRadioButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passphraseUpperCase = mainPanelClass.getPassphraseUpperRadioButton().isSelected();
                if(passphraseUpperCase){
                    cases = PassphraseGenerator.Cases.UPPERCASE;
                }
            }
        });

        mainPanelClass.passphraseLowerRadioButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passphraseLowerCase = mainPanelClass.getPassphraseLowerRadioButton().isSelected();
                if(passphraseLowerCase){
                    cases = PassphraseGenerator.Cases.LOWERCASE;
                }
            }
        });

        mainPanelClass.passphraseTitleRadioButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passphraseTitleCase = mainPanelClass.getPassphraseTitleRadioButton().isSelected();
                if(passphraseTitleCase){
                    cases = PassphraseGenerator.Cases.TITLE_CASE;
                }
            }
        });

        mainPanelClass.randomPasswordButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(wasEditAccountVisible){
                    mainPanelClass.getEditAccountPanel().setVisible(false);
                }else{
                    mainPanelClass.getAddNewAccountPanel().setVisible(false);
                }
                mainPanelClass.getPasswordGeneratorPanel().setVisible(false);
                mainPanelClass.getRandomPasswordPanel().setVisible(true);
            }
        });

        mainPanelClass.includeUppercaseCheckboxListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                includeUpperCase = mainPanelClass.getIncludeUppercaseCheckbox().isSelected();
            }
        });

        mainPanelClass.includeLowercaseCheckboxListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                includeLowerCase = mainPanelClass.getIncludeLowercaseCheckbox().isSelected();
            }
        });

        mainPanelClass.includeNumbersCheckboxListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                includeNumbers = mainPanelClass.getIncludeNumbersCheckbox().isSelected();
            }
        });

        mainPanelClass.includeSymbolsCheckboxListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                includeSymbols = mainPanelClass.getIncludeSymbolsCheckbox().isSelected();
            }
        });

        mainPanelClass.randomPassGenerateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    int length = Integer.parseInt(mainPanelClass.getRandomPasswordLengthTxtField().getText());
                    String randomPass = setRandomPassword(length, includeUpperCase, includeLowerCase, includeSymbols, includeNumbers);
                    mainPanelClass.getRandomPassShowGeneratedPassTxtField().setText(randomPass);
            }
        });

        mainPanelClass.passphraseGenerateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    int num = Integer.parseInt(mainPanelClass.getPassphraseNumberOfWordsTxtField().getText());
                    String separator = mainPanelClass.getPassphraseWordSeparatorTxtField().getText();
                    String passphrase = setPassphrase(num, separator, cases);
                    mainPanelClass.getPassphraseShowGeneratedPassTxtField().setText(passphrase);
            }
        });

        mainPanelClass.passphraseApplyButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  String passphrase = mainPanelClass.getPassphraseShowGeneratedPassTxtField().getText();
                  if(!passphrase.isEmpty()) {
                      if (wasEditAccountVisible) {
                          mainPanelClass.getEditAccountPassTxtField().setText(passphrase);
                          mainPanelClass.getEditAccountShowPassTxtField().setText(passphrase);
                          mainPanelClass.getEditAccountCheckBox().setSelected(true);
                          mainPanelClass.getAddNewAccountPanel().setVisible(false);
                          mainPanelClass.getEditAccountPanel().setVisible(true);
                      } else {
                          mainPanelClass.getAddNewAccountPassField().setText(passphrase);
                          mainPanelClass.getNewAccountShowPassTxtField().setText(passphrase);
                          mainPanelClass.getNewAccountCheckBox().setSelected(true);
                          mainPanelClass.getEditAccountPanel().setVisible(false);
                          mainPanelClass.getAddNewAccountPanel().setVisible(true);
                      }
                      mainPanelClass.getPassphrasePanel().setVisible(false);
                  }else{
                      JOptionPane.showMessageDialog(mainPanelClass.getMainPanel(), "Please, generate a password before applying.");
                  }
            }
        });

        mainPanelClass.randomPassApplyButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String randomPass = mainPanelClass.getRandomPassShowGeneratedPassTxtField().getText();
                if(!randomPass.isEmpty()) {
                    if (wasEditAccountVisible) {
                        mainPanelClass.getEditAccountPassTxtField().setText(randomPass);
                        mainPanelClass.getEditAccountShowPassTxtField().setText(randomPass);
                        mainPanelClass.getEditAccountCheckBox().setSelected(true);
                        mainPanelClass.getAddNewAccountPanel().setVisible(false);
                        mainPanelClass.getEditAccountPanel().setVisible(true);
                    } else {
                        mainPanelClass.getAddNewAccountPassField().setText(randomPass);
                        mainPanelClass.getNewAccountShowPassTxtField().setText(randomPass);
                        mainPanelClass.getNewAccountCheckBox().setSelected(true);
                        mainPanelClass.getEditAccountPanel().setVisible(false);
                        mainPanelClass.getAddNewAccountPanel().setVisible(true);
                    }
                    mainPanelClass.getRandomPasswordPanel().setVisible(false);
                }else{
                    JOptionPane.showMessageDialog(mainPanelClass.getMainPanel(), "Please, generate a password before applying.");
                }
            }
        });

        mainPanelClass.passphraseCancelButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(wasEditAccountVisible){
                    mainPanelClass.getEditAccountPanel().setVisible(true);
                }else{
                    mainPanelClass.getAddNewAccountPanel().setVisible(true);
                }
                mainPanelClass.getPassphrasePanel().setVisible(false);
            }
        });

        mainPanelClass.randomPassCancelButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(wasEditAccountVisible){
                    mainPanelClass.getEditAccountPanel().setVisible(true);
                }else{
                    mainPanelClass.getAddNewAccountPanel().setVisible(true);
                }
                mainPanelClass.getRandomPasswordPanel().setVisible(false);
            }
        });

    }

    private String setPassphrase(int numWords, String wordSeparator, PassphraseGenerator.Cases cases){
        return PassphraseGenerator.getPassphrase(w, numWords, wordSeparator, cases);
    }

    private String setRandomPassword(int length, boolean includeUpperCase, boolean includeLowerCase, boolean includeSymbols, boolean includeNumbers){
        return PasswordGenerator.getPassword(length, includeUpperCase, includeLowerCase, includeSymbols, includeNumbers);
    }

    private void setButtonGroup(){
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(mainPanelClass.getPassphraseUpperRadioButton());
        buttonGroup.add(mainPanelClass.getPassphraseLowerRadioButton());
        buttonGroup.add(mainPanelClass.getPassphraseTitleRadioButton());
    }

    private void tableDefaultSettings(){
        mainPanelClass.getAccountsTable().setDefaultEditor(Object.class, null); //Disables rowTextEditor
        TableColumn idCol = mainPanelClass.getAccountsTable().getColumnModel().getColumn(0);
        TableColumn titleCol = mainPanelClass.getAccountsTable().getColumnModel().getColumn(1);
        TableColumn emailCol = mainPanelClass.getAccountsTable().getColumnModel().getColumn(2);
        TableColumn passCol = mainPanelClass.getAccountsTable().getColumnModel().getColumn(3);
        idCol.setPreferredWidth(1);
        titleCol.setPreferredWidth(90);
        emailCol.setPreferredWidth(90);
        passCol.setPreferredWidth(100);
        mainPanelClass.getAccountsTable().getTableHeader().setEnabled(false);
        mainPanelClass.getAccountsTable().getTableHeader().setReorderingAllowed(false);
        mainPanelClass.getAccountsTable().setRowSelectionAllowed(true);
        mainPanelClass.getMainPanel().setVisible(true);
    }
}

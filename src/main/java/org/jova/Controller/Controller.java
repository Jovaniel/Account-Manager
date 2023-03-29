package org.jova.Controller;
import org.jova.AES256;
import org.jova.View.*;
import org.jova.Model.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.*;
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

    public Controller(MainPanelClass mainPanelClass, Model model) throws SQLException, NoSuchAlgorithmException {
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
                if(mainPanelClass.getNewAccountCheckBox().isSelected()){
                    char[] passwordChars = mainPanelClass.getAddNewAccountPassField().getPassword();
                    String password = new String(passwordChars);
                    mainPanelClass.getNewAccountShowPassTxtField().setText(password);
                }else{
                    //Esconder password
                    mainPanelClass.getNewAccountShowPassTxtField().setText("");
                }
            }
        });

        mainPanelClass.editAccountCheckBoxListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(mainPanelClass.getEditAccountCheckBox().isSelected()){
                    char[] getPassword = mainPanelClass.getEditAccountPassTxtField().getPassword();
                    String password = new String(getPassword);
                    mainPanelClass.getEditAccountShowPassTxtField().setText(password);
                }
                mainPanelClass.getEditAccountShowPassTxtField().setText("");
            }
        });

        mainPanelClass.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    row = mainPanelClass.getAccountsTable().getSelectedRow();
                    Object idObj = tableModel.getValueAt(row, 0);
                    int id = Integer.parseInt(idObj.toString());
                    String title = null;
                    String email = null;
                    String password = null;
                    try {
                        title = model.getTitleFromDB(id);
                        email = model.getEmailFromDB(id);
                        password = model.getPasswordFromDB(id);
                        String pass = AES256.decrypt(password, model.masterKey());
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

                    tableModel.setValueAt(title, row, 1);
                    tableModel.setValueAt(email, row, 2);
                    tableModel.setValueAt(password, row, 3);

                    model.updateDataOfDB(id, title, email, password);

                    mainPanelClass.getEditAccountPanel().setVisible(false);
                    mainPanelClass.getAccountsTablePanel().setVisible(true);
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
                                System.out.println(selectedRowId);
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
                    //Here
                    model.insertDataInDB(title, email, password, mainPanelClass.getAccountsTable(), tableModel);
                }catch (Exception ex){
                    System.out.println("Error while encrypting: " + ex);
                }finally {
                    mainPanelClass.getAccountsTablePanel().setVisible(true);
                    mainPanelClass.getAddNewAccountPanel().setVisible(false);
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
                System.out.println();
                    if(password.equals(confirmPassword)){
                        //Crear base de datos
                        mainPanelClass.getInsertDbPasswordPanel().setVisible(false);
                        mainPanelClass.getAccountsTablePanel().setVisible(true);
                        try {
                            model.getMasterKey(password);
                            model.insertKeyInDB(password);
                            mainPanelClass.getAccountsTable().setVisible(true);
                            model.scanDatabase(mainPanelClass.getAccountsTable(), tableModel);
                            tableDefaultSettings();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }else{
                        JOptionPane.showMessageDialog(mainPanelClass.getMainPanel(), "Both passwords are not the same. Try again.");
                    }
                }
        });

        mainPanelClass.loginNextButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] password = mainPanelClass.getLoginPasswordField().getPassword();
                String passwordStr = new String(password);
                String hashPass = model.generateHash(passwordStr);
                System.out.println(hashPass);
                try {
                    if(hashPass.equals(model.getKeyHashFromDB())) {
                        model.getMasterKey(passwordStr);
                        model.scanDatabase(mainPanelClass.getAccountsTable(), tableModel);
                        tableDefaultSettings();
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

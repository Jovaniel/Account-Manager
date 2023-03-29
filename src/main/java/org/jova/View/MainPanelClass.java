package org.jova.View;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;

public class MainPanelClass extends JFrame{

    public MainPanelClass(){

        accountsTablePanel.addMouseListener(new MouseAdapter() {
        });
    }

    private JPanel mainPanel;
    private JPanel createDatabasePanel;
    private JPanel accountsTablePanel;
    private JPanel addNewAccountPanel;
    private JPanel editAccountPanel;
    private JButton cdbCreateButton;
    private JLabel cdbAccountManagerLabel;
    private JTable accountsTable;
    private JButton addNewAccountButton;
    private JLabel accountsTableLabel;
    private JScrollPane accountsTableScrollPanel;
    private JTextField addNewAccountTitleTxtField;
    private JTextField addNewAccountEmailTxtField;
    private JPasswordField addNewAccountPassField;
    private JLabel addNewAccountTitleLabel;
    private JLabel addNewAccountEmailLabel;
    private JLabel addNewAccountPassLabel;
    private JButton newAccountApplyButton;
    private JButton newAccountCancelButton;
    private JLabel editAccountTitleLabel;
    private JLabel editAccountEmailLabel;
    private JLabel editAccountPassLabel;
    private JTextField editAccountTitleTxtField;
    private JTextField editAccountEmailTxtField;
    private JPasswordField editAccountPassTxtField;
    private JButton editAccountApplyButton;
    private JButton editAccountCancelButton;
    private JButton newAccountGenerateButton;
    private JButton editAccountGenerateButton;
    private JButton accountsTableDeleteButton;
    private JCheckBox newAccountCheckBox;
    private JCheckBox editAccountCheckBox;
    private JPanel insertDbPasswordPanel;
    private JPasswordField insertDbPassword;
    private JLabel insertDbLabel;
    private JButton insertDbNextButton;
    private JButton insertDbCancelButton;
    private JTextField editAccountShowPassTxtField;
    private JTextField newAccountShowPassTxtField;
    private JLabel accountsTableDatabaseNameLabel;
    private JLabel insertDbConfirmLabel;
    private JPasswordField insertDbConfirmPassword;
    private JPanel loginPanel;
    private JPasswordField loginPasswordField;
    private JLabel loginLabel;
    private JButton loginNextButton;
    private JLabel addNewAccountManagerLabel;
    private JButton cdbAddExistingButton;
    private JLabel AccountsTableDatabaseNameLabel;

    public JPanel getCreateDatabasePanel() {
        return createDatabasePanel;
    }

    public JPanel getAccountsTablePanel() {
        return accountsTablePanel;
    }

    public JPanel getAddNewAccountPanel() {
        return addNewAccountPanel;
    }

    public JPanel getEditAccountPanel() {
        return editAccountPanel;
    }

    public JLabel getCdbAccountManagerLabel() {
        return cdbAccountManagerLabel;
    }

    public JTable getAccountsTable() {
        return accountsTable;
    }

    public JLabel getAccountsTableDatabaseNameLabel(){
        return accountsTableDatabaseNameLabel;
    }

    public JButton getAddNewAccountButton() {
        return addNewAccountButton;
    }

    public JLabel getAccountsTableLabel() {
        return accountsTableLabel;
    }

    public JScrollPane getAccountsTableScrollPanel() {
        return accountsTableScrollPanel;
    }

    public JTextField getAddNewAccountTitleTxtField() {
        return addNewAccountTitleTxtField;
    }

    public JTextField getAddNewAccountEmailTxtField() {
        return addNewAccountEmailTxtField;
    }

    public JPasswordField getAddNewAccountPassField() {
        return addNewAccountPassField;
    }

    public JLabel getAddNewAccountTitleLabel() {
        return addNewAccountTitleLabel;
    }

    public JLabel getAddNewAccountEmailLabel() {
        return addNewAccountEmailLabel;
    }

    public JLabel getAddNewAccountPassLabel() {
        return addNewAccountPassLabel;
    }

    public JButton getNewAccountApplyButton() {
        return newAccountApplyButton;
    }

    public JButton getNewAccounCancelButton() {
        return newAccountCancelButton;
    }

    public JLabel getEditAccountTitleLabel() {
        return editAccountTitleLabel;
    }

    public JLabel getEditAccountEmailLabel() {
        return editAccountEmailLabel;
    }

    public JLabel getEditAccountPassLabel() {
        return editAccountPassLabel;
    }

    public JTextField getEditAccountTitleTxtField() {
        return editAccountTitleTxtField;
    }

    public JTextField getEditAccountEmailTxtField() {
        return editAccountEmailTxtField;
    }

    public JPasswordField getEditAccountPassTxtField() {
        return editAccountPassTxtField;
    }

    public JButton getEditAccountApplyButton() {
        return editAccountApplyButton;
    }

    public JButton getEditAccountCancelButton() {
        return editAccountCancelButton;
    }

    public JCheckBox getNewAccountCheckBox() {
        return newAccountCheckBox;
    }

    public JCheckBox getEditAccountCheckBox() {
        return editAccountCheckBox;
    }

    public JTextField getEditAccountShowPassTxtField() {
        return editAccountShowPassTxtField;
    }

    /*-----------------------------------First Layer----------------------------------------------*/
    public JButton getInsertDbNextButton() {
        return insertDbNextButton;
    }

    public JButton getInsertDbCancelButton() {
        return insertDbCancelButton;
    }

    public JPasswordField getInsertDbPassword() {
        return insertDbPassword;
    }

    public JPasswordField getInsertDbConfirmPassword() {
        return insertDbConfirmPassword;
    }

    public JPanel getInsertDbPasswordPanel() {
        return insertDbPasswordPanel;
    }

    public JButton getCdbCreateButton() {
        return cdbCreateButton;
    }

    /*-------------------------------Create New Database Panel Listeners---------------------------*/
    public void cdbCreateButtonListener (ActionListener listener){
        cdbCreateButton.addActionListener(listener);
    }
    public void insertDbPasswordNextButtonListener(ActionListener listener){
        getInsertDbNextButton().addActionListener(listener);
    }
    public void insertDbPasswordCancelButtonListener(ActionListener listener){
        getInsertDbCancelButton().addActionListener(listener);
    }

    /*-------------------------------Accounts Table Panel Listeners--------------------------------*/
    public void addNewAccountButtonListener(ActionListener listener){
        addNewAccountButton.addActionListener(listener);
    }
    public void deleteSelectedAccountButtonListener(ActionListener listener){
        accountsTableDeleteButton.addActionListener(listener);
    }

    public JTextField getNewAccountShowPassTxtField() {
        return newAccountShowPassTxtField;
    }

    /*-------------------------------Add New Account Panel Listeners-------------------------------*/
    public void newAccountApplyButtonListener(ActionListener listener){
        newAccountApplyButton.addActionListener(listener);
    }
    public void newAccountCancelButtonListener(ActionListener listener){
        newAccountCancelButton.addActionListener(listener);
    }
    public void newAccountCheckBoxListener(ItemListener listener){
        newAccountCheckBox.addItemListener(listener);
    }

    /*-------------------------------Edit Account Panel Listeners----------------------------------*/
    public void editAccountApplyButtonListener(ActionListener listener){
        editAccountApplyButton.addActionListener(listener);
    }
    public void editAccountCancelButtonListener(ActionListener listener){
        editAccountCancelButton.addActionListener(listener);
    }
    public void editAccountCheckBoxListener(ItemListener listener){
        editAccountCheckBox.addItemListener(listener);
    }

    /*-------------------------------Login Panel Listener----------------------------------*/

    public void loginNextButtonListener(ActionListener listener){
        loginNextButton.addActionListener(listener);
    }

    /*---------------------------------Main Panel---------------------------------------------------*/
    public JPanel getMainPanel(){
        return mainPanel;
    }

    /*---------------------------------Login Panel--------------------------------------------------*/

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public JPasswordField getLoginPasswordField() {
        return loginPasswordField;
    }

    public JLabel getLoginLabel() {
        return loginLabel;
    }
}

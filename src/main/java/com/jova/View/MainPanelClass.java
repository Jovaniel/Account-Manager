package com.jova.View;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

public class MainPanelClass extends JFrame{
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
    private JPanel passwordGeneratorPanel;
    private JLabel passwordGeneratorLabel;
    private JLabel selectTypeOfPassLabel;
    private JButton passphraseButton;
    private JButton randomPasswordButton;
    private JPanel passphrasePanel;
    private JPanel randomPasswordPanel;
    private JLabel passphraseLabel;
    private JLabel passphraseWordsLabel;
    private JLabel passphraseSeparatorLabel;
    private JTextField passphraseNumberOfWordsTxtField;
    private JTextField passphraseWordSeparatorTxtField;
    private JLabel passphraseCasesLabel;
    private JLabel randomPasswordLabel;
    private JLabel randomPasswordLengthLabel;
    private JTextField randomPasswordLengthTxtField;
    private JLabel randomPasswordIncludeUppderCaseLabel;
    private JCheckBox includeUppercaseCheckbox;
    private JLabel includeLowercaseLettersLabel;
    private JCheckBox includeLowercaseCheckbox;
    private JLabel includeSymbolsLabel;
    private JCheckBox includeSymbolsCheckbox;
    private JLabel includeNumbersLabel;
    private JCheckBox includeNumbersCheckbox;
    private JButton passphraseCancelButton;
    private JButton passphraseApplyButton;
    private JButton randomPassCancelButton;
    private JButton randomPassApplyButton;
    private JTextField passphraseShowGeneratedPassTxtField;
    private JTextField randomPassShowGeneratedPassTxtField;
    private JPanel passphraseGroupOfRadioButtons;
    private JRadioButton passphraseUpperRadioButton;
    private JRadioButton passphraseLowerRadioButton;
    private JRadioButton passphraseTitleRadioButton;
    private JButton passphraseGenerateButton;
    private JButton randomPassGenerateButton;
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

    public JTable getAccountsTable() {
        return accountsTable;
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


    public JPanel getPasswordGeneratorPanel() {
        return passwordGeneratorPanel;
    }


    public JPanel getPassphrasePanel() {
        return passphrasePanel;
    }

    public JPanel getRandomPasswordPanel() {
        return randomPasswordPanel;
    }

    public JTextField getPassphraseNumberOfWordsTxtField() {
        return passphraseNumberOfWordsTxtField;
    }

    public JTextField getPassphraseWordSeparatorTxtField() {
        return passphraseWordSeparatorTxtField;
    }

    public JTextField getRandomPasswordLengthTxtField() {
        return randomPasswordLengthTxtField;
    }

    public JCheckBox getIncludeUppercaseCheckbox() {
        return includeUppercaseCheckbox;
    }

    public JCheckBox getIncludeLowercaseCheckbox() {
        return includeLowercaseCheckbox;
    }

    public JCheckBox getIncludeSymbolsCheckbox() {
        return includeSymbolsCheckbox;
    }

    public JCheckBox getIncludeNumbersCheckbox() {
        return includeNumbersCheckbox;
    }

    public JRadioButton getPassphraseUpperRadioButton() {
        return passphraseUpperRadioButton;
    }

    public JRadioButton getPassphraseLowerRadioButton() {
        return passphraseLowerRadioButton;
    }

    public JRadioButton getPassphraseTitleRadioButton() {
        return passphraseTitleRadioButton;
    }

    public JTextField getPassphraseShowGeneratedPassTxtField() {
        return passphraseShowGeneratedPassTxtField;
    }

    public JTextField getRandomPassShowGeneratedPassTxtField() {
        return randomPassShowGeneratedPassTxtField;
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

    /*-------------------------------Create New Database Panel Listeners---------------------------*/
    public void cdbCreateButtonListener (ActionListener listener){
        cdbCreateButton.addActionListener(listener);
    }
    public void insertDbPasswordNextButtonListener(ActionListener listener){
        getInsertDbNextButton().addActionListener(listener);
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

    /*---------------------------------Generate Password Panels--------------------------------------------------*/

    public void newAccountGenerateButtonListener(ActionListener listener){
        newAccountGenerateButton.addActionListener(listener);
    }

    public void editAccountGenerateButtonListener(ActionListener listener){
        editAccountGenerateButton.addActionListener(listener);
    }

    public void passphraseApplyButtonListener(ActionListener listener){
        passphraseApplyButton.addActionListener(listener);
    }

    public void passphraseCancelButton(ActionListener listener){
        passphraseCancelButton.addActionListener(listener);
    }

    public void randomPassApplyButtonListener(ActionListener listener){
        randomPassApplyButton.addActionListener(listener);
    }

    public void randomPassCancelButton(ActionListener listener){
        randomPassCancelButton.addActionListener(listener);
    }

    public void includeUppercaseCheckboxListener(ItemListener listener){
        includeUppercaseCheckbox.addItemListener(listener);
    }

    public void includeLowercaseCheckboxListener(ItemListener listener){
        includeLowercaseCheckbox.addItemListener(listener);
    }

    public void includeSymbolsCheckboxListener(ItemListener listener){
        includeSymbolsCheckbox.addItemListener(listener);
    }

    public void includeNumbersCheckboxListener(ItemListener listener){
        includeNumbersCheckbox.addItemListener(listener);
    }

    public void passphraseButtonListener(ActionListener listener){
        passphraseButton.addActionListener(listener);
    }

    public void randomPasswordButtonListener(ActionListener listener){
        randomPasswordButton.addActionListener(listener);
    }

    public void passphraseUpperRadioButtonListener(ActionListener listener){
        passphraseUpperRadioButton.addActionListener(listener);
    }

    public void passphraseLowerRadioButtonListener(ActionListener listener){
        passphraseLowerRadioButton.addActionListener(listener);
    }

    public void passphraseTitleRadioButtonListener(ActionListener listener){
        passphraseTitleRadioButton.addActionListener(listener);
    }

    public void passphraseGenerateButtonListener(ActionListener listener){
        passphraseGenerateButton.addActionListener(listener);
    }

    public void randomPassGenerateButtonListener(ActionListener listener){
        randomPassGenerateButton.addActionListener(listener);
    }

}

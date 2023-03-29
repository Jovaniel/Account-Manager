package org.jova;
import org.jova.Controller.Controller;
import org.jova.Model.Model;
import org.jova.View.MainPanelClass;
import password.generator.data.Wordlist;
import password.generator.generators.PassphraseGenerator;
import password.generator.generators.PasswordGenerator;

import javax.swing.*;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, NoSuchAlgorithmException {
        //Añadir t0do esto en el controlador
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        MainPanelClass mainPanelClass = new MainPanelClass();
        Model model = new Model();
        Controller controller = new Controller(mainPanelClass, model);
        mainPanelClass.setContentPane(mainPanelClass.getMainPanel());
        mainPanelClass.setTitle("Account Manager");
        mainPanelClass.setSize(600,400);
        mainPanelClass.setVisible(true);
        mainPanelClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Wordlist w = Wordlist.instantiate(new File("src/main/resources/eff_long_wordlist.txt"));
        String passphrase = PassphraseGenerator.getPassphrase(w, 5, "-.", PassphraseGenerator.Cases.TITLE_CASE);
        String pass = PasswordGenerator.getPassword(5, true, true, true, true);
        System.out.println(passphrase);
        System.out.println(pass);
    }
}

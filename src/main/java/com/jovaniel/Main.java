package com.jovaniel;
import com.jovaniel.Controller.Controller;
import com.jovaniel.Model.Model;
import com.jovaniel.View.MainPanelClass;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        MainPanelClass mainPanelClass = new MainPanelClass();
        Model model = new Model();
        Controller controller = new Controller(mainPanelClass, model);
        mainPanelClass.setContentPane(mainPanelClass.getMainPanel());
        mainPanelClass.setTitle("Account Manager");
        mainPanelClass.setSize(600,400);
        mainPanelClass.setResizable(false);
        mainPanelClass.setVisible(true);
        mainPanelClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

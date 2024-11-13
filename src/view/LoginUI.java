package view;

import business.UserController;
import core.Helper;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame{
    private JPanel container;
    private JPanel pnl_top;
    private JLabel lbl_title;
    private JPanel pnl_buttom;
    private JTextField fld_mail;
    private JButton btn_login;
    private JLabel lbl_mail;
    private JLabel pnl_password;
    private JPasswordField fld_password;
    private UserController userController;

    public LoginUI(){

        this.userController = new UserController();

        this.add(container);
        this.setTitle("Order Management System");
        this.setSize(400,400);

        //to center the screen
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x,y);

        this.setVisible(true);

        this.btn_login.addActionListener(e -> {
            JTextField[] checkList = {this.fld_password,this.fld_mail};

            if (!Helper.isEmailValid(this.fld_mail.getText())){
                Helper.showMessage("Please enter a valid email.");
            }else if(Helper.isFieldListEmpty(checkList)){
                Helper.showMessage("fill");
            }else{
                User user = this.userController.findByLogin(this.fld_mail.getText(), this.fld_password.getText());
                if (user == null){
                    Helper.showMessage("User not found");
                }else {
                    this.dispose();
                    DashboardUI dashboardUI = new DashboardUI(user);
                }
            }
        });
    }
}

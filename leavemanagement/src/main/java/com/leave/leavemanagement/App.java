package com.leave.leavemanagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame mainFrame = new JFrame("Leave Management System");
            mainFrame.setSize(400, 300);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocationRelativeTo(null);

            // Create buttons
            JButton applyLeaveButton = new JButton("Apply for Leave");
            JButton checkStatusButton = new JButton("Check Leave Status");
            JButton adminPanelButton = new JButton("Admin Panel");

            // Add button action listeners
            applyLeaveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ApplyLeaveFrame applyLeaveFrame = new ApplyLeaveFrame();
                    applyLeaveFrame.setVisible(true);
                }
            });

            checkStatusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LeaveStatusFrame leaveStatusFrame = new LeaveStatusFrame();
                    leaveStatusFrame.setVisible(true);
                }
            });

            adminPanelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AdminFrame adminFrame = new AdminFrame();
                    adminFrame.setVisible(true);
                }
            });

            // Create a panel for buttons
            JPanel panel = new JPanel();
            panel.add(applyLeaveButton);
            panel.add(checkStatusButton);
            panel.add(adminPanelButton);

            // Add panel to the main frame
            mainFrame.add(panel);
            mainFrame.setVisible(true);
        });
    }
}
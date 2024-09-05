package com.leave.leavemanagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AdminFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton approveButton;
    private JButton rejectButton;
    private JTextArea statusArea;
    private JTextField applicationIdField;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/LeaveManagement";
    private static final String USER = "root";
    private static final String PASS = "jaya@12";

    public AdminFrame() {
        setTitle("Admin Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Login Panel
        JPanel loginPanel = new JPanel();
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        // Status Management Panel
        JPanel managementPanel = new JPanel();
        applicationIdField = new JTextField(10);
        approveButton = new JButton("Approve");
        rejectButton = new JButton("Reject");
        statusArea = new JTextArea(10, 30);
        statusArea.setEditable(false);
        managementPanel.add(new JLabel("Application ID:"));
        managementPanel.add(applicationIdField);
        managementPanel.add(approveButton);
        managementPanel.add(rejectButton);
        managementPanel.add(new JScrollPane(statusArea));
        managementPanel.setVisible(false);

        // Add panels to frame
        add(loginPanel, "North");
        add(managementPanel, "Center");

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("admin123")) {
                    loginPanel.setVisible(false);
                    managementPanel.setVisible(true);
                    loadPendingApplications();
                } else {
                    JOptionPane.showMessageDialog(AdminFrame.this, "Invalid credentials. Please try again.");
                }
            }
        });

        // Approve button action listener
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeStatus("Approved");
            }
        });

        // Reject button action listener
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeStatus("Rejected");
            }
        });
    }

    private void loadPendingApplications() {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT application_id, employee_id, start_date, end_date, leave_type, status FROM LeaveApplications WHERE status = 'Pending' ORDER BY start_date DESC";
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            statusArea.setText("");

            if (rs.next()) {
                do {
                    int applicationId = rs.getInt("application_id");
                    String employeeId = rs.getString("employee_id");
                    java.sql.Date startDate = rs.getDate("start_date");
                    java.sql.Date endDate = rs.getDate("end_date");
                    String leaveType = rs.getString("leave_type");
                    String status = rs.getString("status");

                    statusArea.append("ID: " + applicationId + " | Employee ID: " + employeeId + " | From " + startDate + " to " + endDate + " | Leave Type: " + leaveType + " | Status: " + status + "\n");
                } while (rs.next());
            } else {
                statusArea.append("No pending leave applications.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusArea.setText("Error loading applications.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeStatus(String newStatus) {
        String applicationId = applicationIdField.getText();
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String updateStatusQuery = "UPDATE LeaveApplications SET status = ? WHERE application_id = ?";
            pstmt = conn.prepareStatement(updateStatusQuery);
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, Integer.parseInt(applicationId));
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(AdminFrame.this, "Leave application status updated to " + newStatus + ".");
                loadPendingApplications();
            } else {
                JOptionPane.showMessageDialog(AdminFrame.this, "Failed to update status. Application ID may be incorrect.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(AdminFrame.this, "Error updating status.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminFrame().setVisible(true));
    }
}
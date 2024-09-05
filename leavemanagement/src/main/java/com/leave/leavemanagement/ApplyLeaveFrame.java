package com.leave.leavemanagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;

public class ApplyLeaveFrame extends JFrame {
    private JTextField employeeIdField;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JComboBox<String> leaveTypeComboBox;
    private JButton applyButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/LeaveManagement";
    private static final String USER = "root";
    private static final String PASS = "jaya@12";

    public ApplyLeaveFrame() {
        setTitle("Apply for Leave");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        employeeIdField = new JTextField(10);

        startDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner = new JSpinner(new SpinnerDateModel());

        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");

        startDateSpinner.setEditor(startEditor);
        endDateSpinner.setEditor(endEditor);

        leaveTypeComboBox = new JComboBox<>(new String[]{"Sick Leave", "Normal Leave"});
        applyButton = new JButton("Apply");

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = employeeIdField.getText();
                Date startDate = (Date) startDateSpinner.getValue();
                Date endDate = (Date) endDateSpinner.getValue();
                String leaveType = (String) leaveTypeComboBox.getSelectedItem();

                if (endDate.before(startDate)) {
                    JOptionPane.showMessageDialog(ApplyLeaveFrame.this, "End date cannot be before start date.");
                    return;
                }

                java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
                java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

                boolean success = processLeaveApplication(employeeId, sqlStartDate, sqlEndDate, leaveType);

                if (success) {
                    JOptionPane.showMessageDialog(ApplyLeaveFrame.this, "Leave application submitted successfully!");
                } else {
                    JOptionPane.showMessageDialog(ApplyLeaveFrame.this, "Failed to submit leave application.");
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Employee ID:"));
        panel.add(employeeIdField);
        panel.add(new JLabel("Start Date:"));
        panel.add(startDateSpinner);
        panel.add(new JLabel("End Date:"));
        panel.add(endDateSpinner);
        panel.add(new JLabel("Leave Type:"));
        panel.add(leaveTypeComboBox);
        panel.add(applyButton);

        add(panel);
    }

    private boolean processLeaveApplication(String employeeId, java.sql.Date startDate, java.sql.Date endDate, String leaveType) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String insertLeaveQuery = "INSERT INTO LeaveApplications (employee_id, start_date, end_date, leave_type, status) VALUES (?, ?, ?, ?, 'Pending')";
            pstmt = conn.prepareStatement(insertLeaveQuery);
            pstmt.setString(1, employeeId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            pstmt.setString(4, leaveType);
            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
            	if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApplyLeaveFrame().setVisible(true));
    }
}
            
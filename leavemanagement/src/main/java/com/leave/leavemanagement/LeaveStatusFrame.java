package com.leave.leavemanagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LeaveStatusFrame extends JFrame {
    private JTextField employeeIdField;
    private JButton checkStatusButton;
    private JTextArea statusArea;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/LeaveManagement";
    private static final String USER = "root";
    private static final String PASS = "jaya@12";

    public LeaveStatusFrame() {
        setTitle("Check Leave Status");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        employeeIdField = new JTextField(10);
        checkStatusButton = new JButton("Check Status");
        statusArea = new JTextArea(10, 30);
        statusArea.setEditable(false);

        checkStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = employeeIdField.getText();
                if (employeeId.isEmpty()) {
                    JOptionPane.showMessageDialog(LeaveStatusFrame.this, "Please enter an Employee ID.");
                } else {
                    checkLeaveStatus(employeeId);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Employee ID:"));
        panel.add(employeeIdField);
        panel.add(checkStatusButton);
        panel.add(new JScrollPane(statusArea));

        add(panel);
    }

    private void checkLeaveStatus(String employeeId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "SELECT application_id, start_date, end_date, leave_type, status FROM LeaveApplications WHERE employee_id = ? ORDER BY start_date DESC";
            pstmt = conn.prepareStatement(query);
             pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();

            statusArea.setText(""); // Clear the text area

            if (rs.next()) {
                do {
                    int applicationId = rs.getInt("application_id");
                    Date startDate = rs.getDate("start_date");
                    Date endDate = rs.getDate("end_date");
                    String leaveType = rs.getString("leave_type");
                    String status = rs.getString("status");

                    statusArea.append("ID: " + applicationId + " | From " + startDate + " to " + endDate + " | Leave Type: " + leaveType + " | Status: " + status + "\n");
                } while (rs.next());
            } else {
                statusArea.append("No leave applications found for employee ID: " + employeeId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusArea.setText("Error checking leave status.");
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
        SwingUtilities.invokeLater(() -> new LeaveStatusFrame().setVisible(true));
    }
}

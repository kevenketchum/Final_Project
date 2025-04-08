package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class is the main Gradebook GUI panel that contains views for both student and teacher users.
 * It uses CardLayout to switch between login, student, and teacher panels within a single window.
 */
public class GradebookGUI extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public GradebookGUI() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel();
        StudentPanel studentPanel = new StudentPanel();
        TeacherPanel teacherPanel = new TeacherPanel();

        mainPanel.add(loginPanel, "login");
        mainPanel.add(studentPanel, "student");
        mainPanel.add(teacherPanel, "teacher");

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Panel for user login.
     */
    private class LoginPanel extends JPanel {
        public LoginPanel() {
            setLayout(new GridLayout(5, 1));
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JButton loginButton = new JButton("Login");

            add(new JLabel("Username:"));
            add(usernameField);
            add(new JLabel("Password:"));
            add(passwordField);
            add(loginButton);

            loginButton.addActionListener(e -> {
                String username = usernameField.getText();
                // TEMP LOGIC: hardcoded login detection
                if (username.equalsIgnoreCase("teacher")) {
                    cardLayout.show(mainPanel, "teacher");
                } else {
                    cardLayout.show(mainPanel, "student");
                }
            });
        }
    }

    /**
     * Panel for student view showing assignments and grades.
     */
    private class StudentPanel extends JPanel {
        public StudentPanel() {
            setLayout(new BorderLayout());
            JLabel welcome = new JLabel("Welcome, Student");
            add(welcome, BorderLayout.NORTH);

            String[] columns = {"Assignment", "Grade"};
            String[][] data = {
                {"Homework 1", "95"},
                {"Quiz 1", "88"},
                {"Midterm", "90"}
            };
            JTable gradeTable = new JTable(data, columns);
            add(new JScrollPane(gradeTable), BorderLayout.CENTER);

            JLabel overall = new JLabel("Overall Grade: 91% (A)");
            add(overall, BorderLayout.SOUTH);
        }
    }

    /**
     * Panel for teacher view showing all students and assignment tools.
     */
    private class TeacherPanel extends JPanel {
        public TeacherPanel() {
            setLayout(new BorderLayout());
            JLabel welcome = new JLabel("Welcome, Teacher");
            add(welcome, BorderLayout.NORTH);

            String[] columns = {"Student", "Assignment", "Grade"};
            String[][] data = {
                {"Alice", "Homework 1", "90"},
                {"Bob", "Homework 1", "85"},
                {"Charlie", "Homework 1", "92"}
            };
            JTable studentTable = new JTable(data, columns);
            add(new JScrollPane(studentTable), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(new JButton("Add Grade"));
            buttonPanel.add(new JButton("Remove Grade"));
            buttonPanel.add(new JButton("Assign Grade"));

            add(buttonPanel, BorderLayout.SOUTH);
        }
    }
} 

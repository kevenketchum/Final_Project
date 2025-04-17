
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import logic.SecurityManager;
import model.*;
import java.util.List;

public class GradeBookGui extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SecurityManager securityManager;
    private User currentUser;

    public GradeBookGui() {
        setLayout(new BorderLayout());
        securityManager = new SecurityManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginPanel(), "login");
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "login");
    }

    /** Login Panel allowing role selection */
    private class LoginPanel extends JPanel {
        public LoginPanel() {
            setLayout(new GridLayout(7, 1));

            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JComboBox<String> roleDropdown = new JComboBox<>(new String[]{"student", "teacher", "admin"});
            JButton loginButton = new JButton("Login");

            add(new JLabel("Username:"));
            add(usernameField);
            add(new JLabel("Password:"));
            add(passwordField);
            add(new JLabel("Select Role:"));
            add(roleDropdown);
            add(loginButton);

            loginButton.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String role = (String) roleDropdown.getSelectedItem();

                if (securityManager.loginUser(username, password)) {
                    switch (role.toLowerCase()) {
                        case "student":
                            currentUser = new Student(username);
                            StudentPanel studentPanel = new StudentPanel((Student) currentUser);
                            mainPanel.add(studentPanel, "student");
                            cardLayout.show(mainPanel, "student");
                            break;
                        case "teacher":
                        case "admin":
                            currentUser = new Teacher(username, new Gradebook());
                            TeacherPanel teacherPanel = new TeacherPanel((Teacher) currentUser);
                            mainPanel.add(teacherPanel, "teacher");
                            cardLayout.show(mainPanel, "teacher");
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Invalid role selected.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed. Check your credentials.");
                }
            });
        }
    }

    /** Student Panel */
    private class StudentPanel extends JPanel {
        public StudentPanel(Student student) {
            setLayout(new BorderLayout());
            JLabel welcome = new JLabel("Welcome, " + student.getUsername());
            add(welcome, BorderLayout.NORTH);

            String[] columns = {"Assignment", "Grade"};
            List<Double> grades = student.getGrades();

            String[][] data = new String[grades.size()][2];
            for (int i = 0; i < grades.size(); i++) {
                data[i][0] = "Assignment " + (i + 1);
                data[i][1] = String.valueOf(grades.get(i));
            }

            JTable gradeTable = new JTable(data, columns);
            add(new JScrollPane(gradeTable), BorderLayout.CENTER);

            JLabel average = new JLabel("Average: " + String.format("%.2f", student.getAverageGrade()));
            add(average, BorderLayout.SOUTH);
        }
    }

    /** Teacher/Admin Panel */
    private class TeacherPanel extends JPanel {
        public TeacherPanel(Teacher teacher) {
            setLayout(new BorderLayout());
            JLabel welcome = new JLabel("Welcome, " + teacher.getUsername());
            add(welcome, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();

            JButton addStudentBtn = new JButton("Add Student to Course");
            JButton assignGradeBtn = new JButton("Assign Grade to Student");
            buttonPanel.add(addStudentBtn);
            buttonPanel.add(assignGradeBtn);

            if (teacher.getRole().equals("admin")) {
                JButton adminAddStudent = new JButton("Admin: Add Student");
                buttonPanel.add(adminAddStudent);
                // adminAddStudent.addActionListener(...); // Add actual admin logic here
            }

            add(buttonPanel, BorderLayout.SOUTH);
        }
    }
}

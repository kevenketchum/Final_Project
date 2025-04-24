package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import logic.SecurityManager;
import model.*;
import java.util.List;
import java.util.ArrayList;

public class GradeBookGui extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SecurityManager securityManager;
    private User currentUser;
    private Gradebook gradebook;

    public GradeBookGui(Gradebook gradebook) {
        this.gradebook = gradebook;
        initializeGui();
    }

    private void initializeGui() {
        setLayout(new BorderLayout());
        securityManager = new SecurityManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginPanel(), "login");
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "login");
    }

    private class LoginPanel extends JPanel {
        public LoginPanel() {
            setLayout(new GridLayout(8, 1));

            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JComboBox<String> roleDropdown = new JComboBox<>(new String[]{"student", "teacher", "admin"});
            JButton loginButton = new JButton("Login");
            JButton registerButton = new JButton("Register");

            add(new JLabel("Username:"));
            add(usernameField);
            add(new JLabel("Password:"));
            add(passwordField);
            add(new JLabel("Select Role:"));
            add(roleDropdown);
            add(loginButton);
            add(registerButton);

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
                            currentUser = new Teacher(username, gradebook);
                            TeacherPanel teacherPanel = new TeacherPanel((Teacher) currentUser);
                            mainPanel.add(teacherPanel, "teacher");
                            cardLayout.show(mainPanel, "teacher");
                            break;
                        case "admin":
                            currentUser = new Admin(username, gradebook);
                            TeacherPanel adminPanel = new TeacherPanel((Teacher) currentUser);
                            mainPanel.add(adminPanel, "teacher");
                            cardLayout.show(mainPanel, "teacher");
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Invalid role selected.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed. Check your credentials.");
                }
            });

            registerButton.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String role = (String) roleDropdown.getSelectedItem();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter both username and password.");
                    return;
                }

                boolean registered = securityManager.registerUser(username, password);
                if (registered) {
                    JOptionPane.showMessageDialog(this, "Registration successful! You can now log in as a " + role + ".");
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.");
                }
            });
        }
    }

    private class StudentPanel extends JPanel {
        public StudentPanel(Student student) {
            setLayout(new BorderLayout());
            JLabel welcome = new JLabel("Welcome, " + student.getUsername());
            add(welcome, BorderLayout.NORTH);

            String[] columns = {"Assignment", "Grade"};
            List<Double> grades = gradebook.getGrades(student.getUsername());

            String[][] data = new String[grades.size()][2];
            for (int i = 0; i < grades.size(); i++) {
                data[i][0] = "Assignment " + (i + 1);
                data[i][1] = String.valueOf(grades.get(i));
            }

            JTable gradeTable = new JTable(data, columns);
            add(new JScrollPane(gradeTable), BorderLayout.CENTER);

            JLabel average = new JLabel("Average: " + String.format("%.2f", gradebook.getAverage(student.getUsername())));
            add(average, BorderLayout.SOUTH);
        }
    }

    private class TeacherPanel extends JPanel {
        public TeacherPanel(Teacher teacher) {
            setLayout(new BorderLayout());
            JLabel welcome = new JLabel("Welcome, " + teacher.getUsername());
            add(welcome, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();

            JButton viewGradebookBtn = new JButton("View Full Gradebook");
            JButton assignGradeBtn = new JButton("Assign Grade to Student");
            JButton createAssignmentBtn = new JButton("Create Assignment");

            buttonPanel.add(viewGradebookBtn);
            buttonPanel.add(assignGradeBtn);
            buttonPanel.add(createAssignmentBtn);

            if (teacher instanceof Admin) {
                JButton adminAddStudent = new JButton("Add Student to Course");
                buttonPanel.add(adminAddStudent);

                // 🔧 FIXED SECTION: Real add-to-course logic
                adminAddStudent.addActionListener(e -> {
                    String student = JOptionPane.showInputDialog("Enter student username:");
                    String courseName = JOptionPane.showInputDialog("Enter course name to enroll the student:");

                    if (student != null && courseName != null) {
                        boolean success = gradebook.addStudentToCourse(student, courseName);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Student '" + student + "' added to course '" + courseName + "'.");
                            gradebook.saveToFile();
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to add student. Check if course or student exists.");
                        }
                    }
                });
            }

            add(buttonPanel, BorderLayout.SOUTH);

            assignGradeBtn.addActionListener(e -> {
                String student = JOptionPane.showInputDialog("Enter student username:");
                String gradeStr = JOptionPane.showInputDialog("Enter grade to assign:");
                try {
                    double grade = Double.parseDouble(gradeStr);
                    gradebook.addGrade(student, grade);
                    gradebook.saveToFile();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid grade format");
                }
            });

            createAssignmentBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                String assignmentName = JOptionPane.showInputDialog("Enter assignment name:");
                String type = JOptionPane.showInputDialog("Enter assignment type (assignment, quiz, test):");

                Course course = new Course(courseName);
                course.createAssignment(assignmentName, type);
                JOptionPane.showMessageDialog(this, "Assignment '" + assignmentName + "' of type '" + type + "' created for course " + courseName);
            });

            viewGradebookBtn.addActionListener(e -> {
                StringBuilder sb = new StringBuilder();
                sb.append("Gradebook:\n");
                for (String student : getAllStudentNames()) {
                    sb.append(student).append(" → Grades: ").append(gradebook.getGrades(student)).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            });
        }

        private List<String> getAllStudentNames() {
            List<String> all = new ArrayList<>();
            all.add("student1");
            all.add("student2");
            all.add("student3");
            return all;
        }
    }

    public static class Admin extends Teacher {
        public Admin(String username, Gradebook gradebook) {
            super(username, gradebook);
            this.role = "admin";
        }
    }
}

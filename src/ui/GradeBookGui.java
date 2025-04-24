package ui;

import javax.swing.*;
import java.awt.*;
import logic.SecurityManager;
import model.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
                            gradebook.registerUser(currentUser);
                            StudentPanel studentPanel = new StudentPanel((Student) currentUser);
                            mainPanel.add(studentPanel, "student");
                            cardLayout.show(mainPanel, "student");
                            break;
                        case "teacher":
                            currentUser = new Teacher(username, gradebook);
                            gradebook.registerUser(currentUser);
                            TeacherPanel teacherPanel = new TeacherPanel((Teacher) currentUser);
                            mainPanel.add(teacherPanel, "teacher");
                            cardLayout.show(mainPanel, "teacher");
                            break;
                        case "admin":
                            currentUser = new Admin(username, gradebook);
                            AdminPanel adminPanel = new AdminPanel((Admin) currentUser);
                            mainPanel.add(adminPanel, "admin");
                            cardLayout.show(mainPanel, "admin");
                            break;
                    }
                    mainPanel.revalidate();
                    mainPanel.repaint();
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

            JPanel coursePanel = new JPanel();
            coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

            double weightedTotal = 0.0;
            int courseCount = 0;

            for (String courseName : gradebook.getAllCourseNames()) {
                Course course = gradebook.getCourse(courseName);
                if (course != null && course.isCourseOngoing()) {
                    List<Assignment> assignments = course.getAssignments();
                    String[][] data = new String[assignments.size()][2];

                    for (int i = 0; i < assignments.size(); i++) {
                        Assignment a = assignments.get(i);
                        data[i][0] = a.getAssignment();
                        data[i][1] = String.valueOf(a.getStudentGrade(student.getUsername()));
                    }

                    String[] columns = {"Assignment", "Grade"};
                    JTable gradeTable = new JTable(data, columns) {
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

                    coursePanel.add(new JLabel("Course: " + course.getName()));
                    coursePanel.add(new JScrollPane(gradeTable));
                    weightedTotal += course.getStudentGrade(student.getUsername());
                    courseCount++;
                }
            }

            double weightedAverage = courseCount == 0 ? 0.0 : weightedTotal / courseCount;
            JLabel average = new JLabel("Weighted Average: " + String.format("%.2f", weightedAverage));
            JLabel gpa = new JLabel("GPA: " + String.format("%.2f", gradebook.getAverage(student.getUsername())));
            add(coursePanel, BorderLayout.CENTER);
            add(average, BorderLayout.SOUTH);
            add(gpa, BorderLayout.EAST);

            JButton backButton = new JButton("Back to Login");
            backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
            add(backButton, BorderLayout.WEST);
        }
    }

    private class TeacherPanel extends JPanel {
        public TeacherPanel(Teacher teacher) {
            setLayout(new BorderLayout());
            JLabel welcome = new JLabel("Welcome, " + teacher.getUsername());
            add(welcome, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton viewGradebookBtn = new JButton("View Full Gradebook");
            JButton assignGradeBtn = new JButton("Assign Grade to Student");
            JButton createAssignmentBtn = new JButton("Create Assignment");
            JButton dropAssignmentBtn = new JButton("Drop Assignment");
            JButton removeAssignmentBtn = new JButton("Remove Assignment");
            JButton sortByNameBtn = new JButton("Sort by Name");
            JButton sortByGradeBtn = new JButton("Sort by Grade");
            JButton changeWeightBtn = new JButton("Change Assignment Weight");
            JButton createGroupBtn = new JButton("Create Student Groups");
            JButton backButton = new JButton("Back to Login");

            buttonPanel.add(viewGradebookBtn);
            buttonPanel.add(assignGradeBtn);
            buttonPanel.add(createAssignmentBtn);
            buttonPanel.add(dropAssignmentBtn);
            buttonPanel.add(removeAssignmentBtn);
            buttonPanel.add(sortByNameBtn);
            buttonPanel.add(sortByGradeBtn);
            buttonPanel.add(changeWeightBtn);
            buttonPanel.add(createGroupBtn);
            buttonPanel.add(backButton);

            add(buttonPanel, BorderLayout.CENTER);

            assignGradeBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                String assignmentName = JOptionPane.showInputDialog("Enter assignment name:");
                String student = JOptionPane.showInputDialog("Enter student username:");
                String gradeStr = JOptionPane.showInputDialog("Enter grade to assign:");
                try {
                    double grade = Double.parseDouble(gradeStr);
                    Course course = gradebook.getCourse(courseName);
                    if (course != null) {
                        course.setGrade(assignmentName, student, grade);
                        gradebook.saveToFile();
                        JOptionPane.showMessageDialog(this, "Grade saved successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Course not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid grade format");
                }
            });

            createAssignmentBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                String assignmentName = JOptionPane.showInputDialog("Enter assignment name:");
                String type = JOptionPane.showInputDialog("Enter assignment type (assignment, quiz, test):");
                Course course = gradebook.getCourse(courseName);
                if (course == null) {
                    course = new Course(courseName);
                    gradebook.addCourse(course);
                    teacher.addCourse(course);
                }
                course.createAssignment(assignmentName, type);
                JOptionPane.showMessageDialog(this, "Assignment created.");
            });

            dropAssignmentBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                String assignmentName = JOptionPane.showInputDialog("Enter assignment name to drop:");
                Course course = gradebook.getCourse(courseName);
                if (course != null) {
                    course.dropAssignment(assignmentName);
                    JOptionPane.showMessageDialog(this, "Assignment dropped.");
                }
            });

            removeAssignmentBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                String assignmentName = JOptionPane.showInputDialog("Enter assignment name to remove:");
                Course course = gradebook.getCourse(courseName);
                if (course != null) {
                    course.removeAssignment(assignmentName);
                    JOptionPane.showMessageDialog(this, "Assignment removed.");
                }
            });

            sortByNameBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                Course course = gradebook.getCourse(courseName);
                if (course != null) {
                    course.getAssignments().sort(Comparator.comparing(Assignment::getAssignment));
                    JOptionPane.showMessageDialog(this, "Assignments sorted by name.");
                }
            });

            sortByGradeBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                Course course = gradebook.getCourse(courseName);
                if (course != null) {
                    course.getAssignments().sort((a1, a2) -> Double.compare(a2.getAllGrades(), a1.getAllGrades()));
                    JOptionPane.showMessageDialog(this, "Assignments sorted by grade sum.");
                }
            });

            changeWeightBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                String type = JOptionPane.showInputDialog("Enter assignment type to change weight:");
                double newWeight = Double.parseDouble(JOptionPane.showInputDialog("Enter new weight:"));
                Course course = gradebook.getCourse(courseName);
                if (course != null) {
                    boolean success = course.setWeight(type, newWeight);
                    if (success) JOptionPane.showMessageDialog(this, "Weight updated.");
                    else JOptionPane.showMessageDialog(this, "Failed to update weight (total != 1.0)");
                }
            });

            createGroupBtn.addActionListener(e -> {
                String courseName = JOptionPane.showInputDialog("Enter course name:");
                int size = Integer.parseInt(JOptionPane.showInputDialog("Enter group size:"));
                Course course = gradebook.getCourse(courseName);
                if (course != null) {
                    List<ArrayList<Student>> groups = course.makeGroups(size);
                    StringBuilder sb = new StringBuilder("Generated Groups:\n");
                    int i = 1;
                    for (List<Student> group : groups) {
                        sb.append("Group ").append(i++).append(": ");
                        sb.append(group.stream().map(Student::getUsername).collect(Collectors.joining(", "))).append("\n");
                    }
                    JOptionPane.showMessageDialog(this, sb.toString());
                }
            });

            viewGradebookBtn.addActionListener(e -> {
                StringBuilder sb = new StringBuilder("Gradebook:\n");
                for (String courseName : gradebook.getAllCourseNames()) {
                    Course course = gradebook.getCourse(courseName);
                    sb.append("Course: ").append(courseName).append("\n");
                    for (String student : gradebook.getAllUsernames()) {
                        User u = gradebook.getUser(student);
                        if (u instanceof Student) {
                            sb.append("  Student: ").append(student).append("\n");
                            for (var entry : course.getGrades(student).entrySet()) {
                                sb.append("    ").append(entry.getKey()).append(" → ").append(entry.getValue()).append("\n");
                            }
                        }
                    }
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            });

            backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        }
    }

    private class AdminPanel extends JPanel {
        public AdminPanel(Admin admin) {
            setLayout(new BorderLayout());

            JLabel welcome = new JLabel("Welcome, Admin " + admin.getUsername());
            add(welcome, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            JButton addStudentButton = new JButton("Add Student(s) to Course");
            JButton backButton = new JButton("Back to Login");

            buttonPanel.add(addStudentButton);
            buttonPanel.add(backButton);
            add(buttonPanel, BorderLayout.SOUTH);

            addStudentButton.addActionListener(e -> {
                JTextArea studentInput = new JTextArea(5, 20);
                JScrollPane scrollPane = new JScrollPane(studentInput);
                String courseName = (String) JOptionPane.showInputDialog(this, "Enter course name:");

                int option = JOptionPane.showConfirmDialog(this, scrollPane, "Enter student usernames (one per line)", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION && courseName != null) {
                    String[] usernames = studentInput.getText().split("\\n");
                    Course course = gradebook.getCourse(courseName);
                    if (course != null) {
                        int added = 0;
                        for (String user : usernames) {
                            if (gradebook.addStudentToCourse(user.trim(), courseName)) {
                                added++;
                            }
                        }
                        JOptionPane.showMessageDialog(this, added + " student(s) added to " + courseName);
                        gradebook.saveToFile();
                    } else {
                        JOptionPane.showMessageDialog(this, "Course not found.");
                    }
                }
            });

            backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        }
    }

    public static class Admin extends Teacher {
        public Admin(String username, Gradebook gradebook) {
            super(username, gradebook);
            this.role = "admin";
        }
    }
}

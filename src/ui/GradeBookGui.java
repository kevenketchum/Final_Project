package ui;

import javax.swing.*;
import java.awt.*;
import logic.SecurityManager;
import model.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

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

            List<Course> enrolledCourses = student.getCourseList();
            JPanel coursePanel = new JPanel();
            coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

            double weightedTotal = 0.0;
            int courseCount = 0;

            for (Course c : enrolledCourses) {
                if (c.isCourseOngoing()) {
                    List<Assignment> assignments = c.getAssignments();
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
                    coursePanel.add(new JLabel("Course: " + c.getName()));
                    coursePanel.add(new JScrollPane(gradeTable));
                    weightedTotal += c.getStudentGrade(student.getUsername());
                    courseCount++;
                }
            }

            double weightedAverage = courseCount == 0 ? 0.0 : weightedTotal / courseCount;
            JLabel average = new JLabel("Weighted Average: " + String.format("%.2f", weightedAverage));

            add(coursePanel, BorderLayout.CENTER);
            add(average, BorderLayout.SOUTH);

            JButton backButton = new JButton("Back to Login");
            backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
            add(backButton, BorderLayout.NORTH);
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
            JButton backButton = new JButton("Back to Login");

            buttonPanel.add(viewGradebookBtn);
            buttonPanel.add(assignGradeBtn);
            buttonPanel.add(createAssignmentBtn);
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

            backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        }

        private List<String> getAllStudentNames() {
            List<String> students = new ArrayList<>();
            for (String username : gradebook.getAllUsernames()) {
                User user = gradebook.getUser(username);
                if (user instanceof Student) {
                    students.add(user.getUsername());
                }
            }
            return students;
        }
    }

    private class AdminPanel extends JPanel {
        public AdminPanel(Admin admin) {
            setLayout(new BorderLayout());

            JLabel welcome = new JLabel("Welcome, Admin " + admin.getUsername());
            add(welcome, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            JButton addStudentButton = new JButton("Add Student to Course");
            JButton backButton = new JButton("Back to Login");

            Set<String> courseNames = gradebook.getAllCourseNames();
            String[] coursesArray = courseNames.toArray(new String[0]);

            buttonPanel.add(addStudentButton);
            buttonPanel.add(backButton);
            add(buttonPanel, BorderLayout.SOUTH);

            addStudentButton.addActionListener(e -> {
                String student = JOptionPane.showInputDialog("Enter student username:");
                if (coursesArray.length == 0) {
                    JOptionPane.showMessageDialog(this, "No courses available. Teachers must create courses first.");
                    return;
                }
                String courseName = (String) JOptionPane.showInputDialog(
                        this, "Select course:", "Course", JOptionPane.QUESTION_MESSAGE, null, coursesArray, coursesArray[0]);

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

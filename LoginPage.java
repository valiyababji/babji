import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPage extends JFrame {
    // JDBC variables
    private static final String DB_URL = "jdbc:mysql://localhost:3306/logindb";
    private static final String DB_USER = "root"; // Change to your database username
    private static final String DB_PASSWORD = "Babji1234"; // Change to your database password

    // Swing components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginPage() {
        // Frame settings
        setTitle("Login Page");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel for input fields
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        panel.add(loginButton);

        // Status label for messages
        statusLabel = new JLabel("", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);

        // Event handling for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        setVisible(true);
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateLogin(username, password)) {
            statusLabel.setText("Login successful!");
        } else {
            statusLabel.setText("Login failed. Please try again.");
        }
    }

    // Method to validate login credentials
    private boolean validateLogin(String username, String password) {
        boolean isValid = false;
        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if user exists
            if (resultSet.next()) {
                isValid = true;
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValid;
    }

    public static void main(String[] args) {
        // Run the login page
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage();
            }
        });
    }
}

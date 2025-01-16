import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.*;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.Arrays;

public class ResumeBuilder extends Application {
    private ResumeData resumeData = new ResumeData();
    private final UserData userData = new UserData();
     // In-memory database to store user accounts (username -> email and password)
    private final Map<String, String[]> usersDatabase = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Resume Builder");

	 // Load user data from file
        usersDatabase.putAll(userData.loadUserData());

        // Login / Create Account Page
        BorderPane loginPage = createLoginPage(primaryStage);
        Scene loginScene = new Scene(loginPage, 400, 300);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private BorderPane createLoginPage(Stage primaryStage) {
        BorderPane loginPage = new BorderPane();

        // UI Elements for Login
        Label loginLabel = new Label("Login");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
	CheckBox showPasswordCheckbox = new CheckBox("Show Password");
        TextField passwordVisibleField = new TextField();
        passwordVisibleField.managedProperty().bind(showPasswordCheckbox.selectedProperty());
        passwordVisibleField.visibleProperty().bind(showPasswordCheckbox.selectedProperty());
        passwordField.managedProperty().bind(showPasswordCheckbox.selectedProperty().not());
        passwordField.visibleProperty().bind(showPasswordCheckbox.selectedProperty().not());
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());

        Button loginButton = new Button("Login");
        Button createAccountButton = new Button("Create Account");

        loginButton.setOnAction(e -> {
    String email = emailField.getText();
    String password = passwordField.getText();

    // Check if both fields are filled
    if (email.isEmpty() || password.isEmpty()) {
        showMessageDialog("Warning", "Please fill out both the email and password fields.");
        return;
    }

    // Email format validation
    if (!email.matches("^[\\w._%+-]+@gmail\\.com$")) {
        showMessageDialog("Error", "Please enter a valid email (e.g., example@gmail.com).");
        emailField.requestFocus();
        return;
    }

     // Authenticate user (Match email and password)
    boolean validCredentials = usersDatabase.values().stream()
            .anyMatch(user -> user[0].equals(email) && user[1].equals(password));

    if (validCredentials) {
        showResumeBuilderPage(primaryStage);
    } else {
        showMessageDialog("Error", "Invalid login credentials.");
    }
});

        createAccountButton.setOnAction(e -> 
            showCreateAccountPage(primaryStage));
 
       // Layout for login form
        VBox formLayout = new VBox(10);
        formLayout.getChildren().addAll(loginLabel, emailField, passwordField, passwordVisibleField, showPasswordCheckbox, loginButton, createAccountButton);
        formLayout.setSpacing(10);

        loginPage.setCenter(formLayout);
        return loginPage;
    }


private void showCreateAccountPage(Stage primaryStage) {
    BorderPane createAccountPage = new BorderPane();

    // UI Elements for Create Account
    Label createAccountLabel = new Label("Create Account");
    TextField usernameField = new TextField();
    usernameField.setPromptText("Username");
    TextField emailField = new TextField();
    emailField.setPromptText("Email");
    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Password");

    // Show password functionality
    TextField passwordTextField = new TextField();
    passwordTextField.setPromptText("Password");
    passwordTextField.setVisible(false);

    CheckBox showPasswordCheckbox = new CheckBox("Show Password");
    showPasswordCheckbox.setOnAction(e -> {
        if (showPasswordCheckbox.isSelected()) {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
        }
    });

    Button createButton = new Button("Create Account");

    createButton.setOnAction(e -> {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.isVisible() ? passwordField.getText() : passwordTextField.getText();

        // Validations
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showMessageDialog("Warning", "All fields are required.");
            return;
        }

        if (!email.matches("^[\\w._%+-]+@gmail\\.com$")) {
            showMessageDialog("Error", "Please enter a valid email (e.g., example@gmail.com).");
            emailField.requestFocus();
            return;
        }

        if (password.length() < 6) {
            showMessageDialog("Error", "Password must be at least 6 characters long.");
            passwordField.requestFocus();
            return;
        }
	

            // Check for duplicate accounts
            if (usersDatabase.containsKey(username)) {
                showMessageDialog("Error", "Username already exists. Please choose another.");
                return;
            }

            boolean emailExists = usersDatabase.values().stream().anyMatch(details -> details[0].equals(email));
            if (emailExists) {
                showMessageDialog("Error", "An account with this email already exists.");
                return;
            }

            // Save account to database
            usersDatabase.put(username, new String[]{email, password});
            userData.saveUserData(usersDatabase); // Save to file
            showMessageDialog("Success", "Account created successfully.");
            showResumeBuilderPage(primaryStage);
    
 });
      
    VBox createAccountForm = new VBox(10, createAccountLabel, usernameField, emailField, passwordField, passwordTextField, showPasswordCheckbox, createButton);
    createAccountForm.setPadding(new Insets(20));
    createAccountPage.setCenter(createAccountForm);

    Scene createAccountScene = new Scene(createAccountPage, 400, 300);
    primaryStage.setScene(createAccountScene);
}
   private void showResumeBuilderPage(Stage primaryStage) {
    // UI elements for Resume Builder Page
    BorderPane resumePage = new BorderPane();
    Label label = new Label("Welcome to the Resume Builder!");
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);

    VBox mainForm = new VBox(20);
    mainForm.setPadding(new Insets(20));
    scrollPane.setContent(mainForm);

    // Personal Information Section
    VBox personalInfoSection = new VBox(10);
    personalInfoSection.getChildren().add(new Label("Personal Information"));

    TextField fullNameField = new TextField();
    fullNameField.setPromptText("Full Name");
    TextField phoneField = new TextField(); // Initialize phoneField
    phoneField.setPromptText("Phone Number (10 digits)");
    TextField emailField = new TextField();
    emailField.setPromptText("Email Address (must be @gmail.com)");

    TextArea permanentAddressField = new TextArea(); // Initialize addressField
    permanentAddressField.setPromptText("Permanent Address");

    TextField linkedinField = new TextField();
    linkedinField.setPromptText("LinkedIn ID");

    TextField githubField = new TextField();
    githubField.setPromptText("GitHub ID");

    TextArea profileSummaryField = new TextArea();
    profileSummaryField.setPromptText("Profile Summary");

    // Profile Picture Section
    ImageView profileImageView = new ImageView(new Image("file:///C:/Users/thris/ResumeBuilder/src/Default_Profile.jpg"));
    profileImageView.setFitWidth(100);
    profileImageView.setFitHeight(100);
    Button uploadImageButton = new Button("Upload Image");
    uploadImageButton.setOnAction(e -> {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", ".png", ".jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            profileImageView.setImage(new Image(selectedFile.toURI().toString()));
            resumeData.setProfilePicturePath(selectedFile.getAbsolutePath());
        }
    });

    // Add components to layout
    personalInfoSection.getChildren().addAll(fullNameField, phoneField, emailField, permanentAddressField, linkedinField, githubField, profileSummaryField, uploadImageButton, profileImageView);

    // Section: Education
    VBox educationSection = new VBox(10);
    educationSection.getChildren().add(new Label("Education"));

    TextField collegeNameField = new TextField(); // Initialize collegeNameField
    collegeNameField.setPromptText("College/University Name");
    TextField degreeField = new TextField();
    degreeField.setPromptText("Degree");
    DatePicker graduationYearField = new DatePicker(); // Initialize graduationYearField
    graduationYearField.setPromptText("Year of Graduation");
    TextField cgpaField = new TextField();
    cgpaField.setPromptText("CGPA");

    educationSection.getChildren().addAll(collegeNameField, degreeField, graduationYearField, cgpaField);

    // Section: Experience
    VBox experienceSection = new VBox(10);
    experienceSection.getChildren().add(new Label("Experience"));

    CheckBox hasExperienceCheckbox = new CheckBox("Do you have any prior experience?");
    VBox experienceDetails = new VBox(10);
    experienceDetails.setDisable(true);

    TextField jobTitleField = new TextField();
    jobTitleField.setPromptText("Job Title");
    TextField companyNameField = new TextField();
    companyNameField.setPromptText("Company Name");
    DatePicker jobStartDateField = new DatePicker(); // Initialize jobStartDateField
    jobStartDateField.setPromptText("Start Date");
    DatePicker jobEndDateField = new DatePicker(); // Initialize jobEndDateField
    jobEndDateField.setPromptText("End Date");

    experienceDetails.getChildren().addAll(jobTitleField, companyNameField, jobStartDateField, jobEndDateField);
    hasExperienceCheckbox.setOnAction(e -> experienceDetails.setDisable(!hasExperienceCheckbox.isSelected()));

    experienceSection.getChildren().addAll(hasExperienceCheckbox, experienceDetails);

    // Section: Skills
    VBox skillsSection = new VBox(10);
    skillsSection.getChildren().add(new Label("Skills"));

    TextField skillsField = new TextField();
    skillsField.setPromptText("Skills (comma-separated)");

    skillsSection.getChildren().add(skillsField);

    // Section: Projects
    VBox projectsSection = new VBox(10);
    projectsSection.getChildren().add(new Label("Projects"));

    TextField projectsField = new TextField();
    projectsField.setPromptText("Projects (comma-separated)");

    projectsSection.getChildren().add(projectsField);

    // Section: Publications
    VBox publicationsSection = new VBox(10);
    publicationsSection.getChildren().add(new Label("Publications"));

    TextField publicationsField = new TextField();
    publicationsField.setPromptText("Publications (comma-separated)");

    publicationsSection.getChildren().add(publicationsField);

    // Section: Certifications
    VBox certificationsSection = new VBox(10);
    certificationsSection.getChildren().add(new Label("Certifications"));

    TextField certificationTitleField = new TextField();
    certificationTitleField.setPromptText("Certification Title");
    TextField issuingAuthorityField = new TextField();
    issuingAuthorityField.setPromptText("Issuing Authority");
    DatePicker certificationDateField = new DatePicker(); // Initialize certificationDateField
    certificationDateField.setPromptText("Certification Date");

    certificationsSection.getChildren().addAll(certificationTitleField, issuingAuthorityField, certificationDateField);

    // Section: Languages
    VBox languagesSection = new VBox(10);
    languagesSection.getChildren().add(new Label("Languages"));

    TextField languagesField = new TextField();
    languagesField.setPromptText("Languages Known (comma-separated)");

    languagesSection.getChildren().add(languagesField);

    // Section: Hobbies
    VBox hobbiesSection = new VBox(10);
    hobbiesSection.getChildren().add(new Label("Hobbies and Interests"));

    TextField hobbiesField = new TextField();
    hobbiesField.setPromptText("Hobbies (comma-separated)");

    hobbiesSection.getChildren().add(hobbiesField);

   // Final Button to Generate PDF
Button generatePDFButton = new Button("Generate PDF"); // Initialize generatePDFButton
generatePDFButton.setOnAction(e -> {
    // Make sure to get the text from input fields and assign to resumeData
    resumeData.setFullName(fullNameField.getText());
    resumeData.setPhoneNumber(phoneField.getText());
    resumeData.setEmail(emailField.getText());
    resumeData.setPermanentAddress(permanentAddressField.getText());
    resumeData.setLinkedin(linkedinField.getText());
    resumeData.setGithub(githubField.getText());
    resumeData.setProfileSummary(profileSummaryField.getText());
    resumeData.setCollegeName(collegeNameField.getText());
    resumeData.setDegree(degreeField.getText());
    resumeData.setGraduationYear(graduationYearField.getValue() != null ? graduationYearField.getValue().toString() : "");
    resumeData.setCgpa(cgpaField.getText());
    resumeData.setJobTitle(jobTitleField.getText());
    resumeData.setCompanyName(companyNameField.getText());
    resumeData.setJobStartDate(jobStartDateField.getValue() != null ? jobStartDateField.getValue().toString() : "");
    resumeData.setJobEndDate(jobEndDateField.getValue() != null ? jobEndDateField.getValue().toString() : "");
    resumeData.setSkills(Arrays.asList(skillsField.getText().split(",")));
    resumeData.setProjects(Arrays.asList(projectsField.getText().split(",")));
    resumeData.setPublications(publicationsField.getText());
    resumeData.setCertificationTitle(certificationTitleField.getText());
    resumeData.setIssuingAuthority(issuingAuthorityField.getText());
    resumeData.setCertificationDate(certificationDateField.getValue() != null ? certificationDateField.getValue().toString() : "");
    resumeData.setLanguages(languagesField.getText());
    resumeData.setHobbies(hobbiesField.getText());
    resumeData.setProfilePicturePath(profileImageView.getImage().getUrl());

    // Now generate PDF
    PDFGenerator.generatePDF(resumeData);

    // Show success message
    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
    successAlert.setTitle("Success");
    successAlert.setHeaderText("Resume PDF Created");
    successAlert.setContentText("Your resume PDF has been created successfully.");
    successAlert.showAndWait(); // Wait for the user to close the dialog
});


    // Add all sections to the main form
    mainForm.getChildren().addAll(personalInfoSection, educationSection, experienceSection, skillsSection, projectsSection, publicationsSection, certificationsSection, languagesSection, hobbiesSection, generatePDFButton);

    // Set up the scene and stage
    resumePage.setCenter(scrollPane);
    Scene scene = new Scene(resumePage, 800, 600);
    primaryStage.setTitle("Resume Builder");
    primaryStage.setScene(scene);
    primaryStage.show();
}

    private void showMessageDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
# Java-based Resume Builder Application

## Overview
A JavaFX-based desktop application designed to help users create and manage their resumes. The application allows users to input personal and professional details and generates a professional PDF resume.

## Features
- **User Authentication**: Secure login and account creation with email and password validation.
- **Persistent Storage**: Stores user data in a JSON file, ensuring persistence across sessions.
- **Resume Generation**: Users can input personal, educational, and work details to generate a custom PDF resume.
- **Interactive GUI**: 
  - **FileChooser** for uploading a profile picture.
  - **Datefixer** for formatting dates correctly.
  - Custom dialogs for success, error, and warning messages.
- **PDF Generation**: 
  - Generates a fully formatted PDF resume with hyperlinks (LinkedIn, GitHub, Email).
  - Includes page borders, footer, and page numbers.
  
## Technologies Used
- **JavaFX**: Used for creating the graphical user interface (GUI).
- **iTextPDF**: Used to generate professional PDF resumes.
- **Jackson (Core, Annotations, Databind)**: Used for JSON handling to persist user data.


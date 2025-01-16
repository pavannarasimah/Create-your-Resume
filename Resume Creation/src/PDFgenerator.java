import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.net.URI;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

class PDFGenerator {

    public static void generatePDF(ResumeData resumeData) {
        Document document = new Document(PageSize.A4);
        try {
            // Set the file path to save the PDF
	    
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Resume_" + resumeData.getFullName() + ".pdf"));
		writer.setPageEvent(new BorderPageEvent());
		writer.setPageEvent(new Footer());
            document.open();


            // Add header
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph header = new Paragraph("Resume Builder Application", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(Chunk.NEWLINE);  // Add space after header

             // Border for the entire page
PdfContentByte cb = writer.getDirectContent();
cb.setLineWidth(1);

// Reducing the margins to move the border closer to the edges
cb.rectangle(10, 10, document.getPageSize().getWidth() - 20, document.getPageSize().getHeight() - 20); // 10 units from each edge
cb.stroke();


	      // Profile Picture Section (optional)
            if (resumeData.getProfilePicturePath() != null && !resumeData.getProfilePicturePath().isEmpty()) {
                Image profilePic = Image.getInstance(resumeData.getProfilePicturePath());
		profilePic.scaleToFit(130, 130);
                profilePic.setAbsolutePosition(430,635);
		profilePic.setBorderWidth(5);
		profilePic.setBorderColor(BaseColor.BLACK);
    		profilePic.setBorder(Rectangle.BOX);
                document.add(profilePic);
            }

            // Personal Information Section
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
	    Font contentFont = new Font(Font.FontFamily.HELVETICA, 12);

            document.add(new Paragraph("Personal Information", sectionFont));
	    document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Full Name: " + resumeData.getFullName(),contentFont));
	    document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Phone Number: " + resumeData.getPhoneNumber(),contentFont));
           document.add(Chunk.NEWLINE);
	    // Create a custom link font with blue color
// Define the font for the clickable email link
Font linkFont = new Font(contentFont.getFamily(), contentFont.getSize(), contentFont.getStyle(), BaseColor.BLUE);

// Create a paragraph for the "Email: " text with the appropriate font
Paragraph emailLabel = new Paragraph("Email: ", contentFont);
document.add(emailLabel);  // Add the "Email: " text to the document

// Create the clickable email link with the custom blue color font
Anchor emailLink = new Anchor(resumeData.getEmail(), linkFont);  // Only the email will be clickable

// Set the reference as the email address with the 'mailto:' protocol
emailLink.setReference("mailto:" + resumeData.getEmail());  // The link is clickable and includes the mailto: protocol

// Add the clickable email link to the document
document.add(emailLink);

	    document.add(Chunk.NEWLINE);
	    document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Permanent Address: " + resumeData.getPermanentAddress(),contentFont));
	    document.add(Chunk.NEWLINE);

           // Create the label text for LinkedIn (plain text)
document.add(new Paragraph("LinkedIn: "));

// Create the hyperlink for the LinkedIn URL
Anchor linkedIn = new Anchor(resumeData.getLinkedin(), linkFont);
linkedIn.setReference(resumeData.getLinkedin()); // Make the URL clickable
document.add(linkedIn);

document.add(Chunk.NEWLINE);

// Create the label text for GitHub (plain text)
document.add(new Paragraph("GitHub: "));

// Create the hyperlink for the GitHub URL
Anchor github = new Anchor(resumeData.getGithub(), linkFont);
github.setReference(resumeData.getGithub()); // Make the URL clickable
document.add(github);

document.add(Chunk.NEWLINE);

	
	PdfPTable table = new PdfPTable(1);
table.setWidthPercentage(100);

// Create a Paragraph for the Profile Summary label
Paragraph profileSummaryLabel = new Paragraph("Profile Summary:");  // Use sectionFont or any desired font for the label
profileSummaryLabel.setSpacingBefore(10); // Space before the label
document.add(profileSummaryLabel);  // Add the label to the document
document.add(Chunk.NEWLINE);
// Create a Paragraph for the Profile Summary content (user-provided data)
Paragraph profileSummaryContent = new Paragraph(resumeData.getProfileSummary(), contentFont);  // Use contentFont for the content
profileSummaryContent.setSpacingBefore(10); // Space before the content
profileSummaryContent.setSpacingAfter(10); // Space after the content

// Create a cell for the Profile Summary content with the border
PdfPCell contentCell = new PdfPCell(profileSummaryContent);
contentCell.setBorder(Rectangle.BOX); 
contentCell.setPadding(10);  // Padding inside the box
contentCell.setBorderColor(BaseColor.BLACK);
contentCell.setBorderWidth(1);

// Create a table for the content (you can use one cell for simplicity)
PdfPTable profileSummaryTable = new PdfPTable(1);  // One column for the content
profileSummaryTable.setWidthPercentage(100);  // Set table width to 100% of page width
profileSummaryTable.addCell(contentCell);  // Add the content cell to the table

// Add the table to the document
document.add(profileSummaryTable);
document.add(Chunk.NEWLINE);  // Optional: Add a new line after the table for spacing

// Education Section with Table
document.add(new Paragraph("Education", sectionFont));
document.add(Chunk.NEWLINE);  // Space between section title and table

// Creating the table with 4 columns (Degree, Institution, Year of Graduation, CGPA)
PdfPTable educationTable = new PdfPTable(4); // Four columns: Degree, Institution, Year of Graduation, CGPA
educationTable.setWidthPercentage(100);  // Set table width to 100%

// Adding headers for the table with padding for spacing
PdfPCell educationHeaderCell = new PdfPCell(new Phrase("Degree", contentFont));
educationHeaderCell.setPadding(10);  // Add padding to the header cells for more space
educationTable.addCell(educationHeaderCell);

educationHeaderCell = new PdfPCell(new Phrase("Institution", contentFont));
educationHeaderCell.setPadding(10);  // Add padding to the header cells
educationTable.addCell(educationHeaderCell);

educationHeaderCell = new PdfPCell(new Phrase("Year of Graduation", contentFont));
educationHeaderCell.setPadding(10);  // Add padding to the header cells
educationTable.addCell(educationHeaderCell);

educationHeaderCell = new PdfPCell(new Phrase("CGPA", contentFont));
educationHeaderCell.setPadding(10);  // Add padding to the header cells
educationTable.addCell(educationHeaderCell);

// Adding content to the table (Degree, College Name, Graduation Year, CGPA)
PdfPCell educationContentCell = new PdfPCell(new Phrase(resumeData.getDegree(), contentFont));
educationContentCell.setPadding(10);  // Add padding to the content cells for more space
educationTable.addCell(educationContentCell);

educationContentCell = new PdfPCell(new Phrase(resumeData.getCollegeName(), contentFont));
educationContentCell.setPadding(10);  // Add padding to the content cells
educationTable.addCell(educationContentCell);

educationContentCell = new PdfPCell(new Phrase(resumeData.getGraduationYear(), contentFont));  // Assuming there's a method to get the graduation year
educationContentCell.setPadding(10);  // Add padding to the content cells
educationTable.addCell(educationContentCell);

educationContentCell = new PdfPCell(new Phrase(resumeData.getCgpa(), contentFont));  // Assuming there's a method to get the CGPA
educationContentCell.setPadding(10);  // Add padding to the content cells
educationTable.addCell(educationContentCell);

// Adding the table to the document
document.add(educationTable);

// Adding extra space after the table for better separation
document.add(Chunk.NEWLINE);  // Space between the table and next content

// Optional: Increase spacing between sections
document.add(Chunk.NEWLINE);  // Additional space between sections


            // Experience Section
            if (resumeData.getJobTitle() != null && !resumeData.getJobTitle().isEmpty()) {
                document.add(new Paragraph("Experience", sectionFont));
                document.add(new Paragraph("Job Title: " + resumeData.getJobTitle()));
                document.add(new Paragraph("Company: " + resumeData.getCompanyName()));
                document.add(new Paragraph("Duration: " + resumeData.getJobStartDate() + " - " + resumeData.getJobEndDate()));
                document.add(Chunk.NEWLINE);
            }

            // Skills Section (Bullet points)
            document.add(new Paragraph("Skills", sectionFont));
	    document.add(Chunk.NEWLINE);

           com.itextpdf.text.List skillsList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
            for (String skill : resumeData.getSkills()) {
                skillsList.add(new ListItem(skill, contentFont));
            }
            document.add(skillsList);

            document.add(Chunk.NEWLINE);

           // Projects Section (Bullet points)
            document.add(new Paragraph("Projects", sectionFont));
	   document.add(Chunk.NEWLINE);

	  com.itextpdf.text.List projectsList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
	for (String project : resumeData.getProjects()) {
    		projectsList.add(new ListItem(project, contentFont));
	}
	document.add(projectsList);


            document.add(Chunk.NEWLINE);

            // Publications Section with Bullet Points
document.add(new Paragraph("Publications", sectionFont));
document.add(Chunk.NEWLINE);

// Create a List for the publications (unordered list with bullet points)
com.itextpdf.text.List publicationsList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED); // Unordered list

// Adding publications to the list (split by commas if multiple publications)
String[] publications = resumeData.getPublications().split(","); // Assumes publications are stored as a comma-separated string
for (String publication : publications) {
    publicationsList.add(new ListItem(publication.trim(), contentFont)); // Add each publication as a list item
}

// Add the list to the document
document.add(publicationsList);

document.add(Chunk.NEWLINE);


// Certifications Section with Table
document.add(new Paragraph("Certification", sectionFont));
document.add(Chunk.NEWLINE);  // Space between section title and table

// Creating the table with 3 columns (Certification Title, Issuing Authority, Certification Date)
PdfPTable certificationTable = new PdfPTable(3); // Three columns: Certification Title, Issuing Authority, Certification Date
certificationTable.setWidthPercentage(100);  // Set table width to 100%

// Adding headers for the table with padding for spacing
PdfPCell certificationHeaderCell = new PdfPCell(new Phrase("Certification Title", contentFont));
certificationHeaderCell.setPadding(10);  // Add padding to the header cells for more space
certificationTable.addCell(certificationHeaderCell);

certificationHeaderCell = new PdfPCell(new Phrase("Issuing Authority", contentFont));
certificationHeaderCell.setPadding(10);  // Add padding to the header cells
certificationTable.addCell(certificationHeaderCell);

certificationHeaderCell = new PdfPCell(new Phrase("Certification Date", contentFont));
certificationHeaderCell.setPadding(10);  // Add padding to the header cells
certificationTable.addCell(certificationHeaderCell);

// Adding content to the table (Certification Title, Issuing Authority, Certification Date)
PdfPCell certificationContentCell = new PdfPCell(new Phrase(resumeData.getCertificationTitle(), contentFont));
certificationContentCell.setPadding(10);  // Add padding to the content cells for more space
certificationTable.addCell(certificationContentCell);

certificationContentCell = new PdfPCell(new Phrase(resumeData.getIssuingAuthority(), contentFont));
certificationContentCell.setPadding(10);  // Add padding to the content cells
certificationTable.addCell(certificationContentCell);

certificationContentCell = new PdfPCell(new Phrase(resumeData.getCertificationDate(), contentFont));
certificationContentCell.setPadding(10);  // Add padding to the content cells
certificationTable.addCell(certificationContentCell);

// Adding the table to the document
document.add(certificationTable);

// Adding extra space between table and next section
document.add(Chunk.NEWLINE);  // Extra space after the table for better separation

// Optional: Increase spacing between sections
document.add(Chunk.NEWLINE);  // Additional space between the Certifications section and the next section

            // Languages Section with Bullet Points
	document.add(new Paragraph("Languages", sectionFont));
	document.add(Chunk.NEWLINE);


	// Create a List for the languages
	com.itextpdf.text.List languagesList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED); // Unordered list (bullet points)

	// Adding languages to the list (split by commas if multiple languages)
	String[] languages = resumeData.getLanguages().split(","); // Assumes languages are stored as a comma-separated string
	for (String language : languages) {
    		languagesList.add(new ListItem(language.trim(), contentFont)); // Add each language as a list item
	}

	// Add the list to the document
	document.add(languagesList);

	// Adding space between sections
	document.add(Chunk.NEWLINE);

            // Hobbies Section with Bullet Points
	document.add(new Paragraph("Hobbies and Interests", sectionFont));
	document.add(Chunk.NEWLINE);


	// Create a List for the hobbies
	com.itextpdf.text.List hobbiesList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED); // Unordered list (bullet points)

	// Adding hobbies to the list (split by commas if multiple hobbies)
	String[] hobbies = resumeData.getHobbies().split(","); // Assumes hobbies are stored as a comma-separated string
	for (String hobby : hobbies) {
    		hobbiesList.add(new ListItem(hobby.trim(), contentFont)); // Add each hobby as a list item
	}

	// Add the list to the document
	document.add(hobbiesList);

	// Adding space between sections
	document.add(Chunk.NEWLINE);

              // Closing the document
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
   // Make BorderPageEvent public or package-private (default access level)
public static class BorderPageEvent extends PdfPageEventHelper {
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // Get the content byte to draw the border
        PdfContentByte cb = writer.getDirectContent();
        cb.setLineWidth(1);  // Set line width for the border

        // Adjust the margins to move the border closer to the edges
        cb.rectangle(10, 10, document.getPageSize().getWidth() - 20, document.getPageSize().getHeight() - 20); 
        // 10 units from each edge, reducing the size of the rectangle
        cb.stroke();  // Apply the stroke to draw the border
    }
}


    public static class Footer extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                Phrase footer = new Phrase("Page " + writer.getPageNumber());
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                        footer, 520, 30, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
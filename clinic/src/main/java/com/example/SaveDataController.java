package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/data")
public class SaveDataController {

	
	@Autowired
    private JavaMailSender mailSender;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveData(@RequestParam Map<String, String> params) {
		// Retrieve form data
		String regNo = params.getOrDefault("regNo", "");
		String date = params.getOrDefault("date", "");
		String patientName = params.getOrDefault("patientName", "");
		String ageStr = params.getOrDefault("age", "0");
		String genderGroup = params.getOrDefault("genderGroup", "");
		String occupation = params.getOrDefault("occupation", "");
		String address = params.getOrDefault("address", "");
		String diagnosis = params.getOrDefault("diagnosis", "");
		String prescriptionVisit1 = params.getOrDefault("prescriptionVisit1", "");
		String prescriptionFollowups = params.getOrDefault("prescriptionFollowups", "");
		String amount1Str = params.getOrDefault("amount1", "0.0");
		String amount2Str = params.getOrDefault("amount2", "0.0");
		String amount3Str = params.getOrDefault("amount3", "0.0");
		String amount4Str = params.getOrDefault("amount4", "0.0");
		String amount5Str = params.getOrDefault("amount5", "0.0");
		String mobileNumber = params.getOrDefault("mobileNumber", "");
		 String email = params.getOrDefault("email", ""); 
		mobileNumber = formatMobileNumber(mobileNumber);

		// Parse numeric fields
		int age = parseInt(ageStr);
		double amount1 = parseDouble(amount1Str);
		double amount2 = parseDouble(amount2Str);
		double amount3 = parseDouble(amount3Str);
		double amount4 = parseDouble(amount4Str);
		double amount5 = parseDouble(amount5Str);

		// Define the file path to save the Excel file
		// String filePath = "D:\\clinic development\\patient_details.xlsx"; // Change
		// this to a valid path on your system

		String filePath = getFilePath();

		Workbook workbook = null;
		Sheet sheet;

		File file = new File(filePath);

		try {
			if (file.exists()) {
				// If file exists, open it
				FileInputStream fis = new FileInputStream(file);
				workbook = new XSSFWorkbook(fis);
				sheet = workbook.getSheetAt(0);
				fis.close();
			} else {
				// If file does not exist, create a new workbook and sheet
				workbook = new XSSFWorkbook();
				sheet = workbook.createSheet("Patient Details");

				// Create the header row
				Row headerRow = sheet.createRow(0);

				 String[] headers = { "Reg No", "Date", "Patient Name", "Age", "Gender", "Occupation", "Address",
		                    "Diagnosis", "Prescription Visit 1", "Prescription Follow-ups", "Amount 1", "Amount 2",
		                    "Amount 3", "Amount 4", "Amount 5", "Mobile Number", "Email" }; // Add Email header

//				String[] headers = { "Reg No", "Date", "Patient Name", "Age", "Gender", "Occupation", "Address",
//						"Diagnosis", "Prescription Visit 1", "Prescription Follow-ups", "Amount 1", "Amount 2",
//						"Amount 3", "Amount 4", "Amount 5", "Mobile Number" };

				for (int i = 0; i < headers.length; i++) {
					headerRow.createCell(i).setCellValue(headers[i]);
				}
			}

			// Check if the record already exists (based on Reg No)
			boolean isUpdated = false;
			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				String existingRegNo = row.getCell(0).getStringCellValue();
				if (existingRegNo.equals(regNo)) {
					// Update existing record
					row.getCell(1).setCellValue(date);
					row.getCell(2).setCellValue(patientName);
					row.getCell(3).setCellValue(age);
					row.getCell(4).setCellValue(genderGroup);
					row.getCell(5).setCellValue(occupation);
					row.getCell(6).setCellValue(address);
					row.getCell(7).setCellValue(diagnosis);
					row.getCell(8).setCellValue(prescriptionVisit1);
					row.getCell(9).setCellValue(prescriptionFollowups);
					row.getCell(10).setCellValue(amount1);
					row.getCell(11).setCellValue(amount2);
					row.getCell(12).setCellValue(amount3);
					row.getCell(13).setCellValue(amount4);
					row.getCell(14).setCellValue(amount5);
					row.getCell(15).setCellValue(mobileNumber);
					  row.createCell(16).setCellValue(email);// For existing record

					isUpdated = true;
					break;
				}
			}

			// If the record was not found, create a new row
			if (!isUpdated) {
				int rowCount = sheet.getLastRowNum() + 1;
				Row dataRow = sheet.createRow(rowCount);
				dataRow.createCell(0).setCellValue(regNo);
				dataRow.createCell(1).setCellValue(date);
				dataRow.createCell(2).setCellValue(patientName);
				dataRow.createCell(3).setCellValue(age);
				dataRow.createCell(4).setCellValue(genderGroup);
				dataRow.createCell(5).setCellValue(occupation);
				dataRow.createCell(6).setCellValue(address);
				dataRow.createCell(7).setCellValue(diagnosis);
				dataRow.createCell(8).setCellValue(prescriptionVisit1);
				dataRow.createCell(9).setCellValue(prescriptionFollowups);
				dataRow.createCell(10).setCellValue(amount1);
				dataRow.createCell(11).setCellValue(amount2);
				dataRow.createCell(12).setCellValue(amount3);
				dataRow.createCell(13).setCellValue(amount4);
				dataRow.createCell(14).setCellValue(amount5);
				dataRow.createCell(15).setCellValue(mobileNumber);
				 dataRow.createCell(16).setCellValue(email);
			}

			// Save the workbook to the file
			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}

			Map<String, String> patientData = new HashMap<>();
			patientData.put("patientName", patientName);
			patientData.put("regNo", regNo);
			patientData.put("date", date);
			patientData.put("age", String.valueOf(age));
			patientData.put("genderGroup", genderGroup);
			patientData.put("occupation", occupation);
			patientData.put("address", address);
			patientData.put("diagnosis", diagnosis);
			patientData.put("prescriptionVisit1", prescriptionVisit1);
			patientData.put("prescriptionFollowups", prescriptionFollowups);
			patientData.put("amount1", String.valueOf(amount1));
			patientData.put("amount2", String.valueOf(amount2));
			patientData.put("amount3", String.valueOf(amount3));
			patientData.put("amount4", String.valueOf(amount4));
			patientData.put("amount5", String.valueOf(amount5));
			patientData.put("mobileNumber", mobileNumber);

			
			
//			if (!email.isEmpty()) {
//                sendEmail(email, patientName);
//            }
			
			if (!email.isEmpty()) {
			    sendDetailedEmail(email, patientData);
			}

			
			
			return ResponseEntity.ok("Data saved successfully to " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("An error occurred while saving data: " + e.getMessage());
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
//	private void sendEmail(String email, String patientName) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Thank you for your visit!");
//        message.setText(
//                "Dear " + patientName + ",\n\n" +
//                "Thank you for visiting our clinic.\n\n" +
//                "Please find your medical bill attached.\n\n" +
//                "Best regards,\n" +
//                "Noble Homeopathy Clinic\n" +
//                "Health, Harmony, Happiness"
//            );
//        mailSender.send(message);
//    }
	
	private void sendDetailedEmail(String email, Map<String, String> patientData) {
	    String patientName = patientData.getOrDefault("patientName", "Valued Patient");
	    String regNo = patientData.getOrDefault("regNo", "N/A");
	    String date = patientData.getOrDefault("date", "N/A");
	    String age = patientData.getOrDefault("age", "N/A");
	    String genderGroup = patientData.getOrDefault("genderGroup", "N/A");
	    String occupation = patientData.getOrDefault("occupation", "N/A");
	    String address = patientData.getOrDefault("address", "N/A");
	    String diagnosis = patientData.getOrDefault("diagnosis", "N/A");
	    String prescriptionVisit1 = patientData.getOrDefault("prescriptionVisit1", "N/A");
	    String prescriptionFollowups = patientData.getOrDefault("prescriptionFollowups", "N/A");
	    String amount1 = patientData.getOrDefault("amount1", "0.0");
	    String amount2 = patientData.getOrDefault("amount2", "0.0");
	    String amount3 = patientData.getOrDefault("amount3", "0.0");
	    String amount4 = patientData.getOrDefault("amount4", "0.0");
	    String amount5 = patientData.getOrDefault("amount5", "0.0");
	    String mobileNumber = patientData.getOrDefault("mobileNumber", "N/A");

	    // Compose the email content
	    StringBuilder emailContent = new StringBuilder();
	    emailContent.append("Dear ").append(patientName).append(",\n\n");
	    emailContent.append("Thank you for visiting Noble Homeopathy Clinic!\n\n");
	    emailContent.append("Here are the details of your visit:\n");
	    emailContent.append("----------------------------------------------------------\n");
	    emailContent.append("Registration Number: ").append(regNo).append("\n");
	    emailContent.append("Visit Date: ").append(date).append("\n");
	    emailContent.append("Age: ").append(age).append("\n");
	    emailContent.append("Gender: ").append(genderGroup).append("\n");
	    emailContent.append("Occupation: ").append(occupation).append("\n");
	    emailContent.append("Address: ").append(address).append("\n");
	    emailContent.append("Diagnosis: ").append(diagnosis).append("\n");
	  
	    emailContent.append("----------------------------------------------------------\n\n");
	    emailContent.append("Charges Summary:\n");
	    emailContent.append("Visit 1: ₹").append(amount1).append("\n");
	    emailContent.append("Visit 2: ₹").append(amount2).append("\n");
	    emailContent.append("Visit 3: ₹").append(amount3).append("\n");
	    emailContent.append("Visit 4: ₹").append(amount4).append("\n");
	    emailContent.append("Visit 5: ₹").append(amount5).append("\n");
	    emailContent.append("----------------------------------------------------------\n");
	    emailContent.append("Contact Number: ").append(mobileNumber).append("\n");
	    emailContent.append("----------------------------------------------------------\n\n");
	    emailContent.append("We look forward to assisting you in achieving optimal health.\n");
	    emailContent.append("Should you have any queries, please do not hesitate to reach out to us.\n\n");
	    emailContent.append("Warm regards,\n");
	    emailContent.append("Noble Homeopathy Clinic\n");
	    emailContent.append("Health, Harmony, Happiness\n");
	    emailContent.append("+916383095276");

	    // Send the email
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(email);
	    message.setSubject("Thank You for Your Visit to Noble Homeopathy Clinic!");
	    message.setText(emailContent.toString());

	    mailSender.send(message);
	}

	
	private String formatMobileNumber(String mobileNumber) {
		// Check if the mobile number starts with "+" and the country code
		if (mobileNumber.startsWith("+91")) {
			return mobileNumber; // Already in correct format
		} else if (mobileNumber.length() == 10) {
			// If the number is 10 digits long, prepend "+91"
			return "+91" + mobileNumber;
		}
		// Return the number as is if it doesn't match the expected formats
		return mobileNumber;
	}

	// Helper method to determine the file path
	private String getFilePath() {
		String filePath = "";

		// For Windows, use the Documents folder
		if (System.getProperty("os.name").startsWith("Windows")) {
			String userHome = System.getProperty("user.home");
			filePath = userHome + "\\Documents\\patient_details.xlsx"; // Change file name as needed
		}

		// For Android/Linux, use app-specific storage (internal storage)
		else if (System.getProperty("os.name").startsWith("Linux")
				|| System.getProperty("os.name").startsWith("Android")) {
			filePath = "patient_details.xlsx"; // Default to app-specific storage on Android
		}

		return filePath;
	}

	@GetMapping("/search")
	public ResponseEntity<Map<String, String>> searchData(@RequestParam("regNo") int regNo) {
		// String filePath = "D:\\clinic development\\patient_details.xlsx"; // Change
		// this to your path
		String filePath = getFilePath();

		File file = new File(filePath);

		if (!file.exists()) {
			return ResponseEntity.status(404).body(createErrorResponse("File not found"));
		}

		try (FileInputStream fis = new FileInputStream(file)) {
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(0);

			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null)
					continue; // Skip empty rows

				Cell cell = row.getCell(0); // Assuming Reg No is in column 0
				if (cell == null)
					continue; // Skip empty cells

				int existingRegNo;
				if (cell.getCellType() == CellType.NUMERIC) {
					existingRegNo = (int) cell.getNumericCellValue(); // Read as a number
				} else if (cell.getCellType() == CellType.STRING) {
					existingRegNo = Integer.parseInt(cell.getStringCellValue()); // Read as a string and convert
				} else {
					continue; // Skip unsupported cell types
				}
				// Compare regNo as numbers
				if (existingRegNo == regNo) {

					Map<String, String> data = new HashMap<>();
					data.put("regNo", row.getCell(0).getStringCellValue());
					data.put("date", row.getCell(1).getStringCellValue());
					data.put("patientName", row.getCell(2).getStringCellValue());
					data.put("age", String.valueOf((int) row.getCell(3).getNumericCellValue()));
					data.put("genderGroup", row.getCell(4).getStringCellValue());
					data.put("occupation", row.getCell(5).getStringCellValue());
					data.put("address", row.getCell(6).getStringCellValue());
					data.put("diagnosis", row.getCell(7).getStringCellValue());
					data.put("prescriptionVisit1", row.getCell(8).getStringCellValue());
					data.put("prescriptionFollowups", row.getCell(9).getStringCellValue());
					data.put("amount1", String.valueOf(row.getCell(10).getNumericCellValue()));
					data.put("amount2", String.valueOf(row.getCell(11).getNumericCellValue()));
					data.put("amount3", String.valueOf(row.getCell(12).getNumericCellValue()));
					data.put("amount4", String.valueOf(row.getCell(13).getNumericCellValue()));
					data.put("amount5", String.valueOf(row.getCell(14).getNumericCellValue()));

					data.put("mobileNumber", row.getCell(15).getStringCellValue());
					data.put("email", row.getCell(16).getStringCellValue());
					
					return ResponseEntity.ok(data);
				}
			}

			return ResponseEntity.status(404).body(createErrorResponse("Record not found"));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(createErrorResponse("An error occurred: " + e.getMessage()));
		}
	}

	// Helper methods
	private int parseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	private Map<String, String> createErrorResponse(String message) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", message);
		return errorResponse;
	}
}

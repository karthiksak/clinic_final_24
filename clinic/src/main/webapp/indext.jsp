<!DOCTYPE html>
<html>
<head>
<title>Patient Details Form</title>
</head>
<body>
	<h2>Patient Details Form</h2>
	<form action="/data/save" method="post">

		Registration Number: <input type="text" name="regNo" required><br>
		Date: <input type="date" name="date" required><br>
		Patient Name: <input type="text" name="patientName" required><br>
		Age: <input type="number" name="age" required><br>
		Gender: <select name="genderGroup">
			<option value="Male">Male</option>
			<option value="Female">Female</option>
			<option value="Other">Other</option>
		</select><br> Occupation: <input type="text" name="occupation"><br>
		Address:
		<textarea name="address"></textarea>
		<br> Diagnosis:
		<textarea name="diagnosis"></textarea>
		<br> Prescription Visit 1:
		<textarea name="prescriptionVisit1"></textarea>
		<br> Prescription Follow-ups:
		<textarea name="prescriptionFollowups"></textarea>
		<br> Amount 1: <input type="number" step="0.01" name="amount1"><br>
		Amount 2: <input type="number" step="0.01" name="amount2"><br>
		Amount 3: <input type="number" step="0.01" name="amount3"><br>
		Amount 4: <input type="number" step="0.01" name="amount4"><br>
		Amount 5: <input type="number" step="0.01" name="amount5"><br>
		<input type="submit" value="Submit">
	</form>
</body>
</html>

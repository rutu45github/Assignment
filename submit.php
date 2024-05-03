<?php
// Check if form is submitted
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Validate reCAPTCHA
    $recaptcha_secret = "YOUR_SECRET_KEY";
    $recaptcha_response = $_POST['g-recaptcha-response'];

    $url = 'https://www.google.com/recaptcha/api/siteverify';
    $data = array(
        'secret' => $recaptcha_secret,
        'response' => $recaptcha_response
    );

    $options = array(
        'http' => array(
            'header' => "Content-type: application/x-www-form-urlencoded\r\n",
            'method' => 'POST',
            'content' => http_build_query($data)
        )
    );

    $context = stream_context_create($options);
    $response = file_get_contents($url, false, $context);
    $responseKeys = json_decode($response, true);

    if (!$responseKeys["success"]) {
        die('reCAPTCHA validation failed.');
    }

    // Process other form fields
    $animalName = $_POST['name'];
    $category = $_POST['category'];
    $description = $_POST['description']; // Add description field
    $lifeExpectancy = $_POST['life_expectancy']; // Add life expectancy field

    // Database connection
    $servername = "localhost";
    $username = "animal";
    $password = "";
    $dbname = "animal";

    $conn = new mysqli($servername, $username, $password, $dbname);
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // SQL to insert data into the database
    $sql = "INSERT INTO animals (name, category, description, life_expectancy)
            VALUES ('$animalName', '$category', '$description', '$lifeExpectancy')";

    if ($conn->query($sql) === TRUE) {
        echo "New record created successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }

    $conn->close();
}
?>

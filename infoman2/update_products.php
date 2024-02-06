<?php
include 'dbconn.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Check if all required parameters are provided
    if (!isset($_POST['product_id'], $_POST['new_product_name'], $_POST['new_unit_price'])) {
        http_response_code(400); // Bad request
        echo "Error: Missing parameters";
        exit();
    }

    // Retrieve parameters
    $productId = $_POST['product_id'];
    $newPName = $_POST['new_product_name'];
    $newPrice = $_POST['new_unit_price'];

    // Prepare the SQL statement
    $sql = "SELECT update_products($1, $2, $3)";
    $stmt = pg_prepare($conn, "update_products", $sql);

    if (!$stmt) {
        http_response_code(500); // Internal server error
        echo "Error: Unable to prepare statement";
        exit();
    }

    // Execute the prepared statement
    $result = pg_execute($conn, "update_products", array($productId, $newPName, $newPrice));

    // Check if the update was successful
    if ($result) {
        echo "Product updated successfully";
        header("Location: index.php");
        exit();
    } else {
        echo "Error in update operation: " . pg_last_error($conn);
    }
} else {
    http_response_code(405); // Method Not Allowed
    echo "Error: Invalid request method";
}

pg_close($conn);
?>
<?php
include 'dbconn.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $productId = $_POST['productId'] ?? null;

    if (!$productId) {
        echo "Error: Invalid product ID.";
        exit();
    }

    // Use parameterized query to prevent SQL injection
    $deleteFunction = "SELECT delete_products($1)";
    $stmt = pg_prepare($conn, "delete_products", $deleteFunction);

    // Execute the query with the parameter
    $result = pg_execute($conn, "delete_products", array($productId));

    if ($result) {
        echo "Product deleted successfully";
    } else {
        echo "Error in delete operation: " . pg_last_error($conn);
    }
}

pg_close($conn);
?>

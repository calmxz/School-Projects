<?php
include 'dbconn.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $product_name = isset($_POST['product_name']) ? $_POST['product_name'] : '';
    $unit_price = isset($_POST['unit_price']) ? $_POST['unit_price'] : '';

    $insertQuery = "INSERT INTO products (product_name, unit_price) VALUES ($1, $2)";
    $stmt = pg_prepare($conn, "insert_products", $insertQuery);

    $result = pg_execute($conn, "insert_products", array($product_name, $unit_price));

    if ($result) {
        echo "Product added successfully";
        header("Location: index.php");
        exit();
    } else {
        echo "Error in insert operation: " . pg_last_error($conn);
    }
}

pg_close($conn);
?>

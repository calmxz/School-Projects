<?php
include 'dbconn.php';

$product_query = "SELECT * FROM products ORDER BY product_id ASC";
$result = pg_query($conn, $product_query);

pg_close($conn);
?>
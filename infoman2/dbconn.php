<?php 
$conn_string = "host=localhost port=5432 dbname=online_shop user=postgres password=admin";

try{
    $conn = pg_connect($conn_string);
} catch(Exception $e) {
    die("System down. " . $e->getMessage());
}
?>

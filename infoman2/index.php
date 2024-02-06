    <?php
    include "add_products.php";
    ?>

    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Online Shop</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    </head>

    <body class="bg-gray-100 py-10 px-5">
        <h1 class="text-4xl font-bold mb-4 text-center">Online Shop Products</h2>
            <form method="POST" action="add_products.php" id = "add_product_form"class="max-w-md mx-auto bg-white p-6 mt-8 mb-10 border rounded-md shadow-md">
                <h2 class="text-2xl font-semibold mb-4 text-center">Add Product</h2>
                <label for="product_name" class="block text-gray-700 text-sm font-bold mb-2">Product Name:</label>
                <input type="text" id="product_name" name="product_name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>

                <label for="unit_price" class="block text-gray-700 text-sm font-bold mb-2">Unit Price:</label>
                <input type="text" id="unit_price" name="unit_price" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>

                <input type="submit" value="Submit" class="mt-4 bg-green-500 text-white px-4 py-2 rounded-md focus:outline-none hover:bg-green-600">
            </form>

            <form method="POST" action="update_products.php" id="update_form" class="max-w-md mx-auto bg-white p-6 mt-8 mb-10 border rounded-md shadow-md" style="display: none;">
                <h2 class="text-2xl font-semibold mb-4 text-center">Update Product</h2>
                <input type="hidden" id="product_id" name="product_id"> <!-- Changed id from 'update_product_id' to 'product_id' -->

                <label for="new_product_name" class="block text-gray-700 text-sm font-bold mb-2">New Product Name:</label>
                <input type="text" id="new_product_name" name="new_product_name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>

                <label for="new_unit_price" class="block text-gray-700 text-sm font-bold mb-2">New Unit Price:</label>
                <input type="text" id="new_unit_price" name="new_unit_price" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>

                <input type="submit" value="Update" class="mt-4 bg-blue-500 text-white px-4 py-2 rounded-md focus:outline-none hover:bg-blue-600">
            </form>

            <div class="overflow-x-auto">
                <table class="striped-rows w-full mx-auto text-sm text-center text-gray-900 bg-white shadow-md rounded-md min-w-max">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-200">
                        <tr>
                            <th class="hidden px-6 py-3">Product ID</th>
                            <th class="px-6 py-3">Product Name</th>
                            <th class="px-6 py-3">Unit Price</th>
                            <th class="px-6 py-3">Action</th>
                        </tr>
                    </thead>

                    <tbody>
                        <?php
                        include 'fetch_products.php';
                        while ($row = pg_fetch_assoc($result)) {
                            echo "
                        <tr class='odd:bg-white even:bg-gray-200 border-b hover:bg-gray-200'>
                            <td class='px-6 py-4 hidden'>$row[product_id]</td>
                            <td class='px-6 py-4'>$row[product_name]</td>
                            <td class='px-6 py-4'>₱$row[unit_price]</td>
                            <td class='px-6 py-4'>
                            <button type='button' onclick='showUpdateForm(" . $row['product_id'] . ")' class='bg-blue-600 text-white p-2 rounded-lg py-2 px-3'>Update</button>
                            <button type='button' onclick='deleteProductConfirmation(" . $row['product_id'] . ")' class='bg-red-600 text-white p-2 rounded-lg py-2 px-3'>Delete</button>
                        </tr>
                        
                    ";
                        }
                        ?>
                    </tbody>
                </table>
            </div>
            <script>
                function showUpdateForm(productId) {
                // Show the update form
                document.getElementById('add_product_form').style.display = 'none';
                document.getElementById('update_form').style.display = 'block';

                // Fill the update form fields with the product details
                document.getElementById('product_id').value = productId;
            }

                function deleteProductConfirmation(productId) {
                    if (confirm("Are you sure you want to delete this product?")) {
                        // If user confirms, proceed with the deletion
                        deleteProduct(productId);
                    }
                }

                function deleteProduct(productId) {
                    $.ajax({
                        type: "POST",
                        url: "delete_products.php",
                        data: {
                            productId: productId
                        },
                        success: function(response) {
                            // Check if the response contains an error message
                            if (response.includes("Error")) {
                                // Display error message if an error occurred during deletion
                                console.log("Error: " + response);
                            } else {
                                // Display success message
                                alert(response);
                                // Reload the page to reflect the changes
                                location.reload();
                            }
                        },
                        error: function(xhr, status, error) {
                            // Handle AJAX request errors
                            console.error("AJAX Request Error:", error);
                            alert("Error: Unable to delete the product. Please try again later.");
                        }
                    });
                }
            </script>

    </body>

    </html>
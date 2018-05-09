<?php

    require "connection.php";

    $book_name = "new book test";
    $book_author = "UNKNOWN";
    $added_on = date("d-m-Y");
    $is_issued = FALSE;

    $get_books = "SELECT * FROM `books` ORDER BY `books`.`book_id`;";
    
    $response = mysqli_query($connection, $get_books);
    
    if($response){
        
        // calculating current book id:
        $json = array();
        while($row = mysqli_fetch_assoc($response)){
            $json[] = $row;
        }
        $last_book_id = (int) explode("-", $json[sizeof($json) - 1]['book_id'])[1];
        $book_id = "SB-" . (string)($last_book_id + 1);
        
        $query_add_book = "INSERT INTO `books` VALUES('$book_id', '$book_name', '$book_author', '$added_on', '$is_issued');";
        
        if(mysqli_query($connection, $query_add_book)){
            echo "added successfully";
        } else {
            echo "error adding " . mysqli_error($connection);
        }
        
    } else {
        echo "error " . mysqli_error($connection);
    }

?>
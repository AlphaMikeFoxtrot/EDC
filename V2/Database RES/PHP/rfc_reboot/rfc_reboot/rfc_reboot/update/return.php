<?php

    require "connection.php";
    
    $type = $_POST["type"]; // "toy";
    $id = $_POST["id"]; // "TL/SB/654321";
    $sub = $_POST["sub"]; // "SB/Lib/9472";
    
    if($type == "book"){
        
        $query_update_books = "UPDATE `books` SET `books`.`is_issued`=0 WHERE `books`.`book_id`='$id';";
        $query_update_subscriber = "UPDATE `subscribers` SET `subscribers`.`book_issued`='0' WHERE `subscribers`.`subscriber_id`='$sub';";
        
        if(mysqli_query($connection, $query_update_books) && mysqli_query($connection, $query_update_subscriber)){
            echo "success";
        } else {
            echo "fail" . mysqli_error($connection);
        }
        
    } else if($type == "toy"){
        
        $query_update_books = "UPDATE `toys` SET `toys`.`is_issued`=0 WHERE `toys`.`toy_id`='$id';";
        $query_update_subscriber = "UPDATE `subscribers` SET `subscribers`.`toy_issued`='0' WHERE `subscribers`.`subscriber_id`='$sub';";
        
        if(mysqli_query($connection, $query_update_books) && mysqli_query($connection, $query_update_subscriber)){
            echo "success";
        } else {
            echo "fail" . mysqli_error($connection);
        }
        
    } else {
        echo "wrong type - " . $type;
    }
    

?>
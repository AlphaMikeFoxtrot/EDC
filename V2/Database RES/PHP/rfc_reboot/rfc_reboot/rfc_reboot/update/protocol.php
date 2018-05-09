<?php

    require "connection.php";
    
    $protocol = $_POST["protocol"]; // "issue";
    $type = $_POST["type"]; // "book";
    $id = $_POST["id"]; // "SB-45";
    $sub = $_POST["sub"]; // "SB/Lib/9472";

    if($protocol == "issue"){

        if($type == "book"){
            
            $query_update_books = "UPDATE `books` SET `books`.`is_issued`=TRUE, `books`.`issued_to_id`='$sub' WHERE `books`.`book_id`='$id';";
            $query_update_subscriber = "UPDATE `subscribers` SET `subscribers`.`book_issued`='1' WHERE `subscribers`.`subscriber_id`='$sub';";
            $date = date('d-M-Y');
            $query_update_log = "INSERT INTO `log`(`log`.`subscriber_id`, `log`.`book_id`, `log`.`date`) VALUES('$sub', '$id', '$date');";
            
            if(mysqli_query($connection, $query_update_books) && mysqli_query($connection, $query_update_subscriber) && mysqli_query($connection, $query_update_log)){
                echo "success";
            } else {
                echo "fail" . mysqli_error($connection);
            }
            
        } else if($type == "toy"){
            
            $query_update_books = "UPDATE `toys` SET `toys`.`is_issued`=TRUE, `toys`.`issued_to_id`='$sub' WHERE `toys`.`toy_id`='$id';";
            $query_update_subscriber = "UPDATE `subscribers` SET `subscribers`.`toy_issued`='1' WHERE `subscribers`.`subscriber_id`='$sub';";
            $date = date('M');
            $query_update_log = "INSERT INTO `log`(`log`.`subscriber_id`, `log`.`toy_id`, `log`.`date`) VALUES('$sub', '$id', '$date');";
            
            if(mysqli_query($connection, $query_update_books) && mysqli_query($connection, $query_update_subscriber) && mysqli_query($connection, $query_update_log)){
                echo "success";
            } else {
                echo "fail" . mysqli_error($connection);
            }
            
        } else {
            echo "wrong type - " . $type;
        }
        
    } else if($protocol == "return") {
        
        if($type == "book"){
        
            $query_update_books = "UPDATE `books` SET `books`.`is_issued`=FALSE, `books`.`issued_to_id`='NULL' WHERE `books`.`book_id`='$id';";
            $query_update_subscriber = "UPDATE `subscribers` SET `subscribers`.`book_issued`='0' WHERE `subscribers`.`subscriber_id`='$sub';";
            
            $date = date('d-M-Y');
            $query_update_return_log = "INSERT INTO `return_log` VALUES('$type', '$id', '$sub', '$date');";
            
            if(mysqli_query($connection, $query_update_books) && mysqli_query($connection, $query_update_subscriber) && mysqli_query($connection, $query_update_return_log)){
                echo "success";
            } else {
                echo "fail" . mysqli_error($connection);
            }
        
        } else if($type == "toy"){
            
            $query_update_books = "UPDATE `toys` SET `toys`.`is_issued`=FALSE, `toys`.`issued_to_id`='NULL' WHERE `toys`.`toy_id`='$id';";
            $query_update_subscriber = "UPDATE `subscribers` SET `subscribers`.`toy_issued`='0' WHERE `subscribers`.`subscriber_id`='$sub';";
            
            $date = date('d-M-Y');
            $query_update_return_log = "INSERT INTO `return_log` VALUES('$type', '$id', '$sub', '$date');";
            
            if(mysqli_query($connection, $query_update_books) && mysqli_query($connection, $query_update_subscriber) && mysqli_query($connection, $query_update_return_log)){
                echo "success";
            } else {
                echo "fail" . mysqli_error($connection);
            }
            
        }
        
    }
        
?>
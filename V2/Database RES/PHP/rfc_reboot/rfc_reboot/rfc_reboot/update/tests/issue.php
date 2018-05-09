<?php

    require "connection.php";
    
    $type = "book";
    $obj_id = "SB-123";
    $sub_id = "SB/Lib/23";
    $date = date('d-M-Y');
    
    /*
    
        1. update subscriber set book_issued = TRUE
        2. update books set is_issued = TRUE
        3. update log insert current 'trasaction';
    
    */
    
    if($type == "book"){
        
        $query_update_sub = "UPDATE `subscribers` SET `subscribers`.`book_issued`=TRUE WHERE `subscribers`.`subscriber_id`='$sub_id';";
        
        $query_update_book = "UPDATE `books` SET `books`.`is_issued`=TRUE WHERE `books`.`book_id`='$obj_id';";
        
        $query_update_log = "INSERT INTO `log`(`log`.`subscriber_id`, `log`.`book_id`, `log`.`date`) VALUES('$sub_id', '$obj_id', '$date');";
        
        if(mysqli_query($connection, $query_update_sub) && mysqli_query($connection, $query_update_book) && mysqli_query($connection, $query_update_log)){
            
            echo "success";
            
        } else {
            echo "fail" . mysqli_error($connection);
        }
        
    } else if($type == "toy") {
        
        // TOOD:
        
    }

?>
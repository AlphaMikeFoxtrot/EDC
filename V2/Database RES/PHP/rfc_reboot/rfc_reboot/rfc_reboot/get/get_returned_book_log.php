<?php

    require "connection.php";
    
    $query = "SELECT * FROM `return_book_log` WHERE 1;";
    
    $response = mysqli_query($connection, $query);
    
    if($response){
        $json = array();
        while($row = mysqli_fetch_assoc($response)){
            $json[] = $row;
        }
        
        echo json_encode($json);
    } else {
        echo "fail".mysqli_error($connection);
    }

?>
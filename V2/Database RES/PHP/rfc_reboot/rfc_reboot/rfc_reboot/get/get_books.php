<?php

    require "connection.php";
    
    $query = "SELECT * FROM `books` WHERE `books`.`is_issued` <> TRUE;";
    
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
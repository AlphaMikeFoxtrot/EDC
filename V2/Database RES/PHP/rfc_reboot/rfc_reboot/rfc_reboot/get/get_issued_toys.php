<?php

    require "connection.php";
    
    $query = "SELECT * FROM `toys` WHERE `toys`.`is_issued` <> '0';";
    
    $response = mysqli_query($connection, $query);
    
    if($response){
        $json = array();
        
        while($row = mysqli_fetch_assoc($response)){
            $json[] = $row;
        }
        
        echo json_encode($json);
    } else {
        echo "error" . mysqli_error($connection);
    }

?>
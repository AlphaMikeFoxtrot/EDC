<?php

    require "connection.php";
    
    $query = "SELECT * FROM `subscribers` WHERE NOT (`subscribers`.`toy_issued` <> FALSE AND `subscribers`.`book_issued` <> FALSE);";
    
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
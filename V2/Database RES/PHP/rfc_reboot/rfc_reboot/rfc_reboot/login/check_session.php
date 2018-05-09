<?php

    require "connection.php";
    
    $username = $_POST["username"];
    
    $query = "SELECT `admin`.`session` FROM `admin` WHERE `admin`.`username`='$username';";
    
    $response = mysqli_query($connection, $query);
    
    if($response){
        $json = array();
        
        while($row = mysqli_fetch_assoc($response)){
            $json[] = $row;
        }
        
        if($json[0]["session"] == 0){
            echo "FALSE";
        } else if ($json[0]["session"] == 1){
            echo "TRUE";
        }
    } else {
        echo "fail" . mysqli_error($connection);
    }

?>
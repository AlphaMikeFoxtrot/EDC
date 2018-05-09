<?php

    require "connection.php";

    $username = $_POST["username"]; // "admin";
    $password = $_POST["password"]; // "password";
    
    $query = "SELECT * FROM `admin` WHERE `admin`.`username`='$username' AND `admin`.`password`='$password';";
    
    $response = mysqli_query($connection, $query);
    
    if($response && sizeof(mysqli_fetch_assoc($response)) > 0){
        // $row = mysqli_fetch_assoc($response);
        // echo $row['clearance'];
        $query_update_session = "UPDATE `admin` SET `admin`.`session`='1' WHERE `admin`.`username`='$username';";
        if(mysqli_query($connection, $query_update_session)){
            echo "clear";
        } else {
            echo "session_fail" . mysqli_error($connection);
        }
    } else {
        echo "auth_fail" . mysqli_error($connection);
    }
?>
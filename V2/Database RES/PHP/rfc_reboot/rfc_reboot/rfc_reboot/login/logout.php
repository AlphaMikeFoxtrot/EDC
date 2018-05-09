<?php

    require "connection.php";
    
    $username = $_POST["username"];
    
    $query = "UPDATE `admin` SET `admin`.`session`='0' WHERE `admin`.`username`='$username';";
    
    if(mysqli_query($connection, $query)){
        echo "success";
    } else {
        echo "fail" . mysqli_query($connection);
    }

?>
<?php

    $database_username = "u347889504_edc";
    $database_password = "rfc_reboot";
    $database_name = "u347889504_edc";
    $database_host = "mysql.hostinger.in";
    
    $connection = mysqli_connect($database_host, $database_username, $database_password, $database_name);
    
    if($connection){
        // echo "connection";
    } else{
        // echo "fail".mysqli_error($connection);
    }

?>
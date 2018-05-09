<?php
    
    require "connection.php";
    
    $sub = "SB/Lib/18";
    
    $query_get_name = "SELECT `subscribers`.`subscriber_name` FROM `subscribers` WHERE `subscribers`.`subscriber_id`='$sub';";
            
    $response_get_name = mysqli_query($connection, $query_get_name);
    
    $row = mysqli_fetch_assoc($response_get_name);
    
    $sub_name = $row['subscriber_name'];
            
    echo " " . $sub_name;

?>
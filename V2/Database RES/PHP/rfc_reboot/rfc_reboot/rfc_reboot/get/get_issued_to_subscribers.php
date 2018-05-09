<?php
    
    require "connection.php";
    
    $query = "SELECT * FROM `subscribers` WHERE `subscribers`.`toy_issued`='1' OR `subscribers`.`book_issued`='1';";
    
    $response = mysqli_query($connection, $query);
    
    if($response){
        $json = array();
        while($row = mysqli_fetch_assoc($response)){
            $json[] = $row;
        }
        echo json_encode($json);
    }
?>
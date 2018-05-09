<?php

    require "connection.php";
    
    /*
    
        CODE FORMAT:
            100 --> book only       (RFC)
            200 --> toy only        (ETL)
            300 --> book and toy    (RFC + ETL)
        
    */
    
    $code = "100"; // $_POST['code'];
    $json = array();

    switch($code){
        
        case 100:
            // book only
            $query = "SELECT * FROM `subscribers` WHERE `subscribers`.`is_book`=TRUE AND `subscribers`.`is_toy`=FALSE;";
            $response = mysqli_query($connection, $query);

            if($response){
                
                while($row = mysqli_fetch_assoc($response)){
                    $json[] = $row;
                }

            }
            break;
        
        case 200:
            // toy only
            $query = "SELECT * FROM `subscribers` WHERE `subscribers`.`is_book`=FALSE AND `subscribers`.`is_toy`=TRUE;";
            $response = mysqli_query($connection, $query);

            if($response){
                
                while($row = mysqli_fetch_assoc($response)){
                    $json[] = $row;
                }

            }
            break;
        
        case 300:
            // book and toy
            $query = "SELECT * FROM `subscribers` WHERE `subscribers`.`is_book`=TRUE AND `subscribers`.`is_toy`=TRUE;";
            $response = mysqli_query($connection, $query);

            if($response){
                
                while($row = mysqli_fetch_assoc($response)){
                    $json[] = $row;
                }

            }
            break;

        default:
            $json = [];
            break;
        
    }

    echo json_encode($json);

?>
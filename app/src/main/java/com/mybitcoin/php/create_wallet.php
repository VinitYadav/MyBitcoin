<?php
	$sql_servername = "127.0.0.1";
	$sql_username = "root";
	$sql_password = "";
	$sql_dbname = "bitcoin";

	// Create connection
	$conn = mysqli_connect($sql_servername, $sql_username, 	$sql_password);
	$db = mysqli_select_db($conn, $sql_dbname);

	if (isset($_POST['name']) && isset($_POST['email']) && 	isset($_POST['password']) && isset($_POST['wallet_address'])  	&& isset($_POST['wallet_label']) ) {

	$name= $_POST['name'];
	$email= $_POST['email'];
	$password= $_POST['password'];
	$wallet_address= $_POST['wallet_address'];
	$wallet_label= $_POST['wallet_label'];

	$query = "SELECT email FROM wallet_user WHERE email LIKE 	'" . $email . "'";

	$result = mysqli_query($conn,$query);
	if (mysqli_num_rows($result) > 0) {
	$response['code']= 300;
	$response['message'] = 'Email already exits';
	echo json_encode($response);
	} else {
		$query = "INSERT INTO wallet_user(name, email, 	password, wallet_address, wallet_label) VALUES('" . $name . 	"', '" . $email . "', '" . $password . "', '" . 	$wallet_address . "', '" . $wallet_label . "')";

	$result = mysqli_query($conn,$query);
	$query = "SELECT * FROM wallet_user WHERE email LIKE '" . 	$email . "'";

	$result = mysqli_query($conn,$query);
	if (mysqli_num_rows($result) > 0) {
		while($row = mysqli_fetch_assoc($result)) {
		$row['code']= 200;
		$row['message'] = 'Create wallet successfully';
		echo json_encode($row);
        	break;
    	}
	} else {
		$row['code']= 300;
		$row['message'] = 'Somting went wrong';
		echo json_encode($row);
	}

	}
	mysqli_close($conn);
}else{
		$row['code']= 300;
		$row['message'] = 'Required field is missing';
		echo json_encode($row);

}

?>
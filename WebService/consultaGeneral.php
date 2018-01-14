<?php
	//Recibe los datos del dispositivo movil
	$dominio = $_REQUEST['host'];
	$db = $_REQUEST['db'];
	$usuario = $_REQUEST['usuario'];
	$pass = $_REQUEST['pass'];
	$consulta = $_REQUEST['consulta'];
	
	//Hace la conexion a la BD
	$cadena = "mysql:host=".$dominio.";dbname=".$db.";charset=UTF8";
	$conn = new PDO($cadena, $usuario, $pass);
	$resultado = $conn->query($consulta);
	
	//Lo convierte en JSON
	$datos = array();
	foreach($resultado as $row)
		$datos[] = $row;
	
	//Regresa el JSON
	echo json_encode($datos);
?>
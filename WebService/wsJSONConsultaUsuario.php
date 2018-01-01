<?php
	$hostname = "localhost";
	$database = "db_usuario";
	$username = "root";
	$password = "";
	
	if (isset($_REQUEST['nombre']))
	{
		//Obtiene los datos
		$nombre = $_REQUEST['nombre'];
		
		//Hace la conexion
		$conexion = mysqli_connect($hostname, $username, $password, $database);
		
		//Hace la consulta
		$consulta = "SELECT nombre, profesion FROM t_usuario WHERE nombre LIKE '$nombre%';";
		mysqli_set_charset($conexion, "utf8");
		$resultado = mysqli_query($conexion, $consulta);
		
		//Lo convierte en JSON
		$datos = array();
		foreach($resultado as $row)
			$datos[] = $row;

		//Regresa el JSON
		mysqli_close($conexion);
		echo json_encode($datos);
	}
?>
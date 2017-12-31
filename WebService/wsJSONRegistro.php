<?php
	$hostname = "localhost";
	$database = "db_usuario";
	$username = "root";
	$password = "";
	
	$datos = array();
	if (isset($_REQUEST['nombre']) && isset($_REQUEST['profesion']))
	{
		//Obtiene los datos
		$nombre = $_REQUEST['nombre'];
		$profesion = $_REQUEST['profesion'];
		
		//Hace la conexion
		$conexion = mysqli_connect($hostname, $username, $password, $database);
		
		//Hace la insercion
		$insert = "INSERT INTO t_usuario(nombre, profesion) VALUES ('$nombre', '$profesion');";
		$resultado = mysqli_query($conexion, $insert);
		
		//Si hace la insercion
		if ($resultado)
		{
			$consulta = "SELECT * FROM t_usuario ORDER BY id DESC LIMIT 1";
			$resultado = mysqli_query($conexion, $consulta);
			
			//Lo convierte en JSON
			foreach($resultado as $row)
				$datos[] = $row;

			//Regresa el JSON
			mysqli_close($conexion);
			echo json_encode($datos);
		}
	}
	else
	{
		$resulta["id"] = "0";
		$resulta["nombre"] = "No Registra";
		$resulta["profesion"] = "No Registra";
		$datos[] = $resulta;
		echo json_encode($datos);
	}
?>
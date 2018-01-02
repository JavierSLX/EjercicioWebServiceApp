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
		$consulta = "SELECT id, nombre, profesion, imagen, ruta_imagen FROM t_usuario WHERE nombre LIKE '$nombre%';";
		mysqli_set_charset($conexion, "utf8");
		$resultado = mysqli_query($conexion, $consulta);
		
		//Lo convierte en JSON
		$datos['usuario'] = array();
		foreach($resultado as $row)
		{
			array_push($datos["usuario"], array('id'=>$row['id'],'nombre'=>$row['nombre'],'profesion'=>$row['profesion'],'imagen'=>base64_encode($row['imagen']), 'ruta_imagen'=>$row['ruta_imagen']));
		}
		
		//Regresa el JSON
		mysqli_close($conexion);
		echo json_encode($datos);
	}
?>
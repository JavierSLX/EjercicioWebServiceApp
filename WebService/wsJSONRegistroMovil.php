<?php
	$hostname = "localhost";
	$database = "morpheus_usuario";
	$username = "morpheus_admin";
	$password = "morpheusadmin";
	
	//Hace la conexion
	$conexion = mysqli_connect($hostname, $username, $password, $database);
	
	$nombre = $_REQUEST["nombre"];
	$profesion = $_REQUEST["profesion"];
	$imagen = $_REQUEST["imagen"];
	
	$path = "imagenes/$nombre.jpg";
	$url = "http://morpheusdss.com/EjercicioWebService/$path";
	
	file_put_contents($path, base64_decode($imagen));
	$bytesArchivo = file_get_contents($path);
	$bytesArchivo = mysqli_real_escape_string($conexion, $bytesArchivo);
	
	$insert = "INSERT INTO t_usuario(nombre, profesion, imagen, ruta_imagen) VALUES ('$nombre', '$profesion', '$bytesArchivo', '$url');";
	$resultado = mysqli_query($conexion, $insert);
	
	mysqli_close($conexion);
	
	if (empty($resultado))
		echo "Guardado";
	else
		echo "No guardado";
?>
<?php
	$hostname = "localhost";
	$database = "morpheus_usuario";
	$username = "morpheus_admin";
	$password = "morpheusadmin";
	
	if (isset($_REQUEST["btn"]))
	{
		//Hace la conexion
		$conexion = mysqli_connect($hostname, $username, $password, $database);
		
		//La ruta en donde se guardará la imagen y el nombre del archivo
		$ruta = "EjercicioWebService/imagenes";
		$archivo = $_FILES['imagen']['tmp_name'];
		
		//Saca el nombre del archivo, lo sube ya actualiza la ruta
		$nombreArchivo = $_FILES['imagen']['name'];
		$ruta = $_SERVER['DOCUMENT_ROOT']."/".$ruta."/".$nombreArchivo;
		move_uploaded_file($archivo, $ruta);
		$imagen = "./imagenes/".$nombreArchivo;
		$rutaServidor = "http://morpheusdss.com/EjercicioWebService/imagenes/".$nombreArchivo;
		echo $rutaServidor;
		
		//Obtiene los demas datos
		$nombre = $_REQUEST["nombre"];
		$profesion = $_REQUEST["profesion"];
		
		//Muestra los datos
		echo "<br>Nombre: $nombre<br>Profesion:$profesion<br>";
		echo "Tipo Imagen: ".$_FILES['imagen']['type']."<br><br>";
		echo "Imagen: <br><img src='$imagen'><br><br>";
		$data = file_get_contents($ruta);
		
		//Escapamos los caracteres para que se puedan almacenar en la base de datos correctamente.
		$data = mysqli_real_escape_string ($conexion, $data);
		
		$insert = "INSERT INTO t_usuario(nombre, profesion, imagen, ruta_imagen) VALUES ('$nombre', '$profesion', '$data', '$rutaServidor');";
		$resultado = mysqli_query($conexion, $insert);
		
		//Notifica que se hizo de manera correcta
		if($resultado)
		{
			echo "Imagen insertada correctamente";
			$consulta = "SELECT id, nombre, profesion, imagen, ruta_imagen FROM t_usuario ORDER BY id DESC LIMIT 1";
			
			mysqli_set_charset($conexion, "utf8");
			$resultado = mysqli_query($conexion, $consulta);
			echo "<br>";
			
			//Lo convierte en JSON
			$datos["usuario"] = array();
			foreach($resultado as $row)
			{
				array_push($datos["usuario"], array('id'=>$row['id'],'nombre'=>$row['nombre'],'profesion'=>$row['profesion'],'imagen'=>base64_encode($row['imagen']), 'ruta_imagen'=>$row['ruta_imagen']));
			}
			
			//Regresa el JSON
			mysqli_close($conexion);
			echo json_encode($datos);
		}
	}
?>
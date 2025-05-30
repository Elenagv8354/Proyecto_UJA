<h1>Wallet SSI</h1>
Aplicación móvil para Android diseñada para la gestión y presentación de credenciales digitales en formato de código QR. 
Permite a los usuarios organizar sus credenciales, generar códigos QR a partir de datos seleccionados, escanear códigos QR, y compartir o descargar sus credenciales digitales de forma sencilla y segura.

<h2>Funcionalidades principales</h2>
<ul>
  <li>Gestión de credenciales</li>
    <ul>
      <li>Visualización de credenciales principales</li>
      <li>Opción para ocultar y restaurar credenciales</li>
      <li>Funcionalidad para eliminar credenciales</li>
      <li>Listado de todas las credenciales (ocultas y visibles)</li>
    </ul>
  <li>Generación de códigos QR</li>
    <ul>
      <li>Genera códigos QR a partir de datos de credenciales seleccionados por el usuario</li>
      <li>Muestra el código QR generado en una interfaz dedicada</li>
    </ul>
  <li>Escaneo de códigos QR</li>
    <ul>
      <li>Permite escanear códigos QR utilizando la cámara del dispositivo</li>
      <li>Permite importar códigos QR desde la galería</li>
    </ul>
  <li>Compartir y descargar QR</li>
    <ul>
      <li>Opción para compartir el código QR generado a través de otras aplicaciones</li>
      <li>Funcionalidad para descargar el código QR directamente en la galería del dispositivo</li>
    </ul>
  <li>Persistencia de QR</li>
    <ul>
      <li>El último código generado se guarda internamente para su posterior visualización o uso</li>
    </ul>
  <li>Actividad reciente</li>
    <ul>
      <li>Panel en la pantalla principal donde se muestran las acciones recientes del usuario</li>
    </ul>
  <li>Gestión de sesión</li>
    <ul>
      <li>Sistema de autenticación de usuario y contraseña</li>
      <li>Opción para cerrar sesión, redirigiendo al usuario a la pantalla de bienvenida</li>
    </ul>
</ul>

<h2>Tecnologías utilizadas</h2>
<ul>
  <li><strong>Lenguaje:</strong> Java (principalmente) </li>
  <li><strong>Framework:</strong> Android SDK</li>
  <li><strong>Librerías clave:</strong></li>
    <ul>
      <li>AndroidX Navigation Component: navegación entre los diferentes fragment de la aplicación.</li>
      <li>ZXing Android Embedded: la base para generación de códigos QR de alta calidad.</li>
      <li>AndoidX CardView: para un diseño limpio y modular de las tarjetas de información.</li>
      <li>FileProvider: gestión segura de archivos para compartir el QR.</li>
    </ul>
  <li><strong>Entorno de Desarrollo:</strong> Android Studio</li>
</ul>

<h2>Ejecución y uso del proyecto</h2>
  <h3>Pasos para tener el proyecto funcionando en tu equipo</h3>
  <ol>
    <li>Clona el repositorio: </li>
      <ul>
        <li>Bash--> "git clone https://github.com/[Tu-Usuario-GitHub]/[nombre-de-tu-repositorio].git cd [nombre-de-tu-repositorio]"</li>
      </ul>
    <li>Abre el proyecto en Android Studio: </li>
      <ul>
        <li>Inicia Android Studio.</li>
        <li>Ve a "File" > "Open"... y selecciona la carpeta del proyecto que acabas de clonar.</li>
        <li>Espera a que Gradle sincronice las dependencias (asegúrate de tener conexión a internet).</li>
      </ul>
    <li>Ejecuta la aplicación: </li>
      <ul>
        <li>Conecta un dispositivo Android (con la depuración USB activada) o inicia un emulador.</li>
        <li>Haz clic en el botón "Run 'app'" (icono triángulo verde) en la barra de herramientas de Android Studio.</li>
      </ul>
  </ol>

  <h2>Requisitos previos</h2>
    <ul>
      <li>Android Studio instalado.</li>
      <li>Android SDK (recomendado API 26 o superior).</li>
      <li>Java Development Kit (JDK).</li>
    </ul>

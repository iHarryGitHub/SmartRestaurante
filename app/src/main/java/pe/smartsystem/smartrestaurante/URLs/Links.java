package pe.smartsystem.smartrestaurante.URLs;

import pe.smartsystem.smartrestaurante.Utilidades.SessionManager;
import pe.smartsystem.smartrestaurante.ui.activity.login.LoginActivity;

public class Links {



    private static String host= LoginActivity.IPgeneral;//"192.168.1.87";//190.81.3.91:82

    final public static String URL_LOGIN_GET="http://"+host+"/WS/consultarDatos.php";
    final public static String URL_TOP_GET="http://"+host+"/WS/TopProductos.php";
    final public static String URL_INSERT_MESA_POST="http://"+host+"/WS/Mesa_INSERT.php";
    final public static String URL_DETALLEMESA_GET="http://"+host+"/WS/consultarDetalleMesa.php?nromesa=";
    final public static String URL_ACTUALIZARMESA_POST="http://"+host+"/WS/Mesa_UPDATE.php";
    final public static String URL_CABEMENSAJE_GET="http://"+host+"/WS/ConsultarMensajes.php?idmensajes=1";
    final public static String URL_MENSAJE_GET="http://"+host+"/WS/ConsultarMensajesDetalle.php?idmensajes=";
    final public static String URL_INSERTMENSAJE_POST="http://"+host+"/WS/Mensaje_UPDATE.php";//?
    final public static String URL_MESAS_GET="http://"+host+"/WS/consultarMesa.php";
    final public static String URL_RECORDMOZOS_GET="http://"+host+"/WS/ConsultaCapacidad.php?mozo=";
    final public static String URL_CONSULTA_MSJ_PRO="http://"+host+"/WS/consultarMensajedeProducto.php";
    final public static String URL_LISTA_CATEGORIAS="http://"+host+"/WS/mostrarCategorias.php";
    final public static String URL_LISTA_PRODUCTOS="http://"+host+"/WS/mostrarProductos.php?nombrecategoria=";
    final public static String URL_ENV_COMANDAS="http://"+host+"/WS/Comanda.php?";
    final public static String URL_VALIDAR_MAC="http://"+host+"/WS/ValidarIMEI.php?caja=";

}

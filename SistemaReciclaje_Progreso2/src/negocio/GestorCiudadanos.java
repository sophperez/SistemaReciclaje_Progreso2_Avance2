package negocio;
import modelo.Ciudadano;
import modelo.Usuario;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Módulo 1. La lista de ciudadanos se mantiene ordenada por cédula para
// poder buscar con búsqueda binaria, que es la búsqueda que usan todos los módulos.
public class GestorCiudadanos {
    private List<Ciudadano> ciudadanos;

    // Criterios de ordenamiento (Comparator) para listar ciudadanos.
    public static final Comparator<Ciudadano> POR_PUNTOS =
            Comparator.comparingInt(Ciudadano::getPuntos).reversed()
                    .thenComparing(Ciudadano::getNombreCompleto);

    public static final Comparator<Ciudadano> POR_NOMBRE =
            Comparator.comparing(Ciudadano::getNombreCompleto, String.CASE_INSENSITIVE_ORDER);

    // Primero el nivel más alto y, dentro del mismo nivel, quien tiene más puntos.
    public static final Comparator<Ciudadano> POR_NIVEL =
            Comparator.comparingInt((Ciudadano c) -> Ciudadano.valorNivel(c.getNivel())).reversed()
                    .thenComparing(Comparator.comparingInt(Ciudadano::getPuntos).reversed());

    public GestorCiudadanos(){
        ciudadanos = new ArrayList<>();
    }

    public List<Ciudadano> getCiudadanos(){
        return ciudadanos;
    }

    // Búsqueda binaria por cédula: O(log n).
    // Si encuentra la cédula devuelve su posición; si no, devuelve
    // -(posición donde debería insertarse) - 1, para saber dónde registrarla.
    private int busquedaBinaria(String cedula){
        int inicio = 0;
        int fin = ciudadanos.size() - 1;
        while(inicio <= fin){
            int medio = (inicio + fin) / 2;
            int cmp = ciudadanos.get(medio).getCedula().compareTo(cedula);
            if(cmp == 0){
                return medio;
            } else if(cmp < 0){
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -(inicio + 1);
    }

    public Ciudadano buscarPorCedula(String cedula){
        if(cedula == null || cedula.isBlank()){
            return null;
        }
        int pos = busquedaBinaria(cedula.trim());
        return pos >= 0 ? ciudadanos.get(pos) : null;
    }

    // Búsqueda secuencial: O(n). Se usa para el nombre de usuario porque la
    // lista solo puede estar ordenada por un criterio (la cédula).
    private Ciudadano buscarPorNombreUsuario(String nombreUsuario){
        for(Ciudadano ci : ciudadanos){
            if(ci.getNombreUsuario().equals(nombreUsuario)){
                return ci;
            }
        }
        return null;
    }

    // Registra al ciudadano en la posición que le corresponde según su cédula,
    // para que la lista siga ordenada. O(n) por el desplazamiento de elementos.
    public String registrarCiudadano(String nombreCompleto, String cedula, String correo,
                                     String telefono, String direccion, String nombreUsuario,
                                     String contrasena) throws Exception {
        for(String campo : new String[]{nombreCompleto, cedula, correo, telefono, direccion, nombreUsuario, contrasena}){
            if(campo == null || campo.isBlank()){
                throw new Exception("Complete todos los campos.");
            }
        }
        if(!Usuario.esNombreValido(nombreCompleto)){
            throw new Exception("El nombre completo solo puede contener letras y espacios.");
        }
        if(!Usuario.esCedulaValida(cedula)){
            throw new Exception(Usuario.mensajeErrorCedula(cedula));
        }
        if(!Usuario.esCorreoValido(correo)){
            throw new Exception("El correo debe contener '@' y '.'.");
        }
        if(!Usuario.esTelefonoValido(telefono)){
            throw new Exception("El teléfono debe tener 10 dígitos.");
        }
        if(!Usuario.esDireccionValida(direccion)){
            throw new Exception("La dirección no es válida.");
        }
        if(!Usuario.esNombreUsuarioValido(nombreUsuario)){
            throw new Exception("El nombre de usuario solo puede tener letras, dígitos y '_'.");
        }
        if(!Usuario.esContrasenaValida(contrasena)){
            throw new Exception("La contraseña debe tener al menos 4 caracteres.");
        }
        int pos = busquedaBinaria(cedula);
        if(pos >= 0){
            throw new Exception("La cédula ya existe en el sistema.");
        }
        if(buscarPorNombreUsuario(nombreUsuario) != null){
            throw new Exception("El nombre de usuario ya existe en el sistema.");
        }
        Ciudadano nuevo = new Ciudadano(nombreCompleto, cedula, correo,
                telefono, direccion, nombreUsuario, contrasena);
        ciudadanos.add(-(pos + 1), nuevo);
        return "Registro exitoso. Bienvenido al programa de reciclaje.";
    }

    public Ciudadano iniciarSesion(String nombreUsuario, String contrasena){
        Ciudadano ci = buscarPorNombreUsuario(nombreUsuario);
        if(ci != null && ci.estaActivo() && ci.iniciarSesion(nombreUsuario, contrasena)){
            return ci;
        }
        return null;
    }

    public boolean ciudadanoInactivo(String nombreUsuario){
        Ciudadano ci = buscarPorNombreUsuario(nombreUsuario);
        return ci != null && !ci.estaActivo();
    }

    public String consultarPerfil(String cedula) throws Exception{
        Ciudadano ci = buscarPorCedula(cedula);
        if(ci == null){
            throw new Exception("Ciudadano no encontrado.");
        }
        return ci.toString();
    }

    public String darDeBaja(String cedula) throws Exception {
        Ciudadano ci = buscarPorCedula(cedula);
        if(ci == null){
            throw new Exception("Ciudadano no encontrado.");
        }
        if(!ci.estaActivo()){
            throw new Exception("El ciudadano ya se encuentra inactivo.");
        }
        ci.setEstado("INACTIVO");
        return "El ciudadano ha sido dado de baja del programa.";
    }

    // Solo el administrador puede usarlo: la contraseña queda igual a la cédula.
    public String resetearContrasena(String cedula) throws Exception {
        Ciudadano ci = buscarPorCedula(cedula);
        if(ci == null){
            throw new Exception("Ciudadano no encontrado.");
        }
        ci.setContrasena(ci.getCedula());
        return "Contraseña restablecida. La nueva contraseña es el número de cédula.";
    }

    // Ordena una copia de la lista para no perder el orden por cédula.
    // List.sort usa Merge Sort: O(n log n).
    public List<Ciudadano> listarOrdenados(Comparator<Ciudadano> criterio){
        List<Ciudadano> copia = new ArrayList<>(ciudadanos);
        copia.sort(criterio);
        return copia;
    }

    // Filtrado por recorrido: O(n).
    public List<Ciudadano> filtrarPorNivel(String nivel){
        List<Ciudadano> resultado = new ArrayList<>();
        for(Ciudadano ci : ciudadanos){
            if(ci.getNivel().equalsIgnoreCase(nivel)){
                resultado.add(ci);
            }
        }
        return resultado;
    }

    public List<Ciudadano> filtrarPorEstado(String estado){
        List<Ciudadano> resultado = new ArrayList<>();
        for(Ciudadano ci : ciudadanos){
            if(ci.getEstado().equalsIgnoreCase(estado)){
                resultado.add(ci);
            }
        }
        return resultado;
    }
}

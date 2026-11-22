package negocio;
import modelo.Administrador;
import modelo.Ciudadano;
import modelo.Material;
import modelo.PuntoRecoleccion;
import modelo.Recompensa;
import modelo.Turno;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Fachada del sistema: la interfaz gráfica solo habla con esta clase,
// y esta clase le pasa cada pedido al gestor del módulo correspondiente.
public class GestorReciclaje {
    private GestorCiudadanos gestorCiudadanos;
    private GestorEntregas gestorEntregas;
    private GestorRecompensas gestorRecompensas;
    private GestorCanjes gestorCanjes;
    private GestorRutas gestorRutas;
    private Administrador administrador;
    private Ciudadano ciudadanoActivo;

    public GestorReciclaje(){
        gestorCiudadanos = new GestorCiudadanos();
        gestorEntregas = new GestorEntregas();
        gestorRecompensas = new GestorRecompensas();
        gestorCanjes = new GestorCanjes();
        gestorRutas = new GestorRutas();
        administrador = new Administrador("Carlos Andrade",
                "1713175071", "gestor@reciclaje.com",
                "0998765432", "Av. Amazonas N34-56", "admin", "admin1234");
        ciudadanoActivo = null;
        cargarDatosDePrueba();
    }

    // ======= DATOS DE PRUEBA (se cargan al iniciar; no hay base de datos) =======

    private void cargarDatosDePrueba(){
        try {
            cargarMapaInicial();

            gestorCiudadanos.registrarCiudadano("Genesis Barreiro", "1710034057",
                    "genesis.barreiro@hotmail.com", "0991234567", "Av. Amazonas N12-34",
                    "gene_barreiro", "gene1234");
            gestorCiudadanos.registrarCiudadano("Tabatha Flores", "1725433252",
                    "tabatha.flores@gmail.com", "0987654321", "Calle Sucre 45-67",
                    "taba_flores", "taba1234");
            gestorCiudadanos.registrarCiudadano("Naomi Jaya", "1750912352",
                    "naomi.jaya@hotmail.com", "0976543210", "Av. 6 de Diciembre N23-45",
                    "nao_jaya", "nao1234");
            gestorCiudadanos.registrarCiudadano("Sophia Perez", "1719876557",
                    "sophia.perez@gmail.com", "0965432109", "Calle Patria 78-90",
                    "soph_perez", "soph123");
            gestorCiudadanos.registrarCiudadano("Teresa Vega", "1723456057",
                    "teresa.vega@outlook.com", "0954321098", "Av. Republica E4-56",
                    "tere_vega", "tere123");
            gestorCiudadanos.registrarCiudadano("Juan Castillo", "1745678951",
                    "juan.castillo@hotmail.com", "0943210987", "Calle Veintimilla 34-12",
                    "juan_castillo", "juan123");
            gestorCiudadanos.registrarCiudadano("Valeria Rios", "1708765456",
                    "valeria.rios@gmail.com", "0932109876", "Av. Colon N15-78",
                    "valeria_rios", "valeria123");

            gestorRecompensas.registrarRecompensa("Bolsa ecologica",
                    "Bolsa reutilizable de tela", "PRODUCTO", 100, 10);
            gestorRecompensas.registrarRecompensa("Descuento supermercado",
                    "Descuento del diez por ciento", "DESCUENTO", 500, 5);
            gestorRecompensas.registrarRecompensa("Entrada al cine",
                    "Una entrada para cualquier funcion", "EXPERIENCIA", 1000, 1);
            gestorRecompensas.registrarRecompensa("Kit de reciclaje",
                    "Kit con bolsas y contenedores", "PRODUCTO", 300, 8);
            gestorRecompensas.registrarRecompensa("Vale de transporte",
                    "Vale para transporte publico", "DESCUENTO", 750, 4);

            // Entregas: dejan ciudadanos de todos los niveles para poder probar la prioridad.
            entregaDePrueba("1710034057", "METAL", 100, "La Carolina");
            entregaDePrueba("1710034057", "ELECTRONICO", 100, "La Carolina");
            entregaDePrueba("1710034057", "PLASTICO", 30, "El Ejido");
            entregaDePrueba("1725433252", "METAL", 60, "La Carolina");
            entregaDePrueba("1725433252", "PLASTICO", 50, "El Ejido");
            entregaDePrueba("1725433252", "VIDRIO", 50, "El Ejido");
            entregaDePrueba("1750912352", "ELECTRONICO", 50, "El Ejido");
            entregaDePrueba("1750912352", "PAPEL", 60, "El Ejido");
            entregaDePrueba("1750912352", "PLASTICO", 40, "La Floresta");
            entregaDePrueba("1719876557", "VIDRIO", 40, "La Floresta");
            entregaDePrueba("1719876557", "PAPEL", 60, "La Floresta");
            entregaDePrueba("1723456057", "PLASTICO", 5, "Quitumbe");
            entregaDePrueba("1745678951", "PAPEL", 10, "Quitumbe");

            Ciudadano genesis = gestorCiudadanos.buscarPorCedula("1710034057");
            Ciudadano tabatha = gestorCiudadanos.buscarPorCedula("1725433252");
            gestorCanjes.realizarCanje(genesis, gestorRecompensas.buscarPorId(1));
            gestorCanjes.realizarCanje(genesis, gestorRecompensas.buscarPorId(2));
            gestorCanjes.realizarCanje(tabatha, gestorRecompensas.buscarPorId(1));
        } catch(Exception e){
            System.out.println("Error cargando datos de prueba: " + e.getMessage());
        }
    }

    private void entregaDePrueba(String cedula, String material, double peso,
                                 String puntoLimpio) throws Exception {
        gestorEntregas.registrarEntrega(gestorCiudadanos.buscarPorCedula(cedula),
                new Material(material), peso, gestorRutas.buscarPorNombre(puntoLimpio));
    }

    // Mapa inicial: 8 puntos de Quito con distancias aproximadas.
    private void cargarMapaInicial() throws Exception {
        PuntoRecoleccion p1 = new PuntoRecoleccion("La Carolina", "Iñaquito", PuntoRecoleccion.Tipo.PUNTO_LIMPIO);
        PuntoRecoleccion p2 = new PuntoRecoleccion("El Ejido", "Centro", PuntoRecoleccion.Tipo.PUNTO_LIMPIO);
        PuntoRecoleccion p3 = new PuntoRecoleccion("La Floresta", "Floresta", PuntoRecoleccion.Tipo.PUNTO_LIMPIO);
        PuntoRecoleccion p4 = new PuntoRecoleccion("Quitumbe", "Quitumbe", PuntoRecoleccion.Tipo.PUNTO_LIMPIO);
        PuntoRecoleccion p5 = new PuntoRecoleccion("Calderon", "Calderón", PuntoRecoleccion.Tipo.PUNTO_LIMPIO);
        PuntoRecoleccion p6 = new PuntoRecoleccion("El Inca", "El Inca", PuntoRecoleccion.Tipo.PUNTO_LIMPIO);
        PuntoRecoleccion p7 = new PuntoRecoleccion("Centro Acopio Norte", "Carcelén", PuntoRecoleccion.Tipo.CENTRO_ACOPIO);
        PuntoRecoleccion p8 = new PuntoRecoleccion("Centro Acopio Sur", "Guamaní", PuntoRecoleccion.Tipo.CENTRO_ACOPIO);
        for(PuntoRecoleccion p : new PuntoRecoleccion[]{p1, p2, p3, p4, p5, p6, p7, p8}){
            gestorRutas.registrarPunto(p);
        }
        gestorRutas.conectarPuntos(p1, p2, 4.5);
        gestorRutas.conectarPuntos(p1, p6, 3.2);
        gestorRutas.conectarPuntos(p1, p7, 6.1);
        gestorRutas.conectarPuntos(p2, p3, 2.1);
        gestorRutas.conectarPuntos(p3, p4, 12.5);
        gestorRutas.conectarPuntos(p4, p8, 3.8);
        gestorRutas.conectarPuntos(p5, p6, 5.0);
        gestorRutas.conectarPuntos(p5, p7, 7.4);
    }

    // ======= AUTENTICACIÓN =======

    public String iniciarSesionCiudadano(String nombreUsuario, String contrasena){
        Ciudadano c = gestorCiudadanos.iniciarSesion(nombreUsuario, contrasena);
        if(c != null){
            ciudadanoActivo = c;
            return "CIUDADANO";
        }
        return null;
    }

    public String iniciarSesionAdmin(String nombreUsuario, String contrasena){
        if(administrador.iniciarSesion(nombreUsuario, contrasena)){
            ciudadanoActivo = null;
            return "ADMINISTRADOR";
        }
        return null;
    }

    public void cerrarSesion(){
        ciudadanoActivo = null;
    }

    public Ciudadano getCiudadanoActivo(){
        return ciudadanoActivo;
    }

    public String getNombreAdministrador(){
        return administrador.getNombreCompleto();
    }

    public boolean ciudadanoInactivo(String nombreUsuario){
        return gestorCiudadanos.ciudadanoInactivo(nombreUsuario);
    }

    // ======= MÓDULO 1: CIUDADANOS =======

    public String registrarCiudadano(String nombreCompleto, String cedula,
                                     String correo, String telefono, String direccion,
                                     String nombreUsuario, String contrasena) throws Exception {
        if(nombreUsuario != null && nombreUsuario.equalsIgnoreCase(administrador.getNombreUsuario())){
            throw new Exception("Ese nombre de usuario está reservado.");
        }
        return gestorCiudadanos.registrarCiudadano(nombreCompleto, cedula,
                correo, telefono, direccion, nombreUsuario, contrasena);
    }

    public String consultarPerfil(String cedula) throws Exception {
        return gestorCiudadanos.consultarPerfil(cedula);
    }

    public String darDeBaja(String cedula) throws Exception {
        return gestorCiudadanos.darDeBaja(cedula);
    }

    public String resetearContrasena(String cedula) throws Exception {
        return gestorCiudadanos.resetearContrasena(cedula);
    }

    private String formatearCiudadanos(String titulo, List<Ciudadano> lista){
        if(lista.isEmpty()){
            return "No se encontraron ciudadanos.";
        }
        StringBuilder sb = new StringBuilder("===" + titulo + "===\n");
        int i = 1;
        for(Ciudadano c : lista){
            sb.append(i++).append(". ").append(c.getNombreCompleto())
              .append(" - ").append(c.getCedula())
              .append(" - ").append(c.getPuntos()).append(" pts - ")
              .append(c.getNivel()).append(" - ").append(c.getEstado()).append("\n");
        }
        return sb.toString();
    }

    public String listarCiudadanosOrdenados(String criterio){
        Comparator<Ciudadano> comparador;
        String titulo;
        switch(criterio){
            case "Cédula":
                // La lista ya está ordenada por cédula (para la búsqueda binaria).
                return formatearCiudadanos("CIUDADANOS POR CÉDULA", gestorCiudadanos.getCiudadanos());
            case "Nombre": comparador = GestorCiudadanos.POR_NOMBRE; titulo = "CIUDADANOS POR NOMBRE"; break;
            case "Nivel":  comparador = GestorCiudadanos.POR_NIVEL;  titulo = "CIUDADANOS POR NIVEL"; break;
            default:       comparador = GestorCiudadanos.POR_PUNTOS; titulo = "RANKING POR PUNTOS"; break;
        }
        return formatearCiudadanos(titulo, gestorCiudadanos.listarOrdenados(comparador));
    }

    // filtro: "BRONCE", "PLATA", "ORO", "PLATINO", "ACTIVO" o "INACTIVO".
    public String filtrarCiudadanos(String filtro){
        if(filtro.equals("ACTIVO") || filtro.equals("INACTIVO")){
            return formatearCiudadanos("CIUDADANOS EN ESTADO " + filtro,
                    gestorCiudadanos.filtrarPorEstado(filtro));
        }
        return formatearCiudadanos("CIUDADANOS DE NIVEL " + filtro,
                gestorCiudadanos.filtrarPorNivel(filtro));
    }

    // ======= MÓDULO 2: ENTREGAS =======

    private Ciudadano buscarCiudadano(String cedula) throws Exception {
        Ciudadano c = gestorCiudadanos.buscarPorCedula(cedula);
        if(c == null){
            throw new Exception("Ciudadano no encontrado.");
        }
        return c;
    }

    public String tomarTurno(String cedula) throws Exception {
        return gestorEntregas.tomarTurno(buscarCiudadano(cedula));
    }

    public String verFila(){
        return gestorEntregas.verFila();
    }

    public String consultarMiTurno(String cedula) throws Exception {
        return gestorEntregas.consultarMiTurno(buscarCiudadano(cedula));
    }

    public Turno verSiguienteEnFila(){
        return gestorEntregas.verSiguiente();
    }

    // Puntos que daría una entrega, para mostrarlos antes de confirmar.
    public int calcularPuntos(String tipoMaterial, double pesoKg) throws Exception {
        if(pesoKg <= 0 || pesoKg > 100){
            throw new Exception("El peso debe ser mayor a 0 y no superar 100 kg.");
        }
        try {
            return (int) new Material(tipoMaterial).calcularPuntos(pesoKg);
        } catch(IllegalArgumentException e){
            throw new Exception(e.getMessage());
        }
    }

    public String atenderSiguiente(String tipoMaterial, double pesoKg, String puntoLimpio) throws Exception {
        return gestorEntregas.atenderSiguiente(tipoMaterial, pesoKg,
                gestorRutas.buscarPorNombre(puntoLimpio));
    }

    public String deshacerUltimaEntrega() throws Exception {
        return gestorEntregas.deshacerUltimaEntrega();
    }

    public String consultarHistorialEntregas(String cedula, String tipoMaterial) throws Exception {
        try {
            return gestorEntregas.consultarHistorial(buscarCiudadano(cedula), tipoMaterial);
        } catch(IllegalArgumentException e){
            throw new Exception(e.getMessage());
        }
    }

    public String calcularImpactoAmbiental(String cedula) throws Exception {
        return gestorEntregas.calcularImpactoAmbiental(buscarCiudadano(cedula));
    }

    public String reporteMaterialPorTipo(){
        return gestorEntregas.reporteMaterialPorTipo();
    }

    public List<String> nombresPuntosLimpios(){
        List<String> nombres = new ArrayList<>();
        for(PuntoRecoleccion p : gestorRutas.getPuntos()){
            if(p.getTipo() == PuntoRecoleccion.Tipo.PUNTO_LIMPIO){
                nombres.add(p.getNombre());
            }
        }
        return nombres;
    }

    // ======= MÓDULO 3: RECOMPENSAS =======

    // El administrador ve el stock; el ciudadano solo ve si está disponible o agotada.
    private String formatearRecompensas(String titulo, List<Recompensa> lista, boolean conStock){
        if(lista.isEmpty()){
            return "No se encontraron recompensas.";
        }
        StringBuilder sb = new StringBuilder("===" + titulo + "===\n");
        for(Recompensa r : lista){
            sb.append("ID ").append(r.getIdRecompensa()).append(" - ").append(r.getNombre())
              .append(" (").append(r.getCategoria()).append(") - ")
              .append(r.getPuntosRequeridos()).append(" pts - ");
            if(conStock){
                sb.append("stock ").append(r.getStock()).append(" - ");
            }
            sb.append(r.getDisponibilidad()).append("\n");
        }
        return sb.toString();
    }

    public String consultarCatalogo(boolean conStock){
        return formatearRecompensas("CATÁLOGO DE RECOMPENSAS", gestorRecompensas.getRecompensas(), conStock);
    }

    public String consultarCatalogoOrdenado(String criterio){
        switch(criterio){
            case "Nombre":
                return formatearRecompensas("CATÁLOGO POR NOMBRE",
                        gestorRecompensas.listarOrdenadas(GestorRecompensas.POR_NOMBRE), true);
            case "Stock":
                return formatearRecompensas("CATÁLOGO POR STOCK",
                        gestorRecompensas.listarOrdenadas(GestorRecompensas.POR_STOCK), true);
            default:
                return formatearRecompensas("CATÁLOGO POR PUNTOS REQUERIDOS",
                        gestorRecompensas.listarOrdenadas(null), true);
        }
    }

    // filtro: "DISPONIBLES", "PRODUCTO", "DESCUENTO" o "EXPERIENCIA".
    public String filtrarCatalogo(String filtro, boolean conStock){
        if(filtro.equals("DISPONIBLES")){
            return formatearRecompensas("RECOMPENSAS DISPONIBLES", gestorRecompensas.filtrarDisponibles(), conStock);
        }
        return formatearRecompensas("CATEGORÍA " + filtro, gestorRecompensas.filtrarPorCategoria(filtro), conStock);
    }

    public String verRecompensasAlcanzables(String cedula, boolean conStock) throws Exception {
        Ciudadano c = buscarCiudadano(cedula);
        List<Recompensa> lista = gestorRecompensas.verAlcanzables(c);
        if(lista.isEmpty()){
            return c.getNombreCompleto() + " todavía no tiene puntos suficientes para ninguna recompensa " +
                    "disponible (" + c.getPuntos() + " pts).";
        }
        return formatearRecompensas("RECOMPENSAS QUE PUEDE CANJEAR - " + c.getNombreCompleto() +
                " (" + c.getPuntos() + " pts)", lista, conStock);
    }

    public String actualizarRecompensa(int idRecompensa, int puntosRequeridos, int stock) throws Exception {
        return gestorRecompensas.actualizarRecompensa(idRecompensa, puntosRequeridos, stock);
    }

    // ======= MÓDULO 4: CANJES Y REPORTES =======

    public String solicitarCanje(String cedula, int idRecompensa) throws Exception {
        Ciudadano c = buscarCiudadano(cedula);
        Recompensa r = gestorRecompensas.buscarPorId(idRecompensa);
        if(r == null){
            throw new Exception("No existe una recompensa con ID " + idRecompensa + ".");
        }
        return gestorCanjes.solicitarCanje(c, r);
    }

    public String procesarSiguienteCanje() throws Exception {
        return gestorCanjes.procesarSiguienteSolicitud();
    }

    public String consultarPendientes(){
        return gestorCanjes.consultarPendientes();
    }

    public String consultarMisSolicitudes(String cedula) throws Exception {
        return gestorCanjes.consultarMisSolicitudes(buscarCiudadano(cedula));
    }

    public String consultarHistorialCanjes(String cedula) throws Exception {
        return gestorCanjes.consultarHistorialCiudadano(buscarCiudadano(cedula));
    }

    public String generarReporte(){
        return gestorCanjes.generarReporte(gestorEntregas, gestorCiudadanos, gestorRecompensas);
    }

    // ======= MÓDULO 5: RUTAS (base del grafo) =======

    public String listarPuntos(){
        return gestorRutas.listarPuntos();
    }

    public String registrarPunto(String nombre, String sector, String tipo) throws Exception {
        PuntoRecoleccion.Tipo t = "CENTRO_ACOPIO".equals(tipo)
                ? PuntoRecoleccion.Tipo.CENTRO_ACOPIO : PuntoRecoleccion.Tipo.PUNTO_LIMPIO;
        try {
            return gestorRutas.registrarPunto(new PuntoRecoleccion(nombre, sector, t));
        } catch(IllegalArgumentException e){
            throw new Exception(e.getMessage());
        }
    }

    public String conectarPuntos(String nombreA, String nombreB, double distanciaKm) throws Exception {
        return gestorRutas.conectarPuntos(gestorRutas.buscarPorNombre(nombreA),
                gestorRutas.buscarPorNombre(nombreB), distanciaKm);
    }

    public String puntosQueRequierenRecoleccion(){
        List<PuntoRecoleccion> lista = gestorRutas.puntosQueRequierenRecoleccion();
        if(lista.isEmpty()){
            return "Ningún punto limpio llega al límite de " + gestorRutas.getLimiteRecoleccion() + " kg.";
        }
        StringBuilder sb = new StringBuilder("===PUNTOS QUE REQUIEREN RECOLECCIÓN (desde " +
                gestorRutas.getLimiteRecoleccion() + " kg)===\n");
        for(PuntoRecoleccion p : lista){
            sb.append("- ").append(p.getNombre()).append(": ").append(p.getKgAcumulados()).append(" kg\n");
        }
        return sb.toString();
    }
}

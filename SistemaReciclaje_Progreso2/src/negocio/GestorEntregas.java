package negocio;
import modelo.Ciudadano;
import modelo.Entrega;
import modelo.Material;
import modelo.PuntoRecoleccion;
import modelo.Turno;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

// Módulo 2. La fila del punto limpio es una cola (FIFO) y el deshacer usa una pila (LIFO).
// Las dos se implementan con ArrayDeque.
public class GestorEntregas {
    private List<Entrega> entregas;
    private Queue<Turno> filaAtencion;
    private Deque<Entrega> ultimasEntregas;
    private int contadorTurnos;

    public GestorEntregas(){
        entregas = new ArrayList<>();
        filaAtencion = new ArrayDeque<>();
        ultimasEntregas = new ArrayDeque<>();
        contadorTurnos = 0;
    }

    public List<Entrega> getEntregas(){
        return entregas;
    }

    // Valida y guarda una entrega. La usan atenderSiguiente y la carga de datos de prueba.
    Entrega registrarEntrega(Ciudadano ciudadano, Material material, double pesoKg,
                             PuntoRecoleccion puntoLimpio) throws Exception {
        if(ciudadano == null){
            throw new Exception("Ciudadano no encontrado.");
        }
        if(!ciudadano.estaActivo()){
            throw new Exception("El ciudadano se encuentra inactivo.");
        }
        if(material == null){
            throw new Exception("Material no válido.");
        }
        if(pesoKg <= 0 || pesoKg > 100){
            throw new Exception("El peso debe ser mayor a 0 y no superar 100 kg.");
        }
        if(puntoLimpio == null || puntoLimpio.getTipo() != PuntoRecoleccion.Tipo.PUNTO_LIMPIO){
            throw new Exception("Seleccione el punto limpio donde se recibe la entrega.");
        }
        Entrega nueva = new Entrega(ciudadano, material, pesoKg, puntoLimpio);
        entregas.add(nueva);
        ciudadano.sumarPuntos(nueva.getPuntosObtenidos());
        puntoLimpio.sumarKg(pesoKg); // los kg acumulados indican cuándo pasa el camión
        return nueva;
    }

    // Busca el turno de un ciudadano en la fila. Recorrido: O(f).
    private Turno buscarTurno(Ciudadano c){
        for(Turno t : filaAtencion){
            if(t.getCiudadano().getCedula().equals(c.getCedula())){
                return t;
            }
        }
        return null;
    }

    // Cuántas personas están antes de ese turno en la fila. Recorrido: O(f).
    private int personasAntes(Turno turno){
        int antes = 0;
        for(Turno t : filaAtencion){
            if(t == turno){
                break;
            }
            antes++;
        }
        return antes;
    }

    // Cola - offer: O(1). Revisar si el ciudadano ya está en la fila es O(f).
    public String tomarTurno(Ciudadano c) throws Exception {
        if(c == null){
            throw new Exception("Ciudadano no encontrado.");
        }
        if(!c.estaActivo()){
            throw new Exception("El ciudadano se encuentra inactivo.");
        }
        Turno existente = buscarTurno(c);
        if(existente != null){
            throw new Exception(c.getNombreCompleto() + " ya tiene el turno " + existente.getCodigo() +
                    " en la fila. Personas antes: " + personasAntes(existente) + ".");
        }
        Turno nuevo = new Turno(++contadorTurnos, c);
        int antes = filaAtencion.size();
        filaAtencion.offer(nuevo);
        return "Turno " + nuevo.getCodigo() + " asignado a " + c.getNombreCompleto() +
                ". Personas antes: " + antes + ".";
    }

    // Cola - peek: O(1). Devuelve el turno que se atenderá, sin sacarlo de la fila.
    public Turno verSiguiente(){
        return filaAtencion.peek();
    }

    public String verFila(){
        if(filaAtencion.isEmpty()){
            return "No hay ciudadanos en la fila.";
        }
        StringBuilder sb = new StringBuilder("===FILA DE ATENCIÓN (" + filaAtencion.size() + " en espera)===\n");
        for(Turno t : filaAtencion){
            sb.append(t).append("\n");
        }
        return sb.toString();
    }

    // Para el ciudadano: su número de turno y cuántas personas tiene antes.
    public String consultarMiTurno(Ciudadano c){
        Turno t = buscarTurno(c);
        if(t == null){
            return "No tiene un turno en la fila. Pulse \"Tomar turno\" para ingresar.";
        }
        return "Su turno: " + t.getCodigo() + ". Personas antes: " + personasAntes(t) + ".";
    }

    // Atiende al primero de la fila (cola - poll) y guarda la entrega en la pila (push).
    // Si los datos de la entrega son inválidos, el ciudadano no pierde su turno.
    public String atenderSiguiente(String tipoMaterial, double pesoKg,
                                   PuntoRecoleccion puntoLimpio) throws Exception {
        Turno turno = filaAtencion.peek();
        if(turno == null){
            throw new Exception("No hay ciudadanos en la fila.");
        }
        Ciudadano c = turno.getCiudadano();
        Material material;
        try {
            material = new Material(tipoMaterial);
        } catch(IllegalArgumentException e){
            throw new Exception(e.getMessage());
        }
        Entrega nueva = registrarEntrega(c, material, pesoKg, puntoLimpio);
        filaAtencion.poll();
        ultimasEntregas.push(nueva);
        return "Turno " + turno.getCodigo() + " atendido: " + c.getNombreCompleto() + ". Entrega #" + nueva.getIdEntrega() +
                " registrada: se sumaron " + nueva.getPuntosObtenidos() + " puntos.";
    }

    // Pila - pop: O(1). Anula la última entrega registrada en la atención.
    public String deshacerUltimaEntrega() throws Exception {
        if(ultimasEntregas.isEmpty()){
            throw new Exception("No hay entregas para deshacer.");
        }
        Entrega e = ultimasEntregas.peek();
        if(e.getCiudadano().getPuntos() < e.getPuntosObtenidos()){
            throw new Exception("No se puede deshacer: el ciudadano ya utilizó esos puntos.");
        }
        ultimasEntregas.pop();
        e.getCiudadano().descontarPuntos(e.getPuntosObtenidos());
        e.getPuntoLimpio().restarKg(e.getPesoKg());
        entregas.remove(e);
        return "Entrega #" + e.getIdEntrega() + " de " + e.getCiudadano().getNombreCompleto() +
                " anulada. Se restaron " + e.getPuntosObtenidos() + " puntos.";
    }

    // Historial de un ciudadano, con filtro opcional por material ("TODOS" o null = sin filtro).
    // Recorrido y filtrado: O(e).
    public String consultarHistorial(Ciudadano ciudadano, String tipoMaterial){
        if(ciudadano == null){
            return "Ciudadano no encontrado.";
        }
        String filtro = null;
        if(tipoMaterial != null && !tipoMaterial.equalsIgnoreCase("TODOS")){
            filtro = new Material(tipoMaterial).getTipoMaterial();
        }
        StringBuilder sb = new StringBuilder("===HISTORIAL DE ENTREGAS - " + ciudadano.getNombreCompleto() + "===\n");
        if(filtro != null){
            sb.append("Material: ").append(filtro).append("\n");
        }
        double totalKg = 0;
        int totalPuntos = 0;
        int cantidad = 0;
        for(Entrega e : entregas){
            if(e.getCiudadano() == ciudadano
                    && (filtro == null || e.getMaterial().getTipoMaterial().equals(filtro))){
                sb.append(e).append("\n-----------\n");
                totalKg += e.getPesoKg();
                totalPuntos += e.getPuntosObtenidos();
                cantidad++;
            }
        }
        if(cantidad == 0){
            return "No hay entregas registradas para este ciudadano" +
                    (filtro != null ? " con ese material." : ".");
        }
        sb.append("Total kg reciclados: ").append(totalKg)
          .append("\nTotal puntos obtenidos: ").append(totalPuntos);
        return sb.toString();
    }

    public String calcularImpactoAmbiental(Ciudadano ciudadano){
        if(ciudadano == null){
            return "Ciudadano no encontrado.";
        }
        double totalKg = 0;
        for(Entrega e : entregas){
            if(e.getCiudadano() == ciudadano){
                totalKg += e.getPesoKg();
            }
        }
        if(totalKg == 0){
            return "No hay entregas registradas para calcular el impacto.";
        }
        double co2Evitado = totalKg * 1.5;
        double arboles = co2Evitado / 21;
        return "===IMPACTO AMBIENTAL===\n" +
                "Ciudadano: " + ciudadano.getNombreCompleto() + "\n" +
                "Total kg reciclados: " + totalKg + "\n" +
                "CO2 evitado: " + String.format("%.2f", co2Evitado) + " kg\n" +
                "Equivale a " + String.format("%.2f", arboles) + " árboles";
    }

    public double totalKgReciclados(){
        double total = 0;
        for(Entrega e : entregas){
            total += e.getPesoKg();
        }
        return total;
    }

    public String reporteMaterialPorTipo(){
        if(entregas.isEmpty()){
            return "No hay entregas registradas.";
        }
        double plastico = 0, vidrio = 0, papel = 0, metal = 0, electronico = 0;
        for(Entrega e : entregas){
            switch(e.getMaterial().getTipoMaterial()){
                case "PLÁSTICO":    plastico += e.getPesoKg(); break;
                case "VIDRIO":      vidrio += e.getPesoKg(); break;
                case "PAPEL":       papel += e.getPesoKg(); break;
                case "METAL":       metal += e.getPesoKg(); break;
                case "ELECTRÓNICO": electronico += e.getPesoKg(); break;
                default: break;
            }
        }
        return "===MATERIAL RECICLADO POR TIPO===\n" +
                "PLÁSTICO:    " + plastico + " kg\n" +
                "VIDRIO:      " + vidrio + " kg\n" +
                "PAPEL:       " + papel + " kg\n" +
                "METAL:       " + metal + " kg\n" +
                "ELECTRÓNICO: " + electronico + " kg\n" +
                "TOTAL:       " + (plastico + vidrio + papel + metal + electronico) + " kg";
    }
}

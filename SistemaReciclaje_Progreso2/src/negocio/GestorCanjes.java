package negocio;
import modelo.Canje;
import modelo.Ciudadano;
import modelo.Recompensa;
import modelo.SolicitudCanje;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

// Módulo 4. Las solicitudes esperan en una cola de prioridad (PriorityQueue).
// El orden lo define SolicitudCanje con Comparable: nivel al solicitar y orden de llegada.
public class GestorCanjes {
    private List<Canje> canjes;
    private PriorityQueue<SolicitudCanje> solicitudesPendientes;
    private List<SolicitudCanje> solicitudesProcesadas;
    private int contadorLlegada;

    public GestorCanjes(){
        canjes = new ArrayList<>();
        solicitudesPendientes = new PriorityQueue<>();
        solicitudesProcesadas = new ArrayList<>();
        contadorLlegada = 0;
    }

    public List<Canje> getCanjes(){
        return canjes;
    }

    // Comprueba que el ciudadano pueda canjear la recompensa en este momento.
    private void verificarElegibilidad(Ciudadano c, Recompensa r) throws Exception {
        if(c == null){
            throw new Exception("Ciudadano no encontrado.");
        }
        if(!c.estaActivo()){
            throw new Exception("El ciudadano se encuentra inactivo.");
        }
        if(r == null){
            throw new Exception("Recompensa no encontrada.");
        }
        if(!r.verificarStock()){
            throw new Exception("La recompensa \"" + r.getNombre() + "\" está agotada.");
        }
        if(c.getPuntos() < r.getPuntosRequeridos()){
            int faltan = r.getPuntosRequeridos() - c.getPuntos();
            throw new Exception("Puntos insuficientes. Le faltan " + faltan + " puntos.");
        }
    }

    // Busca si el ciudadano ya tiene una solicitud en espera. Recorrido: O(k).
    private SolicitudCanje buscarPendiente(Ciudadano c){
        for(SolicitudCanje s : solicitudesPendientes){
            if(s.getCiudadano() == c){
                return s;
            }
        }
        return null;
    }

    // Cola de prioridad - offer: O(log k).
    public String solicitarCanje(Ciudadano c, Recompensa r) throws Exception {
        verificarElegibilidad(c, r);
        if(buscarPendiente(c) != null){
            throw new Exception("El ciudadano ya tiene una solicitud pendiente.");
        }
        SolicitudCanje s = new SolicitudCanje(c, r, ++contadorLlegada);
        solicitudesPendientes.offer(s);
        return "Solicitud registrada: " + r.getNombre() + " para " + c.getNombreCompleto() +
                " (prioridad " + s.getNivelAlSolicitar() + ").\nLos puntos se descontarán cuando el " +
                "administrador procese la solicitud. Solicitudes en espera: " + solicitudesPendientes.size() + ".";
    }

    // Cola de prioridad - poll: O(log k). Antes de canjear se vuelve a verificar
    // la elegibilidad, porque el stock o los puntos pudieron cambiar mientras esperaba.
    public String procesarSiguienteSolicitud() throws Exception {
        SolicitudCanje s = solicitudesPendientes.poll();
        if(s == null){
            throw new Exception("No hay solicitudes pendientes.");
        }
        solicitudesProcesadas.add(s);
        try {
            String detalle = realizarCanje(s.getCiudadano(), s.getRecompensa());
            s.marcarAtendida();
            return "Solicitud de " + s.getCiudadano().getNombreCompleto() + " (" +
                    s.getNivelAlSolicitar() + ") atendida.\n" + detalle;
        } catch(Exception e){
            s.marcarRechazada(e.getMessage());
            return "Solicitud de " + s.getCiudadano().getNombreCompleto() +
                    " rechazada: " + e.getMessage();
        }
    }

    // Muestra las solicitudes en el orden en que se atenderán.
    // Se saca de una copia para no vaciar la cola real: O(k log k).
    public String consultarPendientes(){
        if(solicitudesPendientes.isEmpty()){
            return "No hay solicitudes pendientes.";
        }
        PriorityQueue<SolicitudCanje> copia = new PriorityQueue<>(solicitudesPendientes);
        StringBuilder sb = new StringBuilder("===SOLICITUDES PENDIENTES (" + copia.size() +
                ")===\nSe atenderán en este orden:\n");
        int i = 1;
        while(!copia.isEmpty()){
            sb.append(i++).append(". ").append(copia.poll()).append("\n");
        }
        return sb.toString();
    }

    // Para el ciudadano: su solicitud en espera (con su lugar de atención)
    // y el resultado de las solicitudes que ya fueron procesadas.
    public String consultarMisSolicitudes(Ciudadano c){
        StringBuilder sb = new StringBuilder("===MIS SOLICITUDES===\n");
        SolicitudCanje mia = buscarPendiente(c);
        if(mia != null){
            int delante = 0;
            for(SolicitudCanje s : solicitudesPendientes){
                if(s.compareTo(mia) < 0){
                    delante++;
                }
            }
            sb.append("EN ESPERA: ").append(mia.getRecompensa().getNombre())
              .append(" (prioridad ").append(mia.getNivelAlSolicitar()).append(")\n")
              .append("Será atendida en el lugar ").append(delante + 1).append("; hay ")
              .append(solicitudesPendientes.size()).append(" solicitud(es) en espera.\n")
              .append("El lugar puede cambiar si llega una solicitud de un nivel más alto.\n\n");
        }
        boolean hayProcesadas = false;
        for(SolicitudCanje s : solicitudesProcesadas){
            if(s.getCiudadano() == c){
                sb.append(s.getEstado()).append(": ").append(s.getRecompensa().getNombre())
                  .append(" - ").append(s.getResultado()).append("\n");
                hayProcesadas = true;
            }
        }
        if(mia == null && !hayProcesadas){
            return "No ha realizado solicitudes de canje.";
        }
        return sb.toString();
    }

    // Descuenta los puntos, reduce el stock y guarda el canje.
    String realizarCanje(Ciudadano ciudadano, Recompensa recompensa) throws Exception {
        verificarElegibilidad(ciudadano, recompensa);
        Canje nuevo = new Canje(ciudadano, recompensa);
        String resultado = nuevo.realizarCanje();
        canjes.add(nuevo);
        return resultado;
    }

    public String consultarHistorialCiudadano(Ciudadano ciudadano){
        if(ciudadano == null){
            return "Ciudadano no encontrado.";
        }
        StringBuilder sb = new StringBuilder("===HISTORIAL DE CANJES - " + ciudadano.getNombreCompleto() + "===\n");
        int total = 0;
        int cantidad = 0;
        for(Canje c : canjes){
            if(c.getCiudadano() == ciudadano){
                sb.append(c).append("\n-----------\n");
                total += c.getPuntosDescontados();
                cantidad++;
            }
        }
        if(cantidad == 0){
            return "No hay canjes registrados para este ciudadano.";
        }
        sb.append("Total puntos canjeados: ").append(total);
        return sb.toString();
    }

    // Recibe la lista ya ordenada por puntos (Merge Sort con Comparator) y toma los 5 primeros.
    public String rankingTop5(List<Ciudadano> ordenadosPorPuntos){
        StringBuilder sb = new StringBuilder("===TOP 5 CIUDADANOS===\n");
        int limite = Math.min(5, ordenadosPorPuntos.size());
        for(int i = 0; i < limite; i++){
            Ciudadano c = ordenadosPorPuntos.get(i);
            sb.append(i + 1).append(". ").append(c.getNombreCompleto()).append(" - ")
              .append(c.getPuntos()).append(" puntos - ").append(c.getNivel()).append("\n");
        }
        return sb.toString();
    }

    // Recompensa más canjeada: recorrido de recompensas y canjes, O(r · c).
    private String recompensaMasCanjeada(List<Recompensa> recompensas){
        Recompensa mas = null;
        int maxVeces = 0;
        for(Recompensa r : recompensas){
            int veces = 0;
            for(Canje c : canjes){
                if(c.getRecompensa() == r){
                    veces++;
                }
            }
            if(veces > maxVeces){
                maxVeces = veces;
                mas = r;
            }
        }
        if(mas == null){
            return "Todavía no hay canjes registrados.";
        }
        return mas.getNombre() + " - canjeada " + maxVeces + (maxVeces == 1 ? " vez" : " veces");
    }

    public String generarReporte(GestorEntregas gestorEntregas,
                                 GestorCiudadanos gestorCiudadanos,
                                 GestorRecompensas gestorRecompensas){
        double totalKg = gestorEntregas.totalKgReciclados();
        double co2 = totalKg * 1.5;
        return "===REPORTE GENERAL DEL PROGRAMA===\n\n" +
                gestorEntregas.reporteMaterialPorTipo() + "\n\n" +
                "CO2 total evitado: " + String.format("%.2f", co2) + " kg\n" +
                "Equivale a " + String.format("%.2f", co2 / 21) + " árboles\n\n" +
                rankingTop5(gestorCiudadanos.listarOrdenados(GestorCiudadanos.POR_PUNTOS)) + "\n" +
                "===RECOMPENSA MÁS CANJEADA===\n" +
                recompensaMasCanjeada(gestorRecompensas.getRecompensas());
    }
}

package modelo;

// Solicitud de canje que espera en la cola de prioridad del módulo 4.
// Guarda el nivel que tenía el ciudadano al solicitar, para que su prioridad
// no cambie si sube o baja de nivel mientras espera.
public class SolicitudCanje implements Comparable<SolicitudCanje> {
    private static int contadorId = 0;

    private int idSolicitud;
    private Ciudadano ciudadano;
    private Recompensa recompensa;
    private int ordenLlegada;
    private String nivelAlSolicitar;
    private String estado;    // PENDIENTE, ATENDIDA o RECHAZADA
    private String resultado; // detalle cuando la solicitud ya fue procesada

    public SolicitudCanje(Ciudadano ciudadano, Recompensa recompensa, int ordenLlegada){
        if(ciudadano == null || recompensa == null){
            throw new IllegalArgumentException("La solicitud necesita un ciudadano y una recompensa.");
        }
        this.idSolicitud = ++contadorId;
        this.ciudadano = ciudadano;
        this.recompensa = recompensa;
        this.ordenLlegada = ordenLlegada;
        this.nivelAlSolicitar = ciudadano.getNivel();
        this.estado = "PENDIENTE";
        this.resultado = "";
    }

    public int getIdSolicitud(){ return idSolicitud; }
    public Ciudadano getCiudadano(){ return ciudadano; }
    public Recompensa getRecompensa(){ return recompensa; }
    public int getOrdenLlegada(){ return ordenLlegada; }
    public String getNivelAlSolicitar(){ return nivelAlSolicitar; }
    public String getEstado(){ return estado; }
    public String getResultado(){ return resultado; }

    public void marcarAtendida(){
        this.estado = "ATENDIDA";
        this.resultado = "Se descontaron " + recompensa.getPuntosRequeridos() + " puntos.";
    }

    public void marcarRechazada(String motivo){
        this.estado = "RECHAZADA";
        this.resultado = motivo;
    }

    // Primero se atiende el nivel más alto (PLATINO, ORO, PLATA, BRONCE).
    // Si dos solicitudes tienen el mismo nivel, va primero la que llegó antes.
    @Override
    public int compareTo(SolicitudCanje otra){
        int cmp = Integer.compare(Ciudadano.valorNivel(otra.nivelAlSolicitar),
                Ciudadano.valorNivel(this.nivelAlSolicitar));
        if(cmp != 0) return cmp;
        return Integer.compare(this.ordenLlegada, otra.ordenLlegada);
    }

    @Override
    public String toString(){
        return "Solicitud #" + idSolicitud + " - " + ciudadano.getNombreCompleto() +
                " (" + nivelAlSolicitar + ") -> " + recompensa.getNombre();
    }
}

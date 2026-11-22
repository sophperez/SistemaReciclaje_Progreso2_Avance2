package modelo;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Canje {
    private static int contadorId = 0;

    private int idCanje;
    private Ciudadano ciudadano;
    private Recompensa recompensa;
    private String fecha;
    private int puntosDescontados;

    public Canje(Ciudadano ciudadano, Recompensa recompensa){
        this.idCanje = ++contadorId;
        this.ciudadano = ciudadano;
        this.recompensa = recompensa;
        this.fecha = obtenerFechaActual();
        this.puntosDescontados = recompensa.getPuntosRequeridos();
    }

    public int getIdCanje(){
        return idCanje;
    }

    public Ciudadano getCiudadano(){
        return ciudadano;
    }

    public Recompensa getRecompensa(){
        return recompensa;
    }

    public String getFecha(){
        return fecha;
    }

    public int getPuntosDescontados(){
        return puntosDescontados;
    }

    private String obtenerFechaActual(){
        GregorianCalendar gc = new GregorianCalendar();
        int dia = gc.get(Calendar.DAY_OF_MONTH);
        int mes = gc.get(Calendar.MONTH) + 1;
        int anio = gc.get(Calendar.YEAR);
        return dia + "/" + mes + "/" + anio;
    }

    public String realizarCanje(){
        ciudadano.descontarPuntos(puntosDescontados);
        recompensa.reducirStock();
        return "Canje realizado exitosamente. Se han descontado " +
                puntosDescontados + " puntos de tu cuenta.";
    }

    @Override
    public String toString(){
        return "ID canje: " + idCanje +
                "\nCiudadano: " + ciudadano.getNombreCompleto() +
                "\nRecompensa: " + recompensa.getNombre() +
                "\nFecha: " + fecha +
                "\nPuntos descontados: " + puntosDescontados;
    }

}

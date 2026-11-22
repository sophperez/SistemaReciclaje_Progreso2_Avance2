package modelo;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Entrega {
    private static int contadorId = 0;

    private int idEntrega;
    private Ciudadano ciudadano;
    private Material material;
    private double pesoKg;
    private String fecha;
    private int puntosObtenidos;
    private PuntoRecoleccion puntoLimpio;

    public Entrega(Ciudadano ciudadano, Material material,
                   double pesoKg, PuntoRecoleccion puntoLimpio){
        this.idEntrega = ++contadorId;
        this.ciudadano = ciudadano;
        this.material = material;
        this.pesoKg = pesoKg;
        this.fecha = obtenerFechaActual();
        this.puntosObtenidos = calcularPuntos();
        this.puntoLimpio = puntoLimpio;
    }

    public int getIdEntrega(){
        return idEntrega;
    }

    public Ciudadano getCiudadano(){
        return ciudadano;
    }

    public Material getMaterial(){
        return material;
    }

    public double getPesoKg(){
        return pesoKg;
    }

    public PuntoRecoleccion getPuntoLimpio(){
        return puntoLimpio;
    }

    public String getFecha(){
        return fecha;
    }

    public int getPuntosObtenidos(){
        return puntosObtenidos;
    }

    private String obtenerFechaActual(){
        GregorianCalendar gc = new GregorianCalendar();
        int dia = gc.get(Calendar.DAY_OF_MONTH);
        int mes = gc.get(Calendar.MONTH) + 1;
        int anio = gc.get(Calendar.YEAR);
        return dia + "/" + mes + "/" + anio;
    }

    public int calcularPuntos(){
        return (int) material.calcularPuntos(pesoKg);
    }

    @Override
    public String toString(){
        return "ID entrega: "+idEntrega+"\nCiudadano: "+ciudadano.getNombreCompleto()+"\nMaterial: " + material.getTipoMaterial()+
                "\nPeso: "+pesoKg+" kg"+
                "\nPunto limpio: "+(puntoLimpio != null ? puntoLimpio.getNombre() : "-")+"\nFecha: "+fecha+"\nPuntos obtenidos: "+puntosObtenidos;
    }
}

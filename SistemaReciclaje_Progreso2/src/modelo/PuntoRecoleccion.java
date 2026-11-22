package modelo;

// Vértice del grafo de rutas: un punto limpio o un centro de acopio.
public class PuntoRecoleccion {
    public enum Tipo { PUNTO_LIMPIO, CENTRO_ACOPIO }

    private static int contadorId = 0;
    private final int id;
    private String nombre;
    private String sector;
    private Tipo tipo;
    private double kgAcumulados;
    private int indice; // posición del punto en la lista de adyacencia del grafo

    public PuntoRecoleccion(String nombre, String sector, Tipo tipo){
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre del punto no puede estar vacío.");
        }
        if(tipo == null){
            throw new IllegalArgumentException("El tipo no puede ser nulo.");
        }
        this.id = ++contadorId;
        this.nombre = nombre;
        this.sector = sector;
        this.tipo = tipo;
        this.kgAcumulados = 0.0;
        this.indice = -1;
    }

    public int getId(){ return id; }
    public String getNombre(){ return nombre; }
    public String getSector(){ return sector; }
    public Tipo getTipo(){ return tipo; }
    public double getKgAcumulados(){ return kgAcumulados; }
    public int getIndice(){ return indice; }
    public void setIndice(int indice){ this.indice = indice; }

    public void setSector(String sector){ this.sector = sector; }

    public void sumarKg(double kg){
        if(kg > 0) this.kgAcumulados += kg;
    }

    public void restarKg(double kg){
        if(kg > 0) this.kgAcumulados = Math.max(0, this.kgAcumulados - kg);
    }

    public void reiniciarKg(){
        this.kgAcumulados = 0.0;
    }

    @Override
    public String toString(){
        return "Punto #" + id + " - " + nombre + " (" + tipo + ") - " +
                sector + " - " + kgAcumulados + " kg acumulados";
    }
}

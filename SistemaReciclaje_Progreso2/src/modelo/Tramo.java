package modelo;

// Arista del grafo: une dos puntos en ambos sentidos con una distancia en km.
public class Tramo {
    private final PuntoRecoleccion origen;
    private final PuntoRecoleccion destino;
    private final double distanciaKm;

    public Tramo(PuntoRecoleccion origen, PuntoRecoleccion destino, double distanciaKm){
        if(origen == null || destino == null){
            throw new IllegalArgumentException("Los puntos del tramo no pueden ser nulos.");
        }
        if(origen.getId() == destino.getId()){
            throw new IllegalArgumentException("Un punto no puede conectarse consigo mismo.");
        }
        if(distanciaKm <= 0){
            throw new IllegalArgumentException("La distancia debe ser mayor a 0 km.");
        }
        this.origen = origen;
        this.destino = destino;
        this.distanciaKm = distanciaKm;
    }

    public PuntoRecoleccion getOrigen(){ return origen; }
    public PuntoRecoleccion getDestino(){ return destino; }
    public double getDistanciaKm(){ return distanciaKm; }

    // Devuelve el otro extremo del tramo visto desde el punto p.
    public PuntoRecoleccion otroExtremo(PuntoRecoleccion p){
        return origen == p ? destino : origen;
    }

    public boolean conecta(PuntoRecoleccion a, PuntoRecoleccion b){
        return (origen == a && destino == b) || (origen == b && destino == a);
    }

    @Override
    public String toString(){
        return origen.getNombre() + " <-> " + destino.getNombre() +
                " (" + distanciaKm + " km)";
    }
}

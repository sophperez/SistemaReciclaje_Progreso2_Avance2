package negocio;

import modelo.PuntoRecoleccion;
import modelo.Tramo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Módulo 5 (base). El mapa es un grafo guardado como lista de adyacencia:
// en la posición i de "adyacencia" están los tramos del punto con índice i.
// En este avance se construye el grafo; la ruta más corta, el centro de acopio
// más cercano y la verificación de conexión quedan para la entrega final.
public class GestorRutas {
    private List<PuntoRecoleccion> puntos;
    private List<List<Tramo>> adyacencia;
    private double limiteRecoleccion;

    public GestorRutas(){
        puntos = new ArrayList<>();
        adyacencia = new ArrayList<>();
        limiteRecoleccion = 200.0;
    }

    public List<PuntoRecoleccion> getPuntos(){ return puntos; }
    public double getLimiteRecoleccion(){ return limiteRecoleccion; }

    // Búsqueda secuencial por nombre: O(V).
    public PuntoRecoleccion buscarPorNombre(String nombre){
        if(nombre == null) return null;
        for(PuntoRecoleccion p : puntos){
            if(p.getNombre().equalsIgnoreCase(nombre.trim())){
                return p;
            }
        }
        return null;
    }

    // Agrega un vértice: O(V) por la verificación de nombre único.
    public String registrarPunto(PuntoRecoleccion p) throws Exception {
        if(p == null) throw new Exception("El punto no puede estar vacío.");
        if(buscarPorNombre(p.getNombre()) != null){
            throw new Exception("Ya existe un punto llamado \"" + p.getNombre() + "\".");
        }
        p.setIndice(puntos.size());
        puntos.add(p);
        adyacencia.add(new ArrayList<>());
        return "Punto registrado: " + p.getNombre();
    }

    // Agrega una arista en ambos sentidos. Revisar duplicados recorre los tramos del punto.
    public String conectarPuntos(PuntoRecoleccion a, PuntoRecoleccion b,
                                 double distanciaKm) throws Exception {
        if(a == null || b == null) throw new Exception("Alguno de los puntos no existe en el mapa.");
        if(a == b) throw new Exception("Un punto no puede conectarse consigo mismo.");
        if(distanciaKm <= 0) throw new Exception("La distancia debe ser mayor a 0 km.");
        for(Tramo t : adyacencia.get(a.getIndice())){
            if(t.conecta(a, b)){
                throw new Exception("Ya existe un tramo entre esos dos puntos.");
            }
        }
        Tramo tramo = new Tramo(a, b, distanciaKm);
        adyacencia.get(a.getIndice()).add(tramo);
        adyacencia.get(b.getIndice()).add(tramo);
        return "Tramo registrado: " + tramo;
    }

    public String listarPuntos(){
        if(puntos.isEmpty()){
            return "No hay puntos registrados.";
        }
        StringBuilder sb = new StringBuilder("===PUNTOS DEL MAPA===\n");
        for(PuntoRecoleccion p : puntos){
            sb.append(p).append("\n");
            for(Tramo t : adyacencia.get(p.getIndice())){
                sb.append("     conecta con ").append(t.otroExtremo(p).getNombre())
                  .append(" (").append(t.getDistanciaKm()).append(" km)\n");
            }
        }
        return sb.toString();
    }

    // Puntos que superan el límite de kg, del más lleno al menos lleno.
    // Filtrado O(V) + Merge Sort O(V log V).
    public List<PuntoRecoleccion> puntosQueRequierenRecoleccion(){
        List<PuntoRecoleccion> resultado = new ArrayList<>();
        for(PuntoRecoleccion p : puntos){
            if(p.getKgAcumulados() >= limiteRecoleccion){
                resultado.add(p);
            }
        }
        resultado.sort(Comparator.comparingDouble(PuntoRecoleccion::getKgAcumulados).reversed());
        return resultado;
    }
}

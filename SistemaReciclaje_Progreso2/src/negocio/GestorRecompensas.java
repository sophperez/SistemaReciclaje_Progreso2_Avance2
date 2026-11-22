package negocio;
import modelo.Ciudadano;
import modelo.Recompensa;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Módulo 3. Las recompensas se agregan al final y el id se asigna en orden,
// así que la lista queda ordenada por id y se puede buscar con búsqueda binaria.
public class GestorRecompensas {
    private List<Recompensa> recompensas;

    // Criterios alternativos (Comparator). El orden natural (Comparable) es por puntos.
    public static final Comparator<Recompensa> POR_NOMBRE =
            Comparator.comparing(Recompensa::getNombre, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Recompensa> POR_STOCK =
            Comparator.comparingInt(Recompensa::getStock).reversed()
                    .thenComparing(Recompensa::getNombre);

    public GestorRecompensas(){
        recompensas = new ArrayList<>();
    }

    public List<Recompensa> getRecompensas(){
        return recompensas;
    }

    // Búsqueda binaria por id: O(log r).
    public Recompensa buscarPorId(int idRecompensa){
        int inicio = 0;
        int fin = recompensas.size() - 1;
        while(inicio <= fin){
            int medio = (inicio + fin) / 2;
            int idMedio = recompensas.get(medio).getIdRecompensa();
            if(idMedio == idRecompensa){
                return recompensas.get(medio);
            } else if(idMedio < idRecompensa){
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return null;
    }

    public String registrarRecompensa(String nombre, String descripcion,
                                      String categoria, int puntosRequeridos,
                                      int stock) throws Exception {
        try {
            recompensas.add(new Recompensa(nombre, descripcion, categoria, puntosRequeridos, stock));
            return "Recompensa agregada al catálogo.";
        } catch(IllegalArgumentException e){
            throw new Exception(e.getMessage());
        }
    }

    // Solo el administrador: cambia los puntos requeridos y el stock.
    public String actualizarRecompensa(int idRecompensa, int puntosRequeridos, int stock) throws Exception {
        Recompensa r = buscarPorId(idRecompensa);
        if(r == null){
            throw new Exception("No existe una recompensa con ID " + idRecompensa + ".");
        }
        if(puntosRequeridos <= 0){
            throw new Exception("Los puntos requeridos deben ser mayores a 0.");
        }
        if(stock < 0){
            throw new Exception("El stock no puede ser negativo.");
        }
        r.setPuntosRequeridos(puntosRequeridos);
        r.setStock(stock);
        return "Recompensa actualizada: " + r.getNombre() + " - " + puntosRequeridos +
                " pts - stock " + stock + " (" + r.getDisponibilidad() + ").";
    }

    // Ordena una copia con List.sort (Merge Sort): O(r log r).
    // Con criterio null se usa el orden natural de Comparable (por puntos).
    public List<Recompensa> listarOrdenadas(Comparator<Recompensa> criterio){
        List<Recompensa> copia = new ArrayList<>(recompensas);
        copia.sort(criterio);
        return copia;
    }

    // Filtrado por recorrido: O(r).
    public List<Recompensa> filtrarPorCategoria(String categoria){
        List<Recompensa> resultado = new ArrayList<>();
        for(Recompensa r : recompensas){
            if(r.getCategoria().equalsIgnoreCase(categoria)){
                resultado.add(r);
            }
        }
        return resultado;
    }

    public List<Recompensa> filtrarDisponibles(){
        List<Recompensa> resultado = new ArrayList<>();
        for(Recompensa r : recompensas){
            if(r.verificarStock()){
                resultado.add(r);
            }
        }
        return resultado;
    }

    // Recompensas que el ciudadano puede canjear ahora, de la más barata a la más cara.
    // Filtrado O(r) + Merge Sort O(r log r).
    public List<Recompensa> verAlcanzables(Ciudadano ciudadano){
        List<Recompensa> alcanzables = new ArrayList<>();
        for(Recompensa r : recompensas){
            if(r.verificarStock() && ciudadano.getPuntos() >= r.getPuntosRequeridos()){
                alcanzables.add(r);
            }
        }
        alcanzables.sort(null);
        return alcanzables;
    }
}

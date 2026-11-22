package modelo;

// Implementa Comparable: el orden natural del catálogo es por puntos requeridos.
public class Recompensa implements Comparable<Recompensa> {
    private static int contadorId = 0;
    private int idRecompensa;
    private String nombre, descripcion, categoria;
    private int puntosRequeridos, stock;
    private String disponibilidad;

    public Recompensa(String nombre, String descripcion, String categoria,
                      int puntosRequeridos, int stock){
        this.idRecompensa = ++contadorId;
        setNombre(nombre);
        setDescripcion(descripcion);
        setCategoria(categoria);
        setPuntosRequeridos(puntosRequeridos);
        setStock(stock);
        actualizarDisponibilidad();
    }

    public int getIdRecompensa(){
        return idRecompensa;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre de la recompensa no puede estar vacío.");
        }
        for(int i = 0; i < nombre.length(); i++){
            char c = nombre.charAt(i);
            if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
                    c == ' ' || c == 'á' || c == 'é' || c == 'í' ||
                    c == 'ó' || c == 'ú' || c == 'Á' || c == 'É' ||
                    c == 'Í' || c == 'Ó' || c == 'Ú' || c == 'ñ' ||
                    c == 'Ñ')){
                throw new IllegalArgumentException(
                        "El nombre solo puede contener letras y espacios.");
            }
        }
        this.nombre = nombre;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(String descripcion){
        if(descripcion == null || descripcion.isBlank()){
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        for(int i = 0; i < descripcion.length(); i++){
            char c = descripcion.charAt(i);
            if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
                    (c >= '0' && c <= '9') || c == ' ' || c == 'á' ||
                    c == 'é' || c == 'í' || c == 'ó' || c == 'ú' ||
                    c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' ||
                    c == 'Ú' || c == 'ñ' || c == 'Ñ')){
                throw new IllegalArgumentException(
                        "La descripción contiene caracteres no permitidos.");
            }
        }
        this.descripcion = descripcion;
    }

    public String getCategoria(){
        return categoria;
    }

    public void setCategoria(String categoria){
        if(categoria == null || categoria.isBlank()){
            throw new IllegalArgumentException("La categoría no puede estar vacía.");
        }
        if(categoria.equalsIgnoreCase("PRODUCTO")){
            this.categoria = "PRODUCTO";
        } else if(categoria.equalsIgnoreCase("DESCUENTO")){
            this.categoria = "DESCUENTO";
        } else if(categoria.equalsIgnoreCase("EXPERIENCIA")){
            this.categoria = "EXPERIENCIA";
        } else {
            throw new IllegalArgumentException(
                    "La categoría debe ser PRODUCTO, DESCUENTO o EXPERIENCIA.");
        }
    }

    public int getPuntosRequeridos(){
        return puntosRequeridos;
    }

    public void setPuntosRequeridos(int puntosRequeridos){
        if(puntosRequeridos <= 0){
            throw new IllegalArgumentException("Los puntos requeridos deben ser mayores a 0.");
        }
        this.puntosRequeridos = puntosRequeridos;
    }

    public int getStock(){
        return stock;
    }

    public void setStock(int stock){
        if(stock < 0){
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stock = stock;
        actualizarDisponibilidad();
    }

    public String getDisponibilidad(){
        return disponibilidad;
    }

    public void actualizarDisponibilidad(){
        if(stock > 0){
            this.disponibilidad = "DISPONIBLE";
        } else {
            this.disponibilidad = "AGOTADO";
        }
    }

    public boolean verificarStock(){
        return stock > 0;
    }

    public void reducirStock(){
        if(stock > 0){
            stock--;
            actualizarDisponibilidad();
        }
    }

    // Orden natural: de menos a más puntos requeridos; si empatan, por nombre.
    @Override
    public int compareTo(Recompensa otra){
        int cmp = Integer.compare(this.puntosRequeridos, otra.puntosRequeridos);
        if(cmp != 0) return cmp;
        return this.nombre.compareToIgnoreCase(otra.nombre);
    }

    @Override
    public String toString(){
        return "ID recompensa: "+idRecompensa+"\nNombre: "+nombre+
                "\nDescripción: "+descripcion+"\nCategoría: "+categoria+
                "\nPuntos requeridos: "+puntosRequeridos+"\nStock: "+stock+
                "\nDisponibilidad: "+disponibilidad;
    }

}

package modelo;

public class Ciudadano extends Usuario{
    private int puntos;
    private String nivel, estado;

    public Ciudadano(String nombreCompleto, String cedula, String correo, String telefono, String direccion,
                     String nombreUsuario, String contrasena){
        super(nombreCompleto, cedula, correo, telefono, direccion,
                nombreUsuario, contrasena);
        this.puntos = 0;
        this.nivel = "BRONCE";
        this.estado = "ACTIVO";
    }

    public int getPuntos(){
        return puntos;
    }

    public String getNivel(){
        return nivel;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        if(estado == null || estado.isBlank()){
            this.estado = "ACTIVO";
        } else if(estado.equalsIgnoreCase("ACTIVO")){
            this.estado = "ACTIVO";
        } else if(estado.equalsIgnoreCase("INACTIVO")){
            this.estado = "INACTIVO";
        } else {
            this.estado = "ACTIVO";
        }
    }

    public void actualizarNivel(){
        if(puntos >= 3000){
            this.nivel = "PLATINO";
        } else if(puntos >= 1500){
            this.nivel = "ORO";
        } else if(puntos >= 500){
            this.nivel = "PLATA";
        } else {
            this.nivel = "BRONCE";
        }
    }

    public void sumarPuntos(int puntosGanados){
        if(puntosGanados > 0){
            this.puntos += puntosGanados;
            actualizarNivel();
        }
    }

    public void descontarPuntos(int puntosDescontados){
        if(puntosDescontados > 0 && puntosDescontados <= this.puntos){
            this.puntos -= puntosDescontados;
            actualizarNivel();
        }
    }

    // Convierte el nivel en un número para poder compararlo:
    // PLATINO = 4, ORO = 3, PLATA = 2, BRONCE = 1.
    public static int valorNivel(String nivel){
        if(nivel == null) return 1;
        switch(nivel){
            case "PLATINO": return 4;
            case "ORO":     return 3;
            case "PLATA":   return 2;
            default:        return 1;
        }
    }

    public boolean estaActivo(){
        return this.estado.equals("ACTIVO");
    }

    @Override
    public String toString(){
        return "===CIUDADANO===\n"+super.toString()+"\nPuntos acumulados: "+puntos+"\nNivel: "+ nivel+ "\nEstado: "+estado;
    }
}

package modelo;

// Ticket de la fila del punto limpio: número de turno y ciudadano que lo tomó.
public class Turno {
    private int numero;
    private Ciudadano ciudadano;

    public Turno(int numero, Ciudadano ciudadano){
        this.numero = numero;
        this.ciudadano = ciudadano;
    }

    public int getNumero(){ return numero; }
    public Ciudadano getCiudadano(){ return ciudadano; }

    // Número con cuatro cifras, como en un ticket: 0001, 0002...
    public String getCodigo(){
        return String.format("%04d", numero);
    }

    @Override
    public String toString(){
        return getCodigo() + " - " + ciudadano.getNombreCompleto() + " - " + ciudadano.getCedula();
    }
}

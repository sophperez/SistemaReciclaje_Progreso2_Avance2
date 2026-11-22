package modelo;

import java.util.ArrayList;
import java.util.List;

// Resultado del cálculo de una ruta: secuencia de puntos y distancia total.
// Se usará en la entrega final, cuando se implemente la ruta más corta.
public class Ruta {
    private final List<PuntoRecoleccion> secuencia;
    private double distanciaTotalKm;

    public Ruta(){
        this.secuencia = new ArrayList<>();
        this.distanciaTotalKm = 0.0;
    }

    public List<PuntoRecoleccion> getSecuencia(){ return secuencia; }
    public double getDistanciaTotalKm(){ return distanciaTotalKm; }

    public void agregarPunto(PuntoRecoleccion p){
        if(p != null) secuencia.add(p);
    }

    public void setDistanciaTotalKm(double km){
        if(km >= 0) this.distanciaTotalKm = km;
    }

    public boolean existe(){
        return secuencia.size() >= 2;
    }

    @Override
    public String toString(){
        if(!existe()) return "No existe ruta entre los puntos seleccionados.";
        StringBuilder sb = new StringBuilder("Ruta: ");
        for(int i = 0; i < secuencia.size(); i++){
            sb.append(secuencia.get(i).getNombre());
            if(i < secuencia.size() - 1) sb.append(" -> ");
        }
        sb.append(" | Distancia total: ").append(distanciaTotalKm).append(" km");
        return sb.toString();
    }
}

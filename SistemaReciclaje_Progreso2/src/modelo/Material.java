package modelo;

public class Material {
    private String tipoMaterial;
    private int puntosPorKg;

    public Material(String tipoMaterial){
        setTipoMaterial(tipoMaterial);
    }

    public String getTipoMaterial(){
        return tipoMaterial;
    }

    public static boolean esTipoValido(String tipo) {
        if (tipo == null || tipo.isBlank()) return false;
        return tipo.equalsIgnoreCase("PLASTICO")   ||
                tipo.equalsIgnoreCase("PLÁSTICO")   ||
                tipo.equalsIgnoreCase("VIDRIO")      ||
                tipo.equalsIgnoreCase("PAPEL")       ||
                tipo.equalsIgnoreCase("METAL")       ||
                tipo.equalsIgnoreCase("ELECTRONICO") ||
                tipo.equalsIgnoreCase("ELECTRÓNICO");
    }

    private void setTipoMaterial(String tipoMaterial) {
        if (tipoMaterial == null) {
            throw new IllegalArgumentException("Debe seleccionar un material.");
        }
        tipoMaterial = tipoMaterial.trim();
        if (tipoMaterial.equalsIgnoreCase("PLASTICO") ||
                tipoMaterial.equalsIgnoreCase("PLÁSTICO")) {
            this.tipoMaterial = "PLÁSTICO";
            this.puntosPorKg = 10;
        } else if (tipoMaterial.equalsIgnoreCase("VIDRIO")) {
            this.tipoMaterial = "VIDRIO";
            this.puntosPorKg = 8;
        } else if (tipoMaterial.equalsIgnoreCase("PAPEL")) {
            this.tipoMaterial = "PAPEL";
            this.puntosPorKg = 5;
        } else if (tipoMaterial.equalsIgnoreCase("METAL")) {
            this.tipoMaterial = "METAL";
            this.puntosPorKg = 15;
        } else if (tipoMaterial.equalsIgnoreCase("ELECTRONICO") ||
                tipoMaterial.equalsIgnoreCase("ELECTRÓNICO")) {
            this.tipoMaterial = "ELECTRÓNICO";
            this.puntosPorKg = 20;
        } else {
            throw new IllegalArgumentException("Material no válido. Use plástico, vidrio, papel, metal o electrónico.");
        }
    }

    public int getPuntosPorKg(){
        return puntosPorKg;
    }

    public double calcularPuntos(double kg){
        if(kg > 0){
            return kg*puntosPorKg;
        }else{
            return 0;
        }
    }

    @Override
    public String toString(){
        return "Tipo de material: "+tipoMaterial+"\nPuntos por kg: "+puntosPorKg;
    }
}

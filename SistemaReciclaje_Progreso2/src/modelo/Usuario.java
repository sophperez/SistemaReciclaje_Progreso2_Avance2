package modelo;

public abstract class Usuario {
    private String nombreCompleto, cedula, correo, telefono, direccion,  nombreUsuario, contrasena;

    public Usuario(String nombreCompleto, String cedula, String correo, String telefono, String direccion,
                   String nombreUsuario, String contrasena){
        this.nombreCompleto = nombreCompleto;
        this.cedula = cedula;
        this.nombreUsuario = nombreUsuario;
        setCorreo(correo);
        setTelefono(telefono);
        setDireccion(direccion);
        setContrasena(contrasena);
    }

    // ======= MÉTODOS ESTÁTICOS DE VALIDACIÓN =======

    public static boolean esNombreValido(String nombre){
        boolean valido = true;
        if(nombre == null || nombre.isBlank()){
            valido = false;
        } else {
            for(int i = 0; i < nombre.length(); i++){
                char c = nombre.charAt(i);
                if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
                        c == ' ' || c == 'á' || c == 'é' || c == 'í' ||
                        c == 'ó' || c == 'ú' || c == 'Á' || c == 'É' ||
                        c == 'Í' || c == 'Ó' || c == 'Ú' || c == 'ñ' ||
                        c == 'Ñ')){
                    valido = false;
                    break;
                }
            }
        }
        return valido;
    }

    public static boolean esCedulaValida(String cedula){
        // 1) 10 dígitos numéricos
        if(cedula == null || cedula.length() != 10){
            return false;
        }
        for(int i = 0; i < cedula.length(); i++){
            char c = cedula.charAt(i);
            if(!(c >= '0' && c <= '9')){
                return false;
            }
        }
        // 2) Código de provincia entre 01 y 24
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if(provincia < 1 || provincia > 24){
            return false;
        }
        // 3) Tercer dígito menor que 6 (persona natural)
        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if(tercerDigito >= 6){
            return false;
        }
        // 4) Dígito verificador (algoritmo Módulo 10 del Registro Civil del Ecuador)
        int[] coef = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        for(int i = 0; i < 9; i++){
            int prod = Character.getNumericValue(cedula.charAt(i)) * coef[i];
            if(prod >= 10) prod -= 9;
            suma += prod;
        }
        int verificador = (10 - (suma % 10)) % 10;
        int ultimoDigito = Character.getNumericValue(cedula.charAt(9));
        return verificador == ultimoDigito;
    }

    // Explica por qué la cédula no es válida, para que el usuario sepa qué corregir.
    public static String mensajeErrorCedula(String cedula){
        if(cedula == null || cedula.length() != 10){
            return "La cédula debe tener exactamente 10 dígitos.";
        }
        for(int i = 0; i < cedula.length(); i++){
            if(!(cedula.charAt(i) >= '0' && cedula.charAt(i) <= '9')){
                return "La cédula solo debe contener números.";
            }
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if(provincia < 1 || provincia > 24){
            return "Los dos primeros dígitos de la cédula deben estar entre 01 y 24.";
        }
        return "La cédula no es una cédula ecuatoriana válida.";
    }

    public static boolean esCorreoValido(String correo){
        boolean valido = true;
        if(correo == null || correo.isBlank()){
            valido = false;
        } else {
            boolean tieneArroba = false;
            boolean tienePunto = false;
            for(int i = 0; i < correo.length(); i++){
                if(correo.charAt(i) == '@') tieneArroba = true;
                if(correo.charAt(i) == '.') tienePunto = true;
            }
            if(!tieneArroba || !tienePunto){
                valido = false;
            }
        }
        return valido;
    }

    public static boolean esTelefonoValido(String telefono){
        boolean valido = true;
        if(telefono == null || telefono.isBlank()){
            valido = false;
        } else {
            for(int i = 0; i < telefono.length(); i++){
                char c = telefono.charAt(i);
                if(!(c >= '0' && c <= '9')){
                    valido = false;
                    break;
                }
            }
            if(telefono.length() != 10){
                valido = false;
            }
        }
        return valido;
    }

    public static boolean esDireccionValida(String direccion){
        boolean valido = true;
        if(direccion == null || direccion.isBlank()){
            valido = false;
        } else {
            for(int i = 0; i < direccion.length(); i++){
                char c = direccion.charAt(i);
                if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
                        (c >= '0' && c <= '9') || c == ' ' || c == '-' ||
                        c == '.' || c == 'á' || c == 'é' || c == 'í' ||
                        c == 'ó' || c == 'ú' || c == 'Á' || c == 'É' ||
                        c == 'Í' || c == 'Ó' || c == 'Ú' || c == 'ñ' ||
                        c == 'Ñ')){
                    valido = false;
                    break;
                }
            }
        }
        return valido;
    }

    public static boolean esNombreUsuarioValido(String nombreUsuario){
        boolean valido = true;
        if(nombreUsuario == null || nombreUsuario.isBlank()){
            valido = false;
        } else {
            for(int i = 0; i < nombreUsuario.length(); i++){
                char c = nombreUsuario.charAt(i);
                if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
                        (c >= '0' && c <= '9') || c == '_')){
                    valido = false;
                    break;
                }
            }
        }
        return valido;
    }

    public static boolean esContrasenaValida(String contrasena){
        return contrasena != null && !contrasena.isBlank() && contrasena.length() >= 4;
    }

    // ======= GETTERS =======

    public String getNombreCompleto(){ return nombreCompleto; }
    public String getCedula(){ return cedula; }
    public String getCorreo(){ return correo; }
    public String getTelefono(){ return telefono; }
    public String getDireccion(){ return direccion; }
    public String getNombreUsuario(){ return nombreUsuario; }
    public String getContrasena(){ return contrasena; }

    // ======= SETTERS — solo los que pueden cambiar =======

    public void setCorreo(String correo){
        if(esCorreoValido(correo)){
            this.correo = correo;
        }
    }

    public void setTelefono(String telefono){
        if(esTelefonoValido(telefono)){
            this.telefono = telefono;
        }
    }

    public void setDireccion(String direccion){
        if(esDireccionValida(direccion)){
            this.direccion = direccion;
        }
    }

    public void setContrasena(String contrasena){
        if(esContrasenaValida(contrasena)){
            this.contrasena = contrasena;
        }
    }

    public boolean iniciarSesion(String nombreUsuario, String contrasena){
        if(nombreUsuario == null || contrasena == null){
            return false;
        }
        return this.nombreUsuario.equals(nombreUsuario) &&
                this.contrasena.equals(contrasena);
    }

    @Override
    public String toString(){
        return "Nombre Completo: " + nombreCompleto +
                "\nCédula: " + cedula +
                "\nCorreo: " + correo +
                "\nTeléfono: " + telefono +
                "\nDirección: " + direccion +
                "\nNombre de usuario: " + nombreUsuario;
    }
}

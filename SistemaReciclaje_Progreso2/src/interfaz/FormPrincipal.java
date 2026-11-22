package interfaz;

import modelo.Ciudadano;
import modelo.Turno;
import negocio.GestorReciclaje;

import javax.swing.*;

/**
 * Ventana principal (FormPrincipal.form). Se abre después de iniciar sesión.
 * El administrador (gestor del punto limpio) ve todas las opciones; el ciudadano
 * solo ve su información y las acciones que le corresponden.
 */
public class FormPrincipal {
    private JPanel Ventana;
    private JTabbedPane tabs;
    private JLabel lblSesion;
    private JButton btnCerrarSesion;

    // Ciudadanos (Módulo 1)
    private JPanel panelCiudadanos;
    private JLabel lblTituloCiu;
    private JButton btnLimpiarCiu;
    private JTextField txtCedulaCiu;
    private JButton btnConsultarCiu;
    private JLabel lblOrdenCiu;
    private JComboBox<String> cmbOrdenCiu;
    private JButton btnOrdenarCiu;
    private JLabel lblFiltroCiu;
    private JComboBox<String> cmbFiltroCiu;
    private JButton btnFiltrarCiu;
    private JButton btnResetCiu;
    private JButton btnBajaCiu;
    private JTextArea txtAreaCiu;

    // Entregas (Módulo 2)
    private JPanel panelEntregas;
    private JButton btnLimpiarEnt;
    private JTextField txtCedulaEnt;
    private JButton btnTomarTurno;
    private JLabel lblTituloSiguiente;
    private JLabel lblSiguiente;
    private JButton btnVerFila;
    private JLabel lblPuntoEnt;
    private JComboBox<String> cmbPuntoEnt;
    private JButton btnAtenderEnt;
    private JLabel lblMaterialEnt;
    private JComboBox<String> cmbMaterialEnt;
    private JButton btnDeshacerEnt;
    private JLabel lblPesoEnt;
    private JTextField txtPesoEnt;
    private JButton btnReporteMat;
    private JComboBox<String> cmbFiltroMatEnt;
    private JButton btnHistorialEnt;
    private JButton btnImpactoEnt;
    private JTextArea txtAreaEnt;

    // Recompensas (Módulo 3)
    private JPanel panelRecompensas;
    private JButton btnLimpiarRec;
    private JTextField txtCedulaRec;
    private JButton btnAlcanzablesRec;
    private JLabel lblOrdenRec;
    private JComboBox<String> cmbOrdenRec;
    private JButton btnOrdenarRec;
    private JComboBox<String> cmbFiltroRec;
    private JButton btnFiltrarRec;
    private JLabel lblIdActRec;
    private JTextField txtIdActRec;
    private JLabel lblPuntosActRec;
    private JTextField txtPuntosActRec;
    private JLabel lblStockActRec;
    private JTextField txtStockActRec;
    private JButton btnActualizarRec;
    private JButton btnCatalogoRec;
    private JTextArea txtAreaRec;

    // Canjes y reportes (Módulo 4)
    private JPanel panelCanjes;
    private JButton btnLimpiarCanje;
    private JTextField txtCedulaCanje;
    private JButton btnHistorialCanje;
    private JTextField txtIdRec;
    private JButton btnSolicitarCanje;
    private JButton btnProcesarCanje;
    private JButton btnPendientesCanje;
    private JButton btnReporte;
    private JTextArea txtAreaCanje;

    // Rutas (Módulo 5)
    private JPanel panelRutas;
    private JButton btnLimpiarRutas;
    private JTextField txtNombrePunto;
    private JTextField txtSectorPunto;
    private JComboBox<String> cmbTipoPunto;
    private JButton btnRegistrarPunto;
    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextField txtDistancia;
    private JButton btnConectar;
    private JButton btnListarPuntos;
    private JButton btnRequieren;
    private JTextArea txtAreaRutas;

    private final JFrame frame;
    private final GestorReciclaje gestor;
    private final boolean esAdmin;

    // Acción de un botón que puede fallar; los errores se muestran en un mensaje.
    private interface Accion {
        void ejecutar() throws Exception;
    }

    public FormPrincipal(JFrame frame, String rol, GestorReciclaje gestor) {
        this.frame = frame;
        this.gestor = gestor;
        this.esAdmin = rol.equals("ADMINISTRADOR");

        llenarCombos();
        for (JTextArea a : new JTextArea[]{txtAreaCiu, txtAreaEnt, txtAreaRec, txtAreaCanje, txtAreaRutas}) {
            a.setEditable(false);
            a.setLineWrap(true);
            a.setWrapStyleWord(true);
        }

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        configurarCiudadanos();
        configurarEntregas();
        configurarRecompensas();
        configurarCanjes();
        configurarRutas();

        if (esAdmin) {
            prepararVistaAdministrador();
        } else {
            prepararVistaCiudadano();
        }
        actualizarSesion();
    }

    public JPanel getPanel() { return Ventana; }

    private void llenarCombos() {
        for (String s : new String[]{"Cédula", "Puntos", "Nombre", "Nivel"}) cmbOrdenCiu.addItem(s);
        for (String s : new String[]{"BRONCE", "PLATA", "ORO", "PLATINO", "ACTIVO", "INACTIVO"}) cmbFiltroCiu.addItem(s);
        for (String s : new String[]{"PLASTICO", "PAPEL", "VIDRIO", "METAL", "ELECTRONICO"}) cmbMaterialEnt.addItem(s);
        cmbFiltroMatEnt.addItem("TODOS");
        for (String s : new String[]{"PLASTICO", "PAPEL", "VIDRIO", "METAL", "ELECTRONICO"}) cmbFiltroMatEnt.addItem(s);
        for (String p : gestor.nombresPuntosLimpios()) cmbPuntoEnt.addItem(p);
        for (String s : new String[]{"Puntos", "Nombre", "Stock"}) cmbOrdenRec.addItem(s);
        for (String s : new String[]{"DISPONIBLES", "PRODUCTO", "DESCUENTO", "EXPERIENCIA"}) cmbFiltroRec.addItem(s);
        cmbTipoPunto.addItem("PUNTO_LIMPIO");
        cmbTipoPunto.addItem("CENTRO_ACOPIO");
    }

    // ---------------- ROLES ----------------

    private void prepararVistaAdministrador() {
        actualizarSiguiente();
        txtAreaEnt.setText(gestor.verFila());
        txtAreaRutas.setText(gestor.listarPuntos());
    }

    private void prepararVistaCiudadano() {
        String cedula = gestor.getCiudadanoActivo().getCedula();
        for (JTextField t : new JTextField[]{txtCedulaCiu, txtCedulaEnt, txtCedulaRec, txtCedulaCanje}) {
            t.setText(cedula);
            t.setEditable(false);
        }
        // Módulo 1: solo su perfil
        tabs.setTitleAt(tabs.indexOfComponent(panelCiudadanos), "Mi perfil");
        lblTituloCiu.setText("MI PERFIL");
        ocultar(lblOrdenCiu, cmbOrdenCiu, btnOrdenarCiu, lblFiltroCiu, cmbFiltroCiu, btnFiltrarCiu,
                btnResetCiu, btnBajaCiu, btnLimpiarCiu);
        // Módulo 2: toma su turno y consulta sus entregas; la atención la hace el gestor
        ocultar(lblTituloSiguiente, lblSiguiente, lblPuntoEnt, cmbPuntoEnt, btnAtenderEnt,
                lblMaterialEnt, cmbMaterialEnt, btnDeshacerEnt, lblPesoEnt, txtPesoEnt, btnReporteMat);
        btnVerFila.setText("Ver mi turno");
        // Módulo 3: ve las recompensas sin el stock; ordenar y actualizar son del administrador
        btnAlcanzablesRec.setText("¿Qué puedo canjear?");
        ocultar(lblOrdenRec, cmbOrdenRec, btnOrdenarRec, lblIdActRec, txtIdActRec, lblPuntosActRec,
                txtPuntosActRec, lblStockActRec, txtStockActRec, btnActualizarRec);
        // Módulo 4: solicita y consulta; procesar y reportes son del administrador
        ocultar(btnProcesarCanje, btnReporte);
        btnPendientesCanje.setText("Mis solicitudes");
        // Módulo 5: solo administrador
        tabs.remove(panelRutas);

        mostrar(txtAreaCiu, () -> gestor.consultarPerfil(cedula));
    }

    private void ocultar(JComponent... componentes) {
        for (JComponent c : componentes) c.setVisible(false);
    }

    private void actualizarSesion() {
        if (esAdmin) {
            lblSesion.setText("Sesión: " + gestor.getNombreAdministrador() + " (Administrador)");
        } else {
            Ciudadano c = gestor.getCiudadanoActivo();
            lblSesion.setText("Sesión: " + c.getNombreCompleto() + " (Ciudadano) - " +
                    c.getPuntos() + " pts - " + c.getNivel());
        }
    }

    private void actualizarSiguiente() {
        Turno t = gestor.verSiguienteEnFila();
        lblSiguiente.setText(t == null ? "Nadie en la fila" : "Turno " + t.getCodigo() + " - " +
                t.getCiudadano().getNombreCompleto());
    }

    // Borra lo escrito en los campos y el área de resultados.
    // En la vista del ciudadano la cédula no se borra porque es la suya.
    private void limpiar(JTextArea area, JTextField... campos) {
        for (JTextField c : campos) {
            if (c.isEditable()) c.setText("");
        }
        area.setText("");
    }

    // ---------------- UTILIDADES ----------------

    private void ejecutar(Accion accion) {
        try {
            accion.ejecutar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Ventana, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        actualizarSesion();
    }

    private interface Texto {
        String obtener() throws Exception;
    }

    private void mostrar(JTextArea area, Texto texto) {
        ejecutar(() -> {
            area.setText(texto.obtener());
            area.setCaretPosition(0);
        });
    }

    private void avisar(String mensaje) {
        JOptionPane.showMessageDialog(Ventana, mensaje);
    }

    private boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(Ventana, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private String cedula(JTextField campo) throws Exception {
        String c = campo.getText().trim();
        if (c.isEmpty()) throw new Exception("Ingrese la cédula.");
        return c;
    }

    private double leerDecimal(JTextField campo, String nombre) throws Exception {
        try {
            return Double.parseDouble(campo.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new Exception(nombre + " debe ser un número.");
        }
    }

    private int leerEntero(JTextField campo, String nombre) throws Exception {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException e) {
            throw new Exception(nombre + " debe ser un número entero.");
        }
    }

    private void cerrarSesion() {
        if (confirmar("¿Desea cerrar sesión?")) {
            gestor.cerrarSesion();
            frame.setContentPane(new FormLogin(frame, gestor).getPanel());
            frame.pack();
            frame.setSize(460, 360);
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
        }
    }

    // ---------------- MÓDULO 1: CIUDADANOS ----------------

    private void configurarCiudadanos() {
        btnConsultarCiu.addActionListener(e ->
                mostrar(txtAreaCiu, () -> gestor.consultarPerfil(cedula(txtCedulaCiu))));
        btnLimpiarCiu.addActionListener(e -> limpiar(txtAreaCiu, txtCedulaCiu));
        btnOrdenarCiu.addActionListener(e -> mostrar(txtAreaCiu,
                () -> gestor.listarCiudadanosOrdenados((String) cmbOrdenCiu.getSelectedItem())));
        btnFiltrarCiu.addActionListener(e -> mostrar(txtAreaCiu,
                () -> gestor.filtrarCiudadanos((String) cmbFiltroCiu.getSelectedItem())));
        btnBajaCiu.addActionListener(e -> ejecutar(() -> {
            String c = cedula(txtCedulaCiu);
            if (confirmar("¿Dar de baja al ciudadano con cédula " + c + "?")) {
                avisar(gestor.darDeBaja(c));
                txtAreaCiu.setText(gestor.consultarPerfil(c));
            }
        }));
        btnResetCiu.addActionListener(e -> ejecutar(() -> {
            String c = cedula(txtCedulaCiu);
            if (confirmar("¿Restablecer la contraseña del ciudadano con cédula " + c + "?")) {
                avisar(gestor.resetearContrasena(c));
            }
        }));
    }

    // ---------------- MÓDULO 2: ENTREGAS ----------------

    private void configurarEntregas() {
        btnLimpiarEnt.addActionListener(e -> limpiar(txtAreaEnt, txtCedulaEnt, txtPesoEnt));
        btnTomarTurno.addActionListener(e -> ejecutar(() -> {
            avisar(gestor.tomarTurno(cedula(txtCedulaEnt)));
            if (esAdmin) {
                txtAreaEnt.setText(gestor.verFila());
                actualizarSiguiente();
            } else {
                txtAreaEnt.setText(gestor.consultarMiTurno(cedula(txtCedulaEnt)));
            }
        }));
        btnVerFila.addActionListener(e -> {
            if (esAdmin) {
                mostrar(txtAreaEnt, gestor::verFila);
                actualizarSiguiente();
            } else {
                mostrar(txtAreaEnt, () -> gestor.consultarMiTurno(cedula(txtCedulaEnt)));
            }
        });
        btnAtenderEnt.addActionListener(e -> ejecutar(this::atenderSiguiente));
        btnDeshacerEnt.addActionListener(e -> ejecutar(() -> {
            if (confirmar("¿Deshacer la última entrega registrada?")) {
                avisar(gestor.deshacerUltimaEntrega());
            }
        }));
        btnHistorialEnt.addActionListener(e -> mostrar(txtAreaEnt, () -> gestor.consultarHistorialEntregas(
                cedula(txtCedulaEnt), (String) cmbFiltroMatEnt.getSelectedItem())));
        btnImpactoEnt.addActionListener(e ->
                mostrar(txtAreaEnt, () -> gestor.calcularImpactoAmbiental(cedula(txtCedulaEnt))));
        btnReporteMat.addActionListener(e -> mostrar(txtAreaEnt, gestor::reporteMaterialPorTipo));
    }

    // Atiende al primero de la fila: muestra los puntos y pide confirmación antes de registrar.
    private void atenderSiguiente() throws Exception {
        Turno turno = gestor.verSiguienteEnFila();
        if (turno == null) throw new Exception("No hay ciudadanos en la fila.");
        String material = (String) cmbMaterialEnt.getSelectedItem();
        String punto = (String) cmbPuntoEnt.getSelectedItem();
        double peso = leerDecimal(txtPesoEnt, "El peso");
        int puntos = gestor.calcularPuntos(material, peso);
        String resumen = "Turno " + turno.getCodigo() + ": " + turno.getCiudadano().getNombreCompleto() +
                "\nPunto limpio: " + punto + "\nMaterial: " + material +
                "\nPeso: " + peso + " kg\nPuntos a sumar: " + puntos + "\n\n¿Confirmar la entrega?";
        if (confirmar(resumen)) {
            avisar(gestor.atenderSiguiente(material, peso, punto));
            txtPesoEnt.setText("");
            txtAreaEnt.setText(gestor.verFila());
            actualizarSiguiente();
        }
    }

    // ---------------- MÓDULO 3: RECOMPENSAS ----------------

    private void configurarRecompensas() {
        btnLimpiarRec.addActionListener(e ->
                limpiar(txtAreaRec, txtCedulaRec, txtIdActRec, txtPuntosActRec, txtStockActRec));
        btnCatalogoRec.addActionListener(e -> mostrar(txtAreaRec, () -> gestor.consultarCatalogo(esAdmin)));
        btnOrdenarRec.addActionListener(e -> mostrar(txtAreaRec,
                () -> gestor.consultarCatalogoOrdenado((String) cmbOrdenRec.getSelectedItem())));
        btnFiltrarRec.addActionListener(e -> mostrar(txtAreaRec,
                () -> gestor.filtrarCatalogo((String) cmbFiltroRec.getSelectedItem(), esAdmin)));
        btnAlcanzablesRec.addActionListener(e ->
                mostrar(txtAreaRec, () -> gestor.verRecompensasAlcanzables(cedula(txtCedulaRec), esAdmin)));
        btnActualizarRec.addActionListener(e -> ejecutar(() -> {
            int id = leerEntero(txtIdActRec, "El ID de la recompensa");
            int puntos = leerEntero(txtPuntosActRec, "Los puntos");
            int stock = leerEntero(txtStockActRec, "El stock");
            avisar(gestor.actualizarRecompensa(id, puntos, stock));
            txtIdActRec.setText("");
            txtPuntosActRec.setText("");
            txtStockActRec.setText("");
            txtAreaRec.setText(gestor.consultarCatalogo(true));
        }));
    }

    // ---------------- MÓDULO 4: CANJES Y REPORTES ----------------

    private void configurarCanjes() {
        btnLimpiarCanje.addActionListener(e -> limpiar(txtAreaCanje, txtCedulaCanje, txtIdRec));
        btnSolicitarCanje.addActionListener(e -> ejecutar(() -> {
            String c = cedula(txtCedulaCanje);
            int id = leerEntero(txtIdRec, "El ID de la recompensa");
            avisar(gestor.solicitarCanje(c, id));
            txtIdRec.setText("");
            txtAreaCanje.setText(esAdmin ? gestor.consultarPendientes() : gestor.consultarMisSolicitudes(c));
        }));
        btnProcesarCanje.addActionListener(e -> ejecutar(() -> {
            avisar(gestor.procesarSiguienteCanje());
            txtAreaCanje.setText(gestor.consultarPendientes());
        }));
        btnPendientesCanje.addActionListener(e -> {
            if (esAdmin) {
                mostrar(txtAreaCanje, gestor::consultarPendientes);
            } else {
                mostrar(txtAreaCanje, () -> gestor.consultarMisSolicitudes(cedula(txtCedulaCanje)));
            }
        });
        btnHistorialCanje.addActionListener(e ->
                mostrar(txtAreaCanje, () -> gestor.consultarHistorialCanjes(cedula(txtCedulaCanje))));
        btnReporte.addActionListener(e -> mostrar(txtAreaCanje, gestor::generarReporte));
    }

    // ---------------- MÓDULO 5: RUTAS ----------------

    private void configurarRutas() {
        btnLimpiarRutas.addActionListener(e -> limpiar(txtAreaRutas, txtNombrePunto, txtSectorPunto,
                txtOrigen, txtDestino, txtDistancia));
        btnRegistrarPunto.addActionListener(e -> ejecutar(() -> {
            String tipo = (String) cmbTipoPunto.getSelectedItem();
            avisar(gestor.registrarPunto(txtNombrePunto.getText().trim(),
                    txtSectorPunto.getText().trim(), tipo));
            if ("PUNTO_LIMPIO".equals(tipo)) cmbPuntoEnt.addItem(txtNombrePunto.getText().trim());
            txtNombrePunto.setText("");
            txtSectorPunto.setText("");
            txtAreaRutas.setText(gestor.listarPuntos());
        }));
        btnConectar.addActionListener(e -> ejecutar(() -> {
            double distancia = leerDecimal(txtDistancia, "La distancia");
            avisar(gestor.conectarPuntos(txtOrigen.getText().trim(), txtDestino.getText().trim(), distancia));
            txtDistancia.setText("");
            txtAreaRutas.setText(gestor.listarPuntos());
        }));
        btnListarPuntos.addActionListener(e -> mostrar(txtAreaRutas, gestor::listarPuntos));
        btnRequieren.addActionListener(e -> mostrar(txtAreaRutas, gestor::puntosQueRequierenRecoleccion));
    }
}

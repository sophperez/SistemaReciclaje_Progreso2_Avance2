package interfaz;

import negocio.GestorReciclaje;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pantalla de inicio de sesión (IntelliJ GUI Designer: FormLogin.form).
 * Aquí está el main: el programa se ejecuta desde esta clase.
 * Al iniciar sesión se cambia el contenido de la ventana a FormPrincipal.
 */
public class FormLogin {
    private JPanel Ventana;
    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnLogin;
    private JButton btnRegistrarse;
    private JButton btnOlvide;

    private final JFrame frame;
    private final GestorReciclaje gestor;

    public FormLogin(JFrame frame, GestorReciclaje gestor) {
        this.frame = frame;
        this.gestor = gestor;

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { iniciarSesion(); }
        });
        btnRegistrarse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new FormRegistro(frame, gestor).getPanel());
                frame.revalidate();
                frame.repaint();
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
        });
        btnOlvide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { olvide(); }
        });
    }

    public JPanel getPanel() { return Ventana; }

    private void iniciarSesion() {
        String u = txtUsuario.getText().trim();
        String c = new String(txtClave.getPassword());
        if (u.isEmpty() || c.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese usuario y contraseña.");
            return;
        }
        String rol = gestor.iniciarSesionCiudadano(u, c);
        if (rol == null) rol = gestor.iniciarSesionAdmin(u, c);
        if (rol == null) {
            if (gestor.ciudadanoInactivo(u)) {
                JOptionPane.showMessageDialog(null, "Su cuenta está inactiva.");
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        FormPrincipal principal = new FormPrincipal(frame, rol, gestor);
        frame.setContentPane(principal.getPanel());
        frame.setSize(760, 680);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    // Regla 7 del módulo 1: solo el administrador puede restablecer contraseñas.
    private void olvide() {
        JOptionPane.showMessageDialog(null,
                "Para restablecer su contraseña, acérquese al administrador del punto limpio.\n" +
                "Su nueva contraseña será su número de cédula.",
                "Olvidé mi contraseña", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Reciclaje y Recompensas");
        GestorReciclaje gestor = new GestorReciclaje();
        FormLogin login = new FormLogin(frame, gestor);
        frame.setContentPane(login.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(460, 360);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

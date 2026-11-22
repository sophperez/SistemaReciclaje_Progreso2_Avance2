package interfaz;

import negocio.GestorReciclaje;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pantalla de registro de un nuevo ciudadano (FormRegistro.form).
 * Al registrar correctamente, vuelve a la pantalla de login.
 */
public class FormRegistro {
    private JPanel Ventana;
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtUsuarioReg;
    private JPasswordField txtClaveReg;
    private JButton btnRegistrar;
    private JButton btnVolver;

    private final JFrame frame;
    private final GestorReciclaje gestor;

    public FormRegistro(JFrame frame, GestorReciclaje gestor) {
        this.frame = frame;
        this.gestor = gestor;

        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { registrar(); }
        });
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { volverLogin(); }
        });
    }

    public JPanel getPanel() { return Ventana; }

    private void registrar() {
        try {
            String r = gestor.registrarCiudadano(
                    txtNombre.getText().trim(),
                    txtCedula.getText().trim(),
                    txtCorreo.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtDireccion.getText().trim(),
                    txtUsuarioReg.getText().trim(),
                    new String(txtClaveReg.getPassword()));
            JOptionPane.showMessageDialog(null, r);
            volverLogin();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverLogin() {
        frame.setContentPane(new FormLogin(frame, gestor).getPanel());
        frame.pack();
        frame.setSize(460, 360);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }
}

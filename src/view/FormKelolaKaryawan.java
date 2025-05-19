package view;

import Form.FormTambahKaryawan;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import main.Koneksi;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;


public class FormKelolaKaryawan extends javax.swing.JPanel {

    
    public FormKelolaKaryawan() {
        initComponents();
        
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "RFID", "Nama", "Username", "Password", "Role", "Jenis Kelamin", "No Telepon", "Alamat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Semua sel tidak dapat diedit langsung
            }
        };
        table.setModel(model);
        
        
        
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
                label.setOpaque(true);
                label.setBackground(new Color(0, 123, 255));
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 0, 10, 0)));
                return label;
            }
        });
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);

        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(true);
        
        
        
        
        loadData();
        
        
        
        
        // Style tombol
        btn_tambah.setText("TAMBAH");
        btn_tambah.setBackground(new java.awt.Color(70, 130, 180)); // warna biru steel blue
        btn_tambah.setForeground(Color.WHITE);
        btn_tambah.setFont(new java.awt.Font("Serif", Font.BOLD, 12));
        btn_tambah.setFocusPainted(false);
        btn_tambah.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn_tambah.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn_tambah.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_tambah.setBackground(new java.awt.Color(100, 149, 237)); // Cornflower Blue
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_tambah.setBackground(new java.awt.Color(70, 130, 180));
            }
        });
        
        // Style tombol
        btn_edit.setText("EDIT");
        btn_edit.setBackground(new java.awt.Color(70, 130, 180)); // warna biru steel blue
        btn_edit.setForeground(Color.WHITE);
        btn_edit.setFont(new java.awt.Font("Serif", Font.BOLD, 12));
        btn_edit.setFocusPainted(false);
        btn_edit.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn_edit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn_edit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_edit.setBackground(new java.awt.Color(100, 149, 237)); // Cornflower Blue
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_edit.setBackground(new java.awt.Color(70, 130, 180));
            }
        });
        
        // Style tombol
        btn_hapus.setText("HAPUS");
        btn_hapus.setBackground(new java.awt.Color(70, 130, 180)); // warna biru steel blue
        btn_hapus.setForeground(Color.WHITE);
        btn_hapus.setFont(new java.awt.Font("Serif", Font.BOLD, 12));
        btn_hapus.setFocusPainted(false);
        btn_hapus.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn_hapus.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn_hapus.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_hapus.setBackground(new java.awt.Color(100, 149, 237)); // Cornflower Blue
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_hapus.setBackground(new java.awt.Color(70, 130, 180));
            }
        });
    }
    
    private void loadData() {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0); // clear tabel

    try {
        Connection conn = Koneksi.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM users");

        while (rs.next()) {
            Object[] row = new Object[] {
                rs.getString("Id_user"),
                rs.getString("RFID"),
                rs.getString("Nama"),
                rs.getString("Username"),
                rs.getString("Password"),
                rs.getString("Role"),
                rs.getString("Jenis_kelamin"),
                rs.getString("No_Telepon"),
                rs.getString("Alamat")
            };
            model.addRow(row);
        }

        rs.close();
        st.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
    }
    
    
}

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new main.gradasiwarna();
        jLabel1 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 255));

        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Menu Karywan");

        btn_tambah.setText("TAMBAH");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_edit.setText("EDIT");
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_hapus.setText("HAPUS");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table);

        jLabel3.setBackground(new java.awt.Color(0, 51, 255));
        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("08 - 06 - 2025");

        jLabel5.setBackground(new java.awt.Color(0, 51, 255));
        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("User :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tambah)
                    .addComponent(btn_edit)
                    .addComponent(btn_hapus))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
    FormTambahKaryawan form = new FormTambahKaryawan((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true);
    form.setLocationRelativeTo(this);
    form.setVisible(true);

    
    
     loadData(); // Refresh tabel
    
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_hapusActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}

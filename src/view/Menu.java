/*Saya [Shidiq Arifin Sudrajat] mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
*/
package view;

import model.Database;
import model.Player;
import viewmodel.SoundPlayer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu extends JFrame {
    // Atribut
    private int selectedIndex = -1;
    private Database database;
    private Player player;
    // Atribut komponen komponen pada menu
    private JPanel mainPanel;
    private JTextField usernameField;
    private JTable playerTable;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JButton playButton;
    private JButton quitButton;
    private JScrollPane tableScroll;
    private JFrame menuFrame;
    private JLayeredPane layeredPane;

    // constructor
    public Menu(JFrame menuFrame) {
        this.menuFrame = menuFrame;
        // Buat objek database
        database = new Database();

        // Set up the layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1280, 720));

        ImageIcon backgrounImage;
        backgrounImage = new ImageIcon(getClass().getResource("/assets/background.png"));
        // Load and set the background image
        JLabel background = new JLabel(backgrounImage);
        background.setBounds(0, 0, 1280, 720);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // Initialize components
        titleLabel = new JLabel("UP DOWN");
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        playerTable = new JTable();
        playButton = new JButton("Play");
        quitButton = new JButton("Quit");
        tableScroll = new JScrollPane(playerTable);

        // Set component bounds (absolute positioning)
        titleLabel.setBounds(520, 130, 360, 50); // Title "UP DOWN" in the center top
        usernameLabel.setBounds(540, 200, 200, 25); // Username label below the title
        usernameField.setBounds(540, 230, 200, 25); // Username text field below the username label
        tableScroll.setBounds(340, 280, 600, 200); // Table centered below the username input
        playButton.setBounds(340, 500, 200, 50); // Play button below the table on the left
        quitButton.setBounds(740, 500, 200, 50); // Quit button below the table on the right
        // Customize components
        Font japaneseFontTitle = null;
        Font japaneseFont;
        try {
            File fontStyle = new File("assets/go3v2.ttf");
            japaneseFontTitle = Font.createFont(Font.TRUETYPE_FONT, fontStyle).deriveFont(60f);
            japaneseFont = Font.createFont(Font.TRUETYPE_FONT, fontStyle).deriveFont(20f);
        } catch (Exception e) {
            System.err.println("Font loading failed: " + e.getMessage());
            japaneseFont = new Font("Arial", Font.BOLD, 20); // Fallback font
        }

        // Mengubah font title
        titleLabel.setForeground(Color.white);
        titleLabel.setFont(japaneseFontTitle);

        // Mengubah font dan warna label username
        usernameLabel.setFont(japaneseFont);
        usernameLabel.setForeground(Color.WHITE);

        // Mengubah font dan warna tombol Play
        playButton.setFont(japaneseFont);
        playButton.setBackground(Color.GREEN);
        playButton.setForeground(Color.WHITE);

        // Mengubah font dan warna tombol Quit
        quitButton.setFont(japaneseFont);
        quitButton.setBackground(Color.RED);
        quitButton.setForeground(Color.WHITE);

        // Add components to the layered pane
        layeredPane.add(titleLabel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(usernameLabel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(usernameField, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(tableScroll, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(playButton, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(quitButton, JLayeredPane.PALETTE_LAYER);

        // Set up the main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(layeredPane, BorderLayout.CENTER);

        // Isi tabel player
        playerTable.setModel(setTable());

        tableScroll.setPreferredSize(new Dimension(playerTable.getPreferredSize().width, playerTable.getRowHeight() * 5));

        // Saat tombol play ditekan
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(Menu.this, "Please enter your username or select username from the table.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (selectedIndex == -1) {
                        if (insertData()) {
                            Player player = new Player(username);
                            UpDown game = new UpDown(player);
                            JFrame gameFrame = new JFrame();
                            gameFrame.add(game);
                            gameFrame.setTitle("UP DOWN");
                            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            gameFrame.pack();
                            gameFrame.setLocationRelativeTo(null);
                            gameFrame.setResizable(false);
                            gameFrame.setVisible(true);
                            menuFrame.dispose();

                            // Stop main menu music and start game music
                            SoundPlayer.stopBackgroundMusic();
                            SoundPlayer.playBackgroundMusic("assets/playing_sound.wav");
                        }
                    } else {
                        Player player = new Player(username);
                        UpDown game = new UpDown(player);
                        JFrame gameFrame = new JFrame();
                        gameFrame.add(game);
                        gameFrame.setTitle("UP DOWN");
                        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        gameFrame.pack();
                        gameFrame.setLocationRelativeTo(null);
                        gameFrame.setResizable(false);
                        gameFrame.setVisible(true);
                        menuFrame.dispose();

                        // Stop main menu music and start game music
                        SoundPlayer.stopBackgroundMusic();
                        SoundPlayer.playBackgroundMusic("assets/playing_sound.wav");
                    }
                }
            }
        });

        // Saat tombol quit ditekan
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program
            }
        });

        // Saat salah satu baris tabel ditekan
        playerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = playerTable.getSelectedRow();

                // Simpan value textfield
                String selectedUsername = playerTable.getModel().getValueAt(selectedIndex, 0).toString();  // index 0 for "Username"

                // Ubah isi textfield
                usernameField.setText(selectedUsername);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"Username", "Score", "Up", "Down"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        // isi tabel dengan database
        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM tscore");

            while (resultSet.next()) {
                Object[] row = new Object[4];  // Only 4 columns

                row[0] = resultSet.getString("username");
                row[1] = resultSet.getString("score");
                row[2] = resultSet.getString("up");
                row[3] = resultSet.getString("down");

                temp.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return temp;
    }

    public boolean insertData() {
        String username = usernameField.getText().trim();

        // Jika field username kosong
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon isi username Anda!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Cek apakah username sudah ada di database
        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM tscore WHERE username = '" + username + "'");
            if (resultSet.next()) {
                // Username sudah ada dalam database
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Lanjutkan dengan insert data jika username tersedia
        int score = 0;
        int up = 0;
        int down = 0;

        String sql = "INSERT INTO tscore (username, score, up, down) VALUES ('" + username + "', " + score + ", " + up + ", " + down + ");";
        database.insertUpdateDeleteQuery(sql);

        // Set ulang model tabel setelah insert
        playerTable.setModel(setTable());
        clearForm();
        return true;
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        usernameField.setText("");
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(15, 9, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 12, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(50, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 8, 12, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(50, -1), null, 0, false));
        titleLabel = new JLabel();
        titleLabel.setText("UP DOWN");
        mainPanel.add(titleLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 50), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(14, 3, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 50), null, 0, false));
        playButton = new JButton();
        playButton.setText("Play");
        mainPanel.add(playButton, new com.intellij.uiDesigner.core.GridConstraints(13, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quitButton = new JButton();
        quitButton.setText("Quit");
        mainPanel.add(quitButton, new com.intellij.uiDesigner.core.GridConstraints(13, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameField = new JTextField();
        usernameField.setText("");
        mainPanel.add(usernameField, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        mainPanel.add(usernameLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tableScroll = new JScrollPane();
        mainPanel.add(tableScroll, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 7, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        playerTable = new JTable();
        tableScroll.setViewportView(playerTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}

/*Saya [Shidiq Arifin Sudrajat] mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
*/
package view;

import model.Player;
import viewmodel.*;
import javax.swing.*;
import java.awt.*;

public class UpDown extends JPanel{
    private Game game; // Atribut game
    private Input input; // Atribut input
    private Player player; // Atribut pemain
    private int frameWidth = 1280; // Atribut lebar frame
    private int frameHeight = 720; // Atribut tinggi frame

    // Atribut gambar
    private Image backgroundImage; // Gambar background
    private Image ninjaImage; // Gambar ninja
    private Image houseImage; // Gambar rumah (house)
    private Image laternImage; // Gambar lentera (latern)

    public UpDown(Player players) {
        this.player = players;
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("/assets/background.png")).getImage();//using background image for background
        ninjaImage = new ImageIcon(getClass().getResource("/assets/ninja.gif")).getImage();//using ninja image for ninja
        houseImage = new ImageIcon(getClass().getResource("/assets/house.png")).getImage(); // using house image for house
        laternImage = new ImageIcon(getClass().getResource("/assets/latern.png")).getImage(); // using latern image for latern

        game = new Game(this);
        input = new Input(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.draw(g);
    }
    //Getter-Setter
    public Game getGame() {
        return game;
    }
    public Player getPlayer() {
        return player;
    }
    public int getFrameWidth() {
        return frameWidth;
    }
    public int getFrameHeight() {
        return frameHeight;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Image getNinjaImage() {
        return ninjaImage;
    }

    public void setNinjaImage(Image ninjaImage) {
        this.ninjaImage = ninjaImage;
    }

    public Image getHouseImage() {
        return houseImage;
    }

    public void setHouseImage(Image houseImage) {
        this.houseImage = houseImage;
    }

    public Image getLaternImage() {
        return laternImage;
    }

    public void setLaternImage(Image laternImage) {
        this.laternImage = laternImage;
    }
}

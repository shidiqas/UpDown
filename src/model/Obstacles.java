/*Saya [Shidiq Arifin Sudrajat] mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
*/
package model;

import java.awt.*;

public class Obstacles { // Kelas Obstacles
    private int posX; // Atribut posisi X rintangan
    private int posY; // Atribut posisi Y rintangan
    private int width; // Atribut lebar rintangan
    private int height; // Atribut tinggi rintangan
    private Image image; // Atribut gambar rintangan
    private int velocityX; // Atribut kecepatan horizontal rintangan
    private boolean landed; // Atribut apakah rintangan sudah mendarat
    private boolean initialPath; // Atribut apakah rintangan berada di jalur awal

    // Konstruktor Obstacles dengan parameter posisi, ukuran, dan gambar
    public Obstacles(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        this.velocityX = -2;
        this.landed = false;
        this.initialPath = false;
    }
    //Getter-Setter
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public boolean isLanded() {
        return landed;
    }

    public void setLanded(boolean landed) {
        this.landed = landed;
    }

    public boolean isInitialPath() {
        return initialPath;
    }

    public void setInitialPath(boolean initialPath) {
        this.initialPath = initialPath;
    }
}

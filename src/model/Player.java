/*Saya [Shidiq Arifin Sudrajat] mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
*/
package model;

import java.awt.*;

public class Player {
    // Deklarasi atribut private untuk kelas Player
    private String username; // Atribut username pemain
    private int posX; // Atribut posisi X pemain
    private int posY; // Atribut posisi Y pemain
    private int width; // Atribut lebar pemain
    private int height; // Atribut tinggi pemain
    private Image image; // Atribut gambar pemain
    private int velocityY; // Atribut kecepatan vertikal pemain
    private int velocityX; // Atribut kecepatan horizontal pemain
    private int score; // Atribut skor pemain
    private int up; // Atribut jumlah skor atas
    private int down; // Atribut jumlah skor bawah

    // Konstruktor Player dengan parameter posisi, ukuran, dan gambar
    public Player(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        this.score = 0;
        this.up = 0;
        this.down = 0;
        this.velocityY = -0;
        this.velocityX = 0;
    }

    // Konstruktor Player dengan parameter username
    public Player(String username)
    {
        this.username = username;

        this.posX = 0;
        this.posY = 0;
        this.width = 0;
        this.height = 0;
        this.image = null;
        this.score = 0;
        this.up = 0;
        this.down = 0;
        this.velocityY = -0;
        this.velocityX = 0;
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

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

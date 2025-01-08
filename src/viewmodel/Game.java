/*Saya [Shidiq Arifin Sudrajat] mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
*/
package viewmodel;

import view.UpDown;
import viewmodel.*;
import model.Database;
import model.Obstacles;
import model.Player;
import view.Menu;
import viewmodel.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Game implements ActionListener{
    private UpDown upDown;
    private int frameWidht = 1280;
    private int frameHeight = 720;

    //player
    int playerStartPosX = frameWidht / 8;
    int playerStartPosY = frameHeight * 3 / 4 - 70;
    int playerWidht = 57;
    int playerHeight = 79;
    Player player;

    //obstacles (formerly pipes) attributes
    int obstacleStartPosX = frameWidht;
    int obstacleStartPosY = 0;
    int houseWidth = 128; // width of the house obstacle
    int houseHeight = 360; // height of the house obstacle
    int laternWidth = 128; // width of the latern obstacle
    int laternHeight = 156; // height of the latern obstacle
    ArrayList<Obstacles> obstacles;

    //game logic
    Timer gameLoop;
    Timer obstaclesCooldown;
    int gravity = 1;

    //score
    int upScore = 0;
    int downScore = 0;
    int totalScore = 0;

    // boolean to track game over status
    private boolean gameOver = false;
    private boolean messageGameOver = false;
    //database
    Database database;
    //font
    Font japaneseFont;

    //constructor
    public Game(UpDown upDown) {
        this.upDown = upDown;
        this.player = upDown.getPlayer();

        database = new Database();

        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setWidth(playerWidht);
        player.setHeight(playerHeight);
        player.setImage(upDown.getNinjaImage());

        obstacles = new ArrayList<Obstacles>();

        createInitialPath(); // Initialize the initial path

        obstaclesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeObstacles();
            }
        });
        obstaclesCooldown.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        try {
            File fontStyle = new File("assets/go3v2.ttf");
            japaneseFont = Font.createFont(Font.TRUETYPE_FONT, fontStyle).deriveFont(20f);
        } catch (Exception e) {
            System.err.println("Font loading failed: " + e.getMessage());
            japaneseFont = (new Font("Arial", Font.BOLD, 20)); // Fallback font
        }
    }


    public void draw(Graphics g) {
        g.drawImage(upDown.getBackgroundImage(), 0, 0, frameWidht, frameHeight, null);
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacles obstacle = obstacles.get(i);
            g.drawImage(obstacle.getImage(), obstacle.getPosX(), obstacle.getPosY(), obstacle.getWidth(), obstacle.getHeight(), null);
        }

        // Draw a semi-transparent gray box behind the scores
        g.setColor(new Color(128, 128, 128, 128)); // RGBA for semi-transparent gray
        int boxWidth = 220; // Width of the box
        int boxHeight = 120; // Height of the box
        g.fillRoundRect(10, frameHeight / 2 - 75, boxWidth, boxHeight, 15, 15); // Coordinates and dimensions of the box
        // Draw the scores in the middle-left position, aligned vertically
        g.setColor(Color.WHITE);
        g.setFont(japaneseFont);
        String upScoreText = "Up Score: " + (int) upScore;
        String downScoreText = "Down Score: " + (int) downScore;
        String totalScoreText = "Total Score: " + (int) totalScore;
        int upScoreTextWidth = g.getFontMetrics().stringWidth(upScoreText);
        int downScoreTextWidth = g.getFontMetrics().stringWidth(downScoreText);
        int totalScoreTextWidth = g.getFontMetrics().stringWidth(totalScoreText);
        // Draw the up score text
        g.drawString(upScoreText, 20, frameHeight / 2 - 40);
        g.drawImage(upDown.getLaternImage(), 30 + upScoreTextWidth, frameHeight / 2 - 60, 20, 20, null); // Drawing the lantern image

        // Draw the down score text
        g.drawString(downScoreText, 20, frameHeight / 2 - 10);
        g.drawImage(upDown.getHouseImage(), 30 + downScoreTextWidth, frameHeight / 2 - 30, 20, 20, null); // Drawing the house image
        g.drawString(totalScoreText, 20, frameHeight / 2 + 20);

        if (messageGameOver) {
            // Gambar pesan game over jika perlu
            g.setColor(Color.WHITE);
            try {
                File fontStyle = new File("assets/go3v2.ttf");
                Font japaneseFontBig = Font.createFont(Font.TRUETYPE_FONT, fontStyle).deriveFont(24f);
                g.setFont(japaneseFontBig);
            } catch (Exception e) {
                System.err.println("Font loading failed: " + e.getMessage());
                 g.setFont(new Font("Arial", Font.BOLD, 20)); // Fallback font
            }
            String gameOverMessage = "Game Over Player " + player.getUsername();
            String scoreMessage = "Your score: " + (int) totalScore;
            String restartMessage = "Press 'SPACE' to restart And 'ESC' to return to main menu";
            int gameOverWidth = g.getFontMetrics().stringWidth(gameOverMessage);
            int scoreWidth = g.getFontMetrics().stringWidth(scoreMessage);
            int restartWidth = g.getFontMetrics().stringWidth(restartMessage);
            g.drawString(gameOverMessage, (frameWidht - gameOverWidth) / 2, frameHeight / 2 - 60);
            g.drawString(scoreMessage, (frameWidht - scoreWidth) / 2, frameHeight / 2 - 20);
            g.drawString(restartMessage, (frameWidht - restartWidth) / 2, frameHeight / 2 + 20);
            // Stop game music and play game over sound effect
            SoundPlayer.stopBackgroundMusic();
            SoundPlayer.playSoundEffect("assets/game_over_sound.wav");
        }

        // Gambar skor di atas setiap obstacle
        for (Obstacles obstacle : obstacles) {
            int obstacleCenterX = obstacle.getPosX() + obstacle.getWidth() / 2;
            int obstacleTopY = obstacle.getPosY() - 20; // 20 pixel above the obstacle

            if (obstacle.getImage() == upDown.getLaternImage()) {
                // Menggambar skor untuk latern obstacle (latern)
                int verticalPosition = obstacle.getPosY();
                float scoreIncrease = ((float) (frameHeight - verticalPosition)) / frameHeight * 100 - 45;
                int score = (int) scoreIncrease;
                String scoreText = "" + score;
                int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
                g.setColor(Color.GREEN);
                g.setFont(japaneseFont);

                // Gambar gambar lampion di samping teks skornya
                int imageWidth = 30; // Lebar gambar lampion
                int xImage = obstacleCenterX + scoreWidth / 2;
                int yImage = obstacleTopY - 30; // Menggunakan koordinat yang sama dengan gambar lampion

                // Gambar lampion
                g.drawImage(upDown.getLaternImage(), obstacleCenterX - scoreWidth / 2 - 18, obstacleTopY - 30, imageWidth, imageWidth, null);
                // Gambar teks skor di sebelah kanan gambar lampion
                g.drawString(scoreText, xImage, yImage + imageWidth / 2 + 7);
            } else if (obstacle.getImage() == upDown.getHouseImage()) {
                if (!obstacle.isInitialPath()) {
                    // Gambar skor untuk house obstacle (house)
                    int verticalPosition = obstacle.getPosY();
                    float scoreIncrease = ((float) verticalPosition) / frameHeight * 100 - 40;
                    int score = (int) scoreIncrease;
                    String scoreText = String.valueOf(score); // Hilangkan tanda "+"
                    int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
                    g.setColor(Color.GREEN);
                    g.setFont(japaneseFont);

                    // Gambar rumah di samping teks skornya
                    int imageWidth = 30; // Lebar gambar rumah
                    int xImage = obstacleCenterX + scoreWidth / 2;
                    int yImage = obstacleTopY - 30; // Menggunakan koordinat yang sama dengan gambar rumah

                    // Gambar rumah
                    g.drawImage(upDown.getHouseImage(), obstacleCenterX - scoreWidth / 2 - 18, obstacleTopY - 30, imageWidth, imageWidth, null);

                    // Gambar teks skor di sebelah kanan gambar rumah
                    g.drawString(scoreText, xImage, yImage + imageWidth / 2 + 7);
                }
            }
        }
    }

    public void move() {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosX(player.getPosX() + player.getVelocityX());
        player.setPosY(Math.max(player.getPosY(), 0));
        player.setPosX(Math.min(player.getPosX(), frameWidht - player.getWidth()));

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacles obstacle = obstacles.get(i);
            obstacle.setPosX(obstacle.getPosX() + obstacle.getVelocityX());
        }

        // Check for collisions
        checkCollision();
    }

    public void checkCollision() {
        for (Obstacles obstacle : obstacles) {
            // Check collision with the top of the obstacle (landing)
            if (player.getPosX() + player.getWidth() > obstacle.getPosX()
                    && player.getPosX() < obstacle.getPosX() + obstacle.getWidth()
                    && player.getPosY() + player.getHeight() >= obstacle.getPosY()
                    && player.getPosY() + player.getHeight() <= obstacle.getPosY() + 5) { // Adjust threshold as needed

                // Add score when landing on an obstacle
                if (!obstacle.isLanded()) {
                    obstacle.setLanded(true);
                    if (!obstacle.isInitialPath()) {
                        if (obstacle.getImage() == upDown.getLaternImage()) {
                            // Calculate score based on vertical position of latern
                            int verticalPosition = obstacle.getPosY();
                            float scoreIncrease = ((float) (frameHeight - verticalPosition)) / frameHeight * 100 - 45;
                            upScore += scoreIncrease;
                        } else if (obstacle.getImage() == upDown.getHouseImage()) {
                            // Calculate score based on vertical position of house
                            int verticalPosition = obstacle.getPosY();
                            float scoreIncrease = ((float) verticalPosition) / frameHeight * 100 - 40;
                            downScore += scoreIncrease;
                        }
                        totalScore = upScore + downScore;
                    }
                }
            }

            // Check collision with the obstacle
            if (player.getPosX() + player.getWidth() > obstacle.getPosX()
                    && player.getPosX() < obstacle.getPosX() + obstacle.getWidth()
                    && player.getPosY() < obstacle.getPosY() + obstacle.getHeight()
                    && player.getPosY() + player.getHeight() > obstacle.getPosY()) {
                collide(player, obstacle);
            }

            // Check collision with bottom frame
            if (player.getPosY() + player.getHeight() >= frameHeight || player.getPosX() < 0) {
                gameOver();
            }
        }
    }

    public void collide(Player player, Obstacles obstacle) {
        float leftDistance = Math.abs((player.getPosX() + player.getWidth()) - obstacle.getPosX());
        float rightDistance = Math.abs(player.getPosX() - (obstacle.getPosX() + obstacle.getWidth()));
        float downDistance = Math.abs((player.getPosY() + player.getHeight()) - obstacle.getPosY());
        float upDistance = Math.abs(player.getPosY() - (obstacle.getPosY() + obstacle.getHeight()));

        if (leftDistance < rightDistance && leftDistance < downDistance && leftDistance < upDistance) {
            player.setVelocityX(-2);
            player.setPosX(obstacle.getPosX() - player.getWidth());
        } else if (rightDistance < downDistance && rightDistance < upDistance) {
            player.setVelocityX(0);
            player.setPosX(obstacle.getPosX() + obstacle.getWidth());
        } else if (downDistance < upDistance) {
            player.setVelocityY(0);
            player.setPosY(obstacle.getPosY() - player.getHeight());
        } else {
            player.setVelocityY(0);
            player.setPosY(obstacle.getPosY() + obstacle.getHeight());
        }
    }

    public void gameOver() {
        gameOver = true;
        messageGameOver = true;
        gameLoop.stop();
        obstaclesCooldown.stop();
        String username = player.getUsername();

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM tscore WHERE username='" + username + "'");
            if (resultSet.next()) { // Periksa apakah ada hasil sebelum mengambil nilai
                player.setScore(resultSet.getInt("score"));
                player.setUp(resultSet.getInt("up"));
                player.setDown(resultSet.getInt("down"));

                if (totalScore > player.getScore()) {
                    String sql = "UPDATE tscore SET score = " + totalScore + ", up = " + upScore + ", down = " + downScore + " WHERE username = '" + username + "'";
                    database.insertUpdateDeleteQuery(sql);
                }
            } else {
                // Handle case where no data found for the username
                System.out.println("No data found for username: " + username);
            }
        } catch (SQLException e) {
            // Handle SQLException appropriately
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        upDown.repaint();
    }

    public void returnToMenu() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(upDown);
        topFrame.dispose(); // Close the game frame
        JFrame menuFrame = new JFrame("Main Menu");
        Menu mainMenu = new view.Menu(menuFrame);
        menuFrame.setContentPane(mainMenu.getMainPanel());
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(1280, 720);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setResizable(false);
        menuFrame.setVisible(true);
        SoundPlayer.stopSoundEffect();
        SoundPlayer.playBackgroundMusic("assets/main_menu_sound.wav");
    }

    public void restartGame() {
        gameOver = false;
        messageGameOver = false;

        // Reset player position and velocity
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        player.setVelocityX(0);

        // Clear obstacles and reset scores
        obstacles.clear();
        upScore = 0;
        downScore = 0;
        totalScore = 0;

        // Recreate initial path and restart timers
        createInitialPath();
        obstaclesCooldown.restart();
        gameLoop.restart();

        SoundPlayer.stopBackgroundMusic();
        SoundPlayer.playBackgroundMusic("assets/playing_sound.wav");
    }


    private boolean placeLatern = false;

    public void placeObstacles() {
        int randomPosY;

        if (placeLatern) {
            // Posisi random untuk latern harus di tengah bagian atas frame
            randomPosY = (int) (frameHeight / 4 - laternHeight / 2 + Math.random() * (frameHeight / 4));
            Obstacles latern = new Obstacles(obstacleStartPosX, randomPosY, laternWidth, laternHeight, upDown.getLaternImage());
            obstacles.add(latern);
        } else {
            // Posisi random untuk house harus di bawah setengah tinggi frame
            randomPosY = (int) (obstacleStartPosY + (houseHeight * 4 / 5) + Math.random() * (frameHeight / 2)); // Adjusted to frameHeight / 2
            // Pastikan house berada di bawah setengah tinggi frame
            if (randomPosY < frameHeight / 2) {
                randomPosY = frameHeight * 6 / 10; // Default position if still too low
            }
            Obstacles house = new Obstacles(obstacleStartPosX, randomPosY, houseWidth, houseHeight, upDown.getHouseImage());
            obstacles.add(house);
        }

        // Toggle placeLatern untuk bergantian antara latern atas dan house bawah
        placeLatern = !placeLatern;
    }

    public void createInitialPath() {
        int startX = 0;
        int startY = frameHeight * 3 / 4; // Set initial path at 3/4 of frame height
        int numberOfInitialObstacles = 10; // Number of obstacles in the initial path

        for (int i = 0; i < numberOfInitialObstacles; i++) {
            Obstacles house = new Obstacles(startX, startY , houseWidth, houseHeight, upDown.getHouseImage());
            house.setInitialPath(true);
            obstacles.add(house);
            startX += houseWidth; // Adjust space between initial obstacles
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            upDown.repaint();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}


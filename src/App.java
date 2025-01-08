/*Saya [Shidiq Arifin Sudrajat] mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.


CREDITS:
Assets Gambar:
Background Image: illustAC, 23672092, https://en.ac-illust.com/clip-art/23870247/japanese-landscape-pixel-art
Latern Image: illustAC, Tachibana, https://en.ac-illust.com/clip-art/24172487/paper-lantern-pixel-art
House Image: Craiyon, https://www.craiyon.com/image/X-hUxZryQQ-DIEpjuFPimQ
Ninja Image: CraftPix.net, https://craftpix.net/freebies/free-shinobi-sprites-pixel-art

Assets Audio:
Ingame Sound: https://youtu.be/pXdrz1pB35Q?si=Tl132DTEu1EFHnvk
Game Over Sound: mixkit.co, https://mixkit.co/free-sound-effects/game-over/
Main menu Sound:
Track: Jim Yosef - Samurai [NCS Release]
Music provided by NoCopyrightSounds.
Free Download / Stream: http://NCS.io/Samurai
Watch: https://youtu.be/EZUPEoj3Qjs?si=ef3Ey40kXf206GbO

Assets Font:
Dafont: Gang of Three, https://www.dafont.com/gang-of-three.font

*/
import view.Menu;
import viewmodel.SoundPlayer;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        JFrame menuFrame = new JFrame("Main Menu");

        // Configure the JFrame
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(1280, 720);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setResizable(false);

        // Create an instance of the view.Menu class
        Menu mainMenu = new Menu(menuFrame);
        menuFrame.setContentPane(mainMenu.getMainPanel());

        menuFrame.setVisible(true);
        // Play main menu background music
        SoundPlayer.playBackgroundMusic("assets/main_menu_sound.wav");
    }
}

/*Saya [Shidiq Arifin Sudrajat] mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
*/
package viewmodel;

import view.UpDown;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Input implements KeyListener {
    private UpDown upDown;

    public Input(UpDown upDown) {
        this.upDown = upDown;
        upDown.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            upDown.getGame().getPlayer().setVelocityY(-16);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            upDown.getGame().getPlayer().setVelocityX(-4);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            upDown.getGame().getPlayer().setVelocityX(2);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            upDown.getGame().getPlayer().setVelocityY(8);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            upDown.getGame().restartGame();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (upDown.getGame().isGameOver())
            {
                upDown.getGame().returnToMenu();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            upDown.getGame().getPlayer().setVelocityX(0);
        }
    }
}

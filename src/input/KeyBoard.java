package input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import cena.Cena;

/**
 *
 * @author Grupo sala
 */
public class KeyBoard implements KeyListener{
    private Cena cena;

    public KeyBoard(Cena cena){
        this.cena = cena;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            System.exit(0);
        }

        switch(e.getKeyChar()){
            case 'p':
                cena.pausaJogo();
                break;

            case 'x':
                cena.stopJogo();
                break;

            case 's':
                cena.startJogo();
                break;
            case 'a':
                cena.exibirTelaApresentacao();
                break;
        }

        // Transladar - Move a barra no eixo X
        moveBarra(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Método para movimentar a barra
     *
     * @param e KeyEvent - Contexto opengl
     */
    public void moveBarra(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) // Seta para direita
        {
            // Verifica se atingiu o valor máximo positivo no eixo X
            if(cena.getTransladar() <= 0.79f)
            {
                cena.setTransladar(cena.getTransladar()+0.03f);
            }
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT) // Seta para esquerda
        {
            // Verifica se atingiu o valor máximo negativo no eixo X
            if(cena.getTransladar() >= -0.79f)
            {
                cena.setTransladar(cena.getTransladar()-0.03f);
            }
        }
    }
}
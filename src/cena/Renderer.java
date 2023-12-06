package cena;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import input.KeyBoard;


/**
 *
 * @author Grupo sala
 */
public class Renderer {
    private static GLWindow window = null;
    public static int screenWidth = 1920; // 600;  //1280  - 640
    public static int screenHeight = 1080; //600; //960  - 480

    // Cria a janela de rendeziracao do JOGL
    public static void init(){
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);

        window.setSize(screenWidth, screenHeight);
        window.setResizable(false);
        window.setFullscreen(true);

        Cena cena = new Cena();

        // Adiciona a Cena a Janela
        window.addGLEventListener(cena);

        // Habilita o teclado : cena
        window.addKeyListener(new KeyBoard(cena));


        // Define fps de render na cena
        FPSAnimator animator = new FPSAnimator(window, 60);

        // Inicia o loop de animacao
        animator.start();

        // Encerrar a aplicacao adequadamente
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });

        window.setVisible(true);
    }

    public static void main(String[] args) {
        init();
    }
}


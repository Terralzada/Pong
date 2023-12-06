package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import textura.Textura;

import java.awt.*;
import java.util.Random;


/**
 * @author Grupo sala
 */
public class Cena implements GLEventListener {
    // Constantes para identificar as imagens
    public static final String FACE1 = "imagens/barra.jpg";
    public static final String FACE2 = "imagens/tijolo.jpg";
    public static final String FACE3 = "imagens/esfera.jpg";
    //Quantidade de Texturas a ser carregada
    private final int totalTextura = 3;
    private final int texturaFiltro = GL2.GL_LINEAR;
    private final int texturaWrap = GL2.GL_REPEAT;
    private final int texturaModo = GL2.GL_DECAL;
    public int modo;
    public int tonalizacao;
    public boolean liga;
    public int opcaoLuz;
    public int vidas = 5;
    //implements
    public float movimentoBolaX = 0;
    public float movimentoBolaY = 0;
    public float movimentoPontoX = 0;
    public float movimentoPontoY = 0;
    // Direcao do movimento da bola
    public String direcaoBolaX = "D"; // D = direita, E = esquerda
    public String direcaoBolaY = "C"; // C = cima, B = baixo
    public boolean pause = false;
    public int fase = 1;
    public float fatorDificuldade = 0.0f;
    public Point coordenadasObstaculo = new Point(6);
    GLU glu;
    double posicaoInicioBarra;
    double posicaoFimBarra;
    boolean exibeTelaApresentacao = true;
    boolean exibidaTelaApresentacaoFase2 = false;
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    private TextRenderer textRenderer;
    private TextRenderer textRendererPerdeu;
    private float transladar = 0;
    private int pontuacao;
    //Referencia para classe Textura
    private Textura textura = null;

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        GL2 gl = drawable.getGL().getGL2();


        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -1;
        xMax = yMax = zMax = 1;

        modo = GL2.GL_FILL;
        tonalizacao = GL2.GL_SMOOTH;
        liga = false;
        opcaoLuz = 1;

        //dados iniciais da cena
        glu = new GLU();

        pontuacao = 0;

        coordenadasObstaculo.setXY(0, 0.06f, -0.06f);
        coordenadasObstaculo.setXY(1, -0.02f, -0.08f);
        coordenadasObstaculo.setXY(2, -0.08f, -0.03f);
        coordenadasObstaculo.setXY(3, -0.06f, 0.06f);
        coordenadasObstaculo.setXY(4, 0.03f, 0.08f);
        coordenadasObstaculo.setXY(5, 0.07f, 0.02f);


        textRenderer = new TextRenderer(new Font("Castellar", Font.PLAIN, 28));
        textRendererPerdeu = new TextRenderer(new Font("Castellar", Font.PLAIN, 60));

        //Cria uma instancia da Classe Textura indicando a quantidade de texturas
        textura = new Textura(totalTextura);

        //Habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);


    }


    @Override
    @SuppressWarnings("empty-statement")
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();

        //objeto para desenho 3D
        GLUT glut = new GLUT();

        //define a cor da janela (R, G, G, alpha)
        if (pontuacao == 0) {
            gl.glClearColor(1, 1, 1, 1);
        }


        //limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); //ler a matriz identidade

        /*
         * desenho da cena
         */

        // Desenha eixo X,Y na SRU
        //desenhaEixoXY(gl);

        if (exibeTelaApresentacao == true) {
            exibeApresentacao(gl);
        } else {

            // Exibe menu
            exibeMenu(gl);

            // Exibe fase
            exibeFase(gl);

            // Exibe vidas
            exibeVidas(gl, glut);

            // Fim de jogo - vidas esgotadas
            if (vidas < 1) {
                fimDeJogo(gl);
                desligaLuz(opcaoLuz, gl);
            }

            // Exibe placar
            exibePlacar(gl, pontuacao);

            // Desenha barra
            desenhaBarra(gl);

            // Desenha circulo
            desenhaCirculo(gl, glut);

            // Ponto pequeno para testes de coordenadas
            //desenhaPonto(gl, glut);

            // insere obstaculo fase 2
            if (fase == 2) {
                exibeMensagemFase2(gl);
                insereObstaculoFase2(gl, glut);

            }

            // Liga iluminação
            ligaIluminacao(gl);

            // Liga luz
            ligaLuz(opcaoLuz, gl);


        }

        gl.glFlush();
    }


    /**
     * Método para desenhar a barra do jogador
     *
     * @param gl GL2 - Contexto opengl
     */
    public void desenhaBarra(GL2 gl) {
        // Desenha um cubo no qual a textura é aplicada
        // Não é geração de textura automática
        textura.setAutomatica(false);

        // Configura os filtros
        textura.setFiltro(texturaFiltro);
        textura.setModo(texturaModo);
        textura.setWrap(texturaWrap);

        // Cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, FACE1, 0);

        gl.glColor3f(0, 0, 1);
        gl.glPushMatrix();
        gl.glTranslated(transladar, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-0.2f, -0.9f, 0.2f);

        gl.glTexCoord2f(0.0f, 1);
        gl.glVertex3f(0.2f, -0.9f, 0.2f);

        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(0.2f, -0.85f, 0.2f);

        gl.glTexCoord2f(1, 0.0f);
        gl.glVertex3f(-0.2f, -0.85f, -0.2f);
        gl.glEnd();
        gl.glPopMatrix();

        // Desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 0);

        pegaAreaBarra();
    }

    /**
     * Método para recuperar a area da barra
     */
    public void pegaAreaBarra() {
        posicaoInicioBarra = getTransladar() + (-0.3f);
        posicaoFimBarra = getTransladar() + 0.3f;
    }

    /**
     * Método para desenhar a bolinha
     *
     * @param gl   GL2 - Contexto opengl
     * @param glut GLUT - Contexto opengl
     */
    public void desenhaCirculo(GL2 gl, GLUT glut) {
        if (pause == false) {
            movimentoBola(gl);
        }

        // Geração de textura automática
        textura.setAutomatica(false);

        // Transformações geométricas para as texturas
        gl.glMatrixMode(GL2.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        // Geração de textura automática
        textura.setAutomatica(true);

        // Habilita os filtros
        textura.setFiltro(GL2.GL_LINEAR);
        textura.setModo(GL2.GL_DECAL);
        textura.setWrap(GL2.GL_REPEAT);
        textura.gerarTextura(gl, FACE3, 2);

        gl.glColor3f(0, 0, 1);
        gl.glPushMatrix();
        gl.glTranslated(movimentoBolaX, movimentoBolaY, 0);
        glut.glutSolidSphere(0.03f, 20, 20);
        gl.glPopMatrix();

        textura.desabilitarTextura(gl, 2);
    }

    /*
     * Método para desenhar um ponto na tela para os testes de cálculo de coordenadas, usado apenas para testes manuais de colisao
     *
     * @param gl GL2 - Contexto opengl
     * @param glut GLUT - Contexto opengl
     */

    /**
     * Método para detectar a colisao da bola com a barra
     */
    public void detectaColisaoBarra(GL2 gl) {

        if (movimentoBolaY <= -0.83f) {
            if (movimentoBolaX >= posicaoInicioBarra && movimentoBolaX <= posicaoFimBarra) {
                direcaoBolaY = "C";
                direcaoBolaX = String.valueOf(getRandomDirecao("D", "E"));
                movimentoBolaX += getRandomNumber(-0.02f, 0.02f);
                atualizaPontuacao(gl);
            } else {
                pausaJogo();

                movimentoBolaX = 0;
                movimentoBolaY = 0;

                direcaoBolaY = "C";
                direcaoBolaX = String.valueOf(getRandomDirecao("D", "E"));

                if (vidas > 0) {
                    vidas--;
                }

                pausaJogo();

            }
        }
    }

    /**
     * Método para detectar a colisao da bola com o obstaculo da fase 2
     */
    public void detectaColisaoObstaculo() {
        float[] coordenada0 = coordenadasObstaculo.getXY(0);
        float[] coordenada1 = coordenadasObstaculo.getXY(1);
        float[] coordenada2 = coordenadasObstaculo.getXY(2);
        float[] coordenada3 = coordenadasObstaculo.getXY(3);
        float[] coordenada4 = coordenadasObstaculo.getXY(4);
        float[] coordenada5 = coordenadasObstaculo.getXY(5);

        if (fase == 2) {
            if (
                    movimentoBolaX <= coordenada0[0]
                            &&
                            movimentoBolaX >= coordenada1[0]

                            &&

                            movimentoBolaY <= coordenada0[1]
                            &&
                            movimentoBolaY >= coordenada1[1]
            ) {
                direcaoBolaY = "B";
                direcaoBolaX = String.valueOf(getRandomDirecao("D", "E"));
            }

            if (
                    movimentoBolaX <= coordenada1[0]
                            &&
                            movimentoBolaX >= coordenada2[0]

                            &&

                            movimentoBolaY >= coordenada1[1]
                            &&
                            movimentoBolaY <= coordenada2[1]
            ) {
                direcaoBolaY = "B";
                direcaoBolaX = String.valueOf(getRandomDirecao("D", "E"));
            }

            if (
                    movimentoBolaX >= coordenada2[0]
                            &&
                            movimentoBolaX <= coordenada3[0]

                            &&

                            movimentoBolaY >= coordenada2[1]
                            &&
                            movimentoBolaY <= coordenada3[1]
            ) {
                direcaoBolaY = String.valueOf(getRandomDirecao("C", "B"));
                direcaoBolaX = "E";
            }

            if (
                    movimentoBolaX >= coordenada3[0]
                            &&
                            movimentoBolaX <= coordenada4[0]

                            &&

                            movimentoBolaY >= coordenada3[1]
                            &&
                            movimentoBolaY <= coordenada4[1]
            ) {
                direcaoBolaY = "C";
                direcaoBolaX = String.valueOf(getRandomDirecao("D", "E"));
            }

            if (
                    movimentoBolaX >= coordenada4[0]
                            &&
                            movimentoBolaX <= coordenada5[0]

                            &&

                            movimentoBolaY <= coordenada4[1]
                            &&
                            movimentoBolaY >= coordenada5[1]
            ) {
                direcaoBolaY = String.valueOf(getRandomDirecao("C", "B"));
                direcaoBolaX = "D";
            }

            if (
                    movimentoBolaX <= coordenada5[0]
                            &&
                            movimentoBolaX >= coordenada0[0]

                            &&

                            movimentoBolaY <= coordenada5[1]
                            &&
                            movimentoBolaY >= coordenada0[1]
            ) {
                direcaoBolaY = String.valueOf(getRandomDirecao("C", "B"));
                direcaoBolaX = "D";
            }
        }
    }

    /**
     * Método para exibir a tela de apresentacao do jogo
     *
     * @param gl GL2 - Contexto opengl
     */
    public void exibeApresentacao(GL2 gl) {
        String mensagemNomeJogo = "PONG - THE GAME";
        desenhaTextoGrande(gl, 650, 980, Color.black, mensagemNomeJogo);

        String mensagemInstrucoesTitulo = "Como Jogar";
        desenhaTexto(gl, 850, 660, Color.black, mensagemInstrucoesTitulo);

        String mensagemInstrucoes = "Use as teclas de setinhas (<-, ->) para movimentar a barra";
        desenhaTexto(gl, 450, 620, Color.black, mensagemInstrucoes);

        String mensagemStop = "Aperte \"s\" para iniciar o jogo";
        desenhaTexto(gl, 700, 580, Color.black, mensagemStop);

        String mensagemVoltar = "Aperte \"a\" para voltar para esta tela de apresentação";
        desenhaTexto(gl, 500, 540, Color.black, mensagemVoltar);

        String mensagemSair = "Aperte \"ESC\" para sair do jogo";
        desenhaTexto(gl, 700, 500, Color.black, mensagemSair);
    }

    /**
     * Método para exibir o menu de opcoes do jogo
     *
     * @param gl GL2 - Contexto opengl
     */
    public void exibeMenu(GL2 gl) {
        String mensagemStart = "s: iniciar";
        desenhaTexto(gl, 10, 1050, Color.black, mensagemStart);

        String mensagemPause = "p: pausar";
        desenhaTexto(gl, 10, 1020, Color.black, mensagemPause);

        String mensagemStop = "x: parar";
        desenhaTexto(gl, 10, 990, Color.black, mensagemStop);

        String mensagemSair = "ESC: sair";
        desenhaTexto(gl, 10, 960, Color.black, mensagemSair);

        String mensagemVida = "Vidas:";
        desenhaTextoGrande(gl, 780, 1000, Color.black, mensagemVida);

    }

    /**
     * Método para exibir a fase atual do jogo
     *
     * @param gl GL2 - Contexto opengl
     */
    public void exibeFase(GL2 gl) {
        String mensagemFase = "Nivel: " + fase;
        desenhaTexto(gl, 200, 1050, Color.black, mensagemFase);
    }

    /**
     * Método para desenhar a representacao de vidas do jogador
     *
     * @param gl   GL2 - Contexto opengl
     * @param glut GLUT - Contexto opengl
     */

    public void exibeVidas(GL2 gl, GLUT glut) {
        float distancia = 0.1f;
        for (int i = vidas; i > 0; i--) {
            gl.glColor3f(3, 2, 0);
            gl.glPushMatrix();
            gl.glTranslatef(distancia, 0.9f, 0);
            gl.glRotated(-90, 1, 0, 0);
            glut.glutSolidSphere(0.05f, 15, 15);
            gl.glPopMatrix();

            distancia += 0.1f;
        }
    }

    /**
     * Método para exibir o placar do jogo
     *
     * @param gl        GL2 - Contexto opengl
     * @param pontuacao int - Pontos do jogador
     */
    public void exibePlacar(GL2 gl, int pontuacao) {
        String mensagemPlacar = "Pontos: " + pontuacao;
        desenhaTexto(gl, 1650, 1050, Color.black, mensagemPlacar);
    }

    /**
     * Método para determinar a direcao da bola, prevendo se houve colisao antes de determinar a nova direcao
     */
    public void direcaoBola(GL2 gl) {
        detectaColisaoObstaculo();

        detectaColisaoBarra(gl);

        if (movimentoBolaY >= 1.0f) {
            direcaoBolaY = "B";
        }

        if (movimentoBolaX >= 1.0f) {
            direcaoBolaX = "E";
        }

        if (movimentoBolaX <= -1.0f) {
            direcaoBolaX = "D";
        }
    }

    /**
     * Método para determinar o movimento e direcao da bola, impedindo movimentacao repetida infinita
     */
    public void movimentoBola(GL2 gl) {
        direcaoBola(gl);

        switch (direcaoBolaY) {
            case "C":
                movimentoBolaY += getRandomNumber(0.01f, 0.03f);
                break;
            case "B":
                movimentoBolaY -= getRandomNumber(0.01f, 0.02f);
                break;
        }

        switch (direcaoBolaX) {
            case "D":
                movimentoBolaX += getRandomNumber(0.01f, 0.02f);
                break;
            case "E":
                movimentoBolaX -= getRandomNumber(0.02f, 0.01f);
                break;
        }
    }

    /**
     * Método para recuperar um valor randomico de dois valores possiveis
     *
     * @param min float - Valor minimo
     * @param max float - Valor maximo
     * @return float - Valor recuperado na selecao randomica
     */
    public float getRandomNumber(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min) + fatorDificuldade;
    }

    /**
     * Método para recuperar um valor randomico de dois valores possiveis
     *
     * @param direcao1 String - Valor de direcao 1
     * @param direcao2 String - Valor de direcao 2
     * @return char - Valor recuperado na selecao randomica
     */
    public char getRandomDirecao(String direcao1, String direcao2) {
        Random r = new Random();

        String alphabet = direcao1 + direcao2;
        return alphabet.charAt(r.nextInt(alphabet.length()));
    }

    /**
     * Método para atualizar a pontuacao do jogador de 50 em 50 pontos, determinar se o jogador passou para a fase 2
     * e incrementar o fator de dificuldade do jogo aumentando um pouco a velocidade da bola
     */
    public void atualizaPontuacao(GL2 gl) {
        pontuacao += 50;

        gl.glClearColor(getRandomNumber(0, 1), getRandomNumber(0, 1), getRandomNumber(0, 1), 1);

        if (pontuacao >= 200) {
            fase = 2;
            fatorDificuldade = 0.005f;
        } else {
            fase = 1;
            fatorDificuldade = 0.0f;
        }
    }

    /**
     * Método para pausar e despausar o jogo
     */
    public void pausaJogo() {
        pause = !pause;
    }

    /**
     * Método para terminar com o jogo
     */
    public void stopJogo() {
        transladar = 0;
        movimentoBolaY = 0f;
        movimentoBolaX = 0f;

        vidas = 5;
        pontuacao = 0;

        fatorDificuldade = 0.0f;
        fase = 1;

        pause = true;
    }

    /**
     * Método para inicia um novo jogo
     */
    public void startJogo() {
        exibeTelaApresentacao = false;
        stopJogo();
        pause = false;
    }

    /**
     * Método para desenhar o obstaculo da fase 2
     *
     * @param gl   GL2 - Contexto opengl
     * @param glut GLUT - Contexto opengl
     */
    public void insereObstaculoFase2(GL2 gl, GLUT glut) {
        // Geração de textura automática
        textura.setAutomatica(false);

        // Transformações geométricas para as texturas
        gl.glMatrixMode(GL2.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        // Geração de textura automática
        textura.setAutomatica(true);

        // Habilita os filtros
        textura.setFiltro(GL2.GL_LINEAR);
        textura.setModo(GL2.GL_DECAL);
        textura.setWrap(GL2.GL_REPEAT);
        textura.gerarTextura(gl, FACE2, 1);

        gl.glColor3f(1, 0, 0);
        gl.glPushMatrix();
        gl.glRotated(45, 1, 1, 0);
        gl.glTranslatef(0, 0, 0);
        glut.glutSolidCube(0.1f);
        gl.glPopMatrix();

        // Desabilita a textura
        textura.desabilitarTextura(gl, 1);


    }


    /**
     * Método para exibir o fim de jogo
     *
     * @param gl GL2 - Contexto opengl
     */
    public void fimDeJogo(GL2 gl) {
        String mensagemFase = "Game Over";
        desenhaTextoGrande(gl, 760, 700, Color.BLACK, mensagemFase);

        transladar = 0;
        movimentoBolaY = 0f;
        movimentoBolaX = 0f;

        vidas = 0;

        pause = true;
    }

    /**
     * Método para ligar a iluminacao
     *
     * @param gl GL2 - Contexto opengl
     */

    public void ligaIluminacao(GL2 gl) {
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glShadeModel(tonalizacao);
    }


    /**
     * Método para ligar a iluminacao difusa
     *
     * @param gl GL2 - Contexto opengl
     */
    public void iluminacaoDifusa(GL2 gl) {
        float[] luzDifusa = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] posicaoLuz = {0.0f, 50.0f, 100.0f, 1.0f};

        gl.glEnable(GL2.GL_LIGHT1);

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, posicaoLuz, 0);
    }

    /**
     * Método para ligar a iluminacao especular
     *
     * @param gl GL2 - Contexto opengl
     */
    public void iluminacaoEspecular(GL2 gl) {
        float[] luzEspecular = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] posicaoLuz = {-30.0f, 50.0f, 100.0f, 1.0f};

        gl.glEnable(GL2.GL_LIGHT0);

        int especMaterial = 128;

        gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, especMaterial);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, luzEspecular, 0);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, luzEspecular, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);

    }

    /**
     * Método para ligar a luz
     *
     * @param opcaoLuz int - Opcao de iluminacao: 0 = especular, 1 = difusa
     * @param gl       GL2 - Contexto opengl
     */
    public void ligaLuz(int opcaoLuz, GL2 gl) {
        switch (opcaoLuz) {
            case 0:
                iluminacaoEspecular(gl);
                break;
            case 1:
                iluminacaoDifusa(gl);
                break;
        }
    }

    /**
     * Método para desligar a luz
     *
     * @param opcaoLuz int - Opcao de iluminacao: 0 = especular, 1 = difusa
     * @param gl       GL2 - Contexto opengl
     */
    public void desligaLuz(int opcaoLuz, GL2 gl) {

        switch (opcaoLuz) {
            case 0:
                gl.glDisable(GL2.GL_LIGHT0);
                break;
            case 1:
                gl.glDisable(GL2.GL_LIGHT1);
                break;
        }
    }

    /**
     * Método para desenhar texto padrao
     *
     * @param gl       GL2 - Opcao de iluminacao: 0 = especular, 1 = difusa
     * @param xPosicao int - Posicao X
     * @param yPosicao int - Posicao Y
     * @param cor      Color - Contexto opengl
     * @param frase    String - Frase que sera desenhada
     */
    public void desenhaTexto(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        textRenderer.beginRendering(cena.Renderer.screenWidth, cena.Renderer.screenHeight);
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, modo);
    }

    /**
     * Método para desenhar texto grande
     *
     * @param gl       GL2 - Opcao de iluminacao: 0 = especular, 1 = difusa
     * @param xPosicao int - Posicao X
     * @param yPosicao int - Posicao Y
     * @param cor      Color - Contexto opengl
     * @param frase    String - Frase que sera desenhada
     */
    public void desenhaTextoGrande(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        textRendererPerdeu.beginRendering(cena.Renderer.screenWidth, cena.Renderer.screenHeight);
        textRendererPerdeu.setColor(cor);
        textRendererPerdeu.draw(frase, xPosicao, yPosicao);
        textRendererPerdeu.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, modo);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // Obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();

        // Evita a divisao por zero
        if (height == 0) height = 1;

        // Calcula a proporcao da janela (aspect ratio) da nova janela
        float aspect = (float) width / height;

        // Seta o viewport para abranger a janela inteira
        //gl.glViewport(0, 0, width, height);

        // Ativa a matriz de projecao
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //ler a matriz identidade

        // Projecao ortogonal sem a correcao do aspecto
        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);

        // Ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //ler a matriz identidade
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    /**
     * Método para recuperar a posicao da barra no eixo X
     */
    public float getTransladar() {
        return transladar;
    }

    /**
     * Método para atribuir posicao da barra no eixo X
     *
     * @param transladar float - Posicao da barra no eixo x
     */
    public void setTransladar(float transladar) {
        if (pause == false) {
            this.transladar = transladar;
        }
    }

    /**
     * Método para exibir a tela de apresentacao
     */
    public void exibirTelaApresentacao() {
        exibeTelaApresentacao = true;
    }

    /**
     * Método para desenha a mensagem de fase 2
     *
     * @param gl GL2 - Contexto opengl
     */
    public void exibeMensagemFase2(GL2 gl) {
        if (exibidaTelaApresentacaoFase2 == false) {
            String mensagemFase = "Bem vindo ao nivel " + fase;
            desenhaTextoGrande(gl, 600, 700, Color.black, mensagemFase);

            if (pontuacao >= 240) {
                exibidaTelaApresentacaoFase2 = true;
            }
        }
    }
}


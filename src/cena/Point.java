package cena;

public class Point {
    private final float[] points;

    /**
     * Método construtor
     *
     * @param size int - Tamanho da matriz
     */
    public Point(int size) {
        points = new float[2 * size];
    }

    /**
     * Método para recuperar o valor do eixo X do indice informado
     *
     * @param index int - Indice da matriz
     * @return float - Valor de X do indice informado
     */
    public float getX(int index) {
        return points[2 * index];
    }

    /**
     * Método para atribuir o valor do eixo X do indice informado
     *
     * @param index int - Indice da matriz
     * @param value float - Valor de X
     */
    public void setX(int index, float value) {
        points[2 * index] = value;
    }

    /**
     * Método para recuperar o valor do eixo Y do indice informado
     *
     * @param index int - Indice da matriz
     * @return float - Valor de Y do indice informado
     */
    public float getY(int index) {
        return points[2 * index + 1];
    }

    /**
     * Método para atribuir o valor do eixo Y do indice informado
     *
     * @param index int - Indice da matriz
     * @param value float - Valor de Y
     */
    public void setY(int index, float value) {
        points[2 * index + 1] = value;
    }

    /**
     * Método para atribuir o valor da coordenada do indice informado
     *
     * @param index int - Indice da matriz
     * @param valueX float - Valor de X
     * @param valueY float - Valor de Y
     */
    public void setXY(int index, float valueX, float valueY)
    {
        setX(index, valueX);
        setY(index, valueY);
    }

    /**
     * Método para recuperar o valor da coordenada do indice informado
     *
     * @param index int - Indice da matriz
     * @return float - Coordenada do indice informado
     */
    public float[] getXY(int index)
    {
        float x = getX(index);
        float y = getY(index);

        return new float[] {x, y};
    }
}

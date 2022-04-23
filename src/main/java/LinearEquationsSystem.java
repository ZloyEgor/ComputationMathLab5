public class LinearEquationsSystem {
    public int dimension;
    public double[][] a;
    public double[] b;


    public LinearEquationsSystem(int dimension, double[][] a, double[] b) {
        this.dimension = dimension;
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++) {
                sb.append(a[i][j]);
                if ((j + 1) % 4 == 0)
                    sb.append("\t\t\t");
                else
                    sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

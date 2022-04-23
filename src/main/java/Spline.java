public class Spline extends Function{
    double a;
    double b;
    double c;
    double d;
    double leftBorder;
    double rightBorder;

    public Spline(double a, double b, double c, double d, double leftBorder, double rightBorder) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    @Override
    public double get(double x) {
        return a + b * (x - leftBorder) + c * Math.pow(x - leftBorder, 2) + d * Math.pow(x - leftBorder, 3);
    }

    @Override
    public String getFormula() {
        return "spline";
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public double getRightBorder() {
        return rightBorder;
    }
}

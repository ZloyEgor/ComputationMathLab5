import java.util.ArrayList;

public abstract class Function {
    public abstract double get(double x);
    public abstract String getFormula();

    public ArrayList<Dot> getDots(int amount, double a, double b, boolean makeError) {
        ArrayList<Dot> dots = new ArrayList<>(amount);

        double x = a;
        double step = (b - a) / ((double) amount);

        dots.add(new Dot(x, get(x)));

        for (int i = 0; i < amount; i++) {
            x += step;

            if(!makeError)
                dots.add(new Dot(x, get(x)));
            else {
                if (Math.random() < 0.3)
                    dots.add(new Dot(x, get(x) + 0.5 * Math.random()));
                else
                    dots.add(new Dot(x, get(x)));
            }
        }
        return dots;
    }
}

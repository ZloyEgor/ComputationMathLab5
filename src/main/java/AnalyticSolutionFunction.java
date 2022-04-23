public abstract class AnalyticSolutionFunction extends Function{
    protected double c;
    public abstract void calculateC(double x, double y);

    @Override
    public String getFormula() {
        return null;
    }
}

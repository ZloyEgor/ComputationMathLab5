import java.util.ArrayList;

public class DifferentialEquationSolutionInformation {
    private DifferentialEquation equation;
    private AnalyticSolutionFunction analyticSolutionFunction;
    private ArrayList<Dot> dots;

    private double startX;
    private double endX;

    public DifferentialEquation getEquation() {
        return equation;
    }

    public void setEquation(DifferentialEquation equation) {
        this.equation = equation;
    }

    public AnalyticSolutionFunction getAnalyticSolutionFunction() {
        return analyticSolutionFunction;
    }

    public void setAnalyticSolutionFunction(AnalyticSolutionFunction analyticSolutionFunction) {
        this.analyticSolutionFunction = analyticSolutionFunction;
    }

    public ArrayList<Dot> getDots() {
        return dots;
    }

    public void setDots(ArrayList<Dot> dots) {
        this.dots = dots;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }
}

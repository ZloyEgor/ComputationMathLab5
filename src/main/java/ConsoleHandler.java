import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleHandler {

    public static final double DEFAULT_STEP_LENGTH = 0.1;

    public DifferentialEquationWithAnalyticSolution[] equations = {
            new DifferentialEquationWithAnalyticSolution(new DifferentialEquation(new FunctionTwoArguments() {
                @Override
                public double get(double x, double y) {
                    return Math.pow(x, 2) - 2 * y;
                }

                @Override
                public String getFormula() {
                    return "x^2 - 2y";
                }
            }), new AnalyticSolutionFunction() {
                @Override
                public void calculateC(double x, double y) {
                    c = (y - Math.pow(x, 2) / 2. + x / 2. - 1./4.) * Math.exp(2*x);
                }

                @Override
                public double get(double x) {
                    return c / Math.exp(2*x) + Math.pow(x, 2) / 2. - x / 2 + 1./ 4;
                }

            })
    };



    public DifferentialEquationSolutionInformation handleInput(Scanner in) {
        System.out.println("Choose differential equation:");
        for (int i = 0; i < equations.length; i++) {
            System.out.println(i + 1 + ":\t" + equations[i].getEquation());
        }

        String line;

        int number = 0;
        boolean correctInput = false;

        while (!correctInput) {
            line = in.nextLine();
            try {
                number = Integer.parseInt(line);
                if (number < 1 || number > equations.length)
                    System.out.println("Enter valid data!");
                else
                    correctInput = true;
            } catch (Exception e) {
                System.out.println("Enter valid data!");
            }
        }

        double startX = 0;
        correctInput = false;
        System.out.println("Enter start X");
        while (!correctInput) {
            line = in.nextLine();
            try {
                startX = Double.parseDouble(line);
                correctInput = true;
            } catch (Exception e) {
                System.out.println("Enter valid data!");
            }
        }

        double startY = 0;
        correctInput = false;
        System.out.println("Enter start Y");
        while (!correctInput) {
            line = in.nextLine();
            try {
                startY = Double.parseDouble(line);
                correctInput = true;
            } catch (Exception e) {
                System.out.println("Enter valid data!");
            }
        }

        double endX = 0;
        correctInput = false;
        System.out.println("Enter end X");
        while (!correctInput) {
            line = in.nextLine();
            try {
                endX = Double.parseDouble(line);
                if (endX <= startX) {
                    System.out.println("Error: end X must be greater than start X");
                    continue;
                }
                correctInput = true;
            } catch (Exception e) {
                System.out.println("Enter valid data!");
            }
        }

        DifferentialEquationSolutionInformation information = new DifferentialEquationSolutionInformation();

        information.setEquation(equations[number - 1].getEquation());
        equations[number - 1].getSolutionFunction().calculateC(startX, startY);
        information.setAnalyticSolutionFunction(equations[number - 1].getSolutionFunction());
        information.setDots(getDots(new EulerMethod(), equations[number - 1].getEquation(), startX, startY, endX));

        information.setStartX(startX);
        information.setEndX(endX);

        return information;
    }

    private ArrayList<Dot> getDots(DifferentialEquationSolver solver, DifferentialEquation equation, double startX, double startY, double endX) {
        return solver.solve(equation, startX, startY, endX - startX, DEFAULT_STEP_LENGTH);
    }
}

class DifferentialEquationWithAnalyticSolution {
    private DifferentialEquation equation;
    private AnalyticSolutionFunction solutionFunction;

    public DifferentialEquationWithAnalyticSolution(DifferentialEquation equation, AnalyticSolutionFunction solutionFunction) {
        this.equation = equation;
        this.solutionFunction = solutionFunction;
    }

    public DifferentialEquation getEquation() {
        return equation;
    }

    public AnalyticSolutionFunction getSolutionFunction() {
        return solutionFunction;
    }
}

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleHandler {

    public static final double DEFAULT_STEP_LENGTH = 0.01;

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

            }),

            new DifferentialEquationWithAnalyticSolution(new DifferentialEquation(new FunctionTwoArguments() {
                @Override
                public double get(double x, double y) {
                    return Math.pow(x, 2) + 2 * x + y;
                }

                @Override
                public String getFormula() {
                    return "x^2 + 2x + y";
                }
            }), new AnalyticSolutionFunction() {
                @Override
                public void calculateC(double x, double y) {
                    c = (y + Math.pow(x, 2) + 4 * x + 4) / Math.exp(x);
                }

                @Override
                public double get(double x) {
                    return c * Math.exp(x) - Math.pow(x, 2) - 4 * x - 4;
                }

            }),

            new DifferentialEquationWithAnalyticSolution(new DifferentialEquation(new FunctionTwoArguments() {
                @Override
                public double get(double x, double y) {
                    return -1 * (2 * y + 1) * 1 / Math.tan(x);
                }

                @Override
                public String getFormula() {
                    return "-(2y + 1) * ctg(x)";
                }
            }), new AnalyticSolutionFunction() {
                @Override
                public void calculateC(double x, double y) {
                    c = 2 * Math.pow(Math.sin(x), 2) * (y + 1./2);
                }

                @Override
                public double get(double x) {
                    return c / (2 * Math.pow(Math.sin(x), 2)) - 1./2;
                }
            }),

            new DifferentialEquationWithAnalyticSolution(new DifferentialEquation(new FunctionTwoArguments() {
                @Override
                public double get(double x, double y) {
                    return 7 * Math.exp(x) - y;
                }

                @Override
                public String getFormula() {
                    return "7e^x - y";
                }
            }), new AnalyticSolutionFunction() {
                @Override
                public void calculateC(double x, double y) {
                    c = (y - 7 * Math.exp(x) / 2) * Math.exp(x);
                }

                @Override
                public double get(double x) {
                    return 7 * Math.exp(x) / 2 + c / Math.exp(x);
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
    private final DifferentialEquation equation;
    private final AnalyticSolutionFunction solutionFunction;

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

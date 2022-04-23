import java.util.ArrayList;

public interface DifferentialEquationSolver {

    ArrayList<Dot> solve(DifferentialEquation equation, double startX, double startY, double solutionLength, double stepLength);
}

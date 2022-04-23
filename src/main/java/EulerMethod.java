import java.util.ArrayList;

public class EulerMethod implements DifferentialEquationSolver{

    @Override
    public ArrayList<Dot> solve(DifferentialEquation equation, double startX, double startY, double solutionLength, double stepLength) {

        ArrayList<Dot> dots = new ArrayList<>();

        int dotAmount = (int)(solutionLength / stepLength);

        double prevX = startX;
        double prevY = startY;

        for (int i = 0; i <= dotAmount; i++) {
            if (i == 0){
                dots.add(new Dot(startX, startY));
                continue;
            }

            double currentX = prevX + stepLength;
            double currentY = prevY + stepLength * equation.getFunction().get(prevX, prevY);
            dots.add(new Dot(currentX, currentY));

            prevX = currentX;
            prevY = currentY;
        }
        return dots;
    }


}

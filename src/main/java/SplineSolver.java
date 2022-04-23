import org.apache.commons.math.linear.*;

import java.util.ArrayList;

public class SplineSolver {

    public ArrayList<Spline> getSplines(ArrayList<Dot> dots) {
        LinearEquationsSystem system = makeLinearEquationSystem(dots);
        LinearEquationsSolutionInfo solutionInfo;

        RealMatrix coefficients = new Array2DRowRealMatrix(system.a, false);
        DecompositionSolver solver = new LUDecompositionImpl(coefficients).getSolver();

        RealVector constants = new ArrayRealVector(system.b, false);
        RealVector solution = solver.solve(constants);

        solutionInfo = new LinearEquationsSolutionInfo(solution.getData());

        return getSplinesFromSolutionInfo(solutionInfo, getSplineSegmentBorders(dots));
    }

    //PRIVATE METHODS
    private LinearEquationsSystem makeLinearEquationSystem(ArrayList<Dot> dots) {
        int dimension = (dots.size() - 1) * 4;
        double[][] a = new double[dimension][dimension];
        double[] b = new double[dimension];
        int offset = 0;
        int row = 0;

        ArrayList<SplineSegmentBorder> list = getSplineSegmentBorders(dots);

        for (int i = 0; i < list.size(); i++){

            SplineSegmentBorder spline = list.get(i);

            if (i == 0) {
                a[row][offset + 2] = 2;
                b[row] = 0;
                row++;
            }

            //Левая граница
            a[row][offset] = 1;           //a
            b[row] = list.get(i).leftDot.getY();  //y
            row++;

            //Правая граница
            //a
            a[row][offset] = 1;
            //b
            a[row][offset + 1] = spline.rightDot.getX() - spline.leftDot.getX();
            //c
            a[row][offset + 2] = Math.pow(spline.rightDot.getX() - spline.leftDot.getX(), 2);
            //d
            a[row][offset + 3] = Math.pow(spline.rightDot.getX() - spline.leftDot.getX(), 3);
            b[row] = spline.rightDot.getY();
            row++;

            if (i < list.size() - 1) {
                SplineSegmentBorder nextSpline = list.get(i + 1);

                //S'
                //b
                a[row][offset + 1] = -1;
                //c
                a[row][offset + 2] = -2 * (spline.rightDot.getX() - spline.leftDot.getX());
                //d
                a[row][offset + 3] = -3 * Math.pow(spline.rightDot.getX() - spline.leftDot.getX(), 2);

                //next b
                a[row][offset + 5] = 1;
                //next c
                a[row][offset + 6] = 2 * (spline.rightDot.getX() - nextSpline.leftDot.getX());
                //next d
                a[row][offset + 7] = 3 * Math.pow(spline.rightDot.getX() - nextSpline.leftDot.getX(), 2);
                row++;

                //S''
                //c
                a[row][offset + 2] = -2;
                //d
                a[row][offset + 3] = -6 * (spline.rightDot.getX() - spline.leftDot.getX());

                //next c
                a[row][offset + 6] = 2;
                //next d
                a[row][offset + 7] = 6 * (spline.rightDot.getX() - nextSpline.leftDot.getX());
                row++;
            }
            if (i == list.size() - 1){
                //c
                a[row][offset + 2] = 2;
                //d
                a[row][offset + 3] = 6 * spline.rightDot.getX() - spline.leftDot.getX();
                b[row] = 0;
                row++;
            }

            offset += 4;
        }

        return new LinearEquationsSystem(dimension, a, b);
    }

    private ArrayList<Spline> getSplinesFromSolutionInfo(LinearEquationsSolutionInfo info, ArrayList<SplineSegmentBorder> splineBorders) {
        ArrayList<Spline> splines = new ArrayList<>();

        for (int i = 0; i < splineBorders.size(); i++) {

            double a = info.getX()[i*4];
            double b = info.getX()[i*4 + 1];
            double c = info.getX()[i*4 + 2];
            double d = info.getX()[i*4 + 3];
            splines.add(new Spline(a, b, c, d, splineBorders.get(i).leftDot.getX(), splineBorders.get(i).rightDot.getX()));
        }

        return splines;
    }

    private static class SplineSegmentBorder {
        private final Dot leftDot;
        private final Dot rightDot;

        public SplineSegmentBorder(Dot leftDot, Dot rightDot) {
            this.leftDot = leftDot;
            this.rightDot = rightDot;
        }

    }

    private ArrayList<SplineSegmentBorder> getSplineSegmentBorders(ArrayList<Dot> dots){
        ArrayList<SplineSegmentBorder> list = new ArrayList<>();
        for (int i = 0; i < dots.size() - 1; i++) {
            list.add(new SplineSegmentBorder(dots.get(i), dots.get(i + 1)));
        }
        return list;
    }
}

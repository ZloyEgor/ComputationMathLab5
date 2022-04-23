public class DifferentialEquation {
    private final FunctionTwoArguments function;

    public DifferentialEquation(FunctionTwoArguments function) {
        this.function = function;
    }

    public FunctionTwoArguments getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return "y' = " + function.getFormula();
    }
}

package ch.sfdr.fractals.fractals;

/**
 * Factory for the various fractal functions. The only place that knows about
 * the different functions.
 * @author D.Ritz
 */
public class FractalFactory
{
	/**
	 * a list of all available functions
	 */
	private static Pair[] FUNCTIONS = {
		get("Mandelbrot", Mandelbrot.class)
	};

	/**
	 * returns a list of all fractal functions names
	 * @return all names
	 */
	public static String[] getFractalFunctionsNames()
	{
		String[] ret = new String[FUNCTIONS.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = FUNCTIONS[i].name;
		return ret;
	}

	/**
	 * returns an instance of the specified fractal function
	 * @param idx index
	 * @return instance of the fractal function
	 * @throws Exception on any kind of error
	 */
	public StepFractalFunction getFractalFunction(int idx)
		throws Exception
	{
		return FUNCTIONS[idx].clazz.newInstance();
	}

	/**
	 * returns an instance of the specified fractal function by name
	 * @param name the name of the function
	 * @return instance of the fractal function or null if not found
	 * @throws Exception on any kind of error
	 */
	public StepFractalFunction getFractalFunction(String name)
		throws Exception
	{
		for (Pair p : FUNCTIONS) {
			if (p.name.equals(name))
				return p.clazz.newInstance();
		}
		return null;
	}


	/**
	 * returns a pair of name/function
	 * @param name
	 * @param clazz
	 * @return
	 */
	private static Pair get(String name, Class<? extends StepFractalFunction> clazz)
	{
		Pair p = new Pair();
		p.name = name;
		p.clazz = clazz;
		return p;
	}

	/**
	 * A pair of a name and a function class
	 */
	private static class Pair
	{
		String name;
		Class<? extends StepFractalFunction> clazz;
	}
}

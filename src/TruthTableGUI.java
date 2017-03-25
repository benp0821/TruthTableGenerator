import java.util.ArrayList;


public class TruthTableGUI {
	ArrayList<EquationVariables> variables = new ArrayList<>();
	ArrayList<Object> equation;
	
	public TruthTableGUI(ArrayList<EquationVariables> vars, ArrayList<Object> theEquation){
		variables = vars;
		equation = theEquation;
	}
	
	/**
	 * The method that constructs the truth table
	 * It goes through every possible binary combination for the variables, and calls parseEquation()
	 * for each one. If parseEquation returns false, the program stops executing
	 */
	public void constructTable(){
		//prints out the top row of the truth table
		for (int i = variables.size()-1; i >= 0 ; i--){
			System.out.print(" | " + variables.get(i).getName());
		}
		System.out.println(" || Output");
		
		//Goes through every possible combination of values for the variables and prints them
		for (int i = 1; i < Math.pow(2, variables.size())+1; i++){
			int size = variables.size();
			//Goes through all of the variables
			for (int m = size-1; m >= 0; m--){
				//if the remainder of i divided by the variables significant bit = 1
				if (i%variables.get(m).getSigBit()==1){
					//set the state
					variables.get(m).setState();
				}
				//if the significant bit = 1, also switch state
				if (variables.get(m).getSigBit() == 1){
					variables.get(m).setState();
				}
				System.out.print(" | " + variables.get(m).getState());
			}
			System.out.print(" || ");
			//attempts to parse the equation for the current variable states.
			//If it fails, end the program
			if (!parseEquation()){
				return;
			}
		}
	}
	
	/**
	 * Goes through the equation and performs the calculations.
	 * @return true if the equation is properly formatted, false otherwise
	 */
	public boolean parseEquation(){
		//Creates a temporary array in which the EquationVariables are replaced with the
		//Integer representing their state, for cleaner code
		ArrayList<Object> temp = new ArrayList<>(equation);
		for (int j = 0; j < temp.size();j++){
			if (temp.get(j).getClass().equals(EquationVariables.class)){
				temp.set(j, ((EquationVariables)temp.get(j)).getState());
			}
		}
		for (int i = 0; i < temp.size(); i++){
			//if the current index in the equation is a Character
			if (temp.get(i).getClass().equals(Character.class)){
				//if the object at position i in the equation is equal to +
				if (temp.get(i).equals('+')){
					//makes sure the plus isn't at the beginning or end of the string
					if (!(i == 0) && !(i == temp.size()-1)){
						//makes sure the objects surrounding the plus are integers
						if (temp.get(i-1).getClass().equals(Integer.class)){
							if (temp.get(i+1).getClass().equals(Integer.class)){
								//OR the values to determine whether the answer should be 0 or 1
								if ((Integer)temp.get(i-1) == 1 || (Integer) temp.get(i+1) == 1){
									temp.set(i+1, 1);
								}else{
									temp.set(i+1, 0);
								}
							//if the object following the plus is a ! instead of an integer
							//The value after the ! is inverted and then the + is applied
							}else if (temp.get(i+1).equals('!')){
								if (temp.get(i+2).getClass().equals(Integer.class)){
									if ((Integer)temp.get(i+2)==0){
										temp.set(i+2, 1);
									}else{
										temp.set(i+2, 0);
									}
									if ((Integer)temp.get(i-1) == 1 || (Integer) temp.get(i+2) == 1){
										temp.set(i+2, 1);
										temp.set(i+1, "");
									}else{
										temp.set(i+2, 0);
										temp.set(i+1, "");
									}
								//Ends the method if anything is wrong with the equation
								}else{
									return fail();
								}
							}else{
								return fail();
							}
						}else {
							return fail();
						}
					}else{
						return fail();
					}
				}
				//if the object at position i in the equation is equal to *
				else if (temp.get(i).equals('*')){
					//makes sure the asterisk isn't at the beginning or end of the string
					if (!(i == 0) && !(i == temp.size()-1)){
						//makes sure the objects surrounding the asterisk are integers
						if (temp.get(i-1).getClass().equals(Integer.class)){
							if (temp.get(i+1).getClass().equals(Integer.class)){
								//AND the values to determine whether the answer should be 0 or 1
								if ((Integer) temp.get(i-1) == 1 && ((Integer) temp.get(i+1)) == 1){
									temp.set(i+1, 1);
								}else{
									temp.set(i+1, 0);
								}
							//if the object following the * is a ! instead of an integer
							//The value after the ! is inverted and then the * is applied
							}else if (temp.get(i+1).equals('!')){
								if (temp.get(i+2).getClass().equals(Integer.class)){
									if ((Integer)temp.get(i+2)==0){
										temp.set(i+2, 1);
									}else{
										temp.set(i+2, 0);
									}
									if ((Integer)temp.get(i-1) == 1 && (Integer) temp.get(i+2) == 1){
										temp.set(i+2, 1);
										temp.set(i+1, "");
									}else{
										temp.set(i+2, 0);
										temp.set(i+1, "");
									}
								//Ends the method if anything is wrong with the equation
								}else{
									return fail();
								}
							}else{
								return fail();
							}
						}else {
							return fail();
						}
					}else{
						return fail();
					}
				//if the current index is an ! (such as when it is the first character)
				//The value after the ! is inverted
				}else if (temp.get(i).equals('!')){
					if (temp.get(i+1).getClass().equals(Integer.class)){
						if ((Integer)temp.get(i+1)==0){
							temp.set(i+1, 1);
						}else{
							temp.set(i+1, 0);
						}
					}else{
						return fail();
					}
				}else {
					return fail();
				}
			//If two integers are next to each other, end the program for an improper equation
			}else if (temp.get(i).getClass().equals(Integer.class) && i < temp.size()-1){
				if (temp.get(i+1).getClass().equals(Integer.class)){
					return fail();
				}
			}
		}
		//prints out the solution in the last column of the truth table
		System.out.println(temp.get(temp.size()-1));
		return true;
	}
	
	/*
	 * This method prints an error method and returns false to parseEquation(), ending the program
	 */
	private boolean fail(){
		System.out.println();
		System.out.println("Invalid String");
		return false;
	}
}


public class CellLoc {

	int rowLoc;
	int columnLoc;
	
	
public CellLoc(String cell) {
	
	String temp = cell;
	
	String tempUpperCase = temp.toUpperCase();
	
	rowLoc = (int)tempUpperCase.charAt(0) - 65;
	columnLoc = Integer.parseInt(cell.substring(1)) - 1;
	
}

public CellLoc(int row, int column) {
	rowLoc = row;
	columnLoc = column;
}

public String getFormula(Cell[][] sheetValues) {
	
	return sheetValues[rowLoc][columnLoc].formula;
}

public String getFinalValue(Cell[][] sheetValues) {
	
	return sheetValues[rowLoc][columnLoc].evaluateCell(sheetValues);
}

public Cell[][] AssignFormula(String assignment, Cell[][] sheetValues) {
	
	try {
	sheetValues[rowLoc][columnLoc].formula = assignment;
	} catch (ArrayIndexOutOfBoundsException e) {
		
		System.out.println("The formula location you specified does not exist.");
	}
	return sheetValues;
}

public Cell[][] AssignFinalValue(String assignment, Cell[][]sheetValues) {
	
	if (assignment.equals("0")) {
		
		assignment = "0.0";
	}
	
	try {
		
	sheetValues[rowLoc][columnLoc].finalVal = assignment;
	
	} catch (ArrayIndexOutOfBoundsException e) {
		
		System.out.println("The value location you specified does not exist.");
	}
	return sheetValues;
}

public String getCellName (Cell[][]sheetValues) {
	char rowname = (char)(rowLoc+65);
	String column = ""+(columnLoc+1);
	
	return ""+rowname+column;
}


}

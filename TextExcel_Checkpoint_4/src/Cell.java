
public class Cell {
	
	String formula;
	String finalVal;
	
public Cell(String newformula, String newvalue) {
	
	formula = newformula;
	finalVal = newvalue;
}
	
public void printValue(int row, int column, Cell[][] sheetValues) {
	
	String s = sheetValues[row][column].evaluateCell(sheetValues);
	
	if (s.length() < 10) {
		while (s.length() < 10) {
			s += " ";
		}
	}
	
	if (s.length() > 10) {
		s = s.substring(0,9) + ">";
				
	}
	
	System.out.print(s);
}

public String returnPrintValue(int row, int column, Cell[][] sheetValues) { 
	
String s = sheetValues[row][column].evaluateCell(sheetValues);
	
	if (s.length() < 10) {
		while (s.length() < 10) {
			s += " ";
		}
	}
	
	if (s.length() > 10) {
		s = s.substring(0,9) + ">";
				
	}
	
	return s;
	
}
public String evaluateCell(Cell[][] sheetValues) {
	
	if (formula.equals("")) {
		
		return finalVal;
		
	} else {
		
		finalVal = ""+evaluateFormula(formula,sheetValues);
		return finalVal;
	}
	
	
	
}

public double evaluateFormula(String formula,Cell[][] sheetValues) {
	
	double solution = 0.0;
	
	if (formula.contains("sum") || formula.contains("avg")) {
		
		Cell[] range = getCellRange(formula.substring(4,9),sheetValues);
		
		
			double finalDouble = 0.0;
		
			int count = 0;
			
			double current = 1.0;
			
			
			while (count < range.length) {
				
				
				try {
					
				current = Double.parseDouble(range[count].evaluateCell(sheetValues));
				
				finalDouble += current;
				
				} catch (NumberFormatException e) {
					
					finalDouble += 0.0;
				}
				
				count++;
				
			}
			
			if (formula.contains("avg")) {
				
				finalDouble = finalDouble / range.length;
			}
			
			solution = finalDouble;
			
		
			
	} else {
	String[] pieces = formula.split(" ");
	
	int subjectloc = 0;
	
	double[] operands = new double[(pieces.length/2)+1];
	
	int arrayloc = 0;
	
	while (subjectloc < pieces.length) {
			
			if (isCell(pieces[subjectloc])) {
				
				CellLoc current = new CellLoc(pieces[subjectloc]);
				
				String cellvalue = sheetValues[current.rowLoc][current.columnLoc].evaluateCell(sheetValues);
				
				pieces[subjectloc] = cellvalue;
				
				operands[arrayloc] = Double.parseDouble(pieces[subjectloc]);
				arrayloc++;
				
				subjectloc += 2;
				
			} else {
			
			
			double parsed = parseValue(pieces[subjectloc]);
			
			operands[arrayloc] = parsed;
			
			arrayloc++;
		
		
		subjectloc += 2;
	}
			
	}
	subjectloc = 1;
	
	
	solution = 0.0;
	
	int count = 1;
	
	solution += operands[0];
	
	while (subjectloc < pieces.length) {
		
		String currentOp = pieces[subjectloc];
		
		if (currentOp.contains("+")) {
			
			solution += operands[count];
			
		} else if (currentOp.contains("-")) {
			
			solution = solution - operands[count];
			
		} else if (currentOp.contains("*")) {
			
			solution = solution * operands[count];
			
		} else if (currentOp.contains("/")) {
			
			solution = solution / operands[count];
			
		}
		
		subjectloc += 2;
		
		count++;
	}
	}
	
	
	return solution;
}

public void clearCell() {
	formula = "";
	finalVal = "";
}

public static double parseValue(String string) {
	
	double newValue = 0.0;
	
	if (string.contains(".")) {
		
		newValue = Double.parseDouble(string);
		
	} else {
		newValue = (double)(Integer.parseInt(string));
	}
	
	return newValue;
}

public static boolean isCell(String candidate) {
	boolean isCell = true;
	
	String fixedcandidate = candidate.toUpperCase();
	
	if (fixedcandidate.length() != 2) {
		
		isCell = false;
		
	} else if ((int)(fixedcandidate.charAt(0)) > 72 || (int)(fixedcandidate.charAt(0)) < 65) {
		
		isCell = false;
		
	} else if (Integer.parseInt(""+fixedcandidate.charAt(1)) < 1 || Integer.parseInt(""+fixedcandidate.charAt(1)) > 8) {
		
		isCell = false;
		
	}
	
	return isCell;
}

public static Cell[] getCellRange(String rangecall, Cell[][] sheetValues) {
	
	CellLoc rangeStart = new CellLoc(rangecall.substring(0,2));
	CellLoc rangeEnd = new CellLoc (rangecall.substring(3));

	int rangelength = 0;
	
	
	if (rangeStart.rowLoc == rangeEnd.rowLoc) {
		
		rangelength = rangeEnd.columnLoc - rangeStart.columnLoc + 1;
		
		} else {
	
			rangelength = ((rangeEnd.rowLoc - rangeStart.rowLoc)+1) * ((rangeEnd.columnLoc - rangeStart.columnLoc)+1);
		}
	
	
	Cell[] range = new Cell[rangelength];
	
	int row = rangeStart.rowLoc;
	int column = rangeStart.columnLoc;
	int count = 0;
	
	while (row <= rangeEnd.rowLoc && count < range.length) {
		
		while (column <= rangeEnd.columnLoc && count < range.length) {
			
				range[count] = sheetValues[row][column];
			
			column++;
			
			count++;
		}
		row++;
		column = rangeStart.columnLoc;
	}
	
	return range;
	
}


}
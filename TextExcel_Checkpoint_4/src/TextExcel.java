import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class TextExcel {

		//main method
		public static void main(String[] args) {
			
			Scanner scan = new Scanner(System.in);
			
			//definition of spreadsheet array
			Cell[][] sheetValues = new Cell[8][8];
			
			int row = 0;
			int column = 0;
			
			//initial setting of cell values to be empty
			while (row < 8) {
				
				while (column < 8) {
					
					sheetValues[row][column] = new Cell("","");
					
					column++;
				}
				row++;
				column = 0;
			}
			
			printSpreadSheet(sheetValues);
			
			//ask for input
			String input = scan.nextLine();
			
			
			//create a while loop that handles "quit"
			while (!input.equalsIgnoreCase("QUIT")) {
				
				if (CorrectSyntax(input)) {
					
					
				//handles clearing cells and the spreadsheet
				if (input.contains("clear")) {
					
					if (input.length() == 5) {
						
						System.out.println("Are you sure you want to clear your entire spreadsheet?");
						
						String decision = scan.nextLine();
						
						if (decision.equalsIgnoreCase("YES")) {
						
						sheetValues = clearSpreadSheet(sheetValues);
						
						}
						
					} else {
						CellLoc toBeCleared= new CellLoc(input.substring(5,7));
						
						sheetValues[toBeCleared.rowLoc][toBeCleared.columnLoc].clearCell();
					}
				} else if (input.length() == 6 && input.contains("cell")) {
					//handles cell calls 
					
					CellLoc view = new CellLoc(input.substring(4));
					
					System.out.print(""+view.getCellName(sheetValues)+" = "+view.getFinalValue(sheetValues)+" ("+view.getFormula(sheetValues)+")");
					System.out.println();
					
				} else if (input.substring(0,4).equalsIgnoreCase("SAVE")) {
					//handles saving files
					
					String fileLoc = input.substring(input.indexOf(' ')+1);
					
					//set up a new output file
					File document = new File(fileLoc);
					
					PrintStream output;
					
					
					try {
						 output = new PrintStream(document);
						 
						//write the spreadsheet into the document	
							int saverow = 0;
							int savecolumn = 0;
							
							
							output.println("------------------------------------------------------------------------------------------");
							output.println("|    1     |    2     |    3     |    4     |    5     |    6     |    7     |    8     |");
							output.println("------------------------------------------------------------------------------------------");
							
							
							while (saverow < sheetValues.length) {
								
								output.print("|");
								
								while (savecolumn < sheetValues[saverow].length) {
									
									String toBePrinted = sheetValues[saverow][savecolumn].returnPrintValue(saverow,savecolumn,sheetValues);
									
									output.print(toBePrinted);
									output.print("|");
									
									savecolumn++;
								}
								
								saverow++;
								savecolumn = 0;
								
								output.println();
							}
							
							
							saverow = 0;
							savecolumn = 0;
							
							output.println("Formulas:");
							
							while (saverow < sheetValues.length) {
								
								while (savecolumn < sheetValues[saverow].length) {
									
									if (!sheetValues[saverow][savecolumn].formula.equals("")) {
										
										CellLoc hasformula = new CellLoc(saverow,savecolumn);
										
										output.println(hasformula.getCellName(sheetValues)+": "+hasformula.getFormula(sheetValues));
									} 
									
									savecolumn++;
								}
								
								saverow++;
								
								savecolumn = 0;
							}
							
							saverow = 0;
							savecolumn = 0;
							
							output.println("Truncated Cells:");
							
							while (saverow < sheetValues.length) {
								
								while (savecolumn < sheetValues[saverow].length) {
									
									String tested = sheetValues[saverow][savecolumn].evaluateCell(sheetValues);
									
									if (tested.length() > 10) {
										
										CellLoc haslongvalue = new CellLoc(saverow,savecolumn);
										
										output.println(haslongvalue.getCellName(sheetValues)+": "+haslongvalue.getFinalValue(sheetValues));
										
									}
									
									savecolumn++;
								}
								
								saverow++;
								savecolumn = 0;
							}
							
							//close file
							output.close();
							
					} catch (FileNotFoundException e) {
						
						System.out.println("Error - the file could not be saved.");
						
					}
					
					
				} else if (input.substring(0,4).equalsIgnoreCase("LOAD")) {
					//handles loading files
					
					//open a pre-existing file
					File loadLoc = new File(input.substring(input.indexOf(' ')+1));
					
					Scanner newInput;
					
					
					try {
						
						newInput = new Scanner(loadLoc);
						
						System.out.println("Are you sure that you want to overwrite your current spreadsheet?");
						
						String answer = scan.nextLine();
						
						if (answer.equalsIgnoreCase("NO")) {
							
							newInput.close();
							
						} else if (answer.equalsIgnoreCase("YES")) {
							
							int rowNum = 0;
							
							
							boolean truncatedCellsPassed = false;
							//tells if the queue to find truncated formulas has passed
							
							boolean formulasPassed = false;
							//tells if the queue to find spreadsheet values has passed
							
							while (newInput.hasNext()) {
								
								String currentline = newInput.nextLine();
								
								if (currentline.contains("Formulas:")) {
									
									formulasPassed = true;
								}
								
								if (currentline.contains("Truncated Cells:")) {
									
									truncatedCellsPassed = true;
								}
								
								
								int charchecker = 0;
								int count = 0;
								
								if (formulasPassed == false  && !currentline.contains("|    1     |") && !currentline.contains("-------")) {
									
									while (charchecker < currentline.length()) {
										
										if (charchecker != currentline.length()-1) {
											
											sheetValues[rowNum][count].finalVal = currentline.substring(charchecker+1,currentline.indexOf("|",charchecker+1));
											
											if (sheetValues[rowNum][count].finalVal.equals("          ")) {
												
												sheetValues[rowNum][count].finalVal = "";
												
											}
											count++;
											
										}
										charchecker+=11;
									}
											
									rowNum++;
									
								}
								
								boolean isCell = isCell(currentline.substring(0,2));
								
								if (isCell) {
									
									CellLoc cellAssign = new CellLoc(currentline.substring(0,2));
									
									if (truncatedCellsPassed == true) {
										
										cellAssign.AssignFinalValue(currentline.substring(4), sheetValues);
										
									} else {
										
										cellAssign.AssignFormula(currentline.substring(4), sheetValues);
									} 
								}
								
							}
							
						}
						
						newInput.close();
						
					} catch (FileNotFoundException e) {
						
						System.out.println("Error - the file could not be loaded.");
						
						
					}
					
			
				} else if (input.substring(0,4).equalsIgnoreCase("SORT")) {
					//handles sorting
					
					//get the cells in the called range and put them in an array
					Cell[] sortees = getCellRange(input.substring(6,11), sheetValues);
					
					
					//sort the cells in the array based on final value
					int sortcount = 0;
					int start = 0;
					
					boolean validRange = true;
					
					while (sortcount < sortees.length && validRange == true) {
						
					int candidate = start;
					
					for (int index = start + 1; index < sortees.length && validRange == true; index++) {
						
						try {
							
							double piece1 = 0.0;
							
							double piece2 = 0.0;
							
							
							piece1 = parseValue(sortees[index].evaluateCell(sheetValues));
							
							piece2 = parseValue(sortees[candidate].evaluateCell(sheetValues));
							
							if (piece1 < piece2) {
								
								candidate = index;
								
							}
							
						
					
						} catch (NumberFormatException e) {
							
							System.out.println("Error:  Ranges cannot contain strings or empty cells.");
							
							validRange = false;
							
						}
					
					}	
						
					
						Cell temp = sortees[start];
						sortees[start] = sortees[candidate];
						sortees[candidate] = temp;
					
						sortcount++;
						start++;
					}
					
					//put the cell values back into the spreadsheet
					
					if (validRange) {
						
					
					char sortdecider = input.charAt(4);
					
					sheetValues = returnRange(input.substring(6,11), sortdecider, sortees, sheetValues);
					
					}
					
					
				} else if (input.contains("=")) {
					//handles assignments
					
					
					CellLoc toBeAssigned = new CellLoc(input.substring(0,2));
					
					if (hasOperator(input)) {
						String formula = input.substring(input.indexOf('=')+2);
						
						
						
						String[] pieces = formula.split(" ");
						
						int arrayLoc = 0;
						int operandstracker = 0;
						
						double[] operands = new double[(pieces.length/2) + 1];
						
						
						boolean validEquation = true;
						
						while (arrayLoc < pieces.length && validEquation == true) {
							
							if (arrayLoc % 2 == 0) {
								
							try {
								
								operands[operandstracker] = parseValue(pieces[arrayLoc]);
								
							} catch (NumberFormatException e) {
								
								if (isCell(pieces[arrayLoc])) {
									
									CellLoc cellOperand = new CellLoc(pieces[arrayLoc]);
									
									try {
										
									
										String operandString = sheetValues[cellOperand.rowLoc][cellOperand.columnLoc].evaluateCell(sheetValues);
									
										try {
											
											operands[operandstracker] = Double.parseDouble(operandString);
									
									} catch (NumberFormatException f) {
										
										System.out.println("Error: Strings and empty cells cannot be included in arithmetic operations.");
										
										validEquation = false;
									}
										
									} catch (ArrayIndexOutOfBoundsException g) {
										
										System.out.println("Error: An invalid cell location was called.");
										
										validEquation = false;
									}
									
									
								} else {
									
									validEquation = false;
									
									System.out.println("The assignment you entered cannot be evaluated.");
									
								}
								
							}
							operandstracker++;
						}
							arrayLoc += 2;
						}
						
						if (validEquation) {
							
							toBeAssigned.AssignFormula(formula, sheetValues);
							toBeAssigned.AssignFinalValue("",sheetValues);
						}
						
					} else if (input.contains("sum") || input.contains("avg")) {
						
						String formula = input.substring(input.indexOf("=")+2);
						
						
						toBeAssigned.AssignFinalValue("", sheetValues);
						
						toBeAssigned.AssignFormula(formula, sheetValues);
						
					} else {
						
						toBeAssigned.AssignFinalValue(input.substring(input.indexOf('=')+2), sheetValues);
						
						toBeAssigned.AssignFormula("", sheetValues);
					}
				}
				
				
				
				
				
			} else {
				
				System.out.println("Error: incorrect syntax");
			}
				
				sheetValues = FixNulls(sheetValues);
				
				printSpreadSheet(sheetValues);
				
				//new input
				input = scan.nextLine();
			}
			
			
			System.out.println("Goodbye!");
			
			scan.close();
		}
		 
		
	
//prints the spreadsheet
public static void printSpreadSheet(Cell[][] sheetFormulas) {
	
	System.out.println("------------------------------------------------------------------------------------------------");
	System.out.println("|      |    1     |    2     |    3     |    4     |    5     |    6     |    7     |    8     |");
	System.out.println("------------------------------------------------------------------------------------------------");
	
	int row = 0;
	int column = 0;
	int ascii = 65;
	while (row < 8) {
		System.out.print("||  "+(char)(ascii)+" ||");
		while (column < 8) {
			sheetFormulas[row][column].printValue(row,column,sheetFormulas);
			System.out.print("|");
			column++;
		}
		
		System.out.println();
		System.out.println("------------------------------------------------------------------------------------------------");
		row++;
		column = 0;
		ascii++;
	}
	
	System.out.println("Change?");
}

//changes null spreadsheet values into spaces
public static Cell[][] FixNulls(Cell[][] sheetValues) {
	
	int row = 0;
	int column = 0;
	
	while (row < sheetValues.length) {
		
		while (column < sheetValues[row].length) {
		
			if (sheetValues[row][column].formula == null) {
				sheetValues[row][column].formula = " ";
			}
			column++;
		}
		row++;
		column = 0;
	}
	
	return sheetValues;
}

//clears the whole spreadsheet
public static Cell[][] clearSpreadSheet(Cell[][] sheetValues) {
	
	int row = 0;
	int column = 0;
	
	while (row < sheetValues.length) {
		
		while (column < sheetValues[row].length) {
			sheetValues[row][column].formula = "";
			sheetValues[row][column].finalVal = "";
			
			column++;
		}
		
		row++;
		column = 0;
	}
	
	return sheetValues;
}

//tests to see if a string contains an equation operator
public static boolean hasOperator(String s) {
	
	boolean hasOperator = false;
	if (s.contains(" + ") || s.contains(" - ") || s.contains(" * ") || s.contains(" / ")) {
		
		hasOperator = true;
	} else {
		
		hasOperator = false;
	}
	
	return hasOperator;
}

//parses a value
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
		
	}
	
	int columntest = 0;
	
	try {
		columntest = Integer.parseInt(""+candidate.charAt(1));
		
	} catch (NumberFormatException e) {
		
		isCell = false;
	}
	
	if (columntest > 8 || columntest < 1) {
		
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

public static Cell[][] returnRange (String rangecall, char sortdecider,Cell[] sortees, Cell[][] sheetValues) {
	
	CellLoc rangeStart = new CellLoc(rangecall.substring(0,2));
	CellLoc rangeEnd = new CellLoc (rangecall.substring(3));
	
	int row = rangeStart.rowLoc;
	int column = rangeStart.columnLoc;
	
	if (sortdecider == 'a') {
		
		int count = 0;
		
		while (row <= rangeEnd.rowLoc && count < sortees.length) {
			
			while (column <= rangeEnd.columnLoc && count < sortees.length) {
				
				sheetValues[row][column] = sortees[count];
				
				count++;
				
				column++;
			}
			row++;
			
			column = rangeStart.columnLoc;
		}
	} else if (sortdecider == 'd') {
		
		int count = sortees.length-1;
		
		while (row <= rangeEnd.rowLoc) {
			
			while (column <= rangeEnd.columnLoc) {
				sheetValues[row][column] = sortees[count];
				
				count--;
				
				column++;
			}
			row++;
			
			column = rangeStart.columnLoc;
		}
	}
	
	return sheetValues;
	
}



//method that determines if input has correct syntax
public static boolean CorrectSyntax (String input) {
	
	
	boolean correctSyntax = true;
	
	
	
	if (input.length() < 5) {
		
		correctSyntax = false;
	} else if (input.length() < 6) {
		
		if (!input.equals("clear")) {
			
			correctSyntax = false;
		}
		
	} else {
	
	
		if (input.substring(0,5).equals("clear")) {
		
		
		if (input.length() > 5) {
			
			if (isCell(input.substring(5,7)) == false) {
				
				correctSyntax = false;
				
			}
			
		} 
			
			
		
		
	}  else if (input.contains("=")) {
		
		if (input.indexOf('=') != 3) {
			
			correctSyntax = false;
		}
		
		if (isCell(input.substring(0,2)) == false) {
			
			correctSyntax = false;
		}
		
		if (input.charAt(input.indexOf('=')+1) != ' ' || input.charAt(input.indexOf('=')-1) != ' ') {
			
			correctSyntax = false;
		}
		
		
			
			if (input.contains(" + ")|| input.contains(" - ")||input.contains(" * ")||input.contains(" / ")) {
				
				int charchecker = 0;
				
				while (charchecker < input.length()) {
					
					char current = input.charAt(charchecker);
					
					if (current=='+'||current=='-'||current=='*'||current=='/') {
						
						if ( charchecker+1 > input.length()-1 || input.charAt(charchecker+1)!=' ') {
							
							correctSyntax = false;
						}
						
						if (input.charAt(charchecker-1)!=' ') {
							
							correctSyntax = false;
						}
						
						if (charchecker+2 > input.length()-1 || (input.charAt(charchecker+2) == ' ')) {
							
							
							correctSyntax = false;
						}
						
						if (input.charAt(charchecker-2) == ' ' || input.charAt(charchecker-2) == '=') {
							
							correctSyntax = false;
						}
					}
					
					charchecker++;
				}
			}
			
			if (input.contains("sum") || input.contains("avg")) {
				
				
				
				if (input.length() != 15) {
					
					correctSyntax = false;
					
				} else if (input.charAt(8) != '(' || input.charAt(14) != ')') {
					
					correctSyntax = false;
					
				} else if (input.charAt(11) != '-') {
					
					correctSyntax = false;
					
				} else if (isCell(input.substring(9,11)) == false || isCell(input.substring(12,14)) == false) {
					
					correctSyntax = false;
				
				} else {
					
					String tempinput = input.toUpperCase();
					
					int rowOne = (int)tempinput.charAt(9);
					int rowTwo = (int)tempinput.charAt(12);
					
					int columnOne = Integer.parseInt(tempinput.substring(10,11));
					int columnTwo = Integer.parseInt(tempinput.substring(13,14));
					
					if (rowTwo < rowOne || columnTwo < columnOne) {
						
						correctSyntax = false;
					}
					
					if (rowOne == rowTwo && columnOne == columnTwo) {
						
						correctSyntax = false;
					}
					
				}
			}
		
	} else {
	
		
	 if (input.substring(0,4).equals("save")) {
		
		 if (input.substring(input.length()-2) == "  ") {
			 
			 correctSyntax = false;
		 }
		 
		if (input.equals("save ")) {
			
			correctSyntax = false;
			
		} else if (input.length() > 5 && input.charAt(4) != ' ') {
			
			correctSyntax = false;
		}
		
	} else if (input.substring(0,4).equals("load")) {
		
		if (input.substring(input.length()-2) == "  ") {
			
			correctSyntax = false;
		}
		
		if (input.equals("load ")) {
			
			correctSyntax = false;
			
		} else if (input.length() > 5 && input.charAt(4) != ' ') {
			
			correctSyntax = false;
		}
		
	} else if (input.substring(0,4).equals("sort")) {
		
		 if (input.length() != 12) {
			
			correctSyntax = false;
			
		} else if (input.charAt(4) != 'a' && input.charAt(4) != 'd') {
			
			correctSyntax = false;
			
		} else if (input.charAt(5) != '(' || input.charAt(11) != ')') {
				
			correctSyntax = false;
			
		} else if (input.charAt(8) != '-') {
			
			correctSyntax = false;
			
		} else if (isCell(input.substring(6,8)) == false || isCell(input.substring(9,11)) == false) {
			
			correctSyntax = false;
		
		} else {
			
			String tempinput = input.toUpperCase();
			
			int rowOne = tempinput.charAt(6);
			int rowTwo = tempinput.charAt(9);
			
			int columnOne = Integer.parseInt(tempinput.substring(7,8));
			int columnTwo = Integer.parseInt(tempinput.substring(10,11));
			
			if (rowTwo < rowOne || columnTwo < columnOne) {
				
				correctSyntax = false;
			}
			
			if (rowOne == rowTwo && columnOne == columnTwo) {
				
				correctSyntax = false;
			}
		
		}
		
		
	} else if (input.substring(0,4).equals("cell")) {
		
		if (input.length() !=  6) {
			
			correctSyntax = false;
		
		} else if (isCell(input.substring(4)) == false) {
			
			correctSyntax = false;
		}
		
	} else {
		
		correctSyntax = false;
	}
	 
	}
	}
	
	
	return correctSyntax;
}
}
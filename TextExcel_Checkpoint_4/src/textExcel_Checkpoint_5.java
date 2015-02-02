import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
public class textExcel_Checkpoint_5 {

		public static void main(String[] args) {
			Scanner scan = new Scanner(System.in);
			
			//two-dimensional array that will hold all the values for the spreadsheet
			String[][] sheetValues = new String[8][8];
			
			//nested while loop that turns all null values of sheetValues into a space
			int columncount = 0;
			int rowcount = 0;
			
			while (rowcount < sheetValues.length) {
				
				while (columncount < sheetValues[rowcount].length) {
					
					if (sheetValues[rowcount][columncount] == null) {
						sheetValues[rowcount][columncount] = "";
					}
					columncount++;
				}
				rowcount++;
				columncount = 0;
			}
			printSpreadSheet(sheetValues);
			
			String input = scan.nextLine();
			
			//while loop that handles quit
			while (!input.equalsIgnoreCase("quit")) {
				
				String sorttester = input.substring(0,4);
				/*figure out what type of command has been entered and
				call the appropriate method*/
				if (input.equalsIgnoreCase("CLEAR")) {
					//clears entire spreadsheet
					clearSpreadSheet(sheetValues);
					
				}else if (input.substring(0,4).equalsIgnoreCase("SAVE")){
				
					String fileLoc = input.substring(input.indexOf(' ')+1);
					
					//set up a new output file
					File document = new File(fileLoc);
					
					PrintStream output;
					
					try {
						output = new PrintStream(document);
					} catch (FileNotFoundException e) {
						System.out.println("Error - the file could not be saved.");
						return;
					}
					
					//write the spreadsheet into the document
					int row = 0;
					int column = 0;
					
					output.println("------------------------------------------------------------------------------------------");
					output.println("|    1     |    2     |    3     |    4     |    5     |    6     |    7     |    8     |");
					output.println("------------------------------------------------------------------------------------------");
					
					while (row <= 7) {
						output.print("|");
						while (column <= 7) {
							output.print(sheetValues[row][column]+"|");
							column++;
						}
						row++;
						column = 0;
						output.println();
					}
					
					//close file
					output.close();
					
				} else if (input.substring(0,4).equalsIgnoreCase("LOAD")) {
					
					//open a pre-existing file
					File loadLoc = new File(input.substring(input.indexOf(' ')+1));
					Scanner newInput;
					try {
						newInput = new Scanner(loadLoc);
					} catch (FileNotFoundException e) {
						System.out.println("Error - the file could not be loaded.");
						return;
					}
					System.out.println("Are you sure that you want to overwrite your current spreadsheet?");
					String answer = scan.nextLine();
					if (answer.equalsIgnoreCase("NO")) {
						newInput.close();
					} else if (answer.equalsIgnoreCase("YES")) {
						
					//read variable assignments
						int rowNum = 0;
						
					while (newInput.hasNext()) {
						
						String currentLine = newInput.nextLine();
						
						int charchecker = 0;
						
						boolean spreadsheetLine = false;
						
						while (charchecker < currentLine.length() && spreadsheetLine == false) {
							int ascii = (int)currentLine.charAt(charchecker);
							if (ascii == 32 || ascii == 124 || ascii >= 49 && ascii <= 56 || ascii == 45) {
								spreadsheetLine = false;
							} else {
								spreadsheetLine = true;
							}
							
							charchecker++;
						}
						
						if (spreadsheetLine) {
							
							String fixedcurrentLine = currentLine.substring(0,currentLine.length()-1);
							
							String[] currentLineCells = new String[8];
							
							int cellfinder = 0;
							int count = 0;
							while (cellfinder <= fixedcurrentLine.length()-1) {
								if (fixedcurrentLine.charAt(cellfinder)=='|') {
									if (fixedcurrentLine.indexOf('|',cellfinder+1) == -1) {
									currentLineCells[count] = fixedcurrentLine.substring(cellfinder+1);
									count++;
								} else {
									currentLineCells[count] = fixedcurrentLine.substring(cellfinder+1,fixedcurrentLine.indexOf('|',cellfinder+1));
									count++;
								}
								
							}
								cellfinder++;
							}
							int arraytracker = 0;
							
							while (arraytracker <= 7) {
								sheetValues[rowNum][arraytracker] = currentLineCells[arraytracker];
								arraytracker++;
							}
							rowNum++;
						}
					}
					
					//close file
					newInput.close();
					}
				} else if (sorttester.equalsIgnoreCase("sort")) {
					
					if (input.substring(4,5).equalsIgnoreCase("a")) {
						
						//organizes values within the range into descending order
						Cell sortStart = new Cell(input,6,7);
						Cell sortEnd = new Cell(input,9,10);
						
						//finds the amount of cells in the inputed range
						int arraylength = (sortEnd.column-sortStart.column)+1;
						
						//create an array that contains the double equivalents of the range of cells
						int rowLoc = sortStart.row;
						int columnLoc = sortStart.column;
						System.out.println(rowLoc+" "+columnLoc);
						System.out.println();
						
						int count = 0;
						
						double[] sortArray = new double[arraylength];
						
						
						while (columnLoc <= sortEnd.column) {
							
						
							String current = sheetValues[rowLoc][columnLoc];
							
							System.out.println(current);
							System.out.println();
							
							if (current.indexOf(' ') != -1) {
								current = current.substring(0,current.indexOf(' '));
							}
							
							if (current.indexOf('.') == -1) {
								
								sortArray[count] = (double)Integer.parseInt(current);
								
							} else {
								
								sortArray[count] = Double.parseDouble(current);
								
							}
							
							columnLoc++;
							count++;
						}
						
						//sort the array values in descending order
							int sortcount = 0;
							int start = 0;
							while (sortcount < sortArray.length) {
								
							int candidate = start;
							
							for (int index = start + 1; index < sortArray.length; index++) {
								
								if (sortArray[index] < sortArray[candidate]) {
									candidate = index;
								}
							}
								double temp = sortArray[start];
								sortArray[start] = sortArray[candidate];
								sortArray[candidate] = temp;
								
								System.out.println(sortArray[start]);
							
								
								sortcount++;
								start++;
							}
							
							
						
				
					} else {
					//organizes values within the range into descending order
					Cell sortStart = new Cell(input,6,7);
					Cell sortEnd = new Cell(input,9,10);
					
					//finds the amount of cells in the inputed range
					int arraylength = (sortEnd.column-sortStart.column)+1;
					
					//create an array that contains the double equivalents of the range of cells
					int rowLoc = sortStart.row;
					int columnLoc = sortStart.column;
					System.out.println(rowLoc+" "+columnLoc);
					System.out.println();
					
					int count = 0;
					
					double[] sortArray = new double[arraylength];
					
					
					while (columnLoc <= sortEnd.column) {
						
					
						String current = sheetValues[rowLoc][columnLoc];
						
						System.out.println(current);
						System.out.println();
						
						if (current.indexOf(' ') != -1) {
							current = current.substring(0,current.indexOf(' '));
						}
						
						if (current.indexOf('.') == -1) {
							
							sortArray[count] = (double)Integer.parseInt(current);
							
						} else {
							
							sortArray[count] = Double.parseDouble(current);
							
						}
						
						columnLoc++;
						count++;
					}
					
					//sort the array values in descending order
						int sortcount = 0;
						int start = 0;
						while (sortcount < sortArray.length) {
							
						int candidate = start;
						
						for (int index = start + 1; index < sortArray.length; index++) {
							
							if (sortArray[index] > sortArray[candidate]) {
								candidate = index;
							}
						}
							double temp = sortArray[start];
							sortArray[start] = sortArray[candidate];
							sortArray[candidate] = temp;
							
							System.out.println(sortArray[start]);
						
							
							sortcount++;
							start++;
						}
						
						
					
					}
				} else if (input.indexOf('=') == -1 && !input.substring(0,4).equalsIgnoreCase("sort")) {
					//clears a single cell
					Cell toBeCleared = new Cell(input,5,6);
					
					sheetValues[toBeCleared.row][toBeCleared.column] = null;
					
				} else {
					
					//set up the cell that is being changed
					Cell toBeAssigned = new Cell(input,0,1);
					
					//determine if an equation execution is being attempted
					if (input.contains(" + ") || input.contains(" - ") || input.contains(" * ") || input.contains(" / ")) {
						String expression = input.substring(input.indexOf("=")+2);
						
						//define the two operands
						String firsthalf = expression.substring(0,expression.indexOf(' '));
						String secondhalf = expression.substring(expression.indexOf(' ',expression.indexOf(' ')+1)+1);
						char operator = expression.charAt(expression.indexOf(' ')+1);
						
						//convert any cell calls to strings
						if ((int)firsthalf.charAt(0)>=65 && (int)firsthalf.charAt(0)<=72 && (int)firsthalf.charAt(1)>=49 && (int)firsthalf.charAt(1)<=56) {
							Cell firsthalfCell = new Cell(expression,0,1);
							firsthalf = sheetValues[firsthalfCell.row][firsthalfCell.column];
						}
						
						if ((int)secondhalf.charAt(0)>=65 && (int)secondhalf.charAt(0)<=72 && (int)secondhalf.charAt(1)>=49 && (int)secondhalf.charAt(1)<=56) {
							int Idx = expression.indexOf(' ',expression.indexOf(' ')+2)+1;
							
							Cell secondhalfCell = new Cell(expression,Idx,Idx+1);
							secondhalf = sheetValues[secondhalfCell.row][secondhalfCell.column];
						}
						
						//remove any extra spaces at the end of the operand strings
						if (firsthalf.contains(" ")) {
							firsthalf = firsthalf.substring(0,firsthalf.indexOf(' '));
						}
						if (secondhalf.contains(" ")) {
							secondhalf = secondhalf.substring(0,secondhalf.indexOf(' '));
						}
						
						//parse the integers/doubles
						double firstNum = 0.0;
						double secondNum = 0.0;
						
						
						//create a boolean variable that tells whether the operation can be performed or not
						boolean valid = true;
						
						if (firsthalf.contains(".")) {
							try {
								firstNum = Double.parseDouble(firsthalf);
							} catch (NumberFormatException e) {
								System.out.println("Error: The requested action could not be completed (invalid component(s)).");
								valid = false;
							}
							
						} else {
							
							try {
								firstNum = (double)Integer.parseInt(firsthalf);
							} catch (NumberFormatException e) {
								System.out.println("Error: The requested action could not be completed (invalid component(s)).");
								valid = false;
							}
						}
						
						if (secondhalf.contains(".")) {
							
							try {
							secondNum = Double.parseDouble(secondhalf);
							} catch (NumberFormatException e) {
								System.out.println("Error: The requested action could not be completed (invalid component(s)).");
								valid = false;
							}
							
						} else {
							
							try {
								secondNum = (double)Integer.parseInt(secondhalf);
							} catch (NumberFormatException e) {
								System.out.println("Error: The requested action could not be completed (invalid component(s)).");
								valid = false;
							}
						}
						
						//perform the operation based on the operator
						double finalValue = 0.0;
						if (valid == true) {
						if (operator == '-') {
							finalValue = firstNum - secondNum;
						} else if (operator == '*') {
							finalValue = firstNum * secondNum;
						} else if (operator == '/') {
							finalValue = firstNum / secondNum;
						} else {
							finalValue = firstNum + secondNum;
						}
						
						//assign the final value to the indicated cell
						sheetValues[toBeAssigned.row][toBeAssigned.column] = "" + finalValue;
						}
					} else {
						
					//assigns a value to a single cell
					sheetValues[toBeAssigned.row][toBeAssigned.column] = input.substring(5);
					}
				}
				
				
				columncount = 0;
				rowcount = 0;
				
				
				
				//while loop that turns all null values of sheetValues into spaces
				while (rowcount < sheetValues.length) {
					
					while (columncount < sheetValues[rowcount].length) {
						
						if (sheetValues[rowcount][columncount] == null) {
							sheetValues[rowcount][columncount] = "";
						}
						columncount++;
					}
					rowcount++;
					columncount = 0;
				}
		
				//print the altered spreadsheet
				printSpreadSheet(sheetValues);
				
				//ask for new input
				input = scan.nextLine();
			
			
			}
			System.out.println("Goodbye!");
		
			scan.close();
		}

			
		
		
		//method that prints the spreadsheet with its values
		public static void printSpreadSheet(String[][] sheetValues) {
			//nested while loop that makes the entries the proper length
			int columncount = 0;
			int rowcount = 0;
			
			while (rowcount < sheetValues.length) {
				
				while (columncount < sheetValues[rowcount].length) {
					
					while (sheetValues[columncount][rowcount].length() < 10) {
						sheetValues[columncount][rowcount] = sheetValues[columncount][rowcount] + " ";
					}
					columncount++;
				}
				rowcount++;
				columncount = 0;
			}
			
			//final printout
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("|      |    1     |    2     |    3     |    4     |    5     |    6     |    7     |    8     |");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("||  A ||"+sheetValues[0][0]+"|"+sheetValues[0][1]+"|"+sheetValues[0][2]+"|"+sheetValues[0][3]+"|"+sheetValues[0][4]+"|"+sheetValues[0][5]+"|"+sheetValues[0][6]+"|"+sheetValues[0][7]+"|");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("||  B ||"+sheetValues[1][0]+"|"+sheetValues[1][1]+"|"+sheetValues[1][2]+"|"+sheetValues[1][3]+"|"+sheetValues[1][4]+"|"+sheetValues[1][5]+"|"+sheetValues[1][6]+"|"+sheetValues[1][7]+"|");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("||  C ||"+sheetValues[2][0]+"|"+sheetValues[2][1]+"|"+sheetValues[2][2]+"|"+sheetValues[2][3]+"|"+sheetValues[2][4]+"|"+sheetValues[2][5]+"|"+sheetValues[2][6]+"|"+sheetValues[2][7]+"|");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("||  D ||"+sheetValues[3][0]+"|"+sheetValues[3][1]+"|"+sheetValues[3][2]+"|"+sheetValues[3][3]+"|"+sheetValues[3][4]+"|"+sheetValues[3][5]+"|"+sheetValues[3][6]+"|"+sheetValues[3][7]+"|");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("||  E ||"+sheetValues[4][0]+"|"+sheetValues[4][1]+"|"+sheetValues[4][2]+"|"+sheetValues[4][3]+"|"+sheetValues[4][4]+"|"+sheetValues[4][5]+"|"+sheetValues[4][6]+"|"+sheetValues[4][7]+"|");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("||  F ||"+sheetValues[5][0]+"|"+sheetValues[5][1]+"|"+sheetValues[5][2]+"|"+sheetValues[5][3]+"|"+sheetValues[5][4]+"|"+sheetValues[5][5]+"|"+sheetValues[5][6]+"|"+sheetValues[5][7]+"|");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("||  G ||"+sheetValues[6][0]+"|"+sheetValues[6][1]+"|"+sheetValues[6][2]+"|"+sheetValues[6][3]+"|"+sheetValues[6][4]+"|"+sheetValues[6][5]+"|"+sheetValues[6][6]+"|"+sheetValues[6][7]+"|");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("||  H ||"+sheetValues[7][0]+"|"+sheetValues[7][1]+"|"+sheetValues[7][2]+"|"+sheetValues[7][3]+"|"+sheetValues[7][4]+"|"+sheetValues[7][5]+"|"+sheetValues[7][6]+"|"+sheetValues[7][7]+"|");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("Change? --->");
			
		}

		//method that makes all values in the spreadsheet equal null
		public static void clearSpreadSheet(String[][] sheetValues) {
			int columncount = 0;
			int rowcount = 0;
			
			while (rowcount < sheetValues.length) {
				
				while (columncount < sheetValues[rowcount].length) {
					
					sheetValues[rowcount][columncount] = null;
					columncount++;
				}
				rowcount++;
				columncount = 0;
			}
		}
		
		 
		
	}

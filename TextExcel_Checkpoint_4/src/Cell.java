
public class Cell {
	int row;
	int column;

//constructor
public Cell(String cellCall, int letterindex, int numberindex) {
	
	String letterInitial = "";
	String numberInitial = "";
	
	if (numberindex <= cellCall.length()-2) {
		letterInitial = cellCall.substring(letterindex,letterindex+1);
		numberInitial = cellCall.substring(numberindex,numberindex+1);
	} else {
		letterInitial = cellCall.substring(letterindex,letterindex+1);
		numberInitial = cellCall.substring(numberindex);
	}
	
	String lowerCase = letterInitial.toLowerCase();
	
	char letterChar = lowerCase.charAt(0);
	int rowInt = (96-(int)letterChar)*-1;
	
	int columnInt = Integer.parseInt(numberInitial);
	
	rowInt--;
	columnInt--;
	
	row = rowInt;
	column = columnInt;
}

}

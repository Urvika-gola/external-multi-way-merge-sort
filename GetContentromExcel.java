import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/***
 * A class that fetches data from the CSV files
 * using buffered reader
 * @author ugola
 *
 */
public class GetContentromExcel {
	/***
	 * This method checks if the index passed for the excel is a valid
	 * index or not
	 * @param arr the array of fields 
	 * @param index
	 * @return true if the index is valid, false otherwise.
	 */
	public static boolean isValidIndex(String[] arr, int index) {
        return index >= 0 && index < arr.length;
    }
	/***
	 * This public method gets the next available chip record
	 * @param br the buffered reader
	 * @param sortColumn the column number on which sorting is performed
	 * @return next Chip
	 */
	public static Chips getNextAvailableChip(BufferedReader br, int sortColumn)   
	{  
		String line = "";  
		String splitBy = ",";  
		//List<Chips> chipsList = new ArrayList<Chips>();
		Chips chip = new Chips();
		try {  
			//parsing the chips.csv file into BufferedReader class constructor  
			//Skipping metadata line
			//br.readLine();
			if ((line = br.readLine()) != null) {   //returns a Boolean value  
				String[] chips = line.split(splitBy);    // use comma as separator  
				if (isValidIndex(chips, 0)) 
					chip.setProduct(chips[0]);
				else 
					chip.setProduct("");
				if (isValidIndex(chips, 1)) 
					chip.setType(chips[1]);
				else 
					chip.setType("");
				if (isValidIndex(chips, 2))
					chip.setReleaseDate(chips[2]);
				else
					chip.setReleaseDate("NaT");
				if (isValidIndex(chips, 3)) {
					if(chips[3] == null)
						chips[3] = "";
					chip.setTransistors(chips[3]);
				}
				else 
					chip.setTransistors("");	
				//Initializing the extracted chip with the sortingColumnNumber
				chip.setSortColumnNumber(sortColumn);
				//chipsList.add(chip);
			}  else
				return null; // If no more rows exist in the csv file
		} catch (IOException e) {  
			e.printStackTrace();  
		}
		// chipsList is a list, that will contain all the rows from the CSV file
		return chip;
	}  
	/***
	 * This public method gets the remaining chips
	 * @param br the buffered reader
	 * @param sortColumn the column number on which sorting is performed
	 * @return the remaining chips in form of a list
	 */
	public static List<Chips> getRemainingChips(BufferedReader br, int sortColumn)   
	{  
		String line = "";  
		String splitBy = ",";  
		List<Chips> chipsList = new ArrayList<Chips>();
		
		String[] chips;
		try {  
			//parsing the chips.csv file into BufferedReader class constructor  
			//Skipping metadata line
			//br.readLine();
			while ((line = br.readLine()) != null) {   //returns a Boolean value  
				Chips chip = new Chips();
				chips = line.split(splitBy);    // use comma as separator  
				if (isValidIndex(chips, 0)) 
					chip.setProduct(chips[0]);
				else 
					chip.setProduct("");
				if (isValidIndex(chips, 1)) 
					chip.setType(chips[1]);
				else 
					chip.setType("");
				if (isValidIndex(chips, 2))
					chip.setReleaseDate(chips[2]);
				else
					chip.setReleaseDate("NaT");
				if (isValidIndex(chips, 3)) {
					if(chips[3] == null)
						chips[3] = "";
					chip.setTransistors(chips[3]);
				}
				else 
					chip.setTransistors("");	
				//Initializing the extracted chip with the sortingColumnNumber
				chip.setSortColumnNumber(sortColumn);
				chipsList.add(chip);
			}
		} catch (IOException e) {  
			e.printStackTrace();  
		}
		// chipsList is a list, that will contain all the rows from the CSV file
		return chipsList;
	} 
}
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/***
 * After pass0, when we iterate over the CSV files,
 * the computer iterates over them in random fashion
 * therefore, we sort the file names and store it in File
 * @author ugola
 *
 */
public class GetSortedFileNames {
	/***
	 * This method gets the sorted file names in the CSV path
	 * @param runsCSVPath the Path where all the CSV files are present
	 * @return the sorted name of files in files
	 */
	public static List<String> getSortedFileNames(String runsCSVPath) {
		  File dir = new File(runsCSVPath);
		  //Iterate over the list of files in the CSV path directory
	      File[] listOfFiles = dir.listFiles();
	      // Add the list of files in the List of String
	      List<String> files = new ArrayList<>();
	      for (int i = 0; i < dir.list().length; i++) {
	            String csvFileName = listOfFiles[i].getName();
	            files.add(csvFileName);
	        }

	      // Sort the CSV files based on the number of run number of CSV files, not the pass number 
	      // as we know we are sorting for pass 0 only.
		  Collections.sort(files, new Comparator<String>() {
			    public int compare(String file_1, String file_2) {
			        int hyphenFile1 = file_1.indexOf('-');
			        int dotFile1 = file_1.indexOf('.');
			        int hyphenFile2 = file_2.indexOf('-');
			        int dotFile2 = file_2.indexOf('.');
			        int runNumFile1 = Integer.valueOf(file_1.substring(hyphenFile1 + 1, dotFile1));
			        int runNumFile2 = Integer.valueOf(file_2.substring(hyphenFile2 + 1, dotFile2));
			        if (runNumFile1 == runNumFile2) { return 0; } 
			        else if (runNumFile1 <  runNumFile2) { return -1; }
			        else { return 1; }
			    }});
		  return files;
}}
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
/***
 * This class takes input as a CSV file, number of Runs and the W for N way merge sort
 * and performs external multi way merge sort on the CSV file
 * @author ugola
 *
 */
public class Program1 {
	/***
	 * This private method executes the Pass 0, reads r fields, and sort them,
	 * and save the individual CSVs as unique names as per the convention 
	 * @param chipsCSVPath the path of the CSV file passed by the user
	 * @param r number of R lines
	 * @param w number of W way sort
	 * @param sortColumn column number on which the sorting is performed
	 */
	private static void executePass0(String chipsCSVPath, int r, int w, int sortColumn) {
	
		BufferedReader br;
		List<Chips> listOfChips = new LinkedList<Chips>();
		int runNumber = 0;
		try {
			br = new BufferedReader(new FileReader(chipsCSVPath));
			int n = 1;
			outer: while(true) {
				   	if(n%(r+1) != 0) {
				   		Chips chip = GetContentromExcel.getNextAvailableChip(br, sortColumn);
						if(chip != null) {
							if(chip.getProduct().compareTo("Product") != 0)
								listOfChips.add(chip);
							else {
								n = 1;
								continue outer;
							}
						} else
							break outer;
					} else {
						// Giving it a name as per convention
						writeToRunCSV("run0" + "-" + String.valueOf(runNumber)+".csv", listOfChips);
						runNumber++;
						n=0;	
					}
					n++;	
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		// check if listOfChips has values
		// String testingBlock = null;
		if(!listOfChips.isEmpty())
			writeToRunCSV("run0" + "-" + String.valueOf(runNumber)+".csv", listOfChips);
		
	}
	
	/***
	 * This private method executes the Nth pass
	 * @param runsCSVPath the path of the CSV for the current pass
	 * @param inputFileName 
	 * @param r number of R lines
	 * @param w number of W way sort
	 * @param sortColumn column number on which the sorting is performed
	 */
	private static void executePassN(String runsCSVPath, String inputFileName, int r, int w, int sortColumn) {
	
		Map<Integer, List<String>> runFileNamesMap = getInitialRunFileNamesMap(runsCSVPath);
		
		File file = null;
		File renameFile;
		int currentPassNumber = 0;
		String newRunFileName = null;
 outer: while(true) {
			List<String> runFileNamesList = runFileNamesMap.get(currentPassNumber);
			int runFileProcessCount = 0;
			List<String> runFilesToProcessList = new ArrayList<String>();
			int totalRunFilesInPass = runFileNamesList.size();
			int totalProcessedRunFileInPass = 0;
			// The last pass will have only 1 run file so we need to skip this
			if(totalRunFilesInPass > 1) {
				for(String eachRunFile : runFileNamesList) {
					runFilesToProcessList.add(eachRunFile);
					runFileProcessCount++;
					totalProcessedRunFileInPass++;
					// If the last batch has run files < w
					if(runFileProcessCount == w || totalProcessedRunFileInPass == totalRunFilesInPass) {
						newRunFileName = processRunFile(runsCSVPath, r, w, sortColumn, currentPassNumber, runFilesToProcessList);
						if(runFileNamesMap.get(currentPassNumber+1) == null) {
							List<String> passRunFileList = new ArrayList<String>();
							passRunFileList.add(newRunFileName);
							runFileNamesMap.put(currentPassNumber+1, passRunFileList);
						} else {
							List<String> passRunFileList = runFileNamesMap.get(currentPassNumber+1);
							passRunFileList.add(newRunFileName);
							runFileNamesMap.put(currentPassNumber+1, passRunFileList);
						}
						// Setting back to runFileProcessCount=1 to re process set of w files
						runFileProcessCount=0;
						// Clearing runFilesToProcessList
						runFilesToProcessList.clear();
					}
				}
				System.out.println("Pass " + currentPassNumber + " created " + totalProcessedRunFileInPass + " runs.");
				currentPassNumber++;
			} else { // Breaks the loop and ends the code
				file = new File(runsCSVPath+newRunFileName);
				renameFile = new File(runsCSVPath+inputFileName.substring(0, inputFileName.indexOf('.'))+"-sorted.csv");

				file.renameTo(renameFile);
				System.out.println("Pass " + currentPassNumber + " created 1" + " run.");
				break outer;
			}
		}
 		System.out.println("The completed file's name is " + inputFileName.substring(0, inputFileName.indexOf('.'))+"-sorted.csv");
  
	}
	/***
	 * This private method performs the m way external merge sort
	 * @param runsCSVPath The path of the CSV file
	 * @param r number of R lines
	 * @param w number of W way sort
	 * @param sortColumn column number on which the sorting is performed
	 * @param currentPassNumber the current pass number
	 * @param runFilesToProcessList List of the file names to be processed for 
	 * @return the new run file names
	 */
	private static String processRunFile(String runsCSVPath, int r, int w, int sortColumn,
			int currentPassNumber, List<String> runFilesToProcessList) {
		
		String newRunFileName = getNewRunFileName(currentPassNumber, w, runFilesToProcessList.get(0));
		
		// Total RunFiles to process
		int iTotalRunFilesToProcess = runFilesToProcessList.size();
		// By Default we pick first two run files to process at the start
		int runFilesProcessed = 2;
		// We need two run files to compare always
		// Index of first run file to compare
		int iRunFileComp1Index = 0;
		// Index of second run file to compare
		int iRunFileComp2Index = 1;
		
		int iRunFileNextToPick = 2;
		
		boolean isRunFileComp1ReadComplete = false;
		boolean isRunFileComp2ReadComplete = false;
		
		boolean isPickRChipsComp1 = true;
		boolean isPickRChipsComp2 = true;
		
		String runFileComp1 = runFilesToProcessList.get(iRunFileComp1Index);

		if(runFilesToProcessList.size() > 1) {
			String runFileComp2 = runFilesToProcessList.get(iRunFileComp2Index);
			List<Chips> newRunFileChipsList = new ArrayList<Chips>();
			try {
				BufferedReader brRunFileComp1 = new BufferedReader(new FileReader(runsCSVPath + runFileComp1));
				BufferedReader brRunFileComp2 = new BufferedReader(new FileReader(runsCSVPath + runFileComp2));
				
				List<Chips> runFileComp1Content = new ArrayList<Chips>();
				List<Chips> runFileComp2Content = new ArrayList<Chips>();
				
				int iRunFileComp1ContentIndex = 0;
				int iRunFileComp2ContentIndex = 0;
				
		outer:	while (!isRunFileComp1ReadComplete || !isRunFileComp2ReadComplete) {
					//Read r chips of runFileComps
					if(isPickRChipsComp1) {
						iRunFileComp1ContentIndex = 0;
						runFileComp1Content = getRChipsFromCSV(r, brRunFileComp1, sortColumn);
					}
					if(isPickRChipsComp2) {
						iRunFileComp2ContentIndex = 0;
						runFileComp2Content = getRChipsFromCSV(r, brRunFileComp2, sortColumn);
					}
					
					int runFileComp1ContentSize = runFileComp1Content.size();
					int runFileComp2ContentSize = runFileComp2Content.size();
					
					isRunFileComp1ReadComplete = (runFileComp1ContentSize < r)? true : false;
					isRunFileComp2ReadComplete = (runFileComp2ContentSize < r)? true : false;
					
					// Process Run Files
					if(!isRunFileComp1ReadComplete && !isRunFileComp2ReadComplete) {
						
						while(iRunFileComp1ContentIndex < runFileComp1ContentSize && iRunFileComp2ContentIndex < runFileComp2ContentSize) {
							//First Component is smaller or equal to second component (b-a)
							if(runFileComp1Content.get(iRunFileComp1ContentIndex).compareTo(runFileComp2Content.get(iRunFileComp2ContentIndex)) <= 0) {
								newRunFileChipsList.add(runFileComp1Content.get(iRunFileComp1ContentIndex));
								iRunFileComp1ContentIndex++;
							} else {
								newRunFileChipsList.add(runFileComp2Content.get(iRunFileComp2ContentIndex));
								iRunFileComp2ContentIndex++;
							}
						}
						if(iRunFileComp1ContentIndex >= runFileComp1ContentSize)
							isPickRChipsComp1 = true;
						else
							isPickRChipsComp1 = false;
						
						if(iRunFileComp2ContentIndex >= runFileComp2ContentSize)
							isPickRChipsComp2 = true;
						else
							isPickRChipsComp2 = false;
					}
					// Check if there are more run files to process
					if(isRunFileComp1ReadComplete && (iRunFileNextToPick <= (iTotalRunFilesToProcess-1))) {
						runFileComp1 = runFilesToProcessList.get(iRunFileNextToPick);
						brRunFileComp1 = new BufferedReader(new FileReader(runsCSVPath + runFileComp1));
						iRunFileComp1Index = iRunFileNextToPick;
						iRunFileComp1ContentIndex = 0;
						isRunFileComp1ReadComplete = false;
						isPickRChipsComp1 = true;
						runFilesProcessed++;
						iRunFileNextToPick++;
					} else if(isRunFileComp2ReadComplete && iRunFileNextToPick <= iTotalRunFilesToProcess-1) {
						runFileComp2 = runFilesToProcessList.get(iRunFileNextToPick);
						brRunFileComp2 = new BufferedReader(new FileReader(runsCSVPath + runFileComp2));
						iRunFileComp2Index = iRunFileNextToPick;
						iRunFileComp2ContentIndex = 0;
						isRunFileComp2ReadComplete = false;
						isPickRChipsComp2 = true;
						runFilesProcessed++;
						iRunFileNextToPick++;
					} 
					// Append remaining unread chips new CSV
					else if ((isRunFileComp1ReadComplete || isRunFileComp2ReadComplete) && iRunFileNextToPick >= iTotalRunFilesToProcess) {
				
						if(!isRunFileComp1ReadComplete) {
							// Add remaining Chips from runFileComp1Content
							newRunFileChipsList = appendRemainingChipsToNewRunFileChipList(newRunFileChipsList, runFileComp1Content, iRunFileComp1ContentIndex);
							// Add remaining Chips from CSV
							List<Chips> remainingChipsFromCSV = GetContentromExcel.getRemainingChips(brRunFileComp1, sortColumn);
							newRunFileChipsList = appendRemainingChipsToNewRunFileChipList(newRunFileChipsList, remainingChipsFromCSV, 99999); // 99999 is a placeholder to re use a method
							break outer;
						} 
						if(!isRunFileComp2ReadComplete) {
							// Add remaining Chips from runFileComp2Content
							newRunFileChipsList = appendRemainingChipsToNewRunFileChipList(newRunFileChipsList, runFileComp2Content, iRunFileComp2ContentIndex);
							// Add remaining Chips from CSV
							List<Chips> remainingChipsFromCSV = GetContentromExcel.getRemainingChips(brRunFileComp2, sortColumn);
							newRunFileChipsList = appendRemainingChipsToNewRunFileChipList(newRunFileChipsList, remainingChipsFromCSV, 99999); // 99999 is a placeholder to re-use a method
							break outer;
						}
					}
				}
				Collections.sort(newRunFileChipsList);
				// Writing the chip list to the new run file csv
				writeToRunCSV(newRunFileName, newRunFileChipsList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedReader brRunFileCompFinal1 = new BufferedReader(new FileReader(runsCSVPath + runFileComp1));
				List<Chips> getAllChips = GetContentromExcel.getRemainingChips(brRunFileCompFinal1, sortColumn);
				Collections.sort(getAllChips);
				writeToRunCSV(newRunFileName, getAllChips);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return newRunFileName;
	}
	/***
	 * This private method handles the window of the current CSV file that is being read
	 * if the numbers of records read in the current window, it will check if more
	 * records exist, and accordingly shifts the window and reads at max R records
	 * @param newRunFileChipsList The records are appended to this list
	 * @param remainingChips number of remaining chips
	 * @param index the index from which we need to read records
	 * @return the new run file chip list
	 */
	private static List<Chips> appendRemainingChipsToNewRunFileChipList(List<Chips> newRunFileChipsList,
			List<Chips> remainingChips, int index) {
		
		if(index != 99999)
			for(int i = index; i<remainingChips.size(); i++)
				newRunFileChipsList.add(remainingChips.get(i));
		else
			for(Chips eachChip : remainingChips)
			newRunFileChipsList.add(eachChip);
		
		return newRunFileChipsList;
	}
	/***
	 * This private method brings the R records from the CSV to the main memory and stores it into
	 * a list of Chips
	 * @param r the value of R records to be read
	 * @param brRunFileComp1 the buffers reader
	 * @param sortColumn the column on which we have to sort
	 * @return the list of Chips that have been brought from secondary memory i.e CSV file
	 */
	private static List<Chips> getRChipsFromCSV(int r, BufferedReader brRunFileComp1, int sortColumn) {
		
		List<Chips> rChipsFromCSV = new ArrayList<Chips>();
		for(int i = 1; i<= r; i++) {
			Chips chipExtractedComp1 = GetContentromExcel.getNextAvailableChip(brRunFileComp1, sortColumn);
			if(chipExtractedComp1 != null)
				rChipsFromCSV.add(chipExtractedComp1);
		}
		return rChipsFromCSV;
	}
	/***
	 * This private method forms the name of the CSV file as per the nomenclature run[p]-[r].csv
	 * @param currentPassNumber the pass number to be added in the CSV file name
	 * @param w the value of w-way merge sort
	 * @param firstRunFileName 
	 * @return the CSV file name as a string
	 */
	private static String getNewRunFileName(int currentPassNumber, int w, String firstRunFileName) {
		int runFileNumber = Integer.parseInt(firstRunFileName.substring(getCharacterIndexOf(firstRunFileName, '-') + 1, 
				   getCharacterIndexOf(firstRunFileName, '.')));
		
		return "run" + String.valueOf(currentPassNumber+1) + "-" + String.valueOf(runFileNumber/w) + ".csv";
	}
	/***
	 * This private method generates the CSV files for the pass 0
	 * @param runsCSVPath the path of the CSV file from which we are creating the pass 0
	 * @return returns the list of CSV files names which was generated for pass 0, this will be used to iterate
	 * over the CSV files for the next pass
	 */
	private static Map<Integer, List<String>> getInitialRunFileNamesMap(String runsCSVPath) {
		
		File folder = new File(runsCSVPath);
		List<String> sortedListOfFiles = GetSortedFileNames.getSortedFileNames(runsCSVPath);
		Map<Integer, List<String>> runFileNamesMap = new HashMap<Integer, List<String>>();
		List<String> pass0RunFileList = new ArrayList<String>();
		
		for (int i = 0; i < sortedListOfFiles.size(); i++)
			pass0RunFileList.add(sortedListOfFiles.get(i));
		runFileNamesMap.put(0, pass0RunFileList);
		return runFileNamesMap;
	}
	/***
	 * This private method is used to get the index of a character
	 * @param strFileName
	 * @param ch the characters whose index we are finding
	 * @return returns the index of the character as int
	 */
	private static int getCharacterIndexOf(String strFileName, char ch) {
		for(int i =0; i<strFileName.length(); i++) {
			if(strFileName.charAt(i)== ch)
				return i;
		}	
		return 0;
	}
	private static void validatePathForRenaming(String chipsCSVPath, int sortColumn) {
		try {
			BufferedReader brRunFileCompFinal1 = new BufferedReader(new FileReader(chipsCSVPath));
			List<Chips> chips = GetContentromExcel.getRemainingChips(brRunFileCompFinal1, sortColumn);
			System.out.println("It Contains " + (chips.size() - 1) + " lines.");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***
	 * This method validates if the result CSV we have created is sorted or not
	 * @param renamedfilePath the renamed file path is the final path i.e chips-sorted.csv
	 * @param sortColumn the column on which data was sorted
	 * @throws FileNotFoundException if the final sorted file not found
	 */
	private static void validateSortingOfFinalCSV(String renamedfilePath, int sortColumn) throws FileNotFoundException {
		
		BufferedReader renamedFile = new BufferedReader(new FileReader(renamedfilePath));
		List<Chips> chipsFromSortedCSV = GetContentromExcel.getRemainingChips(renamedFile, sortColumn);	
		List<String> originalCSVColumn = new ArrayList<String>();
		for(Chips chip : chipsFromSortedCSV) {
			switch(sortColumn) {
				case 1:
					originalCSVColumn.add(chip.getProduct());
					break;
				case 2:
					originalCSVColumn.add(chip.getType());
					break;
				case 3:
					originalCSVColumn.add(chip.getReleaseDate());
					break;
				case 4:
					originalCSVColumn.add(chip.getTransistors());
					break;	
			}
		}
		List<String> copy = new ArrayList<String>(originalCSVColumn);
		Collections.sort(copy);
		if(copy.equals(originalCSVColumn))
			System.out.println("VERIFIED: The completed file is in sorted order by field " + sortColumn + ".");
	}
	/***
	 * This method writes the data (column+rows) of the current run to the CSV file 
	 * @param csvName the name of the CSV file to be saved as for this run
	 * @param listOfChips this contains the data, i.e all the column + rows in the form of List of Chips
	 */
	private static void writeToRunCSV(String csvName, List<Chips> listOfChips) {
		
		try {
			if(!listOfChips.isEmpty()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter("csv_result/" + csvName));
				Collections.sort(listOfChips);
				for(Chips chip : listOfChips) {	
					String lineToSave = chip.getProduct() + "," 
										+ chip.getType() + "," 
										+ chip.getReleaseDate() + "," 
										+ chip.getTransistors()
										+ "\n";
					writer.write(lineToSave);	
				}
				//Clearing the list after writing to the csv
				listOfChips.clear();
			    writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/***
	 * main method that takes the command line arguments as input
	 * and performs the expected tasks
	 * @param args command line args
	 * @throws FileNotFoundException if the main CSV not found.
	 */
	public static void main(String args[]) throws FileNotFoundException {
		String chipsCSVPath = args[0];
		int r = Integer.valueOf(args[1]);
		int w = Integer.valueOf(args[2]);
		int sortColumn = Integer.valueOf(args[3]);
		
		if (sortColumn>=1 && sortColumn<=4) {
			String runsCSVPath = "csv_result/";
			// we will delete the contents of this folder before every run
			// but first we will create the dir if it doesn't exist
			File CsvResultDir = new File(runsCSVPath);
			if (!CsvResultDir.exists()) {
				CsvResultDir.mkdirs();
			}
			for (File subfile: CsvResultDir.listFiles()) {
				subfile.delete();
			}	
			Path inputFilePath = Paths.get(chipsCSVPath);
			String inputFileName = inputFilePath.getFileName().toString();
			String finalRenamedFile = inputFileName.substring(0, inputFileName.indexOf('.'))+"-sorted.csv";
			// Validating if the values of r and w are correct
			if(r>0 && w >1) {
				executePass0(chipsCSVPath, r, w, sortColumn);
				executePassN(runsCSVPath, inputFileName, r, w, sortColumn);
				validatePathForRenaming(chipsCSVPath, sortColumn);
				validateSortingOfFinalCSV(runsCSVPath+finalRenamedFile, sortColumn);
			} else
				System.out.println("Please re-check values of r and w");
		}
		else{
		System.out.println("Please enter a valid column number");
		}
	}
}

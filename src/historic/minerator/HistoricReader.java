package historic.minerator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


import utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HistoricReader {
	
	private static final String[] FILE_HEADER_MAPPING = {"fileName", "methodName"};
	private Map<Integer, String> methodMap;
	private List<String> headerList; 
	List csvRecords;
	
	public void retrieveHistoric(String pathHistoric) throws IOException{
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
		FileReader fileReader = new FileReader(pathHistoric);
		CSVParser csvFileParser;

		csvFileParser = new CSVParser(fileReader, csvFileFormat);

		methodMap = new HashMap<Integer, String>();
	
		
     	csvRecords = csvFileParser.getRecords(); 
     	for (int i = 0; i< csvRecords.size(); i++){
             CSVRecord record = (CSVRecord) csvRecords.get(i);
             String method = record.get(Utils.METHOD);            
             methodMap.put(i, method);
     	}
     	
     	parserHeader();
     	   
	}
	
	public Map<String, String> getHistoric(String nameMethod){
		
		final Integer[] key = new Integer[1];
		methodMap.entrySet().forEach( methodHistoric -> {
			
			String methodHistoricIgnoreSpace = methodHistoric.getValue();
			methodHistoricIgnoreSpace  = methodHistoricIgnoreSpace.replaceAll("\\s+","");
			
	        String nameMethodIgnoreSpace = nameMethod;
	        nameMethodIgnoreSpace = nameMethodIgnoreSpace.replaceAll("\\s+","");
	        
			if(methodHistoricIgnoreSpace.equals(nameMethodIgnoreSpace)){
				key[0] = methodHistoric.getKey();
			}
			
		});
		
		return getHistoric(key[0]);
	}
	
	private  List<String> parserHistoric(int numberHistoric){
		
		CSVRecord record = (CSVRecord) csvRecords.get(numberHistoric);
		
		String historicRecord = record.toString();
		
		String historicStart = Utils.VALUES_INDICATOR + Utils.OPEN_VALUES;
		
		int valuesIndex = historicRecord.indexOf(historicStart);

		String valuesPartial = historicRecord.substring(historicStart.length());

		int methodIndexLast = valuesPartial.lastIndexOf(Utils.END_METHOD + Utils.SEPARATOR);

		int valuesEndIndex = valuesPartial.indexOf(Utils.CLOSE_VALUES);
		
		String methodEnd =  Utils.END_METHOD + Utils.SEPARATOR.length(); 

		String historicValue = valuesPartial.substring(methodIndexLast + methodEnd.length() , valuesEndIndex);
		
		List<String> historicList = new LinkedList<>();
		
		historicList.add(record.get(Utils.FILE));
		historicList.add(record.get(Utils.METHOD));

		historicList.addAll(Arrays.asList(historicValue.split("\\s*,\\s*")));
		
		return historicList;
    	
    }
	
	private void parserHeader(){
		
		CSVRecord headerRecord = (CSVRecord) csvRecords.get(0);
		String headerHistoric = headerRecord.toString();
		
		String historicStart = Utils.VALUES_INDICATOR + Utils.OPEN_VALUES;
		
		int valuesIndex = headerHistoric.indexOf(historicStart);

		String valuesPartial = headerHistoric.substring(valuesIndex + historicStart.length());

		String methodEnd = Utils.METHOD + Utils.SEPARATOR;
		
		int methodIndexLast = valuesPartial.lastIndexOf(methodEnd);
		int valuesEndIndex = valuesPartial.indexOf(Utils.CLOSE_VALUES);

		String headerValue = valuesPartial.substring(methodIndexLast +  methodEnd.length(), valuesEndIndex);

		headerList = new LinkedList<>();
		headerList.add(headerRecord.get(Utils.FILE));
		headerList.add(headerRecord.get(Utils.METHOD));
		headerList.addAll(Arrays.asList(headerValue.split("\\s*,\\s*")));

	}
	
	public Map<String, String> getHistoric(int numberHistoric){

		Map<String, String> historicPerMethod = new LinkedHashMap<>();

		List<String> historicPerLine = parserHistoric(numberHistoric);
		
		for (int i = 0; i < headerList.size(); i++) {
			historicPerMethod.put(headerList.get(i), historicPerLine.get(i));
		}

		 return historicPerMethod;
	}
	
	
	

}

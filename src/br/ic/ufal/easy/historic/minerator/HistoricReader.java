package br.ic.ufal.easy.historic.minerator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


import br.ic.ufal.easy.utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class HistoricReader {
	
	private static final String[] FILE_HEADER_MAPPING = {"fileName", "methodName"};
	private Map<Integer, String> methodMap;
	private List<String> headerList; 
	List csvRecords;
	List<Integer> historicModified; 
	Integer currentKey;



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
     	historicModified = new LinkedList<>();
     	   
	}
	
	public Map<String, String> getHistoric(String nameMethod){
		
		Integer key = null;
		
		for (Entry<Integer, String> methodHistoric : methodMap.entrySet()) {
			 
			String methodHistoricIgnoreSpace = methodHistoric.getValue();
			methodHistoricIgnoreSpace  = methodHistoricIgnoreSpace.replaceAll("\\s+","");
			methodHistoricIgnoreSpace  = methodHistoricIgnoreSpace.replaceAll("\"","");
			
	        String nameMethodIgnoreSpace = nameMethod;
	        nameMethodIgnoreSpace = nameMethodIgnoreSpace.replaceAll("\\s+","");
	        nameMethodIgnoreSpace = nameMethodIgnoreSpace.replaceAll("\"","");
	        
	    
			if(methodHistoricIgnoreSpace.equals(nameMethodIgnoreSpace)){
				key = methodHistoric.getKey();
				
			}
		}
	
		
      if(key != null){
		currentKey = key;
		return getHistoric(currentKey);
      }
      
      return new HashMap<String, String>();
		
	}
	
	public void saveMethodKey(){
		historicModified.add(currentKey);
		currentKey = null;
	}
	
	public  List<String> parserHistoric(int numberHistoric){
		
		CSVRecord record = (CSVRecord) csvRecords.get(numberHistoric);
		
		String historicRecord = record.toString();
		
//		System.out.println(historicRecord);
		
		String historicStart = Utils.VALUES_INDICATOR + Utils.OPEN_VALUES;
		
		int valuesIndex = historicRecord.indexOf(historicStart);

		String valuesPartial = historicRecord.substring(historicStart.length());

		int methodIndexLast = valuesPartial.lastIndexOf(Utils.END_METHOD + Utils.SEPARATOR);

		int valuesEndIndex = valuesPartial.lastIndexOf(Utils.CLOSE_VALUES);
		
		if(valuesPartial.charAt(valuesEndIndex - 1) == ']'){
			 valuesEndIndex = valuesEndIndex - 1;
		}
		
		String methodEnd =  Utils.END_METHOD + Utils.SEPARATOR.length(); 

		String historicValue = valuesPartial.substring(methodIndexLast + methodEnd.length() , valuesEndIndex);

		List<String> valueList = Arrays.asList(historicValue.split("\\s*,\\s*"));


		if( !Utils.isIntegerList(valueList)) {
			valueList = Arrays.asList("0", "0", "0", "0");
		}



        List<String> historicList = new LinkedList<>();
		historicList.add(record.get(Utils.FILE));
		historicList.add(record.get(Utils.METHOD));

		historicList.addAll(valueList);

		return  historicList;
    	
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
			if(i < historicPerLine.size()){

				historicPerMethod.put(headerList.get(i), historicPerLine.get(i));
			}	
		}

		 return historicPerMethod;
	}
	
	
	public List<Integer> getOtherHistoric(){
		
		List<Integer> allHistoric = new LinkedList(methodMap.keySet());
		
		//Interserction - remove historic modified
		allHistoric.removeAll(historicModified);   
		
		return allHistoric;
		
		
	}


	public List getCsvRecords() {
		return csvRecords;
	}
}

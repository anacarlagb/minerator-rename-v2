package historic.minerator;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import utils.Utils;

public class HistoricReader {
	
	private static final String[] FILE_HEADER_MAPPING = {"File, Method"};
	private Map<Integer, String> methodMap;
	List csvRecords;
	
	public void retrieveHistoric(String pathHistoric) throws IOException{
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
		FileReader fileReader = new FileReader(pathHistoric);
		CSVParser csvFileParser;
		csvFileParser = new CSVParser(fileReader, csvFileFormat);
		methodMap = new HashMap<Integer, String>();
	
		
	     
     	csvRecords = csvFileParser.getRecords(); 
     	for (int i = 1; i< csvRecords.size(); i++){
             CSVRecord record = (CSVRecord) csvRecords.get(i);
             String file = record.get(Utils.FILE);
             String method = record.get(Utils.METHOD);            
             methodMap.put(i, method);
     	}
     	   
	}
	
	public Map<String, String> getHistoric(String nameMethod){
		
		Map<String, String> historic = new LinkedHashMap<String, String>();
		
		final Integer key[] = null;
		methodMap.entrySet().forEach( methodHistoric -> {
			
			
			if(methodHistoric.getValue().equals(nameMethod)){
				key[0] = methodHistoric.getKey();
			}
			
		});
		
		return getHistoric(key[0]); 
	}
	
	public Map<String, String> getHistoric(int numberHistoric){
		 CSVRecord record = (CSVRecord) csvRecords.get(numberHistoric);
		 
		 return record.toMap();
	}
	
	
	
	public static void main(String[] args) throws IOException{
		HistoricReader reader = new HistoricReader();
		reader.retrieveHistoric("C:\\Users\\Ana Carla\\ProjetosAnalisados\\TestProject.csv");
	}

}

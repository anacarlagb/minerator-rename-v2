package historic.minerator;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class HistoricReader {
	
	private static final String[] FILE_HEADER_MAPPING = {"File, Method"};
	private Map<String, String> methodMap;
	
	public Map<String, String> readerHistoric(String pathHistoric) throws IOException{
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
		FileReader fileReader = new FileReader(pathHistoric);
		CSVParser csvFileParser;
		csvFileParser = new CSVParser(fileReader, csvFileFormat);
		methodMap = new HashMap<String, String>();
	
	     
     	List csvRecords = csvFileParser.getRecords(); 
     	for (int i = 1; i< csvRecords.size(); i++){
             CSVRecord record = (CSVRecord) csvRecords.get(i);
             //String file = record.get(FILE_HEADER_MAPPING[0]);
            // String method = record.get(FILE_HEADER_MAPPING[1]);
             
             System.out.println(record.toMap().toString());
            // System.out.println(file);
            // System.out.println(method);            
            // methodMap.put(method, file);
     	}
     	
     	return methodMap;    
	}
	
	
	
	public static void main(String[] args) throws IOException{
		HistoricReader reader = new HistoricReader();
		reader.readerHistoric("C:\\Users\\Ana Carla\\ProjetosAnalisados\\TestProject.csv");
	}

}

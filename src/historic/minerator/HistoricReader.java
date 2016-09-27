package historic.minerator;

import org.apache.commons.csv.CSVFormat;

public class HistoricReader {
	
	
	private static final String[] FILE_HEADER_MAPPING = {""};
	
	public void readerHistoric(String pathHistoric){
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
	}

}

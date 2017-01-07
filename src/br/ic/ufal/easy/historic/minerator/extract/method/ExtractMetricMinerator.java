package br.ic.ufal.easy.historic.minerator.extract.method;

import br.ic.ufal.easy.historic.minerator.CsvReader;
import br.ic.ufal.easy.historic.minerator.CsvWriter;
import br.ic.ufal.easy.historic.minerator.HistoricReader;
import br.ic.ufal.easy.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Ana Carla on 15/12/2016.
 */
public class ExtractMetricMinerator {

    CsvWriter writer;


    public static void main(String[] args) {
      new ExtractMetricMinerator().minerateExtractMethod("panelAdmin_extract_method.csv");
    }

    public void minerateExtractMethod(String fileName){
        HistoricReader reader = new HistoricReader();
        writer = new CsvWriter(fileName , ',', Charset.forName("ISO-8859-1"));

        try {

            Set<Integer> validHistoricNumbers = reader.retrieveCompleteHistoric(Utils.PANEL_ADMIN_HISTORIC_CSV);
              int size = reader.getCsvRecords().size();
              for(int i = 0; i < size ; i++){

                  if(validHistoricNumbers.contains(i)) {
                      Map<String, String> historicPerLine = reader.getHistoric(i);
                      String className = historicPerLine.get(Utils.FILE);
                      String methodName = historicPerLine.get(Utils.METHOD);
                      findExtractMethod(className, methodName, historicPerLine);
                  }

              }

              writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findExtractMethod(String className, String methodName, Map<String, String> historicPerLine) {

        final String[] commit1 = new String[1];
        String commit2;
        int numberStats1;
        int numberStats2;

        List<String> headerList = new ArrayList<>(historicPerLine.keySet());
        List<String> bodyList = new ArrayList<>(historicPerLine.values());

        for (int i = 0; i < bodyList.size(); i++) {
            String value1 = bodyList.get(i);

            if(value1 != null && !value1.isEmpty()){
                if(i + 1 < bodyList.size()) {
                    String value2 = bodyList.get(i + 1);


                    if (StringUtils.isNumericSpace(value1) && StringUtils.isNumericSpace(value2)) {
                        numberStats1 = Integer.valueOf(value1.replaceAll("\\s+",""));
                        numberStats2 = Integer.valueOf(value2.replaceAll("\\s+",""));

                        if (numberStats1 > numberStats2 && numberStats2 > 0 ) {
                            if (i < headerList.size() && i + 1 < headerList.size()) {
                                try {
                                    writer.write(className);
                                    writer.write(methodName);

                                    writer.write(headerList.get(i));
                                    writer.write(bodyList.get(i));

                                    writer.write(headerList.get(i + 1));
                                    writer.write(bodyList.get(i + 1));

                                    writer.endRecord();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }
            }
        }
    }



}

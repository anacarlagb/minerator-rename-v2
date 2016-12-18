package br.ic.ufal.easy.historic;

import br.ic.ufal.easy.historic.minerator.HistoricWriter;
import br.ic.ufal.easy.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ana.carlagb on 21/09/16.
 */
public class HistoricProject {

    private List<Method> methods = new ArrayList<>();
    private HistoricWriter minner = new HistoricWriter();
    private String historic = Utils.CLIP_OCR_CSV;
    
   
    public void populateMethodHistoric(BufferedReader br) throws IOException{
    	minner.minerate(br);
        minner.getMethodHistoric().stream().forEach( methodHistoric -> {
                Method method = new Method();
                method.setHistoric(methodHistoric);
                methods.add(method);
            }
        );
        
        minner.writeHistoric(this, historic, Utils.CLIP_OCR_HISTORIC_CSV_TEST);

    }

    public static void main(String[] args) {

        HistoricProject project = new HistoricProject();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(Utils.CLIP_OCR_RENAME_FILE));
            project.populateMethodHistoric(br);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

	public List<Method> getMethods() {
		return methods;
	}

   
    
}






















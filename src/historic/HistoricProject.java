package historic;

import historic.minerator.HistoricWriter;
import historic.minerator.rename.method.MethodHistoric;

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
    private String historic = "";
    
    


    public void populateMethodHistoric(BufferedReader br) throws IOException{
    	minner.minerate(br);
        minner.getMethodHistoric().stream().forEach( methodHistoric -> {
                Method method = new Method();
                method.setHistoric(methodHistoric);
                methods.add(method);
            }
        );
        
        minner.writeHistoric(this, historic);

    }

    public static void main(String[] args) {

        HistoricProject project = new HistoricProject();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/home/clip/IdeaProjects/minerator-test" +
                                                    "/src/junit4RefactoringHistoric.txt"));           
            project.populateMethodHistoric(br);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

	public List<Method> getMethods() {
		return methods;
	}

   
    
}






















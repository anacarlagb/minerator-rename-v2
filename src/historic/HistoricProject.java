package historic;

import historic.minerator.MineratorHistoric;
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


    public void populateMethodHistoric(List<MethodHistoric> methodHistories){
        methodHistories.stream().forEach( methodHistoric -> {
                Method method = new Method();
                method.setHistoric(methodHistoric);
                methods.add(method);
            }
        );

    }

    public static void main(String[] args) {

        MineratorHistoric minner = new MineratorHistoric();
        HistoricProject project = new HistoricProject();

        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader("/home/clip/IdeaProjects/minerator-test" +
                                                    "/src/junit4RefactoringHistoric.txt"));

            minner.minerate(br);
            project.populateMethodHistoric(minner.getMethodHistoric());
            project.writeHistoric();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeHistoric() {

        //GetCSV
        //Varre CSV
        //


    }
}






















package historic.minerator;

import historic.minerator.rename.method.MethodHistoric;
import historic.minerator.rename.method.MethodUtils;
import historic.minerator.rename.method.RenameHistoric;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ana.carlagb on 20/09/16.
 */
public class MineratorHistoric {


    List<MethodHistoric> historic;


    public MineratorHistoric() {
        this.historic = new ArrayList<>();
    }

    public void minerate(BufferedReader br) throws IOException {

        String sCurrentLine;

        String commitReference = "Commit:";
        String renameLineReference = "Rename Method";
        String renamedMethodReference = "renamed to";
        String classReference = "in class";
        MethodHistoric methodHistoric;


        String commit = null;
        String renamedMethod;
        String rootMethod;
        String className;
        while ((sCurrentLine = br.readLine()) != null) {


            if(sCurrentLine.indexOf(commitReference) == 0) {
                commit = sCurrentLine.substring(commitReference.length());
            }

            if(sCurrentLine.indexOf(renameLineReference) == 0){

                //rootMethod
                int startMethodIndex = renameLineReference.length() + 1;
                int endMethodIndex = sCurrentLine.indexOf(")") + 1;

                rootMethod = sCurrentLine.substring(startMethodIndex, endMethodIndex);
                int endMethodTypeIndex = rootMethod.indexOf(" ") + 1;
                rootMethod = rootMethod.substring(endMethodTypeIndex);


                //renameMethod
                int startRenamedMethodIndex = sCurrentLine.indexOf(renamedMethodReference) +
                        renamedMethodReference.length() + 1;
                int endRenamedMethodIndex  = sCurrentLine.lastIndexOf(")") + 1;

                renamedMethod = sCurrentLine.substring(startRenamedMethodIndex, endRenamedMethodIndex);
                endMethodTypeIndex = renamedMethod.indexOf(" ") + 1;
                renamedMethod = renamedMethod.substring(endMethodTypeIndex);


                //class
                int startClassIndex = sCurrentLine.indexOf(classReference) + classReference.length() + 1;
                className = sCurrentLine.substring(startClassIndex);

                //parser nameMethod
                rootMethod = MethodUtils.parserParameter(rootMethod);
                renamedMethod = MethodUtils.parserParameter(renamedMethod);

                //Creating method historic
                methodHistoric = new MethodHistoric(className, rootMethod);
                methodHistoric.addRename(commit, renamedMethod);

                historic.add(methodHistoric);

            }
        }


        buildCompleteHistoric();
    }


    public List<RenameHistoric> findRenamesPerRoot(String className, String methodName, int indexMethod){

        List<RenameHistoric> renamesHistoric = new ArrayList<>();

        if(indexMethod>-1 && indexMethod < historic.size()) {

            for (int i = indexMethod; i >= 0; i--) {
                MethodHistoric methodHistoric = historic.get(i);

                if (methodHistoric.getRootName().equals(methodName)
                        && methodHistoric.getClassName().equals(className)
                        && methodHistoric.getFlagMethod() == null) {


                    historic.get(i).setFlagMethod(MethodHistoric.FlagMethod.RENAMED);

                    renamesHistoric.addAll(historic.get(i).getRenames());

                    methodName = historic.get(i).getBaseRename().getMethodName();
                }
            }
        }

        return renamesHistoric;
    }

    public void buildCompleteHistoric(){
        //start from first commit that has rename

        List<MethodHistoric> rootHistoric = new ArrayList<>();

        for(int i = historic.size() - 1; i>=0 ; i--){
            MethodHistoric rootMethod = historic.get(i);

            if(rootMethod.getFlagMethod() == null){

                RenameHistoric renameHistoric =  rootMethod.getBaseRename();
                int nextMethodIndex = i - 1;
                List<RenameHistoric> renamesPerRoot = findRenamesPerRoot(rootMethod.getClassName(),
                                                                         renameHistoric.getMethodName(), nextMethodIndex);

                rootMethod.setFlagMethod(MethodHistoric.FlagMethod.ROOT);
                rootMethod.getRenames().addAll(renamesPerRoot);

                rootHistoric.add(rootMethod);
            }
        }


        for (MethodHistoric methodHistoric : rootHistoric) {
            System.out.println("Root: " + methodHistoric.getRootName());
            System.out.println("Class: " + methodHistoric.getClassName());

            System.out.println("historic ");
            methodHistoric.getRenames().forEach( renameHistoric ->  {

                System.out.println(renameHistoric.getCommit());
                System.out.println(renameHistoric.getMethodName());
            });
            System.out.println();
        }

        historic = rootHistoric;
    }


    public List<MethodHistoric> getMethodHistoric() {
        return historic;
    }
}

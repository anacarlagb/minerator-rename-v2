package historic.minerator.rename.method;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ana.carlagb on 20/09/16.
 */
public class MethodHistoric {


    public void setFlagMethod(FlagMethod flagMethod) {
        this.flagMethod = flagMethod;
    }

    public enum FlagMethod {ROOT, RENAMED}

    private String rootName;
    private String className;
    private List<RenameHistoric> renames;
    private FlagMethod flagMethod;



    public MethodHistoric(String className, String rootName) {
        this.className = className;
        this.rootName = rootName;
        renames = new ArrayList<>();
    }

    public void addRename(String commit, String nameMethod){

        renames.add(new RenameHistoric(rootName, commit, nameMethod));
    }

    public RenameHistoric getBaseRename(){
        return renames.get(0);
    }

    public List<RenameHistoric> getRenames() {
        return renames;
    }

    public RenameHistoric getRenameHistoricPerCommit(String commit){


        Optional<RenameHistoric> renameHistoricOpt = renames.stream()
                                                            .filter(renameHist -> renameHist.getCommit().equals(commit))
                                                            .findFirst();

        if(renameHistoricOpt.isPresent()){
            return renameHistoricOpt.get();
        }
        return null;
    }

    public RenameHistoric getRenameHistoricPerNameMethod(String nameMethod){
        Optional<RenameHistoric> renameHistoricOpt = renames.stream()
                .filter(renameHist -> renameHist.getMethodName().equals(nameMethod))
                .findFirst();

        if(renameHistoricOpt.isPresent()){
            return renameHistoricOpt.get();
        }
        return null;


    }

    public String getClassName() {
        return className;
    }

    public FlagMethod getFlagMethod() {
        return flagMethod;
    }


    public String getRootName() {
        return rootName;
    }
}

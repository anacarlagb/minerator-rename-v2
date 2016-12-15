package br.ic.ufal.easy.historic.minerator.rename.method;

/**
 * Created by ana.carlagb on 20/09/16.
 */
public class RenameHistoric {
    private String commit;
    private String methodName;
    private String nameRoot;


    public RenameHistoric(String nameRoot, String commit, String methodName) {
        this.nameRoot = nameRoot;
        this.commit = commit;
        this.methodName = methodName;
    }

    public String getCommit() {
        return commit;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getNameRoot() {
        return nameRoot;
    }
}

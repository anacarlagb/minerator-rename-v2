package historic.minerator;

import historic.HistoricProject;
import historic.Method;
import historic.minerator.rename.method.MethodHistoric;
import historic.minerator.rename.method.MethodUtils;
import historic.minerator.rename.method.RenameHistoric;
import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by ana.carlagb on 20/09/16.
 */
public class HistoricWriter {


    List<MethodHistoric> historic;
    private HistoricReader reader;
    private CsvWriter writer;


    public HistoricWriter() {
        this.historic = new ArrayList<>();
        this.reader = new HistoricReader();
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

        historic = rootHistoric;
    }


    public List<MethodHistoric> getMethodHistoric() {
        return historic;
    }
    
    public void write(String object){
    	try {
			writer.write(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //To compare 
  //TODO - Make intersection between methods map and project.getMethods() to write method non renamed
    public void writeHistoric(HistoricProject project, String historicProjectURL, String newHistoricURL) 
    		throws IOException{
      
        reader.retrieveHistoric(historicProjectURL);
        writer = new CsvWriter(newHistoricURL , ',', Charset.forName("ISO-8859-1"));
        
        /** write head **/
        reader.getHistoric(0).entrySet().forEach( columnName -> {
        	write(columnName.getKey());
        });
        
        writer.endRecord();
    	for (Method method : project.getMethods()) {
    					
    		/** Compare keys (method name) between historic of renames and old historic*/
			Map<String, String> historicLine = reader.getHistoric(method.getHistoric().getRootName());
			
			/**Compare if className is equals */
			String classeName = historicLine.get(Utils.FILE);
			if(MethodUtils.isClassEquals(classeName,  method.getHistoric().getClassName())){
				
				/** write root historic **/
		        String commitOfRename = method.getHistoric().getRenames().get(0).getCommit();
		        
		        Boolean[] commitOfRenameFound = new Boolean[1];
		        commitOfRenameFound[0] = false;
		        
		         /** writer statements **/
		        historicLine.entrySet().forEach( methodMoment -> {
		        	
		        	if(methodMoment.getKey().equals(commitOfRename)){
		        		commitOfRenameFound[0] = true;
		        	}
		        	if( !commitOfRenameFound[0]){
		        		write(methodMoment.getValue());
		        	}
		        });
		        
		        writeRenameHistoric(method.getHistoric());
		           
		       
			}
			
			writer.endRecord();
					
    	
		}
    	
    	writer.close();
    		
    			
    		
    }
    
    /** write node historic **/
    private void writeRenameHistoric(MethodHistoric methodHistoric){
    	       
   
    		
		for (int renameCurrentIndex = 0; renameCurrentIndex < methodHistoric.getRenames().size(); renameCurrentIndex++) {
			
			RenameHistoric rename = methodHistoric.getRenames().get(renameCurrentIndex);
			String[] commitOfNextRename = new String[1]; 
			int renameNextIndex = renameCurrentIndex + 1;
			
			boolean[] canWriter = new boolean[1]; 
			String[] currentCommit = new String[1];
			
			currentCommit[0] = rename.getCommit();
			canWriter[0] = false;
			
			//if has nextRename, get commitOfNextRename 
			if(renameNextIndex < methodHistoric.getRenames().size()){
				commitOfNextRename[0] = methodHistoric.getRenames().get(renameNextIndex).getCommit();
				System.out.println(commitOfNextRename[0]);
			}
		
			
			Map<String, String> historicLine = reader.getHistoric(rename.getMethodName());
			
    		/**Compare if className is equals */
			String classeName = historicLine.get(Utils.FILE);
			
			if(MethodUtils.isClassEquals(classeName,  methodHistoric.getClassName())){
				
			    historicLine.entrySet().forEach( methodMoment -> {
			   
			    		String methodMomentIgnoreSpace = methodMoment.getKey();
			    		methodMomentIgnoreSpace = methodMomentIgnoreSpace.replaceAll("\\s+","");
			    		
			    		
			    		String currentCommitIgnoreSpace = currentCommit[0];
			    		currentCommitIgnoreSpace = currentCommitIgnoreSpace.replaceAll("\\s+","");
			    		
//			    		String commitOfNextRenameIgnoreSpace = commitOfNextRename[0];
//			    		commitOfNextRenameIgnoreSpace = commitOfNextRenameIgnoreSpace.replaceAll("\\s+","");
			    		
			    		if(methodMomentIgnoreSpace.equals(currentCommitIgnoreSpace)){
			    			
			    			canWriter[0] = true;
			    		}
		    			else
		    			   if(methodMomentIgnoreSpace.equals(commitOfNextRename[0])){	
		    				canWriter[0] = false;
		    			 }
		    		
			    		
			    	
		    			if(canWriter[0]){
		    				write(methodMoment.getValue());
		    			}
		    			
	    	
			    });	
			}
    	
			
		}
		
    }
			    
		
 
    
    
    
}

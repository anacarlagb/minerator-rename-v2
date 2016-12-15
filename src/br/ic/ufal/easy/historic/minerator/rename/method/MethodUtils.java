package br.ic.ufal.easy.historic.minerator.rename.method;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ana.carlagb on 21/09/16.
 */
public class MethodUtils {


    public static String parserParameter(String nameMethod){
        if(hasParameter(nameMethod)) {

            String nameMethodTemp = nameMethod;
            int startParameterIndex = nameMethodTemp.indexOf("(") + 1;
            int endParameterIndex = nameMethodTemp.indexOf(")");
            String parameterList = nameMethodTemp.substring(startParameterIndex, endParameterIndex);
           
            List<String> parameters = Arrays.asList(parameterList.split("\\s*,\\s*"));
            parameterList = "";
            for (int i = 0; i < parameters.size(); i++) {
                int separatorIndex = parameters.get(i).indexOf(" ");
                
                String attribute = parameters.get(i).substring(0, separatorIndex);
                
                String type = parameters.get(i).substring(separatorIndex + 1);
                
                if(type.contains("<") && !type.contains(">")){
                	/*
                	 * Para casos
                	 * parameters Map<String
					   Object>
                	 * */
                   int nextIndex = i + 1;
                   if((nextIndex < parameters.size()) 
                		   && parameters.get(nextIndex).contains(">") && !parameters.get(nextIndex).contains("<")){
                	   parameterList += type + "," + parameters.get(nextIndex) + " " + attribute + ","; 
                	   
                   }
                   i = nextIndex; 
                }
                else{
                	parameterList += type + " " + attribute + ",";
                }
                
			}
            parameterList = parameterList.substring(0, parameterList.length() - 1);
            nameMethod = nameMethodTemp.substring(0, startParameterIndex) + parameterList + nameMethodTemp.substring(endParameterIndex);
        }
        System.out.println(nameMethod);
        return nameMethod;
    }

    private static boolean hasParameter(String nameMethod){
        nameMethod.replaceAll(" ", "");
        return !nameMethod.contains("()");
    }
    
    public static boolean isClassEquals(String class1, String class2){
    	//write root history
    	int class1StartIndex = class1.lastIndexOf('/') + 1;
        int class1EndIndex = class1.lastIndexOf('.');
        String class1Prefix = class1.substring(class1StartIndex, class1EndIndex);

        int class2EndIndex = class2.lastIndexOf(".") + 1;
        String class2Prefix = class2.substring(class2EndIndex);

        return class1Prefix.equals(class2Prefix);
    }
    
    
    
    

}

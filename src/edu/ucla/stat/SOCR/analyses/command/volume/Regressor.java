/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.stat.SOCR.analyses.command.volume;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author azamanya
 */
public class Regressor {

    private ArrayList<String> components;
    private int componentCount;
    private String regressorString;

    public Regressor(String regressor) {
        components = new ArrayList<String>();
        regressorString = regressor;

        StringTokenizer interaction = new StringTokenizer(regressor, "*");
        while (interaction.hasMoreTokens()) {
            componentCount++;
//            System.out.println("componentCount = " + componentCount);
            components.add(interaction.nextToken());
//            System.out.println("added component");
        }
    }

    public String leaveComponentOut(String stringToExclude){
        String subset = "";
        System.out.println("stringToExclude " + stringToExclude);
        boolean excluded = false;
        for (String component:components){
            System.out.println("component = " + component);
            if (!component.equals(stringToExclude)){
                subset = subset + "*" + component;
            }else{
                if (excluded)
                    subset = subset + "*" + component;
                else
                    excluded = true;
            }
        }

        System.out.println("subset = " + subset);

        return subset.substring(1);
    }

    public int getComponentCount(){
        return componentCount;
    }

    public ArrayList<String> getComponents(){
        return components;
    }

    public String getRegressorString(){
        return regressorString;
    }


}

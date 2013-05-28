/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.stat.SOCR.analyses.command.volume;

import java.util.Comparator;

/**
 *
 * @author azamanya
 */
public class RegressorComparator implements Comparator {

    // returns -1 if regressor1 has less interactions (asterisks) than regressor2;
    // returns 0 if the counts are equal, and 1 if regressor2 has more interactions
    public int compare(Object regressor1, Object regressor2){

        Regressor reg1 = (Regressor)regressor1;
        Regressor reg2 = (Regressor)regressor2;

        int interactions1 = reg1.getComponentCount();
        int interactions2 = reg2.getComponentCount();
        
        if (interactions1 < interactions2){
            return -1;
        }
        
        if (interactions1 == interactions2){
            return 0;
        }

        return 1;
    }

    // OBSOLETE
    // returns the number of occurences of the asterisk (*) character in a string
    public int countInteractions(String reg) {

        int interactionCount = 0;
        int fromIndex = 0;
        int nextAsterisk = reg.indexOf('*', fromIndex);

        while (nextAsterisk != -1) {
            interactionCount++;
            fromIndex = nextAsterisk + 1;
            nextAsterisk = reg.indexOf('*', fromIndex);
        }

        return interactionCount;
    }

}

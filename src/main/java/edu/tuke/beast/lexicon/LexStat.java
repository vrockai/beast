/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tuke.beast.lexicon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vrockai
 */
public class LexStat {
    
    private Map<Integer, List<Integer>> learnStat = new HashMap<Integer, List<Integer>>();
    
    public LexStat(){
        
    }
    
    public LexStat(Set<Integer> lexicon){
        for (Integer i : lexicon){
            learnStat.put(i, new ArrayList<Integer>());
        }
    }
    
    public void addAsocValue(Integer token, Integer asocCount){
        List<Integer> li = learnStat.get(token);
        
        if (li == null){
            learnStat.put(token, new ArrayList<Integer>());
            li = learnStat.get(token);
        }
        
        li.add(asocCount);
    }
    
    public List<Integer> getAsocData(Integer token){
        return learnStat.get(token);
    }
    
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        
        for(Integer i : learnStat.keySet()){
            sb.append(i);
            sb.append(": ");            
            sb.append(learnStat.get(i));
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
}

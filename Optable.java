/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;
import java.util.*;
/**
 *
 * @author Amrit
 */
public class Optable {
    public Hashtable<String, String> hm;
    
    public Optable() {
        this.hm = new Hashtable<>();
        hm.put("ADD","18");
        hm.put("AND","40");
        hm.put("COMP","28");
        hm.put("DIV","24");
        hm.put("J","3C");
        hm.put("JEQ","30");
        hm.put("JGT","34");
        hm.put("JLT","38");
        hm.put("JSUB","48");
        hm.put("LDA","00");
        hm.put("LDCH","50");
        hm.put("LDL","08");
        hm.put("LDX","04");
        hm.put("MUL","20");
        hm.put("OR","44");
        hm.put("RD","D8");
        hm.put("RSUB","4C");
        hm.put("STA","0C");
        hm.put("STCH","54");
        hm.put("STL","14");
        hm.put("STSW","E8");
        hm.put("STX","10");
        hm.put("SUB","1C");
        hm.put("TD","E0");
        hm.put("TIX","2C");
        hm.put("WD","DC");   
    }
    
}

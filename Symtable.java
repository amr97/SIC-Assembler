/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.util.Hashtable;

/**
 *
 * @author Amrit
 */
public class Symtable {
    public Hashtable<String,String> hashtable = new Hashtable<>();
    public boolean insert(String label,String address)
    {
        if(hashtable.containsKey(label))
            return false;                               //Symbol already there in symtable=false
        else
        {
            hashtable.put(label,address);
            return true;                                //Symbol not there in symtable=true
        }
    }
    
    public boolean search(String label)
    {
        if(hashtable.containsKey(label))
            return true;                                //symbol there in symtable=true
        else 
            return false;                               //Symbol not there in symtable=false
    }
    
    public boolean delete(String label)
    {
        if(hashtable.containsKey(label))
        {
            hashtable.remove(label);
            return true;                                //Symbol there in symtable and is removed=true
        }
        else
            return false;                               //Symbol not there in symtable=false
    }
}

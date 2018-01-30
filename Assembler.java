/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import javax.swing.JOptionPane;
import java.util.*;
/**
 *
 * @author Amrit
 */
public class Assembler {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Optable OP = new Optable();
        Symtable SYM = new Symtable();
        //ArrayList<String> cmd= new ArrayList<>();                             //Store commands that are used in program
        //Hashtable<String, ArrayList<String>> def = new Hashtable<>();           //Store var/const/frwd ref and their addresses
        //ArrayList<String> ar = new ArrayList<>();
        Reader r1 = new FileReader("C:\\Users\\Amrit\\Documents\\NetBeansProjects\\Assembler\\src\\assembler\\Input.txt");
        Reader r2 = new FileReader("C:\\Users\\Amrit\\Documents\\NetBeansProjects\\Assembler\\src\\assembler\\Input.txt");
        StreamTokenizer st1 = new StreamTokenizer(r1);
        StreamTokenizer st2 = new StreamTokenizer(r2);
        //ArrayList<String> label = new ArrayList<>();
        st1.eolIsSignificant(true);
        st1.commentChar('.');                                                    //Defines the character which represents comment line
        st2.eolIsSignificant(true);
        st2.commentChar('.');  
        boolean reswORresb = false, byt = false, word = false, eof = false;
        int sizeofbyte = 0, sizeofword = 0;
        String locctr = "0";
        int declocctr = 0;
        int charlinecounter = 0;
        
    
        do                                                                      //Pass 1
        {
          int token = st1.nextToken();   
          switch(token)
          {
              case StreamTokenizer.TT_EOF:
                 //System.out.println("End of File encountered.");
                 eof = true;
                 break;
                  
              case StreamTokenizer.TT_EOL:
                 //System.out.println("End of Line encountered.");
                 if(charlinecounter > 3)
                     JOptionPane.showMessageDialog(null,"Error in code. Please follow SIC format.");
                 else
                     charlinecounter = 0;                                       //set to 0 for counting next line
                 if(reswORresb)                                                 //RESW or RESB is always 3 bytes
                 {
                     //declocctr = Integer.parseInt(locctr,16);
                     //declocctr = declocctr + 0x3;
                     //locctr = Integer.toHexString(declocctr);
                     reswORresb = false;
                     //System.out.println(1);
                 }
                 else if(byt)                                                        //BYTE can be of variable size
                 {
                     if(sizeofbyte == 1)                                        
                     {
                         declocctr = Integer.parseInt(locctr,16);
                         declocctr = declocctr + 0x1;
                         locctr = Integer.toHexString(declocctr);
                     }
                     else if(sizeofbyte == 2)
                     {
                         declocctr = Integer.parseInt(locctr,16);
                         declocctr = declocctr + 0x2;
                         locctr = Integer.toHexString(declocctr);
                     }
                     else
                     {
                         declocctr = Integer.parseInt(locctr,16);
                         declocctr = declocctr + 0x3;
                         locctr = Integer.toHexString(declocctr);
                     }
                     byt = false;
                 }
                 else if(word)
                 {
                     declocctr = Integer.parseInt(locctr,16);
                     declocctr = declocctr + sizeofword;
                     locctr = Integer.toHexString(declocctr);
                     word = false;
                 }
                 else
                 {
                     //declocctr = Integer.parseInt(locctr);
                     //declocctr = declocctr + 0x3;
                     locctr = locctr + 0x3;
                     locctr = Integer.toString(declocctr);
                 }
                 break;
                  
              case StreamTokenizer.TT_WORD:
                 //System.out.println("Word: " + st.sval);
                 charlinecounter++;
                 //if(st.sval.equalsIgnoreCase("RSUB"))
                     //{
                         //definitions();
                     //}
                 if((charlinecounter == 1)&&!(OP.hm.containsKey(st1.sval)))  //checking if this is a label or var/const/frwd ref
                 {
                     SYM.hashtable.put(st1.sval, locctr);                        //label and its address added to Symtable
                     if(st1.sval.equalsIgnoreCase("EOF"))
                         SYM.hashtable.replace("EOF", "454F46");
                 }
                 else if((charlinecounter == 1) && (OP.hm.containsKey(st1.sval))); //its a command,left for pass 2
                     
                 if((charlinecounter == 2) && (OP.hm.containsKey(st1.sval)))   //condition when the first character is a label, thus 2nd one is a command
                     ;                                                          //left for pass 2
                 else                                                           //condition when 1st char is not a label but a command, thus there are only 2 chars in this line and this char is a word or byte or forward reference etc
                 {
                     if(st1.sval.equalsIgnoreCase("BYTE"))
                     {
                         byt = true;
                     }
                     else if(st1.sval.equalsIgnoreCase("WORD"))
                     {
                         word = true;
                     }
                     else if(st1.sval.equalsIgnoreCase("RESB"))
                     {
                         reswORresb = true;
                         declocctr = Integer.parseInt(locctr,16);
                         declocctr = declocctr + 0x3;
                         locctr = Integer.toHexString(declocctr);
                     }
                     else if(st1.sval.equalsIgnoreCase("RESW"))
                     {
                         reswORresb = true;
                         declocctr = Integer.parseInt(locctr,16);
                         declocctr = declocctr + 0x3;
                         locctr = Integer.toHexString(declocctr);
                     }
                     else
                         //JOptionPane.showMessageDialog(null,"Unidentified identifier. Recheck.")
                         ;
                 }
                 if(charlinecounter == 3)                                       //this only applies when there are 3 chars in a line, this will be a word or byte or forward ref etc
                         ;
                 break;
                  
              case StreamTokenizer.TT_NUMBER:
                 //System.out.println("Number: " + st.nval);
                 charlinecounter++;
                 if(SYM.hashtable.containsKey("START") && st1.lineno() == 1) //1st line of program, storing starting address in locctr
                 {
                     declocctr = (int)st1.nval;
                     locctr = Integer.toString(declocctr);
                     
                     SYM.hashtable.put("START", locctr);
                 }
                 if(word)
                     sizeofword = (int)st1.nval;
                 if(charlinecounter == 1);                                      //1st char never contains a number in sic
                 if(charlinecounter == 2);                                      //2nd char also doesnt have a number in sic
                 if(charlinecounter == 3)                                       //This may sometimes contain var or const definition
                         ;
                 break;
                  
              default:
                 //System.out.println((char) token + " encountered. Not recognized");
                 char c = (char)token;
                 String ch = Character.toString(c);
                 if(ch.equals("'"))
                 {
                     st1.nextToken();
                     if(st1.ttype == StreamTokenizer.TT_NUMBER)
                         sizeofbyte = String.valueOf(st1.nval).length();
                         //System.out.println(sizeofbyte);
                     if(st1.ttype == StreamTokenizer.TT_WORD)
                         sizeofbyte = st1.sval.length();
                     st1.pushBack();
                 }
            }
         } while (!eof);
        
        
         Set<String> keys = SYM.hashtable.keySet();
         String str;
         Iterator<String> it = keys.iterator();
         while (it.hasNext()) 
         { 
             str = it.next();
             System.out.println("Key: "+str+" & Value: "+SYM.hashtable.get(str));
         }
         
         reswORresb = false; byt = false; word = false; eof = false;
         sizeofbyte = 0; sizeofword = 0;
         locctr = "0";
         declocctr = 0;
         charlinecounter = 0;
         
         do                                                                      //Pass2
        {
          int token2 = st2.nextToken();
          switch(token2)
          {
              case StreamTokenizer.TT_EOF:
                 eof = true;
                 break;
              case StreamTokenizer.TT_EOL:
                 charlinecounter = 0;
                 break;
              case StreamTokenizer.TT_NUMBER:
                 charlinecounter++;
                 break;
              case StreamTokenizer.TT_WORD:
                 charlinecounter++;
                 if((charlinecounter == 1) && (OP.hm.containsKey(st2.sval)))  //its a cmd
                 {
                     if(st2.sval.equalsIgnoreCase("RSUB"))
                         System.out.println(OP.hm.get("RSUB") + "0000");
                     if(!(st2.sval.equalsIgnoreCase("RSUB")))
                             {
                     String cmd = OP.hm.get(st2.sval);
                     st2.nextToken();
                     String obj = cmd.concat(SYM.hashtable.get(st2.sval));
                     st2.pushBack();
                     System.out.println(obj);
                             }
                 }
                 /*if((charlinecounter == 1) && (SYM.hashtable.containsKey(st2.sval)))  //its a label
                 {
                     String label = null;
                     st2.nextToken();
                     //if(OP.hm.containsKey(st2.sval))
                     label = OP.hm.get(st2.sval);
                     
                     st2.nextToken();
                     String obj = label.concat(SYM.hashtable.get(st2.sval));
                     st2.pushBack();
                     st2.pushBack();
                     System.out.println(obj);
                 }
                 if((charlinecounter == 2) && (SYM.hashtable.containsKey(st2.sval)))  //its a label at char 2
                 {
                     String cmd = OP.hm.get(st2.sval);
                     st2.nextToken();
                     String obj = cmd.concat(SYM.hashtable.get(st2.sval));
                     st2.pushBack();
                     System.out.println(obj);
                 }*/
                 break;
          }
        }while(!eof);
         
    }
    
  
}

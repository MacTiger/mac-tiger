package compile;

import java.io.FileWriter;
import java.io.IOException;

public class GestionRegisters {


    private final int REGMAX=16;//nb max de registres R0-> R15
    private final String PATH="misc/asm/temp.src";

    private int regDisp;//Registres dispo : 16 -> -N

    public GestionRegisters(){
        regDisp=REGMAX;
    }


    //Met le registre req dans la pile
    private void putRegisterInStack(int reg){
        String str="\tADQ -2, SP \n \tSTW R"+String.valueOf(reg)+", (SP)\n";
        try {
            char buffer[]=new char[str.length()];
            str.getChars(0,str.length(),buffer,0);
            FileWriter fw = new FileWriter(PATH, true);
            fw.write(buffer);
            fw.close();
        }
        catch(Exception ex){
            System.out.println("Pas le bon path");
            ex.printStackTrace();
        }
    }

    private void putStackInRegister(int reg){
        String str="\tLDW R"+String.valueOf(reg)+", (SP) \n \tADQ 2, SP \n";
        try {
            char buffer[]=new char[str.length()];
            str.getChars(0,str.length(),buffer,0);
            FileWriter fw = new FileWriter(PATH, true);
            fw.write(buffer);
            fw.close();
        }
        catch(Exception ex){
            System.out.println("Pas le bon path");
            ex.printStackTrace();
        }
    }

    //Renvoie l'indice d'un registre disponible
    public int GetRegister(){
        if(regDisp>0){
            return --regDisp;//Pour 16 places renvoie 15, etc.
        }
        else{//Registres pleins
            int reg;
            reg=(REGMAX-1)-(-regDisp)%REGMAX;//Registre à libérer.
            //regDisp = 0 -> libere R15 ; regiDisp=-1 -> libère R14 etc.
            regDisp--;
            putRegisterInStack(reg);
            return reg;
        }
    }


    //Libère le dernier registre "réservé"
    public void freeRegister(){
        if(regDisp>-1 && regDisp<REGMAX){
            regDisp++;
        }
        else{
            int req;
            req=(REGMAX-1)-(-(regDisp+1))%REGMAX;
            putStackInRegister(req);
            regDisp++;
        }
    }

}

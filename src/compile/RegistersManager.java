package compile;

import java.io.FileWriter;

public class RegistersManager {

    //R1 à R10
    private final int REGMAX = 10;//nb max de registres R1->R10
    private final String PATH = "misc/asm/temp.src";

    private int regDisp;//Registres dispo : 16 -> -N
    private Writer writer;

    public RegistersManager(Writer writer){

        this.writer = writer;
        regDisp = REGMAX;
    }


    //Met le registre req dans la pile
    private void putRegisterInStack(int reg){
        writer.write("\tADQ -2, SP\n\tSTW R" + String.valueOf(reg) + ", (SP)\n");
    }

    private void putStackInRegister(int reg){
        writer.write("\tLDW R"+String.valueOf(reg)+", (SP) \n \tADQ 2, SP \n");
    }

    //Renvoie l'indice d'un registre disponible
    public int provideRegister() {
        if(regDisp>0){
            return regDisp--;//Pour 16 places renvoie 15, etc.
        }
        else{//Registres pleins
            int reg;
            reg=(REGMAX)-(-regDisp)%REGMAX;//Registre à libérer.
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
            req=(REGMAX)-(-(regDisp+1))%REGMAX;
            putStackInRegister(req);
            regDisp++;
        }
    }

}

package compile;

import java.io.FileWriter;
import java.util.Stack;

public class RegistersManager {

    //R1 à R10
    private final int REGMAX = 10;//nb max de registres R1->R10
    private final String PATH = "misc/asm/temp.src";

    private Stack<Integer> availableRegisters;//Registres dispo : 16 -> -N
    private int peak;
    private Writer writer;

    public RegistersManager(Writer writer){

        this.writer = writer;
        peak = REGMAX;
    }

    public void saveAll() {
        int registersToSave = (peak >= 0) ? (REGMAX - peak) : (REGMAX);

        for (int i = 1; i <= registersToSave; i++) {
            save(i);
        }

        availableRegisters.push(peak);
        peak = REGMAX;
    }

    public void restoreAll() {
        peak = availableRegisters.pop();
        int registersToRestore = (peak >= 0) ? (REGMAX - peak) : (REGMAX);

        for (int i = registersToRestore; i >= 1; i--) {
            restore(i);
        }
    }


    //Met le registre req dans la pile
    private void restore(int reg){
        writer.write("\tADQ -2, SP\n\tSTW R" + String.valueOf(reg) + ", (SP)\n");
    }

    private void save(int reg){
        writer.write("\tLDW R"+String.valueOf(reg)+", (SP) \n \tADQ 2, SP \n");
    }

    //Renvoie l'indice d'un registre disponible
    public int provideRegister() {
        if(peak > 0){
            return peak--;//Pour 16 places renvoie 15, etc.
        }
        else{//Registres pleins
            int reg;
            reg=(REGMAX)-(-peak)%REGMAX;//Registre à libérer.
            //peak = 0 -> libere R15 ; regiDisp=-1 -> libère R14 etc.
            peak--;
            restore(reg);
            return reg;
        }
    }


    //Libère le dernier registre "réservé"
    public void freeRegister(){
        if(peak > -1 && peak < REGMAX){
            peak++;
        }
        else{
            int req;
            req=(REGMAX)-(-(peak+1))%REGMAX;
            restore(req);
            peak++;
        }
    }

}

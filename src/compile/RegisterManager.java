package compile;

import java.util.Stack;

public class RegisterManager {

    //R1 à R10
    private final int REGMAX = 10;//nb max de registres R1->R10

    private Writer writer;
    private Stack<Integer> availableRegisters;
    private int peak;

    public RegisterManager(Writer writer){
        this.writer = writer;
        this.availableRegisters = new Stack<Integer>();
        this.peak = REGMAX;
    }

    public void descend() {//On descend dans le fils : push du nb de regitres disponibles
        availableRegisters.push(peak);
        peak = REGMAX;
    }

    public void ascend() {
        peak = availableRegisters.pop();
    }

    public void saveAll() {
        int registersToSave = (peak >= 0) ? (REGMAX - peak) : (REGMAX);

        for (int i = 1; i <= registersToSave; i++) {
            save(i);
        }
    }

    public void restoreAll() {
        int registersToRestore = (peak >= 0) ? (REGMAX - peak) : (REGMAX);

        for (int i = registersToRestore; i >= 1; i--) {
            restore(i);
        }
    }


    //Met le registre req dans la pile
    private void save(int reg) {
        writer.writeFunction(String.format("STW R%d, -(SP)", reg));
    }

    private void restore(int reg) {
        writer.writeFunction(String.format("LDW R%d, (SP)+", reg));
    }

    //Renvoie l'indice d'un registre disponible
    public int provideRegister() {
        if(peak > 0){
            return peak--;//Pour 16 places renvoie 15, etc.
        }
        else{//Registres pleins
            int reg = REGMAX - -peak % REGMAX;//Registre à libérer.
            //peak = 0 -> libere R15 ; regiDisp=-1 -> libère R14 etc.
            save(reg);
            peak--;
            return reg;
        }
    }


    //Libère le dernier registre "réservé"
    public void freeRegister(){
        if(peak > -1 && peak < REGMAX){
            peak++;
        }
        else{
            peak++;
            int reg = REGMAX - -peak % REGMAX;
            restore(reg);
        }
    }

}

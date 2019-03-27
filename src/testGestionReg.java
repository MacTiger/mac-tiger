import compile.GestionRegisters;

public class testGestionReg {


    public static void main(String[] args){
        GestionRegisters gr;
        gr=new GestionRegisters();

        for(int i=0 ; i<30 ; i++){
            System.out.println(gr.GetRegister());
        }

        for(int i=0;i<30;i++){
            gr.freeRegister();
        }
    }

}

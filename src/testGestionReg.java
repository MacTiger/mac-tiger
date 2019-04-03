import compile.RegistersManager;
import compile.Writer;

public class testGestionReg {


    public static void main(String[] args){
        RegistersManager gr;
        Writer writer = new Writer();
        gr = new RegistersManager(writer);
        int n = 30;

        for(int i=0 ; i<n ; i++){
            gr.provideRegister();
        }

        for(int i=0;i<n;i++){
            gr.freeRegister();
        }

        System.out.println(writer.toString());
    }

}

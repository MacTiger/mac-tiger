import compile.LabelGenerator;
import compile.RegisterManager;
import compile.Writer;

public class testGestionReg {


    public static void main(String[] args){
        RegisterManager gr;
        Writer writer = new Writer(new LabelGenerator(0));
        gr = new RegisterManager(writer);
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

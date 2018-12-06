package symboltable;
import java.util.ArrayList;

public class Record extends Type {

    private ArrayList<Type> fieldTypes;
    private ArrayList<String> fieldIdentifiers;

    public Record(String identifier, ArrayList<Type> fieldTypes, ArrayList<String> fieldIdentifiers) {
        super(identifier, size(fieldTypes));
        // On doit calculer la taille du record, qui est la somme de la taille de tous ses types
        int size = 0;
        for (Type type : fieldTypes) {
            size = size + type.getSize();
        }
        super(identifier, size);
        this.fieldTypes = fieldTypes;
        this.fieldIdentifiers = fieldIdentifiers;
    }

    private ArrayList<Variable> variable;

    public ArrayList<Variable> getVariable() {
        return variable;
    }
}

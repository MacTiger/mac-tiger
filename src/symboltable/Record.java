package symboltable;
import java.util.ArrayList;

public class Record extends Type {

    private ArrayList<Type> fieldTypes;

    private ArrayList<String> fieldIdentifiers;

    public Record(String identifier, ArrayList<Type> fieldTypes, ArrayList<String> fieldIdentifiers) {
        super(identifier);

        // On doit calculer la taille du record, qui est la somme de la taille de tous ses types
        int size = 0;

        for (Type type : fieldTypes) {
            size = size + type.getSize();
        }

        this.setSize(size);
        this.fieldTypes = fieldTypes;
        this.fieldIdentifiers = fieldIdentifiers;
    }

    public ArrayList<Type> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(ArrayList<Type> fieldTypes) {
        this.fieldTypes = fieldTypes;

        // On doit recalculer la taille du record, qui est la somme de la taille de tous ses types
        int size = 0;

        for (Type type : fieldTypes) {
            size = size + type.getSize();
        }

        this.setSize(size);
    }

    public ArrayList<String> getFieldIdentifiers() {
        return fieldIdentifiers;
    }

    public void setFieldIdentifiers(ArrayList<String> fieldIdentifiers) {
        this.fieldIdentifiers = fieldIdentifiers;
    }
}

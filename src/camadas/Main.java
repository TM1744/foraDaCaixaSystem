package camadas;

import camadas.model.dao.Database;

public class Main {
    public static void main(String[] args) {

        try {
            Database db =new Database();
        } catch (RuntimeException e){
            System.err.println("e.getMessage: " + e.getMessage());
        }

    }
}
package codepot.vendingmachine.infrastructure;


import codepot.vendingmachine.domain.VendingMachine;
import dagger.ObjectGraph;

public class Application {

    public static void main(String[] args) {
        VendingMachine vendingMachine = createVendingMachine();
        System.out.println(vendingMachine.getDisplay());
    }

    private static VendingMachine createVendingMachine() {
        ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule());
        return objectGraph.get(VendingMachine.class);
    }
}

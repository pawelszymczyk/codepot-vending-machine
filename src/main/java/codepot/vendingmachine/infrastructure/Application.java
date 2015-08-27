package codepot.vendingmachine.infrastructure;


import codepot.vendingmachine.domain.VendingMachine;

public class Application {

    public static void main(String[] args) {
        VendingMachine vendingMachine = createVendingMachine();
        System.out.println(vendingMachine.getDisplay());
    }

    private static VendingMachine createVendingMachine() {
        VendingMachine vendingMachine = new VendingMachine.Builder().build();

        return vendingMachine;
    }
}

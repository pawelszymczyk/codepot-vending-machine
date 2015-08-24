package codepot.vendingmachine.infrastructure;


import codepot.vendingmachine.domain.VendingMachine;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Application {

    public static void main(String[] args) {
        VendingMachine vendingMachine = createVendingMachine();
        System.out.println(vendingMachine.getDisplay());
    }

    private static VendingMachine createVendingMachine() {
        Injector injector = Guice.createInjector(new VendingMachineModule());

        return injector.getInstance(VendingMachine.class);
    }
}

package codepot.vendingmachine.infrastructure;


import codepot.vendingmachine.domain.VendingMachine;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan("codepot.vendingmachine")
public class Application {

    public static void main(String[] args) {
        VendingMachine vendingMachine = createVendingMachine();
        System.out.println(vendingMachine.getDisplay());
    }

    private static VendingMachine createVendingMachine() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
        return applicationContext.getBean(VendingMachine.class);
    }
}

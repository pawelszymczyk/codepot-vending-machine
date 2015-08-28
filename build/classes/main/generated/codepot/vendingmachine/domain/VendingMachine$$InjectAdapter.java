// Code generated by dagger-compiler.  Do not edit.
package codepot.vendingmachine.domain;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<VendingMachine>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code VendingMachine} and its
 * dependencies.
 *
 * Being a {@code Provider<VendingMachine>} and handling creation and
 * preparation of object instances.
 */
public final class VendingMachine$$InjectAdapter extends Binding<VendingMachine>
    implements Provider<VendingMachine> {
  private Binding<TransactionFactory> transactionFactory;

  public VendingMachine$$InjectAdapter() {
    super("codepot.vendingmachine.domain.VendingMachine", "members/codepot.vendingmachine.domain.VendingMachine", NOT_SINGLETON, VendingMachine.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    transactionFactory = (Binding<TransactionFactory>) linker.requestBinding("codepot.vendingmachine.domain.TransactionFactory", VendingMachine.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    getBindings.add(transactionFactory);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<VendingMachine>}.
   */
  @Override
  public VendingMachine get() {
    VendingMachine result = new VendingMachine(transactionFactory.get());
    return result;
  }

}

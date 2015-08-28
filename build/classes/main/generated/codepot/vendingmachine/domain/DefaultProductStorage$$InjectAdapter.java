// Code generated by dagger-compiler.  Do not edit.
package codepot.vendingmachine.domain;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<DefaultProductStorage>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code DefaultProductStorage} and its
 * dependencies.
 *
 * Being a {@code Provider<DefaultProductStorage>} and handling creation and
 * preparation of object instances.
 */
public final class DefaultProductStorage$$InjectAdapter extends Binding<DefaultProductStorage>
    implements Provider<DefaultProductStorage> {
  private Binding<java.util.List<codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier>> serviceNotifiers;

  public DefaultProductStorage$$InjectAdapter() {
    super("codepot.vendingmachine.domain.DefaultProductStorage", "members/codepot.vendingmachine.domain.DefaultProductStorage", NOT_SINGLETON, DefaultProductStorage.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceNotifiers = (Binding<java.util.List<codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier>>) linker.requestBinding("java.util.List<codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier>", DefaultProductStorage.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    getBindings.add(serviceNotifiers);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<DefaultProductStorage>}.
   */
  @Override
  public DefaultProductStorage get() {
    DefaultProductStorage result = new DefaultProductStorage(serviceNotifiers.get());
    return result;
  }

}

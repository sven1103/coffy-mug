package de.derfilli.coffy;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.derfilli.coffy.service.CoffyService;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route annotation to announce
 * it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button that shows a greeting
 * message in a notification.
 */
@Route
public class MainView extends VerticalLayout {

  private final Div coffeeCardHolder = new Div();

  private final Div accountCardHolder = new Div();

  /**
   * Construct a new Vaadin view.
   * <p>
   * Build the initial UI state for the user accessing the application.
   *
   * @param service The message service. Automatically injected Spring managed bean.
   */
  public MainView(CoffyService service) {
    Button loadCoffees = new Button("Coffee Brands");
    Button loadAccounts = new Button("Accounts");

    loadAccounts.addClickListener(e -> {
      coffeeCardHolder.setVisible(false);
      accountCardHolder.setVisible(true);
      accountCardHolder.removeAll();
      service.getAccounts().map(AccountCard::new)
          .doOnNext(account -> {
            addAccount(account);
            refresh();
          })
          .doOnComplete(this::refresh)
          .subscribe();
    });

    loadCoffees.addClickListener(e -> {
      coffeeCardHolder.setVisible(true);
      accountCardHolder.setVisible(false);
      coffeeCardHolder.removeAll();
      service.getCoffees().map(CoffeeCard::new)
          .doOnNext(
              item ->
              {
                addCoffee(item);
                refresh();
              })
          .doOnComplete(this::refresh)
          .subscribe();
    });
    HorizontalLayout buttons = new HorizontalLayout(loadCoffees, loadAccounts);
    add(buttons, coffeeCardHolder, accountCardHolder);
  }

  private void addAccount(AccountCard account) {
    getUI().ifPresent(ui -> ui.access(() -> accountCardHolder.add(account)));
  }

  protected void addCoffee(CoffeeCard coffee) {
    getUI().ifPresent(ui -> ui.access(() -> coffeeCardHolder.add(coffee)));
  }

  protected void refresh() {
    getUI().ifPresent(ui -> ui.access(ui::push));

  }
}

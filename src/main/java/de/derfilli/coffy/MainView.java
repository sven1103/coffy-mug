package de.derfilli.coffy;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import de.derfilli.coffy.api.CoffyService;
import de.derfilli.coffy.api.Concepts.Account;
import de.derfilli.coffy.api.Concepts.AccountCreationRequest;

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
@UIScope
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

    TextField accountOwner = new TextField("Account: Owner Name");
    accountOwner.setHelperText("Put your name here");
    Button createAccountButton = new Button("Create Account");
    VerticalLayout accountCreation = new VerticalLayout();
    accountCreation.add(accountOwner, createAccountButton);
    createAccountButton.addClickListener(e -> {
      var request = new AccountCreationRequest(accountOwner.getValue().trim());
      service.createAccount(request).subscribe(this::handleAccountCreation);
    });

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
    add(buttons, accountCreation, coffeeCardHolder, accountCardHolder);
  }

  private void handleAccountCreation(Account account) {
    getUI().ifPresent(ui -> {
      ui.access(() -> new Notification("New account created", 5000).open());
    });
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

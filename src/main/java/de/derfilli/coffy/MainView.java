package de.derfilli.coffy;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.UIScope;
import de.derfilli.coffy.api.CoffyService;
import de.derfilli.coffy.api.CoffyService.RequestFailedException;
import de.derfilli.coffy.api.CoffyService.UnknownAccountException;
import de.derfilli.coffy.api.Concepts.Account;
import de.derfilli.coffy.api.Concepts.AccountCreationRequest;
import de.derfilli.coffy.api.Concepts.PurchaseRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;

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

  private final CoffyService coffyService;

  /**
   * Construct a new Vaadin view.
   * <p>
   * Build the initial UI state for the user accessing the application.
   *
   * @param service The message service. Automatically injected Spring managed bean.
   */
  @Autowired
  public MainView(CoffyService service) {
    this.coffyService = Objects.requireNonNull(service);
    Button loadCoffees = new Button("Coffee Brands");
    Button loadAccounts = new Button("Accounts");
    Button purchaseCoffees = new Button("Purchase Coffee");

    var lazyAccountDialog = new LazyAccountDialog(service);
    lazyAccountDialog.addConfirmListener(listener -> {
      listener.getSource().selectedAccount().ifPresent(
          this::submitPurchaseRequest);
    });

    purchaseCoffees.addClickListener(e -> {
      lazyAccountDialog.open();
    });

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
    add(buttons, accountCreation, purchaseCoffees, coffeeCardHolder, accountCardHolder);
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

  private void submitPurchaseRequest(Account selectedAccount) {
    Notification.show("Purchase request submitted");
    PurchaseRequest purchaseRequest = new PurchaseRequest(selectedAccount.id(),
        "883bb0ca-61fc-40ca-97f6-b1aca132db58", new Random().nextInt(10) + 1);
    coffyService.purchase(purchaseRequest)
        .doOnSuccess(purchaseReceipt -> getUI().ifPresent(
            ui -> ui.access(() -> Notification.show("Your account been deposit for %.2f€.".formatted(purchaseReceipt.amount())))))
        .doOnError(UnknownAccountException.class, throwable ->
            getUI().ifPresent(
                ui -> ui.access(() -> Notification.show("Oooops, the account is not available!"))))
        .doOnError(RequestFailedException.class, throwable ->
            getUI().ifPresent(ui -> ui.access(
                () -> Notification.show("Oooops, we could not process your request!"))))
        .subscribe();
  }

  public static class LazyAccountDialog extends Dialog {

    private final transient CoffyService service;
    private final ComboBox<Account> accounts;

    public LazyAccountDialog(CoffyService service) {
      super();
      this.accounts = new ComboBox<>();
      this.service = Objects.requireNonNull(service);
      this.addAttachListener(this::onAttach);
      add(accounts);

      var confirmButton = new Button("Confirm");
      getFooter().add(confirmButton);
      confirmButton.addClickListener(e -> {
        this.fireEvent(new ConfirmEvent(this, true));
      });
    }

    protected void onAttach(AttachEvent event) {
      super.onAttach(event);
      accounts.setItems(service.getAccounts().toStream().toList());
      accounts.setItemLabelGenerator(
          account -> "%s - %d cups consumed so far".formatted(account.owner(),
              account.consumedTotal()));
    }

    public Optional<Account> selectedAccount() {
      return Optional.ofNullable(this.accounts.getValue());
    }

    public Registration addConfirmListener(ComponentEventListener<ConfirmEvent> listener) {
      return addListener(ConfirmEvent.class, listener);
    }
  }

  public static class ConfirmEvent extends ComponentEvent<LazyAccountDialog> {

    public ConfirmEvent(LazyAccountDialog source, boolean fromClient) {
      super(source, fromClient);
    }
  }
}

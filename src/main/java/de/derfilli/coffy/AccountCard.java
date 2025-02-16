package de.derfilli.coffy;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import de.derfilli.coffy.service.Concepts.Account;

/**
 * <b><class short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
public class AccountCard extends Div {

  public AccountCard(Account account) {
    this.addClassName("card");

    Div title = new Div("Account");
    title.addClassNames("card-divider", "account-title");

    Div cardSection = new Div();
    cardSection.addClassName("card-section");

    H4 owner = new H4(account.owner());
    owner.addClassName("account-owner");

    Div balance = new Div("Balance: €%.2f".formatted(account.balance()));

    cardSection.add(owner, balance);
    add(title, cardSection);
  }

}

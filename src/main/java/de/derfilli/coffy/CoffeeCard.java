package de.derfilli.coffy;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import de.derfilli.coffy.api.Concepts.Coffee;

/**
 * <b><class short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
public class CoffeeCard extends Div {

  public CoffeeCard(Coffee coffee) {
    this.addClassName("card");
    Div title = new Div(coffee.name());
    title.addClassNames("card-divider", "coffee-title");

    Div cardSection = new Div();
    cardSection.addClassName("card-section");

    H4 price = new H4("€ %.2f".formatted(coffee.price()));
    price.addClassName("coffee-price");

    Paragraph description = new Paragraph(coffee.info().description());
    description.addClassName("coffee-description");

    H5 originHeader = new H5("Origin");
    Paragraph origin = new Paragraph(coffee.info().origin().isBlank() ? "unknown" : coffee.info().origin());
    origin.addClassName("coffee-origin");

    Div misc = new Div();
    misc.addClassName("coffee-misc");
    for (var entry : coffee.info().misc().entrySet()) {
      misc.add(new Div(entry.getKey() + " - " + entry.getValue()));
    }

    cardSection.add(price, description, originHeader, origin, misc);
    add(title);
    add(cardSection);
  }

}

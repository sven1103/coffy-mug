package de.derfilli.coffy;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import de.derfilli.coffy.service.CoffyService;
import de.derfilli.coffy.service.Concepts.Coffee;
import java.util.ArrayList;
import java.util.List;

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

  /**
   * Construct a new Vaadin view.
   * <p>
   * Build the initial UI state for the user accessing the application.
   *
   * @param service The message service. Automatically injected Spring managed bean.
   */
  public MainView(CoffyService service) {


    Button loadCoffees = new Button("Load coffees");
    loadCoffees.addClickListener(e -> {
      coffeeCardHolder.removeAll();
      service.getCoffees().map(CoffeeCard::new).doOnNext(
          item ->
          {
            addCoffee(item);
            refresh();
          }
      ).doOnComplete(this::refresh
      ).subscribe();
    });

    add(loadCoffees, coffeeCardHolder);
  }

  protected void addCoffee(CoffeeCard coffee) {
    getUI().ifPresent(ui -> ui.access(() -> coffeeCardHolder.add(coffee)));
  }

  protected void refresh() {
    getUI().ifPresent(ui -> ui.access(() -> {
      ui.push();
    }));

  }
}

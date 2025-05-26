
package acme.features.any.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.Service;

@GuiController
public class AnyServiceController extends AbstractGuiController<Any, Service> {

	private final AnyServiceListService	listService;

	private final AnyServiceShowService	showService;


	@Autowired
	public AnyServiceController(final AnyServiceListService listService, final AnyServiceShowService showService) {
		this.listService = listService;
		this.showService = showService;
	}

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}

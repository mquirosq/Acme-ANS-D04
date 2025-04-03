
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int bookingId;
		Collection<BookingRecord> bookingRecords;
		boolean isDraft;

		bookingId = super.getRequest().getData("id", int.class);
		isDraft = this.repository.findBookingDraftById(bookingId);
		bookingRecords = this.repository.findBookingRecordsByBookingId(bookingId);

		super.getBuffer().addData(bookingRecords);
		super.getResponse().addGlobal("draft", isDraft);
		super.getResponse().addGlobal("bookingId", bookingId);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord);
		dataset.put("passenger", bookingRecord.getPassenger().getFullName());
		dataset.put("passport", bookingRecord.getPassenger().getPassportNumber());
		super.addPayload(dataset, bookingRecord, "passenger.birthDate");
		super.getResponse().addData(dataset);
	}
}

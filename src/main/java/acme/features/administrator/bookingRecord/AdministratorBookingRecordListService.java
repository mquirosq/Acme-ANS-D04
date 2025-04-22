
package acme.features.administrator.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.BookingRecord;

@GuiService
public class AdministratorBookingRecordListService extends AbstractGuiService<Administrator, BookingRecord> {

	@Autowired
	private AdministratorBookingRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int bookingId;
		Collection<BookingRecord> bookingRecords;

		bookingId = super.getRequest().getData("masterId", int.class);
		bookingRecords = this.repository.findBookingRecordsByBookingId(bookingId);

		super.getBuffer().addData(bookingRecords);
		super.getResponse().addGlobal("bookingId", bookingId);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord);
		dataset.put("passenger", bookingRecord.getPassenger().getFullName());
		super.addPayload(dataset, bookingRecord, "passenger.birthDate", "passenger.passportNumber");
		super.getResponse().addData(dataset);
	}
}

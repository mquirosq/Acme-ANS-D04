
package acme.components;

import java.util.Date;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import acme.client.components.datatypes.Money;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.forms.MoneyExchange;

public class MoneyExchangeHelper {

	public static MoneyExchange performExchangeToSystemCurrency(final MoneyExchange exchange) {
		Money source;
		String targetCurrency;
		MoneyExchange current;

		source = exchange.getSource();
		targetCurrency = exchange.getTargetCurrency();
		current = MoneyExchangeHelper.computeMoneyExchange(source, targetCurrency);
		exchange.setMessage("authenticated.money-exchange.api-error");
		if (current.getOops() != null) {
			exchange.setTarget(null);
			exchange.setMoment(null);
		} else {
			exchange.setMoment(current.getMoment());
			exchange.setTarget(current.getTarget());
		}
		return exchange;
	}

	protected static MoneyExchange computeMoneyExchange(final Money source, final String targetCurrency) {
		assert source != null;
		assert !StringHelper.isBlank(targetCurrency);

		MoneyExchange result;

		if (SpringHelper.isRunningOn("production"))
			result = MoneyExchangeHelper.computeLiveExchange(source, targetCurrency);
		else
			result = MoneyExchangeHelper.computeMockedExchange(source, targetCurrency);

		return result;
	}

	protected static MoneyExchange computeMockedExchange(final Money source, final String targetCurrency) {
		assert source != null;
		assert !StringHelper.isBlank(targetCurrency);

		MoneyExchange result;
		Date currentMoment;
		Money target;

		currentMoment = MomentHelper.getCurrentMoment();
		target = new Money();
		target.setAmount(source.getAmount());
		target.setCurrency(targetCurrency);

		result = new MoneyExchange();
		result.setMoment(currentMoment);
		result.setSource(source);
		result.setTarget(target);
		result.setTargetCurrency(targetCurrency);

		return result;
	}

	protected static MoneyExchange computeLiveExchange(final Money source, final String targetCurrency) {
		assert source != null;
		assert !StringHelper.isBlank(targetCurrency);

		MoneyExchange result;
		RestTemplate api;
		HttpHeaders headers;
		HttpEntity<String> parameters;
		ResponseEntity<ExchangeRate> response;
		ExchangeRate record;
		String sourceCurrency;
		Double sourceAmount, targetAmount, rate;
		Money target;
		Date moment;

		try {
			sourceCurrency = source.getCurrency();
			sourceAmount = source.getAmount();

			headers = new HttpHeaders();
			headers.add("apikey", "Q8Bzt7rnuqtZiRHLQ2joPothdJaUSuX0");
			parameters = new HttpEntity<String>("parameters", headers);
			api = new RestTemplate();
			response = api.exchange( //				
				"https://api.apilayer.com/exchangerates_data/latest?base={0}&symbols={1}", //
				HttpMethod.GET, //
				parameters, //
				ExchangeRate.class, //
				sourceCurrency, //
				targetCurrency //
			);
			assert response != null && response.getBody() != null;
			record = response.getBody();
			assert record != null && record.getRates().containsKey(targetCurrency);
			rate = record.getRates().get(targetCurrency);

			targetAmount = rate * sourceAmount;

			target = new Money();
			target.setAmount(targetAmount);
			target.setCurrency(targetCurrency);

			moment = record.getDate();

			result = new MoneyExchange();
			result.setSource(source);
			result.setTargetCurrency(targetCurrency);
			result.setMoment(moment);
			result.setTarget(target);

			MomentHelper.sleep(1000); // HINT: need to pause the requests to the API to prevent banning!
		} catch (final Throwable oops) {
			result = new MoneyExchange();
			result.setOops(oops);
		}

		return result;
	}

}

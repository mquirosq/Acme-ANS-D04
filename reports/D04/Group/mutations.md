# Implemented Mutations Report

Mutations were performed on the branch `task084`.

## 1. Change pattern of IATA Code in Airport Entity

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/Airport.java`  
**Status:** Detected - multiple tests fail, especially administrator/airport/create and administrator/airport/update tests

**Code Change:**
```java
// Before
	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}$", message = "{acme.validation.airport.IATACodePattern.message}")
	@Column(unique = true)
	private String				IATACode;

// After
	@Mandatory
	@ValidString(pattern = "^[A-Z]{2}$", message = "{acme.validation.airport.IATACodePattern.message}")
	@Column(unique = true)
	private String				IATACode;
```

## 2. Increase maximum length of Name in Airport Entity

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/Airport.java`  
**Status:** Detected - multiple tests fail, especially administrator/airport/create and administrator/airport/update tests

**Code Change:**
```java
// Before
	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				name;

// After
	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				name;
```

## 3. Change name of readonly field in Administrator Airport Show

**File:** `/Acme-ANS-D04/src/main/java/acme/features/administrator/airport/AdministratorAirportShowService.java`  
**Status:** Detected - multiple tests fail, especially administrator/airport/show tests.

**Code Change:**
```java
// Before
	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(AirportScope.class, airport.getScope());

		dataset = super.unbindObject(airport, "name", "IATACode", "scope", "website", "email", "phoneNumber", "city", "country");
		dataset.put("readonly", true);
		dataset.put("scopes", choices);

		super.getResponse().addData(dataset);
	}

// After
	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(AirportScope.class, airport.getScope());

		dataset = super.unbindObject(airport, "name", "IATACode", "scope", "website", "email", "phoneNumber", "city", "country");
		dataset.put("readOnly", true);
		dataset.put("scopes", choices);

		super.getResponse().addData(dataset);
	}
```

## 4. Negate Validation confirmation attribute in Administrator Airport create

**File:** `/Acme-ANS-D04/src/main/java/acme/features/administrator/airport/AdministratorAirportCreateService.java`
**Status:** Detected - multiple tests fail, especially administrator/airport/create tests.

**Code Change:**
```java
// Before
	@Override
	public void validate(final Airport airport) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

// After
	@Override
	public void validate(final Airport airport) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(!confirmation, "confirmation", "acme.validation.confirmation.message");
	}
```

## 5. Incorrectly spell an attribute in the unbind method in Administrator Airport update

**File:** `/Acme-ANS-D04/src/main/java/acme/features/administrator/airport/AdministratorAirportUpdateService.java`  
**Status:** Detected - multiple tests fail, especially administrator/airport/update tests

**Code Change:**
```java
// Before
	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "IATACode", "scope", "website", "email", "phoneNumber", "city", "country");
	}

// After
	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "IATACode", "scope", "website", "email", "phoneNumber", "city", "contry");
	}
```

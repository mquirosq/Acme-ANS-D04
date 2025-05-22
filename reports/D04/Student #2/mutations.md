# Implemented Mutations Report

## 1. Reduced Length Limit for Passenger's fullName Attribute

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/Passenger.java`  
**Status:** Detected - multiple tests fail, especially customer/passenger create and update

**Code Change:**
```java
// Before
@Mandatory
@ValidString(min = 1, max = 255)
@Automapped
private String fullName;

// After
@Mandatory
@ValidString(min = 1, max = 50)
@Automapped
private String fullName;
```

## 2. Changed Flight Filtering Logic in Booking Update

**File:** `/Acme-ANS-D04/src/main/java/acme/features/customer/booking/CustomerBookingUpdateService.java`  
**Status:** Detected - multiple tests fail, especially customer/booking update tests

**Code Change:**
```java
// Before
.filter(f -> MomentHelper.isAfter(f.getScheduledDeparture(), currentMoment))

// After
.filter(f -> MomentHelper.isBefore(f.getScheduledDeparture(), currentMoment))
```

## 3. Modified Authorization Logic in BookingRecord Show

**File:** `/Acme-ANS-D04/src/main/java/acme/features/customer/bookingRecord/CustomerBookingRecordShowService.java`  
**Status:** Detected - multiple tests fail, especially customer/booking-record/show tests

**Code Change:**
```java
// Before
authorised = bookingRecord != null && super.getRequest().getPrincipal().getActiveRealm().equals(booking.getCustomer());

// After
authorised = bookingRecord != null || super.getRequest().getPrincipal().getActiveRealm().equals(booking.getCustomer());
```

## 4. Inverted Condition in Passenger List Loading

**File:** `/Acme-ANS-D04/src/main/java/acme/features/customer/passenger/CustomerPassengerListService.java`  
**Status:** Detected - multiple tests fail, especially customer/passenger/list tests

**Code Change:**
```java
// Before
if (allMode)
    passengers = this.repository.findAllPassengersOfCustomer(customerId);
else
    passengers = this.repository.findPassengersOfCustomer(customerId);

// After
if (!allMode)
    passengers = this.repository.findAllPassengersOfCustomer(customerId);
else
    passengers = this.repository.findPassengersOfCustomer(customerId);
```

## 5. Swapped Parameter Order in Repository Call

**File:** `/Acme-ANS-D04/src/main/java/acme/features/customer/bookingRecord/CustomerBookingRecordCreateService.java`  
**Status:** Detected - multiple tests fail, especially customer/booking-record/create tests

**Code Change:**
```java
// Before
passengers = this.repository.findMyPassengersNotAlreadyInBooking(customerId, bookingId);

// After
passengers = this.repository.findMyPassengersNotAlreadyInBooking(bookingId, customerId);
```

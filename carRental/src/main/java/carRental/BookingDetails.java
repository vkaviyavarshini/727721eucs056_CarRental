package carRental;

import java.sql.Date;

public class BookingDetails {
    private int bookId;
    private Date returnDate;
    private int custId;
    private int carId;
    
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	
	public BookingDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BookingDetails(Date returnDate, int custId, int carId) {
		this.returnDate = returnDate;
        this.custId = custId;
        this.carId = carId;
    }
	
	public BookingDetails(int bookId, Date returnDate, int custId, int carId) {
        this.bookId = bookId;
        this.returnDate = returnDate;
        this.custId = custId;
        this.carId = carId;
    }
	
	 @Override
	    public String toString() {
	        return "Booking ID: " + bookId +
	                ", Return Date: " + returnDate +
	                ", Customer ID: " + custId +
	                ", Car ID: " + carId;
	    } //polymorphism
    
}

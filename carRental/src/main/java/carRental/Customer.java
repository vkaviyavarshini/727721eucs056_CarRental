package carRental;

public class Customer {
    private int custId;
    private String name;
    private String address;
    private String email;
    private int age;
    private String gender;
    private String phone;
    private String licenseNo;
    private String password;
    
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Customer(int custId, String name, String address, String email, String gender, int age, String phone,
			String licenseNo, String password) {
		super();
		this.custId = custId;
		this.name = name;
		this.address = address;
		this.email = email;
		this.age = age;
		this.gender = gender;
		this.phone = phone;
		this.licenseNo = licenseNo;
		this.password = password;
	}
	public Customer(String name, String address, String email,String password, int age, String gender, String phone, String licenseNo) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.licenseNo = licenseNo;
        this.password = password;
    }
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}

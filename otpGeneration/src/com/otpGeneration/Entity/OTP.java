package com.otpGeneration.Entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "otp_table", indexes = {
    @Index(name = "unique_email", columnList = "email", unique = true)
})
public class OTP implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 191)  // Fix: Reduced length
    private String email;

    @Column(nullable = false, length = 6)
    private String otpCode;

    // Constructors
    public OTP() {}

    public OTP(String email, String otpCode) {
        this.email = email;
        this.otpCode = otpCode;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
}

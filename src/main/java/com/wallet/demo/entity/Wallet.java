package com.wallet.demo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wallet Entity Object using lombok and hibernate/JPA
 * 
 * @author Iden.Teo
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "Name can't be blank")
	@Size(min = 2, max = 30)
	private String name;
	@NotNull
	@Size(min = 2, max = 30)
	private String accountNumber;
	@Size(max = 100)
	private String description;
	@Min(1)
	@Max(3)
	private Integer priority; // 1=High; 2=Medium; 3=Low
	@ElementCollection
	private List<Integer> coinList = new ArrayList<Integer>();

}

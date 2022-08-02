package com.mulakatbank;

import com.mulakatbank.model.*;
import com.mulakatbank.repository.AccountRepository;
import com.mulakatbank.repository.AddressRepository;
import com.mulakatbank.repository.CustomerRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@EnableCaching
public class MulakatBankApplication implements CommandLineRunner {

	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final AddressRepository addressRepository;

	public MulakatBankApplication(AccountRepository accountRepository,
								  CustomerRepository customerRepository,
								  AddressRepository addressRepository) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
		this.addressRepository = addressRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(MulakatBankApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI(@Value("${application-description}") String description,
								 @Value("${application-version}") String version){
		return new OpenAPI()
				.info(new Info()
				.title("MulakatBank API")
				.version(version)
				.description(description)
				.license(new License().name("MulakatBank API Licence")));
	}



	@Override
	public void run(String... args) throws Exception {
		Customer c1 = Customer.builder()
				.id("1234568")
				.name("enveR")
				.address(Address.builder().city(City.ISTANBUL).postcode("34880").addressDetails("bu bir adrestir").build())
				.city(City.ISTANBUL)
				.dateOfBirth(1986)
				.build();


		Customer c2 = Customer.builder()
				.id("789456")
				.name("AHMET")
				.city(City.IZMIR)
				.address(Address.builder().city(City.IZMIR).postcode("35640").addressDetails("bu bir adrestir 2").build())
				.dateOfBirth(1992)
				.build();

		Customer c3 = Customer.builder()
				.id("EREN")
				.name("")
				.city(City.IZMIR)
				 .address(Address.builder().city(City.IZMIR).postcode("35450").addressDetails("bu bir adrestir 3").build())
				.dateOfBirth(1982)
				.build();

		customerRepository.saveAll(Arrays.asList(c1,c2,c3));

		Account accountOne = Account.builder()
				.id("100")
				.customerId("1234568")
				.city(City.ISTANBUL)
				.balance(1320.0)
				.currency(Currency.TRY)
				.build();
		Account accountTwo = Account.builder()
				.id("101")
				.customerId("789456")
				.city(City.ISTANBUL)
				.balance(5600.0)
				.currency(Currency.TRY)
				.build();
		Account accountThre = Account.builder()
				.id("102")
				.customerId("456238")
				.city(City.ISTANBUL)
				.balance(90000.0)
				.currency(Currency.TRY)
				.build();

		accountRepository.saveAll(Arrays.asList(accountOne,accountTwo,accountThre));
	}





}


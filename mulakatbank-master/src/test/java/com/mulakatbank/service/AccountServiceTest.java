package com.mulakatbank.service;

import com.mulakatbank.dto.AccountDto;
import com.mulakatbank.dto.AccountDtoMapper;
import com.mulakatbank.dto.CreateAccountRequest;
import com.mulakatbank.model.*;
import com.mulakatbank.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.kafka.core.KafkaTemplate;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountRepository accountRepository;
    private CustomerService customerService;
    private AccountDtoMapper accountDtoMapper;
    private DirectExchange exchange;
    private AmqpTemplate rabbitTemplate;
    private KafkaTemplate kafkaTemplate;

    @Before
    public void setUp() throws Exception {
        accountRepository = Mockito.mock(AccountRepository.class);
        customerService = Mockito.mock(CustomerService.class);
        accountDtoMapper = Mockito.mock(AccountDtoMapper.class);
        exchange = Mockito.mock(DirectExchange.class);
        rabbitTemplate = Mockito.mock(AmqpTemplate.class);
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);

        accountService = new AccountService(accountRepository,
                                            customerService,
                accountDtoMapper, exchange, rabbitTemplate, kafkaTemplate);
    }

    @Test
    public void whenCreateAccountCalledWithValidRequest_itShouldReturnValidAccountDto(){
        CreateAccountRequest createAccountRequest = generateCreateAccountRequest();
        Customer customer = generateCustomer();
        Account account = generateAccount(createAccountRequest);
        AccountDto accountDto = generateAccountDto();

        //Determine mock services behavior regarding test scenario
        Mockito.when(customerService.getCustomerById("12345")).thenReturn(customer);
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Mockito.when(accountDtoMapper.mapper(account)).thenReturn(accountDto);

        //Call the testing method
        AccountDto result = accountService.createAccount(createAccountRequest);

        //Check results and verify the mock methods are called
        Assert.assertEquals(result, accountDto);
        Mockito.verify(customerService).getCustomerById("87654");
        Mockito.verify(accountRepository).save(account);
        Mockito.verify(accountDtoMapper).mapper(account);
    }

    @Test(expected = RuntimeException.class)
    public void whenCreateAccountCalledWithNonExistCustomer_itShouldThrowRuntimeException(){
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("1234");
        createAccountRequest.setCustomerId("87654");
        createAccountRequest.setBalance(3000.0);
        createAccountRequest.setCity(City.ISTANBUL);
        createAccountRequest.setCurrency(Currency.TRY);

        Mockito.when(customerService.getCustomerById("87654")).thenReturn(Customer.builder().build());

        AccountDto expectedAccountDto = AccountDto.builder().build();

        AccountDto result = accountService.createAccount(createAccountRequest);

        Assert.assertEquals(result, expectedAccountDto);
        Mockito.verifyNoInteractions(accountRepository);
        Mockito.verifyNoInteractions(accountDtoMapper);
    }

    @Test(expected = RuntimeException.class)
    public void whenCreateAccountCalledWithWithEmptyCustomerId_itShouldThrowRuntimeException(){
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("1234");
        createAccountRequest.setCustomerId("87654");
        createAccountRequest.setBalance(3000.0);
        createAccountRequest.setCity(City.ISTANBUL);
        createAccountRequest.setCurrency(Currency.TRY);

        Mockito.when(customerService.getCustomerById("87654")).thenReturn(Customer.builder()
                .id(" ")
                .build());

        AccountDto expectedAccountDto = AccountDto.builder().build();

        AccountDto result = accountService.createAccount(createAccountRequest);

        Assert.assertEquals(result, expectedAccountDto);
        Mockito.verifyNoInteractions(accountRepository);
        Mockito.verifyNoInteractions(accountDtoMapper);
    }

    @Test(expected = RuntimeException.class)
    public void whenCreateAccountCalledAndRepositoryThrewException_itShouldThrowException() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("1234");
        createAccountRequest.setCustomerId("87654");
        createAccountRequest.setBalance(3000.0);
        createAccountRequest.setCity(City.ISTANBUL);
        createAccountRequest.setCurrency(Currency.TRY);

        Customer customer = Customer.builder()
                .id("12345")
                .address(Address.builder().city(City.ISTANBUL).postcode("34880").addressDetails("adres posta kodu").build())
                .city(City.ISTANBUL)
                .dateOfBirth(1986)
                .name("Enver")
                .build();

        Account account = Account.builder()
                .id(createAccountRequest.getId())
                .balance(createAccountRequest.getBalance())
                .currency(createAccountRequest.getCurrency())
                .customerId(createAccountRequest.getCustomerId())
                .city(createAccountRequest.getCity())
                .build();


        Mockito.when(customerService.getCustomerById("87654")).thenReturn(customer);
        Mockito.when(accountRepository.save(account)).thenThrow(new RuntimeException("Runtime hatası!!"));

        accountService.createAccount(createAccountRequest);

        Mockito.verify(customerService).getCustomerById("87654");
        Mockito.verify(accountRepository).save(account);
        Mockito.verifyNoInteractions(accountDtoMapper);
    }

    private CreateAccountRequest generateCreateAccountRequest(){
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("1234");
        createAccountRequest.setCustomerId("87654");
        createAccountRequest.setBalance(3000.0);
        createAccountRequest.setCity(City.ISTANBUL);
        createAccountRequest.setCurrency(Currency.TRY);
        return createAccountRequest;
    }

    private Customer generateCustomer() {
        return Customer.builder()
                .id("87654")
                .address(Address.builder().city(City.ISTANBUL).postcode("34880").addressDetails("adres posta kodu").build())
                .city(City.ISTANBUL)
                .dateOfBirth(1986)
                .name("enver")
                .build();
    }

    private Account generateAccount(CreateAccountRequest accountRequest) {
        return Account.builder()
                .id(accountRequest.getId())
                .balance(accountRequest.getBalance())
                .currency(accountRequest.getCurrency())
                .customerId(accountRequest.getCustomerId())
                .city(accountRequest.getCity())
                .build();
    }

    private AccountDto generateAccountDto() {
        return AccountDto.builder()
                .id("1234")
                .customerId("87654")
                .currency(Currency.TRY)
                .balance(3000.0)
                .build();
    }


}
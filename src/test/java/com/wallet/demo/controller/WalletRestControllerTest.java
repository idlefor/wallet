package com.wallet.demo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.demo.entity.Wallet;
import com.wallet.demo.service.WalletService;

/**
 * Unit Test for Wallet Rest Controller
 * 
 * @author Iden.Teo
 * 
 */
@SpringBootTest
@AutoConfigureMockMvc
public class WalletRestControllerTest {
	@MockBean
	private WalletService service;
	@Autowired
	private MockMvc mockMvc;

	@Tag("DEV")
	@Test
	@DisplayName("Test - getAll() - /wallet/all/1 - Failure Get All Wallet")
	void testGetAllWallet_Failure() throws Exception {
		// TDD Execute the GET request failure test
		mockMvc.perform(get("/wallet/all/1").contentType(MediaType.APPLICATION_JSON))
				// Validate the response code and content type
				.andExpect(status().isNotFound());
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - getAll() - /wallet/all - Successful Get All Wallet without Content")
	void testGetAllWallet_SucessWithNoContent() throws Exception {
		// TDD Execute the GET request failure test
		mockMvc.perform(get("/wallet/all").contentType(MediaType.APPLICATION_JSON))
				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// Validate the returned fields
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - getAll() - /wallet/all - Successful Get All Wallet with Content")
	void testGetAllWallet_SuccessWithContent() throws Exception {
		Wallet wallet1 = new Wallet();
		wallet1.setId(1L);
		wallet1.setName("Wallet 1");
		wallet1.setAccountNumber("1234");
		Wallet wallet2 = new Wallet();
		wallet2.setId(2L);
		wallet2.setName("Wallet 2");
		wallet2.setAccountNumber("2345");
		doReturn(Lists.newArrayList(wallet1, wallet2)).when(service).getAll();

		// TDD Execute the GET request pass test
		mockMvc.perform(get("/wallet/all").contentType(MediaType.APPLICATION_JSON))
				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// Validate the returned fields
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("Wallet 1"))).andExpect(jsonPath("$[0].accountNumber", is("1234")))
				.andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].name", is("Wallet 2")))
				.andExpect(jsonPath("$[1].accountNumber", is("2345")));
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - getById() - /wallet/{id} - Failure Get Wallet with Id")
	void testGetByIdWallet_Failure() throws Exception {
		// TDD Execute the GET request failure test if never mock any return object list
		mockMvc.perform(get("/wallet/invalid"))
				// Validate the response code and content type
				.andExpect(status().isBadRequest());
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - getById() - /wallet/{id} - Successful Get Wallet with Id")
	void testGetByIdWallet_SuccessWithContent() throws Exception {
		Wallet wallet1 = new Wallet();
		wallet1.setId(1L);
		wallet1.setName("Wallet 1");
		wallet1.setAccountNumber("4456");
		doReturn(wallet1).when(service).getById(1L);

		// TDD Execute the GET request pass test
		mockMvc.perform(get("/wallet/1").contentType(MediaType.APPLICATION_JSON))
				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Wallet 1")))
				.andExpect(jsonPath("$.accountNumber", is("4456")));
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - create() - /wallet/{requestBody} - Failure Create Wallet")
	void testCreateWallet_Failure() throws Exception {
		// TDD Execute the Post request failure test
		mockMvc.perform(post("/wallet").contentType(MediaType.APPLICATION_JSON))
				// Validate the response code and content type
				.andExpect(status().isBadRequest());
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - create() - /wallet/{requestBody}  - Successful Create Wallet")
	void testCreateWallet_SuccessWithContent() throws Exception {
		Wallet wallet1 = new Wallet();
		wallet1.setId(1L);
		wallet1.setName("Wallet 1");
		wallet1.setAccountNumber("0001");
		doReturn(wallet1).when(service).createOrUpdate(wallet1);

		// TDD Execute the Post request pass test
		mockMvc.perform(post("/wallet").contentType(MediaType.APPLICATION_JSON).content(asJsonString(wallet1)))
				// Validate the response code and content type
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Wallet 1")))
				.andExpect(jsonPath("$.accountNumber", is("0001")));

		verify(service, times(1)).createOrUpdate(isA(Wallet.class));
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - update() - /wallet/{id}/{requestBody} - Failure Update Wallet")
	void testUpdateWallet_Failure() throws Exception {
		// TDD Execute the Put request failure test
		mockMvc.perform(put("/wallet/1").contentType(MediaType.APPLICATION_JSON))
				// Validate the response code and content type
				.andExpect(status().isBadRequest());
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - update() - /wallet/{id}/{requestBody}  - Successful Update Wallet")
	void testUpdateWallet_SuccessWithContent() throws Exception {
		Wallet wallet1 = new Wallet();
		wallet1.setId(1L);
		wallet1.setName("Wallet 1");
		wallet1.setAccountNumber("4444");
		Wallet walletSave = new Wallet();
		walletSave.setId(1L);
		walletSave.setName("Wallet 1 Change");
		walletSave.setAccountNumber("5555");
		doReturn(walletSave).when(service).createOrUpdate(any(Wallet.class));

		// TDD Execute the Put request pass test
		mockMvc.perform(put("/wallet/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(wallet1)))
				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Wallet 1 Change")))
				.andExpect(jsonPath("$.accountNumber", is("5555")));

		verify(service, times(1)).createOrUpdate(isA(Wallet.class));
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - delete() - /wallet/[id} - Failure Delete Wallet")
	void testDeleteWallet_Failure() throws Exception {
		// TDD Execute the Delete request failure test
		mockMvc.perform(delete("/wallet/invalidId").contentType(MediaType.APPLICATION_JSON))
				// Validate the response code and content type
				.andExpect(status().isBadRequest());
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - delete() - /wallet/{id}  - Successful Delete Wallet")
	void testDeleteWallet_SuccessWithContent() throws Exception {
		Wallet wallet1 = new Wallet();
		wallet1.setId(1L);
		wallet1.setName("Wallet 1");
		wallet1.setAccountNumber("7777");
		doReturn(wallet1).when(service).getById(1L);

		// TDD Execute the Delete request pass test
		mockMvc.perform(delete("/wallet/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(wallet1)))
				// Validate the response code
				.andExpect(status().isOk());

		verify(service, times(1)).delete(1L);
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - payCoinFromCoinList() - /{id}/{paycoin}/{exactpayment} - Expect HTTP Response 404 if omit put wallet Id")
	void testPayCoinFromCoinList_ExpectHttp404Response() throws Exception {
		List<Integer> mockCoinList = new LinkedList<Integer>(Arrays.asList(1, 2, 2, 3));
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		wallet.setName("Wallet 1");
		wallet.setAccountNumber("0001");
		wallet.setCoinList(mockCoinList);
		doReturn(wallet).when(service).getById(1L);

		// TDD Execute the Put request fail test that will result in 404
		mockMvc.perform(put("/wallet/1/true").contentType(MediaType.APPLICATION_JSON).content(asJsonString(wallet)))
				// Validate the response code and content type
				.andExpect(status().isNotFound());
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - payCoinFromCoinList() - /{id}/{paycoin}/{exactpayment} - Expect HTTP Response 500 if omit coinList")
	void testPayCoinFromCoinList_ExpectHttp500Response() throws Exception {
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		wallet.setName("Wallet 1");
		wallet.setAccountNumber("0001");
		doReturn(wallet).when(service).getById(1L);

		// TDD Execute the Put request fail test that will result in 500
		mockMvc.perform(put("/wallet/1/1/true").contentType(MediaType.APPLICATION_JSON).content(asJsonString(wallet)))
				// Validate the response code and content type
				.andExpect(status().isInternalServerError());
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - payCoinFromCoinList() - /{id}/{paycoin}/{exactpayment}  - Insuffice Coin to Pay when pay(10)")
	void testPayCoinFromCoinList_NotEnoughCoinToPay() throws Exception {
		List<Integer> mockCoinList = new LinkedList<Integer>(Arrays.asList(1, 2, 2, 3));
		List<Integer> changeCoinList = new LinkedList<Integer>(Arrays.asList(-1));
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		wallet.setName("Wallet 1");
		wallet.setAccountNumber("0001");
		wallet.setCoinList(mockCoinList);
		doReturn(wallet).when(service).getById(1L);
		doReturn(changeCoinList).when(service).computeCoinPayment(10, mockCoinList, true);
		doReturn(wallet).when(service).createOrUpdate(any(Wallet.class));

		// TDD Execute the Put request fail test if we put in payCoin is 10 which become
		// not enough to pay
		mockMvc.perform(put("/wallet/1/10/true").contentType(MediaType.APPLICATION_JSON).content(asJsonString(wallet)))
				// Validate the response code and content type
				.andExpect(status().isInternalServerError());

		verify(service, times(1)).getById(1L);
		verify(service, times(1)).computeCoinPayment(10, mockCoinList, true);
		// verify there is not saving of the wallet due to inSuffice coin to pay
		verify(service, times(0)).createOrUpdate(isA(Wallet.class));
	}

	@Tag("DEV")
	@Test
	@DisplayName("Test - payCoinFromCoinList() - /{id}/{paycoin}/{exactpayment}  - Successful Payment")
	void testPayCoinFromCoinList_SuccessPayment() throws Exception {
		List<Integer> mockCoinList = new LinkedList<Integer>(Arrays.asList(1, 2, 2, 3));
		List<Integer> changeCoinList = new LinkedList<Integer>(Arrays.asList(2, 2, 3));
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		wallet.setName("Wallet 1");
		wallet.setAccountNumber("0001");
		wallet.setCoinList(mockCoinList);
		doReturn(wallet).when(service).getById(1L);
		doReturn(changeCoinList).when(service).computeCoinPayment(1, mockCoinList, true);
		doReturn(wallet).when(service).createOrUpdate(any(Wallet.class));

		// TDD Execute the Put request pass test
		mockMvc.perform(put("/wallet/1/1/true").contentType(MediaType.APPLICATION_JSON).content(asJsonString(wallet)))
				// Validate the response code and content type
				.andExpect(status().isOk())
				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Wallet 1")))
				.andExpect(jsonPath("$.accountNumber", is("0001")));

		verify(service, times(1)).getById(1L);
		verify(service, times(1)).computeCoinPayment(1, mockCoinList, true);
		verify(service, times(1)).createOrUpdate(isA(Wallet.class));
	}

	/**
	 * Static method to create json response body from any wallet object use to feed
	 * into the mockMVc on any http test
	 * 
	 */
	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

package com.wallet.demo.service;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.wallet.demo.entity.Wallet;
import com.wallet.demo.exception.WalletException;
import com.wallet.demo.exception.WalletNotEnoughPayException;
import com.wallet.demo.repository.WalletRepository;

/**
 * Unit Test for Wallet Service Business Logic class
 * 
 * @author Iden.Teo
 * 
 */
@SpringBootTest
public class WalletServiceTest {
	@Autowired
	private WalletService service;
	@MockBean
	private WalletRepository repository;

	@Tag("DEV")
	@DisplayName("Test - computeCoinPayment() - Expect coinSum ZERO if never provide valid input")
	@Test
	void testComputeCoinPayment_ExpectZeroCoinSum_IfNeverProvidedValidInput() {
		// TDD write unit test on expect 0 coinSum if no input provided
		Assertions.assertEquals(service.computeCoinPayment(0, new ArrayList<>()), new ArrayList<>());
	}

	@Tag("DEV")
	@DisplayName("Test - computeCoinPayment() - [1st test case]: [1,1,2,2,3] become [1,2,2,3] after execute pay[1]")
	@Test
	void testComputeCoinPayment_TestCaseScenario1_Pay1() {
		List<Integer> mockCoinList = new LinkedList<Integer>(Arrays.asList(1, 1, 2, 2, 3));

		// TDD write pass unit test on expect result if provided valid input
		Assertions.assertEquals(service.computeCoinPayment(1, mockCoinList), Arrays.asList(1, 2, 2, 3));
	}

	@Tag("DEV")
	@DisplayName("Test - handleCoinPaymentWithoutChange() - [2nd test case]: [1,2,2,3] become [2,3] after execute pay[3]")
	@Test
	void testComputeCoinPayment_TestCaseScenario2_Pay3() {
		List<Integer> mockCoinList = new LinkedList<Integer>(Arrays.asList(1, 2, 2, 3));

		// TDD write pass unit test on expect result if provided valid input
		Assertions.assertEquals(service.computeCoinPayment(3, mockCoinList), Arrays.asList(2, 3));
	}

	@Tag("DEV")
	@DisplayName("Test - computeCoinPayment() - [3rd test case]: [2,3] become [1,3] after execute pay[1]")
	@Test
	void testComputeCoinPayment_TestCaseScenario3_Pay1() {
		List<Integer> mockCoinList = new LinkedList<Integer>(Arrays.asList(2, 3));

		// TDD write pass unit test on expect result if provided valid input
		Assertions.assertEquals(service.computeCoinPayment(1, mockCoinList), Arrays.asList(1, 3));
	}

	@Tag("DEV")
	@DisplayName("Test - computeCoinPayment() - [4th test case]: [1,3] become [1,1] after execute pay[2]")
	@Test
	void testComputeCoinPayment_TestCaseScenario4_Pay2() {
		List<Integer> mockCoinList = new LinkedList<Integer>(Arrays.asList(1, 3));

		// TDD write pass unit test on expect result if provided valid input
		Assertions.assertEquals(service.computeCoinPayment(2, mockCoinList), Arrays.asList(1, 1));
	}

	@Tag("DEV")
	@DisplayName("Test - computeCoinPayment() - [5th test case]: [2] unable pay after execute pay[5]")
	@Test
	void testComputeCoinPayment_TestCaseScenario5_Pay5() {
		List<Integer> mockCoinList = new LinkedList<Integer>(Arrays.asList(2));

		// TDD write failed unit test to verify wallet unable to delete
		Assertions.assertThrows(WalletNotEnoughPayException.class, () -> {
			service.computeCoinPayment(5, mockCoinList);
		});
	}

	@Tag("DEV")
	@DisplayName("Test - getById() - Failure Due Wallet Not Exist")
	@Test
	void testGetById_ExpectWalletExceptionThrow_Failure() {
		Wallet wallet1 = new Wallet();
		wallet1.setId(1L);
		doReturn(Optional.of(wallet1)).when(repository).findById(1L);

		// TDD write failed unit test on WalletException throw
		Assertions.assertThrows(WalletException.class, () -> {
			service.getById(2L);
		});
	}

	@Tag("DEV")
	@DisplayName("Test - getById() - Failure Due Wallet Not Same")
	@Test
	void testGetById_NotSameWallet_Failure() {
		Wallet wallet1 = new Wallet();
		wallet1.setId(1L);
		doReturn(Optional.of(wallet1)).when(repository).findById(1L);
		Wallet wallet2 = new Wallet();
		wallet2.setId(1L);
		doReturn(Optional.of(wallet2)).when(repository).findById(2L);

		Wallet returnedWallet = service.getById(2L);

		// TDD write failed unit test if wallet not same
		Assertions.assertNotSame(returnedWallet, wallet1, "The wallet returned was not the same as the mock");
	}

	@Tag("DEV")
	@DisplayName("Test - getById() - Successful Find the Same Wallet")
	@Test
	void testGetById_SameWallet_Success() {
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		doReturn(Optional.of(wallet)).when(repository).findById(1L);

		Wallet returnedWallet = service.getById(1L);

		// TDD write pass unit test and assert the response
		Assertions.assertSame(returnedWallet, wallet, "The wallet returned was not the same as the mock");
	}

	@Tag("DEV")
	@DisplayName("Test - createOrUpdate() - Failure Due Invalid Input Null Object to save")
	@Test
	void testGetById_createOrUpdate_FailureDueToSaveNullObject() {
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		wallet.setName("testing this method");

		// TDD write failed unit test on nothing is save
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.createOrUpdate(null);
		});

		verifyNoInteractions(repository);
	}

	@Tag("DEV")
	@DisplayName("Test - createOrUpdate() - Successful on Saving Wallet Object With Id provided")
	@Test
	void testGetById_createOrUpdate_SuccessSaveWalletObjectWithId() {
		Wallet wallet = new Wallet();
		wallet.setId(1L);

		// TDD write pass unit test to verify wallet is save if Id provided
		Wallet returnedWallet = service.createOrUpdate(wallet);

		verify(repository, times(1)).save(isA(Wallet.class));
		Assertions.assertSame(returnedWallet, wallet, "The wallet returned was not the same as the mock");
	}

	@Tag("DEV")
	@DisplayName("Test - createOrUpdate() - Successful on Saving Wallet Object WithOut Id provided")
	@Test
	void testGetById_createOrUpdate_SuccessSaveWalletObjectWithoutId() {
		Wallet wallet = new Wallet();

		// TDD write pass unit test to verify wallet is save if Id not provided
		Wallet returnedWallet = service.createOrUpdate(wallet);

		verify(repository, times(1)).save(isA(Wallet.class));
		Assertions.assertSame(returnedWallet, wallet, "The wallet returned was not the same as the mock");
	}

	@Tag("DEV")
	@DisplayName("Test - delete() - Expected ExpectWalletExceptionThrow - Failure on deleting Wallet Object due to no Id provided")
	@Test
	void testGetById_delete_ExpectWalletExceptionThrow_FailureDeleteWalletWithoutId() {
		// TDD write failed unit test to verify wallet unable to delete
		Assertions.assertThrows(WalletException.class, () -> {
			service.delete(1L);
		});
	}

	@Tag("DEV")
	@DisplayName("Test - delete() - Successful in deleting Wallet Object")
	@Test
	void testGetById_delete_SucessfulWalletDeletion() {
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		doReturn(Optional.of(wallet)).when(repository).findById(1L);

		// TDD write pass unit test to verify wallet able to delete
		service.delete(1L);

		verify(repository, times(1)).delete(isA(Wallet.class));
	}
}

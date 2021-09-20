package com.stock.price.service.impl.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stock.price.exception.ApiRequestException;
import com.stock.price.service.impl.StockPriceServiceImpl;
import com.stock.price.service.requestDTO.StockWatchRequest;
import com.stock.price.serviceresponseDTO.ServiceResponse;
import com.stock.price.serviceresponseDTO.StockWatchResponse;

import yahoofinance.YahooFinance;

@RunWith(SpringJUnit4ClassRunner.class)
public class StockPriceServiceImplTest {

	@InjectMocks
	private StockPriceServiceImpl stockPriceServiceImpl;

	@Mock
	private HashMap<String, List<String>> watchList;

	@Mock
	private YahooFinance yahoofinance;

	@Test
	public void createStockWatchListTest() throws ApiRequestException {
		StockWatchRequest stockWatchRequest = new StockWatchRequest("test watch list");
		ServiceResponse<String> createWatchListResponse = stockPriceServiceImpl.createStockWatchList(stockWatchRequest);
		Assert.assertNotNull(createWatchListResponse);
		Assert.assertEquals(createWatchListResponse.getPayload(), "Watch list created Successfully");
	}

	@Test(expected = ApiRequestException.class)
	public void createStockWatchListTestWithAlreadyCreatedWatchedList() throws ApiRequestException {
		StockWatchRequest stockWatchRequest = new StockWatchRequest("test watch list");
		Mockito.when(watchList.containsKey("test watch list")).thenReturn(Boolean.TRUE);
		ServiceResponse<String> createWatchListResponse = stockPriceServiceImpl.createStockWatchList(stockWatchRequest);
		Assert.assertNotNull(createWatchListResponse);
		Assert.assertEquals(createWatchListResponse.getPayload().toString(),
				"Watchlist already exist with name : test watch list");
	}

	@Test
	public void deleteStockFromWatchListTest() throws ApiRequestException {
		List<String> watchStockList = new ArrayList<String>();
		watchStockList.add("INTC");
		Mockito.when(watchList.get("test watch list")).thenReturn(watchStockList);
		ServiceResponse<String> createWatchListResponse = stockPriceServiceImpl
				.deleteStockFromStockWatchList("test watch list", "INTC");
		Assert.assertNotNull(createWatchListResponse);
		Assert.assertEquals(createWatchListResponse.getPayload().toString(),
				"Stock Removed Succesfully from Stock watch List");
	}

	@Test(expected = ApiRequestException.class)
	public void deleteStockFromWatchListTestWithNotFound() throws ApiRequestException {
		ServiceResponse<String> createWatchListResponse = stockPriceServiceImpl
				.deleteStockFromStockWatchList("test watch list", "INTC");
		Assert.assertNotNull(createWatchListResponse);
		Assert.assertEquals(createWatchListResponse.getPayload().toString(),
				"Stocks are not available with given name : test watch list");
	}

	@Test
	public void getAvgStockPriceTest() throws ApiRequestException, IOException {
		Mockito.when(watchList.containsKey("test watch list")).thenReturn(Boolean.TRUE);
		List<String> watchStockList = new ArrayList<String>();
		watchStockList.add("INTC");
		Mockito.when(watchList.get("test watch list")).thenReturn(watchStockList);
		ServiceResponse<Map<String, BigDecimal>> averagePriceResponse = stockPriceServiceImpl
				.getAvgStockPrice("test watch list");
		Assert.assertNotNull(averagePriceResponse);
		Assert.assertEquals(averagePriceResponse.getPayload().get("averageStockPrice"),
				new BigDecimal(54.26).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void addStockToStockWatchListTest() throws Exception {
		StockWatchRequest stockWatchRequest = new StockWatchRequest("INTC");
		Mockito.when(watchList.containsKey("test watch list")).thenReturn(Boolean.TRUE);
		List<String> watchStockList = new ArrayList<String>();
		Mockito.when(watchList.get("test watch list")).thenReturn(watchStockList);
		ServiceResponse<StockWatchResponse> addStockToWatchList = stockPriceServiceImpl
				.addStockToStockWatchList(stockWatchRequest, "test watch list");
		Assert.assertNotNull(addStockToWatchList);
		Assert.assertEquals(addStockToWatchList.getPayload().getStockName(), "INTC");
	}

	@Test(expected = ApiRequestException.class)
	public void addStockToStockWatchListTestWithWatchListNotFound() throws Exception {
		StockWatchRequest stockWatchRequest = new StockWatchRequest("INTC");
		Mockito.when(watchList.containsKey("test watch list")).thenReturn(Boolean.FALSE);
		List<String> watchStockList = new ArrayList<String>();
		watchStockList.add("INTC");
		Mockito.when(watchList.get("test watch list")).thenReturn(watchStockList);
		ServiceResponse<StockWatchResponse> addStockToWatchList = stockPriceServiceImpl
				.addStockToStockWatchList(stockWatchRequest, "test watch list");
		Assert.assertNotNull(addStockToWatchList);
	}

}

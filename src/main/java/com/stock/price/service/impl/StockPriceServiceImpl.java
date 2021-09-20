package com.stock.price.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stock.price.exception.ApiRequestException;
import com.stock.price.service.StockPriceService;
import com.stock.price.service.requestDTO.StockWatchRequest;
import com.stock.price.serviceresponseDTO.ServiceResponse;
import com.stock.price.serviceresponseDTO.StockWatchResponse;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@Service
public class StockPriceServiceImpl implements StockPriceService {

	private static final String STOCK_NOT_FOUND = "Stocks are not available with given name : ";
	private static final String WATCH_LIST_NOT_FOUND = "Watch List are not available with given name : ";
	private static final String STOCK_ALREADY_ADDED = "Stock is already added to watch list with stock name : ";

	private Map<String, List<String>> watchList = new HashMap<String, List<String>>();

	@Override
	public ServiceResponse<StockWatchResponse> addStockToStockWatchList(StockWatchRequest stockWatchRequest,
			String watchListName) throws Exception {
		ServiceResponse<StockWatchResponse> prodcutWatchResponse = new ServiceResponse();
		Stock stock = getStock(stockWatchRequest.getName());
		getStockWatchList(watchListName);
		List<String> stockWatchedList = watchList.get(watchListName);
		if (!stockWatchedList.contains(stockWatchRequest.getName())) {
			stockWatchedList.add(stockWatchRequest.getName());
		} else {
			throw new ApiRequestException(HttpStatus.CONFLICT, STOCK_ALREADY_ADDED);
		}
		watchList.put(watchListName, stockWatchedList);
		prodcutWatchResponse
				.setPayload(new StockWatchResponse(stockWatchRequest.getName(), stock.getQuote(true).getPrice()));
		return prodcutWatchResponse;
	}

	private void getStockWatchList(String watchListName) throws ApiRequestException {
		if (watchList.get(watchListName) == null) {
			throw new ApiRequestException(HttpStatus.NOT_FOUND, WATCH_LIST_NOT_FOUND);
		}
	}

	private Stock getStock(String stockName) throws IOException, ApiRequestException {
		Stock stock = YahooFinance.get(stockName);
		if (stock == null) {
			throw new ApiRequestException(HttpStatus.NOT_FOUND, STOCK_NOT_FOUND + stockName);
		}
		return stock;
	}

	@Override
	public ServiceResponse<String> deleteStockFromStockWatchList(String watchListName, String stockName)
			throws ApiRequestException {
		// TODO Auto-generated method stub
		ServiceResponse<String> deleteStockResponse = new ServiceResponse();
		getStockWatchList(watchListName);
		List<String> stockWatchedList = watchList.get(watchListName);
		if (!stockWatchedList.isEmpty()) {
			boolean stockRemoved = stockWatchedList.removeIf(stocks -> stockName.equals(stocks));
			if (!stockRemoved) {
				throw new ApiRequestException(HttpStatus.NOT_FOUND, STOCK_NOT_FOUND + stockName);
			}
			deleteStockResponse.setPayload("Stock Removed Succesfully from Stock watch List");
			return deleteStockResponse;
		}
		throw new ApiRequestException(HttpStatus.NOT_FOUND, STOCK_NOT_FOUND + stockName);
	}

	@Override
	public ServiceResponse<Map<String, BigDecimal>> getAvgStockPrice(String watchListName)
			throws IOException, ApiRequestException {
		ServiceResponse<Map<String, BigDecimal>> averageStockPriceResponse = new ServiceResponse<Map<String, BigDecimal>>();
		getStockWatchList(watchListName);
		List<String> stockWatchedList = watchList.get(watchListName);
		BigDecimal totalStockPrice = new BigDecimal(0);
		Map<String, BigDecimal> averageStockPriceMap = new HashMap<String, BigDecimal>();
		if (!stockWatchedList.isEmpty()) {
			Map<String, Stock> stocks = YahooFinance.get(convertStocksToArray(stockWatchedList)); // single request
			for (String stock : stockWatchedList) {
				totalStockPrice = totalStockPrice.add(stocks.get(stock).getQuote(true).getPrice());
			}
			averageStockPriceMap.put("averageStockPrice",
					totalStockPrice.divide(new BigDecimal(stockWatchedList.size())));
		}
		averageStockPriceResponse.setPayload(averageStockPriceMap);
		return averageStockPriceResponse;
	}

	public String[] convertStocksToArray(List<String> stockList) {
		return stockList.stream().toArray(String[]::new);
	}

	@Override
	public ServiceResponse<String> createStockWatchList(StockWatchRequest stockWatchRequest)
			throws ApiRequestException {
		ServiceResponse<String> prodcutWatchResponse = new ServiceResponse();
		if (watchList.containsKey(stockWatchRequest.getName())) {
			throw new ApiRequestException(HttpStatus.CONFLICT,
					"Watchlist already exist with name :" + stockWatchRequest.getName());
		}
		watchList.put(stockWatchRequest.getName(), new ArrayList<String>());
		prodcutWatchResponse.setPayload("Watch list created Successfully");
		return prodcutWatchResponse;
	}

}

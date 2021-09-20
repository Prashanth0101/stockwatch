package com.stock.price.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.stock.price.exception.ApiRequestException;
import com.stock.price.service.requestDTO.StockWatchRequest;
import com.stock.price.serviceresponseDTO.ServiceResponse;
import com.stock.price.serviceresponseDTO.StockWatchResponse;

public interface StockPriceService {

	ServiceResponse<StockWatchResponse> addStockToStockWatchList(StockWatchRequest stockWatchRequest,
			String watchListName) throws Exception;

	ServiceResponse<String> deleteStockFromStockWatchList(String watchListName, String stockName)
			throws ApiRequestException;

	ServiceResponse<Map<String, BigDecimal>> getAvgStockPrice(String watchListName)
			throws IOException, ApiRequestException;

	ServiceResponse<String> createStockWatchList(StockWatchRequest stockWatchRequest) throws ApiRequestException;

}

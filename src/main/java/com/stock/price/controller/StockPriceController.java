package com.stock.price.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stock.price.exception.ApiRequestException;
import com.stock.price.service.StockPriceService;
import com.stock.price.service.requestDTO.StockWatchRequest;
import com.stock.price.serviceresponseDTO.ServiceResponse;
import com.stock.price.serviceresponseDTO.StockWatchResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@RestController
public class StockPriceController {

	@Autowired
	private StockPriceService stockPriceService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Create a Stock Watch List", authorizations = @Authorization(value = "Authorization", scopes = @AuthorizationScope(description = "write", scope = "write")))
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully got the list of Major Achievements for students"),
			@ApiResponse(code = 400, message = "Bad request that might happen because of header mismatch"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "There is some error occurred and not handled") })
	@PostMapping("/watch-list")
	public ResponseEntity createStockWatchList(@RequestBody StockWatchRequest stockWatchRequest) throws Exception {
		ServiceResponse<String> addStockToWatchListResponse = stockPriceService.createStockWatchList(stockWatchRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(addStockToWatchListResponse.getPayload());

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Add Stocks to Stock Watch List", authorizations = @Authorization(value = "Authorization", scopes = @AuthorizationScope(description = "write", scope = "write")))
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully got the list of Major Achievements for students"),
			@ApiResponse(code = 400, message = "Bad request that might happen because of header mismatch"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "There is some error occurred and not handled") })
	@PostMapping("/watch-list/{watchListName}/stock")
	public ResponseEntity addStockToStockWatchList(@RequestBody StockWatchRequest stockWatchRequest,
			@PathVariable String watchListName) throws Exception {
		ServiceResponse<StockWatchResponse> addStockToWatchListResponse = stockPriceService
				.addStockToStockWatchList(stockWatchRequest, watchListName);
		return ResponseEntity.status(HttpStatus.CREATED).body(addStockToWatchListResponse.getPayload());

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Remove Stocks From Stock Watch List", authorizations = @Authorization(value = "Authorization", scopes = @AuthorizationScope(description = "write", scope = "write")))
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully got the list of Major Achievements for students"),
			@ApiResponse(code = 400, message = "Bad request that might happen because of header mismatch"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "There is some error occurred and not handled") })
	@DeleteMapping("/watch-list/{watchListName}/stock")
	public ResponseEntity deleteStockFromStockWatchList(@PathVariable String watchListName,
			@RequestParam(value = "stockName", required = true) String stockName) throws ApiRequestException {
		ServiceResponse<String> deleteStocksResponse = stockPriceService.deleteStockFromStockWatchList(watchListName,
				stockName);
		return ResponseEntity.status(HttpStatus.OK).body(deleteStocksResponse.getPayload());
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Get Average Price for all watched Stock", authorizations = @Authorization(value = "Authorization", scopes = @AuthorizationScope(description = "write", scope = "write")))
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully got the list of Major Achievements for students"),
			@ApiResponse(code = 400, message = "Bad request that might happen because of header mismatch"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "There is some error occurred and not handled") })
	@GetMapping("/watch-list/{watchListName}")
	public ResponseEntity getAverageWatchedStockPrice(@PathVariable String watchListName) throws Exception {
		ServiceResponse<Map<String, BigDecimal>> stockPriceResponse = stockPriceService.getAvgStockPrice(watchListName);
		return ResponseEntity.status(HttpStatus.OK).body(stockPriceResponse.getPayload());
	}

}

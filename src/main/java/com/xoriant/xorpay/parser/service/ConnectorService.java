package com.xoriant.xorpay.parser.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.threeten.bp.format.DateTimeFormatter;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.xero.api.ApiClient;
import com.xero.api.XeroBadRequestException;
import com.xero.api.XeroForbiddenException;
import com.xero.api.XeroMethodNotAllowedException;
import com.xero.api.XeroNotFoundException;
import com.xero.api.XeroRateLimitException;
import com.xero.api.XeroServerErrorException;
import com.xero.api.XeroUnauthorizedException;
import com.xero.api.client.AccountingApi;
import com.xero.api.client.IdentityApi;
import com.xero.models.accounting.Account;
import com.xero.models.accounting.Accounts;
import com.xero.models.accounting.Address;
import com.xero.models.accounting.Contact;
import com.xero.models.accounting.Contacts;
import com.xero.models.accounting.Element;
import com.xero.models.accounting.Invoice;
import com.xero.models.accounting.Invoices;
import com.xero.models.accounting.Payment;
import com.xero.models.accounting.Payments;
import com.xero.models.accounting.ValidationError;
import com.xero.models.assets.FieldValidationErrorsElement;
import com.xero.models.bankfeeds.Statement;
import com.xero.models.identity.Connection;
import com.xoriant.xorpay.xero.APIConstant;
import com.xoriant.xorpay.xero.NotAutherizedException;
import com.xoriant.xorpay.xero.PaymentDetailPojo;
import com.xoriant.xorpay.xero.TokenRefresh;
import com.xoriant.xorpay.xero.TokenStorage;


@Service
public class ConnectorService {
	
	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;
	
	public static DateTimeFormatter formateYYYYMMDDTHHMMSS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss",Locale.ENGLISH);

	final String TOKEN_SERVER_URL = "https://identity.xero.com/connect/token";
	final String AUTHORIZATION_SERVER_URL = "https://login.xero.com/identity/connect/authorize";
	final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	final JsonFactory JSON_FACTORY = new JacksonFactory();
	final String secretState = "secret" + new Random().nextInt(999_999);

	private AccountingApi accountingApi = null;

	public String startAuthorization() throws IOException {
		ArrayList<String> scopeList = new ArrayList<String>();
		scopeList.add("openid");
		scopeList.add("email");
		scopeList.add("profile");
		scopeList.add("offline_access");
		scopeList.add("accounting.settings");
		scopeList.add("accounting.transactions");
		scopeList.add("accounting.contacts");
		scopeList.add("accounting.journals.read");
		scopeList.add("accounting.reports.read");
		scopeList.add("accounting.attachments");
		scopeList.add("projects");
		scopeList.add("assets");
		scopeList.add("payroll.employees");
		scopeList.add("payroll.payruns");
		scopeList.add("payroll.payslip");
		scopeList.add("payroll.timesheets");
		scopeList.add("payroll.settings");
		// scopeList.add("payroll.payrollcalendars");
		// scopeList.add("paymentservices");
		// scopeList.add("payroll");
		System.out.println("flow -  started");
		DataStoreFactory DATA_STORE_FACTORY = new MemoryDataStoreFactory();
		AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
				HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(TOKEN_SERVER_URL),
				new ClientParametersAuthentication(APIConstant.clientId, APIConstant.clientSecret),
				APIConstant.clientId, AUTHORIZATION_SERVER_URL).setScopes(scopeList)
						.setDataStoreFactory(DATA_STORE_FACTORY).build();

		System.out.println("flow - " + flow.getClientId());
		String url = flow.newAuthorizationUrl().setClientId(APIConstant.clientId).setScopes(scopeList)
				.setState(secretState).setRedirectUri(APIConstant.redirectURI).build();
		System.out.println("redirect to  - " + url);
		return url;
		
	}

	public void callBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("callBack");
		String code = "123";
		if (request.getParameter("code") != null) {
			code = request.getParameter("code");
		}

		ArrayList<String> scopeList = new ArrayList<String>();
		scopeList.add("openid");
		scopeList.add("email");
		scopeList.add("profile");
		scopeList.add("offline_access");
		scopeList.add("accounting.settings");
		scopeList.add("accounting.transactions");
		scopeList.add("accounting.contacts");
		scopeList.add("accounting.journals.read");
		scopeList.add("accounting.reports.read");
		scopeList.add("accounting.attachments");
		scopeList.add("projects");
		scopeList.add("assets");
		scopeList.add("payroll.employees");
		scopeList.add("payroll.payruns");
		scopeList.add("payroll.payslip");
		scopeList.add("payroll.timesheets");
		scopeList.add("payroll.settings");
		// scopeList.add("payroll.payrollcalendars");
		// scopeList.add("paymentservices");
		// scopeList.add("payroll");

		DataStoreFactory DATA_STORE_FACTORY = new MemoryDataStoreFactory();
		System.out.println("callBack 1");
		AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
				HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(TOKEN_SERVER_URL),
				new ClientParametersAuthentication(APIConstant.clientId, APIConstant.clientSecret),
				APIConstant.clientId, AUTHORIZATION_SERVER_URL).setScopes(scopeList)
						.setDataStoreFactory(DATA_STORE_FACTORY).build();
		System.out.println("callBack 2");
		TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(APIConstant.redirectURI).execute();

		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(jsonFactory).setClientSecrets(APIConstant.clientId, APIConstant.clientSecret).build();
		credential.setAccessToken(tokenResponse.getAccessToken());
		credential.setRefreshToken(tokenResponse.getRefreshToken());
		credential.setExpiresInSeconds(tokenResponse.getExpiresInSeconds());

		// Create requestFactory with credentials
		HttpTransport transport = new NetHttpTransport();
		HttpRequestFactory requestFactory = transport.createRequestFactory(credential);

		ApiClient defaultIdentityClient = new ApiClient("https://api.xero.com", null, null, null, null);
		IdentityApi idApi = new IdentityApi(defaultIdentityClient);
		List<Connection> connection = idApi.getConnections(tokenResponse.getAccessToken(), null);

		TokenStorage store = new TokenStorage();
		// store.saveItem(response, "jwt_token", tokenResponse.toPrettyString());
		store.saveItem(response, "access_token", tokenResponse.getAccessToken());
		store.saveItem(response, "refresh_token", tokenResponse.getRefreshToken());
		store.saveItem(response, "expires_in_seconds", tokenResponse.getExpiresInSeconds().toString());
		store.saveItem(response, "xero_tenant_id", connection.get(0).getTenantId().toString());
		System.out.println("callBack end");
	}

	public String getHtmlString() {
		return htmlString;
	}

	private String htmlString = "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">"
			+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">"
			+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>"
			+ "<div class=\"container\"><h1>Xero API - JAVA</h1>" + "<div class=\"form-group\">"
			+ "<a href=\"/Authorization\" class=\"btn btn-default\" type=\"button\">Logout</a>" + "</div>"
			+ "<h3><a href=\"Invoices\" >Invoices</a></h3></div>";

	public void fetchPayment(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws IOException, NotAutherizedException {
		//System.out.println(params);

		UUID invoiceId = null;
		UUID paymentID = null;
		UUID accountId = null;
		UUID contactId = null;
		
		Accounts accounts = null;
		Contacts contacts = null;
		Invoices invoices = null;
		

		// Get Tokens and Xero Tenant Id from Storage
		TokenStorage store = new TokenStorage();
		String savedAccessToken = store.get(request, "access_token");
		String savedRefreshToken = store.get(request, "refresh_token");
		String xeroTenantId = store.get(request, "xero_tenant_id");

		// Check expiration of token and refresh if necessary
		// This should be done prior to each API call to ensure your accessToken is
		// valid
		if (savedAccessToken == null) {
			throw new NotAutherizedException();
		}
		String accessToken = new TokenRefresh().checkToken(savedAccessToken, savedRefreshToken, response);
		// Init AccountingApi client
		ApiClient defaultClient = new ApiClient();
		defaultClient.setConnectionTimeout(6000);
		// Get Singleton - instance of accounting client
		accountingApi = AccountingApi.getInstance(defaultClient);

		// INVOICE
		try {
			paymentID = UUID.fromString("11f0276d-4447-4d5f-a085-6086c0d9891a");
			//paymentID = UUID.fromString("daa559ad-2c6f-43db-8e40-0c18e521c8e2");
			
			Payments payments = accountingApi.getPayment(accessToken, xeroTenantId, paymentID);
			// payments.getPayments().stream().forEach(System.out::print);
			for (Payment payment : payments.getPayments()) {
				System.out.println(payment);
			}
			PaymentDetailPojo paymentDetail = new PaymentDetailPojo();
			System.out.println("-------------------------");

			for (Payment payment : payments.getPayments()) {
				
				paymentDetail.setCreated_Date_Time(payment.getDateAsDate().toString());
				paymentDetail.setEnd_to_End_ID(payment.getPaymentID().toString());
				paymentDetail.setMessage_ID(payment.getPaymentID().toString());
				paymentDetail.setPayment_Information_ID(payment.getPaymentID().toString());
				paymentDetail.setNo_Of_Transfers("1");

				accountId = payment.getAccount().getAccountID();
				System.out.println("Account Id" + accountId);
				accounts = accountingApi.getAccount(accessToken, xeroTenantId, accountId);
				accounts.getAccounts().stream().forEach(System.out::print);
				
				System.out.println("-------------------------");
				
				invoiceId = payment.getInvoice().getInvoiceID();
				System.out.println("Invoice Id" + invoiceId);
				invoices = accountingApi.getInvoice(accessToken, xeroTenantId, invoiceId, 1);
				invoices.getInvoices().stream().forEach(System.out::print);
				
				System.out.println("-------------------------");
				
				contactId = payment.getInvoice().getContact().getContactID();
				System.out.println("Contact Id" + contactId);
				contacts = accountingApi.getContact(accessToken, xeroTenantId, contactId);
				contacts.getContacts().stream().forEach(System.out::print);
				
				System.out.println("-------------------------");
			}
			for (Account account : accounts.getAccounts()) {				
				paymentDetail.setRecipient_Bank_Account_Name(account.getName());
				paymentDetail.setRecipient_Bank_Account_No(account.getBankAccountNumber());
				paymentDetail.setTarget_Account_Type(account.getBankAccountType().getValue());
				paymentDetail.setTax_Type(account.getTaxType());
				paymentDetail.setCurrency(account.getCurrencyCode().toString());
			}
			
			for (Contact contact : contacts.getContacts()) {
				paymentDetail.setCompany_Name(contact.getName());
				//paymentDetail.setCompany_Currency(contact.get);
				paymentDetail.setCompany_Email_Address(contact.getEmailAddress());
				paymentDetail.setCompany_VAT_Registration_No(contact.getTaxNumber());
				paymentDetail.setSender_Bank_Account_No(contact.getAccountNumber());
				paymentDetail.setSender_Bank_Account_Name(contact.getFirstName()+" "+contact.getLastName());
				paymentDetail.setBank_Instruction_Details(contact.getBankAccountDetails());
				

				for (Address address : contact.getAddresses()) {	
					paymentDetail.setCompany_Address(address.getAddressLine1());
					paymentDetail.setCompany_City(address.getCity());
					paymentDetail.setCompany_Country(address.getCountry());
					paymentDetail.setCompany_State(address.getRegion());
					paymentDetail.setCompany_Post_Code(address.getPostalCode());
				}
				
			}
			
			for (Invoice invoice : invoices.getInvoices()) {
				paymentDetail.setInvoice_Date(invoice.getDateAsDate().toString());
				paymentDetail.setInvoice_No(invoice.getInvoiceNumber());
				paymentDetail.setDue_Date(invoice.getDueDateAsDate().toString());
				paymentDetail.setInvoice_Amount(invoice.getTotal());
				paymentDetail.setControl_Sum(invoice.getTotal());
				paymentDetail.setDiscount_Amount(invoice.getTotalDiscount());
				paymentDetail.setTax_Amount(invoice.getTotalTax());
				paymentDetail.setPayment_Amount(invoice.getTotal());
				paymentDetail.setTransactional_cury(invoice.getCurrencyCode().getValue());
				paymentDetail.setTransfer_Date(invoice.getFullyPaidOnDateAsDate().toString());
			}
			
			java.sql.Connection conn;

			conn = DriverManager.getConnection(url, username, password);
			DBQueryService dbQueryService = new DBQueryService();

			dbQueryService.InserPaymentDetailsInDatabase(conn, paymentDetail);

			// System.out.println(payments.getPayments().toString());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
	}

	protected void addToMapIfNotNull(Map<String, String> map, String key, Object value) {
		if (value != null) {
			map.put(key, value.toString());
		}
	}

	protected void addError(XeroForbiddenException e, ArrayList<String> messages) {
		if (e.getMessage() != null) {
			messages.add("Xero Exception: " + e.getStatusCode());
			messages.add("Error Msg: " + e.getMessage());

		} else {
			messages.add("Error status: " + e.getStatusCode());
		}
	}

	protected void addError(XeroNotFoundException e, ArrayList<String> messages) {
		if (e.getMessage() != null) {
			messages.add("Xero Exception: " + e.getStatusCode());
			messages.add("Error Msg: " + e.getMessage());

		} else {
			messages.add("Error status: " + e.getStatusCode());
		}
	}

	protected void addError(XeroUnauthorizedException e, ArrayList<String> messages) {
		if (e.getMessage() != null) {
			messages.add("Xero Exception: " + e.getStatusCode());
			messages.add("Error Msg: " + e.getMessage());

		} else {
			messages.add("Error status: " + e.getStatusCode());
		}
	}

	protected void addError(XeroServerErrorException e, ArrayList<String> messages) {
		if (e.getMessage() != null) {
			messages.add("Xero Exception: " + e.getStatusCode());
			messages.add("Error Msg: " + e.getMessage());

		} else {
			messages.add("Error status: " + e.getStatusCode());
		}
	}

	protected void addError(XeroRateLimitException e, ArrayList<String> messages) {
		if (e.getMessage() != null) {
			messages.add("Xero Exception: " + e.getStatusCode());
			messages.add("Error Msg: " + e.getMessage());

		} else {
			messages.add("Error status: " + e.getStatusCode());
		}
	}

	protected void addMethodNotAllowedException(XeroMethodNotAllowedException e, ArrayList<String> messages) {
		if (e.getPayrollUkProblem() != null) {
			messages.add("Xero Exception: " + e.getStatusCode());
			messages.add("Problem title: " + e.getPayrollUkProblem().getTitle());
			messages.add("Problem detail: " + e.getPayrollUkProblem().getDetail());
			if (e.getPayrollUkProblem().getInvalidFields() != null
					&& e.getPayrollUkProblem().getInvalidFields().size() > 0) {
				for (com.xero.models.payrolluk.InvalidField field : e.getPayrollUkProblem().getInvalidFields()) {
					messages.add("Invalid Field name: " + field.getName());
					messages.add("Invalid Field reason: " + field.getReason());
				}
			}

		}
	}

	protected void addBadRequest(XeroBadRequestException e, ArrayList<String> messages) {
		if (e.getElements() != null && e.getElements().size() > 0) {
			messages.add("Xero Exception: " + e.getStatusCode());
			for (Element item : e.getElements()) {
				for (ValidationError err : item.getValidationErrors()) {
					messages.add("Accounting Validation Error Msg: " + err.getMessage());
				}
			}

		} else if (e.getFieldValidationErrorsElements() != null && e.getFieldValidationErrorsElements().size() > 0) {
			messages.add("Xero Exception: " + e.getStatusCode());
			for (FieldValidationErrorsElement ele : e.getFieldValidationErrorsElements()) {
				messages.add("Asset Field Validation Error Msg: " + ele.getDetail());
			}

		} else if (e.getStatementItems() != null && e.getStatementItems().size() > 0) {
			messages.add("Xero Exception: " + e.getStatusCode());
			for (Statement statement : e.getStatementItems()) {
				messages.add("Bank Feed - Statement Msg: " + statement.getFeedConnectionId());
				for (com.xero.models.bankfeeds.Error statementError : statement.getErrors()) {
					messages.add("Bank Feed - Statement Error Msg: " + statementError.getDetail());
				}
			}

		} else if (e.getEmployeeItems() != null && e.getEmployeeItems().size() > 0) {
			messages.add("Xero Exception: " + e.getStatusCode());
			for (com.xero.models.payrollau.Employee emp : e.getEmployeeItems()) {
				for (com.xero.models.payrollau.ValidationError err : emp.getValidationErrors()) {
					messages.add("Payroll AU Employee Validation Error Msg: " + err.getMessage());
				}
			}

		} else if (e.getPayrollCalendarItems() != null && e.getPayrollCalendarItems().size() > 0) {
			messages.add("Xero Exception: " + e.getStatusCode());
			for (com.xero.models.payrollau.PayrollCalendar item : e.getPayrollCalendarItems()) {
				for (com.xero.models.payrollau.ValidationError err : item.getValidationErrors()) {
					messages.add("Payroll AU Payroll Calendar Validation Error Msg: " + err.getMessage());
				}
			}

		} else if (e.getPayRunItems() != null && e.getPayRunItems().size() > 0) {
			messages.add("Xero Exception: " + e.getStatusCode());
			for (com.xero.models.payrollau.PayRun item : e.getPayRunItems()) {
				for (com.xero.models.payrollau.ValidationError err : item.getValidationErrors()) {
					messages.add("Payroll AU Payroll Calendar Validation Error Msg: " + err.getMessage());
				}
			}

		} else if (e.getSuperFundItems() != null && e.getSuperFundItems().size() > 0) {
			messages.add("Xero Exception: " + e.getStatusCode());
			for (com.xero.models.payrollau.SuperFund item : e.getSuperFundItems()) {
				for (com.xero.models.payrollau.ValidationError err : item.getValidationErrors()) {
					messages.add("Payroll AU SuperFund Validation Error Msg: " + err.getMessage());
				}
			}

		} else if (e.getTimesheetItems() != null && e.getTimesheetItems().size() > 0) {
			messages.add("Xero Exception: " + e.getStatusCode());
			for (com.xero.models.payrollau.Timesheet item : e.getTimesheetItems()) {
				for (com.xero.models.payrollau.ValidationError err : item.getValidationErrors()) {
					messages.add("Payroll AU Timesheet Validation Error Msg: " + err.getMessage());
				}
			}

		} else if (e.getPayrollUkProblem() != null
				&& ((e.getPayrollUkProblem().getDetail() != null && e.getPayrollUkProblem().getTitle() != null)
						|| (e.getPayrollUkProblem().getInvalidFields() != null
								&& e.getPayrollUkProblem().getInvalidFields().size() > 0))) {
			messages.add("Xero Exception: " + e.getStatusCode());
			messages.add("Problem title: " + e.getPayrollUkProblem().getTitle());
			messages.add("Problem detail: " + e.getPayrollUkProblem().getDetail());
			if (e.getPayrollUkProblem().getInvalidFields() != null
					&& e.getPayrollUkProblem().getInvalidFields().size() > 0) {
				for (com.xero.models.payrolluk.InvalidField field : e.getPayrollUkProblem().getInvalidFields()) {
					messages.add("Invalid Field name: " + field.getName());
					messages.add("Invalid Field reason: " + field.getReason());
				}
			}

		} else {
			messages.add("Error Msg: " + e.getMessage());
		}
	}

	protected String saveFile(ByteArrayInputStream input, String fileName) {
		String saveFilePath = null;
		File f = new File("./");
		String dirPath;
		try {
			dirPath = f.getCanonicalPath();

			FileOutputStream output = new FileOutputStream(fileName);

			int DEFAULT_BUFFER_SIZE = 1024;
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int n = 0;
			n = input.read(buffer, 0, DEFAULT_BUFFER_SIZE);
			while (n >= 0) {
				output.write(buffer, 0, n);
				n = input.read(buffer, 0, DEFAULT_BUFFER_SIZE);
			}
			input.close();
			output.close();

			saveFilePath = dirPath + File.separator + fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return saveFilePath;
	}

	public static int loadRandomNum() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100000);
		return randomInt;
	}

	public static int findRandomNum(int total) {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(total);
		return randomInt;
	}

	public static String loadRandChar() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}

}

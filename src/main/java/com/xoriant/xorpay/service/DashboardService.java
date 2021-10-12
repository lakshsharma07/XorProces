package com.xoriant.xorpay.service;

import java.text.ParseException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.constants.XorThreadConstant;
import com.xoriant.xorpay.entity.BatchDetailStatusEntity;
import com.xoriant.xorpay.entity.BatchPaymentStatusMonthEntity;
import com.xoriant.xorpay.entity.XMLTagErrorEntity;
import com.xoriant.xorpay.model.LineChartDetails;
import com.xoriant.xorpay.model.MetaData;
import com.xoriant.xorpay.model.Statistics;
import com.xoriant.xorpay.model.StatisticsDetails;
import com.xoriant.xorpay.repo.BatchDetailsStatusRepo;
import com.xoriant.xorpay.repo.BatchPaymentStatusDayRepo;
import com.xoriant.xorpay.repo.BatchPaymentStatusMonthRepo;
import com.xoriant.xorpay.repo.DashboardRepo;
import com.xoriant.xorpay.repo.SourceSysRepo;
import com.xoriant.xorpay.repo.XMLTagErrorRepo;

@Service
public class DashboardService {

	@Autowired
	private BatchDetailsStatusRepo batchDetailsStatusRepo;

	@Autowired
	private DashboardRepo dashboardRepo;

	@Autowired
	private XMLTagErrorRepo xmlTagErrorRepo;

	@Autowired
	private SourceSysRepo sourceRepo;

	@Autowired
	private BatchPaymentStatusMonthRepo batchPaymentStatusMonthRepo;

	@Autowired
	private BatchPaymentStatusDayRepo batchPaymentStatusDayRepo;

	@Autowired
	private ServiceUtil serviceUtil;

	private static boolean dashboardFlag = true;

	private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

	private int totali = 0, onsi = 0, mbci = 0, ebsi = 0;
	private int totalip = 0, onsip = 0, mbcip = 0, ebsip = 0;
	private int totalt = 0, onst = 0, mbct = 0, ebst = 0;
	private int totalp = 0, onsp = 0, mbcp = 0, ebsp = 0;
	private int totals = 0, onss = 0, mbcs = 0, ebss = 0;
	private int totalf = 0, onsf = 0, mbcf = 0, ebsf = 0;
	private int totalak = 0, onsak = 0, mbcak = 0, ebsak = 0;
	private int totalac = 0, onsac = 0, mbcac = 0, ebsac = 0;
	private int totalrj = 0, onsrj = 0, mbcrj = 0, ebsrj = 0;
	private String source;
	private boolean isONS, isMBC, isEBS;
	private List<StatisticsDetails> statisticsList;

	public List<Statistics> getStatistics() {
		List<Statistics> statisticsList = new ArrayList<>();

		chekForDashboardStatics();

		Statistics stat0 = new Statistics();
		if (isONS) {
			stat0.setStatName(XorConstant.NET_SUITE);
		} else if (isMBC) {
			stat0.setStatName(XorConstant.MS_DYNAMICS);
		} else if (isEBS) {
			stat0.setStatName(XorConstant.ORACLE_EBS);
		}
		statisticsList.add(stat0);

		Statistics stat1 = new Statistics();
		stat1.setStatName(XorConstant.INITIATED);
		stat1.setCount(totali);
		statisticsList.add(stat1);

		Statistics stat2 = new Statistics();
		stat2.setStatName(XorConstant.IN_PROGRESS);
		stat2.setCount(totalip);
		statisticsList.add(stat2);

		Statistics stat3 = new Statistics();
		stat3.setStatName(XorConstant.TRANSLATED);
		stat3.setCount(totalt);
		statisticsList.add(stat3);

		/*
		 * Statistics stat4 = new Statistics(); stat4.setStatName(XorConstant.PENDING);
		 * stat4.setPercentage(totalp); statisticsList.add(stat4);
		 */

		Statistics stat5 = new Statistics();
		stat5.setStatName(XorConstant.ACKNOWLEDGED);
		stat5.setCount(totalak);
		statisticsList.add(stat5);

		Statistics stat6 = new Statistics();
		stat6.setStatName(XorConstant.ACCEPTED);
		stat6.setCount(totalac);
		statisticsList.add(stat6);

		Statistics stat7 = new Statistics();
		stat7.setStatName(XorConstant.REJECTED);
		stat7.setCount(totalrj);
		statisticsList.add(stat7);

		return statisticsList;
	}

	private void chekForDashboardStatics() {

		if (dashboardFlag) {
			Runnable task = () -> getDasgboardStaticDetails();

			int initialDelay = 0;
			int period = 15;
			XorThreadConstant.dashBoardexecutor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
			dashboardFlag = false;
		}
	}

	private void getDasgboardStaticDetails() {
/*
		List<DashboardEntity> dentityList = dashboardRepo.findBySourceSysId(ConfigConstants.SOURCE_SYSTEM_ID).stream()
				.filter(e -> e.getRecordCount() > 0).collect(Collectors.toList());

		List<Integer> sourceIdList = dentityList.stream().map(e -> e.getSourceSysId()).collect(Collectors.toList());

		SourceSysEntity sourceEntity = sourceRepo.findById(ConfigConstants.SOURCE_SYSTEM_ID).get();

		Map<Integer, SourceSysEntity> SourceSysMap = new HashMap<>();

		// sourceList.stream().forEach(e -> {
		SourceSysMap.put(sourceEntity.getId(), sourceEntity);
		// });

		totali = dentityList.stream().filter(e -> e.getDataStage().equalsIgnoreCase(XorConstant.INITIATED))
				.mapToInt(entity -> entity.getRecordCount()).sum();
		totalip = dentityList.stream().filter(e -> e.getDataStage().equalsIgnoreCase(XorConstant.IN_PROGRESS))
				.mapToInt(entity -> entity.getRecordCount()).sum();
		totalt = dentityList.stream().filter(e -> e.getDataStage().equalsIgnoreCase(XorConstant.TRANSLATED))
				.mapToInt(entity -> entity.getRecordCount()).sum();

		totalak = dentityList.stream().filter(e -> e.getDataStage().equalsIgnoreCase(XorConstant.ACKNOWLEDGED))
				.mapToInt(entity -> entity.getRecordCount()).sum();
		totalac = dentityList.stream().filter(e -> e.getDataStage().equalsIgnoreCase(XorConstant.ACCEPTED))
				.mapToInt(entity -> entity.getRecordCount()).sum();
		totalrj = dentityList.stream().filter(e -> e.getDataStage().equalsIgnoreCase(XorConstant.REJECTED))
				.mapToInt(entity -> entity.getRecordCount()).sum();

		sourceIdList.stream().forEach(s -> {
			if (SourceSysMap.containsKey(s)) {
				source = SourceSysMap.get(s).getSrcSystemSort();

				if (source.equalsIgnoreCase(XorConstant.ONS)) {

					onsi = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.INITIATED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					onsip = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.IN_PROGRESS) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					onst = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.TRANSLATED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					onsak = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.ACKNOWLEDGED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					onsac = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.ACCEPTED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					onsrj = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.REJECTED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					isONS = true;
				} else {
					isONS = false;
				}
				if (source.equalsIgnoreCase(XorConstant.MBC)) {
					mbci = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.INITIATED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					mbcip = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.IN_PROGRESS) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					mbct = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.TRANSLATED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					mbcak = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.ACKNOWLEDGED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					mbcac = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.ACCEPTED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					mbcrj = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.REJECTED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();

					isMBC = true;
				} else {
					isMBC = false;

				}
				if (source.equalsIgnoreCase(XorConstant.EBS)) {
					ebsi = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.INITIATED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					ebsip = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.IN_PROGRESS) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					ebst = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.TRANSLATED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					ebsak = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.ACKNOWLEDGED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					ebsac = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.ACCEPTED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					ebsrj = dentityList.stream().filter(
							e -> e.getDataStage().equalsIgnoreCase(XorConstant.REJECTED) && e.getSourceSysId() == s)
							.mapToInt(entity -> entity.getRecordCount()).sum();
					isEBS = true;
				} else {
					isEBS = false;
				}
			}
		});
*/
	}

	public List<StatisticsDetails> getStatisticsDetails() {

		statisticsList = new ArrayList<>();
		if (isONS) {
			StatisticsDetails stat1 = new StatisticsDetails();
			stat1.setStatName(XorConstant.NET_SUITE);
			stat1.setSource(XorConstant.ONS);
			stat1.setInitiatedCount(onsi);
			stat1.setInProgressCount(onsip);
			stat1.setTransmitted(onst);
			// stat1.setPendingCount(onsp);
			stat1.setAcknoledgedCount(onsak);
			stat1.setAccepetedCount(onsac);
			stat1.setRejectedCount(onsrj);
			statisticsList.add(stat1);
		}
		if (isMBC) {
			StatisticsDetails stat2 = new StatisticsDetails();
			stat2.setStatName(XorConstant.MS_DYNAMICS);
			stat2.setSource(XorConstant.MBC);
			stat2.setInitiatedCount(mbci);
			stat2.setInProgressCount(mbcip);
			stat2.setTransmitted(mbct);
			// stat2.setPendingCount(mbcp);
			stat2.setAcknoledgedCount(mbcak);
			stat2.setAccepetedCount(mbcac);
			stat2.setRejectedCount(mbcrj);
			statisticsList.add(stat2);
		}
		if (isEBS) {
			StatisticsDetails stat3 = new StatisticsDetails();
			stat3.setStatName(XorConstant.ORACLE_EBS);
			stat3.setSource(XorConstant.EBS);
			stat3.setInitiatedCount(ebsi);
			stat3.setInProgressCount(ebsip);
			stat3.setTransmitted(ebst);
			// stat3.setPendingCount(ebsp);
			stat3.setAcknoledgedCount(ebsak);
			stat3.setAccepetedCount(ebsac);
			stat3.setRejectedCount(ebsrj);
			statisticsList.add(stat3);
		}
		return statisticsList;

	}

	public List<BatchDetailStatusEntity> getProjectDetails() {

		return batchDetailsStatusRepo.findTop10ByOrderByIdDesc();
	}

	public LineChartDetails getLineChartDetails(String month) throws ParseException {
		LineChartDetails lineChartDetailsObj = new LineChartDetails();
		if ((month == null || month.isEmpty()) || (month != null && month.equals("" + Year.now().getValue()))) {
			getMonthData(lineChartDetailsObj);
		} else if (!month.equalsIgnoreCase("undefined")) {
			getDailyData(month, lineChartDetailsObj);
		}
		return lineChartDetailsObj;
	}

	private void getDailyData(String month, LineChartDetails lineChartDetailsObj) {
		/*String[] monthYear = month.split("\\s+");

		DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM").withLocale(Locale.ENGLISH);
		TemporalAccessor accessor = parser.parse(monthYear[0]);
		int monthNumber = accessor.get(ChronoField.MONTH_OF_YEAR);

		int digits = Integer.toString(monthNumber).trim().length();
		String monthYearLikeString = null;

		if (digits < 2) {
			monthYearLikeString = monthYear[1].concat("0").concat(Integer.toString(monthNumber));
		} else {
			monthYearLikeString = monthYear[1].concat(Integer.toString(monthNumber));
		}
		List<BatchPaymentStatusDayEntity> dayWisePayments = batchPaymentStatusDayRepo
				.findBySourceSysIdAndYearMonthDayStartsWithOrderByYearMonthDayAsc(ConfigConstants.SOURCE_SYSTEM_ID,
						monthYearLikeString);

		List<Integer> dataList = new ArrayList<>();
		if (dayWisePayments.size() == 1) {
			dataList.add(0);
		}
		dataList.addAll(dayWisePayments.stream().map(entity -> entity.getPayment()).collect(Collectors.toList()));

		LineChartData linechartDataObj = new LineChartData();
		linechartDataObj.setData(dataList);
		linechartDataObj.setLabel(XorConstant.PAYMENTS);
		List<String> durationList = new ArrayList<>(13);
		if (dayWisePayments.size() == 1) {
			durationList.add("0");
		}

		durationList.addAll(
				dayWisePayments.stream().map(entity -> entity.getPaymentDay().toString()).collect(Collectors.toList()));
		lineChartDetailsObj.setLineChartData(linechartDataObj);
		lineChartDetailsObj.setLabels(durationList);
	*/}

	private void getMonthData(LineChartDetails lineChartDetailsObj) {
		/*List<String> monthYearLst = new ArrayList<>(12);
		LocalDate ld = LocalDate.now();
		int year = Year.now().getValue();

		ld = LocalDate.of(year, 12, 30);
		monthYearLst.add(ld.getYear() + "-" + ld.getMonthValue());
		for (int i = 0; i < 11; i++) {
			ld = ld.minusMonths(1);
			monthYearLst.add(ld.getYear() + "-" + ld.getMonthValue());
		}
		List<BatchPaymentStatusMonthEntity> payBatchList = batchPaymentStatusMonthRepo
				.findBySourceSysIdAndMonthYearIn(ConfigConstants.SOURCE_SYSTEM_ID, monthYearLst);

		List<Integer> dataList = new ArrayList<>(13);
		if (payBatchList.size() == 1) {
			dataList.add(0);
		}
		dataList.addAll(payBatchList.stream().map(entity -> entity.getPayment()).collect(Collectors.toList()));

		LineChartData linechartDataObj = new LineChartData();
		linechartDataObj.setData(dataList);
		linechartDataObj.setLabel(XorConstant.PAYMENTS);
		List<String> durationList = new ArrayList<>(13);
		if (payBatchList.size() == 1) {
			durationList.add("0");
		}
		durationList.addAll(payBatchList.stream()
				.map(entity -> Month.of(entity.getMonth()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-"
						+ entity.getYear().toString().substring(2))
				.collect(Collectors.toList()));

		lineChartDetailsObj.setLineChartData(linechartDataObj);
		lineChartDetailsObj.setLabels(durationList);
	*/}

	public Map<String, List<Object>> getPieDetails() {
		Map<String, List<Object>> pieDetails = new HashMap<>();
		List<Object> dataList = new ArrayList<>();
		List<Object> labelList = new ArrayList<>();

		if (totalt > 0) {
			dataList.add(((totalt - (totalac + totalrj)) * 100 / totalt));
			// dataList.add((totali * 100 / totalt));
			// dataList.add((totalip / totali) * 100);
			// dataList.add((totalt * 100/ totalt) );
			// dataList.add((totalp / totali) * 100);
			// dataList.add((totalak * 100 / totalt));
			dataList.add((totalac * 100 / totalt));
			dataList.add((totalrj * 100 / totalt));

			// labelList.add(XorConstant.INITIATED);
			// labelList.add(XorConstant.IN_PROGRESS);
			// labelList.add(XorConstant.TRANSMITTED);
			// labelList.add(XorConstant.SUCCESS);
			// labelList.add(XorConstant.FAILED);
			labelList.add(XorConstant.PENDING);
			// labelList.add(XorConstant.ACKNOWLEDGED);
			labelList.add(XorConstant.ACCEPTED);
			labelList.add(XorConstant.REJECTED);

		}
		if (totali == 0 && totalip == 0 && totalt == 0) {
			dataList.add(" ");
			labelList.add(XorConstant.NO_DATA);
		}

		pieDetails.put("Data", dataList);
		pieDetails.put("Label", labelList);
		List<Object> metaDataList = new ArrayList<>();

		MetaData mData1 = new MetaData();
		mData1.setStatName("Mobile");
		mData1.setPercentage("3");
		metaDataList.add(mData1);

		MetaData mData2 = new MetaData();
		mData2.setStatName("Tablet");
		mData2.setPercentage("20");
		metaDataList.add(mData2);

		MetaData mData3 = new MetaData();
		mData3.setStatName("Desktop");
		mData3.setPercentage("75");
		metaDataList.add(mData3);

		MetaData mData4 = new MetaData();
		mData4.setStatName("Others");
		mData4.setPercentage("3");
		metaDataList.add(mData4);

		pieDetails.put("MetaData", metaDataList);

		return pieDetails;
	}

	public List<BatchPaymentStatusMonthEntity> getLineChartMonthYear() {
		return null;// batchPaymentStatusMonthRepo.findTop6BySourceSysIdOrderByYearMnthDesc(ConfigConstants.SOURCE_SYSTEM_ID);
	}

	public XMLTagErrorEntity findErrorById(Integer id) {
		return xmlTagErrorRepo.findById(id).get();
	}

	public List<XMLTagErrorEntity> findErrorsBySource(Integer source, Integer pageNo, Integer pageSize) {

		Pageable paging = serviceUtil.getPagination(pageNo, pageSize);
		return xmlTagErrorRepo.findBySourceSystem(source, paging);
	}

}

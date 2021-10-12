/*
 * package com.xoriant.xorpay;
 * 
 * import java.util.HashMap; import java.util.Map;
 * 
 * import org.apache.logging.log4j.LogManager; import
 * org.apache.logging.log4j.Logger; import
 * org.springframework.beans.factory.annotation.Autowired;
 * 
 * import com.xoriant.xorpay.constants.XorConstant; import
 * com.xoriant.xorpay.data.sync.services.XorProcessService;
 * 
 * import io.cloudio.task.JavaTask;
 * 
 * public class XorpayService<K, V> extends JavaTask<K, V> {
 * 
 * private static Logger logger = LogManager.getLogger(XorpayService.class);
 * 
 * @Autowired private XorProcessService xps;
 * 
 * @Autowired public XorpayService(String taskCode) throws Exception {
 * super(taskCode);
 * 
 * }
 * 
 * public Map<String, Object> executeTask(Map<String, Object> inputParams,
 * Map<String, Object> outputParams, Map<String, Object> inputState) throws
 * Exception { try { logger.info("Starting data process task"); String processId
 * = getWfInstUid();// UUID.randomUUID().toString(); // String workflowId=
 * getWfUid(); xps.startProcess(processId);
 * logger.info("Completed data process task ");
 * 
 * Map<String, Object> outputState = new HashMap<String, Object>();
 * outputState.put("_status", "Success");
 * 
 * outputState.put(XorConstant.PROCESS_ID, processId); return outputState; }
 * catch (Throwable e) { setRunning(false); throw e; }
 * 
 * }
 * 
 * public String getWfInstUid() { return taskRequest.getWfInstUid(); }
 * 
 * public String getWfUid() { return taskRequest.getWfUid(); }
 * 
 * }
 */
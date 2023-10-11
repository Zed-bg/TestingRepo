import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nirvasoft.rp.shared.bulkpaymentdatareq;
import com.nirvasoft.rp.shared.icbs.SMSReturnData;
import com.nirvasoft.rp.util.GeneralUtility;

public class Test {

	public static void main(String[] args) {
//		String date1="2022-09-16";
//		Date d1 = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar cal = Calendar.getInstance();
//		int tenure=1;
//		int datatenure = 1001;
//		int mday =1;
//		try {
//			d1 = sdf.parse(date1);		
//			int day = d1.getDate();
//			int month = d1.getMonth();
//			int year = d1.getYear(); 			
//			int diffday = 0;
//			Date l_newDate = d1;
//			if (datatenure<=100){	//months and years
//				//System.out.println("l_newDate:" + day + "/" + month + "/" + year);
//				int resultMonthCount = year * 12 + month + datatenure;	
//				//System.out.println("resultMonthCount:" + resultMonthCount);
//				int resultYear = resultMonthCount / 12;		     
//				int resultMonth = resultMonthCount - resultYear * 12;	
//				//System.out.println("resultYear:" + resultYear);			  
//				if(resultYear%4==0){
//				  l_newDate.setYear(resultYear);	
//				  l_newDate.setMonth(resultMonth);	
//				  if(day>29 && resultMonth==1)
//					  diffday =  day-30;
//				  //System.out.println("diffday:" + diffday);
//				}else {
//				  l_newDate.setMonth(resultMonth); 		    	  
//				  l_newDate.setYear(resultYear);	
//				  if(day>28 && resultMonth==1)
//					  diffday =  day-29;
//				  //System.out.println("diffday:" + diffday);
//				}		
//				cal.setTime(l_newDate);
//				System.out.println(sdf.format(l_newDate));	
//				cal.add(Calendar.DATE, -diffday-mday); //minus number would decrement the days
//				System.out.println(sdf.format(cal.getTime()));
//			}else if (datatenure>100 && datatenure<=1000){ //days
//				cal.setTime(l_newDate);
//				System.out.println(sdf.format(l_newDate));	
//				diffday = tenure;
//				cal.add(Calendar.DATE, diffday-mday); //minus number would decrement the days
//				System.out.println(sdf.format(cal.getTime()));
//			}else{	//weeks
//				cal.setTime(l_newDate);
//				System.out.println(sdf.format(l_newDate));	
//				diffday = tenure*7;
//				cal.add(Calendar.DATE, diffday-mday); //minus number would decrement the days
//				System.out.println(sdf.format(cal.getTime()));
//			}
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		String url = "MPSS_TOPUP_URL:https://api.myanmarpaymenttopup.com/campaign/jsonaction/";
//		System.out.println(  url.split("MPSS_TOPUP_URL:")[1]);
		
//		ObjectMapper map = new ObjectMapper();
//		SMSReturnData data = new SMSReturnData();
//		try {
//			System.out.println(map.writeValueAsString(data));
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ArrayList<String> l_ArlLogString = new ArrayList<String>();
//		l_ArlLogString.add("Charge2 Amount--" + "0");
//		l_ArlLogString.add("Charge2 GL--" +"gl1");
//		l_ArlLogString.add("Charge1 Amount--" + "00");
//		l_ArlLogString.add("Charge1 GL--" + "GL2");
//		System.out.println(l_ArlLogString.get(0));
//		System.out.println(GeneralUtility.generateSyskey());
//		System.out.println(GeneralUtility.getTodayDate());
//		System.out.println(Long.parseLong("6a5fc7f7"));
//		System.out.println(Long.parseLong("6a5f"));
//		bulkpaymentdatareq data = new bulkpaymentdatareq();
//		data.setAmount(500);
//		data.setToaccnumber("0200101000186266");
//		data.setDescription("row 1");
//		data.setRowno("1");
//		
//		bulkpaymentdatareq[] datalist = new bulkpaymentdatareq[1] ;
//		datalist[0] = data;	
//        System.out.println(Arrays.toString(datalist));
		String res = ";ynwin@gmial.com;";
		String [] email = res.split(";");
		if(email.length>0)
		System.out.println(email[0]);
		else
			System.out.println("length 0");
	}
}

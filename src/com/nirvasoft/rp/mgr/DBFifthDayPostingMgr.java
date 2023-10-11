package com.nirvasoft.rp.mgr;

import com.nirvasoft.rp.shared.POCommissionRateData;

public class DBFifthDayPostingMgr {
	public static double getCommissionRate(double pAmount, POCommissionRateData pComRateData) {
	    double l_CommAmt = 0.0D;
	    String l_comtype = pComRateData.getComType();
	    if (!l_comtype.equals("")) {
	      switch (l_comtype.charAt(0)) {
	        case 'F':
	          l_CommAmt = pComRateData.getCom();
	          break;
	        case 'R':
	          l_CommAmt = pComRateData.getCom() * pAmount;
	          break;
	        case 'P':
	          l_CommAmt = pComRateData.getCom() / 100.0D * pAmount;
	          break;
	      } 
	      if (pComRateData.getComMin() > l_CommAmt) {
	        l_CommAmt = pComRateData.getComMin();
	      } else if (pComRateData.getComMax() != 0.0D && pComRateData.getComMax() < l_CommAmt) {
	        l_CommAmt = pComRateData.getComMax();
	      } 
	    } 
	    return l_CommAmt;
	  }
}

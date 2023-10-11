package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

import com.nirvasoft.rp.shared.icbs.Enum.DateFilterOperator;
import com.nirvasoft.rp.shared.icbs.Enum.NumFilterOperator;
import com.nirvasoft.rp.shared.icbs.Enum.TxTFilterOperator;

public class CurrentIBTInCriteriaData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5232722781659878592L;
	public String pFDate = "";
	public String pTDate = "";
	public String pFBranch = "";
	public String pTBranch = "";
	public String pCrAccNumber = "";
	public String pDrAccNumber = "";
	public double pFAmount = 0;
	public double pTAmount = 0;
	public String IBTType = "";
	public String TransType = "";
	public String TableName = "";
	public String Status = "";
	public long AutoKey = 0;
	public String OnlineSrNo = "";
	public String BYear = "";

	public Enum.DateFilterOperator pFDateOperator = DateFilterOperator.GreaterThanEqual;
	public Enum.DateFilterOperator pTDateOperator = DateFilterOperator.LessThanEqual;
	public Enum.TxTFilterOperator pFBranchOperator = TxTFilterOperator.Equal;
	public Enum.TxTFilterOperator pTBranchOperator = TxTFilterOperator.Equal;
	public Enum.TxTFilterOperator pCrAccNumberOperator = TxTFilterOperator.Contain;
	public Enum.TxTFilterOperator pDrAccNumberOperator = TxTFilterOperator.Contain;
	public Enum.NumFilterOperator pFAmtOperator = NumFilterOperator.GreaterThanEqual;
	public Enum.NumFilterOperator pTAmtOperator = NumFilterOperator.LessThanEqual;
	public Enum.NumFilterOperator statusOperator = NumFilterOperator.Equal;
	public Enum.TxTFilterOperator AutoKeyOperator = TxTFilterOperator.Equal;
	public Enum.NumFilterOperator pOnlineSrNoOperator = NumFilterOperator.Equal;
	public Enum.TxTFilterOperator pBYearOperator = TxTFilterOperator.Equal;

}

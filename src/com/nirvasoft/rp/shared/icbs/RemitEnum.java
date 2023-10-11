package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class RemitEnum implements Serializable {

	// Type in RateSetup
	public class CommissionType {
		public static final int RemitCommType = 1;
		public static final int IBTComType = 3;
	}

	public class IBTStatus {
		public static final int cNew = 0;
		public static final int cSave = 1;
		public static final int cSend = 2;
		public static final int cPost = 3;
		public static final int cPostHome = 4;
		public static final int cPostTransact = 4;
		public static final int cPass = 5;
		public static final int cCancel = 6;
		public static final int cEdit = 9;
	}

	public class IBTTransType {
		public static final int CashWithdraw = 0;
		public static final int TransWithdraw = 1;
		public static final int CashDeposit = 2;
		public static final int TransDeposit = 3;
		public static final int POCashWithdraw = 4;
		public static final int POTrWithdraw = 5;
		public static final int GCCashWithdraw = 6;
		public static final int GCTrWithdraw = 7;
		public static final int BETransWithdraw = 8;
		public static final int PbFdCashDeposit = 12;
		public static final int PbFdTransferDeposit = 13;
	}

	public class IBTType {
		public static final int Withdraw = 0;
		public static final int Deposit = 1;
		public static final int CashDeposit_S = 2;
		public static final int CashWithdraw_S = 3;
		public static final int TRDeposit_S = 4;
		public static final int TRWithdraw_S = 5;
		public static final int CashDeposit_D = 6;
		public static final int CashWithdraw_D = 7;
		public static final int TRDeposit_D = 8;
		public static final int TRWithdraw_D = 9;
	}

	public class KindOfTr {
		public static final int RemitAmount = 1;
		public static final int Com1 = 2;
		public static final int Com2 = 3;
		public static final int Com3 = 4;
		public static final int Com4 = 5;
		public static final int Com5 = 6;
		public static final int Com6 = 7;
	}

	public class MenuType {
		public static final int Window = 1;
		public static final int Web = 2;
	}

	public class OnlineMsgType {
		public static final int Sendsuccess = 255;
		public static final int SendFail = 99;
		public static final int CannotWithdrawWithinWeek = 252;
		public static final int InsuificentBalance = 250;
		public static final int CheckChqueStatus = 256;
		public static final int CheckAccountStatus = 258;
		public static final int IsValidAccountStatus = 259;
		public static final int IsAlreadyPosted = 300;
		public static final int LinkAccountAlreadyMadeTransaction = 400;
		public static final int SerialNoAlreadyExist = 222;
		public static final int InitialDeposit = 221;
		public static final int UpdateMaxPONoFail = 225;
		public static final int NotMatchCardAcc = 777;
	}

	public class ProcessID {
		public static final int EntryProcessEntry = 11;
		public static final int EntryProcessExcel = 12;
		public static final int EntryProcessCSV = 13;
		public static final int OnlineProcessXml = 14;
		public static final int SendProcessOnline = 21;
		public static final int SendProcessCD = 22;
		public static final int SendProcessOther = 23;
	}

	public class RMTStatus {
		public static final int cRemitNew = 0;
		public static final int cRemitSave = 1;
		public static final int cReviseSave = 2;
		public static final int cRemitPost = 2;
		public static final int cRemitSend = 4;
		public static final int cReviseIn = 4;
		public static final int cRevisePost = 5;
		public static final int cCancel = 6;
		public static final int Suspend = 3;
	}

	public class RMTTransType {
		public static final int cNrcToNrc = 0;
		public static final int cNrcToAc = 1;
		public static final int cAcToAc = 2;
		public static final int cAcToNrc = 3;
		public static final int cDomesToNrc = 4;
		public static final int cDomesToAc = 5;
	}

	private static final long serialVersionUID = -5903601462399691418L;
}

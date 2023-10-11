
package com.nirvasoft.rp.startup;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.nirvasoft.rp.core.ccbs.LogicMgr;
import com.nirvasoft.rp.core.ccbs.data.db.DBFECCurrencyRateMgr;
import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.framework.ConnAdmin;
import com.nirvasoft.rp.framework.ServerSession;
import com.nirvasoft.rp.mgr.DBSystemMgr;
import com.nirvasoft.rp.util.GeneralUtil;
import com.nirvasoft.rp.util.GeneralUtility;
import com.nirvasoft.rp.util.ServerGlobal;
import com.nirvasoft.rp.shared.SharedLogic;

public class WLStartupServlet implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {		
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {		
		getPath(arg0);
		//ConnAdmin.readConnection();
		//ConnAdmin.readConfig();
		//GeneralUtil.readCCDAPIServiceSetting();		
		try {
			GeneralUtil.readBrCodeLength();
			ConnAdmin.readConnConfig();
			ConnAdmin.readEnvConfig("envName1", "");
			ConnAdmin.readEnvConfig("envName2", "");
			
			System.out.println("ENV Path1: "+ServerGlobal.getmEnvPath1());
			System.out.println("ENV Path2: "+ServerGlobal.getmEnvPath2());
			GeneralUtil.readDebugLogStatus();
			
			SharedLogic.setSystemData(DBSystemMgr.readSystemData());
			com.nirvasoft.rp.shared.SharedLogic.setSystemData(SharedLogic.getSystemData());
			com.nirvasoft.rp.core.SingletonServer.setSystemData(SharedLogic.getSystemData());
			SharedLogic.getSystemData().setpFECCurrencyRateList(DBSystemMgr.getFECCurrencyRateList());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void getPath(ServletContextEvent contextPath) {		
		ServerSession.serverPath = contextPath.getServletContext().getRealPath("/") + "/";
		DAOManager.AbsolutePath = contextPath.getServletContext().getRealPath("/") + "/";
		com.nirvasoft.rp.core.SingletonServer.setAbsPath(contextPath.getServletContext().getRealPath("/") + "/WEB-INF/");
	}
	
	
	
	public void addMInBalSetting() {
		
	}
	
}

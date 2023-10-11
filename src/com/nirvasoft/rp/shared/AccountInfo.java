/**
 * AccountInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.nirvasoft.rp.shared;

public class AccountInfo  implements java.io.Serializable {
    private String accnumber;

    private String cardacc;

    private String curcode;

    private double availablebalance;
    
    private double currentbalance;

    private String acctype;

    private String accdesc;

    private String accnrc;

    private String customerid;

    private String accname;

    private String accdob;

    private double minbal;
    
    private String accstatus;
    private String openingdate;
    private String brname;
    private String brcode;
    private String productname;
    private String Producttype;
    private String cifid;

    public String getAccnumber() {
		return accnumber;
	}

	public String getCardacc() {
		return cardacc;
	}

	public String getCurcode() {
		return curcode;
	}

	public double getAvailablebalance() {
		return availablebalance;
	}

	public String getAcctype() {
		return acctype;
	}

	public String getAccdesc() {
		return accdesc;
	}

	public String getAccnrc() {
		return accnrc;
	}

	public String getCustomerid() {
		return customerid;
	}

	public String getAccname() {
		return accname;
	}

	public String getAccdob() {
		return accdob;
	}

	public double getMinbal() {
		return minbal;
	}

	public String getOpeningdate() {
		return openingdate;
	}

	public String getBrname() {
		return brname;
	}

	public String getProductname() {
		return productname;
	}

	public String getProducttype() {
		return Producttype;
	}

	public java.lang.Object get__equalsCalc() {
		return __equalsCalc;
	}

	public boolean is__hashCodeCalc() {
		return __hashCodeCalc;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public void setCardacc(String cardacc) {
		this.cardacc = cardacc;
	}

	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}

	public void setAvailablebalance(double availablebalance) {
		this.availablebalance = availablebalance;
	}

	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}

	public void setAccdesc(String accdesc) {
		this.accdesc = accdesc;
	}

	public void setAccnrc(String accnrc) {
		this.accnrc = accnrc;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public void setAccdob(String accdob) {
		this.accdob = accdob;
	}

	public void setMinbal(double minbal) {
		this.minbal = minbal;
	}

	public void setOpeningdate(String openingdate) {
		this.openingdate = openingdate;
	}

	public void setBrname(String brname) {
		this.brname = brname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public void setProducttype(String producttype) {
		Producttype = producttype;
	}

	public void set__equalsCalc(java.lang.Object __equalsCalc) {
		this.__equalsCalc = __equalsCalc;
	}

	public void set__hashCodeCalc(boolean __hashCodeCalc) {
		this.__hashCodeCalc = __hashCodeCalc;
	}

	public static void setTypeDesc(org.apache.axis.description.TypeDesc typeDesc) {
		AccountInfo.typeDesc = typeDesc;
	}

	public String getAccstatus() {
		return accstatus;
	}

	public void setAccstatus(String accstatus) {
		this.accstatus = accstatus;
	}

	public String getCifid() {
		return cifid;
	}

	public void setCifid(String cifid) {
		this.cifid = cifid;
	}

	public AccountInfo() {
    }

    public AccountInfo(
           String accnumber,
           String cardacc,
           String curcode,
           double availablebalance,
           double currentbalance,
           String acctype,
           String accdesc,
           String accnrc,
           String customerid,
           String accname,
           String accdob,
           String accstatus,
           String openingdate,
           String brname,
           String productname,
           String Producttype,
           String cifid,
           String brcode,
           double minbal) {
           this.accnumber = accnumber;
           this.cardacc = cardacc;
           this.curcode = curcode;
           this.availablebalance = availablebalance;
           this.currentbalance = currentbalance;
           this.acctype = acctype;
           this.accdesc = accdesc;
           this.accnrc = accnrc;
           this.customerid = customerid;
           this.accname = accname;
           this.accdob = accdob;
           this.minbal = minbal;
           this.accstatus = accstatus;
           this.openingdate = openingdate;
           this.brname = brname;
           this.productname = productname;
           this.Producttype = Producttype;
           this.cifid = cifid;
           this.brcode = brcode;
    }

  
    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccountInfo)) return false;
        AccountInfo other = (AccountInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accnumber==null && other.getAccnumber()==null) || 
             (this.accnumber!=null &&
              this.accnumber.equals(other.getAccnumber()))) &&
            ((this.cardacc==null && other.getCardacc()==null) || 
             (this.cardacc!=null &&
              this.cardacc.equals(other.getCardacc()))) &&
            ((this.curcode==null && other.getCurcode()==null) || 
             (this.curcode!=null &&
              this.curcode.equals(other.getCurcode()))) &&
            this.availablebalance == other.getAvailablebalance() &&
            ((this.acctype==null && other.getAcctype()==null) || 
             (this.acctype!=null &&
              this.acctype.equals(other.getAcctype()))) &&
            ((this.accdesc==null && other.getAccdesc()==null) || 
             (this.accdesc!=null &&
              this.accdesc.equals(other.getAccdesc()))) &&
            ((this.accnrc==null && other.getAccnrc()==null) || 
             (this.accnrc!=null &&
              this.accnrc.equals(other.getAccnrc()))) &&
            ((this.customerid==null && other.getCustomerid()==null) || 
             (this.customerid!=null &&
              this.customerid.equals(other.getCustomerid()))) &&
            ((this.accname==null && other.getAccname()==null) || 
             (this.accname!=null &&
              this.accname.equals(other.getAccname()))) &&
            ((this.accdob==null && other.getAccdob()==null) || 
             (this.accdob!=null &&
              this.accdob.equals(other.getAccdob()))) &&
            this.minbal == other.getMinbal();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAccnumber() != null) {
            _hashCode += getAccnumber().hashCode();
        }
        if (getCardacc() != null) {
            _hashCode += getCardacc().hashCode();
        }
        if (getCurcode() != null) {
            _hashCode += getCurcode().hashCode();
        }
        _hashCode += new Double(getAvailablebalance()).hashCode();
        if (getAcctype() != null) {
            _hashCode += getAcctype().hashCode();
        }
        if (getAccdesc() != null) {
            _hashCode += getAccdesc().hashCode();
        }
        if (getAccnrc() != null) {
            _hashCode += getAccnrc().hashCode();
        }
        if (getCustomerid() != null) {
            _hashCode += getCustomerid().hashCode();
        }
        if (getAccname() != null) {
            _hashCode += getAccname().hashCode();
        }
        if (getAccdob() != null) {
            _hashCode += getAccdob().hashCode();
        }
        _hashCode += new Double(getMinbal()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AccountInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.epixws.nirvasoft.com/", "accountInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositAcc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DepositAcc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardAcc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CardAcc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CCY");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CCY"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avlBal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AvlBal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AccType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accDesc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AccDesc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accNRC");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AccNRC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accCustomerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AccCustomerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AccName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accDOB");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AccDOB"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minBal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MinBal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

	public double getCurrentbalance() {
		return currentbalance;
	}

	public void setCurrentbalance(double currentbalance) {
		this.currentbalance = currentbalance;
	}

	public String getBrcode() {
		return brcode;
	}

	public void setBrcode(String brcode) {
		this.brcode = brcode;
	}

}

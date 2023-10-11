/**
 * CustInfoResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.nirvasoft.rp.shared;

public class CustInfoResult  implements java.io.Serializable {
    private java.lang.String customerId;

    private java.lang.String code;

    private java.lang.String desc;

    private double haveAmountTotal;

    private double oweAmountTotal;

    private double principalAmount;

    private double settledAmount;

    private AccountInfo[] accountInfoArr;

    public CustInfoResult() {
    }

    public CustInfoResult(
           java.lang.String customerId,
           java.lang.String code,
           java.lang.String desc,
           double haveAmountTotal,
           double oweAmountTotal,
           double principalAmount,
           double settledAmount,
           AccountInfo[] accountInfoArr) {
	           this.customerId = customerId;
	           this.code = code;
	           this.desc = desc;
	           this.haveAmountTotal = haveAmountTotal;
	           this.oweAmountTotal = oweAmountTotal;
	           this.principalAmount = principalAmount;
	           this.settledAmount = settledAmount;
	           this.accountInfoArr = accountInfoArr;
    		}


    /**
     * Gets the customerId value for this CustInfoResult.
     * 
     * @return customerId
     */
    public java.lang.String getCustomerId() {
        return customerId;
    }


    /**
     * Sets the customerId value for this CustInfoResult.
     * 
     * @param customerId
     */
    public void setCustomerId(java.lang.String customerId) {
        this.customerId = customerId;
    }


    /**
     * Gets the code value for this CustInfoResult.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this CustInfoResult.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the desc value for this CustInfoResult.
     * 
     * @return desc
     */
    public java.lang.String getDesc() {
        return desc;
    }


    /**
     * Sets the desc value for this CustInfoResult.
     * 
     * @param desc
     */
    public void setDesc(java.lang.String desc) {
        this.desc = desc;
    }


    /**
     * Gets the haveAmountTotal value for this CustInfoResult.
     * 
     * @return haveAmountTotal
     */
    public double getHaveAmountTotal() {
        return haveAmountTotal;
    }


    /**
     * Sets the haveAmountTotal value for this CustInfoResult.
     * 
     * @param haveAmountTotal
     */
    public void setHaveAmountTotal(double haveAmountTotal) {
        this.haveAmountTotal = haveAmountTotal;
    }


    /**
     * Gets the oweAmountTotal value for this CustInfoResult.
     * 
     * @return oweAmountTotal
     */
    public double getOweAmountTotal() {
        return oweAmountTotal;
    }


    /**
     * Sets the oweAmountTotal value for this CustInfoResult.
     * 
     * @param oweAmountTotal
     */
    public void setOweAmountTotal(double oweAmountTotal) {
        this.oweAmountTotal = oweAmountTotal;
    }


    /**
     * Gets the principalAmount value for this CustInfoResult.
     * 
     * @return principalAmount
     */
    public double getPrincipalAmount() {
        return principalAmount;
    }


    /**
     * Sets the principalAmount value for this CustInfoResult.
     * 
     * @param principalAmount
     */
    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }


    /**
     * Gets the settledAmount value for this CustInfoResult.
     * 
     * @return settledAmount
     */
    public double getSettledAmount() {
        return settledAmount;
    }


    /**
     * Sets the settledAmount value for this CustInfoResult.
     * 
     * @param settledAmount
     */
    public void setSettledAmount(double settledAmount) {
        this.settledAmount = settledAmount;
    }


    /**
     * Gets the accountInfoArr value for this CustInfoResult.
     * 
     * @return accountInfoArr
     */
    public AccountInfo[] getAccountInfoArr() {
        return accountInfoArr;
    }


    /**
     * Sets the accountInfoArr value for this CustInfoResult.
     * 
     * @param accountInfoArr
     */
    public void setAccountInfoArr(AccountInfo[] accountInfoArr) {
        this.accountInfoArr = accountInfoArr;
    }

    public AccountInfo getAccountInfoArr(int i) {
        return this.accountInfoArr[i];
    }

    public void setAccountInfoArr(int i, AccountInfo _value) {
        this.accountInfoArr[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CustInfoResult)) return false;
        CustInfoResult other = (CustInfoResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.customerId==null && other.getCustomerId()==null) || 
             (this.customerId!=null &&
              this.customerId.equals(other.getCustomerId()))) &&
            ((this.code==null && other.getCode()==null) || 
             (this.code!=null &&
              this.code.equals(other.getCode()))) &&
            ((this.desc==null && other.getDesc()==null) || 
             (this.desc!=null &&
              this.desc.equals(other.getDesc()))) &&
            this.haveAmountTotal == other.getHaveAmountTotal() &&
            this.oweAmountTotal == other.getOweAmountTotal() &&
            this.principalAmount == other.getPrincipalAmount() &&
            this.settledAmount == other.getSettledAmount() &&
            ((this.accountInfoArr==null && other.getAccountInfoArr()==null) || 
             (this.accountInfoArr!=null &&
              java.util.Arrays.equals(this.accountInfoArr, other.getAccountInfoArr())));
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
        if (getCustomerId() != null) {
            _hashCode += getCustomerId().hashCode();
        }
        if (getCode() != null) {
            _hashCode += getCode().hashCode();
        }
        if (getDesc() != null) {
            _hashCode += getDesc().hashCode();
        }
        _hashCode += new Double(getHaveAmountTotal()).hashCode();
        _hashCode += new Double(getOweAmountTotal()).hashCode();
        _hashCode += new Double(getPrincipalAmount()).hashCode();
        _hashCode += new Double(getSettledAmount()).hashCode();
        if (getAccountInfoArr() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAccountInfoArr());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAccountInfoArr(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CustInfoResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.epixws.nirvasoft.com/", "custInfoResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customerId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CustomerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Desc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("haveAmountTotal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HaveAmountTotal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oweAmountTotal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OweAmountTotal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("principalAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PrincipalAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("settledAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SettledAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountInfoArr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AccountInfoArr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservice.epixws.nirvasoft.com/", "accountInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
           java.lang.String mechType, 
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
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
